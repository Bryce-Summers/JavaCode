package Data_Structures.ADTs;

import java.util.Iterator;

import Data_Structures.ADTs.Sets.SimpleSet;
import Data_Structures.Structures.Data_Structure;
import Data_Structures.Structures.SingleLinkedList;

/*
 * Abstract Data Type Binary Search Trees.
 * 
 * Written by Bryce Summers, 12 - 19 - 2013.
 * 
 * Updated 12 - 22 - 2013 : Removed heap and priority Queue functionality.
 * 
 * Provides a universal specification for the capabilities
 * that Binary search tree structures need to provide.
 * 
 * Binary Search Trees are defined recursively.
 */


public abstract class BST<E extends Comparable<E>, T extends BST<E, T>> extends Data_Structure<E> implements SimpleSet<E>
{
	// -- Private Data.
	
	// Trees start being empty.
	protected int size = 0;
	
	// -- Public Interface.
	
	/*
	 * Adds the given element to the tree,
	 * creating a key from the element's hash code.
	 */ 
	
	public int size()
	{
		return size;
	}
	
	public boolean isEmpty()
	{
		return size == 0;
	}

	// BST's need to be able add elements.
	// O(log_2(n)) if balanced.
	public abstract void add(E elem);
	public abstract BST<E, T> add_static(E elem);
	
	// O(log_2(n)) if balanced.
	public abstract boolean remove(E elem);
	// Returns the BST without the input element, 
	// returns the original BST if this BST does not contain the given element.
	public abstract BST<E, T> remove_static(E elem);
	
	// Returns true iff this tree is a leaf node.
	public abstract boolean isLeaf();
	public abstract E getRoot();
	
	// O(log_2(n)) if balanced.
	@Override
	public abstract boolean contains(E elem);

	// O(1).
	protected abstract boolean hasLeftChild();
	protected abstract boolean hasRightChild();
	protected abstract BST<E, T> getLeftChild();
	protected abstract BST<E, T> getRightChild();
	
	// O(n).
	@Override
	public String toString()
	{
		// The left and right substrings.
		String sl, sr;
			
		// Compute the left substring.
		if(hasLeftChild())
		{
			sl =  getLeftChild().toString();
		}
		else
		{
			sl = "null";
		}
		
		// Compute the right substring.
		if(hasRightChild())
		{
			sr = getRightChild().toString();
		}
		else
		{	
			sr = "null";
		}
		
		// Compute the center substring.
		String sdata = getRoot().toString();
		
		return "N(" + sl + ", " + sdata + ", " + sr + ")";
	}

	@Override
	public abstract Data_Structure<E> clone();

	public Iterator<E> iterator()
	{
		return new Iter(this);
	}
	
	// FIXME : Put some thought into Tree traversals.
	// Implement different iteration orderings.

	// FIXME : Implement different ordering modes for iteration.
	// -- preorder, postorder, breadthfirst, leaves, path to root.
	
	// Currently searches left child, parent, child in increasing comparator order.
	private class Iter implements Iterator<E>
	{
		
		Stack<BST<E, T>> Unexplored;
		Stack<BST<E, T>> parents;
		
		// -- Constructor.
		private Iter(BST<E, T> start)
		{
			Unexplored = new SingleLinkedList<BST<E, T>>();
			Unexplored.push(start);
			
			parents = new SingleLinkedList<BST<E, T>>();
		}
		
		@Override
		public boolean hasNext()
		{
			return !(Unexplored.isEmpty() && parents.isEmpty());
		}

		@Override
		public E next()
		{
			if(!Unexplored.isEmpty())
			{
				BST<E, T> current = Unexplored.pop();
				current = bottom_left(current);
				
				tagRightChild(current);
				
				return current.getRoot();
			}
			
			BST<E, T> current = parents.pop();
			
			tagRightChild(current);
			
			return current.getRoot();
		}
		
		// Tags the right child of a tree for future exploration.
		private void tagRightChild(BST<E, T> current)
		{
			if(current.hasRightChild())
			{
				Unexplored.push(current.getRightChild());
			}
		}
		
		// Moves the iterator to the bottom left Node from the current location.
		// ENSURES : Returns an AVL tree that has no left child,
		//			 and the parents list should contain all of the parents of the node. 
		public BST<E, T> bottom_left(BST<E, T> current)
		{
			while(current.hasLeftChild())
			{
				parents.push(current);
				current = current.getLeftChild();
			}
			
			return current;
		}

		@Override
		public void remove()
		{
			throw new Error("Not Implemented");
		}
		
	}
	
}