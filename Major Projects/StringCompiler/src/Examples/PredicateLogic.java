package Examples;

import Core.Deriver;
import Core.Matcher;
import Core.Rule;
import Core.Type;
import Core.Matchers.AlphaNumericMatcher;
import Core.Types.type_List;
import Core.Types.type_Tuple;

/*
 * An example for my String Compiler
 * Written by Bryce Summers on 2/1/2015.
 * 
 * Purpose: This example shows how we can define a grammar for predicate logic and 
 * 			how we can use this grammar to reduced strings to a canonical form, which would aid any autograding 
 * 			program trying to check strings of predicate logic symbols for syntactic equivalence.
 * 
 * I have taken the liberty of expressing a disjunction as '|',
 * because it is safer and easier to handle than "V" or "V."
 * 
 * Current output as of 2/1/2015:
 * 
	In:  "  ( A  & B ) "
	Out: "(A&B)"
	In:  "(A)"
	Out: "A"
	In:  "(A--->B)"
	Out: "(A->B)"
	In:  " ( A  |B )"
	Out: "(A|B)"
	In:  "(j <----> k)"
	Out: "(j<->k)"
	In:  "(A & (B --> C)) "
	Out: "(A&(B->C))"
	In:  "((A & B) | (B & C))"
	Out: "((A&B)|(B&C))"
	In:  "((A & (B --> C)) | (J<---> k) )"
	Out: "null"
	In:  "(a& ( b&c))"
	Out: "(a&(b&c))"
	In:  "((a & b )&c))"
	Out: "((a&b)&c)"
	In:  "((A&B) &(A&B))"
	Out: "((A&B)&(A&B))"
	In:  "~ (H & J ) "
	Out: "~(H&J)"
	In:  "V(a, b, c, d)"
	Out: "V(a,b,c,d)"
	In:  "((h & j(k)) ---> g(a, b)))"
	Out: "((h&j(k))->g(a,b))"
	In:  " (E. )  (Bryce --> Sieg)"
	Out: "(E.)(Bryce->Sieg)"
	In:  "(  A.  ) (h(a,b ,  c,  d))"
	Out: "(A.)h(a,b,c,d)"
	In:  "(((((atom)))) & other atom)"
	Out: "null"
 */


public class PredicateLogic extends Type
{

	public static void main(String[] args)
	{
		// -- Define the type names first.
		Type EXPRESSION = new Type();
		Type BINARY_CONNECTIVE = new Type();
		Type UNARY_CONNECTIVE = new Type();
		
	
		Type DASHES = new type_List('-');
		Type SPACES = new type_List(' ');
		
		Type ATOM = new Type();
		
		Matcher matcher_atom = new AlphaNumericMatcher();
		Type TUPLE = new type_Tuple('(', ',', ')', matcher_atom);

		
		// -- Define the names of Rules.
		
		// Define the rules names.
		
		Rule rule_binary_decomposition = new Rule();
		
		Rule rule_conditional = new Rule();
		Rule rule_disjunction = new Rule();
		Rule rule_conjunction = new Rule();
		Rule rule_equivalence = new Rule();
		Rule rule_negation    = new Rule();
		Rule rule_forall 	  = new Rule();
		Rule rule_exists 	  = new Rule();

		Rule SPACE_REMOVAL_LEFT  = new Rule();
		Rule SPACE_REMOVAL_RIGHT = new Rule();
		Rule PAREN_REMOVAL = new Rule();
		
		Rule rule_ATOM = Rule.MP(ATOM);
		
		Rule rule_predicate = new Rule();
		
		// -- Define which types have which rules.
		
		// Note: Precedence is determined based the order the rules are added.
		
		EXPRESSION.addRule(rule_forall);
		EXPRESSION.addRule(rule_exists);
		EXPRESSION.addRule(rule_negation);
		
		EXPRESSION.addRule(rule_binary_decomposition);
		EXPRESSION.addRule(SPACE_REMOVAL_LEFT);
		//EXPRESSION.addRule(SPACE_REMOVAL_RIGHT);
		
		EXPRESSION.addRule(PAREN_REMOVAL);
		
		// These can be combined at the head to improve performance.s
		EXPRESSION.addRule(rule_predicate);
		EXPRESSION.addRule(rule_ATOM);
		
		BINARY_CONNECTIVE.addRule(rule_equivalence);
		BINARY_CONNECTIVE.addRule(rule_conditional);
		BINARY_CONNECTIVE.addRule(rule_conjunction);
		BINARY_CONNECTIVE.addRule(rule_disjunction);
		
		
		// Define some explicit instances of type names.
		Type E1 = EXPRESSION.clone();
		Type E2 = EXPRESSION.clone();		
		
		Type SPACES1 = SPACES.clone();
		Type SPACES2 = SPACES.clone();
		
		// -- Define the Actual Derivation and Grammatical rules.
		
		rule_binary_decomposition.define(new Rule(
				Rule.P("(", E1, SPACES, BINARY_CONNECTIVE, SPACES, E2, ")"),
				Rule.P("(", E1, BINARY_CONNECTIVE, E2, ")")
				));
		
		rule_conditional.define(new Rule(Rule.P(DASHES, ">"), Rule.P("->")));
		rule_disjunction.define(Rule.MP("|"));
		rule_conjunction.define(Rule.MP("&"));
		
		rule_equivalence.define(new Rule(Rule.P("<", DASHES, ">"),
									Rule.P("<->")));
		
		rule_negation.define(Rule.MP("~", EXPRESSION));
		
		rule_forall.define(new Rule(Rule.P("(", SPACES1, "A.", SPACES2, ")", E1),
									   Rule.P("(A.)", E1)));
		
		rule_exists.define(new Rule( Rule.P("(", SPACES1, "E.", SPACES2, ")", E1),
				   						Rule.P("(E.)", E1)));
		
		SPACE_REMOVAL_LEFT.define(new Rule(Rule.P(" ", E1), Rule.P(E1)));
		SPACE_REMOVAL_RIGHT.define(new Rule(Rule.P(E1, " "), Rule.P(E1)));
		PAREN_REMOVAL.define(new Rule(Rule.P("(", E1, ")"), Rule.P(E1)));
		
		
		// Define Atomic syntax.
		Type ATOM_LIST = new type_List(matcher_atom);
		Rule rule_ATOM_parts = new Rule(Rule.P(SPACES, matcher_atom, ATOM_LIST, SPACES),
										Rule.P(matcher_atom, ATOM_LIST));
		ATOM.addRule(rule_ATOM_parts);
		
		// -- Define Predicate syntax.
		rule_predicate.define(new Rule(Rule.P(SPACES, ATOM, SPACES, TUPLE, SPACES),
									   Rule.P(ATOM, TUPLE)));
		
		
		
		// -- Test out the syntax with example.
		
		String[] inputs = {"  ( A  & B ) ", "(A)", "(A--->B)", " ( A  |B )", "(j <----> k)", "(A & (B --> C)) ", 
							"((A & B) | (B & C))", "((A & (B --> C)) | (J<---> k) )",
							"(a& ( b&c))", "((a & b )&c))", "((A&B) &(A&B))",
							"~ (H & J ) ", "V(a, b, c, d)", "((h & j(k)) ---> g(a, b)))", " (E. )  (Bryce --> Sieg)", "(  A.  ) (h(a,b ,  c,  d))",
							"(((((atom)))) & other atom)"};
		
		for(String s : inputs)
		{
			String input = s;
			String output = Deriver.derive(input, EXPRESSION);
			println("In:  \"" + input + "\"");
			println("Out: \"" + output + "\"");
		}
		

		
		
		// Define the rules.
		// = new Rule{E1, BINARY_CONNECTIVE, E2);
		
	}
	
	public static void println(String s)
	{
		System.out.println(s);
	}
	

	

}
