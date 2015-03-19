package BryceMath.DoubleMath;

import java.awt.Color;

public class ColorB extends Vector
{

	public ColorB(Color c)
	{
		super(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);
	}
	
	public Color toColor()
	{
		int r = bound(get(0)*255, 0, 255);
		int g = bound(get(1)*255, 0, 255);
		int b = bound(get(2)*255, 0, 255);
		
		return new Color(r, g, b);
	}

	int bound(double val, int lower, int upper)
	{
		return Math.min(Math.max(lower, (int)val), upper);
	}
}
