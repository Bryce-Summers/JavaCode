package Core.Types;

import Core.Matcher;
import Core.Rule;
import Core.Type;
import Data_Structures.Structures.List;

/*
 * Written by Bryce Summers on 1 / 29 / 2015.
 * 
 * String types the first X characters that fulfill the given matcher predicate.
 * 
 * This can be used to peel off strings of a repeating syntactic form.
 * 
 * if O = 'B' : Character, then this will match the string "B", "BB", "BBB", etc.
 * 
 * if O : Matcher, then this will match the string of the first X characters that match the predicate defined by the matcher.
 * 
 * if O : Type, then this will match the first X repeated instances of the chosen type.
 */

public class type_List extends Type
{
	// Recursive definition for a list
	Rule rule_same;
	Rule rule_same_base;
	
	public type_List(Character C)
	{
		Type type_List = this;
		
		// List --> T :: List.
		addRule(Rule.MP(C, type_List));

		// Base Case: List --> "";
		addRule(Rule.NULL_RULE);
	}
	
	// This list will match lists of characters that corresponds to the given matcher.
	public type_List(Matcher M)
	{
		Type type_List = this;
		
		// List --> T :: List.
		addRule(Rule.MP(M, type_List));

		// Base Case: List --> "";
		addRule(Rule.NULL_RULE);
	}
	
	// Matches the input_syntax of the given rule, and returns the output syntax.
	public type_List(Rule R)
	{
		List<Object> input  = R.get_input_copy();
		List<Object> output = R.get_output_copy();
		
		Type type_List = this;
		
		input.push(type_List);
		output.push(type_List);
		
		addRule(new Rule(input, output));
		addRule(Rule.NULL_RULE);
		
	}
	
	// O should be one of the currently supported syntactic types supported by this program.
	public type_List(Type T)//List<Object> input_syntax, List<Object> output_syntax)
	{
		Type type_List = this;
		
		// List --> T :: List.
		addRule(Rule.MP(T, type_List));

		// Base Case: List --> "";
		addRule(Rule.NULL_RULE);
	}

	
}