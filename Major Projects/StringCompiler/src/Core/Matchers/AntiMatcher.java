package Core.Matchers;

import Core.Matcher;
import Data_Structures.Structures.HashingClasses.Set;

/*
 * Written by Bryce Summers on 2/1/2015.
 * 
 * Matches any character, except those in the set of excluded symbols 
 * specified by a given domain of discourse. 
 * 
 */

public class AntiMatcher extends Matcher
{
	Set<Character> domain;
	
	public AntiMatcher(Character ... avoids)
	{
		domain = new Set<Character>();
		
		domain.set_add(avoids);
	}
	
	public boolean match(Character c)
	{
		return !domain.includes(c);
	}
}