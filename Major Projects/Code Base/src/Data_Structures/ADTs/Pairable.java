package Data_Structures.ADTs;

/*
 * The Pairable interface.
 * 
 * Written by Bryce Summers on 6 - 3 - 2013.
 * 
 * Purpose : Allows for me to cast data structures as pairs for users
 * 			 without exposing extraneous functions that may confuse them.
 */

public interface Pairable<T>
{
	public abstract T getFirst();
	public abstract T getLast();
	
	public abstract boolean equals(Object o);
}
