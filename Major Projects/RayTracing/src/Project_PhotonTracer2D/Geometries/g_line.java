package Project_PhotonTracer2D.Geometries;

import Components.Geometry;
import Components.Material;
import Components.photonColor;
import Math.Vector3;
import Project_PhotonTracer2D.Geometry2D.Box;

/* 2D Line Class
 * 
 * Written by Bryce Summers on 7/16/2015.
 * 
 * 
 * Represents Line Segments, and possibly rays and lines.
 * Used in my 2 Dimensional Renderer.
 */

public class g_line extends Geometry
{

	final Vector3 p1;
	// Direction normal to the line.
	final Vector3 normal_2d;
	
	// Direction parallel to the line.
	final Vector3 line_dir;
	
	final Vector3 offset;
	
	final double length;
	
	public g_line(Material mat, Vector3 p1, Vector3 p2)
	{
		super(mat);
				
		offset = p2.sub(p1);
		
		this.p1  = p1;
		length   = offset.mag();
		
		line_dir =  offset.norm();
		
		// 2D normal direction.
		normal_2d = new Vector3(line_dir.data[1], -line_dir.data[0], 0).norm();
	}

	@Override
	protected double sub_getIntersection(Vector3 ray_point, Vector3 ray_dir)
	{
		// Ray is parallel to this line, no intersection.
		if(Math.abs(ray_dir.dot(line_dir)) >= .999)
		{
			return -1;
		}
		
	    // Find the intersection point.

	    /*
	    u = ((bs.y - as.y) * bd.x - (bs.x - as.x) * bd.y) / (bd.x * ad.y - bd.y * ad.x)
	    v = ((bs.y - as.y) * ad.x - (bs.x - as.x) * ad.y) / (bd.x * ad.y - bd.y * ad.x)
	    Factoring out the common terms, this comes to:

	    dx = bs.x - as.x
	    dy = bs.y - as.y
	    det = bd.x * ad.y - bd.y * ad.x
	    u = (dy * bd.x - dx * bd.y) / det
	    v = (dy * ad.x - dx * ad.y) / det
	    */

	    // Extract the relevant points.
	    Vector3 as = p1;
	    Vector3 bs = ray_point;
	    
	    // We use a non normalized direction so that 
	    // the time coordinates will go from 0 to 1.
	    Vector3 ad = offset;
	    Vector3 bd = ray_dir;

	    final int x = 0;
	    final int y = 1;
	    
	    double dx  = bs.data[x] - as.data[x];
	    double dy  = bs.data[y] - as.data[y];
	    double det = bd.data[x] * ad.data[y] - bd.data[y] * ad.data[x];
	    double u = (dy * bd.data[x] - dx * bd.data[y]) / det;
	    double v = (dy * ad.data[x] - dx * ad.data[y]) / det;
	    
	    /* The intersection is at time coordinates u and v.
	     * Note: Time is relative to the offsets, so p1 = time 0 and p2 is time 1.
	     * u is the time coordinate for this line.
	     * v is the time coordinate for the other line.
	     */
	    
	    // Bounds for line segment, incoming ray collision.
	    if(0 <= u && u <= 1 && v > 0)
	    {
	    	return v;
	    }
	    else // Ray doesn't hit within segment, or ray goes backwards.
	    {
	    	return -1;
	    }
	    
	    // Bounds for line, incoming ray collision.
	    /*
	    if(v > 0)
	    {
	    	return v;
	    }
	    else // Ray goes backwards.
	    {
	    	return -1;
	    }
	    */
	    
	    // Bounds for ray, incoming ray collision.
	    /*
	    if(u > 0 && v > 0)
	    {
	    	return v;
	    }
	    else // Ray goes backwards.
	    {
	    	return -1;
	    }
	    */
	}

	@Override
	public photonColor getColor()
	{
		return photonColor.WHITE;
	}

	@Override
	protected Vector3 sub_getNormal(Vector3 point)
	{
		return normal_2d;
	}

}
