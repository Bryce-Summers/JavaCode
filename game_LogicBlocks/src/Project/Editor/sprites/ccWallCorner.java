package Project.Editor.sprites;

import static BryceMath.Calculations.MathB.min;
import static BryceMath.Calculations.Random_math.random;

import java.awt.Color;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Colors;

/*
 * Background Square for my teleporter game.
 */

public class ccWallCorner extends ColorCalculator
{

	// Size of all of the grass.
	int size;
	
	// Size of one Game Square.
	double block;
	
	final Color GRASS1 = Colors.Color_hsv(112, 77, 66);
	
	final int dir;
	
	public ccWallCorner(int width, int height, int dir)
	{
		super(width, height);
		size = width;
		block = size/2.0;
		
		antiAliasing = 3;
		
		this.dir = dir;
	}

	
	// Provides different configurations for types of grass.
	@Override
	public Color getColor(double x, double y)
	{
		int w = getWidth();
		int h = getHeight();
		int size = w/3;
		
		int cx, cy;
		
		// Arbitrary Ordering for the corners.
		switch(dir)
		{
			default:
			case 3:// Bottom Right. 
				cx = w;
				cy = h;
				break;
			case 1:// Top Right.
				cx = w;
				cy = 0;
				break;
			case 0:// Top left.
				cx = 0;
				cy = 0;
				break;
			case 2:// Bottom left.
				cx = 0;
				cy = h;
				break;
		}
		
		if(BryceMath.sqrDistance(x, y, cx, cy) < size*size)
		{
			return randomColor(28, 39, 30 + lineColor(x, y));
		}
		
		return Colors.C_CLEAR;
	}
	
	private int lineColor(double x, double y)
	{
		double v1 = adist(x, room_width);
		double v2 = adist(y, room_height);
		double v3 = adist(x, 0);
		double v4 = adist(y, 0);

		
		double seed = min(v1, v2, v3, v4);
		

		
		double output = seed; 
		return Math.min((int)output, 10);
	}
	
	private double adist(double val1, double val2)
	{
		return Math.abs(val1 - val2);
	}

	private Color randomColor(int hue, int sat, int val)
	{
		return Colors.Color_hsv(hue + random(-5, 5), sat + random(-5, 5), val + random(-5, 5));
	}

}
