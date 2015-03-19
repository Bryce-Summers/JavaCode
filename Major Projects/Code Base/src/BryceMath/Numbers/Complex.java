package BryceMath.Numbers;

public class Complex extends Number<Complex>
{
	
	/*
	 * Complex number class,
	 * Written by Bryce Summers on 1 - 23 - 2013.
	 * Updated by Bryce Summers on 6 - 1  - 2013.
	 * Purpose: To provide complex number functionality for my math programs.
	 * Update : - Implemented the updated Number required functions.
	 * 			- Did some clean up.
	 * 			- Did some refactoring.
	 */
	
	// NOTE: This class is immutable.
	
	// FIXME: implement square roots.
	// FIXME: Allow for the calculation of magnitudes and other cool complex number stuff.
	// FIXME : Use Polar coordinates to calculate square roots.

	// Local data.
	private Rational real;
	private Rational imaginary;
	
	// Identities.
	public static final Complex ZERO = new Complex(0);
	public static final Complex ONE  = new Complex(1);
	
	// A Complex number representing the imaginary constant.
	public static final Complex I = new Complex(0, 1);
	
	public Complex(Rational real, Rational imaginary)
	{
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public Complex(Rational real)
	{
		this.real = real;
		imaginary = Rational.ZERO;
	}
	
	public Complex(IntB real)
	{
		this.real = new Rational(real);
		imaginary = Rational.ZERO;
	}
	
	public Complex(long real)
	{
		this.real = new Rational(real);
		imaginary = Rational.ZERO;
	}
	
	public Complex(int real)
	{
		this.real = new Rational(real);
		imaginary = Rational.ZERO;
	}
	
	public Complex(int real, int imaginary)
	{
		this.real = new Rational(real);
		this.imaginary = new Rational(imaginary);
	}
	
	public Complex(long real, long imaginary)
	{
		this.real = new Rational(real);
		this.imaginary = new Rational(imaginary);
	}
	
	/*
	// Create a complex number from a string.
	public Complex(String string)
	{		
		
		string = string.replace(" ", "");
		
		String[] parts = string.split("+");
		
		// Try to split the two terms, while not splitting the potential initial negative.
		if(parts.length == 1)
		{
			parts = string.substring(1).split("-");
		}
		
		// Tack on the first character again.
		parts[0] = string.charAt(0) + parts[0];
		
		int real_sign;
		
		if(charAt)
		
		// Parse the real and imaginary parts.
		if(parts.length == 2)
		{
			
			real = new Rational(parts[0].replace(" ", ""));
			
			
			
			imaginary = new Rational(parts[1].replace("i", ""));
			return;
		}
		
		// Parse a completely real or completly imaginary string.
		
	}*/
	
	
	
	// -- Complex Helper functions.

	private Complex C(Rational r, Rational i)
	{
		// -- Deal with real rational identities.
		
		// if real...
		if(i.eq(0))
		{
			// zero...
			if(r.eq(0))
			{
				return ZERO;
			}
			
			// one...
			if(r.eq(1))
			{
				return ONE;
			}
		}
		return new Complex(r, i);
	}
	
	protected Complex N(long input)
	{
		if(input == 0)
		{
			return ZERO;
		}
		
		if(input == 1)
		{
			return ONE;
		}
		
		return new Complex(input);
	}
	
	// Returns the conjugation of this number.
	@Override
	public Complex conj()
	{
		return C(real, imaginary.neg());
	}

	// -- Number math functions.
	
	@Override
	public Complex add(Complex n)
	{
		return C(real.add(n.real), imaginary.add(n.imaginary));
	}

	@Override
	public Complex sub(Complex n)
	{
		return C(real.sub(n.real), imaginary.sub(n.imaginary));
	}
	
	@Override
	public Complex div(Complex n)
	{
		//(a + bi)/(c + di) = (ac + bd)/(c^2 + d^2) + (bc - ad)i / (c^2 + d^2)
		Rational r = real.mult(n.real).add(imaginary.mult(n.imaginary)).div(n.real.mult(n.real).add(n.imaginary.mult(n.imaginary)));
		Rational i = imaginary.mult(n.real).sub(real.mult(n.imaginary)).div(n.real.mult(n.real).add(n.imaginary.mult(n.imaginary)));
		return C(r, i);
	}

	@Override
	public boolean eq(Complex other)
	{
		// Returns true iff and only iff both of the rational coefficients are equal.
		// Remember that the rational coefficients reduce themselves automatically to simplest terms. 
		return real.eq(other.real)&& imaginary.eq(other.imaginary); 
	}

	@Override
	public Complex mult(Complex n)
	{
		Rational r = real.mult(n.real).sub(imaginary.mult(n.imaginary));
		Rational i = real.mult(n.imaginary).add(imaginary.mult(n.real));
		
		return C(r, i);
	}

	// FIXME: Implement a real square root function!
	
	
	// $%($#%&$()%&#$%)($#&%)(#$&%)#$(&%)(#~~~~!!!!!!)!!!!!!
	
	@Override
	public Complex sqrt()
	{
		throw new Error("Complex square roots have not yet be conceptualized.");
	}

	@Override
	public Complex one()
	{
		// Returns a complex number with real coefficient 1,
		// and imaginary coefficient 0;
		return ONE;
	}
	
	@Override
	public Complex zero()
	{
		return ZERO;
	}
	
	public Rational part_real()
	{
		return real;
	}
	
	public Rational part_imaginary()
	{
		return imaginary;
	}

	@Override
	public int hashCode()
	{
		return real.hashCode() + imaginary.hashCode();
	}
	
	// -- Sign data and setting functions that extract the sign data from the real part.
	public Complex neg()
	{
		return new Complex(real.neg(), imaginary.neg());
	}
	
	public boolean isNegative()
	{
		return sign() == -1;
	}
	
	public boolean isPositive()
	{
		return sign() == 1;
	}
	
	public int sign()
	{
		int output = real.sign();
		
		if(output != 0)
		{
			return output;
		}
		
		return imaginary.sign();
	}
	
	public Complex abs()
	{
		if(sign() < 0)
		{
			return neg();
		}
		
		return this;
	}

	// Returns the complex number representing I to the specified power.
	public static Complex power_of_i(int i)
	{
		return power_of_i(new IntB(i));
	}

	// Returns the complex number representing I to the specified power.
	public static Complex power_of_i(IntB i)
	{
		// We know that this will be in bounds, because we are modulating by 4.
		int Case = i.mod(4).toInt();
		
		switch(Case)
		{
		// i^0, i^4, ect (an equivilance class).
		case 0 : return ONE;
		
		case 1 : 
		case -3 : return I;
		
		case 2 : 
		case -2:
			return ONE.neg();
			
		case 3  :
		case -1 :
			 return I.neg();
			 
		default : throw new Error("Something is wrong with IntB.mod");
		}
	}

	@Override
	public boolean isInt()
	{
		return imaginary.eq(0) && real.isInt();
	}

	@Override
	public IntB toIntB()
	{
		return real.toIntB();
	}

	@Override
	public int toInt()
	{
		return real.toInt();
	}
	
	// Converts this number's internal information into human readable mathematical notation.
	public String toString()
	{
		
		// Trivial imaginary component.
		if(imaginary.eq(0))
		{
			return "" + real;
		}
		
		// Trivial real component;
		if(real.eq(0))
		{
			return "" + imaginary.toCoef() + " i";
		}
		
		// -- Two non trivial components.
		
		if(imaginary.isPositive())
		{
			return "" + real + " + " + imaginary.toCoef() + "i";
		}

		return "" + real + " - " + imaginary.abs().toCoef() + "i";
		
	}

	public String toSerialString()
	{
		if(eq(0))
		{
			return "0";
		}
		
		if(imaginary.eq(0))
		{
			return real.toSerialString();
		}
		
		if(real.eq(0))
		{
			return imaginary.toSerialString() + "i";
		}
		
		return real.toSerialString() + " + " + imaginary.toSerialString() + "i";
	}
}
