package BryceMath.Numbers;

import Data_Structures.Structures.UBA;

/* 
 * The Base Number class.
 * Written by Bryce Summers on 3 - 22 - 2013.
 * 
 * Purpose: This number provides a base num_columns representation of a number.
 * 
 * It allows its numbers to be added and multiplied.
 * This should be used to help out the other number systems with tasks such as printing.
 * 
 * These numbers are represented by an Unbounded array, with each entry corresponding to
 *   the digit associated with base exponentiated to the indice's base.
 *   The 0th entry represents the sign of the number.
 *   
 *   This class is completely destructive, so watch out with your data.
 */

// Note This class is mutable!

public class BaseNumber
{
	private UBA<FiniteInteger> data;
	
	final int base;
	boolean positive = true;
	
	public BaseNumber(int base_in)
	{
		base = base_in;
		data = new UBA<FiniteInteger>(2);
		
	}
	
	public void neg()
	{
		positive = !positive;
	}
	
	// Adds the given input to the BaseNumber.
	// Works for any integer sized input, as long as input + base < Integer.max_integer.
	public void add(int input)
	{
		
		// Handle troublesome overflow. This class can handle arbitrarily big numbers,
		// as long as they are constructed from adding ints that are feasibly small.
		if(FiniteInteger.MAX_INT_VALUE - base < input)
		{
			throw new Error("ERROR: BaseNumber add: int is too large!");
		}
		
		if(input == 0)
		{
			return;
		}
		
		int digitNum = 0;
		int carry = input;
		
		while(carry != 0)
		{
			int  digit = getDigit(digitNum).toInt() + carry;
			
			// Fit the maximum amount of data into each digit.
			int newDigit = digit % base;
			
			// carry the remaining data to the next digit.
			carry = digit / base;
			
			// Set the current digit to the fitted data.
			setDigit(digitNum, i(newDigit));
		
			// Move on to the next digit.
			digitNum++;
		}
		
		
	} // End of Add.
	
	public void mult(int input)
	{
		
		// Handle troublesome overflow. This class can handle arbitrarily big numbers,
		// as long as they are constructed from adding ints that are feasibly small.
		if(FiniteInteger.MAX_INT_VALUE/base < input)
		{
			throw new Error("ERROR: BaseNumber add: int is too large!");
		}
		
		// Return the base number 0.
		if(input == 0)
		{
			BaseNumber temp = new BaseNumber(base);
			data = temp.data;
			return;
		}
		
		int digitNum = 0;
		int carry = 0;
		int len = data.size();
		
		// Continue until we no longer have digits left to multiply
		// and we no longer have a carry that we need to distribute. 
		while(digitNum < len || carry != 0)
		{
			int  digit = getDigit(digitNum).toInt()*input + carry;
		
			// Fit the maximum amount of data into each digit.
			int newDigit = digit % base;
			
			// carry the larger data to the next digit.
			carry = digit / base;
			
			// Set the current digit to the fitted data.
			setDigit(digitNum, i(newDigit));
		
			// Move on to the next digit.
			digitNum++;
		}

		
	}
	
	// Helper function.
	private FiniteInteger i(int input)
	{
		return new FiniteInteger(input);
	}
	
	
	// Returns the current digit at position digitNum. in the number 1234,
	// the 4 is the 0th digit, the 3 is the 1st, etc.
	FiniteInteger getDigit(int digitNum)
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
		return i(0);
	}
	
	// Returns the current digit at position digitNum.
	// For example : in the number 1234,
	// the 4 is the 0th digit, the 3 is the first, etc.
	// REQUIRES The given digit should be lower than this numbe's base.
	private void setDigit(int digitNum, FiniteInteger digit)
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
			//compress();
			return;
		}
		
		// There is no point setting a leading zero to be zero.
		if(digit.eq(0))
		{
			return;
		}
		
		// Tack on zeroes until we get to the digit that we want to set,
		// because we need to fill in place holder zeroes to preserve
		// the desired magnitude of the number to be set.
		while(len < digitNum)
		{
			data.add(i(0));
			len++;
		}
		
		data.add(digit);
		
	}
	
	// The to string function currently supports up to hexadecimal.
	
	// FIXME : Perhaps use a StringBuilder.
	
	@Override
	public String toString()
	{
		// Remember that the first integer bit represents the sign of the number.
		int len = data.size();
		
		if(len == 0)
		{
			return "0";
		}
		
		char[] output;
		
		if(!positive)
		{
			output = new char[len + 1];
			output[0] = '-';
		}
		else
		{
			output = new char[len];
		}
		
		int output_len = output.length;
		
		// Cycle through all digits from digit 1 to the last digit 'lenl'.
		for(int i = 0; i < len; i++)
		{
			int indice = output_len - 1 - i;
			
			// Extract the digits from the UBA.
			int digit = getDigit(i).toInt();
			
			if(digit > 9)
			{
				// Convert the number to a proper letter
				// according to its ascii integral representation.
				output[indice] = (char)(digit - 10 + 65);
				
				// ascii 65 -> 'A'
				continue;
			}			
			
			// Handle decimal or lesser digits.
			// ascii 48 -> '0'.
			output[indice] = (char)(digit +  48);
			
		}
		
		return new String(output);
		
	}
	
	public IntB toInteger()
	{
		return new IntB(toString());
	}
	
}
  