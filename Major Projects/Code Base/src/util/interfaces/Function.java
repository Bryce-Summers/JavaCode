package util.interfaces;

/*
 * The Bryce Summers function interface.
 * 
 * Allows for functions that map from T1 to T2 to be passed as arguments to functions.
 * 
 *  Written by Bryce Summers on 6 - 12 - 2013.
 *  
 *  FIXME : I have many classes that do not need the return argument. I should separate them into a different interface.
 */

public interface Function<T1, T2>
{
	public abstract T2 eval(T1 input);
}
