package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

// Perlin Noise : use any screen dimensions that are divisible by size.


import static BryceImages.ColorCalculators.RayMarching.BryceMath.abs;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.pow;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sqr;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.v;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.Rendering.ColorCalculator;

public class ccFakePerlinNoise extends ColorCalculator
{
	// -- Private Data.
	Vector v[][];
	int size = 30;
	
	public ccFakePerlinNoise(Dimension dim)
	{
		super(dim);
		iData();
	}

	public ccFakePerlinNoise(int width, int height)
	{
		super(width, height);
		iData();
	}
	
	private void iData()
	{
		BryceMath m = new BryceMath();
		
		m.random_set_seed(5);
		v = new Vector[room_width/size+2][room_width/size+2];
		for(int c = 0; c <= room_width /size + 1; c++)
		for(int r = 0; r <= room_height/size + 1; r++)
		{	
			v[c][r] = v(m.random(2) - 1, m.random(2) - 1, 0).norm();
		}
		antiAliasing = 3;
	}

	@Override
	public Color getColor(double x, double y) {

		y += 3600;
		
		x = abs(x) % room_width;
		y = abs(y) % room_height;
		
		Vector z = v(x, y, 0);
		Vector d1, d2, d3, d4;
		
		Vector v1, v2, v3, v4;
		int x1, x2, y1, y2;
		int xfactor = size;
		int yfactor = size;
		x1 = (int)(x)/(xfactor);
		x2 = x1 + 1;
		y1 = (int)(y)/(yfactor);
		y2 = y1 + 1;
				
		v1 = v[x1][y1];
		v2 = v[x2][y1];
		v3 = v[x1][y2];
		v4 = v[x2][y2];
		
		d1 = v(x1*xfactor, y1*yfactor, 0).sub(z).norm();
		d2 = v(x2*xfactor, y1*yfactor, 0).sub(z).norm();
		d3 = v(x1*xfactor, y2*yfactor, 0).sub(z).norm();
		d4 = v(x2*xfactor, y2*yfactor, 0).sub(z).norm();
		
		double s = d1.dot(v1);
		double t = d2.dot(v2);
		double u = d3.dot(v3);
		double v = d4.dot(v4);
		
		
		// Compute average of these functions.
		double xWeight = s + function(t - s) + u + function(v - u);
		double yWeight = s + function(u - s) + t + function(v - t);
		double val = (xWeight + yWeight)/2;
		
		return Color_hsv(0,0,50 + 50*cos(val) );
		
		//return null;
	}
	
	double function(double in)
	{
		return 3*sqr(in) - 2*pow(in, 3);
	}

}
