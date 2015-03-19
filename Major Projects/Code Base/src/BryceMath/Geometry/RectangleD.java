package BryceMath.Geometry;

import static BryceMath.Calculations.MathB.max;
import static BryceMath.Calculations.MathB.min;
import Data_Structures.Structures.UBA;


/*
 * Rectangle Class.
 * My replacement for the built in Rectangle classes.
 * Adapted for Double prescision arithmetic on 12 - 30 - 2013.
 *   
 */

public class RectangleD
{

	// -- Private encapsulated data.
	private double x, y;
	private double w, h;
	
	private double x2,y2;
	
	public RectangleD(double x, double y, double w, double h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		x2 = x + w;
		y2 = y + h;
	}
	
	// Returns true if and only if this rectangle contains the given point.
	public boolean containsPoint(double px, double py)
	{
		return	px >= x && px <= x2 &&
				py >= y && py <= y2;
	}
	
	// Returns the intersection of this and another rectangle
	// The returned rectangle will have negative width and height dimensions if no intersection exists.
	public RectangleD intrsection(RectangleD other)
	{		
		double nx = max(x, other.x);
		double ny = max(y, other.y);

		double nx2 = min(x2, other.x2);
		double ny2 = min(y2, other.y2);
		
		double nw = nx2 - nx;
		double nh = ny2 - ny;
		
		return new RectangleD(nx, ny, nw, nh);
	}
	
	// Returns the smallest rectangle that contains both this and the other rectangle.
	public RectangleD union(RectangleD other)
	{
		double nx = min(x, other.x);
		double ny = min(y, other.y);

		double nx2 = max(x2, other.x2);
		double ny2 = max(y2, other.y2);
		
		double nw = nx2 - nx;
		double nh = ny2 - ny;
		
		return new RectangleD(nx, ny, nw, nh);		
	}
	
	public void setX(double x_new)
	{
		x = x_new;
		x2 = x + w;
	}
	
	public void setY(double y_new)
	{
		y  = y_new;
		y2 = y + h;		
	}
	
	public void setW(double w_new)
	{
		w = w_new;
		x2 = x + w;
	}
	
	public void setH(double h_new)
	{
		h  = h_new;
		y2 = y + h;
	}
	
	// - Data access functions.
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getW()
	{
		return w;
	}
	
	public double getH()
	{
		return h;
	}
	
	//(x1, y1, x2 y2);
	public UBA<java.lang.Double> getBounds()
	{
		UBA<java.lang.Double> output = new UBA<java.lang.Double>(4);
		
		output.add(x );
		output.add(y );
		output.add(x2);
		output.add(x2);
		
		return output;
		
	}
	
	// Returns this rectangle translated to another x, y coordinate by the given amounts.
	public RectangleD offset(double dx, double dy)
	{
		return new RectangleD(x + dx, y + dy, w, h);
	}
	
	public String toString()
	{
		return "Rectangle[ X = " + x +", Y = " + y + ", W = " + w + ", H = " + h + "]";
	}
	
}
