package Data_Structures.ADTs;

import Data_Structures.Structures.Pair;

/*
 * Queue interface.
 * 
 * Written by Bryce Summers on 7 - 28 - 2013.
 * Updated 12 - 20 - 2013 : Added Cost Bounds. Repurposed this class as an Abstract Data Type.
 * 
 * Purpose : An Abstract Data Type Specification of First in, First out queues.
 */

public interface Queue<E>
{
	// -- Operation functions.
	public void enq(E elem); 	// O(1).
	public E deq();				// O(1).
	
	// Static functions for immutable queues.
	public Queue<E> enq_static(E elem);		// O(1).
	public Pair<E, Queue<E>> deq_static();	// O(1).

	// -- Size functions.
	public boolean isEmpty();	// O(1).
	public int size();		 	// O(1).
	
	public E peek();
	
}