package util;

//- Useful testing functions.
import static util.Print.print;
import static util.testing.ASSERT;
import Data_Structures.Structures.List;

public class aaTestingAndCasePrinter
{

	public aaTestingAndCasePrinter()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		aaTestingAndCasePrinter test = new aaTestingAndCasePrinter();
		
		test.printCases();
		test.testStringParser();
		
		print("All Tests have passed!");
	}
	
	
	// This function allows be to print out a ton of case statements for switch statements.
	private void printCases()
	{
		// Generation code for a whole bunch of key events according to characters.
		// 'A' - 'Z'
/*		for(int i = 65; i <= 90; i++)
		{
			// Uppercase characters.
			char c = (char)i;
			print("case '" + c + "' :");
			
			
			// Lowercase characters.
			char c2 = (char)(i + 32);
			print("case '" + c2 + "':");
			
			print( "return KeyEvent.VK_" + c +";");
		}
		*/
		
		// Case upon numerical characters.
		/*for(int i = 0; i < 10; i++)
		{
			// Uppercase characters.
			print("case '" + i + "' : return KeyEvent.VK_" + i + ";");
						
			
		}*/
	}
	
	private void testStringParser()
	{
		String e1;//, e2, e3;
		//List<String> result;
		List<String> expected;
		
		// Trivial Input.
		e1 = "";
		expected = L();
		ASSERT(correctParsing(e1, expected));
		
		// -- Constant Integers.
		
		e1 = "1";
		expected = L("1");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "1234321";
		expected = L("1234321");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "1234321.2432749789723498";
		expected = L("1234321.2432749789723498");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "42357890274589742324779327498.43642873463298789";
		expected = L("42357890274589742324779327498.43642873463298789");
		ASSERT(correctParsing(e1, expected));
		
		// -- Variables
		e1 = "A";
		expected = L("A");
		ASSERT(correctParsing(e1, expected));
		
		// -- Simple connectives.
		
		e1 = "1 + 1";
		expected = L("1", "+", "1");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "5+ 7 + 8+3.325";
		expected = L("5", "+", "7", "+", "8", "+", "3.325");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "6 + y + 4.2";
		expected = L("6", "+", "y", "+", "4.2");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "6x";
		expected = L("6", "x");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "x^2";
		expected = L("x", "^", "2");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "x^2 + 5x + 1";
		expected = L("x", "^", "2", "+", "5", "x", "+", "1");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "x^2 + 5x + 1";
		expected = L("x", "^", "2", "+", "5", "x", "+", "1");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "7xyz^2 + 6x^7/23^2";
		expected = L("7", "x", "y", "z", "^", "2", "+", "6", "x", "^", "7", "/", "23", "^", "2");
		ASSERT(correctParsing(e1, expected));
		
		// -- Test Parentheses.
		e1 = "5 + (8xy +5)/[54-x] + {5+x^{5}}^{ei}";
		expected = L("5", "+", "(", "8xy+5", ")", "/", "[", "54-x", "]", "+", "{", "5+x^{5}", "}", "^", "{", "ei", "}");
		ASSERT(correctParsing(e1, expected));
		
		
		// -- Malformed data attempted correction.
		
		
		e1 = "{5{3{2}}}}6";
		//print(StringParser.parseExpression(e1));
		expected = L("{", "5{3{2}}", "}", "6");
		ASSERT(correctParsing(e1, expected));
		
		e1 = "45}]})})5";
		expected = L("45", "5");
		ASSERT(correctParsing(e1, expected));
		
		// This case could potentially build up a ton of recursion...
		e1 = "({[{{{{[[[{({((45";
		expected = L("(", "{[{{{{[[[{({((45", ")");
		ASSERT(correctParsing(e1, expected));
		
		// -- Test parentheses.
		
		e1 = "'Bryce' + 'work' = " + "\"Joy\"?";
		expected = L("\'", "Bryce", "'", "+", "\'", "work", "\'", "=", "\"", "Joy", "\"");
		//print(StringParser.parseExpression(e1));
		ASSERT(correctParsing(e1, expected));
		
		
		e1 = "(";
		expected = L();
		//print(StringParser.parseExpression(e1));
		ASSERT(correctParsing(e1, expected));
	}
	
	// The predicate used in these unit tests.
	private boolean correctParsing(String input, List<String> expected_output)
	{
		return StringParser.parseExpression(input).equals(expected_output);
	}
	
	private List<String> L(String ... input)
	{
		return new List<String>(input);
	}

}
