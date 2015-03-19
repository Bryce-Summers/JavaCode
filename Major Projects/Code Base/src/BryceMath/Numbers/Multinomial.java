package BryceMath.Numbers;

import java.util.Iterator;

import Data_Structures.ADTs.Pairable;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.AArray;

/* The Multinomial class.
 * 6 - 27 - 2013
 * 
 * Written by Bryce Summers on 6 - 27 - 2013.
 * 
 *  Capabilities : Represents linear expressions of an arbitrarily number of variables.
 *  
 *  	The expression is represented by an associative array that maps Variables to their scalars.
 *  
 *  	The constant term is represented by the empty_string variable "".
 *  
 *  This class specifically represents Multinomial expressions.
 *  
 *  7 - 24 - 2013. This class has been updated to include local evaluations for imaginary numbers.
 *   - The variable "i" is hereby reserved for referring to imaginary numbers.
 *  
 *  - Updated 9 - 4 - 2013 : Fixed a bug with the serialization of larger variables such as '\l'
 *  							- Created an explicit toSerialString() function.
 */

// FIXME : This class should be for linear expressions.

public class Multinomial extends ModularNumber<Multinomial> implements Comparable<Multinomial>
{	
	// -- Private data.
	
	// FIXME : I should probably use some sort of ordered data, so that the terms and coefficients do not need to be sorted for every printing.
	// FIXME : Instead, I should probably cache the values of the string representation, leading term, and leading scalar, because everything is immutable.
	// FIXME : I should probably outsource the terms to be their own Monomial class.
	
	// Associate various terms with Complex coefficients.
	private AArray<Term, Complex> data;
	
	// -- Identities.
	public static Multinomial ZERO = new Multinomial(0);
	public static Multinomial ONE  = new Multinomial(1);
	
	// The trivial term, used for storing constants that are not associated with any variables.
	private static final Term CONSTANT_TERM = new Term();
	
	
	/* 
	 * Terms are basically multisets that define multinomial terms.
	 * Terms act as monomials.
	 */
	private static class Term extends AArray<String, IntB> implements Comparable<Term>
	{
		// The trivial empty term.
		private Term(){}
		private Term(String var)
		{
			super();
			insert(var, IntB.ONE);
			
			// FIXME : Forbid bad variable names such as "" or "    ".
		}
		
		private Term(String var, IntB power)
		{
			super();
			insert(var, power);
		}
		
		public Term mult(Term other)
		{
			Term output = clone();
			
			List<String> vars = other.getKeys();
			
			for(String s : vars)
			{
				// Refrain from increasing the power of the trivial variable.
				if(s.equals(""))
				{
					continue;
				}
				
				IntB power_other = other.lookup(s);
				
				output.increase_var(s, power_other);
			}
			
			return output;
		}

		// Should not be called outside of the term class.
		// This is a helper funtion for multiplying Terms.
		private void increase_var(String var, IntB amount)
		{
			IntB current_power = lookup(var);
			
			if(current_power == null)
			{
				insert(var, amount);
				return;
			}

			insert(var, current_power.add(amount));
		}
		
		@Override
		public int compareTo(Term other)
		{
			// Put higher power terms in front.
			IntB m1 = getMagnitude();
			IntB m2 = other.getMagnitude();
			
			if(!m1.eq(m2))
			{
				return m2.sub(m1).sign();
			}
			
			// Put alphabetically superior terms first.
			return toVariableNameString(false, false).compareTo(other.toVariableNameString(false, false));
		}
		
		public Term clone()
		{
			Term output = new Term();
			
			List<String> keys = getKeys();
			
			for(String s : keys)
			{
				output.insert(s, lookup(s));
			}
			
			return output;
		}
		
		private IntB getMagnitude()
		{
			IntB output = IntB.ZERO;
			
			List<IntB> powers = getValues();
			
			for(IntB i : powers)
			{
				output = output.add(i);
			}
			
			return output;
		}
		
		// Returns if and only if this term is divisible by the given term.
		// E.G. x^2*y is divisible by x, x^2, y, xy, and itself, but not z, xz, x^3, ect.
		public boolean isDivisible(Term divisor)
		{
			// Extract all of the variables in the divisor.
			List<String> divisorVars = divisor.getKeys();
			
			for(String s : divisorVars)
			{
				IntB divisor_power = divisor.lookup(s);
				IntB numerator_power = lookup(s);

				// Convert null pointers to zero values.
				if(divisor_power == null)
				{
					divisor_power = IntB.ZERO;
				}
				
				if(numerator_power == null)
				{
					numerator_power = IntB.ZERO;
				}
				
				// Return false if the numerator has a smaller magnitude of the given term than the divisor.
				if(s == null || numerator_power.compareTo(divisor_power) < 0)
				{
					return false;
				}
			}
			
			return true;
		}
		
		// Returns the result of division between this term and another term.
		// If they are not divisible, then this will return a term with some negative powers.
		// If this is not desired, then the isDivisible function should be called.
		public Term div(Term divisor)
		{
			Term output = clone();
			
			List<String> divisor_vars = divisor.getKeys();
			
			for(String s : divisor_vars)
			{
				// Compute the resultant power in the returned term.
				IntB resultant_power = lookup(s);
				if(resultant_power == null)
				{
					resultant_power = IntB.ZERO;
				}
				
				resultant_power = resultant_power.sub(divisor.lookup(s));
				
				// Remove trivial resultant terms.
				if(resultant_power.eq(0))
				{
					output.remove_key(s);
					continue;
				}				
				
				// Update the new power within the output term. 
				output.update(s, resultant_power);
			}
			
			// NOTE : All terms that are inside of the numerator,
			// but not the denominator remain unchanged.
			return output;
			
		}
		
		public String toString(boolean serial)
		{
			return toVariableNameString(true, serial);
		}
		
		public String toString()
		{
			return toVariableNameString(true, false);
		}
		
		// Returns a string representation of this term with, or without its powers.
		private String toVariableNameString(boolean showPowers, boolean enclose_variables)
		{
			List<String> keys = getKeys();
			
			if(keys.isEmpty())
			{
				return "";
			}
			
			keys.sort();
			
			StringBuilder output = new StringBuilder();
			
			for(String s : keys)
			{
				IntB power = lookup(s);
				
				// Enclose \l and the like as '\l' for proper serialization.
				if(enclose_variables && s.length() > 1)
				{
					s = "'" + s + "'";
				}
				
				if(power.eq(1))
				{
					output.append(s);

					continue;
				}

				if(showPowers)
				{
					output.append(s + "^{" + power + "}");
				}
				else
				{
					output.append(s);
				}
			}

			return output.toString();			
		}
	}

	// -- Constructors.


	// Create a constant expression.
	public Multinomial(long scalar)
	{
		data = new AArray<Term, Complex>(1);

		if(scalar != 0)
		data.insert(new Term(), new Complex(scalar));

	}

	// Create a constant expression.
	public Multinomial(IntB scalar)
	{
		data = new AArray<Term, Complex>(1);
		
		if(!scalar.eq(0))
		data.insert(new Term(), new Complex(scalar));
		
	}

	// Create a constant expression.
	public Multinomial(Rational scalar)
	{
		data = new AArray<Term, Complex>(1);
		
		if(!scalar.eq(0))
		data.insert(new Term(), new Complex(scalar));
		
	}
	
	// Create a constant expression.
	public Multinomial(Complex scalar)
	{
		data = new AArray<Term, Complex>(1);
		
		if(!scalar.eq(0))
		data.insert(CONSTANT_TERM, scalar);
		
	}
	
	// Create a constant expression from an arbitrarily sized string.
	// FIXME : Perhaps instead, we should make this create variables.
	public Multinomial(String scalar_str)
	{
		data = new AArray<Term, Complex>(1);
	
		Complex scalar = new Complex(new Rational(scalar_str));
		
		if(!scalar.eq(0))
		data.insert(CONSTANT_TERM, scalar);

	}
	
	// Create a constant expression.
	public Multinomial(long scalar, String var)
	{
		data = new AArray<Term, Complex>(1);
		
		// Do nothing if the scalar is 0.
		if(scalar == 0)
		{
			return;
		}
		
		// Set the imaginary part of the constant term if the variable is the imaginary constant.
		if(var.equals("i"))
		{
			data.insert(CONSTANT_TERM, new Complex(0, scalar));
			return;
		}

		// Handle regular case.
		data.insert(new Term(var), new Complex(scalar));
		return;
	}
	
	// Create a single variable expression.
	public Multinomial(Rational scalar, String var)
	{
		data = new AArray<Term, Complex>(1);
		
		// Do nothing if the scalar is 0.
		if(scalar.eq(0))
		{
			return;
		}
		
		// Set the imaginary part of the constant term if the variable is the imaginary constant.
		if(var.equals("i"))
		{
			data.insert(CONSTANT_TERM, new Complex(Rational.ZERO, scalar));
			return;
		}

		// Handle regular case.
		data.insert(new Term(var), new Complex(scalar));
		return;
	}
	
	// Create a single variable expression.
	public Multinomial(Complex scalar, String var)
	{
		data = new AArray<Term, Complex>();

		// Do nothing if the scalar is equal to 0.
		if(scalar.eq(0))
		{
			return;
		}
		
		// Handle the imaginary constant case.
		if(var.equals("i"))
		{
			scalar = scalar.mult(Complex.I);
			data.insert(CONSTANT_TERM, scalar);
			return;
		}

		// Handle the normal case.
		data.insert(new Term(var), scalar);
	}

	// Create a single variable expression.
	public Multinomial(Complex scalar, String var, IntB power)
	{
		data = new AArray<Term, Complex>();

		// Do nothing if the scalar is equal to 0.
		if(scalar.eq(0))
		{
			return;
		}
		
		// Handle the imaginary constant case.
		if(var.equals("i"))
		{
			scalar = scalar.mult(Complex.power_of_i(power));
			data.insert(CONSTANT_TERM, scalar);
			return;
		}

		// Handle the normal case.
		data.insert(new Term(var, power), scalar);
	}
	
	// Create a single variable expression.
	public Multinomial(Complex scalar, String var, int power)
	{
		data = new AArray<Term, Complex>();

		// Do nothing if the scalar is equal to 0.
		if(scalar.eq(0))
		{
			return;
		}
		
		// Handle the imaginary constant case.
		if(var.equals("i"))
		{
			scalar = scalar.mult(Complex.power_of_i(power));
			data.insert(CONSTANT_TERM, scalar);
			return;
		}

		// Handle the normal case.
		data.insert(new Term(var, new IntB(power)), scalar);
	}
	
	// Create a single variable expression.
	public Multinomial(String var, int power)
	{
		data = new AArray<Term, Complex>();
		
		// Handle the imaginary constant case.
		if(var.equals("i"))
		{
			Complex scalar = (Complex.power_of_i(power));
			data.insert(CONSTANT_TERM, scalar);
			return;
		}

		// Handle the normal case.
		data.insert(new Term(var, new IntB(power)), Complex.ONE);
	}

	
	// Create a single variable expression.
	public Multinomial(String var, IntB power)
	{
		data = new AArray<Term, Complex>();
	
		// Handle the imaginary constant case.
		if(var.equals("i"))
		{
			Complex scalar = (Complex.power_of_i(power));
			data.insert(CONSTANT_TERM, scalar);
			return;
		}

		// Handle the normal case.
		data.insert(new Term(var, power), Complex.ONE);
	}

	// Create a single term.
	private Multinomial(Complex scalar, Term term)
	{
		data = new AArray<Term, Complex>();
		
		if(!scalar.eq(0))
		data.insert(term, scalar);

	}
	
	private Multinomial(AArray<Term, Complex> data_new)
	{
		data = data_new;
	}

	// Number methods.
	
	@Override
	Multinomial N(long n)
	{		
		return new Multinomial(n);
	}

	@Override
	public Multinomial zero()
	{
		return ZERO;
	}

	@Override
	public Multinomial one()
	{
		return ONE;
	}

	@Override
	public Multinomial add(Multinomial input)
	{
		if(input.equals(ZERO))
		{
			return this;
		}
		
		// Create a clone of this expression's data.
		AArray<Term, Complex> output = data.clone();
		
		AArray<Term, Complex> data2 = input.data;
		
		// Extract the keys from the input Multinomial.
		List<Term> keys = data2.getKeys();
		
		for(Term t : keys)
		{
			Complex add1 = output.lookup(t);
			Complex add2 = data2.lookup(t);
			
			if(add1 == null)
			{
				output.insert(t, add2);
				continue;
			}
			
			Complex result = add1.add(add2);
			
			// Filter out trivial results.
			if(result.eq(0))
			{
				output.remove_key(t);
				continue;
			}
			
			output.insert(t, result);
		}
		
		return new Multinomial(output);
	}

	@Override
	public Multinomial sub(Multinomial input)
	{
		if(input.equals(ZERO))
		{
			return this;
		}
		
		if(input.equals(this))
		{
			return ZERO;
		}
		
		// Create a clone of this expression's data.
		AArray<Term, Complex> output = data.clone();
		
		AArray<Term, Complex> data2 = input.data;
		
		// Extract the keys from the input Multinomial.
		List<Term> keys = data2.getKeys();
		
		for(Term t : keys)
		{
			Complex minuend = output.lookup(t);
			
			Complex subtrahend = data2.lookup(t);
			
			if(minuend == null)
			{
				output.insert(t, subtrahend.neg());
				continue;
			}
			
			Complex result = minuend.sub(subtrahend);
			
			// Filter out trivial results.
			if(result.eq(0))
			{
				output.remove_key(t);
				continue;
			}
			
			output.insert(t, result);
		}
		
		return new Multinomial(output);
	}
	
	public Multinomial mult(Multinomial other)
	{
		if(other.eq(0))
		{
			return ZERO;
		}
		
		// Create a list to populate with a sum of terms.
		List<Multinomial> sums = new List<Multinomial>();
		
		// Multiply slow and steady like we did back in grade school.
		List<Term> keys = data.getKeys();
		
		for(Term k : keys)
		{
			Multinomial addend = other.mult(data.lookup(k), k);
			sums.add(addend);
		}
		
		return sum(sums);
		
	}
	
	private Multinomial mult(Complex scalar, Term input)
	{
		if(scalar.eq(0))
		{
			return ZERO;
		}
		
		// Multiply each terms constant scalar by the given input value.
		AArray<Term, Complex> output = new AArray<Term, Complex>(Math.max(1, data.size()));
		
		List<Term> keys = data.getKeys();
		
		for(Term t : keys)
		{
			Complex multiple1	= data.lookup(t);
			Complex result		= multiple1.mult(scalar);
			
			// Populate the new data with terms whose variables,
			// and scalars have been multiplied.
			output.insert(t.mult(input), result);
		}
		
		return new Multinomial(output);
		
	}
	
	// Returns a pair with a quotient and remainder.
	public Pairable<Multinomial> division(Multinomial input)
	{
		List<Multinomial> output = new List<Multinomial>();
		
		// Handle division by a Complex constant.
		Complex scalar = input.toComplex();
		
		if(scalar != null)
		{
			output.add(div(scalar));
			output.add(ZERO);
			
			return output;
		}

		/*
		// Handle division by a Term.
		AArray <Term, Complex> divisor_Data = input.data;
		if(divisor_Data.size() == 1)
		{
			// Compute the requisite division parameters.
			List<Term> keys = divisor_Data.getKeys();
			Term term_in = keys.getFirst();
			Complex scalar_in = divisor_Data.lookup(term_in);
			
			// Perform the division, package it up,
			// and ship it to the outside world.
			output.add(div(term_in, scalar_in));
			output.add(ZERO);
			return output;
		}*/
		
		
		// --  Perform Long division.
		
		
		// First compute the leading Term from the input.
		Term leading = input.getLeadingTerm();
				
		// -- Initiate the iterative process of long division.
		
		// Mutable quotients and remainders.
		Multinomial quotient  = ZERO;
		Multinomial remainder = this;
		
		// The original numerator and divisor.
		Multinomial divisor    = input;
		
		
		boolean found = true;
		
		while(found)
		{
			// The loop escapes, when no further reduction by a divisible term can take place.
			found = false;
			
			List<Term> keys = remainder.getTerms();
			keys.sort();

			for(Term t : keys)
			{
				if(t.isDivisible(leading))
				{
					found = true;
					
					Term mult_term = t.div(leading);
					Complex mult_scalar = remainder.getCoef(t).div(divisor.getCoef(leading));

					Multinomial Quotient_term = new Multinomial(mult_scalar, mult_term);

					// Add the Quotient_term to the quotient.
					quotient = quotient.add(Quotient_term);
					
					// Compute the new term to subtract from the current numerator term.
					Multinomial subtrahend = divisor.mult(Quotient_term);
					
					remainder = remainder.sub(subtrahend);
					
					break;
				}
			}
		}
		
		output.add(quotient);
		output.add(remainder);
		
		return output;
	}

	private Multinomial div(Complex input)
	{
		if(input.eq(0))
		{
			throw new Error("Division by 0 is not well defined!");
		}
		
		if(input.eq(1))
		{
			return this;
		}
		
		AArray<Term, Complex> output = new AArray<Term, Complex>(data.getTableSize());
		
		List<Term> keys = data.getKeys();
		
		for(Term t : keys)
		{
			Complex dividend	= data.lookup(t);
			Complex result		= dividend.div(input);
			output.insert(t, result);
		}
		
		return new Multinomial(output);
	}

	/*
	// Divides the given Multinomial by the given term and coefficient.
	private Multinomial div(Term divisor, Complex divisor_scalar)
	{
		AArray<Term, Complex> output = new AArray<Term, Complex>();
		
		List<Term> keys = data.getKeys();
		
		// Perform the distributed division amongst the terms and scalars,
		// and store the results in the output data.
		for(Term t : keys)
		{
			Complex scalar = data.lookup(t).div(divisor_scalar);
			Term t_new = t.div(divisor);
			output.insert(t_new, scalar);
		}
		
		return new Multinomial(output);
	}*/

	@Override
	public Multinomial sqrt()
	{
		throw new Error("Not Yet implemented");
	}

	@Override
	public boolean eq(Multinomial other)
	{
		return data.equals(other.data);
	}
	
	public boolean isComplex()
	{
		return toComplex() != null;
	}
	
	public Complex toComplex()
	{
		int len = data.size();
		
		if(len > 1)
		{
			return null;
		}
		
		if(len == 0)
		{
			return Complex.ZERO;
		}

		Term key = data.getKeys().getFirst();
		
		if(key.equals(CONSTANT_TERM))
		{
			return data.lookup(key);
		}
		
		return null;
	}

	// Why reinvent the wheel, when we have no need to?
	@Override
	public int hashCode()
	{
		if(isInt())
		{
			return toIntB().hashCode();
		}
		
		// FIXME : This hash function is actually horrible.
		return data.hashCode();
	}

	public Multinomial neg()
	{
		// Shallow copy the data structure. (Copy the nodes, but do use the original references.
		// This is permitted, because the Numbers are guaranteed to be immutable.
		AArray<Term, Complex> data_new = data.clone();
		List<Term> keys = data_new.getKeys();
		
		// Negate all of the values.
		for(Term t : keys)
		{
			data_new.update(t, data_new.lookup(t).neg());
		}
		
		// Return a new expression.
		return new Multinomial(data_new);
	}
	
	private Multinomial sum(List<Multinomial> sums)
	{
		// The additive identity.
		Multinomial output = Multinomial.ZERO;
		for(Multinomial e : sums)
		{
			output = output.add(e);
		}
		
		return output;
	}
	
	private List<Term> getTerms()
	{
		return data.getKeys();
	}
	
	// Returns the coefficient of the given term.
	private Complex getCoef(Term t)
	{
		Complex output = data.lookup(t);
		
		// Handle non existent coefficients.
		if(output == null)
		{
			return Complex.ZERO;
		}
		
		return output;
	}	
	
	// FIXME optimize this by using a more ordered data structure to represent the terms.
	private Term getLeadingTerm()
	{
		List<Term> divisor_keys = data.getKeys();
		divisor_keys.sort();
		
		return divisor_keys.getFirst();
	}
	
	public Complex getLeadingCoefficient()
	{
		return getCoef(getLeadingTerm());
	}
	
	@Override
	public boolean isPositive()
	{
		return getLeadingCoefficient().isPositive();
	}
	
	@Override
	public boolean isNegative()
	{
		return getLeadingCoefficient().isNegative();
	}

	// Implement Multinomial comparison through string comparison.
	@Override
	public int compareTo(Multinomial other)
	{
		return toString().compareTo(other.toString());
	}
	
	public List<Complex> getCoefs()
	{
		return data.getValues();
	}
	
	public Object pow(Multinomial power)
	{
		throw new Error("Please Implement me!");
	}

	@Override
	public boolean isInt()
	{
		Complex c = toComplex();
		
		return c != null && c.isInt(); 
	}

	@Override
	public IntB toIntB()
	{
		Complex c = toComplex();
		
		if(c == null)
		{
			return IntB.ZERO;
		}
		
		return c.toIntB();
	}

	@Override
	public int toInt()
	{
		Complex c = toComplex();
		
		if(c == null)
		{
			return 0;
		}
		
		return c.toInt();
	}

	// Return a string suitable for displaying in BryceTEX,
	// but not suitable for serializing to a file.
	@Override
	public String toString()
	{
		return toString(false);
	}
	
	public String toSerialString()
	{
		return toString(true);
	}
	
	public String toString(boolean serial)
	{
		List<Term> keys = data.getKeys();
		
		if(keys.isEmpty())
		{
			return "0";
		}
		
		keys.sort();

		// Construct an output string efficiently piece by piece.
		StringBuilder output = new StringBuilder();
		
		Iterator<Term> iter = keys.iterator();
		while(iter.hasNext())
		{
			Term t = iter.next();
			
			Complex scalar = data.lookup(t);

			// Handle complicated optimization of sign displaying.
			if(scalar.isNegative())
			{
				if(!output.toString().equals(""))
				{
					output.append(" - ");
				}
				else
				{
					output.append("-");	
				}
			}
			else if(!output.toString().equals(""))
			{
				output.append(" + ");
			}			
			
			// Parse the string, removing the negative sign if necessary to enable the prettier printing of " - ";
			String scalar_str;
			
			if(t.equals(CONSTANT_TERM))
			{
				scalar_str = scalar.toString();
			}
			else
			{
				scalar_str = scalar.toCoef();
			}
			
			// Remove any leading negative sign.
			if(scalar_str.length() > 0 && scalar_str.charAt(0) == '-')
			{
				scalar_str = scalar_str.substring(1);
			}

			// Attach the terms to the final output string.
			output.append("" + scalar_str + t.toString(serial));
			
		}
		
		return output.toString();		
	}

	@Override
	public Multinomial conj()
	{
		// Shallow copy the data structure.
		AArray<Term, Complex> data_new = data.clone();
		List<Term> keys = data_new.getKeys();
		
		// Negate all of the values.
		for(Term t : keys)
		{
			data_new.update(t, data_new.lookup(t).conj());
		}
		
		// Return a new expression.
		return new Multinomial(data_new);
	}

}