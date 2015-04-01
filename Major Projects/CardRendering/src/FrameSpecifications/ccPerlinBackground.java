package FrameSpecifications;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.ccPerlinNoise;
import BryceImages.Rendering.ColorCalculator;
import BryceMath.Geometry.Rectangle;

public class ccPerlinBackground extends ColorCalculator
{

	ccPerlinNoise perlin;
	final int hue, sat;
		
	public ccPerlinBackground(Dimension dim, int hue, int sat)
	{
		super(dim);
		
		int w = (int) dim.getWidth();
		int h = (int) dim.getHeight();
		
		perlin = new ccPerlinNoise(w, h, 0, 1/4.0, 3);
		
		this.hue = hue;
		this.sat = sat;
	
	}
	
	public ccPerlinBackground(int w, int h, int hue, int sat)
	{
		super(w, h);
		
		perlin     = new ccPerlinNoise(w, h, 0, 3/4.0, 3);
		
		this.hue = hue;
		this.sat = sat;
	}

	@Override
	public Color getColor(double x, double y)
	{
		double val = perlin.getVal(x, y);

		return Color_hsv(hue, sat, 75 + 25*val);
	}

}
