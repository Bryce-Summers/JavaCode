package Project.Editor.sprites;

import java.awt.Color;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Colors;
import BryceMath.Calculations.MathB;

public class ccMover extends ColorCalculator
{

	final int hue;
	
	public ccMover(int width, int height, int index)
	{
		super(width, height);
		antiAliasing = 4;
		
		hue = index*72;
	}

	@Override
	public Color getColor(double x, double y)
	{
		double dist = MathB.distance(x, y, getWidth()/2, getHeight()/2);
		
		double radius = getWidth()/3;
		
		if(dist > radius)
		{
			return Colors.C_CLEAR;
		}
		
		return Colors.Color_hsv(hue, 100, 100.0 - 100.0*Math.pow(dist/radius, 4));
	}

}
