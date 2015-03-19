package Data_Structures.Structures.InDevelopment.Trees;

import static util.testing.ASSERT;

import java.util.Random;
import java.util.Spliterator;
import java.util.function.Consumer;

import Data_Structures.ADTs.BST;
import Data_Structures.ADTs.Heap;
import Data_Structures.ADTs.MeldableHeap;
import Data_Structures.ADTs.Pairable;
import Data_Structures.ADTs.PriorityQueue;
import Data_Structures.Structures.Box;
import Data_Structures.Structures.Data_Structure;
import Data_Structures.Structures.UBA;

/*
 * Immutable Purely Functional implementation of Treaps.
 * Written by Bryce Summers on 12 - 29 - 2013.
 * Majorly implemented during the period between 5 - 19 - 2014 and 5 - 30 - 2014 at the beach, on the train, and in Omaha.
 * 
 * These will be used as primitive components in more complex structures such as Range querying structures,
 * and will be ideal Trees for many applications due to their immutability
 * and therefore fast modification times and memory efficiency.
 * 
 * Each node is either empty, or contains an element and non null left and right sub trees.
 * 
 * the invariants specified by this structure are not necessarily maintained if the elements are mutated externally.
 * 
 * Invariants :
 * 1. BST property on the compare() operation of type E.
 * 2. heap property, with higher parent nodes having higher priorities than their children. MAX HEAP. 
 * 			// FIXME : This should probably be turned into a min heap to fulfill the meldable heap specification.
 * 3. root is never null;
 * 4. Non empty nodes always have non null left and right nodes.
 * 
 * FIXME : Consider making the treaps themselves the nodes instead of maintaining a root and extra constraints.
 */


public class Treap<E extends Comparable<E>> extends BST<E, Treap<E>> implements MeldableHeap<E, Treap<E>>, Heap<E>,
		PriorityQueue<E, Treap<E>>
{
	// Priority Generator for Exclusive use by Treaps.
	static Random R = new Random();
	
	Node root;
	
	// -- Constructors.
	
	public Treap()
	{
		 root = EMPTY_NODE;
		 size = 0;
	}
	
	// Efficient bulk tree construction.
	public Treap(E ... data)
	{
		throw new Error("Implement Me Please!");
	}
	
	private Treap(Node n)
	{
		root = n;
	}
	
	// Override superclass size management system.
	@Override
	public int size()
	{
		return root.size;
	}
	
	public boolean isEmpty()
	{
		return root.isEmpty();
	}
	
	// -- Interface functions, call immutable nodes.
	// Returns an immutable tree representing this tree. Warning : This may perform useless work if the element is already in the treap.
	
	public Treap<E> add_static(E elem)
	{
		int priority = generatePriority();
		return add_static(elem, priority);
	}
	
	// Same as add_static(elem), except the priority is given instead of being random.
	// Utilizing this function may invalidate the asymptotic time complexity assumptions.
	// This can be used to treat the treap like a priority queue.
	public Treap<E> add_static(E elem, int priority)
	{
		priority = restrictRange(priority);
		
		Node result = root.add(elem, priority);
		
		return result == null ? this : new Treap<E>(result);
		
	}

	public Treap<E> static_append(E ... data)
	{
		Treap<E> other = new Treap<E>(data);
		return meld(other);
	}
	
	@Override
	public boolean remove(E elem)
	{
		throw new Error("Try \"static_remove\" instead");
	}
	
	// Static removal function. Returns an immutable Treap representing
	// this Treap with the given element not present.
	public Treap<E> remove_static(E elem)
	{
		Node temp = root.remove(elem);
		
		// -- Node found and removed.
		if(temp != null)
		{
			return new Treap<E>(temp);
		}
	
		// Node not found and not removed.
		return this;
	}

	@Override
	public boolean isLeaf()
	{
		return root.isLeaf();
	}

	@Override
	public E getRoot()
	{
		if(root.isEmpty())
		{
			throw new Error("Error : Tree is empty");
		}
		
		return root.data;
	}

	@Override
	public boolean contains(E elem)
	{
		return root.contains(elem);
	}

	@Override
	protected boolean hasLeftChild()
	{
		return root.left != null;
	}

	@Override
	protected boolean hasRightChild()
	{
		return root.right != null;
	}

	@Override
	protected Treap<E> getLeftChild()
	{
		if(root.isEmpty())
		{
			throw new Error("Error: Empty trees have no left child.");
		}
		
		return new Treap<E>(root.left);
	}

	@Override
	protected Treap<E> getRightChild()
	{
		if(root.isEmpty())
		{
			throw new Error("Error: Empty trees have no right child.");
		}
		
		return new Treap<E>(root.right);
	}

	// Since Treaps are immutable, this method is trivial.
	@Override
	public Data_Structure<E> clone()
	{
		return this;
	}

	// Generates a safe integer for use as a priority.
	private int generatePriority()
	{
		int val = R.nextInt();
		
		return restrictRange(val);
	}
	
	/* Restricts the range of the given val so as to leave special
	 * values available for my priority changing algorithms.
	 */
	private int restrictRange(int val)
	{
		switch(val)
		{
			case Integer.MIN_VALUE : return Integer.MIN_VALUE + 1;
			case Integer.MAX_VALUE : return Integer.MAX_VALUE - 1;
			default : return val;
		}
	}
	
	// Updates the priority of the given node.
	private Treap<E> updatePriority(E elem, int priority)
	{
		Treap<E> output = remove_static(elem);
		return output.add_static(elem, priority);
	}
	
	/* REQUIRES : The input middle element should be non null.
	 * ENSURES : Returns two Treaps, one with all elements less than the given element,
	 * 			 and one with all elements greater than the given element.
	 */
	@SuppressWarnings("unchecked")
	public Pairable<Treap<E>> split(E mid)
	{
		// Rotate the mid node to the root of the tree through a priority change.
		Treap<E> output = updatePriority(mid, Integer.MAX_VALUE);
		
		// Extract the tree of lesser nodes and the tree of greater nodes.
		UBA<Treap<E>> treaps = new UBA<Treap<E>>(2);
		treaps.append(new Treap<E>(output.root.left), new Treap<E>(output.root.right));
		
		return treaps;
		
	}
	
	
	// Naive merging algorithm, could probably be implemented more efficiently.
	// O(n*log(n)), where n is the minimum size of this or other.
	// FIXME : Optimize this function.
	@Override
	public Treap<E> meld(Treap<E> other)
	{
		boolean big = size() > other.size();
		Treap<E> bigger  =  big ? this : other;
		Treap<E> smaller = !big ? this : other;
		
		for(E elem : smaller)
		{
			bigger = bigger.add_static(elem);
		}
		
		return bigger;
	}
	
	// REQUIRES: all elements in left should be strictly less than the elements in right.
	// 			 mid should not be less than any element in left.
	//			 mid should be less than any element in right
	// ENSURES : Undoes a split, removes the midpoint, 
	// FIXME : Decide whether the value should be retained or not.
	// Merges the two trees with the mid element, then removes the splitting element.
	public Treap<E> unsplit(Treap<E> left, E mid, Treap<E> right)
	{
		Node output = new Node(mid, Integer.MAX_VALUE, left.root, right.root);
		return new Treap<E>(output).remove_static(mid);
	}
	
	// Function like the unsplit function, except it retains the mid element and set it to the input priority.
	public Treap<E> unsplit_and_insert(Treap<E> left, E mid, Treap<E> right, int priority)
	{
		Node output = new Node(mid, Integer.MAX_VALUE, left.root, right.root);
		return new Treap<E>(output).updatePriority(mid, priority);
	}
	
	// Same as unsplit_and_insert, but generates the mid node's priority randomly.
	public Treap<E> unsplit_and_insert(Treap<E> left, E mid, Treap<E> right)
	{
		Node output = new Node(mid, Integer.MAX_VALUE, left.root, right.root);
		return new Treap<E>(output).updatePriority(mid, generatePriority());
	}
	
	// Range querying functions.
	
	// Returns a Treap containing all elements within the given bounds, as specified by the compare() operator. Inclusive.
	// (left, right]
	// Implemented via 2 splits.
	public Treap<E> range(E left_bound, E right_bound)
	{
		return split(right_bound).getFirst().split(left_bound).getLast();
	}
	
	// Returns a Treap containing all elements less than or equal to the given right bound.
	// This function can be used in computational geometry problems.
	public Treap<E> left_range(E right_bound)
	{
		return split(right_bound).getFirst();
	}	
	
	
	/* Immutable nodes carry all of the data.
	 * 
	 * All nodes are either EMPTY, or they contain data,
	 * and non null left and right sub trees.
	 * 
	 * FIXME : If this class were made public, then we could make a static EMPTY_NODE, but 1 empty node per tree is still fine. 
	 */

	Node EMPTY_NODE = new Node();
	
	private class Node
	{		
		// Static empty node.
		
		// final Private data;
		// We make all of these fields private so that immutability is ensured syntactically,
		// and the size variable is guaranteed to be accurate.
		final Node left;
		final Node right;
		
		final E data;
		
		// Randomized Priorities.
		final int priority;
		
		final int size;
		
		// This should be commented out once we know for sure that Treaps are always treaps.
		// This is used to speed up the treap invariant check from O(n^2) to O(n).
		boolean isTreap = false;

		// -- Immutability Type constructors.
		
		// Empty node, should only be called to make EMPTY_NODE.
		public Node()
		{
			left  = null;
			right = null;
			data  = null;
			size = 0;
			priority = 0;
		}
		
		// Leaf node constructor.
		private Node(E elem, int prior)
		{
			data  = elem;
			left  = EMPTY_NODE;
			right = EMPTY_NODE;
			priority = prior;
			size = 1;
		}
		
		// Non Leaf Node.
		private Node(E elem, int prior, Node left_in, Node right_in)
		{
			data  = elem;
			left  = left_in;
			right = right_in;
			priority = prior;
			size = left.size + 1 + right.size;
		}

		// Cloning function. Pretty simple, since these nodes are immutable.
		@Override
		public Node clone()
		{
			return this;
		}
		
		// One of the two states of a node.
		public boolean isEmpty()
		{
			return data == null;
		}

		public boolean isLeaf()
		{
			if(!isEmpty() && left.isEmpty() && right.isEmpty())
			{
				return true;
			}
			
			return false;
		}
		
		/* Sometimes Bryce likes insert more than add...
		public Node insert(E elem, int priority_new)
		{
			return add(elem, priority_new);
		}
		*/
		
		// REQUIRES : An element and a priority.
		// ENSURES : Returns a treap containing all of the original elements and priorities along with the input element and priority.
		// If the input node is already contained within the treap, then this function returns null.
		public Node add(E elem, int priority_new)
		{

			if(isEmpty())
			{
				return new Node(elem, priority_new);
			}
			
			int compare = elem.compareTo(data);

			// Equality results in a key update.
			if(compare == 0)
			{
				return null;
			}
			
			Node output, left_new, right_new;
			
			// Element is less than this nodes' data.
			if(compare < 0)
			{
				left_new = left.add(elem, priority_new); 

				if(left_new == null)
				{
					return null;
				}
				
				// Heap property holds.
				if(left_new.isEmpty() || left_new.priority <= this.priority)
				{
					output = new Node(data, priority, left_new, right);
					return output;
				}
				
				// Need to tree rotate if the heap property is not held.

				// FIXME : I may be cloning excessively.
				Node output_right = new Node(data, priority, left_new.right, right);
				output 			  = new Node(left_new.data, left_new.priority, left_new.left, output_right);
							
				return output;

			}
			
			// -- Element is greater than this nodes' data.
			right_new = right.add(elem, priority_new); 

			if(right_new == null)
			{
				return null;
			}
			
			// Heap property holds.
			if(right_new.isEmpty() || right_new.priority <= this.priority)
			{
				output = new Node(data, priority, left, right_new);
				return output;
			}
			
			// Need to tree rotate if the heap property is not held.

			// FIXME : I may be cloning excessively.
			
			Node output_left = new Node(data, priority, left, right_new.left);
			output 			 = new Node(right_new.data, right_new.priority, output_left, right_new.right);
			
			return output;

		}
		
		/* Brilliant node removal from the suggestion on Wikipedia.
		 * REQUIRES : Removes an element that is identical to the input elem.
		 * ENSURES : Returns a node representing a new tree that is equivalent to the old tree with the given node removed.
		 * ENSURES : Returns null if the given node is not found.
		 * See node deletion : http://en.wikipedia.org/wiki/Treap
		 */
		public Node remove(E elem)
		{
			// Node not found.
			if(isEmpty())				
			{
				return null;
			}
			
			// Element found. Impossible to return null in this case!
			if(data.equals(elem))
			{
				// Simply delete leafs.
				if(isLeaf())
				{
					return EMPTY_NODE;
				}
				
				// Contract the tree if it is merely a path.
				if(left.isEmpty())
				{
					return right;
				}
				
				// Simetric path case.
				if(right.isEmpty())
				{
					return left;
				}
				
				
				// -- Handle 2 non leaf children.
				
				// Note: the equality preference is to swap shift treap nodes left.
				if(left.priority > right.priority)
				{
					// -- Immmutable Rotate right.
					
					Node output_right = new Node(data, priority, left.right, right);
					// Recursion down the right Treap.
					output_right = output_right.remove(elem);
					Node output = new Node(left.data, left.priority, left.left, output_right);
				
					
					return output;
				}
				else
				{
					// Immutable Rotate right.
					Node output_left = new Node(data, priority, left, right.left);
					// Recursion down the left Treap.
					output_left = output_left.remove(elem);
					Node output = new Node(right.data, right.priority, output_left, right.right);
					
					return output;					
				}
			}
			
			// If the node is not found, then recursively pass the operation into the
			// correct side of the tree according to the binary search invariant.
			int compare = elem.compareTo(data);
			Node output = null;
			
			if(compare < 0)
			{
				Node left_new = left.remove(elem);
				
				// only malloc if the elem nod was found and removed,
				// otherwise pass the null up the stack without mallocing.
				if(left_new != null)
				{
					output = new Node(data, priority, left_new, right);
				}
			}
			else
			{
				Node right_new = right.remove(elem);
				
				if(right_new != null)
				{
					output = new Node(data, priority, left, right_new);
				}
			}

			// Will still be null if the recursive call is null.
			return output;
		}

		// Returns true iff this Treap Node contains the given input element by the equals() function.
		public boolean contains(E elem)
		{
			if(isEmpty())
			{
				return false;
			}
			
			if(data.equals(elem))
			{
				return true;
			}
			
			Node child = elem.compareTo(data) < 0 ? left : right;
			
			if(child == null)
			{
				return false;
			}
			
			return child.contains(elem);
		}
		
		/*
		// REQUIRES : The given element 'elem' must be comparable.
		// Returns two disjoint treaps whose union is the original treap,
		// and the first treap contains all elements less than or equal to 'elem' 
		// and the right treap contains all elements greater than 'elem'.
		public Pairable<Node> split(E elem)
		{
			Node output = insert(elem, Integer.MAX_VALUE);
			
			UBA<Node> output_pair = new UBA<Node>(2);
			output_pair.set(0, output.left);
			output_pair.set(1, output.right);
			
			return output_pair; 
		}
		*/
		
		// String function for testing purposes.
		public String toString()
		{
			if(isEmpty())
			{
				return "EMPTY";
			}
			
			return "Node(" + left + ", " + data + ", Size = " + size + ", priority = " + priority + ", " + right + ")";
		}
		
		// O(n^2), but O(1) for each individual call for elements in the same treap due to dynamic programming.
		public boolean isTreap()
		{
			if(isLeaf() || isEmpty())
			{
				return true;
			}
			
			if(isTreap)
			{
				return true;
			}
			
			// Precise error highlighting assertions.
			ASSERT(left.isTreap());
			ASSERT(right.isTreap());
			ASSERT(left.data  == null || data.compareTo(left.data) >= 0);
			ASSERT(right.data == null || data.compareTo(right.data) <= 0);
			ASSERT(left.isEmpty()  || priority >= left.priority);
			ASSERT(right.isEmpty() || priority >= right.priority);
			ASSERT(size == left.size + 1 + right.size);
			
			isTreap =	left.isTreap()  &&
						right.isTreap() &&
						(left.isEmpty()  || data.compareTo(left.data) >= 0) &&
						(right.isEmpty() || data.compareTo(right.data) < 0) &&
						(left.isEmpty()  || priority >= left.priority)  &&
						(right.isEmpty() || priority >= right.priority) &&
						size == left.size + 1 + right.size;
			return isTreap;
		}
		
	}// End of Node class.

	@Override
	public String toString()
	{
		return root.toString();
	}
	
	// -- Meldable heap specification.
	
	public Treap<E> deleteMin(Box<E> output)
	{
		output.val = peekMin();
		return deleteMin();
	}
		
	// FIXME : We need to change this implementation from a max heap to a min heap.
	@Override
	public E peekMin()
	{
		throw new Error("Not Yet Implemented");
	}
	
	public Treap<E> deleteMin()
	{
		throw new Error("Not Yet Implemented");
	}

	
	// Required mutation non implemented methods.
	@Override
	public void add(E elem, int priority)
	{
		throw new Error("Treaps are immutable, so please use add_static() instead.");
	}

	@Override
	public void add(E elem)
	{
		throw new Error("Treaps are immutable, so please use add_static() instead.");
	}
	
	@Override
	public void append(E ... elem)
	{
		throw new Error("Treaps are immutable, please use append_static() instead");
	}
	
	// O(n) is treap check facilitated by dynamic programming stored boolean values.
	public boolean isTreap()
	{
		return root.isTreap();
	}

	@Override
	public Treap<E> insert(E item)
	{
		return add_static(item);
	}

	@Override
	public E delMin()
	{
		throw new Error("Not Yet Implemented");
	}

}
