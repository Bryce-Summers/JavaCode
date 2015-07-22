package Project.Editor.sprites;

import java.awt.Color;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Colors;

public class ccTeleportTarget_teleporter extends ColorCalculator
{

	// -- Private Data.
	final int hue;
	
	public ccTeleportTarget_teleporter(int width, int height, int hue)
	{
		super(width, height);

		antiAliasing = 4;
		this.hue = hue*76 + 30;
	}

	@Override
	public Color getColor(double x, double y)
	{
		
		int w = getWidth();
		int h = getHeight();
		
		x -= w/2;
		y -= h/2;
		
		x = Math.abs(x);
		y = Math.abs(y);
		
		if(x > w/3)
		{
			return Colors.C_CLEAR;
		}
		
		if(y > h/3)
		{
			return Colors.C_CLEAR;
		}
		
		return Colors.Color_hsv(hue, 97, 97 - x/w*15 - y/h*30);
	}

}
