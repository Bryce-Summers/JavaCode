package BryceMath.Numbers;

import java.io.PrintStream;

import util.Genarics;


/*
 * The Bryce Summers Equation class.
 * 
 * Written by Bryce Summers on 7, 29, 2013.
 * 
 * Purpose : Extends expressions to equations.
 * 
 * Equations are represented by 2 expressions.
 * 
 * Equations can leave their second null to behave like expressions.
 * 
 * FIXME : I should decide whether Equations are allowed to be impossibilities or not.
 * 			- I could add functionality for determining whether an equation is always true, sometimes true, or never true.
 */

public class Equation extends Number<Equation>
{
	
	// Identities.
	public static Equation ONE  = new Equation(1);
	public static Equation ZERO = new Equation(0);

	// The additive identity tautology.
	public static Equation ZERO_ZERO = new Equation(0, 0);
	
	public static Genarics<Expression> ge_e = new Genarics<Expression>();
	
	// Representation.
	Expression left, right;
	
	// -- Constructors
	
	public Equation(Expression e1, Expression e2)
	{
		left  = e1;
		right = e2;
	}
	

	// Equations with only 1 non null side are expressions.
	public Equation(Expression e)
	{
		left = e;
	}
	
	/*
	public Equation(Multinomial numerator, Multinomial denominator)
	{
	}
	 */
	
	public Equation(long numerator, long denominator)
	{
		left  = new Expression(numerator);
		right = new Expression(denominator);
	}
	
	/*
	public Equation(Multinomial numerator)
	{

	}
	*/
	
	public Equation(long numerator)
	{
		left = new Expression(numerator);
		right = null;
	}
	
	/*
	public Equation(IntB numerator)
	{

	}
	
	public Equation(Rational numerator)
	{

	}
	
	public Equation(Complex numerator)
	{

	}
	
	public Equation(String numerator)
	{
	}
	
	
	// FIXME: This function might introduce unnecessary floating point errors.
	// FIXME : Phase this constructor out once proper approximation classes have been created.
	public Equation(double input)
	{

		
	}
	*/

	@Override
	Equation N(long n)
	{
		Expression e = new Expression(n);
		
		return new Equation(e);
	}

	@Override
	public Equation zero()
	{
		return ZERO;
	}

	@Override
	public Equation one()
	{
		return ONE;
	}
	
	
	// -- public Interface Functions.

	@Override
	public Equation add(Equation input)
	{
		Equation e1 = this;
		Equation e2 = input;
		
		// Handle trivial cases.
		if(e1.eq(0))
		{
			return e2; 
		}
		
		if(e2.eq(0))
		{
			return e1;
		}
		
		Expression outL, outR;
		
		// Handle all equation arithmetic cases.
		switch(get_case(e1, e2))
		{
			case MONO:
				outL = e1.left.add(e2.left);
				return new Equation(outL);
				
			case MONO_DUAL:
				outL = e1.left.add(e2.left);
				outR = e1.left.add(e2.right);
				return new Equation(outL, outR);

			case DUAL_MONO:
				outL = e1.left.add(e2.left);
				outR = e1.right.add(e2.left);
				return new Equation(outL, outR);
				
			case DUAL:
				outL = e1.left.add(e2.left);
				outR = e1.right.add(e2.right);
				return new Equation(outL, outR);
		}
		
		throw new Error("Should Never get here!");
	}

	@Override
	public Equation sub(Equation input)
	{
		Equation e1 = this;
		Equation e2 = input;
		
		// Handle trivial cases.
		if(e1.eq(0))
		{
			return e2.neg(); 
		}
		
		if(e2.eq(0))
		{
			return e1;
		}
		
		Expression outL, outR;
		
		// Handle all equation arithmetic cases.
		switch(get_case(e1, e2))
		{
			case MONO:
				outL = e1.left.sub( e2.left);
				return new Equation(outL);
				
			case MONO_DUAL:
				outL = e1.left.sub(e2.left);
				outR = e1.left.sub(e2.right);
				return new Equation(outL, outR);

			case DUAL_MONO:
				outL = e1.left.sub(e2.left);
				outR = e1.right.sub(e2.left);
				return new Equation(outL, outR);
				
			case DUAL:
				outL = e1.left.sub(e2.left);
				outR = e1.right.sub(e2.right);
				return new Equation(outL, outR);
		}
		
		throw new Error("Should Never get here!");
	}

	@Override
	public Equation mult(Equation input)
	{
		Equation e1 = this;
		Equation e2 = input;
		Expression outL, outR;
		
		if(e1.eq(0))
		{
			return e1;
		}
		
		if(e2.eq(0))
		{
			return e2;
		}
		
		// Handle all equation arithmetic cases.
		switch(get_case(e1, e2))
		{
			case MONO:
				outL = e1.left.mult( e2.left);
				return new Equation(outL);
				
			case MONO_DUAL:
				outL = e1.left.mult(e2.left);
				outR = e1.left.mult(e2.right);
				return new Equation(outL, outR);

			case DUAL_MONO:
				outL = e1.left.mult(e2.left);
				outR = e1.right.mult(e2.left);
				return new Equation(outL, outR);
				
			case DUAL:
				outL = e1.left. mult(e2.left);
				outR = e1.right.mult(e2.right);
				return new Equation(outL, outR);
		}
		
		throw new Error("Should Never get here!");
	}

	@Override
	public Equation div(Equation input)
	{
		Equation e1 = this;
		Equation e2 = input;
		
		Expression outL, outR;

		// Denominator contains a 0,
		// numerator equals 0 needs to be dealt with by cases.
		if(e2.left.eq(0) || (e2.right != null && e2.right.eq(0)))
		{
			throw new Error("Error : Cannot divide by zero!");
		}
		
		// Divide by 1.
		if(e2.eq(1))
		{
			return e1;
		}
		
		// Handle all equation arithmetic cases.
		switch(get_case(e1, e2))
		{
			case MONO:
				outL = e1.left.div( e2.left);
				return new Equation(outL);
				
			case MONO_DUAL:
				outL = e1.left.div(e2.left);
				outR = e1.left.div(e2.right);
				return new Equation(outL, outR);

			case DUAL_MONO:
				outL = e1.left.div(e2.left);
				outR = e1.right.div(e2.left);
				return new Equation(outL, outR);
				
			case DUAL:
				outL = e1.left. mult(e2.left);
				outR = e1.right.mult(e2.right);
				return new Equation(outL, outR);
		}

		throw new Error("Should Never get here!");
	}

	public Equation conj()
	{
		Expression e1 = null, e2 = null;
		
		if(left != null)
		{
			e1 = left.conj();
		}
		
		if(right != null)
		{
			e2 = right.conj();
		}
		
		// Handle the trivial mutation case.
		if(e1 == left && e2 == right)
		{
			return this;
		}
		
		return new Equation(e1, e2);
	}
	
	@Override
	public Equation sqrt()
	{
		Expression e1 = null, e2 = null;
		
		if(left != null)
		{
			e1 = left.sqrt();
		}
		
		if(right != null)
		{
			e2 = right.sqrt();
		}

		// Handle the trivial mutation case.
		if(e1 == left && e2 == right)
		{
			return this;
		}
		
		return new Equation(e1, e2);
	}

	@Override
	public boolean eq(Equation other)
	{
		Equation e1 = this;
		Equation e2 = other;
		
		return 	ge_e.xequal(e1.left, e2.left) &&
				ge_e.xequal(e1.right, e2.right);
	}

	@Override
	public int hashCode()
	{
		int i1 = left.hashCode();
		int i2 = right != null ? right.hashCode() : 0;
		
		// Use a large prime number.
		return i1 * 16769023 + i2; 
	}

	public String toSerialString()
	{
		if(right == null)
		{
			return left.toSerialString(); 
		}
		
		return left.toSerialString() + " = " + right.toSerialString();
	}
	
	@Override
	public String toString()
	{
		if(right == null)
		{
			return left.toString(); 
		}
		
		return left + " = " + right;
	}

	@Override
	public Equation neg()
	{
		Expression e1, e2;
		
		e1 = left.neg();
		e2 = right != null ? right.neg() : null;
		
		return new Equation(e1, e2);
	}

	/* We will arbitrarily define the sign of the equation
	 * to be the left expression's sign.
	 */
	@Override
	public boolean isNegative()
	{
		return left.isNegative();
	}

	@Override
	public boolean isPositive()
	{
		return left.isPositive();
	}

	@Override
	public int sign()
	{
		return left.sign();
	}

	@Override
	public Equation abs()
	{

		boolean b1 = left.isPositive();
		
		if(isExpression())
		{
			return b1 ? this : neg();
		}
		
		if(b1 && right.isPositive())
		{
			return this;
		}
		
		Expression e1, e2;
		
		e1 = left.abs();
		e2 = right.abs();
		
		return new Equation(e1, e2);
	}

	public boolean isExpression()
	{
		return right == null;
	}
	
	@Override
	public boolean isInt()
	{
		return isExpression() && left.isInt(); 
	}

	
	// Warning, this function forgets about the right side.
	@Override
	public IntB toIntB()
	{
		return left.toIntB();
	}

	@Override
	public int toInt()
	{
		return left.toInt();
	}
	
	// -- Equation specific functions.
	
	public Expression getLeft()
	{
		return left;
	}
	
	public Expression getRight()
	{
		if(isExpression())
		{
			throw new Error("Error : Equation only has 1 side.");
		}
		
		return right;
	}
	
	// Puts the right equation on the left and the left equation on the right.
	public Equation switch_sides()
	{
		return new Equation(right, left);
	}
	
	// Algebraically manipulates the equation into an expression equal to zero.
	public Equation reduce_zero()
	{
		if(isExpression())
		{
			throw new Error("Error : Equation has no right side!");
		}
		
		Expression e1 = left.sub(right);
		Expression e2 = Expression.ZERO;
		
		return new Equation(e1, e2);
	}
	
	
	// -- Helper functions.
	
	/* Parses which state a binary operation is in.
	 * -- when both equations have 2 sides, then the operation is performed independently on both sides.
	 * When at least 1 of the equations e lacks a right side,
	 * then the operation is applied with the left side of e and both valid sides of the other equation.
	 */
	private enum op
	{
		MONO, MONO_DUAL, DUAL_MONO, DUAL;
	}
	private static op get_case(Equation e1, Equation e2)
	{
		boolean m_1 = e1.right == null;
		boolean m_2 = e2.right == null;
		
		if(m_1)
		{
			return m_2 ? op.MONO : op.MONO_DUAL;
		}
		
		return m_2 ? op.DUAL_MONO : op.DUAL;
	}

	public Equation mult_inverse()
	{
		Expression out_l, out_r;
		
		out_l = left.mult_inverse();
		out_r = right != null ? right.mult_inverse(): null;
		
		return new Equation(out_l, out_r);
	}
	
	@Override
	public void serializeTo(PrintStream stream)
	{
		stream.println(toSerialString());
		return;
	}

}
