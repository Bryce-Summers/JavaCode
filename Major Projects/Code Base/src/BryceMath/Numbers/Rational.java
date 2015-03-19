package BryceMath.Numbers;

import static util.testing.ASSERT;

/*
 *  A Rational number class.
 *  
 *  Written by Bryce Summers 11/14/2012.
 *  Finished: 12 - 21 - 2012.
 *  Updated : 6 - 1 - 2013.
 *  
 *  Purpose: Deals with exact rational arithmetic with IntB precision. (Pretty Large).
 *  Updated, 1 - 8 - 2013, Changed fancy printing into single line printing.
 */
public class Rational extends Number<Rational> implements Comparable<Rational>
{

	/* Interface:
	 * 		constructor.
	 * 		add.
	 * 		subtract.
	 * 		multiply.
	 * 		divide.
	 * 		simplify. (divide by GCF).
	 * 		LCM, GCF (Euclid's Algorithm).
	 * 
	 * 		Bonus : Modular calculation.
	 */
	
	// -- Local Variables.
	private IntB num, denom;
	
	// Identities.
	public static final Rational ZERO = new Rational(0);
	public static final Rational ONE  = new Rational(1);
	
	// -- Constructors
	public Rational(IntB numerator, IntB denominator)
	{
		// It is safe to do this, because Integers are immutable.
		num   = numerator;
		denom = denominator;
		
		simplify();
	}
	
	public Rational(long numerator, long denominator)
	{
		num   = I(numerator);
		denom = I(denominator);
		
		simplify();
	}
	
	public Rational(IntB numerator)
	{
		num   = numerator;
		denom = IntB.ONE;
	}
	
	public Rational(long numerator)
	{
		num   = I(numerator);
		denom = IntB.ONE;
	}
	
	// FIXME : Implement interpretation of fractions.
	
	public Rational(String str)
	{
		// First recursively handle division characters.
		// 5/7/8 -> (5/7) / 8;
		String[] parts = str.split("/");
		
		int len = parts.length;
		
		if(len > 1)
		{
			Rational r1 = new Rational(parts[0]);

			for(int i = 1; i < len; i++)
			{
				r1 = r1.div(new Rational(parts[i]));
			}
			
			num = r1.num;
			denom = r1.denom;
			return;
		}
		
		// Now handle the decimal case if the given string contains no division signs.
		
		ASSERT(len == 1);
		
		str = parts[0];
		
		parts = str.split("\\.");
		
		if(parts.length == 0)
		{
			num   = IntB.ZERO;
			denom = IntB.ONE;
			return;
		}
		
		IntB pi = new IntB(parts[0]);
		
		// No decimal portion.
		if(parts.length < 2)
		{
			num = pi;
			denom = IntB.ONE;
			return;
		}
		
		len = parts[1].length();
		
		StringBuilder sb = new StringBuilder();
		sb.append("1");
		
		for(int i = 0; i < len; i++)
		{
			sb.append("0");
		}
		
		num		= new IntB(parts[0] + parts[1]);
		denom	= new IntB(sb.toString());
		
		simplify();	
	}
	
	// FIXME: This function might introduce unnecessary floating point errors.
	// FIXME : Phase this class out once proper approximation classes have been created.
	// FIXME : This function is probably rather inefficient.
	public Rational(double input)
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
	public Rational one()
	{
		return ONE;
	}
	
	// Returns the additive identity.
	public Rational zero()
	{
		return ZERO;
	}
	
	// -- Elementary Arithmetic operations with two rationals.
	public Rational add(Rational input)
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
		Rational r1 = clone();
		Rational r2 = input.clone();
		
		// Normalize the two fractions.
		IntB denom_out = normilize_denominators(r1, r2);
				
		// Perform standard arithmetic on the numerators.
		Rational output = new Rational(r1.getNum().add(r2.getNum()), denom_out);
		
		return output;
		
	}
	
	public Rational sub(Rational input)
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
		Rational r1 = clone();
		Rational r2 = input.clone();
		
		// Normalize the two fractions.
		IntB denom_out = normilize_denominators(r1, r2);
				
		// Perform standard arithmetic on the numerators.
		Rational output = new Rational(r1.getNum().sub(r2.getNum()), denom_out);
		
		return output;
	}
	
	public Rational neg()
	{
		return new Rational(num.neg(), denom);
	}

	public Rational mult(Rational r)
	{
		Rational output = new Rational(num.mult(r.getNum()), denom.mult(r.getDenom()));
		
		return output.simplify();
	}
	
	// Multiply by the multiplicative inverse.
	@Override
	public Rational div(Rational r)
	{
		if(r.eq(ZERO))
		{
			throw new Error("Cannot divide Rationals by 0");
		}
		
		Rational output = new Rational(num.mult(r.getDenom()), denom.mult(r.getNum()));
		
		return output.simplify();
	}
	
	// Perform modular arithmetic on Rational numbers,
	// assuming they are viewed with equal denominators.
	// This should return the correct integer result if the two rational numbers are both integers.
	public Rational mod(Rational input)
	{
		
		if(input.eq(ZERO))
		{
			throw new Error("Cannot mod Rationals by 0");
		}
		
		// Perform mutations on copies, not the immutable originals.
		Rational r1 = clone();
		Rational r2 = input.clone();
		
		// Normalize the two fractions.
		IntB denom_out = normilize_denominators(r1, r2);
				
		// Perform standard arithmetic on the numerators.
		Rational output = new Rational(r1.getNum().mod(r2.getNum()), denom_out);
		
		return output;
			
	}
	
	public int compareTo(Rational input)
	{
		// Perform mutations on copies, not the immutable originals.
		Rational r1 = clone();
		Rational r2 = input.clone();
		
		// Normalize the two fractions.
		normilize_denominators(r1, r2);
		
		IntB n1 = r1.getNum();
		IntB n2 = r2.getNum();
		
		return n1.compareTo(n2);
	}
	
	// FIXME: Implement my own sqrt function.
	@Override
	public Rational sqrt()
	{
		throw new Error("Unimplemented.");
	}
	
	// -- Equality functions.
	
	@Override
	// returns whether this rational is equal to another rational,
	// assuming both are in simplest form.
	public boolean eq(Rational other)
	{		
		return num.eq(other.num) && denom.eq(other.denom);
	}
	
	// -- Fraction functions.
	
	// Simplifies a rational in place,
	private Rational simplify()
	{
		if(denom.eq(0))
		{
			throw new Error("Rationals cannot have denominators equal to 0!");
		}
		
		// Greatest common factor found by Euclid's algorithm.
		IntB gcd = Euclid(num, denom);
		num	  = num  .div(gcd);
		denom = denom.div(gcd);
		
		// Push negatives to the top.
		if(denom.isNegative())
		{
			num   = num.neg();
			denom = denom.neg();
		}
		
		return this;
	}
	
	// Scales two rational numbers to have the same denominators.
	// Destructive
	// returns the denominator for convenience.
	private IntB normilize_denominators(Rational r1, Rational r2)
	{
		// Compute the final denominators.
		IntB denom1 = r1.getDenom();
		IntB denom2 = r2.getDenom();
		IntB lcm = LCM(denom1, denom2);
		
		// Compute how much each fraction need so to be scaled by.
		IntB scalar1 = lcm.div(denom1);
		IntB scalar2 = lcm.div(denom2);
		
		// Scale these two Rationals.
		r1.scale(scalar1);
		r2.scale(scalar2);
	
		// Return the final denominators.
		return lcm;
		
	}
	
	// Allows Rational numbers to be unsimplified.
	private void scale(IntB scale)
	{
		num   = num  .mult(scale);
		denom = denom.mult(scale);
	}
	
	// Returns the greatest common divisor of two numbers a and b;
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
	
	// Least common Multiple
	public static IntB LCM(IntB a, IntB b)
	{		
		return a.mult(b).div(Euclid(a, b));
	}
	
	// Returns whether this rational is an integer.
	@Override
	public boolean isInt()
	{
		return denom.equals(IntB.ONE);
	}
	
	// -- Data Access functions.
	public IntB getNum()
	{
		// Clones are not necessary,
		// because Integers are immutable.
		return num;
	}
	
	public IntB getDenom()
	{
		// Clones are not necessary,
		// because Integers are immutable.
		return denom;
	}
	
	/*
	 *  Allows for the sign of rational numbers to be queried.
	 * -1 means it is negative.
	 *  0 means that it is zero.
	 *  1 means that it is positive.
	 */
	public int sign()
	{
		return num.sign();
	}
	
	public Rational mult_inverse()
	{
		return new Rational(denom, num);
	}
	
	// Every Rational number can be written as the sum of an integer and a proper fraction.
	public IntB part_int()
	{
		return num.div(denom);
	}
	
	public Rational part_frac()
	{
		Rational output = new Rational(num.abs().mod(denom), denom);
		
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
	
	public Rational abs()
	{
		if(isNegative())
		{
			return neg();
		}
		
		return this;
	}

	// -- Helper Functions
	@Override
	public Rational clone()
	{
		return new Rational(num, denom);
	}
	
	// FIXME : There is room for improvement for this hash function.
	@Override
	public int hashCode()
	{
		return num.hashCode() + denom.hashCode();
	}
	
	// FIXME : perhaps these generic functions should be refactored into one class.
	
	
	// FIXME : Phase this out when proper approximations such as pi come out in an approximation class.
	
	public double toDouble()
	{
		return num.toLong() * 1.0 / denom.toLong();
	}

	// Creation functions.
	
	@Override
	Rational N(long n)
	{
		if(n == 0)
		{
			return ZERO;
		}
		
		if(n == 1)
		{
			return ONE;
		}
		
		return new Rational(n);
	}
	
	private IntB I(long i)
	{
		// Hack due to the limitations of abstract classes.
		return IntB.ONE.N(i);
	}

	@Override
	public IntB toIntB()
	{
		return num;
	}

	@Override
	public int toInt()
	{
		return num.toInt();
	}
	
	@Override
	public String toString()
	{
		if(denom.equals(IntB.ONE))
		{
			return "" + num;
		}
		
		return "\\frac{" + num + "}{" + denom + "}";
	}

	public String toSerialString()
	{
		if(denom.eq(1))
		{
			return num.toString();
		}
		
		return "(" + num + ")/(" + denom + ")";
	}	
}
