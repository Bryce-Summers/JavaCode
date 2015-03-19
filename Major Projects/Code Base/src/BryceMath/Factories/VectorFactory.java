package BryceMath.Factories;

import BryceMath.Numbers.Complex;
import BryceMath.Numbers.Equation;
import BryceMath.Numbers.Expression;
import BryceMath.Numbers.FiniteInteger;
import BryceMath.Numbers.IntB;
import BryceMath.Numbers.Multinomial;
import BryceMath.Numbers.Rational;
import BryceMath.Structures.Vector;

/*
 * Vector factory class.
 * 
 * Written by Bryce Summers on 6 - 27 - 2013.
 * 
 * Purpose : Provides functionality for creating many types of vectors.
 * 			 These functions should me imported into any classes that desire to make the given structures.
 * 
 * 	1. Pure Number to vector functions.
 * 
 *  2. Vectors from integer functions.
 */

public class VectorFactory
{
	// -- 1. Vector creation functions from pure numbers.
	
	public static Vector<FiniteInteger> v(FiniteInteger ... input)
	{
		return new Vector<FiniteInteger>(input);
	}
	
	public static Vector<IntB> v(IntB ... input)
	{
		return new Vector<IntB>(input);
	}
	
	public static Vector<Rational> v(Rational ... input)
	{
		return new Vector<Rational>(input);
	}
	
	public static Vector<Complex> v(Complex ... input)
	{
		return new Vector<Complex>(input);
	}
	
	public static Vector<Multinomial> v(Multinomial ... input)
	{
		return new Vector<Multinomial>(input);
	}
	
	public static Vector<Expression> v(Expression ... input)
	{
		return new Vector<Expression>(input);
	}
	
	public static Vector<Equation> v(Equation ... input)
	{
		return new Vector<Equation>(input);
	}
	
	// -- 2. Vector creation functions from integers.
	
	public static Vector<IntB> v_i(int ... input)
	{
		int len = input.length;
		IntB[] data = new IntB[len];

		for(int i = 0; i < len; i++)
		{
			data[i] = new IntB(input[i]);
		}
		return new Vector<IntB>(data);
	}
	
	public static Vector<Rational> v_r(int ... input)
	{
		int len = input.length;
		Rational[] data = new Rational[len];

		for(int i = 0; i < len; i++)
		{
			data[i] = new Rational(input[i]);
		}
		return new Vector<Rational>(data);
	}

	public static Vector<Complex> v_c(int ... input)
	{
		int len = input.length;
		Complex[] data = new Complex[len];
		
		for(int i = 0; i < len; i++)
		{
			data[i] = new Complex(input[i]);
		}		
		return new Vector<Complex>(data);
	}
	
	public static Vector<Multinomial> v_e(int ... input)
	{
		int len = input.length;
		Multinomial[] data = new Multinomial[len];

		for(int i = 0; i < len; i++)
		{
			data[i] = new Multinomial(input[i]);
		}
		return new Vector<Multinomial>(data);
	}
	
	public static Vector<Expression> v_re(int ... input)
	{
		int len = input.length;
		Expression[] data = new Expression[len];

		for(int i = 0; i < len; i++)
		{
			data[i] = new Expression(input[i]);
		}
		return new Vector<Expression>(data);
	}
	
	public static Vector<Equation> v_ex(int ... input)
	{
		int len = input.length;
		Equation[] data = new Equation[len];

		for(int i = 0; i < len; i++)
		{
			data[i] = new Equation(input[i]);
		}
		return new Vector<Equation>(data);
	}

}
