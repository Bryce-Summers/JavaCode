package geometry;

import BryceImages.ColorCalculators.RayMarching.BryceMath;

/*
 * A vector class specifically meant for 2D geometric operations.
 */

public class Point
{
	/*
	Point.

	Written by Bryce Summers on 1 - 2 - 2017.

	Implements Arithmetic.

	add, sub, multScalar
	*/
	
	public double x, y, z;
	
	public Point(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
    public Point clone()
    {
        return new Point(x, y, z);
    }

    public Point add(Point pt)
    {
        Point out = this.clone();
        out.x += pt.x;
        out.y += pt.y;
        out.z += pt.z;
        return out;
    }

    public Point sub(Point pt)
    {
    	Point out = this.clone();
        out.x -= pt.x;
        out.y -= pt.y;
        out.z -= pt.z;
        return out;
    }

    public Point multScalar(double s)
    {
        Point out = this.clone();
        out.x *= s;
        out.y *= s;
        out.z *= s;
        return out;
    }

    Point divScalar(double s)
    {
        Point out = this.clone();
        out.x /= s;
        out.y /= s;
        out.z /= s;
        return out;
    }

    public String toString()
    {
        String output = "Point(" + this.x + ", " + this.y;

        output += ", " + this.z;
        output += ")";

        return output;
    }

    public double magnitude()
    {
        return this.norm();
    }

    // Euclidean Norm.
    public double norm()
    {
        return Math.sqrt(this.norm2());
    }

    void setMag(double mag)
    {
    	double scalar = mag / norm();
    	Point p = this.multScalar(scalar);
    	this.copyFrom(p);
    }
    
    public void copyFrom(Point p)
    {
    	this.x = p.x;
    	this.y = p.y;
    	this.z = p.z;
    }
    
    // Square of the Euclidean norm.
    public double norm2()
    {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }

    public Point min(Point pt)
    {
        Point out = this.clone();
        out.x = Math.min(this.x, pt.x);
        out.y = Math.min(this.y, pt.y);
        out.z = Math.min(this.z, pt.z);
        return out;
    }

    public Point max(Point pt)
    {
        Point out = this.clone();
        out.x = Math.max(this.x, pt.x);
        out.y = Math.max(this.y, pt.y);
        out.z = Math.max(this.z, pt.z);
        return out;
    }

    public Point normalize()
    {
        return divScalar(this.norm());
    }

    public double dot(Point pt)
    {
        return pt.x*this.x + pt.y*this.y + pt.z*this.z;
    }

    // returns true iff this point is less than the input point on every dimension.
    public boolean lessThan(Point pt)
    {
        return this.x < pt.x && this.y < pt.y && this.z < pt.z;
    }

    public boolean lessThanOrEqual(Point pt)
    {
        return this.x <= pt.x && this.y <= pt.y && this.z <= pt.z;
    }

    // returns true iff this point is greater than the input point on ever dimension.
    public boolean greaterThan(Point pt)
    {
        return this.x > pt.x && this.y > pt.y && this.z > pt.z;
    }

    public boolean greaterThanOrEqual(Point pt)
    {
        return this.x >= pt.x && this.y >= pt.y && this.z >= pt.z;
    }
    
    public static double dist(Point p1, Point p2)
    { 
    	return BryceMath.distance(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
    }
    
    public Point cross(Point o)
    {	
		double x_out = y*o.z - z*o.y;
		double y_out = z*o.x - x*o.z;
		double z_out = x*o.y - y*o.x;
		
		return new Point(x_out, y_out, z_out);
    }
    
    public Point reflection(Point normal)
	{
		// Note: All vector calls with no vector name are called by this vector!!!
		return sub( normal.multScalar(2).multScalar(dot(normal)));
	}
    
    public Point perp()
    {
    	return new Point(-y, x);
    }
}