package Data_Structures.ADTs.Sets;

/*
 * Written by Bryce Summers on 12 - 29 - 2013.
 * 
 * Abstract Data Type for Sets.
 * 
 * The bare minimum operations on a set.
 * 
 * Sets either contain or do not contain every possible element of type E.
 * 
 * update : 5 - 22 - 2014 at the beach.
 * - Renamed this class "Simple Set" to denote the limit capabilities specified by this structure.
 */

public interface SimpleSet<E>
{
	// We must be able to add elements to the Set.
	public void add(E elem);
	
	// Sets allow for inclusion to be queried.
	public boolean contains(E elem);
	
	// Sets allow for elements to be removed from the set.
	// Returns true if the element was removed from the set.
	public boolean remove(E elem);
}
