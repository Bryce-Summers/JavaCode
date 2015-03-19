package Data_Structures.ADTs;

/*
 * Defines the interface for a Deque
 * 
 * writeen by Bryce SUmmers on 5 - 12 - 2014.
 */

public interface Deque<E>
{
	// Adds an element to the head of the structure.
	public void push_front(E elem);

	// Adds an element to the tail of the structure.
	public void push_back(E elem);
	
	// Removes an element from the front or back of the structure.
	public E pop_front();
	public E pop_back();
	
	// Returns the element at the front or back of the structure without removing it.
	public E peep_front();
	public E peep_back();
	
}
