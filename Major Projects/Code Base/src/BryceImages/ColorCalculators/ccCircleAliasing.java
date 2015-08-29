package BryceImages.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Colors;

public class ccCircleAliasing extends ColorCalculator
{
	
	public ccCircleAliasing(Dimension dim)
	{
		super(dim);
		antiAliasing = 36;
	}

	@Override
	public Color getColor(double x, double y)
	{
		int offset_x = 0;//getWidth()/2;
		int offset_y = getHeight()/2;
		
		double zoom_factor = .02445;//2.45;////18.53;
		
		// Offset First.
		x -= offset_x;
		y -= offset_y;
		
		// Zoom second.
		x /= zoom_factor;
		y /= zoom_factor;
		
		
		double val = 49* Math.sin(Math.toRadians((x*x + y*y)/5000)) + 50;
		
		val = Math.max(0,  Math.min(val, 100));
		
		return Colors.Color_hsv(0, 0, val);
	}

}
