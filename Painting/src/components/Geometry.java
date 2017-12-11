package components;

import geometry.Box;
import geometry.Point;
import geometry.Ray;
import structures.RayQueryable;

/*
 * Written on 12 - 27 - 2014.
 * 
 * This class represents pieces of geometry that may be fed into the Ray Tracer.
 */

public abstract class Geometry extends RayQueryable
{
	
	public static final double INDICE_VACUUM = 1.0;
	
	
	public final Material material;
	
	private Point source_point = null;
	private double distance = -1; 
	private Point intersect_point = null;
	private Point incoming_dir = null;
	
	public Point  normal 		  = null;
	
	private Box _AABB = null;
	
	public Geometry(Material mat)
	{
		this.material = mat;
	}
	
	// Returns the distance to the intersection point given a ray originating at
	// the given point and pointed in the given direction.
	// Ray dir should be normalized.
	public double computeDistance(Point ray_point, Point ray_dir)
	{

		if(source_point != ray_point || incoming_dir != ray_dir)
		{
			source_point = ray_point;
			incoming_dir = ray_dir;
			distance = sub_getIntersection(ray_point, ray_dir);
		}
		
		return distance;
	}
	
	// REQURIRES : computeDistance should have b
	public Point computeIntersectionPoint()
	{
		intersect_point = source_point.add(incoming_dir.multScalar(distance));
		return intersect_point;
	}
	
	/** Returns the distance to the intersection point given a ray originating at
	 * the given point and pointed in the given direction.
	 * 
	 * REQUIRES : Dir should be normalized.
	 */
	protected abstract double sub_getIntersection(Point point, Point dir);

	// This allows the geometry to return a different color based on the location of intersection.
	/*
	 * FIXME : I still need to work out the details of how this can be useful.
	 */
	public abstract photonColor getColor();
	
	// Returns the surface normal for the last intersection point.
	// REQUIRES: the compute_intersection_point method should have been called already.
	public Point getNormal()
	{

		normal = sub_getNormal(intersect_point);
		
		// Return the normal facing the incoming direction.
		if(normal.dot(incoming_dir) > 0)
		{
			return normal.multScalar(-1);
		}
		
		return normal;
	}
	
	protected abstract Point sub_getNormal(Point point);

	
	// Returns the reflectance vector for the last intersection point.
	public Point getReflectionDirection()
	{
		Point normal = getNormal();
		return incoming_dir.reflection(normal);
	}

	public double getNextRefractiveIndex(double n1)
	{
		return (n1 == 1.0) ? material.refractive_index : 1.0;
	}
	
	// Given the refractive index of the incoming ray,
	// returns the Vector in the direction of refraction.
	public Point refract(double n1)
	{
		double n2 = getNextRefractiveIndex(n1);
		
		Point normal = getNormal();
		Point incident = incoming_dir;
		
		final double n = n1/n2;
		final double cosI = Math.abs(-normal.dot(incident));
		final double sinT2 = n * n * (1.0 - cosI * cosI);
		
		if(sinT2 > 1.0) return null; // Total Internal Reflection.

		final double cosT = Math.sqrt(1.0 - sinT2);
		return incident.multScalar(n).add(normal.multScalar(n * cosI - cosT));
		
	}
	
	//	Returns a percentage value between 0 and 1.0 that specifies how much percentage of the light
	//  is reflected and what percentage is transmitted.
	// The input value is the refractive index of the incoming ray.
	double Schlick2(double n1)
	{
		double n2 = getNextRefractiveIndex(n1);
						
		Point normal = getNormal();
		Point incident = incoming_dir;
		
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
	
	
	// Solves the visibility problem for a list of Geometries and a ray.
	// Returns null if no geometry was found.
	public static Geometry visible(Point z, Point dz, Geometry[] data)
	{
		double min_dist = Double.MAX_VALUE;
		Geometry min_g = null;
	
		// Trivial bare bones closest geometry loop.
		// FIXME : Add some bounding boxes.
		for(Geometry g : data)
		{
			double dist = g.computeDistance(z, dz);
			if(0 <= dist && dist < min_dist)
			{
				min_dist = dist;
				min_g = g;
			}
		}
		
		return min_g;
	}
}