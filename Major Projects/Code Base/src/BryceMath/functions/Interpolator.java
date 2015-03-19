package BryceMath.functions;

import util.interfaces.Function;
import BryceMath.Numbers.Rational;
import BryceMath.Structures.Polynomial;

/*
 * The Interpolation function class,
 * written by Bryce Summers on 4 - 4 - 2013.
 * 
 * Purpose: This class provides functions that interpolate from:
 *         [0,1] -> [0,1], f'(0) = 0 & f'(1) = 0;  
 */

// FIXME : Separate this into two Interpolation functions.

public class Interpolator implements Function<Double, Double>
{
	
	public static final int POLY = 0;
	public static final int TRIG = 1;
	
	private int myMode;

	// My favorite Polynomial: 3x^2 - 2x^3.
	static Polynomial f1 = new Polynomial(-2, 3, 0, 0);
	
	public Interpolator(int mode)
	{
		switch(mode)
		{
			case 0: myMode = POLY;
					break;
			case 1: myMode = TRIG;
					break;
			default: throw new Error("Interpolation mode not supported.");
		}
	}

	public Rational eval(Rational x)
	{
		if(x.part_int().toInt() != 0)
		{
			throw new Error("Intepolator out of bounds.");
		}
		
		switch(myMode)
		{
			case 0: return f1.eval(x);
			case 1: return new Rational(1,2).sub(new Rational(.5*Math.cos(Math.PI*x.toDouble())));
			default: throw new Error("Interpolation mode not supported.");
		}
	}
	
	// This interpolation function uses trigonometric functions to create a perfect acceleration/decceleration.
	public Double eval(Double x)
	{
		if(x < 0 || x > 1)
		{
			throw new Error("Intepolator out of bounds : val = " + x);
		}
		
		switch(myMode)
		{
			case 0:  return f1.eval(new Rational(x)).toDouble();
			case 1:  return .5 - .5*Math.cos(Math.PI*x);
			default: throw new Error("Interpolation mode not supported.");
		}
		
		
		
	}
	
}
