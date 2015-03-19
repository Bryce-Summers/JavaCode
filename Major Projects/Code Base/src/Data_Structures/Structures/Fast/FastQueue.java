package Data_Structures.Structures.Fast;

import java.util.Iterator;

import Data_Structures.ADTs.Queue;
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
 * WARNING : This class is probably blatantly flawed and has not yet been tested. FIXME
 */


public class FastQueue<E> extends FastStructure implements Queue<E>, Iterable<E>
{

	// --  Private data fields.
	
	// Points to the last node added.
	private FastNode head = null;
	private FastNode tail = null;
	
	/* -- Interface Functions -- */
	
	int size = 0;
	
	@Override
	public E deq()
	{
		if(isEmpty())
		{
			throw new Error("Cannot deq from empty queue!");
		}		
		
		@SuppressWarnings("unchecked")
		E output = (E)head.data;
		
		FastNode old_head = head;
		head = head.link;
		yard_push(old_head);
		
		if(head == null)
		{
			tail = null;
		}
		
		size--;
		return output;
	}
	
	// Returns the top element of the stack.
	@SuppressWarnings("unchecked")
	public E peek()
	{
		if(isEmpty())
		{
			throw new Error("Cannot Peep at the head of an empty stack.");
		}
		
		return (E)head.data;
	}

	@Override
	public void enq(E elem)
	{
		FastNode new_tail = newNode();
		new_tail.data = elem;
		
		if(head == null)
		{
			head = new_tail;
			tail = new_tail;
			size = 1;
			return;
		}

		tail.link = new_tail;
		tail = new_tail;
		
		size++;
	}
	
	// very simple empty check.
	@Override
	public boolean isEmpty()
	{
		return head == null;
	}

	// -- Immutable static queue methods.
	
	// returns a queue structure that is the result of an enq,
	// Does not disturb the original structure.
	// Undefined behavior if the original stack gets mutated.
	@Override
	public Queue<E> enq_static(E elem)
	{
		Queue<E> output = shallow_clone();
		output.enq(elem);
		return output;
	}

	// returns an element and a queue structure that is the result of a deq operation.
	// does not disturb the original structure.
	// Undefined behavior if the original stack gets mutated.
	@Override
	public Pair<E, Queue<E>> deq_static()
	{
		FastQueue<E> output = shallow_clone();
		
		E elem  = output.peek();
		
		output.head = output.head.link;
		
		return new Pair<E, Queue<E>>(elem, output);
	}
	
	// Clones the outer pointers, but does not touch the internal nodes.
	// Useful for fast static function with immutable nodes.
	private FastQueue<E> shallow_clone()
	{
		FastQueue<E> output = new FastQueue<E>();
		output.head = head;
		output.tail = tail;
		output.size = size;
		
		return output;
	}

	@Override
	public int size()
	{
		return size;
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
			return current != tail;
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
