package BryceMath.Geometry;

import Data_Structures.ADTs.Pairable;

public class Point implements Pairable<Double>
{

	public double x, y;
	
	public Point(double x_in, double y_in)
	{
		x = x_in;
		y = y_in;
	}

	@Override
	public Double getFirst()
	{
		return x;
	}

	@Override
	public Double getLast()
	{
		return y;
	}
	
	public Double getX()
	{
		return x;
	}
	
	public Double getY()
	{
		return y;
	}

}
