package Data_Structures.Structures.InDevelopment.Database;

import Data_Structures.ADTs.Queue;
import Data_Structures.Structures.Pair;
import Data_Structures.Structures.InDevelopment.Trees.Treap;

/*
 * Range Query Structure.
 * 
 * Written by Bryce Summers on 5 - 30 - 2014.
 * 
 * Purpose : This provides efficient query operations for elements within a given range.
 * 
 * This structure should be useful for an arbitrary number of dimensions.
 * 
 * RQ will make extensive use of the Bryce Treap class, due to its memory efficiency, time efficiency,
 * and the fact that it was perfectly designed to be used optimally in algorithmic structures such as Range Query trees.
 * 
 * I am currently only supporting dimensions of identical types.
 * 
 * FIXME : Do some thinking.
 */

public class RangeQueryStructure<E extends Comparable<E>>
{
	// The elements in the current dimension.
	final int dimensionality;
	Treap<E> myDimension;
	RangeQueryStructure<E> recursive_dimensions;
	
	// -- Constructors.
	public RangeQueryStructure(E ... collection)
	{
		dimensionality = 1;
		myDimension = new Treap<E>(collection);
	}

	// Recursively construct a RangeQueryStructure.
	public RangeQueryStructure(Queue<E[]> dimensions)
	{
		dimensionality = dimensions.size();
		
		Pair<E[], Queue<E[]>> deq = dimensions.deq_static();
		myDimension = new Treap<E>(deq.getKey());
		
		recursive_dimensions = new RangeQueryStructure<E>(deq.getVal());
	}
}
