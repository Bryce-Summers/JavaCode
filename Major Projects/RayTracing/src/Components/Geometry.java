package Components;

import BryceMath.DoubleMath.Vector;

/*
 * Written on 12 - 27 - 2014.
 * 
 * This class represents pieces of geometry that may be fed into the Ray Tracer.
 */

public abstract class Geometry 
{
	
	public final Material material;
	
	private Vector source_point = null;
	private double distance = -1; 
	private Vector intersect_point = null;
	private Vector incoming_dir = null;
		
	
	public boolean normal_current = false;
	public Vector  normal 		  = null;
	
	public Geometry(Material mat)
	{
		this.material = mat;
	}
	
	// Returns the distance to the intersection point given a ray originating at
	// the given point and pointed in the given direction.
	public double computeDistance(Vector ray_point, Vector ray_dir)
	{
		if(source_point != ray_point || incoming_dir != ray_dir)
		{
			source_point = ray_point;
			incoming_dir = ray_dir;
			distance = sub_getIntersection(ray_point, ray_dir);
			normal_current = false;
		}
		
		return distance;
	}
	
	// REQURIRES : computeDistance should have b
	public Vector computeIntersectionPoint()
	{
		intersect_point = source_point.add(incoming_dir.mult(distance));
		return intersect_point;
	}
	
	/** Returns the distance to the intersection point given a ray originating at
	 * the given point and pointed in the given direction.
	 * 
	 * REQUIRES : Dir should be normalized.
	 */
	protected abstract double sub_getIntersection(Vector point, Vector dir);
		
	public abstract photonColor getColor();
	
	// Returns the surface normal for the last intersection point.
	// REQUIREs: the compute_intersection_point method should have been called already.
	public Vector getNormal()
	{
		if(normal_current)
		{
			return normal;
		}
		
		normal = sub_getNormal(intersect_point);
		normal_current = true;
		return normal;				
	}
	
	protected abstract Vector sub_getNormal(Vector point);

	
	// Returns the reflectance vector for the last intersection point.
	public Vector getReflectionDirection()
	{
		Vector normal = getNormal();
		return incoming_dir.reflection(normal);
	}
		
	public Vector refract(double n1, double n2)
	{
		Vector normal = getNormal();
		Vector incident = incoming_dir;

		final double n = n1/n2;
		final double cosI = -normal.dot(incident);
		final double sinT2 = n * n * (1.0 - cosI * cosI);
		
		if(sinT2 > 1.0) return null; // Total Internal Reflection.
		
		final double cosT = Math.sqrt(1.0 - sinT2);
		return incident.mult(n).add(normal.mult(n * cosI - cosT));		
		
	}
	
	//	Returns a percentage value between 0 and 1.0 that specifies how much percentage of the light
	//  is reflected and what percentage is transmitted.
	double Schlick2(double n1, double n2)
	{
		Vector normal = getNormal();
		Vector incident = incoming_dir;
		
		double r0 = (n1 - n2) / (n1 + n2);
		r0 *= r0;
		
		double cosI = -normal.dot(incident);
		
		if(n1 > n2)
		{
			final double n = n1 / n2;
			final double sinT2 = n*n*(1.0 - cosI * cosI);
			
			if(sinT2 > 1.0) return 1.0; // Total Internal Reflection.
			
			cosI = Math.sqrt(1.0 - sinT2);
		}
		
		final double x = 1.0 - cosI;
		
		double x_sqr = x*x;
		
		return r0 + (1.0 - r0) * x_sqr * x_sqr * x;
	}

}
