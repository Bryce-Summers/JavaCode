package BryceImages.ColorCalculators.RayMarching.Textures;

//cc2dStripes texture.
// Written by Bryce Summers 7/23/2012 based on a formula developed the week before.
// This is originally made for the floor of my first outstanding reflection sphere test.

import static BryceImages.ColorCalculators.RayMarching.BryceMath.abs;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class texture_stripes extends ColorCalculator
{

	public texture_stripes(Dimension dim) {
		super(dim);
		// TODO Auto-generated constructor stub
	}

	public texture_stripes(int width, int height)
	{
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Color getColor(double x, double y)
	{
		
		x = abs(x)+.5;
		
		if(x%2<1)
			return Color_hsv(y,100,100);
				
		return Color_hsv(y+180,100,100);

	}

}
