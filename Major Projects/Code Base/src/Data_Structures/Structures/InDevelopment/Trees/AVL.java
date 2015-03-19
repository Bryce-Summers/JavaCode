package Data_Structures.Structures.InDevelopment.Trees;

import Data_Structures.ADTs.BST;


/*
 * AVL Binary Search Tree.
 * Rewritten by Bryce Summers: 12 - 20 - 2013.
 * Initial draft finished on 12 - 29 - 2013.
 * 
 * Purpose: allows for the efficient storing of comparable elements in a searchable. tree that maintains the BST property.
 * 
 * WARNING : This is an imperative data structure, it allow mutations and is not parallel safe.
 * 
 * BST property :	Every Node in a BST contains an element that is greater than the element in its 
 * 					left subtree according to the comparison function specified on the elements.
 * 					Every Node's element is less than its right subtree.
 * 
 * Maintains a collection of Element storing nodes in the for of a rooted tree.
 * Uses rotations to keep the tree approximately balanced.
 * i.e. the height of the left sub tree and the right sub tree will never differ by more than 1.
 * 
 * 
 * Assumes that a.compareTo(b) --> a.equals(b) == true.
 */

public class AVL<E extends Comparable<E>> extends BST<E, AVL<E>>
{
	
	/* Internal Data Representation.
	 * 
	 * INVARIANT :	The height of the left and right subtree's should differ by at most 1.
	 * 				height = max(left.height, right.height) + 1.
	 * 				An empty tree has a height of 0. A singleton tree has a height of 1.
	 */
	private AVL<E> left; 
	private E data;
	private AVL<E> right;

	private int height;
	
	public AVL()
	{
		size   = 0;
		left   = null;
		right  = null;
		height = 0;
	}

	@Override
	public boolean isLeaf()
	{
		return left == null && right == null;
	}

	@Override
	public E getRoot()
	{
		return data;
	}

	@Override
	protected boolean hasLeftChild()
	{
		return left != null;
	}

	@Override
	protected boolean hasRightChild()
	{
		return right != null;
	}

	@Override
	protected AVL<E> getLeftChild()
	{
		return left;
	}

	@Override
	protected AVL<E> getRightChild()
	{
		return right;
	}

	// -- Public Operation Functions.

	@Override
	public void add(E elem)
	{
		// Base Case.
		if(isEmpty())
		{
			data = elem;
			
			size++;
			height = 1;
			
			return;
		}
		
		add_helper(elem);
	}
	

	private void add_helper(E elem)
	{		
		// -- Binary Sorting.
		
		int compare = elem.compareTo(data);
		
		// Equality implies that the element is already in this binary Tree.
		if(compare == 0)
		{
			return;
		}
		
		// Element is Less Than this one.
		if(compare < 0)
		{
			if(left == null)
			{
				left = N(elem);
			}
			else
			{
				left.add_helper(elem);
			}
		}
		// Element is greater than this one.
		else
		{
			if(right == null)
			{
				right = N(elem);
			}
			else
			{
				right.add_helper(elem);
			}
		}
		
		// Update meta data.
		size++;

		// -- Maintain the Height Invariant.
		int hl = left  != null ? left.height  : 0;
		int hr = right != null ? right.height : 0;
		
		height = 1 + Math.max(hl, hr);
		
		balance();
	}

	/* 
	 * Recursively perform a Binary Search for the given element, utilizing the BST property.
	 * O(log_2(n)), because this tree's balance is maintained.
	 */
	@Override
	public boolean contains(E elem)
	{
		if(isEmpty())
		{
			return false;
		}
		
		return contains_helper(elem);
	}
	
	private boolean contains_helper(E elem)
	{
		if(data.equals(elem))
		{
			return true;
		}
		
		AVL<E> child = elem.compareTo(data) < 0 ? left : right;
		
		if(child == null)
		{
			return false;
		}
		
		return child.contains_helper(elem);
	}
	
		
	// O(log_2(n)) removal of comparable elements.
	@Override
	public boolean remove(E elem)
	{
		// Do not remove from empty trees.
		if(isEmpty())
		{
			return false;
		}
		
		return remove_search(elem, null);
	}
	
	/* Search from the node with the given element.
	 * and return true iff the element has been found and deleted.
	 */
	public boolean remove_search(E elem, AVL<E> parent)
	{
		// Replace this data with the leftmost successor.
		if(data.equals(elem))
		{
			remove_remove(parent);
			return true;
		}
		
		AVL<E> child = elem.compareTo(data) < 0 ? left : right;
		
		if(child == null)
		{
			return false;
		}
		
		return child.remove_search(elem, this);
	}
	
	// REQUIRES : parent is never null.
	private void remove_remove(AVL<E> parent)
	{
		// Trivial Leaf node.
		if(left == null && right == null)
		{
			if(parent != null)
			{
				if(parent.left == this)
				{
					parent.left = null;
				}
				else
				{
					parent.right = null;
				}
			}
			
			data = null;
			size = 0;
			return; // No balancing needed for empty trees.
		}
		
		// Handle 1 child cases.
		// ASSERT(left != null).
		if(right == null)
		{
			// Copy all of the properties of the left child onto this node.
			// The left node will then be garbage collected.
			if(left == null)
			{
				throw new Error("This should be impossible");
			}
			
			copy_from(left);
			return;// Left tree is already balanced.
		}
		
		if(left == null)
		{
			copy_from(right);
			return;
		}
		
		// Handle rotational 2 child cases.
		if(right.left == null)
		{
			AVL<E> l = left;
			
			copy_from(right);
			left = l;
			
			updateHeight();
			balance();
			return;
		}
		
		data = right.left.remove_leftmost(right);
		
		updateHeight();
		balance();
		
		return;
	}
	
	// Removes the leftmost element from this tree, given this tree's parent.
	// REQUIRES : parent != NULL.
	private E remove_leftmost(AVL<E> parent)
	{
		// Base Case.
		if(left == null)
		{
			parent.left = right;
			parent.updateHeight();
			return data;
		}
		
		// Recursion.
		E output = left.remove_leftmost(this);
		size--;
		
		balance();
		
		return output;		

	}

	// -- Private Helper functions.

	
	// Destructively copy all of the attributes of another AVL tree onto this one.
	private void copy_from(AVL<E> other)
	{
		data = other.data;
		size = other.size;
		right= other.right;
		left = other.left;
		height = other.height;
	}
	
	
	// Takes a tree that has gone through at most 1 insertion or deletion
	// and restores the balance invariant.
	private void balance()
	{
		int balance = balanceFactor();
		
		// Finished, if the tree is balanced.
		if(Math.abs(balance) < 2)
		{
			return;
		}

		// -- Now Balance the tree.
		// -- Balancing algorithmic instructions from Wikipedia/AVL Trees.
		
		// Unbalanced left.
		if(balance > 0)
		{
			int left_bal = left != null ? left.balanceFactor() : 0;
			
			// Left - Right case.
			if (left_bal < 0)
			{
				left.rotateLeft();
			}
		 
			// Left - Left case.
			rotateRight();
		}
		else // Unbalanced Right.
		{
			int right_bal = right != null ? right.balanceFactor() : 0;
			
			// Right - Left case.
			if (right_bal > 0)
			{
				right.rotateRight();
			}
			
			// Right - Right case.
			rotateLeft();
		}
	}
	
	// Returns the difference of the right subtree's height and the left subtree's height.
	private int balanceFactor()
	{
		int hl = left  != null ? left.height  : 0;
		int hr = right != null ? right.height : 0;

		return hl - hr; 
	}
	
	private void updateHeight()
	{
		int h1 = left  != null ? left.height  : 0;
		int h2 = right != null ? right.height : 0;
		
		height = 1 + Math.max(h1, h2);
	}
	
	// Restores the proper size of this AVL tree.
	// REQUIRES : The sizes of the child trees must be correct.
	// 			  This tree should have null data if it is empty.
	private void updateSize()
	{
		int s1 = left  != null ? left.size  : 0;
		int s2 = right != null ? right.size : 0;
		
		int mysize = data != null ? 1 : 0;
		
		size = mysize + s1 + s2;
	}
	
	// Creates a new singleton AVL Tree.
	private AVL<E> N(E elem)
	{		
		AVL<E> node = new AVL<E>();
		node.data = elem;
		node.size = 1;
		node.height = 1;
		
		return node;
	}
	
	/* 
	 * Performs a silent imperative right rotation through mutations.
	 * Does not disturb pointers pointing to this tree.
	 */
	private void rotateRight()
	{
		
		if(left == null)
		{
			throw new Error("Error : Cannot perform right rotations when no left subtree exists.");			
		}
		
		// First store all relevant information in local variables.
		AVL<E> s_left, s_leftl, s_leftr, s_right;
		
		s_left  = this.left;
		s_right = this.right;
		
		boolean b = left == null;
		
		s_leftl = b ? null : s_left.left;
		s_leftr = b ? null : s_left.right;
		
		E s_elem = data;
		E s_left_elem = left.data;
		
		/* 
		 * Now perform the rotation through mutations.
		 * Warning : Not Thread Safe.
		 */
		
		/* 
		 * Exchange the states of this node and its left node
		 * to preserve the integrity of this node's parent's pointers.
		 * While performing a right rotation.
		 */
		data  = s_left_elem;
		left  = s_leftl;
		right = s_left;
		
		s_left.data = s_elem;
		s_left.left  = s_leftr;
		s_left.right = s_right;
		
		// Correct the height metadata.
		//left .updateHeight();
		right.updateHeight();
		this .updateHeight();
		
		// Correct the size metadata.
		right.updateSize();
		this.updateSize();
	}

	/* 
	 * Performs a silent imperative left rotation through mutations.
	 * Does not disturb pointers pointing to this tree.
	 */
	private void rotateLeft()
	{
		if(right == null)
		{
			throw new Error("Error : Cannot perform left rotations when no left subtree exists.");			
		}
		
		// First store all relevant information in local variables.
		AVL<E> s_right, s_rightl, s_rightr, s_left;
		
		s_left  = this.left;
		s_right = this.right;
		
		boolean b = right == null;
		
		s_rightl = b ? null : s_right.left;
		s_rightr = b ? null : s_right.right;
		
		E s_elem = data;
		E s_right_elem = right.data;
		
		/* 
		 * Now perform the rotation through mutations.
		 * Warning : Not Thread Safe.
		 */
		
		/* 
		 * Exchange the states of this node and its left node
		 * to preserve the integrity of this node's parent's pointers.
		 * While performing a right rotation.
		 */
		data  = s_right_elem;
		right = s_rightr;
		left  = s_right;
		
		s_right.data  = s_elem;
		s_right.right = s_rightl;
		s_right.left  = s_left;
		
		// Correct the height metadata.
		left .updateHeight();
		//right.updateHeight();
		this .updateHeight();
		
		// Correct the size metadata.
		left.updateSize();
		this.updateSize();
	}

	@Override
	public AVL<E> clone()
	{
		// Create a new output node.
		AVL<E> output = new AVL<E>();
				
		// Copy the MetaData.
		output.data 	= data;
		output.height	= height;
		output.size		= size;
		
		output.left  = left  != null ? left .clone() : null;
		output.right = right != null ? right.clone() : null;
		
		return output;
	}

	
	// -- Immutable superfluous unimplemented methods.
	@Override
	public BST<E, AVL<E>> add_static(E elem)
	{
		throw new Error("Please use add(), because AVLs are mutable.");
	}

	@Override
	public BST<E, AVL<E>> remove_static(E elem)
	{
		throw new Error("Please use remove(), because AVLs are mutable.");
	}

}
