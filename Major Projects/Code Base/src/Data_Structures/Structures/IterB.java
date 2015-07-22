package Data_Structures.Structures;

import java.util.Iterator;


/* The Bryce Iterator Interface.
 * 
 * Written by Bryce Summers on 7 - 28 - 2013.
 * 
 * Purpose : This class specifies powerful iterators that can move forward and backward and perform removals and insertions.
 * 
 * This will allow me to dynamically keep track of a location in objects such as lists.
 * 
 *	In lists, this allows me to create text boxes that support a pointer,
 *  with left and right movement capabilities and the ability to delete and insert elements inside of the list.
 *  
 *  
 *  NOTE : In other words, once a user has extracted an IterB, the IterB acts like a data structure that is perfect for Text Editing.
 *  
 *  // FIXME : Perhaps give it a better name.
 *  // FIXME : Implement the Data_structure signature?????
 */

public interface IterB<E> extends Iterator<E>
{

	// Returns whether there is another element after the one that this IterB is pointing to.
	@Override
	public abstract boolean hasNext();

	// Returns the next element, this behavior is only defined if called after hasNext() returns true.
	@Override
	public abstract E next();

	// Returns true if the iterator has a current node that it is pointing to.
	public abstract boolean hasCurrent();
	
	/** Returns the current node that this iterB is pointing to.
	 *  Should be called after a hasCurrent call.
	 *  Returns the last element that was returned, even if it has been removed.
	 *  Returns the replacement node, if it has been replaced.
	 */
	public abstract E current();
	
	// Returns whether there is an element in the structure before this one.
	public abstract boolean hasPrevious();
	
	// Returns the next element, this behavior is only defined if called after hasPrevious() returns true.
	public abstract E previous();

	// -- Mutation functions.
	
	// Removes that last element that was returned from the implementing structure.
	// This should not change the performance of the next of previous nodes that will be returned by this iterator.
	@Override
	public abstract void remove();
	
	
	// Replaces the last element that was returned from the implementing structure with the given element.
	public abstract void replace(E elem);
	
	/** Inserts an element after the last element that has been called.
	 *  The iterator will now point to this new element.
	 * 
	 * @param elem
	 */
	public abstract void insertAfter(E elem);
	
	/** Inserts an element before the last element that has been called.
	 * The iterator will now point to this new element.
	 * 
	 * @param elem
	 */
	public abstract void insertBefore(E elem);

	// Enables iterator objects to override the default clone function.
	public abstract Object clone();
	
	public abstract boolean equals(Object o);
	
}
