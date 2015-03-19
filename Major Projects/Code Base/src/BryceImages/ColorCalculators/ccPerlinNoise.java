package BryceImages.ColorCalculators;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.functions.Interpolator;


public class ccPerlinNoise extends ColorCalculator
{
	final int size = 100;
	double[][][] noise = new double[size][size][size];
	
	Interpolator interp = new Interpolator(Interpolator.TRIG);
	
	int time;
	double persistence = 1/4.0;
	int Number_Of_Octaves = 1;
	
	public ccPerlinNoise(Dimension dim, int time, double persistence, int octaves)
	{
		super(dim);
		antiAliasing = 3;
		generateNoise();
		
		this.time = time;
		this.persistence = persistence;
		Number_Of_Octaves = octaves;
	}

	public ccPerlinNoise(int width, int height, int time, double persistence, int octaves)
	{
		super(width, height);
		antiAliasing = 3;
		generateNoise();
		
		this.time = time;
		this.persistence = persistence;
		Number_Of_Octaves = octaves;
	}
	
	private void generateNoise()
	{
		Random random = new Random();
		
		random.setSeed(0);
		
		for(int a = 0; a < size; a++)
		for(int b = 0; b < size; b++)
		for(int c = 0; c < size; c++)
		{
			noise[a][b][c] = random.nextDouble();
		}
	}
	
	public void setTime(int time)
	{
		this.time = time;
	}
	
	// The function that returns the color.
	@Override
	public Color getColor(double x, double y)
	{
		return Color_hsv(0, 0, 100*getVal(x, y));
	}
	
	// REturns a value between 0 and 1.
	public double getVal(double x, double y)
	{
		x = Math.abs(x);
		y = Math.abs(y);
		double scale  = 1.0*size/room_width;
		
		double val = PerlinNoise_2D(x*scale, y*scale);

		//val = exp(val);
		//val = grain(val);
		//val*=2;
		
		return val;
	}
	
	public double grain(double val)
	{
		val *= 20;
	    val  = val - (int)val;
	    return val;
	}
	
	// CloudCover in (0, 1) = percentage of screen filled with clouds.
	// CloudSharpness in (0, 1);
	public double exp(double val)
	{
		double CloudCover = .5;
		double CloudSharpness = .01;
		
		double c = val - CloudCover;
		if(c < 0)
		{
			c = 0;
		}
			 
		double CloudDensity = 1.0 - (Math.pow(CloudSharpness, c)); 

		return CloudDensity;
	}

	// Seed Noise.
	double noise(int x, int y)
	{
		return noise(x, y, time);
	}
	
	double noise(int x, int y, int z)
	{
		
		int a = x % size;
		int b = y % size;
		int c = z % size;
		
		if(a < 0)
		{
			a = size + a;
		}
		
		if(b < 0)
		{
			b = size + b;
		}
		
		if(c < 0)
		{
			c = size + c;
		}
		
	
		return noise[a][b][c];
	}
	
	// Smooths the Noise out.
	double SmoothedNoise(int x, int y)
	{
		double corners = ( noise(x-1, y-1)+noise(x+1, y-1)+noise(x-1, y+1)+noise(x+1, y+1) ) / 16;
		double sides   = ( noise(x-1, y)  +noise(x+1, y)  +noise(x, y-1)  +noise(x, y+1) ) /  8;
		double center  =  noise(x, y) / 4;
		
		return corners + sides + center;
	}
	 
	double InterpolatedNoise_1(double x, double y)
	{
		int	integer_X = (int)x;
		double fractional_X = x - integer_X;

	    int integer_Y    = (int)y;
	    double fractional_Y = y - integer_Y;

	    double v1 = SmoothedNoise(integer_X,     integer_Y);
	    double v2 = SmoothedNoise(integer_X + 1, integer_Y);
	    double v3 = SmoothedNoise(integer_X,     integer_Y + 1);
	    double v4 = SmoothedNoise(integer_X + 1, integer_Y + 1);

	    double i1 = Interpolate(v1 , v2 , fractional_X);
	    double i2 = Interpolate(v3 , v4 , fractional_X);

	    return Interpolate(i1 , i2 , fractional_Y);

	}

	double Interpolate(double val1, double val2, double percent2)
	{
		double weight2 = interp.eval(percent2);
		
		double weight1 = 1 - weight2;
		
		return val1*weight1 + val2*weight2;
	}

	double PerlinNoise_2D(double x, double y)
	{
		/*
		double x_i = interp.eval(x / room_width);
		double y_i = interp.eval(y / room_height);
		
		double r1 = Interpolate(noise[0][0][0], noise[0][1][0], x_i);
		double r2 = Interpolate(noise[1][0][0], noise[1][1][0], x_i);
		
		if(true)
		return Interpolate(r1, r2, y_i);
		*/
		
		double total = 0; // Accumulator variable.
	    double p = persistence;
	    int n = Number_Of_Octaves;
	    
	    double amplitude = 1;
	    
	    for(int i = 0; i < n; i++)
	    {
	       int frequency = (int) Math.pow(2, i);
	       amplitude = Math.pow(p, i+1);

	       total = total + InterpolatedNoise_1(x * frequency, y * frequency) * amplitude;

	    }

	    // Evenly scale the geometric series values.
	    double scale = p*(1 - Math.pow(p, n))/(1 - p);
	    scale = 1.0 / scale;
	    
	    return total * scale;
	}
	
	
}
