package Game_Engine.GUI.Sprites;

import static BryceMath.Calculations.MathB.abs;
import static BryceMath.Calculations.MathB.distance;

import java.awt.Color;

import BryceImages.Rendering.ColorCalculator;

public class ccGui_corner extends ColorCalculator
{

	/*
	 * Corners.
	 * Updated by Bryce Summers 1 - 13 - 2013.
	 * Purpose: Provides corners that can connect horizontal building blocks to vertical building blocks.
	 * 1 - 13 - 2013: Remove lost of floating point errors.
	 */
	
	// - Local data.
	
	public static Color color = Color.red;
	
	public ccGui_corner(int width, int height)
	{
		super(width, height);
		antiAliasing = 4;
	}

	@Override
	public Color getColor(double x, double y)
	{

		x -= room_width/2.0;
		y -= room_height/2.0;
		
		double dist = distance(0, 0, x, y);
		
		dist = dist - 1.0*room_height*3/8;
		
		if(abs(dist) >= 1.0*room_height/8)
		{
			return Color_hsv(0,0,0,0);
		}
		
		double val = dist*4/room_height;
		val = val + .53;
		
		
		return StyleSpec.getBorderColor(1 - val);
	}
	
}