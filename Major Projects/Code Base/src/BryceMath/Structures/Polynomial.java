package BryceMath.Structures;

import util.Genarics;
import BryceMath.Numbers.Rational;
import Data_Structures.Structures.UBA;

/*
 * Polynomial.
 * Written by Bryce Summers, 12 - 31 - 2012.
 * Purpose: To provide a representation of a polynomial in standard Ax^2 + Bx + C form.
 * 			This representation is arbitrarily big and can be differentiated and integrated.
 */

// FIXME : Incorporate this functionality into the Multinomials class.
// 7 - 30 - 2013.

public class Polynomial
{
	// -- Local variables.
	private UBA<Rational> data;
	
	// -- Constructors.
	
	public Polynomial(int ... input)
	{
		int len = input.length;
		
		// Initialize coefficients data.
		data = new UBA<Rational>(len);
		
		// Copy the array of input coefficients into the local data.
		// Reduce this polynomial to its lowest representative degree.
		boolean leading_coef_found = false;
		for(int i: input)
		{
			if(!leading_coef_found && i != 0)
			{
				leading_coef_found = true;
			}
			
			if(leading_coef_found)
			{
				data.add(new Rational(i));
			}
		}
	}
	
	public Polynomial(Rational ... input)
	{
		ivars(input);
	}
	
	// Create a polynomial that passes through the given points.
	public Polynomial(Vector<Rational> ... input)
	{
		int len = input.length;
		
		Genarics<Vector<Rational>> ge_v = new Genarics<Vector<Rational>>();		
		Vector<Rational>[] rows = ge_v.Array(len, input[0]);
		
		
		// First create the row vectors.
		for(int r = 0; r < len; r++)
		{
			Rational [] row_new = new Rational[len];
			
			assert(input[r].length == 2);
			
			Rational x = input[r].get(0);
			
			for(int c = 0; c < len; c++)
			{
				row_new[c] = x.pow(len - 1 - c);
			}
			
			rows[r] = new Vector<Rational>(row_new);
		}
		
		// Create Systems of equations matrix.
		Matrix<Rational> A = new Matrix<Rational>(rows);
		
		Rational[] col = new Rational[len];
		
		for(int i = 0; i < len; i++)
		{
			col[i] = input[i].get(1);
		}
		
		Vector<Rational> b = new Vector<Rational>(col);
		
		Vector<Rational> result = A.solve(b);
				
		// Use the result as the input to the normal polynomial constructor.
		ivars(result.toArray());
	}
	
	private void ivars(Rational ... input)
	{
		int len = input.length;
		
		// Initialize coefficients data.
		data = new UBA<Rational>(len);
		
		// Copy the array of input coefficients into the local data. 
		boolean leading_coef_found = false;
		for(Rational r: input)
		{
			if(r == null)
			{
				r = new Rational(0);
			}
			if(!leading_coef_found && !(r.eq(0)))
			{
				leading_coef_found = true;
			}
			
			if(leading_coef_found)
			{
					data.add(r);
			}
		}
	}
	
	// -- Arithmetic operations.
	
	// Addition
	public Polynomial add(Polynomial other)
	{
		// Compute the size of the output Polynomial. 
		int end_degree = Math.max(get_degree(), other.get_degree());
		
		Rational[] output = new Rational[end_degree + 1];
		
		// Perform the addition.
		for(int i = 0; i <= end_degree; i++)
		{
			output[end_degree - i] = get_coef(i).add(other.get_coef(i));
		}
		
		return new Polynomial(output);		
	}
	
	// Subtraction.
	public Polynomial sub(Polynomial other)
	{
		// Compute the size of the output Polynomial. 
		int end_degree = Math.max(get_degree(), other.get_degree());
		
		Rational[] output = new Rational[end_degree + 1];
		
		// Perform the addition.
		for(int i = 0; i <= end_degree; i++)
		{
			output[end_degree - i] = get_coef(i).sub(other.get_coef(i));
		}
		
		return new Polynomial(output);		
	}
	
	public Polynomial mult(Polynomial other)
	{
		// Compute the size of the output Polynomial. 
		int end_degree = get_degree() + other.get_degree();

		// Compute which polynomial we will use as the first operand,
		// because we can conserve additions in this way.
		Polynomial p1, p2;
		int min_passes;
		
		if(get_degree() < other.get_degree())
		{
			min_passes = get_degree() + 1;
			p1 = this;
			p2 = other;
		}
		else
		{
			min_passes = other.get_degree() + 1;
			p1 = other;
			p2 = this;
		}
		
		// Start with an empty polynomial.
		Polynomial output = new Polynomial(0);
		
		// Now perform polynomial multiplication.
		for(int r = 0; r < min_passes; r++)
		{
			Rational coef1 = p1.get_coef(r);
			
			Rational[] temp = new Rational[end_degree + 1];
			
			for(int c = 0; c < other.get_degree() + 1; c++)
			{
				Rational coef2 = p2.get_coef(c);
				
				// Multiply powers of x and set them properly.
				temp[end_degree - (c+r)] = coef1.mult(coef2);
			}
			
			// Add up the component sums. 
			output = output.add(new Polynomial(temp));
		}
		
		return output;		
		
	}
	
	// -- Calculus
	
	// Derivative
	public Polynomial der()
	{
		Polynomial output = clone();
		
		// Mask the local data.
		UBA<Rational>data = output.data;
		
		for(int i = 0; i < get_degree(); i++)
		{
			data.set(i, data.get(i).mult(get_degree() - i));
		}
		
		// Remove the trailing 0 coefficient,
		// and thereby reduce the powers of the polynomial.
		data.rem();
		
		return output;
	}
	
	// FIXME: We can make this more efficient by writing our
	// own function instead of reusing the der() function,
	// because we do not need to clone the data at each pass.
	public Polynomial der(int der_num)
	{
		Polynomial output = this.der();
		for(int i = 1; i < der_num; i++)
		{
			output = output.der();
		}
		
		return output;
	}
	
	// Integral.
	public Polynomial integrate()
	{
		Polynomial output = clone();
		
		// Mask the local data.
		UBA<Rational>data = output.data;
		
		for(int i = 0; i < get_degree(); i++)
		{
			data.set(i, data.get(i).div(get_degree() - i + 1));
		}
		
		// Remove the trailing 0 coefficient,
		// and thereby reduce the powers of the polynomial.
		
		// FIXME: Deal with the trailing c, it is not really 0.
		
		data.add(new Rational(0));
		
		return output;
	}
	
	// -- Data access functions
	
	// Get the coefficient of the term of the given power.
	public Rational get_coef(int power)
	{
		int size = data.size();

		if(power >= size)
		{
			return new Rational(0);
		}
		
		return data.get(size - power - 1);
	}
	
	public int get_degree()
	{
		return data.size() - 1;
	}
	
	public void setCoef(int power, Rational input)
	{
		data.set(data.size() - power - 1, input);
	}
		
	// Returns the evaluation of this polynomial.
	public Rational eval(Rational x)
	{
		Rational output = get_coef(0);
		
		int deg = get_degree();
		
		Rational x_current = x.one();
		
		for(int i = 1; i <= deg; i++)
		{
			// Increase the power of x.
			x_current = x_current.mult(x);
			
			// Add the ith term to the output.
			output = output.add(get_coef(i).mult(x_current));
						
		}
		
		return output;
	}
	
	public Rational eval(int x)
	{
		return eval(new Rational(x));
	}
	
	// -- Helper methods.
	
	public String toString()
	{
		String output = "";
		
		int len = get_degree();
		
		for(int i = len; i >= 0; i--)
		{
			if(i == 0)
			{
				output = output + get_coef(0);
				break;
			}
			
			if(get_coef(i).eq(new Rational(0)))
			{
				continue;
			}
			
			if(!get_coef(i).eq(new Rational(1)))
			{
				output = output + get_coef(i);
			}	
			
			if(i > 0)
			{
				output = output + "X";
			}
			
			if(i > 1)
			{
				 output = output + "^" + i;
			}
			
			if(i > 0)
			{
				output = output + " + ";
			}
		}
		
		return output;
	}
	
	
	public Polynomial clone()
	{
		Rational[] temp = data.toArray();
		return new Polynomial(temp);
	}
	
	// returns an empty zero polynomial.
	public static Polynomial zero()
	{
		return new Polynomial(0);
	}
}
