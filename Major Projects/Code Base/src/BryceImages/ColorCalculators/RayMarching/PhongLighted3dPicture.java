package BryceImages.ColorCalculators.RayMarching;

/* This class contains all of the code that enables me to render realistic 3d pictures with accurate lighting.
 * 3d Picture distance estimators and color definitions should be defined within subclasses of this class.
 * 
 * FIXME : Rewrite this class, or perhaps clean it up someday.
 * 
 * Updated : 2 - 4 - 2014 by Bryce Summers :Refactored this package into sub packages.
 */
import static BryceImages.ColorCalculators.RayMarching.BryceMath.abs;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.max;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.min;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sign;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;

import BryceImages.Rendering.ColorCalculator;
import Data_Structures.Structures.List;

public abstract class PhongLighted3dPicture extends ColorCalculator
{

	public static double prescision = .0001;// The precision of the ray marching.

	//-Rendering variables, these should be changed by a subclass in its constructor. Any valid modifications should produce "correct" perspective pictures.
	protected Vector CameraZ = new Vector( 0,0, -15); // These should be set in a subclass to control the camera's variables.
	protected Vector CameraFocus = new Vector(0,0,0);
	
	protected double CameraDepth = 3;
	protected int reflectionDepth = 3;
	
	// This defines which direction is up for the renderer's internal camera coordinate system.
	protected Vector up = new Vector(0,0,-1);
	
	// Geometry and Scene variables
	protected Vector[] lights;
	/*LinkedList<Geometry> geoms		= new LinkedList<Geometry>();
	LinkedList<Geometry> geoms_start= new LinkedList<Geometry>();// Used to store the starting order of the geoms.
	
	LinkedList<Geometry> antigeoms	= new LinkedList<Geometry>();*/
	
	protected List<Geometry> geoms		= new List<Geometry>();
	protected List<Geometry> geoms_start  = new List<Geometry>();// Used to store the starting order of the geoms.
	
	LinkedList<Geometry> antigeoms	= new LinkedList<Geometry>();
	
	protected double defaultRefelectivity = .5;
	
	Geometry tempGeomPointer;	// This will be used to store the Geometry that the last ray came closest to.
	
	Vector i,j,k;

	public PhongLighted3dPicture(Dimension tempDim)
	{
		super(tempDim);
		
		lights = new Vector[2];
		lights[0] = new Vector(5,5,3);
		lights[1] = new Vector(-5,-5,3);
		
		loadGeoms();
		createCoordinateSystem();
	}
	
	// This ColorCalculator must override clone, because it needs to deep copy the lists.
	public Object clone()
	{
		PhongLighted3dPicture p = (PhongLighted3dPicture) super.clone();
		
		p.geoms		   = new List<Geometry>();
		p.geoms_start  = new List<Geometry>();// Used to store the starting order of the geoms.
		p.antigeoms    = new LinkedList<Geometry>();
		
		p.loadGeoms();
		p.createCoordinateSystem();
		
		return p;
	}
	
	public void createCoordinateSystem()
	{
		
		// Create a coordinate system from the camera point and
		//I am going to call them i,j, and k, with i and j representing a conceptual x & y, and with j representing the direction into the plane.
		k = CameraFocus.sub(CameraZ);
		k = k.norm();// Only normalized vectors are necessary for an orthogonal curvilinear coordinate system.
		
		//replace the -1 below with: sign(k.z) if you want to perform forward or backward summersaults.
		j = up;// A vector representing the "up" direction in the world. If you modify this, then make sure j ends up being a unit vector.
		
		// The j inside of this orthogonal coordinate system is equal to the component of j that is perpendicular to k.
		j = j.sub(k.mult(j.dot(k)));// Subtract the part of J parallel to k from j to get the perpendicular part.
		// Now normalize j to finalize its instantiation as a unit vector in the j direction.
		j = j.norm();
				
		i = j.cross(k);// The third and final direction is easily found by the cross product of the other two.
		
	}
	
	
	public Color getColor(double x, double y)
	{
		// Nifty tools for rendering chunks at a time.
		//room_width = 7200;
		//room_height= 7200;

		//y += 7000;
		
		// Now translate the inputed x and y screen coordinates into vectors for the camera pixels.
		
		x -= room_width/2;	// Center the vectors on the middle of the screen.
		y -= room_height/2;

		// Scale them appropriately to create a standard width and height of the aperture.
		int factor = (int) min(room_width,room_height);
				
		// Now create the vector that represents the initial ray direction from the Camera to the Scene at this location.
		Vector dz = i.mult(x/factor).add(j.mult(y/factor)).add(k.mult(CameraDepth));
		
		// Old way using normal Cartesian directions.
		//dz =  new Vector( x/factor, y/factor, CameraDepth);
		dz = dz.norm();// Normalize this vector so that it can march at precise distances into the scene.
				
		// The geometric location vector.
		Vector z = CameraZ;

		z = pick(z,dz);
		if(z == null)
		{
			return Color_hsv(0,0,0,0);
		}
		

		// Now that we have the Vector coordinate of intersection and the direction the ray went from the camera,
		// we can calculate this pixel's color.
		return calculateColor(z, dz, reflectionDepth);
			
	}

	// The Phong Lighting model with reflective recursion for good measure.
	public Color calculateColor(Vector z, Vector dz, int recursion)
	{
		double finalLighting = 0;
		double finalPhong = 0;
		
		Vector zi,dzi;
		
		
		Geometry geomPointer = null;// Store the correct geometry pointer, before any more picks are made.
		if(tempGeomPointer!=null)// Note: this is local, so it is recursion safe!!!
		{										
			geomPointer = tempGeomPointer;	// Only set this if the shape array is used. 
			 								// Otherwise some subclasses will want to do all of the distance estimation and color calculation manually.
		}
				
		Vector normal = computeNormal(z,.0000001);// Compute the normal Vector to the scene's geometry.
		dz = dz.reflection(normal);// Use the reflection formula for vectors on a surface to compute the reflected trajectory.
		
		zi	= z;
		dzi	= dz;
		
		//--Now compute lighting considering all lights in the scene.
		for(Vector Light1: lights){

			z	= zi;
			dz	= dzi;
					
			double lighting = light(z,Light1);// Calculate Diffuse Lighting
			double phong = 0;
		
			if(lighting!=0)// If This part of the object is not in complete shadow calculate the phong lighting.
			{
				//--Now calculate the specular lighting.
				
				Vector lightToSurface = Light1.sub(z);
				Vector lightToSurface_norm = lightToSurface.norm();
				
				phong = dz.dot(lightToSurface_norm); // Calculate Specular lighting
				phong = phong / Math.max(1, lightToSurface.mag()/10);
				
				double phongThresh = .95;// Calculate how much of the light's color to draw (White) and how much of the ColorCalculator's color to draw.
				if(phong > phongThresh){
					phong = (phong - phongThresh)*1/(1 - phongThresh);
				}
				else{
					phong = 0;
				}
			}
			
			lighting = min(lighting+.2,1);// Add ambient lighting.
			
			
			// Now add these component lights and phong to the final lights and phong.
			finalLighting = min(1,lighting+finalLighting);
			finalPhong = max(finalPhong,phong);
			
		}// end of Light Loop
		
		// The color of this point in space without regard to reflected light beams.
		
		Color output;
		if(geomPointer!=null)
		{
			output = weightedAverageColor(geomPointer.getColor(z,finalLighting),Color.white,finalPhong);
		}
		else // manually implementation of DE and Surface colors.
		{
			output = weightedAverageColor(getColor(z,finalLighting),Color.white,finalPhong);
		}
		
		
		if(sign(dz.dot(normal))>=0)
			z = deCollide(z,dz);
		
		z = pick(z,dz);
		
		if(z != null && recursion > 0)
		{
			if(geomPointer!=null){
				if(geomPointer.reflectivity()>0)
					output = weightedAverageColor(output, calculateColor(z,dz,recursion-1),geomPointer.reflectivity());
			}
			else // Manual implementation.
			{
				output = weightedAverageColor(output, calculateColor(z,dz,recursion-1),defaultRefelectivity);
			}
		}
		
		return output;
	}
	
	public Color weightedAverageColor(Color c1, Color c2, double pC2){
		// Percent C2 (pC2) is an input.
		double pC1 = 1 - pC2;// Percent C2;
		
		
		double red 		= c1.getRed()	*pC1 + c2.getRed()	*pC2;
		double green 	= c1.getGreen()	*pC1 + c2.getGreen()*pC2;
		double blue 	= c1.getBlue()	*pC1 + c2.getBlue()	*pC2;
		
		return new Color((int)red,(int)green, (int)blue );
		
	}
	
	// A function for the color of a point at z with a light variable ranging from 0 to 1;
	public abstract Color getColor(Vector z, double light);
	
	// Returns whether the given coordinates are within bounds.
	public abstract boolean withinBounds(Vector z);
	
	// The current implementation of my lighting algorithm includes standard cosine shading and basic shadow calculating.
	// Inputs are a geometric vector and the vector of a light.
	private double light(Vector z, Vector l){
		double dotProduct;
		double e = .000000001;
		
		
		Vector normal = computeNormal(z,e);// Compute the normal at vector z with an epsilon tolerance of e.
				
		// Compute a Normalized vector in the direction of the light from the current geometric coordinate.
		Vector dz = l.sub(z).norm();
		
		
		double distToLight = z.sub(l).mag(); 
		Vector previousZ = z;// Store a Pointer to the previous z;
		
		// March this z out of collision range from the internal geometry so the proceeding pick is meaningful.
		// Only do this if the vector is not going through the current surface.
		if(sign(dz.dot(normal))>=0){
			z = deCollide(z, dz);
		}
		else// if the path to the light goes through this piece of geometry, then the light does not illuminate this point.
		{
				return 0;
		}
		//Now compute shadow.

		z = pick(z,dz);
		
		if(z == null || z.sub(previousZ).mag() > distToLight){
			dotProduct = abs(dz.dot(normal));
		}
		else
		{
			dotProduct = 0;	
		}
		
		return dotProduct * l.val;// /sqr(distToLight);// Scale by the light vector's val which represents its intensity.
	}
	
	// A method that marches the current vector in the given direction until it does not collide with the scene's geometry.
	private Vector deCollide(Vector z, Vector dz)
	{
		for(;;){// March out of the current collision.
			double temp = DE(z);
			if(temp > prescision)
			{
				break;
			}
			z = march(z,dz,Math.max(temp,prescision));	
		}
		return z;
	}
	
	
	private Vector pick(Vector z, Vector dz)
	{
		
		//FIXME: You should implement your own linked list and array data structure for remembering minDists.
		optimizeGeomList(z);
		
		double minStepSize;

		double distGone = 0;
		for(;;)// While true.
		{
			
			//System.out.println("Hello");
			
			if(!withinBounds(z))
				break;

			
			// Calculate the minimum StepSize
			
			double minDist = Double.MAX_VALUE;
			double tempDist;
			tempGeomPointer = geoms.getFirst();// This pointer is used to remember the closest geometry, which will be the one to provide the color.

			/*
			//Perform and optimized resorting of the geoms list. only those elements that have recalculated their distances need to be resorted.
			LinkedList<Geometry> tempGeoms = new LinkedList<Geometry>();
			while(!geoms.isEmpty() && geoms.getFirst().distChanged)
			{
				tempGeoms.add(geoms.pop());
			}
			Collections.sort(tempGeoms);
			int k = 0;
			while(k < geoms.size())
			{
				while(!tempGeoms.isEmpty() && tempGeoms.getFirst().minDist < geoms.get(k).minDist)
				{
					// insert the geometry into the list and increment the list element pointer.
					Geometry g = tempGeoms.pop();
					g.distChanged = false;
					geoms.add(k,g);
					k++;
				}
				k++;
			}
			// Now add the ramining elements into the list.
			while(!tempGeoms.isEmpty())
			{
				Geometry g = tempGeoms.pop();
				g.distChanged = false;
				geoms.add(g);
			}//*/
			
			//Collections.sort(geoms);
			geoms.sort();
		    
			/*
			for(Geometry i: geoms)
			{
				System.out.print((int)i.minDist + " ");
			}
			System.out.println();
			System.out.println();
			*/
			
						
			// Perform the distance estimation
			boolean flag = false;
			//double flagVal = 0;
			for (Geometry i: geoms)
			{
				
				if(i.minDist > distGone + 3*minDist)
				{
					if(!flag)
					{
						flag = true;
						//flagVal = i.minDist;
					}
					continue;
				}

				tempDist = i.DE(z,dz); 		// Retrieve this shape's distance estimation
				i.minDist = tempDist + distGone;
				i.distChanged = true;
				
				if( tempDist < minDist ) 	// Set the current minimum distance to the minimum of the previous ones and the current one.
				{
					//if(flag)
						//System.out.println(flagVal - (tempDist + distGone));
					minDist = tempDist;
					tempGeomPointer = i;	// Set the current geometry pointer to the current Geometry object inside of the list.
				}
			}// end of geoms list iteration loop.
			
			if(geoms.getFirst() == null)
			{
				minDist = DE(z, dz);
			}
			
			minStepSize = minDist;//DE(z,dz);
		
			if(minStepSize < prescision)// Collision detected.
			{
				if(minStepSize<.000001)
				{
					z = march(z,dz,-.00001);// Take a step back if this result is so good that it messes up the raymarching whith its exactness.
				}

				return z;
			}
			
			z = march(z, dz, minStepSize);
			// Very important.
			distGone += minStepSize+.00001;
		}

		
		// This seems kind of excessive, because if there were a collision, then the code would not get here, but better safe than sorry.
		return null;// No Collisions within Bounds.
	}
	
	public void optimizeGeomList(Vector z)
	{

		if(geoms.getFirst() == null)
		{
			return;
		}
		
		// Optimized for the starting coordinate.
		if(z.equals(CameraZ))
		{
			//if start dist unititialized.
			if (geoms.getFirst().startDist < 0)
			{
				for(Geometry g: geoms)
				{
					g.startDist = g.DE(z);
					g.minDist	= g.startDist;
				}
				//Collections.sort(geoms);
				geoms.sort();
			}
			for(Geometry g: geoms)
			{
				g.minDist	= g.startDist;
			}
			// Collections.sort(geoms);
			return;
		}
		for(Geometry g: geoms)
		{			
			g.minDist = g.DE(z);
		}
	}
	
	// Returns a new Geometric vector for a vector that has been marched a given distance along the given velocity vector (dz).
	private Vector march(Vector z, Vector dz, double distance)
	{
		return z.add(dz.mult(distance));	
	}

	protected Vector computeNormal(Vector z, double e)
	{

		double d1, d2;
		
		Vector out = new Vector(0,0,0);
		if(tempGeomPointer != null){
			// x		// The first DE() ensures that the correct tempGeomPointer is used.
			d1 = 				 DE(new Vector(z.x + e, z.y, z.z));
			d2 = tempGeomPointer.DE(new Vector(z.x - e, z.y, z.z));
			out.x = (d1 - d2)/(2*e);// The average slope over this region.
			
			// y
			d1 = tempGeomPointer.DE(new Vector(z.x, z.y + e, z.z));
			d2 = tempGeomPointer.DE(new Vector(z.x, z.y - e, z.z));
			out.y = (d1 - d2)/(2*e);// The average slope over this region.
			
			// z
			d1 = tempGeomPointer.DE(new Vector(z.x, z.y, z.z + e));
			d2 = tempGeomPointer.DE(new Vector(z.x, z.y, z.z - e));
			out.z = (d1 - d2)/(2*e);// The average slope over this region.
		}
		else
		{
			// x
			d1 = DE(new Vector(z.x + e, z.y, z.z));
			d2 = DE(new Vector(z.x - e, z.y, z.z));
			out.x = (d1 - d2)/(2*e);// The average slope over this region.
			
			// y
			d1 = DE(new Vector(z.x, z.y + e, z.z));
			d2 = DE(new Vector(z.x, z.y - e, z.z));
			out.y = (d1 - d2)/(2*e);// The average slope over this region.
			
			// z
			d1 = DE(new Vector(z.x, z.y, z.z + e));
			d2 = DE(new Vector(z.x, z.y, z.z - e));
			out.z = (d1 - d2)/(2*e);// The average slope over this region.
		}
		
		return out.norm(); // Normalize this normal calculation.
		
	}

	// This function should be implemented by subclasses to add geometry to the geoms list. It is assumed that at least 1 piece of geometry will be added.
	public abstract void loadGeoms();
	
	// This is the basic DE method, which can be overridden by a subclass.
	public double DE (Vector z)
	{
		double minDist = Double.MAX_VALUE;
		double tempDist;
		tempGeomPointer = geoms.getFirst();

		
		

		for (Geometry i: geoms)
		{
			tempDist = i.DE(z); // Retrieve this shape's distance estimation.
			if( tempDist < minDist ) // Set the current minimum distance to the minimum of the previous ones and the current one.
			{
				minDist = tempDist;
				tempGeomPointer = i;	// Set the current geometry pointer to the current Geometry object inside of the list.
			}
		}// end of geoms list iteration loop.
		
		return minDist;
	}
	
	// This is a very important function. This is what determines the internal scene geometry. It should only be invoked when using the geom list approach.
	public double DE(Vector z, Vector dz)
	{		
		double minDist = Double.MAX_VALUE;
		double tempDist;
		tempGeomPointer = geoms.getFirst();// This pointer is used to remember the closest geometry, which will be the one to provide the color.
		
		// Iterate through the geoms list.
		for (Geometry i: geoms)
		{

			tempDist = i.DE(z,dz); 		// Retrieve this shape's distance estimation.
			
			if( tempDist < minDist ) 	// Set the current minimum distance to the minimum of the previous ones and the current one.
			{
				minDist = tempDist;
				tempGeomPointer = i;	// Set the current geometry pointer to the current Geometry object inside of the list.
			}
		}// end of geoms list iteration loop.
		
		
		
		// antigeoms code.
		if(!antigeoms.isEmpty())
		{
			for (Geometry i: antigeoms)
			{
				tempDist = i.DE(z,dz); // Retrieve this shape's distance estimation.
				minDist = max(minDist,tempDist);
			}// end of geoms list iteration loop.
		}
		return minDist;
		
	}
	
}
