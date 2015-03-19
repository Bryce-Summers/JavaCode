package BryceImages.ColorCalculators.RayMarching.ArtPresentation;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class ccBoring extends ColorCalculator
{

	public ccBoring(int width, int height)
	{
		super(width, height);
		antiAliasing = 4;
	}

	public ccBoring(Dimension dim)
	{
		super(dim);
		antiAliasing = 4;
	}

	@Override
	public Color getColor(double x, double y)
	{
		if(y > x){return Color.BLACK;}
		return Color.BLUE;
	}

}
