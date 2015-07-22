package Components;

import java.awt.Color;

/*
 * Special Colors used for representing ray tracing photon intensities.
 * 
 * These colors are represented by intensity percentage values between 0 and 1.0 
 * that can be multiplied and added together along with being clamped in ways that 
 * keep the values in range and are used in the standard ray tracing equations in the standard literature.
 * 
 * Written on 12 - 27 - 2014.
 * 
 * 
 */

public class photonColor
{

	// A photon with no light.
	public final static photonColor BLACK = new photonColor(0.0, 0.0, 0.0);
	public static final photonColor WHITE = new photonColor(1.0, 1.0, 1.0);;
	
	
	// -- Private variables.
	public double red, green, blue;
	
	// Converts from photon Colors to General java colors.
	
	public photonColor(Color color)
	{
		red   = color.getRed()/255.0;
		green = color.getGreen()/255.0;
		blue  = color.getBlue()/255.0;
	}
	
	// Create from color space integers.
	public photonColor(int red, int green, int blue)
	{
		this.red = red/255.0;
		this.green = green/255.0;
		this.blue = blue/255.0;
	}
	
	public photonColor(photonIrradiance input)
	{
		red   = input.red;
		green = input.green;
		blue  = input.blue;
	}
	
	private photonColor(double r, double g,  double b)
	{
		red = r;
		green = g;
		blue = b;
		
		if(red > 100000 || green > 10000 || blue > 10000)
		{
			//throw new Error("Oh Dear");
		}
	}
	
	// Performs clamping.
	public Color toColor()
	{
		int r = (int) (Math.min(red, 1.0)*255);
		int g = (int) (Math.min(green, 1.0)*255);
		int b = (int) (Math.min(blue, 1.0)*255);
		
		r = Math.max(0, r);
		g = Math.max(0, g);
		b = Math.max(0, b);
		
		return new Color(r, g, b);
	}
	
	public photonColor mult(double scalar)
	{
		double r = red  *scalar;
		double b = blue *scalar;
		double g = green*scalar;
		
		return new photonColor(r, g, b);
	}
	
	public photonColor mult(photonColor other)
	{
		double r = red  *other.red;
		double b = blue *other.blue;
		double g = green*other.green;
		
		return new photonColor(r, g, b);
	}
	
	// Adds the intensities of each of the input photons to this photon.
	public photonColor add(photonColor ... others)
	{
		double r = red;
		double g = green;
		double b = blue;
		
		for(photonColor other : others)
		{
			r += other.red;
			g += other.green;
			b += other.blue;
		}
		
		return new photonColor(r, g, b);
	}
	
	public photonColor sub(photonColor ... others)
	{
		double r = red;
		double g = green;
		double b = blue;
		
		for(photonColor other : others)
		{
			r -= other.red;
			g -= other.green;
			b -= other.blue;
		}
		
		return new photonColor(r, g, b);		
	}
	
	public Object clone()
	{
		return new photonColor(red, green, blue);
	}
	
	public String toString()
	{
		return "Red = " + red + ", Green = " + green + " Blue = " + blue;
	}

	public boolean nonZero()
	{
		return red > 0 || green > 0 || blue > 0;
	}

	public void clamp()
	{
		red   = Math.min(red, 1.0);
		green = Math.min(green, 1.0);
		blue  = Math.min(blue, 1.0);
		
	}

	public void addTo(photonIrradiance irradiance)
	{
		irradiance.red   += red;
		irradiance.green += green;
		irradiance.blue  += blue;
	}
	
	public void addScaledTo(photonIrradiance irradiance, double scalar)
	{
		irradiance.red   += red*scalar;
		irradiance.green += green*scalar;
		irradiance.blue  += blue*scalar;
	}
	
	
	public photonIrradiance toIrradiance()
	{
		photonIrradiance output = new photonIrradiance();
		
		output.red   = red;
		output.green = green;
		output.blue  = blue;

		return output;
	}

	public double getMagnitude()
	{
		return Math.sqrt(red*red + green*green + blue*blue);
	}
	
}
