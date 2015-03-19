package Data_Structures.ADTs;

/*
 * Specifies the Mathematical Abstract Data type for a Priority Queue.
 * 
 * Written by Bryce Summers on 12 - 20 - 2013.
 */

public interface PriorityQueue<E, T extends PriorityQueue<E, T>>
{
	/* 
	 * Elements can be added to the PQ associated with priorities.
	 * In theory, the priorities can be ordered in increasing or decreasing order. 
	 */
	public void add(E elem, int priority);
	public T add_static(E elem, int priority);
	
	/* Returns the element with the minimum priority,
	 * while also removing it from the PQ.
	 */
	public E delMin();
	
	// Returns the element with the minimum priority, while not removing it from the PQ.
	public E peekMin();

	
}
