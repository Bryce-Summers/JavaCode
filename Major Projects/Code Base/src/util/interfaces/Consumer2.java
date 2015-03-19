package util.interfaces;

/*
 * The Bryce Summers consumer function interface.
 *
 * Provides a function interface for void functions that take in two arguments.
 *
 * Written on 9 - 14 - 2014.
 */

public interface Consumer2<T1, T2>
{
	public abstract void eval(T1 input1, T2 input2);
}
