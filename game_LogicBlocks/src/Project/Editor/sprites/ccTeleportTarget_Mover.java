package Project.Editor.sprites;

import java.awt.Color;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Colors;
import BryceMath.Calculations.MathB;

public class ccTeleportTarget_Mover extends ColorCalculator
{

	final int hue;

	public ccTeleportTarget_Mover(int width, int height, int hue)
	{
		super(width, height);
		
		this.hue = hue*76 + 30;
	}

	@Override
	public Color getColor(double x, double y)
	{
		double dist = MathB.distance(x, y, getWidth()/2, getHeight()/2);
		
		double radius = getWidth()/2;
		
		if(dist > radius)
		{
			return Colors.C_CLEAR;
		}
		
		return Colors.Color_hsv(hue, 100, 100*dist/radius);
	}

}
