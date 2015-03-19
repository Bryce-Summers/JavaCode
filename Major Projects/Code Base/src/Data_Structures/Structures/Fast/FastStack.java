package Data_Structures.Structures.Fast;

import java.util.Iterator;

import Data_Structures.ADTs.Stack;
import Data_Structures.Structures.Pair;

/*
 * Fast Stack class.
 * 
 * Written by Bryce Summers on 5 - 12 - 2014.
 * 
 * Purpose : This class is a stack that tries to provide as efficient as possible performance by observing various traits of systems programming.
 * 
 * - This class recycles popped nodes and stores them in a static stack for future use.
 * - This "freight yard" allows for minimal mallocing to be performed thus dramatically increasing performance for demanding 50 percent push, 50 percent pop applications.
 * 
 * - This class is focused on stacks. The data structures in this "Fast" package will be very focused on performance. Classes outside of this package are mainly made with
 * 	 the desire for broadest applicability and correctness and efficiency, but do not really care about system performance.
 * 
 * 
 */

public class FastStack<E> extends FastStructure implements Stack<E>, Iterable<E>
{

	// --  Private data fields.
	
	// Points to the last node added.
	private FastNode head = null;
	
	/* -- Interface Functions -- */
	
	@Override
	public E pop()
	{
		if(isEmpty())
		{
			throw new Error("Cannot Pop from empty stack!");
		}		
		
		@SuppressWarnings("unchecked")
		E output = (E)head.data;
		
		FastNode old_head = head;
		head = head.link;
		yard_push(old_head);
		
		return output;
	}
	
	// Returns the top element of the stack.
	@SuppressWarnings("unchecked")
	public E top()
	{
		if(isEmpty())
		{
			throw new Error("Cannot Peep at the head of an empty stack.");
		}
		
		return (E)head.data;
	}

	@Override
	public void push(E elem)
	{
		FastNode new_head = newNode();
		
		new_head.data = elem;
		new_head.link = head;
		head = new_head;		
	}
	
	// very simple empty check.
	@Override
	public boolean isEmpty()
	{
		return head == null;
	}

	// -- Immutable static stack methods.
	
	// returns a stack structure that is the result of a push,
	// Does not disturb the original structure.
	// Undefined behavior if the original stack gets mutated.
	@Override
	public Stack<E> push_static(E elem)
	{
		Stack<E> output = shallow_clone();
		output.push(elem);
		return output;
	}

	// returns an element and a stack structure that is the result of a pop operation.
	// does not disturb the original structure.
	// Undefined behavior if the original stack gets mutated.
	@Override
	public Pair<E, Stack<E>> pop_static()
	{
		FastStack<E> output = shallow_clone();
		E elem = output.top();
		
		output.head = output.head.link;
		
		return new Pair<E, Stack<E>>(elem, output);
	}
	
	// Clones the outer pointers, but does not touch the internal nodes.
	// Useful for fast static function with immutable nodes.
	private FastStack<E> shallow_clone()
	{
		FastStack<E> output = new FastStack<E>();
		output.head = head;
		return output;
	}

	@Override
	public Iterator<E> iterator()
	{
		return new iter();
	}
	
	private class iter implements Iterator<E>
	{

		FastNode current = head;
		
		// Has a next element if we have yet to reach the tail.
		// This allows correctness even in the midst of nested persistent queues via the static methods.
		@Override
		public boolean hasNext()
		{
			return current.link != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next()
		{
			current = current.link;
			return (E) current.data;
		}

		@Override
		public void remove()
		{
			throw new Error("Not Yet Implemented");
		}
		
	}

}
