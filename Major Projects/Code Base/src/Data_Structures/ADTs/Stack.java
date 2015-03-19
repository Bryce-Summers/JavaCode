package Data_Structures.ADTs;

import Data_Structures.Structures.Pair;

/*
 * Stack interface.
 * 
 * Written by Bryce Summers on 7 - 28 - 2013.
 * Updated 12 - 20 - 2013 : Added Cost Bounds. Repurposed this class as an Abstract Data Type.
 * 
 * Purpose : An Abstract Data Type Specification of Last in, First out stacks.
 */

public interface Stack<E>
{
	// -- Operation functions.
	public void push(E elem);	// O(1).
	public E pop();				// O(1).
	
	// Static functions for immutable stacks.
	public Stack<E> 		 push_static(E elem); // O(1).
	public Pair<E, Stack<E>> pop_static();	  // O(1).
	
	public E top(); // O(1).
	
	// -- Size functions.
	public boolean isEmpty();	// O(1).
	//public int size();		// O(1).
}
