package geometry;

import Data_Structures.Structures.UBA;

/*
	Box.

	Written by Bryce Summers on 1 - 5 - 2017.
	Adapted to Java on 12.06.2017
*/

public class Box
{
	public Point min, max;
	private boolean _isFilled; // True iff the box contains its area.
	
	public Box()
	{
		min = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		max = new Point(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
		_isFilled = true;
	}
	
	public Box(boolean _isFilled)
	{
		min = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		max = new Point(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
		_isFilled = true;
	}
	
    // BDS.Point, BDS.Point, bool
    public Box(Point min, Point max, boolean _isFilled)
    {
    	if(min == null)
    	{
    		min = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
    	}

    	if(max == null)
    	{
    		max = new Point(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    	}
    	
    	this.min = min;
    	this.max = max;

    	// Whether the box contains its area or not.
    	this._isFilled = _isFilled;
    }
    
    // BDS.Point, BDS.Point, bool
    public Box(Point min, Point max)
    {
    	if(min == null)
    	{
    		min = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
    	}

    	if(max == null)
    	{
    		max = new Point(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    	}
    	
    	this.min = min;
    	this.max = max;

    	// Whether the box contains its area or not.
    	this._isFilled = true;
    }

    public Box clone()
    {
        return new Box(min.clone(), max.clone());
    }

    public boolean isFilled()
    {
    	return _isFilled;
    }

    public void expandByPoint(Point pt)
    {
        min = this.min.min(pt);
        max = this.max.max(pt);
    }

	public Box union(Box box)
	{
		Box out = this.clone();
	    out.min = this.min.min(box.min);
	    out.max = this.max.max(box.max);
	    return out;
	}

    // Returns the intersection of this box and the other box.
    public Box intersect(Box box)
    {
        Box out = this.clone();
        out.min = this.min.max(box.min);
        out.max = this.max.min(box.max);
        return out;
    }

    public boolean containsPoint(Point pt)
    {
        return pt.greaterThanOrEqual(this.min) && pt.lessThanOrEqual(this.max);
    }

    public Point getRandomPointInBox()
    {
        Point range = this.max.sub(this.min);

        double x = min.x + Math.random()*range.x;
        double y = min.y + Math.random()*range.y;
        double z = min.z + Math.random()*range.z;

        return new Point(x, y, z);
    }

    public double area()
    {
        Point diff = this.max.sub(this.min);

        double area = Math.abs(diff.x*diff.y);

        // Invert area if necessary.
        if(diff.x < 0 || diff.y < 0)
        {
            area = -area;
        }

        return area;
    }

    public boolean intersects_box(Box box)
    {
        Box intersection = this.intersect(box);

        // Intersection exists if the intersection has a positive area.
        // Or if we are intersecting a box that acts as an axis aligned line, which would have 0 area.
        return intersection.area() >= 0;
    }

    public Polyline toPolyline()
    {

        Point p0 = this.min.clone();

        Point p1 = min.clone();
        p1.x = max.x;

        Point p2 = max.clone();

        Point p3 = min.clone();
        p3.y = max.y;

        UBA<Point> points = new UBA<Point>(4);
        points.append(p0, p1, p2, p3);

        // Closed, potentially filled polyline, with the coordinates of this box.
        Polyline polyline = new Polyline(true, points, this._isFilled);
        return polyline;
    }

	public Point getCentroid()
	{
		return min.add(max).divScalar(2);
	}

}