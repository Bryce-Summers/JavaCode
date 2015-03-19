package Examples;

import Core.Deriver;
import Core.Matcher;
import Core.Rule;
import Core.Type;
import Core.Matchers.AntiMatcher;
import Core.Types.type_List;

/*
 * An example finished on 2/1/2015 by Bryce Summers.
 * 
 * This example shows a grammar that translates tuples enclosed by '(' and ')' and deliminated by ','
 * 
 * to a tuple with the elements now enclosed with double quotation marks: '"'
 * 
 * If you wanted to you could parameterize the symbols and then replace the occurrences of 
 * those symbols with the variables. Check out the type_Tuple.java class for an example of this.
 * 
 * i.e. char LEFT_PARENS = '('; char DELIMETER = ',',
 * 
 * Proper input and output for the example given is the following:
 * 
 * 	In:  ()
 *	Out: ("")
 *	In:  (a)
 *	Out: ("a")
 *	In:  (a, b)
 *	Out: ("a"," b")
 *	In:  ( a,b, c ,d ,e,f)
 *	Out: (" a","b"," c ","d ","e","f")
 *	In:  a,b,c)
 *	Out: null
 *	In:  (a,
 *	Out: null
 *
 *	Note that null is returned when the input expression does not parse.
 *
 * This example is also a good display of the power of lists, which can match empty lists as well as any concurrent 
 * listing of similar types.
 * 
 */


public class ListStringAdder extends Type
{

	public static void main(String[] args)
	{
		
		Matcher WORD = new AntiMatcher('(', ',', ')');
		
		Type S1 = new type_List(WORD);
		
		Rule rule_elem = new Rule(Rule.P(S1, ","),
								  Rule.P("\"", S1, "\","));
		
	
		// Rule
		Type ELEM_COMMA = new type_List(rule_elem);
		

		// Define the syntactic structure of a tuple and the transformation we wish to apply to it.
		// In this case the procedure is A --> "A"
		Rule rule_removal = new Rule(Rule.P("(", ELEM_COMMA, S1, ")"),
									 Rule.P("(", ELEM_COMMA, "\"", S1, "\"", ")"));
		
		Type type_removal = new Type(rule_removal);
		
		// Several somewhat interesting inputs.
		// Note: Not all of these are well formed.
		String[] inputs = {"()", "(a)", "(a, b)", "( a,b, c ,d ,e,f)", "a,b,c)", "(a,"};
		
		for(String s : inputs)
		{
			String input = s;
			String output = Deriver.derive(input, type_removal);
			println("In:  " + input);
			println("Out: " + output);
		}
		
		
	}
	
	public static void println(String s)
	{
		System.out.println(s);
	}
	

	

}
