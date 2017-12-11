package paintings;

import components.FillTriangle;
import components.Geometry;
import components.Material;
import Data_Structures.Structures.UBA;
import components.g_line;
import components.Painting;
import components.Path_Visual_Factory;
import components.photonColor;
import geometry.Curve;
import geometry.Point;
import geometry.Polyline;
import geometry.Ray;
import math.Direction_math;
import structures.QueryList;
import structures.RayQuery;

/*
 * A Scene for testing my 2D ray Tracer.
 */


public class painting_nypl extends Painting
{	
	
	final int reference_N = 1000*1000;
	double NFactor;
	
	public painting_nypl(int width, int height)
	{
		super(width, height);
	}

	@Override
	protected void makeImage()
	{
		
		int w = getWidth();
		int h = getHeight();
		int N = w*h;
		
		NFactor = N*1.0 / reference_N;
		System.out.println(NFactor);

		//constructGeometry();

		//shootRandomPhotons();
		
		//scaleCache(.3);
		
		illuminatePaths();
		
		//scaleCache(3.0);
		//scaleFinalCache(3.0);
	}
	
	
	private void illuminatePaths()
	{
	
				
		// Branching pattern of curves.
		UBA<QueryList<Geometry>> frontier = new UBA<QueryList<Geometry>>();
		
		// Store a running list, so that we do not intersect, but instead make a tree of paths.
		QueryList<Geometry> all_geoms  = new QueryList<Geometry>(null);
		QueryList<Geometry> all_geoms2 = new QueryList<Geometry>(null);
		
		all_geoms.append(screen_bounds);
		frontier.enq(screen_bounds);
	
		// Expand the paths N times.
		int N = 5;
		for(int i = 0; i < N; i++)
		{
			 System.out.println("Paths are " + i*1.0/N +" % done");
			 illuminatePath(frontier, all_geoms, all_geoms2); // Also links up line links.
			 scaleCache(3.0);
			 transferFinishedWork();
		}
		
		//clearCache();
		
		/*
		 * Illuminate the faces. 
		 */
		for(QueryList<Geometry> qList: frontier)
		{
			g_line line = (g_line) qList.getRandomElement();
			Polyline face = this.getFace(line);
			//face = face.shrink(.5);
			illuminatePolyline(face, new photonColor(255, 76, 115));//new photonColor(250, 255, 163)
			transferFinishedWork();
			//scaleFinalCache(3);
		}	
		
		// Add offset lights.
		this.setGeometry(all_geoms2);
		
		int size = frontier.size();
		int iter = 0;
		
		/*
		for(QueryList<Geometry> geomList : frontier)
		{
			
			System.out.println(iter*1.0/size +" % done with frontier lights");
			illuminateOffsetLight(geomList);
			iter++;
		}
		*/
		
	}
	
	private void illuminatePolyline(Polyline face, photonColor photonColor)
	{
		UBA<Geometry> geoms = polyLineToGeometryList(face, false, getPathMaterial());
		QueryList<Geometry> qList = new QueryList<Geometry>(geoms);
		this.setGeometry(qList);
		
		int N = (int) (25000*NFactor);
		for(int i = 0; i < N; i++)
		{
			if(i % 10000 == 0)
			{
				System.out.println(i*1.0/N +" % done");
			}
			
			Geometry geom = qList.getRandomElement();
			g_line line = (g_line)geom;
			 
			Point pt = line.getRandomPoint();
			Point dir_original = line.getRightNormal();
			
			// -- We are going to shoot a ray to find the endpoint for this curve.
			Point start = pt.add(dir_original.normalize());
		
			
			Point location  = start;
			Point direction = dir_original;
			
			trace(location, direction, photonColor, 0, 1.0);
		}
		
	}

	void illuminatePath(UBA<QueryList<Geometry>> frontier,
			QueryList<Geometry> all_geoms,
			QueryList<Geometry> all_geoms2)
	{
		 QueryList<Geometry> list = frontier.deq();
		 Geometry geom = list.getRandomElement();
		 g_line line = (g_line)geom;
		 
		 Point pt = line.getMidPoint();
		 Point dir = line.getRightNormal().multScalar(-1);
		 
		 // -- We are going to shoot a ray to find the endpoint for this curve.
		 Point start = pt.add(dir);
		 Ray ray = new Ray(pt, dir);
		 RayQuery rayQuery = new RayQuery(ray);
		 Geometry geom2 = all_geoms.rayquery_min(rayQuery);
		 geom2.computeDistance(pt, dir);
		 
		 Point end = geom2.computeIntersectionPoint();
		 
		 Curve curve = Path_Visual_Factory.newCurve();
		 curve.addPoint(start);

		 addInbetweenPoint(curve, start, end, 3);
		 
		 curve.addPoint(end);
		 
		 
		 
		 UBA<Geometry> left_lines    = new UBA<Geometry>();
		 UBA<Geometry> right_lines   = new UBA<Geometry>();
		 photonColor color = photonColor.WHITE;
		 double width = getPathWidth();
		 illuminatePath(curve, width, color, color, getPathMaterial(), left_lines, right_lines);
		 
		 // -- Link up light lines, so we can extract faces in the next pass.
		 linkLines(line, left_lines, (g_line)geom2, right_lines);
		 
		 
		 width -= 1.0;
		 
		 all_geoms.append(left_lines);
		 all_geoms.append(right_lines);
		 
		 all_geoms2.append(left_lines);
		 all_geoms2.append(right_lines);
		 
		 frontier.enq(new QueryList<Geometry>(left_lines));
		 frontier.enq(new QueryList<Geometry>(right_lines));
	}
	
	private void linkLines(g_line line_first, UBA<Geometry> forwards_lines, g_line line_last, UBA<Geometry> back_lines)
	{
		// Perform the linking.
		((g_line)forwards_lines.getLast()).next = line_last.next;
		((g_line)back_lines.getLast()).next = line_first.next;
		
		line_first.next = (g_line) forwards_lines.getFirst();		
		line_last.next  = (g_line) back_lines.getFirst();
		
		for(int i = 0; i < forwards_lines.size() - 1; i++)
		{
			g_line line1 = (g_line) forwards_lines.get(i);
			g_line line2 = (g_line) forwards_lines.get(i + 1);
			line1.next = line2;
		}
		
		for(int i = 0; i < back_lines.size() - 1; i++)
		{
			g_line line1 = (g_line) back_lines.get(i);
			g_line line2 = (g_line) back_lines.get(i + 1);
			line1.next = line2;
		}
	}

	void illuminateOffsetLight(QueryList<Geometry> geomList)
	{
		Geometry geom = geomList.getRandomElement();
		g_line line = (g_line)geom;
		 
		Point pt = line.getMidPoint();
		Point dir_original = line.getRightNormal().multScalar(-1);
		 
		double o_width = getPathWidth();
		
		// -- We are going to shoot a ray to find the endpoint for this curve.
		Point start = pt.add(dir_original.multScalar(o_width*5));
		
		int N = (int) (25000*NFactor);
		for(int i = 0; i < N; i++)
		{
			Point dir = Direction_math.random_hemi(dir_original);
			
			Point location  = start;
			Point direction = dir;
			// yellow.
			photonColor color = new photonColor(250, 255, 163);// new photonColor(0xFAFFA3);
			
			trace(location, direction, color, 3, 1.0);
		}
	}
	
	// Canonical reference path width.
	private double getPathWidth()
	{
		return 50*NFactor;
	}
	
	private Material getPathMaterial()
	{
		// Defined colors and materials.
		//photonColor end_color   = photonColor.WHITE;//new photonColor(255, 255, 255);
		
		// Conservation of energy dictates that these value cannot add to more than 255 in any channel.
		photonColor specular     = photonColor.BLACK;//new photonColor(55, 55, 55);
		photonColor diffuse      = photonColor.WHITE;//new photonColor(255, 255, 255);
		photonColor transmission = photonColor.BLACK;//new photonColor(255, 255, 255);
		
		// 255 - the sum is the coefficient of absorption.
		
		
		Material mat = new Material(2.0, 0.0, diffuse, specular, transmission);
		return mat;
	}
	
	void addInbetweenPoint(Curve curve, Point start, Point end, int recursions_left)
	{
		if(recursions_left <= 0)
		{
			return;
		}
		
		double p = .32;
		
		Point dir = end.sub(start);
		Point offset = dir.multScalar(p);
		
		Point perp = dir.perp();
		
		Point midPoint = start.add(offset).add(perp.multScalar(p*p));
		
		curve.addPoint(midPoint);
		addInbetweenPoint(curve, midPoint, end, recursions_left - 1);
		return;
	}
	
	private void illuminatePath(Curve curve, double width, photonColor start_color, photonColor end_color, Material mat,
			UBA<Geometry> left_lines, UBA<Geometry> right_lines)
	{
		// Build the interior and boundaries.
		int prescision = 10;	
		Path_Visual_Factory pathFactory = new Path_Visual_Factory();
		
		UBA<FillTriangle> triangles = new UBA<FillTriangle>();
		
		pathFactory.getVisual(prescision, curve, width, start_color, end_color, mat, triangles, left_lines, right_lines);
		
		UBA<Geometry> curve_geom = new UBA<Geometry>();
		curve_geom.append(left_lines);
		curve_geom.append(right_lines);
		
		QueryList<Geometry> boundaries = new QueryList<Geometry>(curve_geom);
		this.setGeometry(boundaries);
		
		int N = (int)(10000*NFactor);
		
		// Shoot all photons.
		for(int i = 0; i < N; i++)
		{			
			int triIndex = (int)(Math.random()*triangles.size());
			FillTriangle tri = triangles.get(triIndex);
			
			Point location  = tri.getRandomPoint();
		    Point direction = tri.interpolateTangentAtPt(location);

		    //direction = direction.normalize();
		    
		    // 0 - 1.
		    //photonColor color  = tri.interpolatePhotonColorAtPt(location);
		    
		    double offset = tri.interpolateOffsetAtPt(location);
		    photonColor color;
		    if(offset < 0)
		    {
		    	color = new photonColor(255, 177, 109);// Orange.
		    }
		    else
		    {
		    	color = new photonColor(96, 218, 255);// Blue.
		    }
		    
		    
		    int recursion = 2;
		    double refractive_index = 1.0;
		 
		    	     
		    trace(location, direction, color, recursion, refractive_index);
		}
	
		return;
	}
	
	private void constructGeometry()
	{
		int w = getWidth();
		int h = getHeight();
		
		UBA<Geometry> geoemtries = new UBA<Geometry>();
		 
		photonColor black = photonColor.BLACK;
		photonColor white = photonColor.WHITE;
				
		// Conservation of energy dictates that these value cannot add to more than 255 in any channel.
		photonColor specular     = black;//new photonColor(55, 55, 55);
		photonColor diffuse      = white;//new photonColor(255, 255, 255);
		photonColor transmission = black;//new photonColor(200, 200, 200);//black;//new photonColor(255, 255, 255);
		
		// 255 - the sum is the coefficient of absorption.
		
		
		Material mat = new Material(2.0, 0.0, diffuse, specular, transmission);
		/*
		g_Circle circle1 = new g_Circle(mat, new Point(500, 500), 490);
		geometries[0] = circle1;
		g_Circle circle2 = new g_Circle(mat, new Point(300, 500), 100);
		geometries[1] = circle2;
		g_Circle circle3 = new g_Circle(mat, new Point(500, 400), 7);
		geometries[2] = circle3;
		*/
		
		g_line line1;
		
		Point p1 = new Point(.01, h/2);
		Point p2 = new Point(w - .01, h/2);
		
		line1 = new g_line(mat, p1, p2);
		geoemtries.push(line1);

		QueryList<Geometry> queryable = new QueryList<Geometry>(geoemtries);
		this.setGeometry(queryable);
	}
	
	/*
	UBA<Geometry> createPath(Curve curve, double w, Material mat)
	{
		
	}*/
	
	private void shootRandomPhotons()
	{
		int w = getWidth();
		int h = getHeight();
		
		int N = w*h;
		N = 100;
		
		for(int i = 0; i < N; i++)
		{
			shootRandomPhoton();
			int gran = 10000;
			if(i % gran == 0)
			{
				System.out.println(i/gran + " out of " + N/gran);

			}
		}
	}
	
	private void shootRandomPhoton()
	{
		double x, y, dx, dy;
	
		double border = 10;
		
		int w = getWidth();
		int h = getHeight();
		
		x = border + Math.random()*(w - border*2);
		y = border + Math.random()*(h - border*2);
		
		double angle = Math.random()*Math.PI*2;
		
		dx = Math.cos(angle);
		dy = Math.sin(angle);
		
		Point location = new Point(x, y);
		Point dir = new Point(dx, dy);
		
		photonColor color = photonColor.WHITE;
		int recursion = 1;
		double refractive_indice = 1.0;
		
		trace(location, dir, color, recursion, refractive_indice);
	}
}