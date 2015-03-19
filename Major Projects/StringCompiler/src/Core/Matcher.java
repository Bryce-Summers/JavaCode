package Core;

import Core.Matchers.UniversalMatcher;

/* 
 * Written by Bryce Summers on 1/30/2015.
 * 
 * A very simple class that defines a predicate for valid characters.
 * 
 * I believe that I could probably implement matchers in terms of types with a-->a rules, but I will not.
 */

public abstract class Matcher
{
	// Useful Matchers.
	public static Matcher ALL = new UniversalMatcher();

	public Matcher()
	{
		
	}
	
	public abstract boolean match(Character iter);

	@Override
	// Make sure to never change this, it is here explicity to discourage future modifiers 
	// of this code from overriding the default equality function.
	// The inference rules will break if they cannot be associated using the parsing associative arrays.
	public boolean equals(Object o)
	{
		return this == o;
	}
	
	// Make sure to never change this, it is here explicity to discourage future modifiers 
	// of this code from overriding the default equality function.
	// The inference rules will break if they cannot be associated using the parsing associative arrays.
	public int hashCode()
	{
		return super.hashCode();
	}
	
}
