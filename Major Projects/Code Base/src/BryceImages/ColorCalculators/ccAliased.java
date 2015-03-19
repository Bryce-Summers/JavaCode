package BryceImages.ColorCalculators;
import static BryceMath.Calculations.MathB.cos;
import static BryceMath.Calculations.MathB.distance;
import static BryceMath.Calculations.MathB.sin;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

/*
 * A test of a renderer's Anti Aliasing capabilities.
 * Written by Bryce Summers, 12 - 22 - 2012.
 * 
 * Mantra: Eliminate the Jagged Pixels!
 */

public class ccAliased extends ColorCalculator {

	public ccAliased(Dimension dim)
	{
		super(dim);
		iVars();

	}
	public ccAliased(int w, int h)
	{
		super(w,h);
		iVars();
	}
	
	public void iVars()
	{
		// 16x.
		antiAliasing = 9;
	}

	public Color getColor(double x, double y)
	{
		x -= room_width/2;
		y -= room_height/2;
		
		x *= 1000;
		y *= 1000;
		
		// Density of rings.
		double d1 = .5;
		
		// Density of light color shifts.
		double d2 = .5;
		
		double dist = distance(0,0,x,y);
		
		// Exploits the density of radians and colors all hues.
		return Color_hsv(
							180*sin(d2*dist),		// Hue.
							100,						// Saturation.
							cos(d1*dist),			// Value.
							50 + 50*sin(d2*dist));	// Alpha.
	}
	
	

}
