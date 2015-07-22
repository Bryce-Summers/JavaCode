package Project_PhotonTracer2D;

import java.awt.Color;

import Components.photonColor;
import Components.photonIrradiance;


/*
 * Stores Irradiance Values.
 * Written by Bryce Summers on 7/16/2015.
 */


public class IrradianceCache
{
	
	// Row major ordered.
	photonIrradiance[][] values;
	
	// Stores the maximum values for light normalization.
	photonIrradiance maximums;
	
	public IrradianceCache(int w, int h)
	{
		values = new photonIrradiance[h + 1][w + 1];
		
		for(int r = 0; r <= h; r++)
		for(int c = 0; c <= w; c++)
		{
			values[r][c] = new photonIrradiance();
		}
		
		maximums = new photonIrradiance();
	}
	
	public void addIrradiance(int x, int y, photonColor color, double scalar, boolean invert)
	{
		// Swap values if they are inverted.
		if(invert)
		{
			int temp = x;
			x = y;
			y = temp;
		}
		
		photonIrradiance val = values[y][x];
		
		// It is best to keep floating point numbers low.
		// We only care about the relative intensities for the irradiance cache.
		// We use the magic number to achieve our numerical goals.
		color.addScaledTo(val, scalar*.01);
				
		maximums.maximize(val);
	}

	public Color getColor(double x, double y)
	{
		int x_int = (int)x;
		int y_int = (int)y;

		double per_x_c = x - x_int;
		double per_y_c = y - y_int;

		double per_x = 1 - per_x_c;
		double per_y = 1 - per_y_c;

		photonColor radx0y0 = getPhotonColor(x_int,     y_int);
		photonColor radx1y0 = getPhotonColor(x_int + 1, y_int);
		photonColor radx0y1 = getPhotonColor(x_int,     y_int + 1);
		photonColor radx1y1 = getPhotonColor(x_int + 1, y_int);


		photonColor result = radx0y0.mult(per_x*per_y)
							 .add(radx1y0.mult(per_x_c*per_y))
							 .add(radx0y1.mult(per_x*per_y_c))
							 .add(radx1y1.mult(per_x_c*per_y_c));
		
		return result.toColor();
	}

	private photonColor getPhotonColor(int x, int y)
	{
		photonIrradiance irradiance = values[y][x];
		double red   = irradiance.red / maximums.red;
		double green = irradiance.green / maximums.green;
		double blue  = irradiance.blue / maximums.blue;
		
		return new photonColor(new photonIrradiance(red, green, blue));
	}
	
	// scale by 2 will increase the intensities of the colors by 2.
	// etc.
	public void scale_exposure(double scalar)
	{
		maximums.red /= scalar;
		maximums.green /= scalar;
		maximums.blue /= scalar;
	}
	
}