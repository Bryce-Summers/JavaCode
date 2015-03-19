package BryceMath.Numbers;

import Data_Structures.ADTs.Pairable;
import Data_Structures.Structures.List;

/*
 * The Bryce representation of an 2's complement Integer.
 * Written by Bryce Summers on 1 - 8 - 2013.
 * Updated : 6 - 1 - 2013.
 * Purpose: Allows me to perform all of my numerical methods
 * 			on Integers that follow my representation.
 * 
 * UPDATE : This class has been replaced by the Integer Class.
 * 			I might put this in a code attic somewhere,
 *			because so far it is not going to see much use.
 */

// NOTE: This class is immutable.

public class FiniteInteger extends ModularNumber<FiniteInteger>
{
	// Integers are represented by integers!
	
	private final long val;
	
	// Minimum and maximum integral values for a two's complement 32 - bit integer.
	// Note: The first bit determines the sign of the integer.
	public static final int MAX_INT_VALUE = 2147483647; // 2^31 - 1;
	public static final int MIN_INT_VALUE = -2147483648;// 2^31
	
	// Identities.
	public static final FiniteInteger ZERO = new FiniteInteger(0);
	public static final FiniteInteger ONE  = new FiniteInteger(1);
	
	public FiniteInteger(int input)
	{
		val = input;
	}
	
	public FiniteInteger(long input)
	{
		val = input;
	}
	
	// -- Data access methods.

	
	
	public int toInt()
	{
		if (val > MAX_INT_VALUE)
		{
			throw new Error("Error: INTEGER to big to be converted to int.");
		}
		
		return (int)val;
	}
	
	public long toLong()
	{
		return val;		
	}
	
	// -- Number methods.
	
	@Override
	public FiniteInteger add(FiniteInteger n)
	{
		return i(val + n.val);
	}
	
	@Override
	public FiniteInteger add(int input)
	{
		return i(val + input);
	}
	
	@Override
	public FiniteInteger sub(FiniteInteger n)
	{
		return i(val - n.val);
	}
	
	@Override
	public FiniteInteger sub(int input)
	{
		return sub(i(val - input));
	}

	@Override
	public FiniteInteger mult(FiniteInteger n)
	{
		return i(val*n.val);
	}

	@Override
	public FiniteInteger mult(int n)
	{
		return i(val*n);
	}
	
	@Override
	public Pairable<FiniteInteger> division(FiniteInteger input)
	{
		return new List<FiniteInteger>(div(input), mod(input));
	}
	
	@Override
	public FiniteInteger div(FiniteInteger n)
	{
		return i(val/n.val);
	}
	
	@Override
	public FiniteInteger div(int input)
	{
		return i(val/input);
	}

	@Override
	public FiniteInteger mod(FiniteInteger n)
	{
		return i(val % n.val);
	}

	@Override
	public FiniteInteger mod(int input)
	{
		return i(val % input);
	}
	
	@Override
	public boolean eq(FiniteInteger other)
	{
		return val == other.val;
	}
	
	@Override
	public boolean eq(int other)
	{
		return val == other;
	}

	// FIXME: Implement Newton's method for integer square root calculations.
	
	@Override
	public FiniteInteger sqrt()
	{
		return i((int)Math.sqrt(val));
	}

	@Override
	public FiniteInteger zero()
	{
		return ZERO;
	}
	
	@Override
	public FiniteInteger one()
	{
		return ONE;
	}
	
	public int sign()
	{
		// Negative.
		if(val < 0)
		{
			return -1;
		}
		
		// Zero.
		if(val == 0)
		{
			return 0;
		}
		
		// Positive.
		return 1;
		
	}
	
	// -- Helper Methods.
	
	// Makes it easier to create integers, allows for laziness.
	// NOTE : This function does not take into account identities,
	// because it deals with primitives and therefore needs no further optimization.
	private FiniteInteger i(long input)
	{
		return new FiniteInteger(input);
	}
	
	// FIXME : Perhaps I should throw an exception for non integer values.
	
	@Override
	FiniteInteger N(long n)
	{
		int n2 = (int)n;
		
		return new FiniteInteger(n2);
	}


	// -- Object methods.
	
	@Override
	public String toString()
	{
		return "" + val;
	}

	@Override
	public int hashCode()
	{
		return (int)val;
	}

	@Override
	public FiniteInteger neg()
	{
		return new FiniteInteger(- val);
	}

	@Override
	public boolean isNegative()
	{
		return val < 0;
	}

	@Override
	public boolean isPositive()
	{
		return val > 0;
	}

	@Override
	public FiniteInteger abs()
	{
		if(val < 0)
		{
			return neg();
		}
		
		return this;
	}

	@Override
	public boolean isInt()
	{
		return true;
	}

	@Override
	public IntB toIntB()
	{
		return new IntB(val);
	}
}
