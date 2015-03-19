package Data_Structures.ADTs.Sets;

/*
 * Recursive Abstract data type.
 * 
 * Written by Bryce Summers on 5 - 23 - 2014.
 */

public interface RecursiveSet<E> extends SimpleSet<E>
{
	// Add functionality to add RecursiveSets in addition to elements.
	public void add(RecursiveSet<E> set);
	public void remove(RecursiveSet<E> set);
	public void contains(RecursiveSet<E> set);
	
}
