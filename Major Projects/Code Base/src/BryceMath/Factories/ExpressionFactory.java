package BryceMath.Factories;

import static BryceMath.Factories.NumberFactory.E;
import static BryceMath.Factories.NumberFactory.R;
import static util.testing.ASSERT;
import util.StringParser;
import BryceMath.Numbers.Equation;
import BryceMath.Numbers.Expression;
import BryceMath.Numbers.Multinomial;
import BryceMath.Numbers.Rational;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;

/*
 * The Expression / Equation creation class.
 * 
 * Written by Bryce Summers on 7 - 19 - 2013.
 * 
 * Purpose : This class allows for me to make the process of constructing expressions easier and more intuitive.
 * 
 * Updated : 2 - 5 - 2014 : I have added a Equation parsing function.
 * 
 * Updated 7 - 28 - 2013. Arbitrary expression parsing was added to enable the user to create RationalExpressions from Strings.
 * 
 * Updated : 5 - 18 - 2014 : I have factored out the Multinomial creation functions into the MultinomialFactory class.
 */

public class ExpressionFactory 
{
	// Parse Multinomials.
	public static Multinomial createMultinomial(String mult_string)
	{
		Expression e = createExpression(mult_string);
		
		Multinomial num   = e.getNum();
		Multinomial denom = e.getDenom();
		
		if(denom.isComplex())
		{
			num = num.div(denom);
		}
		
		return num;
	}
	

	// Parse Equations on top of expressions.
	/*
	 * REQUIRES : A string that contains standard mathematical expression
	 * 			  text possible separated with at most 1 equals sign.
	 * FIXME : Perhaps handle more equal signs.
	 */
	public static Equation createEquation(String eq_string)
	{
		// Handle the trivial case.
		if(eq_string.equals(""))
		{
			return Equation.ZERO;
		}	
		
		String[] parts = eq_string.split("=");
		
		String left  = parts[0];
		String right = parts.length == 2 ? parts[1] : null;
		
		// Output Components.
		Expression out_left = null, out_right = null;
		
		if(!left.equals(""))
		{
			out_left = createExpression(left);
		}
		
		if(right != null && !right.equals(""))
		{
			out_right = createExpression(right);
		}
		
		if(out_left != null)
		{
			return new Equation(out_left, out_right);
		}
		
		if(out_right != null)
		{
			return new Equation(out_right);
		}
		
		return Equation.ZERO;
	}
	
	// Returns an Expression from the given string that must have been serialized in an Multinomial class.
	public static Expression createExpression(String exp_string)
	{		

		// Handle the trivial case.
		if(exp_string.equals(""))
		{
			return Expression.ZERO;
		}	
		
		// First convert the string into a list of manageable parts.
		
		// e.g. "5 + (8xy +5)/[54-x] + {5+x^{5}}^{ei}" ->
		// 		["5", "+", "(", "8xy+5", ")", "/", "[", "54-x", "]", "+", "{", "5+x^{5}", "}", "^", "{", "ei", "}]"
		
		// All quotations and braces are guaranteed to be matched.
		
		List<String> comps = StringParser.parseExpression(exp_string);
				
		if(comps.isEmpty())
		{
			return Expression.ZERO;
		}
		
		// -- Now we reduce the expression according to the order of operations.
		
		// -- 1. Reduce the component list to a list of Objects, where the objects are Expressions and connectives.
	
		List<Object> output = parseSubExpressions(comps);
		
		if(output.isEmpty())
		{
			return Expression.ZERO;
		}
		
		// -- 2. Sanitize the list ( remove duplicate connectives.).

		sanitizeConnectives(output);

		// Constants and variables get converted to expressions.
		
		// -- 3. Reduce Exponents from backwards to forwards.
		
		reduceExpontents(output);

		// -- 4. Reduce multiplication and division from front to back.
		
		reduceMultAndDivision(output);

		// -- 5. Reduce Addition and subtraction.
	
		reduceAddAndSubtraction(output);

		ASSERT(output.size() == 1);
		
		return (Expression)output.getFirst();
	}
	
	// Takes in a list of strings that have been parsed through the String parser's expression parsing capabilities.
	/*
	 * Returns a list of Character connectives and Expressions.
	 * 
	 * All variable names and single character variables evaluate to expressions.
	 * All Constant numbers evaluate to expressions.
	 * All Data inside of parentheses get reduced to expressions.
	 * All connectives get reduced to characters.
	 * 
	 * ["5.56", "+", "A", "^", "(", "6", "x", "^", "5", ")", "+", "\'", "Bryce", "'"].
	 * -->
	 * E(R(5.56)), '+', E("A"), '^', E(6x^5), '+', E(1, "Bryce");
	 *
	 */
	private static List<Object> parseSubExpressions(List<String> input)
	{		
		List<Object> output = new List<Object>();
		
		IterB<String> iter = input.getIter();
		
		while(iter.hasNext())
		{
			String str = iter.next();
			
			// Convert constant numbers to expressions.
			if(StringParser.isConstantNumber(str))
			{
				output.add(E(R(str)));
				continue;
			}
			
			// -- Parse connectives and single variables.

			// All component strings should have length 1,
			// unless enclosed in Parentheses or quotes that are of length 1.;
			ASSERT(str.length() == 1);
			
			char c = str.charAt(0);
			
			switch(c)
			{
				// -- Handle connectives
				case '+':
				case '-':
				case '^':
				case '*':
				case '/':
					output.add(new Character(c));
					continue;
					
				// -- Handle enclosing Parentheses.
				case '(' :
				case '[' :
				case '{' :
					ASSERT(iter.hasNext());
					
					// Recursion for parsing parentheses enclosed expressions.
					output.add(createExpression(iter.next()));
					
					// Skip the closing parentheses.
					ASSERT(iter.hasNext());
					ASSERT(iter.next().charAt(0) == StringParser.getRightParen(c));
					continue;
					
				// Handle enclosed string literals.
				case '\'':
				case '"' :
					ASSERT(iter.hasNext());
					
					String next = iter.next();
					
					// Ignore empty strings.
					if(next.equals(c + ""))// '"' or '\'' respectivly.
					{
						continue;
					}
					
					// Create an expression from the variable name.
					output.add(E(next));
					
					// Skip the closing parentheses.
					ASSERT(iter.hasNext());
					ASSERT(iter.next().charAt(0) == StringParser.getRightParen(c));
					continue;
					
				default :
					// -- if it is nothing else, then the string must be a single character variable name.
					output.add(E(Rational.ONE, "" + c));
					continue;
			}
		}
		
		return output;
	}
	
	// The goal of this method is to reduce the given list of input object to the form :
	// Exp, Char, Expr, Char, ...., Char, Exp.
	// There should be no duplicate characters and Expressions should be on the ends.
	private static void sanitizeConnectives(List<Object> input)
	{
	
		// Sanitize the head of the input.
		Object first = input.getFirst();
		if(first instanceof Character)
		{
			input.prepend(Expression.ZERO);
		}
		
		// Sanatize the tail of the input.
		Object last = input.getLast();
		if(last instanceof Character)
		{
			input.add(Expression.ONE);
		}
		
		IterB<Object> iter = input.getIter();
		boolean charLast = false;
		
		while(iter.hasNext())
		{
			Object o = iter.next();
			
			if(o instanceof Character)
			{
				if(charLast)
				{
					iter.remove();
				}
				else
				{
					charLast = true;
				}
				
				continue;
			}
			
			charLast = false;

		}
	}
	
	// Reduces the given collection from the point of iteration backwards.
	private static void reduceExpontents(List<Object> input)
	{
		IterB<Object> iter = input.getTailIter();
		while(iter.hasPrevious())
		{
			Object o = iter.previous();
			
			// Reduce [E1, '^' E2] to E1^E2.  
			if(o instanceof Character && (Character)o == '^')
			{
				// Remove the '^' connective.
				iter.remove();
				
				Expression power = (Expression) iter.next();
				iter.remove();
				
				Expression base = (Expression) iter.previous();
				iter.replace(base.pow(power));
				
				continue;
			}
		}
	}
	
	private static void reduceMultAndDivision(List<Object> input)
	{

		IterB<Object> iter = input.getIter();
		
		// Keep track of a product of the terms for each multiplication and division separated connective.
		Expression product = Expression.ONE;
		
		while(iter.hasNext())
		{
			Object o = iter.next();

			// A non character implies an expression.
			if(!(o instanceof Character))
			{
				iter.insertBefore("*");
				iter.remove();

				product = mult_helper(iter, product);
				continue;
			}
			
			char c = (Character)o;
			
			if(c == '*')
			{
				iter.remove();
				product = mult_helper(iter, product);
				continue;
			}

			if(c == '/')
			{
				iter.remove();
				product = divide_helper(iter, product);
				continue;
			}


			// -- Handle addition and subtraction characters.
					
			// Insert the last term before this connective.
			iter.insertBefore(product);

			// Bring the iterator back to the connective.
			iter.next();

			// Revert the product to the multiplicative identity.
			product = Expression.ONE;
			continue;
		}
	}
	
	// Reduces E(A)*E(B) or E(A)E(B) to E(AB). Removes both of the elements from the collection that produced the iterator. 
	// Inserts the result if the collection has ended.
	// REQUIRES : The iterator should return B when next is called.
	private static Expression mult_helper(IterB<Object> iter, Expression product)
	{
		product = product.mult((Expression)iter.next());
		
		if(!iter.hasNext())
		{
			iter.replace(product);
		}
		else
		{
			iter.remove();
		}
		
		return product;
	}
	
	private static Expression divide_helper(IterB<Object> iter, Expression product)
	{
		product = product.div((Expression)iter.next());
		
		if(!iter.hasNext())
		{
			iter.replace(product);
		}
		else
		{
			iter.remove();
		}
		
		return product;
	}
	
	private static void reduceAddAndSubtraction(List<Object> input)
	{
		IterB<Object> iter = input.getIter();
		
		while(iter.hasNext())
		{
			Object o = iter.next();
			
			// Reduce [E1, '+' E2] to E1+E2.
			if(o instanceof Character && (Character)o == '+')
			{
				// Delete the '+' character.
				iter.remove();
				
				Expression term2 = (Expression) iter.next();
				iter.remove();
				
				Expression term1 = (Expression) iter.previous();
				iter.replace(term1.add(term2));
				continue;
			}
			
			// Reduce [E1, '-' E2] to E1-E2.
			if(o instanceof Character && (Character)o == '-')
			{
				// Delete the '-' character.
				iter.remove();
				
				Expression subtrahend = (Expression) iter.next();
				iter.remove();
				
				Expression minuend = (Expression) iter.previous();
				iter.replace(minuend.sub(subtrahend));
				continue;
			}
		}
	}
}