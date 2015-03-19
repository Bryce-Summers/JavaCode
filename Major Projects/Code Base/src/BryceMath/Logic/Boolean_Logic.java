package BryceMath.Logic;

/*
 * Boolean Logic class.
 * Written by Bryce Summers on 5 - 23 - 2013.
 * 
 * Purpose: Provides functions fo working with boolean values.
 */

public class Boolean_Logic
{

	public static boolean xor(boolean b1, boolean b2)
	{
		return (b1 || b2) && (!b1 || !b2); 
	}
	
}
