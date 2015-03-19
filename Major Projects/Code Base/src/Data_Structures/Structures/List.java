package Data_Structures.Structures;

import java.util.Iterator;

import util.Genarics;

import Data_Structures.ADTs.Deque;
import Data_Structures.ADTs.Pairable;
import Data_Structures.ADTs.Queue;
import Data_Structures.ADTs.Stack;
import Data_Structures.Operations.Sort;
import Data_Structures.Structures.HashingClasses.Set;

/*
 * List, used as Stacks and Queues.
 * Written by Bryce Summers, 12 - 23 - 2012.
 * Updated Slightly in Data_Structure superclassing on 5 - 22 - 2013.
 * 
 * Update 7 - 28 - 2013 : The Backwards iterator was phased out.
 * The forward iterator was upgraded to implement the IterB specification that allows the
 * iterators to act like pointers to the list that can move forward and backwards,
 * and perform insertions and deletions along the way.
 * 
 * Update 12 - 19 - 2013 :  The IterB interface implementation was completed.
 * 							The List class has now been fully tested.
 * 							Implemented List Reversal.
 * Updated 12 - 23 - 2013 : Implemented Stack and Queue ADTs.
 * 
 * Push / Enq adds to the tail.
 * Pop removes from teh tail.
 * deq() removes from the head.
 */


public class List<E> extends Data_Structure<E> implements Pairable<E>, Stack<E>, Queue<E>, Deque<E>
{

	// -- Local structures.
	private class Node
	{
		Node prev;
		Node next;
		E elem;
	}
	
	// -- Header data.
	private Node first;
	private Node last;
	private int size = 0;

	// Deals with E elements.
	Genarics<E> ge_e = new Genarics<E>(); 
	
	// Empty List constructor.
	public List(){}
	
	public List(E ... data)
	{
		append(data);
	}

	// -- Push, Pop, Enq, and Deq.
	public void push(E elem)
	{
		
		// Create node.
		Node n = new Node();
		
		// Add the data.
		n.elem = elem;
		
		if(isEmpty())
		{
			first = n;
			last = n;
			
			size++;
			return;
		}
		
		// Append it to the list.
		n.prev = last;
		last.next = n;
		last = n;
		
		size++;
	}
	
	public E pop()
	{
		if(isEmpty())
		{
			throw new Error("ERROR: List: cannot pop from empty list!");
		}
		
		size--;
		
		Node output = last;
		last = last.prev;
		
		if(last != null)
		{
			last.next = null;
		}
		else
		{
			// Make sure to forget all imformation in empty lists.
			first = null;
		}
		
		return output.elem;		
	}
	
	public void enq(E elem)
	{
		push(elem);		
	}
	
	public E deq()
	{
		if(isEmpty())
		{
			throw new Error("ERROR: List: cannot deq from empty list!");
		}
		
		size--;
		
		Node output = first;
		
		first = first.next;
		
		if(first != null)
		{
			first.prev = null;
		}
		else
		{
			// Make sure to forget all imformation in empty lists.
			last = null;
		}
		
		return output.elem;
		
	}
	
	public void add(E elem)
	{
		push(elem);
	}
	
	public E rem()
	{
		return pop();
	}
	
	// Removes the first occurrence of the given element.
	public boolean remove(E elem)
	{
		IterB<E> iter = getIter();
		
		while(iter.hasNext())
		{
			E e = iter.next();
			if(e.equals(elem))
			{
				iter.remove();
				return true;
			}
		}
		
		return false;
	}
	
	// Pushes an element onto the lead of the list instead of the tail of the list.
	public void prepend(E elem)
	{		
		// Create node.
		Node n = new Node();
		
		// Add the data.
		n.elem = elem;
		
		if(isEmpty())
		{
			first = n;
			last = n;
			
			size++;
			return;
		}
		
		// Prepend it to the list.
		n.next = first;
		first.prev = n;
		first = n;
		
		size++;
	}
	
	// Reverse this list in place.
	public void reverse()
	{
		// Save the data.
		E[] data = toArray();
		
		// Erase the internal structure of this list.
		clear();
		
		// Insert the data in reverse order.
		for(E e : data)
		{
			prepend(e);			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sort()
	{
		// Do not perform the sort, if this list is empty.
		if(isEmpty())
		{
			return;
		}
		
		// Get an array form of this list, assuming this is a list of comparable elements.
		Comparable<E>[] A = toComparableArray();
		
		// Sort the array.
		Sort.msort(A);
		
		// Empty this list.
		clear();
		
		// Append the newly sorted array to the now empty list.
		append((E[])A);
	}
	
	// -- helper functions.
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	public int size()
	{
		return size;
	}
	
	// Allows for the first element to be non destructively peeked at.
	public E getFirst()
	{
		// Deal with an empty list.
		if(isEmpty())
		{
			return null;
		}
		return first.elem;
	}

	// Allows for the last element to be non destructively peeked at.
	public E getLast()
	{
		// Deal with an empty list.
		if(isEmpty())
		{
			return null;
		}
		
		return last.elem;
	}
	
	// Returns the array version of this list.

	@SuppressWarnings("unchecked")
	public Comparable<E>[] toComparableArray()
	{
		if(isEmpty())
		{
			return null;
		}
		
		// FIXME : Include a type checker here. Do some Java genarics research.
		
		Comparable<E>[] output = ge_e.Comparable_Array(size);
		Node n = first;
		
		int i = 0;
		
		// FIXME : The second clause of this 'AND' statement should be unneccesary,
		//         I should track down and smite the bug in the iterator.
		while(n != null && i < size)
		{
			output[i] = (Comparable<E>) n.elem;
			n = n.next;
			i++;
		}
		
		return output;
	}
	
	// Allows for lists to be deep copied.
	
	@Override
	public List<E> clone()
	{
		List<E> other = new List<E>();
		
		for(E elem : this)
		{
			other.add(elem);
		}
		
		return other;
	}
	
	public void clear()
	{
		first = null;
		last  = null;
		size  = 0;
	}
	
	// IterB starting before the head of the list.
	public IterB<E> getIter()
	{		
		// Start out on a node pointing to the first node;
		Node temp = new Node();
		temp.next = first;
		return new iter(temp);
	}
	
	// Returns an iterator that starts after the tail of the list.
	public IterB<E> getTailIter()
	{
		// Start out on a node pointing to the first node;
		Node temp = new Node();
		temp.prev = last;
		return new iter(temp);
	}
	
	// Makes this data structure compatible with Java.lang.Iterable.
	@Override
	public Iterator<E> iterator()
	{
		return getIter();
	}

	// -- Iteration
	
	private class iter implements IterB<E>
	{
		private Node current_node;
		
		private boolean has_current = false;
				
		public iter(Node n)
		{
			current_node = n;
		}
		
		@Override
		public boolean hasNext()
		{
			// Deal with the current_node pointing to a null node.
			if(current_node == null)
			{
				return false;
			}
			return current_node.next != null;
		}

		@Override
		public E next()
		{
			current_node = current_node.next;
			has_current = true;
			return current_node.elem;
		}
		
		@Override
		public boolean hasPrevious()
		{
			return current_node.prev != null;
		}

		@Override
		public E previous()
		{
			has_current = true;
			current_node = current_node.prev;
			return current_node.elem;
		}

		// Removes the current node,
		// but does not change the results of the next next() or previous() call.
		@Override
		public void remove()
		{
			
			if(!has_current)
			{
				throw new Error("Iteration must take place before removal.");
			}
			
			has_current = false;
			
			Node prev = current_node.prev;
			Node next = current_node.next;
			
			// Unlink the current node from the nodes previous. 
			if(prev != null)
			{
				prev.next = next;
			}
			
			// Unlink this node from the nodes succeeding it.
			if(next != null)
			{
				next.prev = prev;
			}
			
			if(current_node == first)
			{
				first = next;
			}
			
			if(current_node == last)
			{
				last = prev;
			}
			
			current_node.elem = null;
			
			// Decrement the size of the list.
			size--;
			
			/* Note: We have not modified the current_node,
			 * so when we call next again,
			 * it will behave as expected, 
			 * e.g. the iterator is pointing to the node that was deleted,
			 * which still has the same next and previous pointers.
			 */
		}
		
		// Replaces the last node that was returned with this iterator with the given element.
		public void replace(E elem)
		{
			if(!has_current)
			{
				throw new Error("Iteration must take place before a replacement.");
			}
			
			current_node.elem = elem;
		}

		// REQUIRES : The iterator should have a current_node value that is not null.
		// ENSRUES : Inserts the given element before the last node that the Iterator returned with a next() or previous() call.
		// ENSURES : The iterator will point to the newly inserted node.
		// Simply adds the node to the the list, if it is empty.
		@Override
		public void insertBefore(E elem) 
		{
			if(!has_current)
			{
				add(elem);
				current_node = first;
				has_current = true;
				return;
			}
			
			// Create the new node.
			Node node_new = new Node();
			node_new.elem = elem;
			
			Node prev = current_node.prev;
			Node next = current_node;
			
			// Link the new node to to the proper node.
			
			node_new.prev = prev;
			node_new.next = next;
			
			// Link the previous node to the new node. 
			if(prev != null)
			{
				prev.next = node_new;
			}
			
			// Link the next node to the new_node. (the current_node).
			if(next != null)
			{
				next.prev = node_new;
			}
			
			if(current_node == first)
			{
				first = node_new;
			}
			
			/*
			if(current_node == last)
			{
				last = node_new;
			}
			*/
			
			// Increment the size of the list.
			size++;
						
			// We must update the location of the current_node.
			current_node = node_new;
		}

		
		// REQUIRES : The iterator should have a current_node value that is not null.
		// ENSRUES : Inserts the given element before the last node that the Iterator returned with a next() or previous() call.
		// ENSURES : The iterator will point to the newly inserted node.
		// Simply adds the node to the the list, if it is empty.
		@Override
		public void insertAfter(E elem)
		{			
			if(!has_current)
			{
				add(elem);
				current_node = first;
				has_current = true;
				return;
			}

			// Create the new node.
			Node node_new = new Node();
			node_new.elem = elem;
			
			Node next = current_node.next;
			Node prev = current_node;
			
			// Link the new node to to the proper node.
			
			node_new.prev = prev;
			node_new.next = next;
			
			// Link the next node to the new node. 
			if(next != null)
			{
				next.prev = node_new;
			}
			
			// Link the previous node (The current_node) to the new_node.
			if(prev != null)
			{
				prev.next = node_new;
			}
			
			/*
			if(current_node == first)
			{
				first = node_new;
			}
			*/
			
			if(current_node == last)
			{
				last = node_new;
			}
			
			// Increment the size of the list.
			size++;
						
			// We must update the location of the current_node.
			current_node = node_new;

		}

		// Should be called after a hasCurrent call.
		// Returns the last element that was returned, even if it has been removed.
		@Override
		public E current()
		{
			return current_node.elem;
		}

		// We should always be pointing to a node if we have iterated.
		@Override
		public boolean hasCurrent()
		{
			return has_current;
		}
		
		@Override
		public Object clone()
		{
			iter output = new iter(current_node);
			output.has_current = has_current;
			return output;
		}
		
		// Non typesafe equality function.
		public boolean equals(Object o)
		{
			iter other = (iter)o;
			return other.current_node == current_node;
		}
		
	}// End of iter class.
	
	public String toString()
	{
		
		StringBuilder output = new StringBuilder();
		
		output.append("List [Size = " + size + "]\n");
		
		if(isEmpty())
		{
			return output + "[Empty]";
		}
		
		Node n = first;
		
		while(n != null)
		{
			output.append(n.elem + " <-->\n"); 
			n = n.next;
		}
		
		output.append("[End of List]");
		
		return output.toString();
	}

	
	// -- DEQUE interface mappings.
	
	@Override
	public E peep_back()
	{
		return getLast();
	}

	@Override
	public E peep_front()
	{
		return getFirst();
	}

	@Override
	public E pop_back()
	{
		return pop();
	}

	@Override
	public E pop_front()
	{
		return deq();
	}

	@Override
	public void push_back(E elem)
	{
		push(elem);	
	}

	@Override
	public void push_front(E elem)
	{
		prepend(elem);
	}

	// Peep back. Stack interface function.
	@Override
	public E top()
	{
		return getLast();
	}
	
	@Override
	public E peek()
	{
		return getFirst();
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
		throw new Error("Not Implemented");
	}

	// returns an element and a stack structure that is the result of a pop operation.
	// does not disturb the original structure.
	// Undefined behavior if the original stack gets mutated.
	@Override
	public Pair<E, Stack<E>> pop_static()
	{
		throw new Error("Not Implemented");
	}

	public List<E> removeNulls()
	{
		Iterator<E> iter = iterator();
		while(iter.hasNext())
		{
			if(iter.next() == null)
			{
				iter.remove();
			}
		}
		
		return this;
	}
	
	// Requires each of the elements in this list to implement a proper hash function for efficiency.
	// This function destructivly removes all duplicate entries in this list using a hash set.
	public List<E> removeDuplicates()
	{
		Set<E> set = new Set<E>();
			
		Iterator<E> iter = iterator();
			
		while(iter.hasNext())
		{
			E elem = iter.next();
			if(set.includes(elem))
			{
				iter.remove();
				continue;
			}
			
			set.set_add(elem);
		}

		return this;
	}

	// DANGEROUS functions. Use extreme caution, i.e. please know what you are doing if you use them.
	
	// Use this function at your own risk. It appends the input list to this one 
	public void destructiveAppend(List<E> other)
	{
		
		// Degenerate.
		if(other.isEmpty())
		{
			return;
		}
		
		if(isEmpty())
		{
			first = other.first;
			last  = other.last;
			size  = other.size;
			return;
		}
		
		last.next = other.first;
		other.first.prev = last;
		last = other.last;
		size += other.size;

	}
	
}