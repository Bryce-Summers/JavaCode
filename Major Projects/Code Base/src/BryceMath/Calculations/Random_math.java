package BryceMath.Calculations;

import java.util.Random;


/*
 * Random class. Provides functionality for dealing with Random numbers.
 * Written on 3 - 10 - 2013 by Bryce Summers
 * 
 * This class most likely consists of purely static methods.
 * 
 * Update : 5 - 15 - 2014.
 * Added some rand_int functionality. Improved the style a bit.
 */

/* FIXME: Perhaps make this class multithreading compatible.
 * Perhaps make the random number generator non static to allow for multiple
 * distinct consistent independent random processes to be carried out.
 */

public class Random_math
{
	
	public static Random random     = new Random();
	public static final long mySeed = (long) random(Integer.MAX_VALUE);

	public static double random()
	{
		return random.nextDouble();
	}
	
	public static double random(int n)
	{		
		return n*random.nextDouble();
	}
	
	public static  double random(double n)
	{
		return n*random.nextDouble();
	}
	
	//returns a random double that is in lower inclusive, upper exclusive.
	public static double random(int f, int l)
	{
		return (l - f)*random.nextDouble() + f  ;
	}
	
	//returns a random double that is in lower inclusive, upper exclusive.
	public static double random(double f, double l)
	{
		return (l - f)*random.nextDouble() + f  ;
	}
	
	//returns a random double that is in lower inclusive, upper exclusive.
	public static int rand_int(int f, int l)
	{
		return (int)random(f, l);
	}
	
	
	// Direct the random number generator to go to a defined consistent number.
	public static void random_set_seed()
	{
		random.setSeed(mySeed);
	}
	
	public static  void random_set_seed(int seed)
	{
		random.setSeed(seed);
	}
	
	/* produces uniform points in a given circle.
	 * This implementation finds uniform square points and then throws them out until one is in the circle.
	 */
	public static double[] randomPointInCircle(double center_x, double center_y, double radius)
	{
		double x, y;
		
		double x_min = center_x - radius;
		double x_max = center_x + radius;
		double y_min = center_y - radius;
		double y_max = center_y + radius;
		
		// Find uniform square points.
		do
		{
			x = random(x_min, x_max);
			y = random(y_min, y_max);
		} // Loop while the points are not in the circle.
		while(MathB.distance(center_x, center_y, x, y) > radius);
		
		
		double[] output = new double[2];
		output[0] = x;
		output[1] = y;
		
		return output;
	}
	
	
}
