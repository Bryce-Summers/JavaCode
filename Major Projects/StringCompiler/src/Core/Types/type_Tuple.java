package Core.Types;

import Core.Matcher;
import Core.Rule;
import Core.Type;
import Core.Matchers.AntiMatcher;
import Core.Matchers.Matcher_Minus;
import Core.Matchers.Matcher_union;

/*
 * 
 * Written by Bryce Summers on 2/1/2015.
 *
 * 
 *  Matches and derives tuples corresponding to the given special characters for
 *  tuple_begin, delimiting, and tuple_end.
 *  When derived, tuples remove symbols that are not in the Word matched by the input matcher.
 *  
 *  For instance (,) Match alphabetic characters would produce results like the following:
 *  
 *  "(a, b, asjk , dkjs , asdlj sajkd)" --> "(a,b,asjk,dkjs,asdlj)"
 */

public class type_Tuple extends Type
{

	public type_Tuple(char leftP, char DELIMETER, char rightP, Matcher WORD_MATCHER)
	{

		Matcher NON_SPECIAL = new AntiMatcher(leftP, DELIMETER, rightP);
		
		Matcher WORD = new Matcher_union(WORD_MATCHER, NON_SPECIAL);
		
		Matcher NOT_WORD = new Matcher_Minus(NON_SPECIAL, WORD_MATCHER);
		
		Type type_non_word = new type_List(NOT_WORD);
		Type type_word = new type_List(WORD);
		Type excess = new type_List(NON_SPECIAL);
		
		// When derived, tuples remove symbols that are not in the Word matched by the input matcher.
		Rule rule_elem = new Rule(Rule.P(type_non_word, type_word, excess, DELIMETER),
								  Rule.P(type_word, DELIMETER));
				
		// Rule
		Type ELEM_COMMA = new type_List(rule_elem);

		
		Rule tuple_rule = new Rule(Rule.P("(", ELEM_COMMA, type_non_word, type_word, excess, ")"),
									 Rule.P("(", ELEM_COMMA, type_word, ")"));
		
		addRule(tuple_rule);
		
	}

}
