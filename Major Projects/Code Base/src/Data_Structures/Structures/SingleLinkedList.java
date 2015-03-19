package Data_Structures.Structures;

import java.util.Iterator;

import Data_Structures.ADTs.Queue;
import Data_Structures.ADTs.Stack;

/*
 * This class was conceived on 7 - 28 - 2013.
 * 
 * Written by Bryce Summers.
 * 
 * Purpose : This class should be a less powerful version of the Bryce Lists that only uses single links.
 * 
 * This class should explicitly implement some sort of STACK and QUEUE interface.
 * 
 * This class saves memory by only linking once.
 * O(2n) memory.
 *
 *
 * This should be used mostly for Efficient Stacks and Queues.
 * It should probably not be used for the representation of sequences.
 */

public class SingleLinkedList<E> extends Data_Structure<E> implements Stack<E>, Queue<E>
{
	// -- Characteristic Data.
	Node head;
	Node tail;
	int  size;
	
	private class Node
	{
		E elem;
		Node next;
		
		private Node(E data)
		{
			elem = data;
			next = null;
		}
	}
	
	// -- Constructor.
	public SingleLinkedList()
	{
		clear();
	}

	// -- Public Methods.
	
	// Clears all of the contents from this list.
	public void clear()
	{
		head = null;
		tail = null;
		size = 0;
	}
	
	public E getFirst()
	{
		return head.elem;
	}
	
	public E getLast()
	{
		return tail.elem;
	}
	
	public int size()
	{
		return size;
	}
	
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	// Equivalent to a push.
	@Override
	public void add(E elem)
	{
		push(elem);
	}
	
	// -- STACK and QUEUE functions.
	
	@Override
	public void enq(E elem)
	{
		Node n = new Node(elem);
		size++;
		
		// Empty.
		if(head == null)
		{
			head = n;
			tail = n;
			return;
		}
		
		tail.next = n;
		tail = n;
	}

	// Removes and returns the Head of the list (First in).
	@Override
	public E deq()
	{
		switch(size)
		{
			case 0 : throw new Error("ERROR : Cannot Deq from an Empty SL list!");
			
			case 1 : E output = head.elem;
					 size = 0;
					 head = null;
					 tail = null;
					 return output;
					 
			default: output = head.elem;
					 head = head.next;
					 size--;
					 return output;
		
		}
	}

	// Pushes elements onto the head of the list, instead of the tail.
	// (Last in First out.)
	// Analagous to enq.
	@Override
	public void push(E elem)
	{
		Node n = new Node(elem);
		size++;
		
		// Empty.
		if(head == null)
		{
			head = n;
			tail = n;
			return;
		}
		
		n.next = head;
		head = n;
	}

	// Removes and returns the head of the list.
	@Override
	public E pop()
	{
		return deq();		
	}
	
	@Override
	public E top()
	{
		return head.elem;
	}
	
	// Copied from List toString().
	public String toString()
	{
		
		StringBuilder output = new StringBuilder();
		
		output.append("\nList [Size = " + size + "]\n");
		
		if(isEmpty())
		{
			return output + "[Empty]";
		}
		
		Node n = head;
		
		while(n != null)
		{
			output.append(n.elem + " <-->\n"); 
			n = n.next;
		}
		
		output.append("[End of List]\n");
		
		return output.toString();
	}

	@Override
	public SingleLinkedList<E> clone()
	{
		SingleLinkedList<E> other = new SingleLinkedList<E>();
		
		for(E elem : this)
		{
			other.add(elem);
		}
		
		return other;
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return new Iter();
	}
	
	// Standard Iterator code.
	private class Iter implements Iterator<E>
	{

		// Used for removal.
		Node prev;
		Node current;
		
		@Override
		public boolean hasNext()
		{
			return size != 0 && (current == null || current.next != null);
		}

		@Override
		public E next()
		{
			prev    = current;
			current = current == null ? head : current.next;
			
			return current.elem;
		}

		/* REQUIRES : current != null and current points to the last 
		 *				Node whose element was returned by the next() method.
		 */
		@Override
		public void remove()
		{
			//ASSERT(size > 0);
			
			// Handle head.
			if(head == current)
			{
				head = head.next;
			}
			
			if(tail == current)
			{
				tail = prev; 
			}
			
			if(prev != null)
			{
				prev.next = current.next;
			}
			
			size--;
		}
		
	}
	
	
	/******************************************************************/
	/*		Immutable Deque Methods!								  */
	/******************************************************************/
	
	
	// -- Immutable queue operations.
	
	// returns a queue that is the result of an enq() operation.
	// Does not disturb the original structure.
	// Undefined behavior if the original queue gets mutated.
	@Override
	public Queue<E> enq_static(E elem)
	{
		throw new Error("Not Implemented");
	}

	// returns an element and a queue structure that is the result of a deq() operation.
	// does not disturb the original structure.
	// Undefined behavior if the original queue gets mutated.
	@Override
	public Pair<E, Queue<E>> deq_static()
	{
		throw new Error("Not Implemented");
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
		Stack<E> output = shallow_clone();
		E elem  = output.pop();
		
		return new Pair<E, Stack<E>>(elem, output);
	}
	
	// Clones the outer pointers, but does not touch the internal nodes.
	// Useful for fast static function with immutable nodes.
	private SingleLinkedList<E> shallow_clone()
	{
		SingleLinkedList<E> output = new SingleLinkedList<E>();
		
		output.head = head;
		output.tail = tail;
		output.size = size;
				
		return output;
		
	}

	@Override
	public E peek()
	{
		if(isEmpty())
		{
			throw new Error("Empty list has not element to peek at.");
		}
		
		return head.elem;
	}

}
