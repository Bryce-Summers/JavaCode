package Core;


import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import util.StringParser;

/*
 *  Written by Bryce Summers on 2/1/2015.
 *  
 *  Specifies the simple interface for deriving a string according to 
 *  the derivation rules specified by a given type.
 */

public class Deriver
{

	// Takes in an input string and parses it and mutates it according to the given type.
	public static String derive(String input, Type t)
	{
		List<Character> data = StringParser.stringToCharList(input);
		
		// We push a character to the start of the data array to allow the deriver to
		// backtrack back to before the input characters.
		// This character will never be read.
		data.push_front(' ');
		IterB<Character> iter = data.getIter();
		iter.next();
		List<Character> output = t.derive(iter);
		
		if(output == null)
		{
			return null;
		}
		
		return StringParser.charStructureToString(output);
	}

}
