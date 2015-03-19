package Core;

import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;

/*
 * Written by Bryce Summers on 2/1/2015.
 * 
 * Types are defined by an ordered list of possible inference rules.
 * 
 * Algorithm: A type tries to derive an input stream of characters by attempting to apply each of its inference rules.
 * 
 * It returns an output list of characters corresponding 
 * to the consequent of the first inference rule that parses correctly.
 */

public class Type
{
	
	// The rules that define this type.
	private List<Rule> rules;

	// -- Constructor.
	public Type(Rule ... rules)
	{
		this.rules = new List<Rule>(rules);
	}
	
	private Type(List<Rule> rules)
	{
		this.rules = rules;
	}
	
	// -- Allows the user to append rules to the list of rules.
	public void addRule(Rule rule)
	{
		rules.add(rule);
	}
	
	// This function applies this Types rules for transformation to
	// the Stream of characters at the point given.
	// Returns null if the given stream of characters does not parse.
	// The position of the iterator will be unchanged if the stream does not parse.
	// The position of the iterator will point to the first character after the consumed tokens if the string does parse.
	public List<Character> derive(IterB<Character> iter)
	{
		for(Rule r : rules)
		{
			if(r == Rule.NULL_RULE)
			{
				return new List<Character>();
			}
			
			List<Character> term = r.transform(iter);
			if(term != null)
			{
				return term;
			}
		}
		
		return null;
	}
	
	// Enable the cloning of identical types.
	public Type clone()
	{
		return new Type(rules.clone());
	}
	
	@Override
	// Make sure to never change this, it is here explicitly to discourage future modifiers 
	// of this code from overriding the default equality function.
	// The inference rules will break if they cannot be associated using the parsing associative arrays.
	public boolean equals(Object o)
	{
		return this == o;
	}
	
	// Make sure to never change this, it is here explicitly to discourage future modifiers 
	// of this code from overriding the default equality function.
	// The inference rules will break if they cannot be associated using the parsing associative arrays.
	public int hashCode()
	{
		return super.hashCode();
	}
}
