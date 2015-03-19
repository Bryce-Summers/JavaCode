package Core.Matchers;

import Core.Matcher;

/*
 * Written by Bryce Summers on 2/1/2015.
 * 
 * Matches any character matched by A, but not matched by B.
 */

public class Matcher_Minus extends Matcher {

	Matcher A, B;
	
	public Matcher_Minus(Matcher A, Matcher B)
	{
		this.A = A;
		this.B = B;
	}

	@Override
	public boolean match(Character iter)
	{
		return A.match(iter) && !B.match(iter);
	}

}
