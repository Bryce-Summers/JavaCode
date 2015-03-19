package BryceMath.Numbers;

import static BryceMath.Logic.Boolean_Logic.xor;

import java.util.Iterator;

import BryceMath.Calculations.MathB;
import Data_Structures.ADTs.Pairable;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;

/*
 * The Bryce representation of an arbitrarily large Integer.
 * 
 * Written by Bryce Summers on 1 - 8 - 2013.
 * Major Update : 5 - 30 - 2013 - 6 - 1 - 2013.
 * Finished : 6 - 1 - 2013. Most functions have been tested in aaTesting.
 * 
 * Purpose: Allows me to perform all of my numerical methods
 * 			on Integers that follow my representation.
 * 			This class represents natural numbers, which are attached to a positive or negative sign.
 * 			All arithmetic operations respect this sign.
 */

// NOTE: This class is immutable.
// NOTE : Bit operations work on the body of booleans and then perform the operations on the sign of the numbers. 

// FIXME : Consider reimplementing addition / other arithmetic operations through parallel bit level boolean operations.
// FIXME : Refactor this code into a top down piece of code.

public class IntB extends ModularNumber<IntB> implements Iterable<Boolean>, Comparable<IntB>
{	
	// -- Local data.
	
	// Integers are represented by an array of binary numbers.
	// The 0th bit is the 1s digit, the 1st bit is the 2's digit, the 2nd bit is the 4's digit, ect. 
	private UBA<Boolean> data;
	
	// The sign of the number is given by this internal flag.
	// This should be true iff the number is 0 or positive. 
	private boolean positive = true;

	
	// -- public constants.
	public static final IntB ZERO = new IntB(0);
	public static final IntB ONE  = new IntB(1);
	
	// Minimum and maximum integral values for a two's complement 32 - bit integer.
	// Note: The first bit determines the sign of the integer.
	public static final int MAX_FINITE_INT_VALUE = 2147483647; // 2^31 - 1;
	public static final int MIN_FINITE_INT_VALUE = -2147483648;// 2^31
	
	// Note this is probably excessive, because the long version will handle it.
	public IntB(int input)
	{
		// Handle the extreme low values.
		if(input == MIN_FINITE_INT_VALUE)
		{
			positive = false;
			data = new UBA<Boolean>(32);
			for(int i = 0; i < 31; i++)
			{
				data.add(false);
			}
			data.add(true);
			return;
		}
		
		// Set the sign flag.
		positive = input >= 0;
		
	    // Make sure we calculate the number of digits for a positive number.
		input = Math.abs(input);
		
				
		// FIXME : Implement a logA method using integers. Also check the following line.
		int digit_num = (int)Math.max(MathB.logA(input, 2), 1);
		data = new UBA<Boolean>(digit_num);

		// Convert the incoming number to binary and insert it in least significant to most significant bit order.
		while(input > 0)
		{
			data.add(input % 2 == 1);
			input = input / 2;
		}
	}
	
	public IntB(long input)
	{
		
		// Handle the extreme low values.
		if(input == Long.MIN_VALUE)
		{
			positive = false;
			data = new UBA<Boolean>(64);
			for(int i = 0; i < 63; i++)
			{
				data.add(false);
			}
			data.add(true);
			return;
		}
		
		// Set the sign flag.
		positive = input >= 0;
		
	    // Make sure we calculate the number of digits for a positive number.
		input = Math.abs(input);
		
		// FIXME : Implement a logA method using integers. Also check the following line.
		int digit_num = (int)Math.max(MathB.logA(input, 2), 1);
		data = new UBA<Boolean>(digit_num);

		// Convert the incoming number to binary and insert it in least significant to most significant bit order.
		while(input > 0)
		{
			data.add(input % 2 == 1);
			input = input / 2;
		}
	}
	
	// Allows Integers to be constructed from strings.
	public IntB(String input)
	{
		char[] numbers = input.toCharArray();
		
		int len = numbers.length;
		
		// Handle zero input.
		if(len == 0)
		{
			data = new UBA<Boolean>();
			positive = true;
			return;
		}
		
		// index of current number.
		int i = 0;
		
		positive = true;
		
		// Handle negative input.
		if(numbers[0] == '-')
		{
			positive = false;
			i++;
			
			if(len == 1)
			{
				throw new Error("Error : Invalid Number '-' ");
			}
		}		
		
		// Efficiently convert the String from decimal to Binary.
		BaseNumber binary = new BaseNumber(2);
		
		for(int k = i; k < len; k++)
		{
			binary.mult(10);
			
			// Cast the characters to numbers.
			int digit = (int)(numbers[k]) - 48;
			binary.add(digit);

		}

		// Now populate this Integer's data with the bits from the BaseNumber.
		char[] bits = binary.toString().toCharArray();
		
		len = bits.length;
		
		data = new UBA<Boolean>(len);
	
		for(int k = 0; k < len; k++)
		{
			
			boolean bit = bits[len - 1 - k] == '1';
			data.add(bit);
		}
		
		compress();

	}
	
	private IntB(UBA<Boolean> input, boolean sign)
	{
		// Set sign flag.
		positive = sign;
		
		// Set magnitude data.
		data = input;
	}
	
	@Override
	protected IntB N(long i)
	{
		if(i == 0)
		{
			return ZERO;
		}
		
		if(i == 1)
		{
			return ONE;
		}
		
		return new IntB(i);
	}
	
	// -- Data access methods.

	public long toLong()
	{
		int len = num_digits();

		// Handle low extreme.
		if(!positive && len == 64 && getNum1s() == 1)
		{
			return Long.MIN_VALUE;
		}
		
		// Deal with Long over and underflow.
		if (len > 63)
		{
			throw new Error("ERROR: Longs can only hold 63 bits of information.");
		}
		
		long output = 0;
		
		long mult = 1;
		
		// Interpret the binary data in decimal.
		for(Boolean b : data)
		{
			if(b)
			{
				output += mult;
			}
			
			mult = mult*2;
		}
		
		if(positive)
		{
			return output;
		}
		else
		{
			return -output;
		}
		
	}
	
	@Override
	public int toInt()
	{
		int len = num_digits();

		// Handle low extreme.
		if(!positive && len == 32 && getNum1s() == 1)
		{
			return MIN_FINITE_INT_VALUE;
		}
		
		// Deal with integer over and underflow.
		if (len > 31)
		{
			throw new Error("ERROR: Ints can only hold 31 bits of information.");
		}
		
		int output = 0;
		
		int mult = 1;
		
		// Interpret the binary data in decimal.
		for(Boolean b : data)
		{
			if(b)
			{
				output += mult;
			}
			
			mult = mult*2;
		}
		
		if(positive)
		{
			return output;
		}
		else
		{
			return -output;
		}
	}
	
	// Efficiently returns an immutable negation of this Integer.
	// O(1) time complexity. O(1) extra memory.
	public IntB neg()
	{
		// Perform no foolish trickery if this Integer is 0.
		if(eq(0))
		{
			return ZERO;
		}
		
		// NOTE : this new Integer will share data,
		// but will be safe due to the separation of data and sign.
		return new IntB(data, !positive);
	}

	@Override
	public boolean isPositive()
	{
		return sign() == 1;
	}

	@Override
	public boolean isNegative()
	{
		return !positive;
	}
	
	// -- Equality and comparison functions.

	// This method returns true iff the other BigInteger represents the same number.
	// This method assumes that both BigIntegers keep their arrays compressed.
	public boolean eq(IntB other)
	{
		
		// Handle positive != negative.
		if(this.positive != other.positive)
		{
			return false;
		}
		
		UBA<Boolean> data2 = other.data;
		int len = data.size();
		
		// Make sure both arrays are of the same size.
		if(data2.size() != len)
		{
			return false;
		}
		
		// Make sure that each element inside of the arrays represent the same boolean value.
		for(int i = 0; i < len; i++)
		{
			if((boolean)data.get(i) != (boolean)data2.get(i))
			{
				return false;
			}
		}
		
		return true;
		
	}
	
	// REQUIRES: this and the other BigInteger must be compressed.
	public int compareTo(IntB other)
	{
		
		boolean s1 = this .sign() >= 0;
		boolean s2 = other.sign() >= 0;
		
		// positive compare to a negative.
		if(s1 && !s2)
		{
			// greater than.
			return 1;
		}
		
		if(!s1 && s2)
		{
			// less than.
			return -1;
		}
		
		// Negative compared to a negative.
		if(!s1 && !s2)
		{			
			// return the negation of the comparison of the two number's negations.
			return -1*this.neg().compareTo(other.neg());
		}
		
		// Assumes that two positive numbers are now being compared.
		
		
		UBA<Boolean> data2 = other.data;
		int len1 = data.size();
		int len2 = data2.size();
		
		// Handle the case where the other's data is of a larger order of magnitude.
		if(len1 < len2)
		{
			return -1;
		}
		
		// Handle the case where the other's data is of a smaller order of magnitude.
		if(len2 < len1)
		{
			return 1;
		}
		
		// All of the lengths should be the same.
		int len = len1;// = len2;
		
		// Make sure that each element inside of the arrays represents the same boolean value.
		for(int i = len - 1; i >= 0; i--)
		{
			boolean b1 = getDigit(i);
			boolean b2 = other.getDigit(i);
			
			// If we have not detected an inequality, then we should continue.
			if((boolean)b1 == (boolean)b2)
			{
				continue;
			}
			
			// This big integer is greater than the other BigInteger,
			// because it has a greater first unequal significant bit.
			if(b1)
			{
				return 1;
			}
			
			// This big integer is less than the other BigInteger,
			// because it has a lesser first unequal significant bit.
			return -1;
		}
		
		// They must be equal.
		return 0;
	}
	
	// Addition.
	// REQUIRES: this and the other BigInteger should be compressed.
	// ENSURES : return a BigInteger representation of the addition between this
	//           and the other BigInteger's represented numbers.
	public IntB add(IntB other)
	{		
		// No overflows are possible!!!
		
		// - Deal with signs.
		
		int sign1 = sign();
		int sign2 = other.sign();
		
		// Return the other BigInteger trivially if this is zero.
		if(sign1 == 0)
		{
			return other;
		}
		
		// Return this BigInteger trivially if the other is zero.
		if(sign2 == 0)
		{
			return (IntB)(this);
		}

		boolean positive1 = sign1 == 1;
		boolean positive2 = sign2 == 1;
		
		// -- Handle the cases where the signs are not equal.
		
		// positive + negative. (x) + (-y) => (x) - (y)
		if(positive1 && !positive2)
		{
			return sub(other.neg());
		}
		
		// negative + positive. (-x) + (y) => (y) - (x).
		if(!positive1 && positive2)
		{
			// retrieve the output of the calculation.
			return other.sub(this.neg());
		
		}
		
		// -- From this point on , we should be performing addition on two integers of equal sign.
		// ... ASSERT(positive1 == positive2).
		
		// Compute the maximal length of the two BigIntegers.
		int len = Math.max(this.num_digits(), other.num_digits());
		
		// Create a new data UBA that already has a limit that is as large as a possible result would need.
		UBA<Boolean> outputData = new UBA<Boolean>(len + 1);
		
		// Prepare the output Integer with the correct sign.
		IntB output = new IntB(outputData, positive1);
		
		boolean carry = false;
		
		// Iterate through the digits.
		for(int d = 0; d < len; d++)
		{
			// Compute the value of this digit's computation.
			// The final result will be 0, 1, 2, or 3.
			short val = 0;
			
			if(carry)
			{
				val++;
			}
			
			if(getDigit(d))
			{
				val++;
			}
			
			if(other.getDigit(d))
			{
				val++;
			}
			
			// Set the digit to the base 2 modulus.
			output.setDigit(d, val % 2 == 1);
			
			// Set the carry to the val / 2;
			carry = val > 1;
		}
		
		// Add the carry to the output number.
		if(carry)
		{
			output.setDigit(len, true);
		}
		
		// Note: No compression is needed,
		// because there is no case in which leading zeros would be introduced.
		
		return output;
		
	} // End of Add.

	// Subtraction.
	public IntB sub(IntB other)
	{
		// - Deal with signs.		
		
		int sign1 = sign();
		int sign2 = other.sign();
		
		// Return the other BigInteger trivially if this is zero.
		if(sign1 == 0)
		{
			return other.neg();
		}
		
		// Return this BigInteger trivially if the other is zero.
		if(sign2 == 0)
		{
			return this;
		}
		
		// Handle the case where the signs are not equal.
		if(sign1 != sign2)
		{
			// Efficiently negate the other through a mutation.
			return add(other.neg()); 
		}
				
		// -- Now perform subtraction on BigIntegers of the same sign.
		
		// First determine the order of the absolute values of two BigIntegers.
		int comp = abs(this).compareTo(abs(other));
		
		// If the two numbers are equal,
		// then subtraction will result in the value zero.
		if(comp == 0)
		{
			return ZERO;
		}
		
		// The bigger  will be mutated by subtracting the smaller.
		// The smaller will not be mutated.
		IntB bigger, smaller;
		boolean output_positive;
		
		// - Fist we must create a new BigInteger that will start being the bigger BigInteger,
		//   but will be mutated into Output form by subtracting the smaller.
		
		// The other is bigger.
		if(comp < 0)
		{
			bigger  = other.clone();
			smaller = this;
			output_positive = !bigger.positive;
		}
		else // The other is smaller.
		{
			bigger  = this.clone();
			smaller = other;
			output_positive = bigger.positive;
		}
		
		// -- Now just subtract the smaller from the bigger,
		//    and tack on the correct sign.
		
		int len = smaller.num_digits();
		
		// Perform subtraction as our grade school teachers taught us.
		// Going bit by bit.
		for(int i = 0; i < len; i++)
		{
			// extract the subtrahend.
			boolean b2 = smaller.getDigit(i);
			
			// No work is necessary to subtract 0.
			if(!b2)
			{
				continue;
			}
			
			// extract the minuend. 
			boolean b1 = bigger.getDigit(i);

			// If the bigger digit is 1,
			// then the result is 0. (1 - 1 = 0).
			if(b1)
			{
				bigger.setDigit(i, false);
				continue;
			}
			
			// If the big digit is 0, then we need to search for another 1 to carry back.
			// We know that is must exist, or else smaller would be bigger than bigger.
			
			// First we know that the current bit will be a 1, because 2 - 1 = 1;
			bigger.setDigit(i, true);
			
			// Search for the next true bit,
			// performing accurate subtraction along the way.
			for(;;)
			{
				// Handle the case where we have found the bit.
				if(bigger.getDigit(i + 1))
				{
					// negate the bit, and carry on with subtraction starting at this bit.
					bigger.setDigit(i + 1, false);
					break;
				}
				
				i++;
				
				// All bits in between will be the negation of the smaller number,
				// because they will either be 1 - 1 = 0, or 1 - 0 = 1.
				boolean nDigit = !smaller.getDigit(i);
				bigger.setDigit(i, nDigit);
			}
			
		}// End of the for loop that iterates through the bits.

		// Now polish off the final output.
		IntB output = bigger;
		
		// Subtraction may lead to decompressed results. (1111 - 1100 = 0011 => 11).
		output.compress();
		
		// Set the output
		output.positive = output_positive;
		
		// Once we have iterated through all of the bits, then we are done.
		return output;
	}
	
	// -- Helper Methods.
	// Returns the current digit at position digitNum. in the number 1234,
	// the 4 is the 0th digit, the 3 is the 1st, etc.
	Boolean getDigit(int digitNum)
	{
		
		// Handle erroneous digit location queries.
		if(digitNum < 0)
		{
			throw new Error("Error: Negative digits do not exist.");
		}
		
		
		int len = data.size();
		
		if(digitNum < len)
		{
			return data.get(digitNum);
		}
		
		// All bits that are too significant to be defined are leading zeroes.
		return false;
	}
	
	// Returns the current digit at position digitNum.
	// For example : in the number 1234,
	// the 4 is the 0th digit, the 3 is the first, etc.
	private void setDigit(int digitNum, Boolean digit)
	{
		
		if(digitNum < 0)
		{
			throw new Error("Error: Negative digits do not exist, and therefore cannot be set.");
		}
		
		int len = data.size();
		
		// Handle the setting of a not leading 0 bit.
		if(digitNum < len)
		{
			data.set(digitNum, digit);
			
			// Make sure the number returned is canonical.
			compress();
			
			return;
		}
		
		// There is no point setting a leading zero to be zero.
		if(digit == false)
		{
			return;
		}
		
		// Tack on zeroes until we get to the digit that we want to set,
		// because we need to fill in place holder zeroes to preserve
		// the desired magnitude of the number to be set.
		while(len < digitNum)
		{
			data.add(false);
			len++;
		}
		
		data.add(true);
		
	}
	
	// The compress function compresses a BigInteger's representation to 
	// only include the bare minimum number of bits.
	private void compress()
	{
		int len = data.size();
		
		int i   = len - 1;
		
		// Remove each and every extra digit.
		while(i >= 0)
		{
			// Stop once we have reached a most significant bit.
			if(getDigit(i))
			{
				return;
			}
			
			// Remove the leading zero from the number.
			data.rem();
			
			i--;
			
		}
	}
	
	// Returns the default base 10 representation of this Integer.
	@Override
	public String toString()
	{
		return toBase(10);
	}
	
	// Returns the base num_columns representation of this Integer
	public String toBase(int n)
	{
		/*
		 * Use the BaseNumber class to implement printing.
		 * take each digit starting from largest going to least and switch between
		 * adding them to the baseNumber and doubling the base number.
		 */
		
		// Construct a BaseNumber to represent the output representation.
		// Decimal by default.
		BaseNumber output = new BaseNumber(n);
		
		if(!positive)
		{
			output.neg();
		}
		
		int len = data.size();
		
		for(int i = 0; i < len; i++)
		{
			// We are converting from binary to decimal.
			output.mult(2);
			
			// Go through the digits from most significant to least significant.
			if(getDigit(len - 1 - i))
			{
				output.add(1);
			}
		}
		
		return output.toString();
	}

	// -- Identity functions.
	
	@Override
	public IntB zero()
	{
		return ZERO;
	}

	@Override
	public IntB one()
	{
		return ONE;
	}

	// returns the product of this and the input Integer.
	@Override
	public IntB mult(IntB input)
	{
		// Name the two factors.
		IntB in1 = this;
		IntB in2 = input;
		
		// Calculate the sign of the output.
		int s1 = in1.sign();
		int s2 = in2.sign();
		
		// Handle multiplication involving a factor which is zero.
		if(s1 == 0 || s2 == 0)
		{
			return ZERO;
		}
		
		boolean b1 = s1 == 1;
		boolean b2 = s2 == 1;
		
		// isPositive.
		boolean output_sign = (!xor(b1, b2));
				
		// -- Handle Multiplicative identities.
		if(abs(in1).eq(ONE))
		{
			IntB output = abs(in2);
			
			// Positive.
			if(output_sign)
			{
				return output;
			}
			
			// Negative.
			return output.neg();
		}
		
		if(abs(in2).eq(ONE))
		{
			IntB output = abs(in1);
			
			// Positive.
			if(output_sign)
			{
				return output;
			}
			
			// Negative.
			return output.neg();
		}
		
		/*
		 * Compute the sign of the product.
		 * Count the number of true values in this
		 * Generate all of the required multiples
		 */
		
		// - Used to keep track of the output number.
		IntB output = ZERO;
		
		IntB bigger, smaller;
		
		if(in1.getNum1s() < in2.getNum1s())
		{
			bigger  = in2;
			smaller = in1;
		}
		else
		{
			bigger  = in1;
			smaller = in2;
		}
		
		// - Used to add powers of 2 multiples of bigger to the output.
		IntB adder  = abs(bigger).clone();

		// Perform grade school multiplication.
		for(boolean b : smaller)
		{
			if(b)
			{
				output = output.add(adder);
			}
			
			adder = adder.bitShiftL();

		}
		
		// Positive.
		if(output_sign)
		{
			return output;
		}
		
		// Negative.
		return output.neg();
		
	}
	
	@Override
	public IntB clone()
	{
		return new IntB(data.copy(), positive);
	}
	
	// Safe bit shift left.
	public IntB bitShiftL()
	{
		IntB output = clone();
		
		// Efficiently mutate it.
		output.mult2();
		
		return output;
	}
	
	// Safe bit shift right.
	public IntB bitShiftR()
	{
		IntB output = clone();
		
		// Efficiently mutate it.
		output.div2();
		
		return output;
	}
	
	// Destructively multiplies this number by 2.
	// Bit shift left.
	private void mult2()
	{
		// Add a 0 to the least significant end of the number.
		data.prepend(false);
		
		return;
	}
	
	// Destructively divides this number by 2.
	// Bit shift right.
	private void div2()
	{
		// Remove the least significant bit.
		data.deq();
	}
	
	public IntB AND(IntB input)
	{
		IntB i1 = this;
		IntB i2 = input;
		
		int digits1 = i1.num_digits();
		int digits2 = i2.num_digits();
		
		// NOTE : Leading zeros will result in false,
		// so we only need to iterate to the minimum.
		int len = Math.min(digits1, digits2);
	
		// Handle Zero.
		if(len == 0)
		{
			return ZERO;
		}
		
		UBA<Boolean> output_data = new UBA<Boolean>(len);
		
		for(int i = 0; i < len; i++)
		{
			output_data.add(i1.getDigit(i) && i2.getDigit(i));
		}
		
		boolean output_sign = i1.positive && i2.positive;
		IntB output = new IntB(output_data, output_sign); 
		
		// Decompressed data is possible.
		output.compress();

		return output;
	}
	
	public IntB AND(int input)
	{
		return AND(N(input));
	}	

	public IntB OR(IntB input)
	{
		IntB i1 = this;
		IntB i2 = input;
		
		int digits1 = i1.num_digits();
		int digits2 = i2.num_digits();
		
		int len = Math.max(digits1, digits2);

		// Handle Zero.
		if(len == 0)
		{
			return ZERO;
		}
		
		UBA<Boolean> output_data = new UBA<Boolean>(len);
		
		for(int i = 0; i < len; i++)
		{
			output_data.add(i1.getDigit(i) || i2.getDigit(i));
		}
		
		boolean output_sign = i1.positive || i2.positive;
		IntB output = new IntB(output_data, output_sign); 
		
		// Decompressed data is possible.
		output.compress();
		
		return output;
	}
	
	public IntB OR(int input)
	{
		return OR(N(input));
	}
	
	public IntB XOR(IntB input)
	{
		IntB i1 = this;
		IntB i2 = input;
		
		int digits1 = i1.num_digits();
		int digits2 = i2.num_digits();
		
		int len = Math.max(digits1, digits2);
		
		// Handle Zero.
		if(len == 0)
		{
			return ZERO;
		}
		
		UBA<Boolean> output_data = new UBA<Boolean>(len);
		
		for(int i = 0; i < len; i++)
		{
			output_data.add(xor(i1.getDigit(i), i2.getDigit(i)));
		}
		
		boolean output_sign = xor(i1.positive, i2.positive);
		IntB output = new IntB(output_data, output_sign); 
		
		// Decompressed data is possible.
		output.compress();
		
		return output;
	}
	
	public IntB XOR(int input)
	{
		return XOR(N(input));
	}
	
	// Returns the number of ones in a bigInteger.
	int getNum1s()
	{
		int output = 0;
		
		for(boolean b : this)
		{
			if(b)
			{
				output++;
			}
		}
		
		return output;
	}
	
	// Returns the number of digits in this number.
	public int num_digits()
	{
		return data.size();
	}
	
	// Returns a pair with a quotient and remainder.
	public Pairable<IntB> division(IntB input)
	{		
		if(input.eq(ZERO))
		{
			throw new Error("Cannot divide or modulo by zero");
		}
		
		// Handle trivial division.
		if(input.eq(ONE))
		{
			List<IntB> output = new List<IntB>();
			
			// Add quotient.
			output.add(this);
			
			// Add remainder.
			output.add(ZERO);
			return output;
		}
		
		// Mutable quotients and remainders.
		IntB quotient  = ZERO.clone();
		IntB remainder = ZERO.clone();
		
		// Immutable numerator and divisor. WARNING : These have lost their sign information.
		IntB numerator = abs(this);
		IntB divisor   = abs(input);
		
		int len = Math.max(numerator.num_digits(), divisor.num_digits());

		// Standard bitwise long division.
		for(int i = len - 1; i >= 0; i--)
		{
			  // Bit shift the remainder left.
			  remainder = remainder.bitShiftL();
			  remainder.setDigit(0, numerator.getDigit(i));

			  if(remainder.compareTo(divisor) >= 0)
			  {
				  remainder = remainder.sub(divisor);
				  quotient.setDigit(i, true);
			  }
		}
		
		// Create a list that will act as a return pair.
		List<IntB> output = new List<IntB>();

		// -- Handle negative calculations.
		
		/* For reference:
		 * 
		 *  78 %  67 =  11
		 * -78 %  67 =  56
		 *  78 % -67 =  56
		 * -78 % -67 = -11
		 */
		
		IntB signed_numerator = this;
		IntB signed_divisor   = input;

		// Actual signs, not absolute value signs.
		boolean quotient_sign = !xor(signed_numerator.positive,
									 signed_divisor  .positive);
		
		if(quotient_sign)
		{
			if(!signed_numerator.positive)
			{
				remainder = remainder.neg();
			}
		}
		else
		{
			// Negate the quotient.
			quotient  = quotient.neg();
			
			// Correct the remainder.
			remainder = remainder.sub(divisor);
			
			// negate the remainder if needed.
			if(signed_divisor.positive)
			{
				remainder = remainder.neg();
			}
		}
				
		// Add data to the output pair. (list<Integer>).
		output.add(quotient);
		output.add(remainder);
		
		return output;

	}

	@Override
	public IntB sqrt()
	{
		throw new Error("Square root not implemented");
	}

	private IntB abs(IntB input)
	{
		if(!input.positive)
		{
			return input.neg();
		}
		
		return input;

	}
	
	// Returns the absolute value of this Integer.
	public IntB abs() 
	{
		if(positive)
		{
			return this;
		}
		
		return neg();
	}
	
	@Override
	public boolean eq(int other)
	{
		return eq(N(other));
	}
	
	@Override
	public int sign()
	{
		// Handle zero case.
		if(eq(0))
		{
			return 0;
		}
		
		// Handle the positive case.
		if(positive)
		{
			return 1;
		}
		
		// Handle the negative case.
		return -1;
	}

	@Override
	public int hashCode()
	{
		int add = 1;
		int output = 0;
		
		for(boolean b : this)
		{
			if(b)
			{
				output += add;
			}
			
			add *= 2;
		}
		
		return output;
	}

	// Allows a user to iterate through the bits from least significant to most significant.
	// Makes no implication of the sign.
	@Override
	public Iterator<Boolean> iterator()
	{
		return data.iterator();
	}

	// -- These methods are rather silly, but we might as well be consistent...
	@Override
	public boolean isInt()
	{
		return true;
	}

	@Override
	public IntB toIntB()
	{
		return this;
	}
}