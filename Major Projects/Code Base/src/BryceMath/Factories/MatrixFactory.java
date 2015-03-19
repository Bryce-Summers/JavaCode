package BryceMath.Factories;

import BryceMath.Numbers.Complex;
import BryceMath.Numbers.Equation;
import BryceMath.Numbers.Expression;
import BryceMath.Numbers.IntB;
import BryceMath.Numbers.Multinomial;
import BryceMath.Numbers.Rational;
import BryceMath.Structures.Matrix;
import BryceMath.Structures.Vector;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;

/*
 * Matrix factory class.
 * 
 * Written by Bryce Summers on 6 - 27 - 2013.
 * 
 * Purpose : Provides functionality for creating many types of Matrices.
 * 			 These functions should me imported into any classes that desire to make the given structures.
 * 
 * 	1. Matrices from row vectors of all number types.
 * 
 *  2. Matrices from column vectors of all number types.
 *  
 *  Modified : 3 - 29 - 2014 : Fixed a problem where I did not replace expressions with equations in the parse add function.
 */


// FIXME : Add helper functions for Equation Matrices.
public class MatrixFactory
{
	

	// -- Matrices from row vectors.
	
	public static Matrix<IntB> m_i_row(Vector<IntB> ... input)
	{
		return new Matrix<IntB>(input);
	}
	
	public static Matrix<Rational> m_r_row(Vector<Rational> ... input)
	{
		return new Matrix<Rational>(input);
	}
		
	public static Matrix<Complex> m_c_row(Vector<Complex> ... input)
	{
		return new Matrix<Complex>(input);
	}
	
	public static Matrix<Multinomial> m_e_row(Vector<Multinomial> ... input)
	{
		return new Matrix<Multinomial>(input);
	}
	
	public static Matrix<Expression> m_re_row(Vector<Expression> ... input)
	{
		return new Matrix<Expression>(input);
	}
	
	
	// -- Matrices from column vectors.
	
	public static Matrix<IntB> m_i_col(Vector<IntB> ... input)
	{
		return new Matrix<IntB>(true, input);
	}
	
	public static Matrix<Rational> m_r_col(Vector<Rational> ... input)
	{
		return new Matrix<Rational>(true, input);
	}
		
	public static Matrix<Complex> m_c_col(Vector<Complex> ... input)
	{
		return new Matrix<Complex>(true, input);
	}
	
	public static Matrix<Multinomial> m_e_col(Vector<Multinomial> ... input)
	{
		return new Matrix<Multinomial>(true, input);
	}
	
	public static Matrix<Expression> m_re_col(Vector<Expression> ... input)
	{
		return new Matrix<Expression>(true, input);
	}
	
	public static Matrix<Equation> m_ex_col(Vector<Equation> ... input)
	{
		return new Matrix<Equation>(true, input);
	}
	
	@SuppressWarnings("unchecked")
	public static Matrix<Expression> Exp_to_Matrix(Expression exp)
	{
		return m_re_col(new Vector<Expression>(exp));
	}
	
	@SuppressWarnings("unchecked")
	public static Matrix<Equation> Equation_to_Matrix(Equation exp)
	{
		return m_ex_col(new Vector<Equation>(exp));
	}
	
	
	// -- Serious Matrix expression evaluating.
	
	// -- This is analogous to my String parsing algorithm for expressions.
	// We need to completely parse the inputed Matrix expression to return a Matrix.
	
	/*
	 * 1. Parens.
	 * 2. Powers
	 * 3. Multiplication / Division
	 * 4. Addition / Subtraction. 
	 */	
	
	/*
	 * Possible objects are:
	 *		- gui_ExpressionInput for scalars.
	 * 		- gui_labels for connectives.
	 * 		- gui_Matrices for Matrixes.
	 */
	
	/* This function is analogous to the createEquation() function in the Equation Factory class.
	 * 
	 * REQUIRES : A list of Character connectives, Matrix<Equation> and Equation scalars.
	 * 				This cannot parse anything else. All constants should be converted into equations.
	 * 
	 * ENSURES : returns the simplified expression, if it is well formed.
	 * 			 returns null if the input expression is not well formed.
	 */

	@SuppressWarnings("unchecked")
	public static Matrix<Equation> evaluateExpression(List<Object> elems) throws Error
	{
		/* First simplify parens. */
		elems = parseParens(elems);
		
		if(elems.isEmpty())
		{
			throw new Error("Trivial Evaluation");
		}
		
		// Attempt to simplify via multiplication.
		parseMult(elems);

		if(elems.isEmpty())
		{
			throw new Error("Trivial Evaluation");
		}
		
		// Attempt to simplify via Addition.
		parseAdd(elems);
		
		if(elems.isEmpty())
		{
			throw new Error("Trivial Evaluation");
		}
		
		// Return the resulting first expression.
		if(elems.size() != 1)
		{
			throw new Error("Expression fails to reduce");
		}
		
		Object result = elems.getFirst();
		
		if(result instanceof Matrix<?>)
		{
			return (Matrix<Equation>) elems.getFirst();
		}
		
		if(result instanceof Equation)
		{
			return Equation_to_Matrix((Equation) elems.getFirst());
		}
		
		throw new Error("Type : " + result.getClass() + " is not supported");
	}
	
	
	// Reduces all Parens inside of an expression.
	private static List<Object> parseParens(List<Object> elems)
	{
		List<Object> result = new List<Object>();
		
		IterB<Object> iter = elems.getIter();
		
		int parens_depth = 0;
		List<Object> sub_list = new List<Object>();
		
		
		// Go through every element.
		/*
		 * Put Parentheses enclosed characters inside of the sub_list for evaluation.
		 */
		while(iter.hasNext())
		{			
			Object o = iter.next();
			
			if(o instanceof Character)
			{
				Character c = (Character)o;
				if(c == '(')
				{
					parens_depth++;
					continue;
				}
				
				if(c == ')')
				{
					if(parens_depth == 0)
					{
						continue;
					}

					
					parens_depth--;
					
					// The end parentheses logic.
					if(parens_depth == 0)
					{
						// Recursion.
						handleEndP(result, sub_list);
					}
					
					continue;
				}
				
			}
			
			if(parens_depth > 0)
			{
				sub_list.add(o);
			}
			else
			{
				result.add(o);
			}
			
		}

		// Handle overly mathched left parens.
		if(parens_depth > 0)
		{
			// Recursion.
			handleEndP(result, sub_list);
		}		
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static void parseMult(List<Object> elems) throws Error
	{
		IterB<Object> iter = elems.getIter();
		
		// Begin on the second element.
		Object last = iter.next();
		
		while(iter.hasNext())
		{
			Object o = iter.next();
			
			if(o instanceof Matrix<?>)
			{				
				Matrix<Equation> m2 = (Matrix<Equation>) o;
				
				if(last instanceof Equation)
				{
					Equation scalar = (Equation) last;
					
					Matrix<Equation> result = m2.mult(scalar);
					
					replaceLast2(iter, result);
					last = result;
					
					continue;
				}

				if(last instanceof Matrix<?>)
				{
					Matrix<Equation> m1 = (Matrix<Equation>)last;
					
					Matrix<Equation> result;
					
					if(m1.getColNum() == m2.getRowNum())
					{
						result = m1.mult(m2);// Normal multiplication.
					}
					// Try to infer the dot product computation for improper dimensional matrices.
					else if (m1.canVectorDotWith(m2))	
					{
						if(m1.getRowNum() == 1)
						{
							result = m1.mult(m2.transpose());// Dot product.	
						}
						else
						{
							result = m1.transpose().mult(m2);// Dot product.
						}
					}
					else
					{
						throw new Error("Error : Improper dimensions for Matrix multiplication.");
					}
					
					replaceLast2(iter, result);
					last = result;
					
					continue;
				}
				
				last = o;
				continue;
			}
			
			if(o instanceof Equation)
			{				
				Equation exp = (Equation) o;
				
				if(last instanceof Equation)
				{
					
					Equation scalar = (Equation) last;
					
					Equation result = exp.mult(scalar);
					
					replaceLast2(iter, result);
					last = result;
					
					continue;
				}

				if(last instanceof Matrix<?>)
				{
					Matrix<Equation> m1 = (Matrix<Equation>)last;
					
					Matrix<Equation> result = m1.mult(exp);
					
					replaceLast2(iter, result);
					last = result;					

					continue;
				}
				
				last = o;
				continue;
			}

			// Pretend that multiplication symbols do not exist.
			if(o instanceof Character)
			{
				char c = (Character)o;
				
				if(c == '*')
				{
					iter.remove();
					continue;
				}

				Object old_last = last;
				last = c;
								
				
				if(c != '/')
				{
					continue;
				}
			
				// Ignore the stray connective if it is at the end.
				if(!iter.hasNext())
				{
					continue;					
				}
				
				Object o2 = iter.next();
				
				if(!(o2 instanceof Equation))
				{
					throw new Error("Can't divide by non scalars.");
				}
				
				Equation scalar = (Equation)o2;
				
				if(old_last instanceof Equation)
				{
					Equation exp = (Equation)old_last;
					last = replaceLast3(iter, exp.div(scalar));
					continue;
				}
				
				if(old_last instanceof Matrix<?>)
				{
					Matrix<Equation> m = (Matrix<Equation>)old_last;
					
					last = replaceLast3(iter, m.mult(scalar.mult_inverse()));
					
					continue;
				}

			}

			throw new Error("Type : " + o.getClass() + " is not supported");

		}

	}

	@SuppressWarnings("unchecked")
	private static void parseAdd(List<Object> elems) throws Error
	{
		
		if(elems.getFirst().equals('-'))
		{
			elems.prepend(Equation.ZERO);
		}
		else if(elems.getFirst().equals('+'))
		{
			elems.prepend(Equation.ZERO);
		}
		
		IterB<Object> iter = elems.getIter();
		
		// Begin on the second element.
		Object last = iter.next();
		
		while(iter.hasNext())
		{
			Object o = iter.next();
			
			// Pretend that multiplication symbols do not exist.
			if(!(o instanceof Character))				
			{
				continue;
			}
			
			char c = (Character)o;
				
			if(c != '+' && c != '-')
			{
				iter.remove();
				continue;
			}
			
			// Ignore the stray conncective if it is at the end.
			if(!iter.hasNext())
			{
				continue;					
			}
			
			Object o2 = iter.next();
			
			if(o2 instanceof Equation)
			{
				if(!(last instanceof Equation))
				{
					throw new Error("Cannot add Scalars and Matrices.");
				}
				
				Equation scalar1 = (Equation)last;
				Equation scalar2 = (Equation)o2;
				
				Equation result;
				
				if(c == '+')
				{
					result = scalar1.add(scalar2);
				}
				else
				{
					result = scalar1.sub(scalar2);
				}
				
				last = replaceLast3(iter, result);
				continue;
			}
			
			if(o2 instanceof Matrix<?>)
			{
				if(!(last instanceof Matrix<?>))
				{
					throw new Error("Cannot add Scalars and Matrices.");
				}
				
				Matrix<Expression> matrix1 = (Matrix<Expression>)last;
				Matrix<Expression> matrix2 = (Matrix<Expression>)o2;

				Matrix<Expression> result;
				
				if(c == '+')
				{
					result = matrix1.add(matrix2);
				}
				else
				{
					result = matrix1.sub(matrix2);
				}
				
				last = replaceLast3(iter, result);
				continue;
			}
			
			
			
		}
		
	}
	
	// THe recursive handling of parenthesized expressions.
	private static void handleEndP(List<Object> results, List<Object> sub_exp)
	{
		if(sub_exp.isEmpty())
		{
			return;
		}
		
		
		Matrix<Equation> sub_result = evaluateExpression(sub_exp);
			
		if(sub_result.isTrivial())
		{
			results.add(sub_result.get(0, 0));
		}
		else
		{
			results.add(sub_result);
		}
				
		sub_exp.clear();
	}
	
	private static void replaceLast2(IterB<Object> iter, Object result)
	{
		// Remove both old elements and replace them with the new product element.
		iter.remove();
		iter.previous();
		iter.replace(result);
	}
	
	private static Object replaceLast3(IterB<Object> iter, Object result)
	{
		// Remove both old elements and replace them with the new product element.
		iter.remove();
		iter.previous();
		iter.remove();
		iter.previous();
		iter.replace(result);
		
		return result;
	}
}
