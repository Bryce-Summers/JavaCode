package Data_Structures.Structures.HashingClasses;

import Data_Structures.Structures.HashTable;

/*
 * Recursive Set Class.
 * Written by Bryce Summers on 5 - 23 - 2013.
 * 
 * Purpose : Allows for the functional use of non multi sets.
 * 
 * 			1. Inclusion.
 * 			2. Union
 * 			3. Intersection.
 * 			4. Cardinality.
 * 			5. Equality.
 * 			6. Recursive sets.
 * 
 * 
 * 
 * FIXME : Implement set complement with regards to a given Universe.
 *			I could implement this by putting a flag in that states whether a set includes elements or excludes elements.
 *			I might need to maintains 2 sets, 1 for the inclusions, 1 for the exclusions.
 */

public class Set<E> extends HashTable<Pair<E, Set<E>>>
{
	// -- Local data.
	
	// P is typed to be a pair of the appropriate type.
	private class P extends Pair<E, Set<E>>
	{
		public P(E elem, Set<E> set)
		{
			super(elem, set);
		}
	}
	
	// -- Constructors.
	public Set()
	{
		super(1);
	}
	
	public Set(int intial_size)
	{
		super(intial_size);
	}
	
	public Set(E ... input)
	{
		super(input.length);
		
		for(E elem : input)
		{
			set_add(elem);
		}
	}
	
	// Addition and Subtraction (due to naming conflicts.)
	public void set_add(E elem)
	{
		insert(P(elem));
	}
	
	public void set_add(E ... elems)
	{
		for(E elem : elems)
		{
			insert(P(elem));
		}
	}
	
	// Add a clone to prevent infinitely recursive sets.
	public void set_add(Set<E> set)
	{
		insert(P(set.clone()));
	}
	
	// Removes the given element from the set.
	// This is a mutagen.
	// Returns true iff the given element was present in the set.
	public boolean rem(E elem)
	{
		return remove(P(elem));
	}
	
	public void rem(Set<E> set)
	{
		remove(P(set));
	}
	
	public boolean includes(E elem)
	{		
		return contains(P(elem));
	}
	
	public boolean includes(Set<E> set)
	{
		return contains(P(set));
	}
	
	public Set<E> union	(Set<E> other)
	{
		if(cardinality() == 0)
		{
			return other.clone();
		}
		
		if(other.cardinality() == 0)
		{
			return this.clone();
		}
		
		Set<E> output = (Set<E>) clone();
		
		for(Pair<E, Set<E>> elem : other)
		{
			output.insert(elem);
		}
		
		return output;
	}
	
	public Set<E> intersection (Set<E> other)
	{
		if(cardinality() == 0)
		{
			return this;
		}
		
		if(other.cardinality() == 0)
		{
			return other;
		}
		
		Set<E> output = new Set<E>();
		
		for(Pair<E, Set<E>> elem : other)
		{
			if(this.contains(elem))
			{
				output.insert(elem);
			}
		}
		
		return output;
	}
	
	public Set<E> Diference (Set<E> other)
	{
		if(cardinality() == 0 || other.cardinality() == 0)
		{
			return this;
		}
		
		Set<E> output = (Set<E>) clone();
		
		for(Pair<E, Set<E>> elem : other)
		{
			output.remove(elem);
		}
		
		return output;		
	}

	public int cardinality()
	{
		return size();
	}
	
	@Override
	public Set<E> clone()
	{
		Set<E> output = new Set<E>(table.length);
		
		for(Pair<E, Set<E>> elem : this)
		{
			output.insert(elem.clone());
		}
		
		return output;
	}

	// -- Internal Helper functions.
	
	private P P(E elem)
	{
		return new P(elem, null);
	}
	
	private P P (Set<E> set)
	{
		return new P(null, set);
	}

}
