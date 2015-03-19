package BryceMath.Geometry;

import static BryceMath.Calculations.MathB.max;
import static BryceMath.Calculations.MathB.min;
import Data_Structures.Structures.UBA;


/*
 * Rectangle Class.
 * My replacement for the built in Rectangle classes
 * Written by Bryce Summers 3 - 11 - 2013.
 * Updated by Bryce Summers 6 - 3  - 2013.
 * 
 *  FIXME: Eventually transfer this to a primitive math package and rewrite this using more powerful numbers.
 *  
 */

public class Rectangle
{

	// -- Private encapsulated data.
	private int x, y;
	private int w, h;
	
	private int x2,y2;
	
	public Rectangle(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		x2 = x + w;
		y2 = y + h;
	}
	
	// Returns true if and only if this rectangle contains the given point.
	public boolean containsPoint(int px, int py)
	{
		return	px >= x && px <= x2 &&
				py >= y && py <= y2;
	}
	
	// Returns the intersection of this and another rectangle
	// The returned rectangle will have negative width and height dimensions if no intersection exists.
	public Rectangle intersection(Rectangle other)
	{		
		int nx = max(x, other.x);
		int ny = max(y, other.y);

		int nx2 = min(x2, other.x2);
		int ny2 = min(y2, other.y2);
		
		int nw = nx2 - nx;
		int nh = ny2 - ny;
		
		return new Rectangle(nx, ny, nw, nh);
	}
	
	// Returns the smallest rectangle that contains both this and the other rectangle.
	public Rectangle union(Rectangle other)
	{
		int nx = min(x, other.x);
		int ny = min(y, other.y);

		int nx2 = max(x2, other.x2);
		int ny2 = max(y2, other.y2);
		
		int nw = nx2 - nx;
		int nh = ny2 - ny;
		
		return new Rectangle(nx, ny, nw, nh);		
	}
	
	public void setX(int x_new)
	{
		x = x_new;
		x2 = x + w;
	}
	
	public void setY(int y_new)
	{
		y  = y_new;
		y2 = y + h;		
	}
	
	public void setW(int w_new)
	{
		w = w_new;
		x2 = x + w;
	}
	
	public void setH(int h_new)
	{
		h  = h_new;
		y2 = y + h;
	}
	
	// - Data access functions.
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getW()
	{
		return w;
	}
	
	public int getH()
	{
		return h;
	}
	
	//(x1, y1, x2 y2);
	public UBA<java.lang.Integer> getBounds()
	{
		UBA<java.lang.Integer> output = new UBA<java.lang.Integer>(4);
		
		output.add(x );
		output.add(y );
		output.add(x2);
		output.add(x2);
		
		return output;
		
	}
	
	// Returns this rectangle translated to another x, y coordinate by the given amounts.
	public Rectangle offset(int dx, int dy)
	{
		return new Rectangle(x + dx, y + dy, w, h);
	}
	
	// Returns this rectangle translated to another x, y coordinate by the given amounts.
	public Rectangle offset(double dx, double dy)
	{
		return offset((int)dx, (int) dy);
	}
	
	public String toString()
	{
		return "Rectangle[ X = " + x +", Y = " + y + ", W = " + w + ", H = " + h + "]";
	}
	
	// Equality check for rectangles.
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		
		if(o == this)
		{
			return true;
		}
		
		if(o instanceof Rectangle == false)
		{
			return false;
		}
		
		Rectangle r = (Rectangle) o;
		
		return 	this.x == r.x &&
				this.y == r.y &&
				this.w == r.w &&
				this.h == r.h;
	}
	
	public Rectangle clone()
	{
		return new Rectangle(x, y, w, h);
	}
	
	@Override
	public int hashCode()
	{
		return x +  193939*y + w*w* 1193 + h*h*h*23981;
	}
}
