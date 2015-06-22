package Components;

import BryceMath.DoubleMath.Vector;

/*
 * Written on 12 - 27 - 2014.
 * 
 * This class specifies a point camera and a distribution of rays that it sends out into the world.
 * 
 * This class serves to keep the specification for the rendering attributes separate from the ray tracer itself.
 */

public class Camera
{

	// A Camera is a a location and looks towards a focus point.
	private Vector location, focus;
	
	private Vector up;
	
	// The zoom factor that controls the spread on the distribution of rays.
	private double zoom;
	
	// Image variables.
	private int image_w, image_h;
	private int half_w, half_h;
	
	// These vectors are orthogonal unit vectors that span 3d space and are aligned with this camera
	// and the image that it will generate.
	Vector i, j, k;
	
	
	// Rendering properties such as reflectance depth.
	private int reflectance_depth;
			
	public Camera(Vector location, Vector focus, Vector up, double zoom,
		int image_w, int image_h,
		int reflectance_depth)
	{
		this.location = location;
		this.focus    = focus;
		
		this.up = up;
		
		this.zoom     = zoom;
		
		this.image_w = image_w;
		this.image_h = image_h;
		
		half_w = image_w/2;
		half_h = image_h/2;
		
		this.reflectance_depth = reflectance_depth;
		
		createCoordinateSystem();
	}
	
	private void createCoordinateSystem()
	{
		
		// Create a coordinate system from the camera point and
		// I am going to call them i,j, and k, with i and j representing a conceptual x & y, and with j representing the direction into the plane.
		k = focus.sub(location);
		k = k.norm();// Only normalized vectors are necessary for an orthogonal curvilinear coordinate system.
		
		//replace the -1 below with: sign(k.z) if you want to perform forward or backward summersaults.
		j = up;// A vector representing the "up" direction in the world. If you modify this, then make sure j ends up being a unit vector.
		
		// The j inside of this orthogonal coordinate system is equal to the component of j that is perpendicular to k.
		j = j.sub(k.mult(j.dot(k)));// Subtract the part of J parallel to k from j to get the perpendicular part.
		// Now normalize j to finalize its instantiation as a unit vector in the j direction.
		j = j.norm();
				
		i = j.cross(k);// The third and final direction is easily found by the cross product of the other two.
		
	}
	
	// REQUIRES : The x and y locations in screen pixel space.
	// ENSURES : Returns the point at which the ray will begin.
	// This can be changed to specify the properties of the camera, 2D, 3D.	
	public Vector getPoint(double x, double y)
	{
		return location;
	}
	
	public Vector getDirection(double x, double y)
	{
		// Center the vectors on the middle of the screen.
		x -= half_w;
		y -= half_h;

		// Scale them appropriately to create a standard width and height of the aperture.
		int factor = (int) Math.min(image_w, image_h);
				
		// Now create the vector that represents the initial ray direction from the Camera
		// to the Scene at this location.
		Vector dz = i.mult(x/factor).add(j.mult(y/factor)).add(k.mult(zoom));
		
		dz = dz.norm();
		return dz;
	}
	
	// FIXME Perhaps I should change this to specify the minimum tolerable photon energy ratio.
	// i.e. a photon should not be traced further if only contains .001 percent of its initial energy.
	public int getReflectanceDepth()
	{
		return reflectance_depth;
	}
	
}
