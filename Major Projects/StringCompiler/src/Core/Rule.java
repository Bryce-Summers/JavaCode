package Core;

import util.StringParser;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.AArray;

/*
 * Written by Bryce Summers on 1/30/2015.
 * 
 * A Rule object specifier a rule for deriving one syntax to another.
 * 
 * Algorithm: Consume the input characters to associate strings of characters with a given Type object.
 *  Then return the result of applying the inference rules to the recursive tree of types.
 *  Rules automatically detect and then backtrack when they find that they do not apply to a 
 *  given stream of characters due to it not parsing within the syntactic grammar of the input syntax.
 *    
 */

public class Rule
{

	/* A Rule is defined by an input syntax and an Output syntax.
	 * 
	 * For a rule to map input types to output types, the same type object should be used.
	 */
	List<Object> input_syntax;
	List<Object> output_syntax;
	
	
	public static final Rule NULL_RULE = new Rule();
	
	// Delayed definition constructor, mainly for recursively defined rules.
	// The programmer will be responsible for calling the define function with the rules information.
	public Rule()
	{
		
	}
	
	public Rule(Iterable<Object> input_raw, Iterable<Object> output_raw)
	{
		define(input_raw, output_raw);
	}
	
	public Rule(Object[] input_raw, Object[] output_raw)
	{
		define(input_raw, output_raw);
	}
	
	// Defines a rule that merely defines a parse pattern, but no internal mutations.
	public Rule(Object[] pattern_match)
	{
		define(pattern_match, pattern_match);
	}
	
	// Allows for rules to be defined after their instantiation.
	// This is particularly important for recursively defined rules.
	public void define(Iterable<Object> input_raw, Iterable<Object> output_raw)
	{
		this.input_syntax  = new List<Object>();
		this.output_syntax = new List<Object>();
		
		translate(input_raw, input_syntax);
		translate(output_raw, output_syntax);
	}
	
	// Allows for rules to be defined after their instantiation.
	// This is particularly important for recursively defined rules.
	public void define(Object[] input_raw, Object[] output_raw)
	{
		this.input_syntax  = new List<Object>();
		this.output_syntax = new List<Object>();
		
		translate(input_raw, input_syntax);
		translate(output_raw, output_syntax);
	}
	
	public void define(Rule R)
	{
		input_syntax  = R.input_syntax.clone();
		output_syntax = R.output_syntax.clone();
	}
	
	// Translates from raw syntactic instructions to an atomic form.
	private static void translate(Iterable<Object> data_raw, List<Object> data)
	{
		// Process the output syntax.
		for(Object o : data_raw)
		{
			if(o instanceof String)
			{
				Object[] chars = StringParser.stringToCharArr((String)o);
				data.append(chars);
				continue;
			}
			
			if(o instanceof Type || o instanceof Matcher)
			{
				data.append(o);
				continue;
			}			
			
			if(o instanceof Character)
			{
				data.append(o);
				continue;
			}
			
			throw new Error("Malformed Rule!! Please only use Characters, Strings, Types, and predicate matchers! " + o.getClass());
		}
	}
	
	// Translates from raw syntactic instructions to an atomic form.
	private static void translate(Object[] data_raw, List<Object> data)
	{
		// Process the output syntax.
		for(Object o : data_raw)
		{
			if(o instanceof String)
			{
				Object[] chars = StringParser.stringToCharArr((String)o);
				data.append(chars);
				continue;
			}
			
			if(o instanceof Character || o instanceof Type || o instanceof Matcher)
			{
				data.append(o);
				continue;
			}			
			
			throw new Error("Malformed Rule!! Please only use Strings, Types, and predicate matchers! " + o.getClass());
		}
	}

	// Returns a list of characters that are the result of the rule being applied.
	// Returns null if the inputs stream cannot be properly parsed.
	// If it returns true then the rule will have been applied.
	// Maintains its proper iteration state via recursion.
	public List<Character> transform(IterB<Character> data)
	{
		
		IterB<Object> input_syntax_iter = this.input_syntax.getIter();
		
		// Associates Types and Matchers with Lists of characters.
		// We are expoiting the standard java hashing and equality functions based on the reference address.
		// Objects will match only if they are the exact same objects in memory.
		AArray<Object, List<Character>> parsedTypes = new AArray<Object, List<Character>>();
		
		boolean success = transform(input_syntax_iter, data, parsedTypes);
		
		if(!success)
		{
			return null;
		}
		
		List<Character> output = new List<Character>();
		
		// Map the parsed input types to output types.
		for(Object o : output_syntax)
		{
			if(o instanceof Character)
			{
				output.add((Character)o);
				continue;
			}
			
			if(o instanceof Type || o instanceof Matcher)
			{
				List<Character> term = parsedTypes.lookup(o);
				
				// FIXME : Use destructiveAppend and work make sure we are not reusing temporary lists.
				// UPDATE : The reoson destructive append cannot be used, is because some grammars specify a 
				// duplication of the given type, so we might append the list multiple times.
				if(term == null)
				{
					throw new Error("Output Term is unbound! It is possble that you have listed a term in" +
									" the output syntax list that was not listed in the input.");
				}
				output.append(term);
				continue;
			}
			
			throw new Error("Malformed Output Tokens!");
		}
		
		// Return the completed list of Objects.	
		return output;
	}
	
	// Must be guaranteed to not change the position of the data pointer if it returns false. 
	private boolean transform(IterB<Object> input_syntax, IterB<Character> data, 
			AArray<Object, List<Character>> parsedTypes)
	{
		// Done parsing the input syntax.
		if(!input_syntax.hasNext())
		{
			return true;
		}
		
		Object o = input_syntax.next();
		
		/* Note: It is ok if the data branch has no remaining characters as long as the 
		 *       input syntax consists of nonconstant characters.
		 *       This is very important, because the user may have specified a grammar that can correctly 
		 *       parse types that can be satisfied by empty character strings. 
		 *       Please see the type_List class for a example of a type that matches 
		 *       an empty list as well as any sequence of types.      
		 */
		
		if(o instanceof Type)
		{
			
			IterB<Object> iter_revert = (IterB<Object>)data.clone();
			
			Type t = (Type)o;
			List<Character> subTerm = t.derive(data);
			
			// Parsing failed.
			if(subTerm == null)
			{
				rewind(data, iter_revert);
				return false;
			}
			
			// Associate the parsed term data.
			parsedTypes.insert(t, subTerm);
			
			// Parse the remaining input syntax.
			boolean success = transform(input_syntax, data, parsedTypes);
			
			if(!success)
			{
				rewind(data, iter_revert);
			}
			
			return success;
			
		}
		
		// -- Characters and Matchers that directly look at the character stream.
		
		// Data has ended when a character is expected.
		// It is imperative that this check be here instead of above.
		// FIXME : Perhaps I should propagate an error message saying what the problem was.
		// FIXME : Determine whether this conditional should be moved inside of each of the following type checks.
		if(!data.hasNext())
		{
			return false;
		}

		if(o instanceof Character)
		{
			char syntax_c = (Character)o;
						
			char data_c = data.next();
			
			// Parse failed due to unmatched characters.
			if(syntax_c != data_c)
			{
				data.previous();
				return false;
			}
			
			boolean success = transform(input_syntax, data, parsedTypes);
			
			if(!success)
			{
				data.previous();
			}
			
			return success;
		}
		

		
		if(o instanceof Matcher)
		{
			Matcher predicate = (Matcher)o;
			
			char data_c = data.next();
			
			// Parse failed to to unmatched characters.
			// FIXME : Update matchers to handle strings of length >= 1.
			if(!predicate.match(data_c))
			{
				data.previous();
				return false;
			}
			
			parsedTypes.insert(predicate, new List<Character>(data_c));
			
			boolean success = transform(input_syntax, data, parsedTypes);
			
			// Rewind the stream of characters upon failure.
			if(!success)
			{
				data.previous();
			}
			
			return success;
		}
		
		throw new Error("Malformed Input Syntax");

	}
	
	private void rewind(IterB<Character> data, IterB<Object> iter_revert)
	{
		while(!data.equals(iter_revert))
		{
			data.previous();
		}
	}

	// Converts a list of objects into an array of objects suitable for 
	// passing as a parameter for a rule construction.
	// P stands for "Pattern"
	public static Object[] P(Object... objs)
	{
		return objs;
		
	}
	
	// Matching pattern, directly returns a rule.
	public static Rule MP(Object ... objs)
	{
		return new Rule(objs);
	}
	
	public List<Object> get_input_copy()
	{
		return input_syntax.clone();
	}
	
	public List<Object> get_output_copy()
	{
		return output_syntax.clone();
	}
}
