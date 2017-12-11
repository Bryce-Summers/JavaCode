package components;

import geometry.Point;
import geometry.Ray;

/* 2D Line Class, represents all geometric primitives used in Painting.
 * 
 * Written by Bryce Summers on 7/16/2015.
 * 
 * 
 * Represents Line Segments, and possibly rays and lines.
 * Used in my 2 Dimensional Renderer.
 */

public class g_line extends Geometry
{

	final Point p1;
	// Direction normal to the line.
	final Point normal_2d;
	
	// Direction parallel to the line.
	final Point line_dir;
	
	final Point offset;
	
	final double length;
	
	// Used for certain operations.
	public g_line next;
	
	public g_line(Material mat, Point p1, Point p2)
	{
		super(mat);
				
		offset = p2.sub(p1);
		
		this.p1  = p1;
		length   = offset.magnitude();
		
		line_dir =  offset.normalize();
		
		// 2D normal direction.
		normal_2d = new Point(line_dir.y, -line_dir.x, 0).normalize();
	}

	@Override
	protected double sub_getIntersection(Point ray_point, Point ray_dir)
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
	    Point as = p1;
	    Point bs = ray_point;
	    
	    // We use a non normalized direction so that 
	    // the time coordinates will go from 0 to 1.
	    Point ad = offset;
	    Point bd = ray_dir;

	    final int x = 0;
	    final int y = 1;
	    
	    double dx  = bs.x - as.x;
	    double dy  = bs.y - as.y;
	    double det = bd.x * ad.y - bd.y * ad.x;
	    double u = (dy * bd.x - dx * bd.y) / det;
	    double v = (dy * ad.x - dx * ad.y) / det;
	    
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
	protected Point sub_getNormal(Point point)
	{
		return normal_2d;
	}

	@Override
	protected double _isect_ray(Ray ray)
	{
		return sub_getIntersection(ray.getPoint(), ray.getDirection());
	}

	@Override
	protected Exit_Ray_Event _exit_ray(Ray ray, double enter_time)
	{
	
		Exit_Ray_Event output = new Exit_Ray_Event();

		// For now we are ignoring exit events. They might be good for effects later on.
		output.needed = false;
		return output;
	}

    // Returns the normal oriented to the right.
    public Point getRightNormal()
    {
    	return normal_2d.clone();
    }
    
    public Point getMidPoint()
    {
    	return p1.add(offset.multScalar(.5));
    }

	public Point getRandomPoint()
	{
		double time = Math.random();
		return p1.add(offset.multScalar(time));
	}
}