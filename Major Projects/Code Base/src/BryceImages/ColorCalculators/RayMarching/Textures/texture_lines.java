package BryceImages.ColorCalculators.RayMarching.Textures;

//cc2dStripes texture.
// Written by Bryce Summers 7/23/2012 based on a formula developed the week before.
// This is originally made for the floor of my first outstanding reflection sphere test.

import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class texture_lines extends ColorCalculator
{

	public texture_lines(Dimension dim)
	{
		super(dim);
		
		antiAliasing = 3;
		
	}

	public texture_lines(int width, int height)
	{
		super(width, height);
		
		antiAliasing = 3;
	}

	//private static double line_w = 1;
	
	@Override
	public Color getColor(double x, double y)
	{
		
		double dist = distance(0, 0, x, y);
		
		if(dist % 1 < .1)
		{
			return Color.white;
		}
				
		return Color.black;

	}

}
