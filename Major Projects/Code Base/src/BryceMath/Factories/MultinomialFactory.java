package BryceMath.Factories;

import static BryceMath.Factories.NumberFactory.M;
import BryceMath.Numbers.IntB;
import BryceMath.Numbers.Multinomial;
import BryceMath.Numbers.Rational;
import Data_Structures.Structures.List;

/*
 * The Expression / Equation creation class.
 * 
 * Written by Bryce Summers on 5 - 18 - 2014.
 * 
 * Purpose : This class allows for me to make the process of constructing Multinomials Easier.
 * 
 * FIXME : This old code can probably be phased out, because the createMultinomial(string) function in the Expression Factory class is most likely far easier to use.
 */

public class MultinomialFactory 
{

	List<Multinomial> terms;
	Multinomial current_term;
	
	public MultinomialFactory()
	{
		terms = new List<Multinomial>();
	}

	// Creates a new term.
	public void term()
	{
		current_term = Multinomial.ONE;
		terms.add(current_term);
	}

	public void mult(int scalar)
	{
		mult(M(scalar));
	}
	
	public void mult(IntB scalar)
	{
		mult(M(scalar));
	}
	
	public void mult(Rational scalar)
	{
		mult(M(scalar));
	}
	
	private void mult(Multinomial e)
	{
		current_term = current_term.mult(e);
		terms.pop();
		terms.push(current_term);		
	}
	
	public void mult(String var)
	{
		current_term = current_term.mult(M(var));
		terms.pop();
		terms.push(current_term);
	}
	
	public void mult(String var, IntB power)
	{
		current_term = current_term.mult(M(var, power));
		
		// Replace the current term on the stack/
		terms.pop();
		terms.push(current_term);
	}	
	
	// FIXME : This conversion could be improved from within the expressions class.
	public Multinomial getExpression()
	{
		Multinomial output = Multinomial.ZERO;
		
		for(Multinomial term : terms)
		{
			output = output.add(term);
		}
		
		return output;
	}
	
}
