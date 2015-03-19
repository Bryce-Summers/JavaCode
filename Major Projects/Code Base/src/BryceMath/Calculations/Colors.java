package BryceMath.Calculations;

import java.awt.Color;


/*
 * Colors class.
 * 
 * Written by Bryce Summers on 6 - 6 - 2013
 */

public class Colors
{
	// A nifty blue header color.
	public static Color C_BLUE_HEADING = Colors.Color_hsv(180, 20, 97);
	public static Color C_DIVIDER = Colors.Color_hsv(0, 0, 90);

	// A nifty neon red wrong like color
	public static Color C_ERROR = Colors.Color_hsv(0, 70, 97);
	
	// Completely transparent.
	public static Color C_CLEAR = Colors.Color_hsv(0,0,0,0);
	
	public static Color C_YELLOW = Color_hsv(51, 68, 100);
	
	// Practical Shades of Grey.
	public static Color C_GRAY1 = Color_hsv(0, 0, 99);
	public static Color C_GRAY2 = Color_hsv(0, 0, 90);
	public static Color C_GRAY3 = Color_hsv(0, 0, 85);
	
	public static Color C_RED   = Color_hsv(0, 80, 100);
	public static Color C_GREEN = Color_hsv(120, 80, 100);
	
	// -- Color Math. 
	
	// Includes alpha values.
	public static Color Color_hsv(double h, double s, double v, double a)
	{
		/* 
		 * Bound the Alpha Value. (Although it might be better to just
		 * require the user to provide already in bound alpha values to
		 * maintain good continuity.)
		 */
		 
		a = a%101;
		if(a<0){a += 101;}
		a = Math.min(a,100);
		a = a / 100 * 255;
		
		Color temp = Color_hsv(h,s,v);
		return new Color(temp.getRed(),temp.getGreen(),temp.getBlue(),(int)a);
	}
	
	public static Color Color_hsv(double h, double s,double v)
	{
		double r, g, b;
		int i;
		double f, p, q, t;
		
		// Use modular arithmetic to bound the 2 input values.
		
		// Hue.
		h = h%360;
		if(h<0){h += 360;}
		
		// Saturation.
		s = s%101;
		if(s<0){s += 101;}
		
		// Value.
		v = v%101;
		if(v<0){v += 101;}
		
		h = Math.min(h,360);
		s = Math.min(s,100);
		v = Math.min(v,100);
 	 	 
		// We accept saturation and value arguments from 0 to 100 because that's
		// how Photoshop represents those values. Internally, however, the
		// saturation and value are calculated from a range of 0 to 1. We make
		// That conversion here.
		s /= 100;
		v /= 100;
	 
		if(s == 0)
		{
			// Achromatic (grey)
			r = g = b = v;
			return new Color((int)(r * 255),(int)(g * 255), (int)(b * 255));
		}
	 
		h /= 60; // sector 0 to 5
		i = (int) Math.floor(h);
		f = h - i; // factorial part of h
		p = v * (1 - s);
		q = v * (1 - s * f);
		t = v * (1 - s * (1 - f));
	 
		switch(i)
		{
			case 0:
				r = v;
				g = t;
				b = p;
				break;
	 
			case 1:
				r = q;
				g = v;
				b = p;
				break;
	 
			case 2:
				r = p;
				g = v;
				b = t;
				break;
	 
			case 3:
				r = p;
				g = q;
				b = v;
				break;
	 
			case 4:
				r = t;
				g = p;
				b = v;
				break;
	 
			default: // case 5:
				r = v;
				g = p;
				b = q;
		}
	 
		return new Color((int)(r * 255),(int)(g * 255),(int)(b * 255));
	}
	
	public static Color weightedAverageColor(Color c1, Color c2, double pC2)
	{
		if(pC2 > 1)
		{
			pC2 = 1.0;
		}
		
		if(pC2 < 0)
		{
			pC2 = 0.0;
		}
		
		// Percent C2 (pC2) is an input.
		double pC1 = 1 - pC2;// Percent C2;
				
		double red 		= c1.getRed()	*pC1 + c2.getRed()	*pC2;
		double green 	= c1.getGreen()	*pC1 + c2.getGreen()*pC2;
		double blue 	= c1.getBlue()	*pC1 + c2.getBlue()	*pC2;
		
		double alpha 	= c1.getAlpha()	*pC1 + c2.getAlpha()*pC2;

		try
		{
			return new Color( (int)red,(int)green, (int)blue, (int) alpha );
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("Red = " + red);
			System.out.println("pC1 = " + pC1);
			System.out.println("pC2 = " + pC2);
			e.printStackTrace();
			throw new Error("EXIT");
		}

	}
	
	// -- Bit twiddling functions for color integers.
	// These should be more efficient than mallocing new Color objects.
	
	// Returns red component of a color integer. 
	public static int rgbToRed(int color)
	{
		return (color >> 16) &0xFF;
	}
	
	public static int rgbToGreen(int color)
	{
		return (color >> 8) &0xFF;
	}
	
	public static int rgbToBlue(int color)
	{
		return color & 0xFF;
	}
	
	public static int rgbToAlpha(int color)
	{
		return (color >> 24) & 0xFF;
	}
	
	public static int makeRgb(int red, int green, int blue)
	{
		int rgb = red;
		rgb = (rgb << 8) + green;
		rgb = (rgb << 8) + blue;
		
		return rgb;
	}

	public static Color color_random()
	{
		return new Color((int)(Math.random()*255), ((int)Math.random()*255),((int) Math.random()*255));
	}
}

