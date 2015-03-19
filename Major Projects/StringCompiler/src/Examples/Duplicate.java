package Examples;

import Core.Deriver;
import Core.Matcher;
import Core.Rule;
import Core.Type;
import Core.Types.type_List;

/*
 * Example of a translation that duplicates the input.
 * 
 * Example works correctly as of 2/30/2015
 * 
 * Written by Bryce Summers.
 * 
 * Proper Output for the example given:
 * 
 * 	Bryce Summers Bryce Summers 
	has has 
	achieved achieved 
	duplication duplication 

 */

public class Duplicate extends Type
{

	public static void main(String[] args)
	{
		
		// Create a Universal Type that matches anything.
		Type S = new type_List(Matcher.ALL);
		
		// S --> SS
		Rule rule_duplicate = new Rule(Rule.P(S), Rule.P(S, S));
		Type type_duplicate = new Type(rule_duplicate);

		String output;
		output = Deriver.derive("Bryce Summers ", type_duplicate);
		println(output);
		
		output = Deriver.derive("has ", type_duplicate);
		println(output);
		
		output = Deriver.derive("achieved ", type_duplicate);
		println(output);
		
		output = Deriver.derive("duplication ", type_duplicate);
		println(output);
	}
	
	public static void println(String s)
	{
		System.out.println(s);
	}	

}
