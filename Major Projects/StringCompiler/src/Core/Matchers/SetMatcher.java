package Core.Matchers;

import Core.Matcher;
import Data_Structures.Structures.HashingClasses.Set;

/*
 * Written by Bryce Summers on 2/1/2015.
 * 
 * Matches a set of characters.
 */

public class SetMatcher extends Matcher
{
	Set<Character> domain;
	
	public SetMatcher(Character ... includes)
	{
		domain = new Set<Character>();
		
		domain.set_add(includes);
	}
	
	public boolean match(Character c)
	{
		return domain.includes(c);
	}
}