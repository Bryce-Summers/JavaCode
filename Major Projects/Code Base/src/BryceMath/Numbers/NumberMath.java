package BryceMath.Numbers;

import Data_Structures.Structures.List;

/*
 * A Collection of Number Theoretic functions for use with Bryce Number Structures.
 * 
 * Written on 5 - 18 - 2014 by Bryce Summers.
 * 
 * Purpose : This will be a counterpart to the MathB class in that it provides mathematics functions involving numbers.
 * 
 * 			These functions will operate on the types specified in the Bryce Numbers Package.
 */

public class NumberMath
{

	public static IntB GCF(List<IntB> input) 
	{
		IntB gcf = IntB.ZERO;
		
		for(IntB i : input)
		{
			gcf = Euclid(gcf, i);
		}
		
		return gcf;
	}
	
	public static IntB LCM(Iterable<IntB> input)
	{
		// Universal least common multiple.
		IntB lcm = IntB.ONE;
		
		for(IntB i : input)
		{
			lcm = LCM(lcm, i);
		}
		
		return lcm;
	}
	
	public static IntB LCM(IntB a, IntB b)
	{
		return a.mult(b).div(Euclid(a, b));	
	}

	// Returns the least greatest strictly positive number that divides both a and b.
	// REQUIRES : a and b probably should not be 0...
	private static IntB Euclid(IntB a, IntB b)
	{
		// Keep the modulus function safe.
		a = a.abs();
		b = b.abs();
		
		// Temporary variable used to store b.
		IntB c;
		while(!b.eq(0))
		{
			c = b;
			b = a.mod(b);
			a = c;
		}
		return a;
	}

}
