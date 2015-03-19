package BryceMath.Numbers;

import Data_Structures.ADTs.Pairable;

/*
 * The RationalComponent class.
 * 
 * Written by Bryce Summers on 7 - 19 - 2013.
 * 
 * Purpose : This class specifies numbers that can be used in rational expressions.
 * 
 * The main point of this class is that numbers implementing it must be
 * able to provide consistent functionality for computing quotients and remainders.
 * If a number is able to do this,
 * then the RationalConstructs can simplify the expressions via Euclid's
 * algorithm for finding the Greatest Common Divisor.
 */

public abstract class ModularNumber<T extends ModularNumber<T>> extends Number<T> implements Comparable<T>
{
	// The most important function.
	public abstract Pairable<T> division(T input);
	
	
	// -- Classes probably will compute these efficiently in parrallel inside of the division function.
	@Override
	public T div(T input)
	{
		return division(input).getFirst();
	}

	// Modular Numbers know how to compute a modulus, which is the remainder under division.
	public T mod(T input)
	{
		return division(input).getLast();
	}
	
	public T mod(int i)
	{
		return mod(N(i));
	}

	// Rational Components need to be consistently represented.
	public abstract boolean isPositive();
	public abstract boolean isNegative();
	
	public T abs()
	{
		if(isNegative())
		{
			return mult(-1);
		}
		else
		{
			return mult(1);
		}
	}
	
	public int sign()
	{
		// Handle zero case.
		if(eq(0))
		{
			return 0;
		}
		
		// Handle the positive case.
		if(isPositive())
		{
			return 1;
		}
		
		// Handle the negative case.
		return -1;
	}
	
	public abstract T neg();
}
