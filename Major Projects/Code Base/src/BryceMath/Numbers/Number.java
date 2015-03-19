package BryceMath.Numbers;

import java.io.PrintStream;

import util.SerialB;

/*
 * Number class,
 * Written by Bryce Summers 12 - 21 - 2012.
 * Major Update : 5 - 30 - 2013. 
 * Purpose: Used to implement various interchangeable number classes.
 * 
 * FIXME : Perhaps standardize toSerialString functions!
 */

public abstract class Number<T> implements SerialB, Comparable<T>
{
	
	// NOTE : All number implemented by this class should be immutable.
	// NOTE : Null values will be treated as zero for equality.
	
	// -- Constructor function.
	// N stands for number.
	abstract T N(long n);
	
	// Identities.
	public abstract T zero();
	public abstract T one();
	
	// -- Elementary arithmetic operations.
	public abstract T add (T input);
	public T add (int input)
	{
		return add(N(input));
	}
	
	public abstract T sub (T input);
	public T sub (int input)
	{
		return sub(N(input));
	}
	
	public abstract T mult(T input);
	public T mult (int input)
	{
		return mult(N(input));
	}
	
	// -- Division
	
	public abstract T div (T input);

	
	public T div (int input)
	{
		return div(N(input));
	}
			
	// -- Rather necessary.
	public abstract T sqrt();
	
	@SuppressWarnings("unchecked")
	public T sqr()
	{
		T number = (T)this;
		return mult(number);
	}
	
	public T pow(int pow)
	{
		return pow(new IntB(pow));
	}
	
	/* Computes this number raised to the given power.
	 * This method should be very efficient, given an efficient multiplication implementation.
	 */
	@SuppressWarnings("unchecked")
	public T pow(IntB pow)
	{
		
		if(pow.isNegative())
		{
			throw new Error("Negative powers are not supported."); 
		}
		
		if(pow.eq(0))
		{
			return one();
		}
		
		if(pow.eq(1))
		{
			return (T)this;
		}
		
		IntB binary = pow;
		
		int len = binary.num_digits() + 1;
		
		Number<T> output_product = (Number<T>)one();
		
		Number<T> factor = (Number<T>)(this);
		
		// Compute the output factor from the powers
		// of two of the original number (this).
		for(int i = 0; i < len; i++)
		{
			if(binary.getDigit(i))
			{
				output_product = (Number<T>) output_product.mult((T)factor);
			}
			
			if(i < len - 1)
			{
				factor = (Number<T>) factor.sqr();
			}
		}
		
		return (T)output_product;

	}
	
	// Equalities.
	public abstract boolean eq(T other);
	public boolean eq (int input)
	{
		return eq(N(input));
	}
	
	// Object functions.
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other)
	{
		// Treat null values as zeros.
		if(other == null)
		{
			return this.equals(zero());
		}
		
		// Handle ints
		if(other instanceof java.lang.Integer)
		{
			return eq((java.lang.Integer)other);
		}
		
		// Handle equality of the same types. 
		if(this.getClass() != other.getClass())
		{
			return false;
		}
		
		return eq((T) other);
		
	}
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract String toString();
	
	// Returns a string that is optimized for use as a coefficient. ("1" -> "").
	public String toCoef()
	{
		String str = toString();

		// Convert 1s to empty strings.
		if(str.equals("1"))
		{
			return "";
		}
		
		if(str.equals("-1"))
		{
			return "-";
		}
		
		// Add parentheses if there is a non negation connective.
		if(str.contains("+")|| str.substring(1).contains("-"))
		{
			return "(" + str + ")";
		}

		return str + "";
	}
	
	// -- Sign functions.
	// -- Sign data and setting functions that extract the sign data from the real part.
	public abstract T neg();
	
	/**
	 * @return the conjugate of this number. 
	 * This should be overridden by classes that could contain imaginary values.
	 */
	// numbers that might actually have an imaginary part.
	@SuppressWarnings("unchecked")
	public T conj()
	{
		return (T)this;
	}
	
	
	public abstract boolean isNegative();
	
	public abstract boolean isPositive();

	
	public abstract int sign();
	
	public abstract T abs();
	
	public void serializeTo(PrintStream stream)
	{
		throw new Error("Serialization of " + getClass() + "is not yet supported!");
	}
	
	public String getSerialName()
	{
		return getClass().toString();
	}
	
	public int compareTo(T other)
	{
		throw new Error("Comparisons of " + getClass() + "is not yet supported!");
	}
	
	
	// -- Number conversion Functions.
	// This could use some work.
	public abstract boolean isInt();
	
	public abstract IntB toIntB();
	
	public abstract int toInt();
	
}
