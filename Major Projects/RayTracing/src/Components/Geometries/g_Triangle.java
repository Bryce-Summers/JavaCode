package Components.Geometries;

import BryceMath.DoubleMath.Vector;
import Components.Geometry;
import Components.Material;
import Components.photonColor;

public class g_Triangle extends Geometry
{

	Vector p0, p1, p2, u, v, w;
	
	Vector normal;
	
	boolean plane;
	
	// Compute Basis vectors for the plane of the triangle.

		
	// -- Constructor.
	public g_Triangle(
			Material mat,
			Vector p0,
			Vector p1,
			Vector p2,
			boolean plane)
	{
		super(mat);
		
		// Points.
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		
		// Line direction vectors.
		u = p1.sub(p0);
		v = p2.sub(p1);
		w = p0.sub(p2);
		
		this.normal = u.cross(v);
		this.normal = this.normal.norm();
		
		
		this.plane = plane;
	}


	/*
	 * Returns the time it takes the given ray to get to the out put,
	 * returns 0 or a negative number if no intersection exists.
	 */
	@Override
	protected double sub_getIntersection(Vector point, Vector dir)
	{

		// Ray components.
		Vector R_pos = point;
		Vector R_dir = dir;

		double perp_comp = -R_dir.dot(normal);

		// No intersection if the ray does not point towards the plane.
		if (perp_comp <= 0)
		{
			return -1;
		}

		// Compute the amount of time it will take for the ray to hit the plane.
		double t = R_pos.sub(p0).dot(normal) / perp_comp;

		if(plane)
		{
			return t;
		}

		
		// The part that determines the constraining of the set within the plane.
		
		
		Vector point_on_plane = getPointOnRay(R_pos, R_dir, t);
		
		
		// Determine whether the point on the plane is within the triangles.
		// Three lineside tests.
		boolean b1 = u.cross(point_on_plane.sub(p0)).dot(normal) >= 0;
		boolean b2 = v.cross(point_on_plane.sub(p1)).dot(normal) >= 0;
		boolean b3 = w.cross(point_on_plane.sub(p2)).dot(normal) >= 0;

		if (b1 && b2 && b3)
		{
			return t;
		}

		return -1;
	}
	
	// Helper function for computing points on a ray.
	Vector getPointOnRay(Vector position, Vector direction, double time)
	{
		return position.add(direction.mult(time));
	}

	// FIXME : Return the color of the texture.
	@Override
	public photonColor getColor()
	{
		return photonColor.WHITE;
	}

	@Override
	protected Vector sub_getNormal(Vector point)
	{
		return normal;
	}
	
}
