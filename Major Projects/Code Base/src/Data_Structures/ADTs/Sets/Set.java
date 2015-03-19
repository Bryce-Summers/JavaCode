package Data_Structures.ADTs.Sets;

import java.util.Iterator;


/*
 * Written by Bryce Summers on 12 - 29 - 2013.
 * 
 * Abstract Data Type for Sets.
 *
 * 
 * This type specification is designed to represent fully featured Set data structures.
 * 
 * Possible implementations : Hash tables, Binary Search Trees.
 * 
 * Terms that may be used to describe different implementations of sets:
 * 
 * 1. Frozen / static : The set is immutable and only allows queries.
 * 2. dynamic / mutable : allow for insertions and deletions to mutate the structure.
 * 
 */

public interface Set<E> extends SimpleSet<E>, Iterable<E>
{
	// --  Simple set minnimum operations.
	
	// We must be able to add elements to the Set.
	public void add(E elem);
	
	// Sets allow for inclusion to be queried.
	public boolean contains(E elem);
	
	// Sets allow for elements to be removed from the set.
	// Returns true if the element was removed from the set.
	public boolean remove(E elem);
	
	// -- Set Algebra operations.
	public Set<E> intersect(Set<E> other);
	public Set<E> union(Set<E> other);
	public Set<E> difference(Set<E> other);
	public boolean subset(Set<E> subset);
	
	// Returns the size of the set.
	public int cardinality();
	
	// Returns true iff this set is the null set.
	public boolean isEmpty();
	
	
	// Extra methods that might need to be removed.
	public Iterator<E> iterator();
	
	// Maybe should be a list.
	// Returns a list of elements in the set.
	public E[] enumerate();
	
	// returns a new set form the given elements.
	public Set<E> build(E ... input);
	public Set<E> build(Iterable<E> input);
	
	
	// Predicate functions.
	
	// map(f(E) -> y);
	// filter.
	// fold.
	// Sum, nearest, min, max.
	
	public void clear();
	
	public boolean equals(Set<E> other);
	
	@Override
	public int hashCode();
	
	
}
