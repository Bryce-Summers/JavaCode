package Project3D.Geometries;

import BryceMath.DoubleMath.Vector;
import Components.Geometry;
import Components.Material;
import Components.photonColor;
import Math.Vector3;

public class g_Sphere extends Geometry
{

	protected Vector3 location;
	double radius_sqr;
	
	// -- Constructor.
	public g_Sphere(
			Material mat,
			Vector3 location,
			double radius)
	{
		super(mat);
		
		this.location = location;
		this.radius_sqr = radius*radius;
	}
	
	public g_Sphere(
			Material mat,
			Vector3 location,
			double radius, boolean sqr)
	{
		super(mat);
		
		this.location = location;
		
		this.radius_sqr = sqr ? radius : radius*radius;

	}

	// FIXME : Clean this up, because it is rather ugly.
	@Override
	protected double sub_getIntersection(Vector3 point, Vector3 dir)
	{		
		Vector3 o = point;
		Vector3 l = dir;
		
		//Vector3 c = Vector3(0,0,0);
		
		Vector3 o_sub_c = o.sub(location);
		double o_sub_c_sqr = o_sub_c.dot(o_sub_c);
		double loc = o_sub_c.dot(l);// Projection. Needs normal vector.
		double det_sqr = loc*loc - o_sub_c_sqr + radius_sqr;

		if (det_sqr < 0)
		{
			return -1;
		}

		double det = Math.sqrt(det_sqr);

		// Compute the two solutions to the quadratic formula.
		double time1 = -loc + det;
		double time2 = -loc - det;

		if (time2 > 0)
		{
			return time2;
		}

		if (time1 > 0)
		{
			return time1;
		}

		// All intersections are on the line, but not the ray.
		return -1;
	}

	// FIXME : This should return a color based on a texture that has been mapped to a sphere.
	// FIXME : Maybe we should remove this function, because it seems less and less important.
	@Override
	public photonColor getColor()
	{
		return photonColor.WHITE;
	}

	@Override
	protected Vector3 sub_getNormal(Vector3 point)
	{
		Vector3 normal = point.sub(location);
		
		return normal.norm();
	}

	@Override
	public Object clone()
	{
		return new g_Sphere(material,
			location,
			radius_sqr, true);
	}



}
