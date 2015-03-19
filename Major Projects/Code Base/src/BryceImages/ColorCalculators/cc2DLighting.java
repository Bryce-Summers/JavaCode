package BryceImages.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.DoubleMath.Vector;

public class cc2DLighting extends ColorCalculator
{


	public cc2DLighting(Dimension dim)
	{
		super(dim);
	}


	public cc2DLighting(int width, int height)
	{
		super(width, height);
	}

	@Override
	public Color getColor(double x, double y)
	{
		Color blue = new Color(200, 200, 255);
		Vector in = new Vector(x, y);
		Vector eye1 = new Vector(getWidth()/3, getHeight()/3);
		Vector eye2 = new Vector(getWidth()*2/3, getHeight()/3);
		Vector center = new Vector(getWidth()/2, getHeight()/2);
		Vector mouth = new Vector(getWidth()/2, getHeight()*.6);
		
		Color c1 = getPointColor(eye1, in, blue, 1.0);
		Color c2 = getPointColor(eye2, in, blue, 1.0);
		Color c3 = getArcColor(center, getHeight()/2.1, in, .001);
		Color cMouth = getPointColor(mouth, in, new Color(255, 100, 100), .2);
		
		Color green = new Color(10, 10, 10);
		Color innerMouth = getPointColor(mouth, in, new Color(200, 100, 100), .1);
		Color innerEye1 = getPointColor(eye1, in, green, .1);
		Color innerEye2 = getPointColor(eye2, in, green, .1);
		
		
		Color output = max_color(c1, c2, cMouth);
		//return output;
		return sub_color(output, innerMouth, innerEye1, innerEye2);

	}
	
	Color max_color(Color ... colors)
	{
		int r = 0, g = 0, b = 0;
		
		for(Color c: colors)
		{
			r = Math.max(r, c.getRed());
			g = Math.max(g, c.getGreen());
			b = Math.max(b, c.getBlue());
		}
		
		return new Color(r, g, b);
	}
	
	Color sub_color(Color initial, Color ... colors)
	{
		int r = initial.getRed(), g = initial.getBlue(), b = initial.getBlue();
		
		for(Color c: colors)
		{
			r -= c.getRed();
			g -= c.getGreen();
			b -= c.getBlue();
		}
		
		r = Math.max(0, r);
		g = Math.max(0, g);
		b = Math.max(0, b);
		
		return new Color(r, g, b);
		
	}
	
	// Get a color in the light gradiant of a point.
	Color getPointColor(Vector point, Vector in, Color c, double intensity)
	{
		
		Vector diff = in.sub(point);
		
		double mag = diff.mag();
		
		return Color_rgb(attenutate(c.getRed()*intensity, mag),
						 attenutate(c.getGreen()*intensity, mag),
						 attenutate(c.getBlue()*intensity, mag));
	}
	
	// Get a color in the light gradient of an arc.
	Color getArcColor(Vector center, double dist, Vector in, double intensity)
	{
		Vector onArc = in.sub(center).norm().mult(dist).add(center);
		double mag = onArc.sub(in).mag();
		
		return Color_rgb(attenutate(255*intensity, mag), attenutate(100*intensity, mag), attenutate(100*intensity, mag));
	}
	
	Color Color_rgb(double r, double g, double b)
	{
		return new Color(bound(r, 0, 255), bound(g, 0, 255), bound(b, 0, 255));
	}

	int bound(double val, int lower, int upper)
	{
		return Math.min(Math.max(lower, (int)val), upper);
	}
	
	double attenutate(double val, double distance)
	{
		distance /= 200.0;
		return val/(distance*distance);
	}
	
}
