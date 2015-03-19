package BryceImages.ColorCalculators.RayMarching.ArtPresentation;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.functions.Interpolator;


// Renders Functions beautifully to the Screen.
// Written by Bryce Summers on 2 - 13 - 2014.

public class ccFunction extends ColorCalculator
{

	public ccFunction(int width, int height)
	{
		super(width, height);
		antiAliasing = 3;
	}
	
	public ccFunction(Dimension dim)
	{
		super(dim);
		antiAliasing = 3;
	}
	
	
	double line_w = .005;

	@Override
	public Color getColor(double x, double y)
	{
		
		double view_size = (room_width + line_w*2);
		
		x = x/view_size + line_w;
		y = (room_width - y)/view_size + line_w;

		// White is transparent and 100.
		// Black is opaque and 0.
		double val = val(x, y);
		
		return Color_hsv(0, 0, val, 100 - val);
	}
	
	// Returns the value of the output color.
	// Computes smooth lines to represent the 1 dimensional function f.
	private double val(double x, double y)
	{
	
		double y_new = g(x);
		double dydx = (g(x + .0001) - y_new) / .0001;
		

		
		if(Math.abs(y - y_new) < line_w + Math.abs(dydx)*line_w)
		{
			return 0;
		}
		
		return 100;
			
	}
	
	Interpolator iterp = new Interpolator(Interpolator.TRIG);
	
	// applies a transformation to the basic function x;
	private double g(double x)
	{
		return f(x);
		//return Math.pow(f(x), 2);
		//return Math.pow(f(x), .3);
		//return Math.pow(f(x), .1);
	}
	
	// Specifies a function.
	private double f(double x)
	{
		//return x;
		//return iterp.eval(x);
		
		/*
		double x_2 = x*x;
		return 3*x_2 - 2*x_2*x;
		*/
		
		return .5*Math.cos(x*30)*Math.sin(x*20) + .5;
	}
	
	


	
	

}
