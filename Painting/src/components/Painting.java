package components;


import java.awt.Color;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Rendering.ColorCalculator;
import Data_Structures.Structures.UBA;
import components.Material.Bounce_Type;
import geometry.Point;
import geometry.Polyline;
import geometry.Ray;
import math.Direction_math;
import structures.QueryList;
import structures.RayQuery;


/*
 * Provides rendering functionality to the subclassed image generators.
 * 
 * Features:
 * 
 * Rendering and rasterization:
 *  - Maintains a final irradiance cache that will specify the final image.
 *  - Maintains a working irradiance cache that can be used to generate each portion of the image.
 *  - Grafts the working irradiance cache onto the final cache.
 *
 *  - Given a set of 2D geometries adds a customizable portion of the photon's path to the working irradiance cache.
 *  
 * Geometry and Light Generation.
 *  - Provides capabilities for generating geometry and light.
 *  - Helpful rendering functions for a given light and geometry.
 *  
 *  What does this class need to know? What are the photons, how far do they go, and what are the geometric constraints?
 */

public abstract class Painting extends ColorCalculator
{
	final static double epsilon = .01;
	
	// Bounds for the viewing window.
	int x1, x2, y1, y2;
	
	// Used when beams to not hit any geometry.
	protected QueryList<Geometry> screen_bounds;
	private   QueryList<Geometry> scene_geometry;

	// Caches the current work.
	private IrradianceCache irradiance_work;
	
	// Caches the composite work that constitutes the final image.
	private IrradianceCache irradiance_final;
	
	// Generates the Irradiance Caches.
	public Painting(int width, int height)
	{
		super(width, height);
		
		irradiance_work  = new IrradianceCache(width, height);
		irradiance_final = new IrradianceCache(width, height);
		
		// To ensure the stability of the photon tracing routines,
		// Screen bound geometry is generated within the renderer.
		initialize_screen_bounds();
		
		// Default empty geometry array.
		UBA<Geometry> array = new UBA<Geometry>();
		scene_geometry = new QueryList<Geometry>(array);
		
		// Sub class makes the image by first performing work on the irradiance cache and
		// then compositing it onto the final cache. 
		makeImage();
	}

	// Must set state, such as geometry.
	protected abstract void makeImage();
	
	private void initialize_screen_bounds()
	{
		UBA<Geometry> screen_bounds_array = new UBA<Geometry>(4);
		
		Material mat_absorb = new Material(1.0, 1.0, photonColor.BLACK, photonColor.BLACK);
		
		int w = getWidth();
		int h = getHeight();
		Point v1 = new Point(1,1);
		Point v2 = new Point(w - 2, 1);
		Point v3 = new Point(w - 2, h - 2);
		Point v4 = new Point(1, h - 2);
		
		screen_bounds_array.push(new g_line(mat_absorb, v1, v2));
		screen_bounds_array.push(new g_line(mat_absorb, v2, v3));
		screen_bounds_array.push(new g_line(mat_absorb, v3, v4));
		screen_bounds_array.push(new g_line(mat_absorb, v4, v1));
		
		// Associate next pointers.
		int size = screen_bounds_array.size();
		for(int i = 0; i < size; i++)
		{
			g_line l1 = (g_line) screen_bounds_array.get(i);
			g_line l2 = (g_line) screen_bounds_array.get((i + 1) % size);
			l1.next = l2;
		}
		
		screen_bounds = new QueryList<Geometry>(screen_bounds_array);
	}

	@Override
	public Color getColor(double x, double y)
	{
		return irradiance_final.getColor(x, y);
		//return irradiance_work.getColor(x, y);
	}
	
	// Scales the working cache.
	public void scaleCache(double amount)
	{
		irradiance_work.scale_exposure(amount);
	}
	
	public void scaleFinalCache(double amount)
	{
		irradiance_final.scale_exposure(amount);
	}
	
	// -- Photon shooting functions.
	
	protected void setGeometry(QueryList<Geometry> geometry)
	{
		scene_geometry = geometry;
	}
	
	// Trace the paths of photons against the given geometries, with the given recursion limit.
	protected void shootPhoton(Light light)
	{
		// The three properties of this light.
		Point origin    = light.getLocation();
		Point direction = light.getDirection();
		
		photonColor photon = light.diffuse(0);
		
		// Should this be offset by 1?
		int recursion_limit = light.getBounceLimit();		
		
		trace(origin, direction, photon, recursion_limit, Geometry.INDICE_VACUUM);

	}

	// Traces a photon.
	protected void trace(Point src,
						 Point direction,
						 photonColor photon,
						 int recursion_left,
						 double refractive_indice)
	{	
		
		Ray ray = new Ray(src, direction);
		RayQuery rayQuery = new RayQuery(ray);
		Geometry geom = scene_geometry.rayquery_min(rayQuery);
		if(geom == null)
		{
			geom = screen_bounds.rayquery_min(rayQuery);
		}
		
		// Going off into space, no collision.
		if(geom == null)
		{				
			return;
			//throw new Error("Not possible, because we surround the scene with impermeable walls.");
		}
		
		geom.computeDistance(ray.getPoint(), ray.getDirection());
		Point dest = geom.computeIntersectionPoint();
		
		// If we get to a wacky ray that is out of bounds, we terminate.
		if(dest.x < 0 || dest.x >= irradiance_work.getWidth() ||
		   dest.y < 0 || dest.y >= irradiance_work.getHeight())
		{
			return;
		}
		
		if(src.x < 0 || src.x >= irradiance_work.getWidth() ||
		   src.y < 0 || src.y >= irradiance_work.getHeight())
		{
			return;
		}
		
		// Add the light.
		double attenuation = add_light_line(src.x, src.y, dest.x, dest.y, photon);
	
		// FIXME : Implement reflectance, transmittance / refraction.
		
		// 100 % absorption right now.
		
		Material mat = geom.material;
		
		// Base Case.
		if(recursion_left < 1)
		{
			return;
		}		
		
		// Recursive photon Bouncing.
		
		// Diffuse.
		Point normal = geom.getNormal();
		
		// Make sure the normal is not the wrong direction.
		if(normal.dot(direction) < 0)
		{
			normal = normal.multScalar(-1);
		}
			
		
		Bounce_Type val = mat.getWeightedBounceEvent();
		
		switch(val)
		{
		case DIFFUSE:
			Point diffuse_bounce_dir = Direction_math.random_hemi_2D(normal);
			photonColor photon_new = photon.mult(attenuation).mult(mat.diffuse);
			trace(dest.add(diffuse_bounce_dir.multScalar(epsilon)), diffuse_bounce_dir, photon_new, recursion_left - 1, refractive_indice);
			break;
		case SPECULAR:
			photon_new = photon.mult(attenuation).mult(mat.specular);
			Point specular_bounce_dir = geom.getReflectionDirection();
			trace(dest.add(normal.multScalar(epsilon)), specular_bounce_dir, photon_new, recursion_left - 1, refractive_indice);
			break;
			
		case TRANSMISSION:
			Point refracted_bounce_dir = geom.refract(refractive_indice);
			
			// Total Internal Reflection.
			if(refracted_bounce_dir == null)
			{
				return;
			}
			
			photon_new = photon.mult(attenuation).mult(mat.transmission);
			double bounce_refraction_indice = geom.getNextRefractiveIndex(refractive_indice);
			trace(dest.add(refracted_bounce_dir.multScalar(epsilon)),
					       refracted_bounce_dir,
					       photon_new,
					       recursion_left - 1,
					       bounce_refraction_indice);
			break;
		}
		

		return;
		
	}
	
	// -- Draw antialiased line of width 1.
	// By draw It means that we are adding irradiance.
	// Returns the attenuation that has transpired along the beam.	
	private double add_light_line(double x1, double y1, double x2, double y2, photonColor photon_start)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		boolean vertical = Math.abs(dy) > Math.abs(dx);
	
		boolean inverted = false;
		
		// Make sure the main coordinate is increasing.
		if((vertical && dy < 0) ||
		   (!vertical && dx < 0))
		{
			double temp_x = x1;
			x1 = x2;
			x2 = temp_x;
			
			double temp_y = y1;
			y1 = y2;
			y2 = temp_y;
			
			inverted = true;
		}
	
		if(vertical)
		{
			return light_line_helper(y1, y2, x1, x2, true, inverted, photon_start);
		}
		else
		{
			return light_line_helper(x1, x2, y1, y2, false, inverted, photon_start);
		}
	}
	
	// Helps to reconfigure the coordinates.
	// Returns the total attenuation.
	private double light_line_helper(double x1, double x2, double y1, double y2, 
				boolean rotated, boolean invert1, photonColor photon_start)
	{

		// Left End Point calculations.

		int x1_int = (int) x1;
		int y1_int = (int) y1;

		double per_x1_c = x1 - x1_int;
		double per_y1_c = y1 - y1_int;
		
		double per_x1 = 1 - per_x1_c;
		double per_y1 = 1 - per_y1_c;
		
		double scalar_1 = per_x1 * per_y1;
		irradiance_work.addIrradiance(x1_int, y1_int, photon_start, scalar_1, rotated);
		scalar_1 = per_x1 * per_y1_c;
		irradiance_work.addIrradiance(x1_int, y1_int + 1, photon_start, scalar_1, rotated);
 

		// Right End Point calculations.

		int x2_int = (int) x2;
		int y2_int = (int) y2;

		double per_x2_c = x2 - x2_int;
		double per_y2_c = y2 - y2_int;
		double per_y2 = 1 - per_y2_c;
		
		double scalar_2 = per_x2_c*per_y2_c;
		irradiance_work.addIrradiance(x2_int + 1, y2_int + 1, photon_start, scalar_2, rotated);
		scalar_2 = per_x2_c*per_y2;
		irradiance_work.addIrradiance(x2_int + 1, y2_int, photon_start, scalar_2, rotated);

		
		// Draw the main body.
		
		// ASSERT this is positive.
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		
		double distance = BryceMath.distance(x1, y1, x2, y2);
		
		for(int x = x1_int + 1; x <= x2_int; x++)
		{
			double percentage = (x - x1) / dx;
			double y = y1 + dy*percentage;
			
			int y_int = (int)y;
			double per_y_c = y - y_int;
			double per_y = 1 - per_y_c;
			
			// An within pixel accuracy approximation for the attenuation.
			// For more prescision, use the actual distance.
			double partial_distance = invert1 ? (1 - percentage)*distance
											  : percentage*distance;
			
			double attenuation = 1.0;//current_light.attenuation(partial_distance);
						
			irradiance_work.addIrradiance(x, y_int, photon_start,     per_y*attenuation, rotated);
			irradiance_work.addIrradiance(x, y_int + 1, photon_start, per_y_c*attenuation, rotated);
		}
		
		return 1.0;//FIXME: handle attenuation.//current_light.attenuation(distance);
	}
	
	// -- Operations of irradiance caches.
	protected void transferFinishedWork()
	{
		// Adds the normalized values to the final cache.
		irradiance_final.normalizeAndAdd(irradiance_work);
		irradiance_work.clear();
	}
	
	protected void clearCache()
	{
		irradiance_work.clear();
	}
	
	// -- Geometric and aesthetic processing helper functions.
	
	// Returns a polyline representing this face.
	protected Polyline getFace(g_line geom)
	{
		g_line start = geom;
		g_line current = start;
		
		Polyline output = new Polyline(true);
		
		do
		{
			output.addPoint(current.p1);
			current = current.next;
		}while(current != start);
		
		return output;
	}
	
	protected UBA<Geometry> polyLineToGeometryList(Polyline points, boolean invert, Material mat)
	{
		UBA<Geometry> output = new UBA<Geometry>();
		
    	int len = points.size();
    	for(int i = 0; i < len - 1; i++)
    	{
    		int i1 = i;
    		int i2 = i + 1;
    		
    		Point pt1 = points.getPoint(i1);
    		Point pt2 = points.getPoint(i2);
    		
    		// FIXME: Put tangent attributes here. use times and curve.
    		g_line line;
    		if(invert)
    		{
    			line = new g_line(mat, pt2, pt1);
    		}
    		else
    		{
    			line = new g_line(mat, pt1, pt2);
    		}
    		
    		if(output.size() > 0)
    		{
    			g_line prev = (g_line) output.getLast();
    			if(!invert)
    			{
    				prev.next = line;
    			}
    			else
    			{
    				line.next = prev;
    			}
    		}
    		output.push(line);
    	}
    	
		g_line prev = (g_line) output.getLast();
		g_line line = (g_line) output.getFirst();
		if(!invert)
		{
			prev.next = line;
		}
		else
		{
			line.next = prev;
		}
    	
    	return output;
    }
}