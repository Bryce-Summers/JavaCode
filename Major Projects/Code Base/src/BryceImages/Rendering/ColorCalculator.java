package BryceImages.Rendering;

import java.awt.Color;
import java.awt.Dimension;

import BryceMath.Calculations.Colors;

/*
 * Color Calculator
 * Written by Bryce Summers 6/29/2012
 * 
 * Purpose: Provides a standard function for creating images from mathematical formulas.
 * 			Think of a Color Calculator as a Vector image file interface.
 */

public abstract class ColorCalculator implements Cloneable
{
	
	//--Local variables
	protected int room_width;
	protected int room_height;
	public Dimension dim;
	
	// Default Anti Aliasing is set to 1X.
	protected int antiAliasing = 1;
	
	// -- Constructor
	public ColorCalculator(Dimension dim)
	{
		// Assign the local image dimension variables correctly
		room_width  = (int) dim.getWidth();
		room_height = (int) dim.getHeight();
		
		this.dim = dim;
	}

	public ColorCalculator(int width, int height)
	{		
		// Assign the local image dimension variables correctly
		room_width  = width;
		room_height = height;
		
		dim = new Dimension(width, height);
	}
	
	// Allow these color calculator objects to be cloneable for multithreading in the Brenderer.
	public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch( CloneNotSupportedException e )
        {
            return null;
        }
    } 

	// The abstract function that will be implemented by image definition subclasses.
	public abstract Color getColor(double x, double y);
	
	//--Function that returns this Color Calculator's current Anti Aliasing value.
	public int getAntiAliasing()
	{		
		return antiAliasing;
	}
	
	//--Function that returns the width of this Color Calculator's mathematical space.
	public int getWidth()
	{
		return room_width;
	}
	
	//--Function that returns the height of this Color Calculator's mathematical space.
	public int getHeight()
	{
		return room_height;
	}
	
	// -- Useful Color creation Functions.
	
	public Color weightedAverageColor(Color c1, Color c2, double percentage)
	{
		return Colors.weightedAverageColor(c1, c2, percentage);
	}
	
	public static Color Color_hsv(double h, double s,double v)
	{
		return Colors.Color_hsv(h, s, v);
	}
	
	public static Color Color_hsv(double h, double s, double v, double a)
	{
		return Colors.Color_hsv(h, s, v, a);
	}

	public int getAliasingThreshold()
	{
		return 30;
	}
	
}