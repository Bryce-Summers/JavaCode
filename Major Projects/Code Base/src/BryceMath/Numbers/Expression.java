package BryceMath.Numbers;

import java.io.PrintStream;

import Data_Structures.Structures.List;

/*
 *  A RationalMultinomial number class.
 *  
 * Written after much wasted time by Bryce Summers on 7 - 19 - 2013.
 * 
 * This class is virtually identical to the Rational number class, but I was unable to combined the two of them.
 * 
 * FIXME : This class is not a true expression class yet. It is actually a Rational Multinomial class.
 * FIXME : Delete this class and incorporate its simplification functionality into the Multinomial class.
 */
public class Expression extends Number<Expression> implements Comparable<Expression>
{

	/* Interface:
	 * 		constructor.
	 * 		add.
	 * 		subtract.
	 * 		multiply.
	 * 		divide.
	 * 		simplify. (divide by GCF).
	 * 		LCM, GCF (Euclid's Algorithm).
	 */
	
	// -- Local Variables.
	private Multinomial num, denom;
	
	// Identities.
	public static final Expression ZERO = new Expression(0);
	public static final Expression ONE  = new Expression(1);
	
	// -- Constructors
	public Expression(Multinomial numerator, Multinomial denominator)
	{
		// It is safe to do this, because Integers are immutable.
		num   = numerator;
		denom = denominator;
		
		simplify();
	}
		
	public Expression(long numerator, long denominator)
	{
		num   = I(numerator);
		denom = I(denominator);
		
		simplify();
	}
	
	public Expression(Multinomial numerator)
	{
		num   = numerator;
		denom = Multinomial.ONE;
	}
	
	public Expression(long numerator)
	{
		num   = I(numerator);
		denom = Multinomial.ONE;
	}
	
	public Expression(IntB numerator)
	{
		num   = new Multinomial(numerator);
		denom = Multinomial.ONE;
	}
	
	public Expression(Rational numerator)
	{
		num   = new Multinomial(numerator);
		denom = Multinomial.ONE;
	}
	
	public Expression(Complex numerator)
	{
		num   = new Multinomial(numerator);
		denom = Multinomial.ONE;
	}
	
	public Expression(String numerator)
	{
		num   = new Multinomial(numerator);
		denom = Multinomial.ONE;
	}
	
	// FIXME: This function might introduce unnecessary floating point errors.
	// FIXME : Phase this class out once proper approximation classes have been created.
	public Expression(double input)
	{
		String s = "" + input % 1;
		int len  = s.length();
	
		
		// Parse the input double number by number as a long.
		denom = I(1);
		num   = I((long)(input));
		
		input = input % 1;
		
		for(int i = 1; i < len - 1; i++)
		{
			num	  = num.mult(10);
			denom = denom.mult(10);
			input = input * 10;
			num   = num.add(I((long)input));
			
			input = input % 1;
		}
		
		simplify();
		
	}

	// Returns the multiplicative identity.
	public Expression one()
	{
		return ONE;
	}
	
	// Returns the additive identity.
	public Expression zero()
	{
		return ZERO;
	}
	
	// -- Elementary Arithmetic operations with two RationalRationalMultinomials.
	public Expression add(Expression input)
	{
		
		if(input.eq(ZERO))
		{
			return this;
		}
		
		if(this.eq(ZERO))
		{
			return input;
		}
		
		// Perform mutations on copies, not the immutable originals.
		Expression r1 = clone();
		Expression r2 = input.clone();
		
		// Normalize the two fractions.
		Multinomial denom_out = normilize_denominators(r1, r2);
				
		// Perform standard arithmetic on the numerators.
		Expression output = new Expression(r1.getNum().add(r2.getNum()), denom_out);
		
		return output;
		
	}
	
	public Expression sub(Expression input)
	{
		if(input.eq(ZERO))
		{
			return this;
		}
		
		if(this.eq(ZERO))
		{
			return input.neg();
		}
		
		// Perform mutations on copies, not the immutable originals.
		Expression r1 = clone();
		Expression r2 = input.clone();
		
		// Normalize the two fractions.
		Multinomial denom_out = normilize_denominators(r1, r2);
				
		// Perform standard arithmetic on the numerators.
		Expression output = new Expression(r1.getNum().sub(r2.getNum()), denom_out);
		
		return output;
	}
	
	public Expression neg()
	{
		return new Expression(num.neg(), denom);
	}

	public Expression mult(Expression r)
	{
		// Combine and simplify the two expressions gradually,
		// so that the Euclid's algorithm is applied to each set of divisor independently 
		// so if will not fail as readily.
		Expression o_1 = new Expression(num, r.getDenom());
		o_1.simplify();
		Expression o_2 = new Expression(r.getNum(), denom);
		o_2.simplify();
		
		Multinomial output_num   = o_1.getNum().mult(o_2.getNum());
		Multinomial output_denom = o_1.getDenom().mult(o_2.getDenom());
		
		Expression output = new Expression(output_num, output_denom);
		
		// Simplify the result a final time.
		return output.simplify();
	}
	
	// Multiply by the multiplicative inverse.
	@Override
	public Expression div(Expression r)
	{
		if(r.eq(ZERO))
		{
			throw new Error("Cannot divide RationalMultinomials by 0");
		}
	
		// We reduce division to multiplication to ensure the simplification remains relatively safe.
		return mult(r.mult_inverse());
	}			

	
	// Perform modular arithmetic on RationalMultinomial numbers,
	// assuming they are viewed with equal denominators.
	// This should return the correct integer result if the two RationalMultinomial numbers are both integers.
	public Expression mod(Expression input)
	{
		
		if(input.eq(ZERO))
		{
			throw new Error("Cannot mod RationalMultinomials by 0");
		}
		
		// Perform mutations on copies, not the immutable originals.
		Expression r1 = clone();
		Expression r2 = input.clone();
		
		// Normalize the two fractions.
		Multinomial denom_out = normilize_denominators(r1, r2);
				
		// Perform standard arithmetic on the numerators.
		Expression output = new Expression(r1.getNum().mod(r2.getNum()), denom_out);
		
		return output;
			
	}
	
	public int compareTo(Expression input)
	{
		// Perform mutations on copies, not the immutable originals.
		Expression r1 = clone();
		Expression r2 = input.clone();
		
		// Normalize the two fractions.
		normilize_denominators(r1, r2);
		
		Multinomial n1 = r1.getNum();
		Multinomial n2 = r2.getNum();
		
		return n1.compareTo(n2);
	}
	
	// FIXME: Implement my own sqrt function.
	@Override
	public Expression sqrt()
	{
		throw new Error("Unimplemented.");
	}
	
	// -- Equality functions.
	
	@Override
	// returns whether this RationalMultinomial is equal to another RationalMultinomial,
	// assuming both are in simplest form.
	public boolean eq(Expression other)
	{		
		return num.eq(other.num) && denom.eq(other.denom);
	}
	
	// -- Fraction functions.
	
	// Simplifies a RationalMultinomial in place,
	// FIXME : This simplification algorithm fails for [(a - b)/a] * [(c-d)/c] / [(a - b)/a].
	private Expression simplify()
	{
		if(denom.eq(0))
		{
			throw new Error("RationalMultinomials cannot have denominators equal to 0!");
		}
		
		// Greatest common factor found by Euclid's algorithm.
		Multinomial gcd = Euclid(num, denom);

		num	  = num  .div(gcd);
		denom = denom.div(gcd);
				
		// Push negatives to the top.
		if(denom.isNegative())
		{
			num   = num.neg();
			denom = denom.neg();
		}
		
		// Now we are going to simplify the RationalMultinomial further so that it contains only integer coefficients.
		
		// Find all of the rational coefficients in the numerator and the denominator.
		List<Complex> coefs = num.getCoefs();
		coefs.append(denom.getCoefs());

		// Extract all of the denominator terms.
		List<IntB> denoms = new List<IntB>();
		
		for(Complex r : coefs)
		{
			denoms.add(r.part_real().getDenom());
			denoms.add(r.part_imaginary().getDenom());
		}
		
		// Compute the least common multiple of all of the denominator terms.
				
		IntB lcm = NumberMath.LCM(denoms);

		num   = num.mult(new Multinomial(lcm));
		denom = denom.mult(new Multinomial(lcm));
		
		// -- We also need to compute the greatest common factor of the numerators.
		
		// Extract all of the denominator terms.
		List<IntB> nums = new List<IntB>();
				
		for(Complex r : coefs)
		{
			nums.add(r.part_real().getNum());
			nums.add(r.part_imaginary().getNum());
		}
		
		IntB gcf = NumberMath.GCF(nums);
		
		num   = num.div(new Multinomial(gcf));
		denom = denom.div(new Multinomial(gcf));

		return this;
	}
	
	// Scales two RationalMultinomial numbers to have the same denominators.
	// Destructive
	// returns the denominator for convenience.
	public Multinomial normilize_denominators(Expression r1, Expression r2)
	{
		// Compute the final denominators.
		Multinomial denom1 = r1.getDenom();
		Multinomial denom2 = r2.getDenom();
		Multinomial lcm = LCM(denom1, denom2);
		
		// Compute how much each fraction need so to be scaled by.
		Multinomial scalar1 = lcm.div(denom1);
		Multinomial scalar2 = lcm.div(denom2);
		
		// Scale these two RationalExpressions.
		r1.scale(scalar1);
		r2.scale(scalar2);
	
		// Return the final denominators.
		return lcm;
		
	}
	
	// Allows RationalMultinomial numbers to be unsimplified.
	private void scale(Multinomial scale)
	{
		num   = num  .mult(scale);
		denom = denom.mult(scale);
	}
	
	// Returns the greatest common divisor of two numbers a and b;
	// FIXME : This algorithm does not find all Greatest common factors.
	// FIXME : This simplification algorithm fails for [(a - b)/a] * [(c-d)/c] / [(a - b)/a].
	// It is probably a subtle error in the extended mod function.
	private Multinomial Euclid(Multinomial a, Multinomial b)
	{
		// Keep the modulus function safe.
		a = a.abs();
		b = b.abs();
		
		// Temporary variable used to store b.
		Multinomial c;
			
		// The numbers should only reverse at most once.
		// If they reverse more than once, it means that similar terms are 
		boolean reverse = false;
		
		while(!b.eq(0))
		{
			c = b;
			b = a.mod(b);
			
			if(b.eq(a))
			{
				if(reverse)
				{
					return Multinomial.ONE;
				}
				else
				{
					reverse = true;
				}
			}
			
			a = c;
	
		}		

		return a;
	}
	
	// Least common Multiple
	private Multinomial LCM(Multinomial a, Multinomial b)
	{		
		return a.mult(b).div(Euclid(a, b));
	}

	// -- Data Access functions.
	public Multinomial getNum()
	{
		// Clones are not necessary,
		// because Integers are immutable.
		return num;
	}
	
	public Multinomial getDenom()
	{
		// Clones are not necessary,
		// because Integers are immutable.
		return denom;
	}
	
	/*
	 *  Allows for the sign of RationalMultinomial numbers to be queried.
	 * -1 means it is negative.
	 *  0 means that it is zero.
	 *  1 means that it is positive.
	 */
	public int sign()
	{
		return num.sign();
	}
	
	public Expression mult_inverse()
	{
		return new Expression(denom, num);
	}
	
	// Every RationalMultinomial number can be written as the sum of an integer and a proper fraction.
	public Multinomial part_int()
	{
		return num.div(denom);
	}
	
	public Expression part_frac()
	{
		Expression output = new Expression(num.abs().mod(denom), denom);
		
		// Deal with negatives.
		if(num.isNegative())
		{
			return output.neg();
		}
		
		return output;
	}
	
	public boolean isPositive()
	{
		return num.isPositive();
	}
	
	public boolean isNegative()
	{
		return num.isNegative();
	}
	
	public Expression abs()
	{
		if(isNegative())
		{
			return neg();
		}
		
		return this;
	}

	// -- Helper Functions
	@Override
	public Expression clone()
	{
		return new Expression(num, denom);
	}
	
	@Override
	public String toString()
	{
		if(denom.equals(Multinomial.ONE))
		{
			return "" + num;
		}
		
		return "\\frac{" + num + "}{" + denom + "}";
	}
	
	@Override
	public void serializeTo(PrintStream stream)
	{
		stream.println(toSerialString());
		return;
	}

	public String toSerialString()
	{
		if(denom.equals(Multinomial.ONE))
		{
			return "" + num.toSerialString();
		}

		return "(" + num.toSerialString() + ")/(" + denom.toSerialString() + ")";
	}
	
	// FIXME : There is room for improvement for this hash function.
	@Override
	public int hashCode()
	{
		return num.hashCode() + denom.hashCode();
	}

	// Creation functions.
	
	@Override
	Expression N(long n)
	{
		if(n == 0)
		{
			return ZERO;
		}
		
		if(n == 1)
		{
			return ONE;
		}
		
		return new Expression(n);
	}
	
	private Multinomial I(long i)
	{
		// Hack due to the limitations of abstract classes.
		return Multinomial.ONE.N(i);
	}

	public Expression pow(Expression power)
	{
		if(eq(0))
		{
			return ZERO;
		}
		
		if(eq(1))
		{
			return ONE;
		}
		
		if(power.eq(0))
		{
			return ONE;
		}
		
		if(power.eq(1))
		{
			return this;
		}
		
		if(power.isInt())
		{
			IntB exponent = power.toIntB();
			
			if(exponent.isNegative())
			{
				return pow(exponent.neg()).mult_inverse();
			}
				
			return pow(exponent);
		}
		
		
		// FIXME, FIXME, FIXME!!! Exponentiation by non integral powers is not defined.
		return this;
		//throw new Error("Implement Me please");
	}
	
	/**
	 * We compute the complex conjugate of this expression by conjugating the numerator,
	 * because complex terms are always simplified out of the denominator.
	 */
	@Override
	public Expression conj() 
	{
		Multinomial num_out = num.conj();
		
		if(num_out == num)
		{
			return this;
		}
		
		return new Expression(num_out, denom);
	}
	
	/**
	 * Returns true iff this Expression is representable in its entirety by an integral constant.
	 */
	public boolean isInt()
	{
		return denom.eq(1) && num.isInt();
	}
	
	public IntB toIntB()
	{
		return num.toIntB();
	}

	@Override
	public int toInt()
	{
		return num.toInt();
	}
}