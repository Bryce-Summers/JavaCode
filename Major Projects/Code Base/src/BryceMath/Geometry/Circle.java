package BryceMath.Geometry;

import BryceMath.DoubleMath.Vector;

/*
 * Circle Class
 * 
 * Written by Bryce Summers on 4 - 24 - 2014.
 * 
 * data[0] = x.
 * data[1] = y.
 * data[2] = radius.
 */

public class Circle implements Shape
{
	private Vector data;
	
	public Circle(double x, double y, double radius)
	{
		data = new Vector(x, y, radius);
	}
	
	public Circle(Vector center, double radius)
	{
		data = new Vector(center.data[0], center.data[1], radius);
	}
	
	public double getX()
	{
		return data.data[0];
	}
	
	public double getY()
	{
		return data.data[1];
	}
	
	public double getRadius()
	{
		return data.data[2];
	}
	
}
