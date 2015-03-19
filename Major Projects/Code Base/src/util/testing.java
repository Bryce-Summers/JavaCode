package util;

/*
 * Testing Utility class.
 * Written by Bryce Summers on 5 - 30 - 2013.
 * 
 * Purpose : This class provides standard useful unit testing functions.
 */

public class testing
{
	public static void ASSERT(boolean predicate)
	{
		if(predicate == false)
		{
			throw new Error("Assertion Failed");
		}
	}
}
