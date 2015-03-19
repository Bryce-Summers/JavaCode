package BryceImages.ColorCalculators;
import static BryceMath.Calculations.MathB.distance;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

/*
 * A test of a renderer's Anti Aliasing capabilities.
 * Written by Bryce Summers, 12 - 22 - 2012.
 * 
 * Mantra: Eliminate the Jagged Pixels!
 * Note: Circle should blend into clear, not black.
 */

public class ccCircle extends ColorCalculator {

	public ccCircle(Dimension dim)
	{
		super(dim);
		iVars();

	}
	public ccCircle(int w, int h)
	{
		super(w,h);
		iVars();
	}
	
	public void iVars()
	{
		// 16x.
		antiAliasing = 4;
	}

	public Color getColor(double x, double y)
	{
		// Shift coordinates to the center of the room.
		x -= room_width/2;
		y -= room_height/2;
		
		double dist = distance(0, 0, x, y);
		
		if(dist <= Math.min(room_width /4, room_height/4))
		{
			// A pleasant red color.
			return Color_hsv(0,80,100);
		}
		
		// Clear.
		return Color_hsv(0,0,0,0);
		
	}
	
	

}
