package BryceMath.Factories;

import BryceMath.Numbers.Complex;
import BryceMath.Numbers.Expression;
import BryceMath.Numbers.IntB;
import BryceMath.Numbers.Multinomial;
import BryceMath.Numbers.Rational;

/*
 * The number factory class.
 * 
 * Written by Bryce Summers on 6 - 27 - 2013.
 * 
 * This class contains functions for easily creating numbers.
 */

public class NumberFactory
{
	// -- number creation functions.
	
	public static Rational r(long a, long b)
	{
		return new Rational(a, b);
	}

	public static Complex C(Rational real, Rational imaginary)
	{
		return new Complex(real, imaginary);
	}

	public static Complex C(Rational real)
	{
		return new Complex(real);
	}
	
	public static Complex C(int real)
	{
		return new Complex(real);
	}
	
	public static Complex C(int r, int i)
	{
		return new Complex(r, i);
	}

	public static IntB I(int i)
	{
		return new IntB(i);
	}

	public static IntB I(long i)
	{
		return new IntB(i);
	}

	public static IntB I(String i)
	{
		return new IntB(i);
	}

	public static Rational R(int r)
	{
		return new Rational(r);
	}

	public static Rational R(int num, int denom)
	{
		return new Rational(num, denom);
	}

	public static Rational R(long r)
	{
		return new Rational(r);
	}

	public static Rational R(long num, long denom)
	{
		return new Rational(num, denom);
	}

	public static Rational R(IntB num, IntB denom)
	{
		return new Rational(num, denom);
	}
	
	public static Rational R(String num)
	{
		return new Rational(num);
	}

	public static Multinomial M(IntB input)
	{
		return new Multinomial(input);
	}
	
	public static Multinomial M(Rational input)
	{
		return new Multinomial(input);
	}
	
	public static Multinomial M(Complex input)
	{
		return new Multinomial(input);
	}
	
	public static Multinomial M(long input)
	{
		return new Multinomial(input);
	}
	
	public static Multinomial M(long scalar, String var)
	{
		return new Multinomial(scalar, var);
	}
	
	public static Multinomial M(Rational scalar, String var)
	{
		return new Multinomial(scalar, var);
	}
	
	public static Multinomial M(Complex scalar, String var)
	{
		return new Multinomial(scalar, var);
	}
	
	public static Multinomial M(String var)
	{
		return new Multinomial(1, var);
	}
	
	// Allows for the creation single variable expressions to the indicated power.
	public static Multinomial M(String var, int power)
	{
		return new Multinomial(var, power);
	}
	
	// Allows for the creation single variable expressions to the indicated power.
	public static Multinomial M(String var, IntB power)
	{
		return new Multinomial(var, power);
	}
	
	
	// -- Rational Multinomials.
	/*	
	public static RationalMultinomial RM(Multinomial e1, Multinomial e2)
	{
		return new RationalMultinomial(e1, e2);
	}
		
	public static RationalMultinomial RM(Rational input)
	{
		return RM(M(input));
	}
	
	public static RationalMultinomial RM(long input)
	{
		return RM(M(input));
	}
	
	public static RationalMultinomial RM(long scalar, String var)
	{
		return RM(M(scalar, var));
	}
	
	public static RationalMultinomial RM(Rational scalar, String var)
	{
		return RM(M(scalar, var));
	}
	
	public static RationalMultinomial RM(String var)
	{
		return RM(M(var));
	}
	
	public static RationalMultinomial RM(Multinomial e)
	{
		return new RationalMultinomial(e, Multinomial.ONE);
	}
	*/
	
	// FIXME : Standardize these once I have fully finalized my representation of Expressions.
	
	// -- Full expression creation functions.
	
	public static Expression E(int i)
	{
		return new Expression(i);
	}
	
	public static Expression E(long l)
	{
		return new Expression(l);
	}
	
	
	public static Expression E(IntB r)
	{
		return new Expression(r);
	}
	
	public static Expression E(Rational r)
	{
		return new Expression(r);
	}
	
	public static Expression E(Complex r)
	{
		return new Expression(r);
	}
	
	/*
	public static Expression E(char c)
	{
		return new Expression("" + c);
	}*/
	
	public static Expression E(Rational scalar, String var)
	{
		return new Expression(M(scalar, var));
	}
	
	// Creates a new Expression that is a single variable corresponding to the given string.
	public static Expression E(String s)
	{
		return new Expression(M(s));
	}
}
