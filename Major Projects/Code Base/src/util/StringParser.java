package util;

import java.util.Iterator;

import Data_Structures.Structures.Data_Structure;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;

/*
 * String Parsing class,
 * written by Bryce Summers on 5 - 23 - 2013.
 * 
 * Majorly Updated on 7 - 28 - 2013 through 7 - 30 - 2013.
 * 
 * Purpose : 1. This will allow objects to be de serialized easily.
 * 			 2. This will be useful for user input.
 * 			 3. This will work for everyday string processing needs.
 * 
 * 	This class takes a raw expression string and parses it into meaningful parts.
 * 	This class provides useful conversion functions between Characters and Strings.
 */

// FIXME : I should add a function that adds elements such as ',', ' ', or ';' to the interior of groups of data.
// "The", "Cat", "Ran", "Fast" --> "The", " ", "Cat", " ", "Ran", " ", "Fast".

public class StringParser
{
	// Converts a Character array to a string.
	public static String charArrToString(Character ... input)
	{
		if(input == null)
		{
			return "";
		}
		
		StringBuilder sb = new StringBuilder(input.length);
		
		for (Character c : input)
		{
		    sb.append(c.charValue());
		}

		return sb.toString();
	}
	
	// -- Converts a string into a Character array.
	public static Character[] stringToCharArr(String str)
	{
		int len = str.length();
		
		Character[] output = new Character[len];
		
		for(int i = 0; i < len; i++)
		{
			output[i] = str.charAt(i);
		}

		return output;
	}
	
	// -- Converts a string into a Character array.
	public static List<Character> stringToCharList(String str)
	{
		int len = str.length();
		
		List<Character> output = new List<Character>();
		
		for(int i = 0; i < len; i++)
		{
			output.add(str.charAt(i));
		}

		return output;
	}
	
	// Converts a Character Data_Structure into a String.
	public static String charStructureToString(Data_Structure<Character> data)
	{
		
		StringBuilder output = new StringBuilder();
		
		for(Character c : data)
		{
			output.append(c);
		}
		
		return output.toString();
	}
	
	// -- Converts a string into a Character array.
	public static IterB<Character> createIterator(String str)
	{
		return stringToCharList(str).getIter();
	}
	
	// -- String parsing of Expressions.
	
	/* Takes in a string representing an expression.
	 * Returns a list of Strings representing sub expressions with connectives in between them.
	 * This list can then be parsed into an expression by following the order of operations.
	 * e.g (5 + 6x)/5 ^3 -> [(, 5+6x, ), /, 5, ^, 3].
	 * 		( -> []
	 */
	
	// NOTE : multiplication connectives are omitted. Any two terms that are not joined by a connective are assumed to be joined by a multiplication connective.
	// NOTE : This function guaranteed that all left quotes and braces are matched by right quotes and braces.	
	
	// FIXME : Remove excessive Parentheses from data. ((((7)))) should be reduced to 7.
	// FIXME : Decide whether of not to convert '{', '}' and '[', ']' to left and right parentheses.
	
	// FIXME : Convert all horrible dash like characters to standardized '-' characters.

	public static List<String> parseExpression(String e)
	{
		
		// Remove all spaces.
		// e = e.replace(" ", "");

		/*
		// Remove all multiplication connectives.
		e = e.replace("*", "");
		*/
		
		/*
		e = e.replace("{", "(");
		e = e.replace("[", "(");
		e = e.replace("]", ")");
		e = e.replace("}", ")");
		*/
				
		char[] array = e.toCharArray();
		
		List<String> output = new List<String>();
		StringBuilder current_string = new StringBuilder();
		
		int parens_depth = 0;
		
		char parens_charL = ' ';
		char parens_charR = ' ';
		
		// Process all of the characters.
		for(char c : array)
		{
			/* -- Ignore all non supported characters,
			 *    if they are not part of a string literal.
			 */
			switch(c)
			{
				case ' ' :
				case '!' :
				case '?' :
				case '\\':
				case '_' :
				case '@' :
				case '#' :
				case '$' :
				case '%' :
				case '&' :
				case ';' :

					// Skip non supported characters if we are not enclosed sub string.
					if(parens_charR == ' ')
					{
						continue;
					}
				default: // Do nothing.
						
			}
			
			if(c == parens_charL)
			{
				parens_depth++;
			}
			
			if(c == parens_charR)
			{
				parens_depth--;
			}
			
			// Just tack on any and all characters if we are inside of a parentheses.
			if(parens_depth > 0)
			{
				current_string.append(c);
				continue;
			}
			
			// Handle braces and connectives.
			switch(c)
			{
				case '(' :
				case '[' :
				case '{' :
					
					// The last string is done being parsed.
					current_string = addLastParsedString(output, current_string);
					
					// Add the left parentheses.
					output.add("" + c);
					
					// Update the searched for characters.
					parens_charL = c;
					parens_charR = getRightParen(c);
					parens_depth++;
					continue;
					
				case ')' :
				case ']' :
				case '}' :
					
					// Ignore misplaced parentheses, but do not concatenate seperated numbers!!
					if(c != parens_charR)
					{
						// The last string is done being parsed.
						current_string = addLastParsedString(output, current_string);
						continue;
					}

					// The last string is done being parsed.
					current_string = addLastParsedString(output, current_string);
					
					// Add the closing brace.
					output.add("" + c);
					
					// Indicate that we are no longer searching for characters.
					parens_charL = ' ';
					parens_charR = ' ';
					continue;
					
				case '+':
				case '-':
				case '^':
				case '=':
				case '/':
					// The last string is done being parsed.
					current_string = addLastParsedString(output, current_string);
					
					// Add the connective.
					output.add("" + c);
					continue;
					
				case '\'':
				case '"' :
					
					// Handle the right quote.
					if(parens_charR == c)
					{
						// The last string is done being parsed.
						current_string = addLastParsedString(output, current_string);
						
						// Add the closing brace.
						output.add("" + c);
						
						// Indicate that we are no longer searching for characters.
						parens_charL = ' ';
						parens_charR = ' ';
						continue;
					}
					
					// Handle left quote.
					
					// The Left and right quotes are the same, so we cannot allow multiple depths.
					//parens_charL = c;
					
					// look for ' or ":
					parens_charR = getRightParen(c);
					parens_depth++;
			}

			
			// Handle Variables, numbers, and periods. (and Commas?).
			
			
			// --Handle numbers.
			if(partOfANumber(c))
			{
				// Just tack on the number, because the current_string is guranteed to be a number string.
				current_string.append(c);
				continue;
			}
			
			// -- Handle the new variable case.
			
			// Load the last number, and reset the current_string.
			current_string = addLastParsedString(output, current_string);
			
			// Add the Variable that is a character currently.
			output.add("" + c);
		}
		
		// Add the last parsed string the output;
		addLastParsedString(output, current_string);
		
		// Add right parens to malformed statements,
		//remove left parens if their would be no data inside of them.
		if(parens_depth > 0)
		{
			String last = output.getLast();

			if(!last.equals(parens_charL + "") || parens_depth > 1)
			{
				output.add("" + parens_charR);			
			}
			else
			{
				output.rem();
			}
		}
		
		return output;
	}
	
	// -- Helper functions for Expression parsing.
	private static StringBuilder addLastParsedString(List<String> output, StringBuilder current_string)
	{
		// Do nothing if the entered string builder is trivial. 
		if(current_string.length() == 0)
		{
			return current_string;
		}
		
		output.add(current_string.toString());
		return new StringBuilder();
	}
	
	// FIXME : Remove these if they are not needed.
	
	public static char getRightParen(char c)
	{
		switch(c)
		{
			case '(' :
				return ')';
				
			case '{' :
				return '}';
				
			case '[' :
				return ']';
				
			case '\'' :
				return '\'';
				
			case '"' :
				return '"';
		}
		
		throw new Error("Character '" + c + "' has no supported matching right brace.");
	}
	
	public static boolean isConstantNumber(String str)
	{
		return partOfANumber(str.charAt(0));
	}
	
	private static boolean partOfANumber(char c)
	{
		return Character.isDigit(c) || c == '.';
	}
	
	public static boolean isAlphaNumericCharacter(char c)
	{
		return Character.isDigit(c) || Character.isLetter(c);
	}
	
	public static void scanFor(Iterator<Character> iter, char looking_for)
	{
		while(iter.hasNext() && iter.next() != looking_for);
	}
	
	/*
	// Returns true if it has stopped on the next occurrence of the given character.
	private static boolean scanFor(Iterator<Object> iter, char looking_for)
	{
		while(iter.hasNext())
		{
			Object o = iter.next();
			
			if(!(o instanceof Character))
			{
				continue;
			}
			
			if((Character)o == looking_for)
			{
				return true;
			}
		}
		
		return false;
	}*/
	
	// REQUIRES : "String" + x, where x is a decimal digit between 0 - 9.
	// ENSURES : returns x.
	public static int getEndDigit(String str)
	{
		int length = str.length();
		
		char c = str.charAt(length - 1);
		
		return Character.getNumericValue(c);
	}
}
