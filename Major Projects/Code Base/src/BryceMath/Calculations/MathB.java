package BryceMath.Calculations;

// FIXME : This code should be cleaned up some day, because it is centuries old.
// ~ Bryce Summers transcribed this on 6 - 2 - 2013.
// Some refactoring done on 6 - 27 - 2013.

public class MathB
{
	
	// -- Constants
	final double pi = Math.PI;
	final double e  = Math.E;
	
	// -- Mathematical Helper functions.
		
	// Compute the distance between two 2 - dimensional points.
	public static double distance(double x1, double y1, double x2, double y2)
	{
		// Pythagorean formula!
		return sqrt(sqr(x1 - x2) + sqr(y1 - y2));
	}
	
	public static double sqrt(double input)
	{
		return Math.sqrt(input);
	}
	
	public static double sqr(double input)
	{
		
		return input*input;
	}
	
	// - Trigonometry
	
	// Radians
	public static double cos(double input)
	{
		return Math.cos(input);
	}
	
	// Radians.
	public static double sin(double input)
	{
		return Math.sin(input);
	}

	// positive x = 0, negative y = 90, negative x = 180, positive y = 270.
	public static double lineAngle(double x, double y, double centerX, double centerY){
		
		// The Java.math functions use different theta orientations than I do,
		// so reversing the x and y values is able to correctly fix this.
		// Note the y is also negated so that the orientation has east, north, west,
		// and south the same as on a computer screen.
		return (deg(Math.atan2(x - centerX, y - centerY)) + 270) % 360;
		
	}
	
	// Converts a number from radians to degrees.
	public static double deg(double radians)
	{
		return Math.toDegrees(radians);
	}
	
	// Converts a number from degrees to radians.
	public static double rad(double degrees)
	{
		return Math.toRadians(degrees);
	}
	
	// - Logarithms.
	
	public static double ln(double input)
	{
		return Math.log(input);
	}
	
	public static double log10(double input)
	{
		return Math.log10(input);
	}
	
	// Powers.
	public static double pow(double number, double p)
	{
		return Math.pow(number,  p);
	}
	
	//Finds the log to the base specified.
	public static double logA(double input,int base)
	{
		// Compute using the change of base rule.
		return ln(input)/ln(base);
	}	
	
	// - Rounding.
	
	public static long round(double input)
	{
		return Math.round(input);
	}
	
	public static int floor(double input)
	{
		return (int)input;
	}
	
	public static int ceil(double input)
	{
		return (int)(input + 1);
	}
	
	public static double min(double ... V)
	{
		double output = Double.MAX_VALUE;
		
		for(double d : V)
		{
			output = d < output ? d : output;
		}
		
		return output;
	}
	
	// -- Minimizing primitive functions.
	public static int min(int a, int b)
	{
		if(a < b)
		{
			return a;
		}
		return b;
	}
	
	public static double min(double a, double b)
	{
		if(a < b)
		{
			return a;
		}
		return b;
	}
	
	public static long min(long a, long b)
	{
		if(a < b)
		{
			return a;
		}
		return b;
	}
	
	public static short min(short a, short b)
	{
		if(a < b)
		{
			return a;
		}
		return b;
	}
	
	// -- Maximizing functions
	public static int max(int a, int b)
	{
		if(a > b)
		{
			return a;
		}
		return b;
	}
	
	public static double max(double a, double b)
	{
		if(a > b)
		{
			return a;
		}
		return b;
	}
	
	public static long max(long a, long b)
	{
		if(a > b)
		{
			return a;
		}
		return b;
	}
	
	public static short max(short a, short b)
	{
		if(a > b)
		{
			return a;
		}
		return b;
	}
	
	// -- Absolute value primitive functions.
	public static int abs(int n)
	{
		if(n < 0)
		{
			n *= -1;
		}
		return n;
	}
	
	public static double abs(double n )
	{
		if(n < 0)
		{
			n *= -1.0;
		}
		return n;
	}
	
	public static int sign(int n)
	{
		if(n == 0)
		{
			return 0;
		}
		
		return n > 0 ? 1 : -1;
	}
	
	// Returns the maximum of two comparables.
	public static <T extends Comparable<T>> T max(T c1, T c2)
	{
		if(c1.compareTo(c2) < 0)
		{
			return c2;
		}
		
		return c1;
	}
	
	// Returns the minimum of two comparables.
	public static <T extends Comparable<T>> T min(T c1, T c2)
	{
		if(c1.compareTo(c2) > 0)
		{
			return c2;
		}
		
		return c1;
	}
	
	// Returns the absolute difference between two values.
	public static double adist(double val1, double val2)
	{
		return Math.abs(val1 - val2);
	}
	
	// -- Number theory
	
	public static int LCM(Iterable<Integer> input)
	{
		// Universal least common multiple.
		int lcm = 1;
		
		for(int i : input)
		{
			lcm = LCM(lcm, i);
		}
		
		return lcm;
	}
	
	public static int LCM(int a, int b)
	{
		return a*b/(Euclid(a, b));	
	}
	
	private static int Euclid(int a, int b)
	{
		// Keep the modulus function safe.
		a = Math.abs(a);
		b = Math.abs(b);
		
		// Temporary variable used to store b.
		int c;
		while(b != 0)
		{
			c = b;
			b = a % b;
			a = c;
		}
		
		return a;
	}
	
	public static double bound(double in, double min, double max)
	{
		return Math.min(max, Math.max(in, min));
	}
}
