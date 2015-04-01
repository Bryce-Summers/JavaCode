package FrameSpecifications;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.ccPerlinNoise;
import BryceImages.Rendering.ColorCalculator;
import BryceMath.Geometry.Rectangle;

public class ccDinnerTableItem extends ColorCalculator
{

	ccPerlinNoise perlin;
	
	Rectangle non_border;
	
	int value = 72;
	int hue = 41;
	
	public ccDinnerTableItem(Dimension dim, int border_size)
	{
		super(dim);
		
		int w = (int) dim.getWidth();
		int h = (int) dim.getHeight();
		
		perlin = new ccPerlinNoise(w, h, 0, 1/4.0, 3);
		
		non_border = new Rectangle(border_size, border_size, w - border_size*2, h - border_size*2);
		
	}
	
	public ccDinnerTableItem(int w, int h, int border_size)
	{
		super(w, h);
		
		perlin     = new ccPerlinNoise(w, h, 0, 3/4.0, 3);
		non_border = new Rectangle(border_size, border_size, w - border_size*2, h - border_size*2);
	}
	
	public ccDinnerTableItem(int w, int h, int border_size, int val, int hue)
	{
		super(w, h);
		
		perlin     = new ccPerlinNoise(w, h, 0, 3/4.0, 3);
		non_border = new Rectangle(border_size, border_size, w - border_size*2, h - border_size*2);
		this.value = val;
		this.hue = hue;
	}

	@Override
	public Color getColor(double x, double y)
	{
		double val = perlin.getVal(x, y);
		
		if(non_border.containsPoint((int)x, (int)y))
		{
			return Color_hsv(hue, 100*val, value);			
		}	
		
		return Color_hsv(41, 100, 100*val);
		
	}

}
