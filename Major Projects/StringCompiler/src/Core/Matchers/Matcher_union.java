package Core.Matchers;

import Core.Matcher;

/*
 * Written by Bryce Summers on 2/1/2015.
 * 
 * Matches all characters matched by any of the input matchers.
 */

public class Matcher_union extends Matcher
{

	Matcher A, B;
	
	public Matcher_union(Matcher A, Matcher B)
	{
		this.A = A;
		this.B = B;
	}

	@Override
	public boolean match(Character iter)
	{
		return A.match(iter) && B.match(iter);
	}

}