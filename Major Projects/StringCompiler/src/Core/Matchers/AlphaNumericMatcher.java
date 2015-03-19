package Core.Matchers;

import Core.Matcher;
import Data_Structures.Structures.HashingClasses.Set;

/*
 * Written by Bryce Summers on 2/1/2015.
 * 
 * Matches any alphabetic or numerical character,
 * 	i.e 'a','b','c',
 *  * ..., '1','2','3','4','5','6','7','8','9','0'
 */

public class AlphaNumericMatcher extends Matcher
{
	Set<Character> domain;
	
	public boolean match(Character c)
	{
		return Character.isLetterOrDigit(c);
	}
}