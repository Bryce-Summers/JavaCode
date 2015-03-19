package BryceMath.Structures;

// This important, because the compiler might confuse this with the built it Integer class.
import BryceMath.Numbers.IntB;
import BryceMath.Numbers.Rational;
import Data_Structures.Structures.List;

/*
 * The Continued Fraction Class,
 * Written by Bryce Summers 1 - 8 - 2013.
 * Purpose: Provides a representation of continued fractions.
 */

public class Continued_Fraction
{
	
	// -- Local Variables.
	List<IntB> data = new List<IntB>();
	
	// Form a Continued Fraction from a Rational Number.
	public Continued_Fraction(Rational input)
	{
		Rational zero = input.zero();

		// Must Terminate, because we are dealing with rational numbers.
		// http://en.wikipedia.org/wiki/Continued_fraction
		while(true)
		{
			data.add(input.part_int());
			
			Rational temp = input.part_frac();
			
			// No more fractional part.
			if(temp.eq(zero))
			{
				break;
			}
			
			input = temp.mult_inverse();
		}
	}
	
	public List<IntB> getData()
	{
		return data.clone();
	}
	
}
