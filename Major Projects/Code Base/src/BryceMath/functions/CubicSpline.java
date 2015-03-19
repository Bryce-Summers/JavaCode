package BryceMath.functions;

import util.interfaces.Function;
import BryceMath.DoubleMath.Vector;

/*
 * Cubic spline interpolation class.
 * 
 * Given 2 points and tangent vectors, provides an interpolator between them.
 * 
 * Derived and implemented by Bryce Summers on 6 - 15 - 2014.
 */

public class CubicSpline implements Function<Double, Vector>
{
	// COefficients of a cubic polynomial.
	Vector A, B, C, D;

	public CubicSpline(Vector p1, Vector t1, Vector p2, Vector t2)
	{
		/* 
		 * A =  2*p1 - 2*p2 + t1 + t2;
		 * B = -3*p1 + 3*p2 - T1 - 2*t2
		 * C = T2;
		 * D = P1;
		 */
		
		A = p1.mult(2).sub(p2.mult(2)).add(t1).add(t2);
		B = p1.mult(-3).add(p2.mult(3)).sub(t1).sub(t2.mult(2));
		C = t2;
		D = p1;
	}

	// REQUIRES : input should be between 0 and 1.0;
	@Override
	public Vector eval(Double input)
	{
		if(input < 0 || input > 1.0)
		{
			throw new Error("Error: Input not in function domain!");
		}
		
		double x_1 = input;
		double x_2 = x_1*input;
		double x_3 = x_2*input;
		
		return A.mult(x_3).add(B.mult(x_2)).add(C.mult(x_1)).add(D);
	}

}
