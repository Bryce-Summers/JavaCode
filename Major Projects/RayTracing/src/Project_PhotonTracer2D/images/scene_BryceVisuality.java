package Project_PhotonTracer2D.images;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.MathB;
import BryceMath.DoubleMath.ColorB;

public class scene_BryceVisuality extends ColorCalculator
{

	public double width;
	public double height;
	
	public double center_x;
	public double center_y;

	public double divisions = 16;// = 16;
	public double offset = divisions/3;
	public double radius = divisions/4;//6;

	public scene_BryceVisuality(Dimension dim)
	{
		super(dim);
		width  = dim.getWidth();
		height = dim.getHeight();
		center_x = width/3;
		center_y = height/2;
	}

	@Override
	public Color getColor(double x, double y)
	{
		// Remove the butterflies.
		//x += .5;
		//y += .5;
		
		double max_val = 0;
		
		for(double x1 = x - divisions; x1 <= x + divisions; x1 += divisions)
		for(double y1 = y - divisions; y1 <= y + divisions; y1 += divisions)
		{
			if(x1 < 0)
			{
				continue;
			}
			if(y1 < 0)
			{
				continue;
			}
			
			double px = MathB.floor(x1/divisions)*divisions + divisions/2;
			double py = MathB.floor(y1/divisions)*divisions + divisions/2;
			double val = getFlow(px, py, x, y);
			max_val = combine(max_val, val);
		}
		
		return this.Color_hsv(Math.random()*360, 100, max_val*100, max_val*100);
	}
	
	/*
	 * (0, 2) --> (0, 1)
	 *	Let a and b be in the range (0, 2).
		
	 *	a = a/2
	 *	b = b/2
		
	 *	// a and b are now in the range (0, 1)
	 *	c = combine(a, b)
	 *	a = a*2
	 *	b = b*2
		
	 *	// a and b are now back to the range (0, 2)
		
		
		
	 *	(-1, 1) -> (0, 1)
	 *	Let a and b be in the range (0, 2).
	 *	a = (a + 1)/2
	 *	b = (b + 1)/2
		
	 *	c = combine(a, b)
		
	 *	a = a*2 - 1
	 *	b = b*2 - 1
	 */
	
	public double combine(double a, double b) // x is time.
	{
	   
	   double max = Math.max(a, b);
	   double min = Math.min(a, b);
	   
	   double diff = Math.abs(max - min);
	    
	   
	   // Elegantly smooth between the minimum and maximum value,
	   // keeping the derivatives smooth when the maximum function that
	   // was dominating crosses the minimum function. 
	   double smoothed_value = (diff*max + (1 - diff)*min);
	   
	   // We interpolated between contributing the raw min value when min is very low,
	   // and the interpolated value when min is higher.
	   // The idea is that we want to add very little contribution when min is very low,
	   // because then a function can be smoothly animated in without discontinuously influencing the original function from the onset.
	   smoothed_value = (1 - min)*min + min*smoothed_value;
	   
	   // The maximum value will dominate more when it is closer to 1.
	   return max + (1 - max)*smoothed_value;
	}

	
	// Get the float at pixel (x, y) looking at impression px, py.
	public double getFlow(double px, double py, double x, double y)
	{
		
		double angle = MathB.lineAngle(center_x, center_y, px, py);
		angle = MathB.rad(angle);
		//angle += Math.PI/2; // Curl.
		angle += Math.PI; // Curl.
		//angle = 0;
		
		double par_x = MathB.cos(angle) * offset;
		double par_y = -MathB.sin(angle) * offset; 
		double perp_x = -par_y;
		double perp_y =  par_x;
		
		double min_dist = 1000000;
	
		// The output grayscale value.
		double val = 0;
		
		for(double x1 = -1; x1 <= 1.01; x1 += 1.0 )
		for(double y1 = -1; y1 <= 1.01; y1 += 1.0 )
		{
			double cx = px + perp_x*x1 + par_x*y1;
			double cy = py + perp_y*x1 + par_y*y1;
			
			if(x1 != 0 && y1 != 0)
			{
				//cx = cx*.5 + px*.5;
				//cy = cy*.5 + py*.5;
			}
			
			double dist = MathB.distance(cx, cy, x, y);
		
			if(dist < min_dist)
			{
				min_dist = dist;
				val = 1.0 - MathB.min(dist / radius, 1);
				val /= (y1*-1 + 2);
			}
			
			
		}
		
		//val = 1.0 - val;
		
		// 360, 100, 100, 100
		return val;
		//return this.Color_hsv(0, 0, 0);
	}

}
