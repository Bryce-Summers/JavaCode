package Data_Structures.Structures.InDevelopment.Heaps;

import java.util.Iterator;

import Data_Structures.ADTs.Heap;

import Data_Structures.Structures.Data_Structure;
import Data_Structures.Structures.UBA;

/*
 * Implements a standard heap implementation of Heaps using arrays.
 * 
 * Should be written by Bryce Summers on 5 - 23 - 2014.
 * 
 * Note : This is a MIN heap.
 * 
 * Heap property :	For every element in the tree,
 *  				it is less than or equal to its left and right children in the tree as defined by the elements at the indexes associated by
 *  				the relationships index_left(index) and index_right(index).
 *  
 * Root node is always at index 0.
 * 
 * Left biased, when equal keys are present, the one on the left will be chosen.
 * 
 * Allows for duplicate keys.
 * 
 * Binary tree invariants :
 * The heap is represented by a binary tree that is encoded by index relationships within an unbounded array.
 * We maintain the UBA with a minimality of nodes, so the UBA will only contain N elements, when size = n.
 * 
 * The heap is as balanced as possible. This causes their to be a preference for left children over right children.
 * 
 * FIXME : I will need to work to preserve key stability, so that all keys will eventually be deleted, even if all keys entered are equal.
 */

public class ArrayHeap<E extends Comparable<E>> extends Data_Structure<E> implements Heap<E>
{
	// -- Private Data.
	UBA<E> data;
	
	// This number may be changed to optimize performance.
	// It controls what the branching factor of the tree is.
	final static int D = 3;

	public ArrayHeap()
	{
		data = new UBA<E>();
	}
	
	public ArrayHeap(int initial_size)
	{
		data = new UBA<E>(initial_size);
	}
	
	public ArrayHeap(UBA<E> data_in)
	{
		data = data_in.clone();
		heapify();
	}

	// -- Public interface functions.
	
	public int size()
	{
		return data.size();
	}
	
	public boolean isEmpty()
	{
		return data.isEmpty();
	}
	
	public void add(E elem)
	{
		int len = data.size();
		data.add(elem);
		sift_up(len);
	}
	
	public E peek_dominating()
	{
		return data.get(0);
	}
	
	// O(log(n)) deletes and returns the minimum element.
	public E extract_dominating()
	{
		// Trivial 1 element heap.
		if(data.size() == 1)
		{
			return data.rem();
		}
		
		// Extract the minimum element.
		E output = data.get(0);
				
		// Maintain the minimum binary tree invariants.
		E last = data.rem();
		data.set(0, last);
		sift_down(0);
		
		return output;
	}
	
	// -- Data_structure interface functions.
	public UBA<E> toUBA()
	{
		return data.clone();		
	}
	
	// -- Private functions.
	
	// Heapifies all of the nodes of the Tree with a root at the given index.
	// Builds the heap invariant downwards to all sub trees.
	// O(n), checks each node in the tree once.
	// Transforms a random array into an array that meets the heap invariants.
	private void heapify()
	{
		for(int i = data.size() - 1; i >= 0; i--)
		{
			sift_down(i);
		}

	}
	
	// Given an index, swaps the node down the tree while maintaining the min
	// heap invariant until the node is in an invariant correct place.
	// O(log(n)). Non recursive, so has O(1) function calls.
	// SIFT down.
	private void sift_down(int index)
	{
		int size   = data.size();

		int child_index = index_child(index, 1);
				
		E elem = data.get(index);
		
		// While the node has at least 1 child.
		while(child_index < size)
		{
						
			int min_elem_index = -1;
			E min_elem = elem;
			
			// ASSUMES that Children are contiguous in memory.
			for(int i = child_index; i < child_index + D && i < size; i++)
			{
				E child = data.get(i);
				
				// If child is lesser.
				if(child.compareTo(min_elem) < 0)
				{					
					min_elem = child;
					min_elem_index = i;
				}
			}
			
			// The heap invariants are held and no further swaps need to be made.
			// Reference comparison.
			if(min_elem == elem)
			{
				return;
			}
			
			min_first(index, min_elem_index);
			index = min_elem_index;
			child_index = index_child(index, 1);
						
		}// End of while loop.
		
	}
	
	// Builds the heap invariant going up the tree from a given child node.
	private void sift_up(int index)
	{
		int parent_index = index_parent(index); 
		
		// Root node is always at index 0.
		while (index > 0 && min_first(parent_index, index))
		{
			index = parent_index;
			parent_index = index_parent(index);
		}
		
	}
	
	// -- Array tree transversing functions.
	
	private int index_parent(int index)
	{
		return (index-1)/D;
	}

	// REQUIRES: The index of a given node, which child is desired. Child in [1, D]
	// ENSURES: Returns the child_index'th child of the node at the given index in the array.
	private int index_child(int index, int child_index)
	{
		return D*index + child_index;
	}

	// REQUIRES : index1 < index2.
	// Performs a swap to fix heap invariant errors for the elements at the given indices.
	// Returns true iff the swap was performed.
	private boolean min_first(int index1, int index2)
	{
		E elem1 = data.get(index1);
		E elem2 = data.get(index2);
		
		if(elem1.compareTo(elem2) > 0)
		{
			data.swap(index1, index2);
			return true;
		}
		
		return false;
	}

	@Override
	public Iterator<E> iterator()
	{
		return data.iterator();
	}

	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		output.append("\nMinHeap[");
		
		for(E elem : this)
		{
			output.append(elem);
			output.append(",\n");
		}
		
		output.append("]");
		
		return output.toString();
	}

	@Override
	public ArrayHeap<E> clone()
	{
		return new ArrayHeap<E>(data);
	}
	
	// Returns a UBA that is sorted from least to greatest.
	public UBA<E> toSortedUBA()
	{
		int len = data.size();
		UBA<E> output = new UBA<E>(len);
		
		ArrayHeap<E> heap = clone();
		
		for(int i = 0; i < len; i++)
		{
			output.set(i, heap.extract_dominating());
		}
		
		return output;
	}

	
}