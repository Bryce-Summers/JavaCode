package Data_Structures.Structures.InDevelopment.Heaps;

import Data_Structures.ADTs.MeldableHeap;
import Data_Structures.ADTs.Queue;
import Data_Structures.Structures.Box;
import Data_Structures.Structures.List;
import Data_Structures.Structures.SingleLinkedList;

/*
 * Leftist Heap implementations.
 * 
 * Will be written by Bryce Summers. 5 - 23 - 2014.
 * 
 * Tree Node based.
 * 
 * This implementation is based on the descriptions in Data Structures and Network Algorithms by Robert Tarjan.
 * 
 * This implementation is Persistent in that the fields of any given leftist heap node will never change.
 * My Leftist trees can never be empty. if a function would return an empty tree, then it returns null.
 * This implementation has no notion of the size of the heap.
 * The branching factor for leftist trees is inherently 2.
 * 
 * FIXME : Implement Tombstones to allow for the arbitrary deletion of elements.
 * FIXME : Test this code.
 */

public class LeftistHeap<E extends Comparable<E>> implements MeldableHeap<E, LeftistHeap<E>>
{

	// The rank of a leftist heap is the minimum distance to an external node.
	final int rank;
	final E data;
		
	final LeftistHeap<E> left;
	final LeftistHeap<E> right;
	
	// Make a singleton heap.
	public LeftistHeap(E elem)
	{
		rank  = 1;
		data  = elem;
		left  = null;
		right = null;
	}
	
	// Construct a new Leftist heap node.
	private LeftistHeap(E elem, LeftistHeap<E> left, LeftistHeap<E> right)
	{
		rank = right.rank + 1;
		data = elem;
		this.left  = left;
		this.right = right;
	}
	
	@SafeVarargs
	public static <E extends Comparable<E>> LeftistHeap<E> makeHeap(E... elems)
	{
		 Queue<LeftistHeap<E>> Q  = new SingleLinkedList<LeftistHeap<E>>();
		 
		 for(E elem : elems)
		 {
			 Q.enq(new LeftistHeap<E>(elem));
		 }
		 
		 // A beautiful balanced binary merge.
		 while(Q.size() > 1)
		 {
			 Q.enq(meld(Q.deq(), Q.deq()));
		 }
		 
		 LeftistHeap<E> output = Q.deq();
		 return output;
	}

	E findMin()
	{
		return data;
	}
	
	@Override
	public LeftistHeap<E> meld(LeftistHeap<E> other)
	{
		return meld(this, other);
	}
		
	// meld takes two heaps and returns a new heap containing the elements 
	// of both heaps that meets the leftist heap invariants.
	// Efficient special case null checks.
	public static <E extends Comparable<E>> LeftistHeap<E> meld(LeftistHeap<E> h1, LeftistHeap<E> h2)
	{
		if(h2 == null)
		{
			return h1;
		}
		
		if(h1 == null)
		{
			return h2;
		}
		
		return mesh(h1, h2);
	}
	
	// REQUIRES : h1 != null and h2 != null.
	private static <E extends Comparable<E>> LeftistHeap<E> mesh(LeftistHeap<E> h1, LeftistHeap<E> h2)
	{
		// Ensure that h1 has the minimum key.
		if(h1.data.compareTo(h2.data) > 0)
		{
			LeftistHeap<E> temp = h1;
			h1 = h2;
			h2 = temp;
		}
		
		LeftistHeap<E> right = h1.right == null
						     ? h2
							 : mesh(h1.right, h2);
		
		LeftistHeap<E> left;
		
		if(h1.left.rank < right.rank)
		{
			left  = right;
			right = h1.left;
		}
		else
		{
			left = h1.left;
		}
				
		return new LeftistHeap<E>(h1.data, left, right);
	}
	
	public LeftistHeap<E> insert(E item)
	{
		LeftistHeap<E> node = new LeftistHeap<E>(item); 
		return mesh(this, node);
	}
	
	// REQUIRES : a reference box to put the outputed minimum element into.
	// ENSURES : returns a heap that contains all of the elements except the minimum element.
	public LeftistHeap<E> deleteMin(Box<E> output)
	{
		output.val = data;
		return deleteMin();
	}
	
	public LeftistHeap<E> deleteMin()
	{
		return meld(left, right);
	}
	
	@Override
	public E peekMin()
	{
		return data;
	}
	
	// Returns a list containing all elements less than or equal to the upper bound.
	public List<E> listMin(E upper_bound)
	{
		List<E> output = new List<E>();
		listMin(this, upper_bound, output);
		return output;
	}
	
	// ENSURES : Adds any and all found elements that are <= the upper_bound into the output.
	// Runs in O(|output|).
	private static <E extends Comparable<E>> void listMin(LeftistHeap<E> heap, E upper_bound, List<E> output)
	{
		if(heap == null || heap.data.compareTo(upper_bound) > 0)
		{
			return;
		}
		
		output.add(heap.data);
		listMin(heap.left, upper_bound, output);
		listMin(heap.right, upper_bound, output);
		
	}

	
}
