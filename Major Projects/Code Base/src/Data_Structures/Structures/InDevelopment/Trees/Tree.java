package Data_Structures.Structures.InDevelopment.Trees;

import java.util.Iterator;

import Data_Structures.Structures.Data_Structure;
import Data_Structures.Structures.List;
import Data_Structures.Structures.SingleLinkedList;

/*
 * The Bryce Summers tree class.
 * 
 * Conceived on 7 - 28 - 2013, By Bryce Summers.
 * Implemented on 12 - 29 - 2013.
 * 
 * Purpose : Unlike the binary search trees that will be used for things like priority queues,
 * 			Trees care a lot more about their structure than their search and ordering performance.
 * 			This class will be used for :
 * 			1. Creating efficient structures for storing Lots of Strings. (Splay tree, ect).
 * 
 * 			2. Just defining trees.
 * 
 * 			3. Defining execution paths such as in my Linear Algebra Math program,
 *				in which users could use the tree functionality to see the past mistakes that they made.
 */

// Implement Path iterators.
// FIXME : Scoop out the code from the gui_tree class and put it into this class, then update that class to use this classes' functionality.

public class Tree<E> extends Data_Structure<E>
{
	// -- Private Data characteristic of a tree.
	List<Tree<E>> children;
	E data;
	
	public Tree(E elem)
	{
		children = new List<Tree<E>>();
		data	 = null;
	}
	
	@Override
	public void add(E elem)
	{
		children.add(new Tree<E>(elem));
	}
	
	// Allows for trees to be constructed recursively.
	public void add(Tree<E> t)
	{
		children.add(t);
	}
	
	// Provides access to all of the Children Trees.
	// FIXME : The Adversary could ruin their trees due to the lack of cloning here.
	public List<Tree<E>> getChildren()
	{
		return children;
	}

	@Override
	public String toString()
	{
		return "Node(" + data + ", " + children + ")";
	}

	// Performs a cloning of all of the structural data of the tree.
	// Does not clone the elements.
	@Override
	public Tree<E> clone()
	{
		Tree<E> output = new Tree<E>(data);
		
		for(Tree<E> t : children)
		{
			output.children.add(t.clone());
		}
		
		return output;
	}

	// -- Iteration.
	
	@Override
	public Iterator<E> iterator()
	{
		return new Iter(Iter.DFS, this);
	}
	
	private class Iter implements Iterator<E>
	{
		
		static final int DFS = 0;
		static final int BFS = 1;
		
		SingleLinkedList<Tree<E>> frontier;
		int mode;
		
		public Iter(int mode, Tree<E> root)
		{
			frontier.add(root);
			this.mode = mode;
		}

		@Override
		public boolean hasNext()
		{
			return !frontier.isEmpty();
		}

		@Override
		public E next()
		{
			Tree<E> output;
			Iterator<Tree<E>> iter;
			
			switch(mode)
			{
				default:
				case DFS :	output = frontier.pop();
							iter = output.children.getTailIter();
							while(iter.hasNext())
							{
								frontier.push(iter.next());
							}
							return output.data;
							
				case BFS :	output = frontier.deq();
							for(Tree<E> t : output.children)
							{
								frontier.enq(t);
							}
							return output.data;
			}
		}

		@Override
		public void remove()
		{
			throw new Error("Not Implemented");
		}
		
	}
}