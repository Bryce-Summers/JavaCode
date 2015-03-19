package Data_Structures.ADTs;

import Data_Structures.Structures.Box;
import Data_Structures.Structures.InDevelopment.Heaps.LeftistHeap;

/*
 * Abstract Data Type Specification for Meldable Heaps.
 * 
 * Written by Bryce Summers on 12 - 20 - 2013.
 * 
 * Purpose : MeldableHeaps are Heaps that support a merging operation. 
 */

public interface MeldableHeap<E extends Comparable<E>, T extends MeldableHeap<E, T>> extends Heap<E>
{
	
	// Merges two Meldable Heaps together.
	// Destroys the input heap.
	public MeldableHeap<E, T> meld(T other);
	
	public T deleteMin();
	public E peekMin();
	
	// A Combination of peekMin and deleteMin.
	public T deleteMin(Box<E> output);
	
	public T insert(E item);
	
}
