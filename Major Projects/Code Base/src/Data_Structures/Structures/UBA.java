package Data_Structures.Structures;

import java.util.Iterator;

import Data_Structures.ADTs.Deque;
import Data_Structures.ADTs.Pairable;
import Data_Structures.ADTs.Queue;
import Data_Structures.ADTs.Stack;
import Data_Structures.Operations.Sort;

/*
 * UBA: Unbounded Array.
 * Written by Bryce Summers.
 * Purpose: allows for arrays with mutable sizes.
 * 
 * Capabilities:
 *  		 - O(1) search.
 *  		 - Amortized O(1) resizing.
 *  		 - Amortized O(1) adding
 *  		 - Amortized O(1) removing.
 *  		 - Amortized equivalent complexity queue/stack functionality.
 *  		 - I am really proud of this data structure!
 */

// FIXME : Improve bounding of the allowed memory growth Make sure that it grows to maximum integer size, before overflowing.

public class UBA<E> extends Data_Structure<E> implements Pairable<E>, Stack<E>, Queue<E>, Deque<E>
{

	// -- Local variables
	private E[] data;
	
	// Specify the offset of where the array's wrapped around data begins.
	int start;
	
	// Number of elements inside the array.
	int size;
	
	// Capacity of the array.
	int limit;
	
	// -- Constructor.
	
	public UBA()
	{
		clear();
	}
	
	// REQUIRES : limit_start must be strictly positive.
	public UBA(int limit_start)
	{
		clear(limit_start);
	}
	
	// Should be passed non original data.
	private UBA(E[] input, int size_in)
	{
		size  = size_in;
		start = 0;
		limit = input.length;
		data  = input;
	}
	
	// -- Interface Functions.
	
	public void add(E elem)
	{
		if(size == limit)
		{
			resize(limit*2);
		}

		// The index that the element will be deposited at.
		int index = size;
		
		// Increase the allowed size.
		size++;// creates a next locations that is accessible.
		
		// Set the proper array element to the given elem.
		set(index, elem);
		
	}
	
	// Allows users to add elements to the front of the array.
	// WARNING : This addition will change the index of every element inside of the array.
	// This can be used for number implementations that want to be multiplied by 2.
	public void prepend(E elem)
	{
		if(size == limit)
		{
			resize(limit*2);
		}

		// The index that the element will be deposited at.
		int index = 0;
		
		// Increase the allowed size.
		size++;// creates a next locations that is accessible.

		// Move the start back 1 so that the given element can be prepended.
		start = (start +limit - 1) % limit;
		
		// Set the proper array element to the given elem.
		set(index, elem);
	}
	
	// Removes the last element from the array.
	// This method can be thought of as the inverse of the add method.
	public E rem()
	{

		if(size == 0)
		{
			throw new Error("ERROR UBA: Cannot remove element from empty array.");
		}

		// Get the last element.
		E elem = get(size - 1);
		
		// Force the UBA to forget the last element.
		set(size - 1, null);
		
		size--;

		// Do not use size/2, because it fails when size = 0.
		if (size < limit/4)
		{
			resize(limit/2);
		}

		return elem;

	}
	
	// Stack / Queue methods.
	public void push(E elem)
	{
		add(elem);
	}
	
	public E pop()
	{
		return rem();
	}
	
	public void enq(E elem)
	{
		add(elem);
	}
	
	public E deq()
	{
		if(size == 0)
		{
			throw new Error("ERROR UBA: Cannot remove element from empty array.");
		}
		
		// Get the first element.
		E output = get(0);
		
		// Force the UBA to forget the first element.
		set(0, null);
		
		// Increment the start pointer.
		start = (start + 1) % limit;
		size--;
		
		// Be careful that the limit is never resized to 0.
		if (size < limit / 4)
		{
			resize(limit / 2);
		}
		
		return output;
		
	}	
	
	// -- Data access methods.
	
	public void set(int index, E elem)
	{

		if(0 <= index && index < size)
		{
			// JAVA deals with negatives.
			data[(start + index) % limit] = elem;
			return;
		}
		
		throw new Error(" ERROR: UBA: invalid index (" + index + ") for Set.");

	}
	
	public E get(int index)
	{
		if(0 <= index && index < size)
		{
			// JAVA deals with negatives.
			return data[(start + index) % limit];
		}
		
		throw new Error("ERROR: UBA: invalid index for get : " + index);
	}
	
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	
	@Override
	public int size()
	{
		return size;
	}

	public E getFirst()
	{
		if(size == 0)
		{
			return null;
		}
		
		return get(0);
	}

	public E getLast()
	{
		if(size == 0)
		{
			return null;
		}
		
		return get(size - 1);
	}


	// Returns an array with the given amount of data.
	// Non data containing elements will all be null.
	// The array will be in order from element 0 to element amount - 1.
	private E[] getData(int amount)
	{
		if(amount < size)
		{
			throw new Error("ERROR: Data Array lacks room for data.");
		}
		
		// Initialize output array.
		@SuppressWarnings("unchecked")
		E[] output = (E[]) new Object[amount];
		
		// Copy the UBA's data.
		for(int i = 0; i < size; i++)
		{
			output[i] = get(i);
		}		
		
		// Return the copy.
		return output;
	}
	
	// -- Helper methods.
	
	private void resize(int new_limit)
	{
		// Copy over the data into an array of the new size.
		data = getData(new_limit);
		
		// Now mutate the internal ints,
		// now that the data is safe.
		
		// Set a new limit.
		limit = new_limit;
		
		// We have reverted the data to begin at start.
		start = 0;
	}
	
	public UBA<E> copy()
	{
		return clone();
	}
	
	@Override
	public UBA<E> clone()
	{
		// Create new array.
		E[] temp = getData(limit);
		
		// Create a new cloned UBA.
		return new UBA<E>(temp, size);
	}
	
	// Swaps two elements in this UBA.
	public void swap(int i1, int i2)
	{
		E temp = get(i1);
		set(i1, get(i2));
		set(i2, temp);
	}
	
	// insertion sort.
	public void isort()
	{
		sort(false);
	}
	
	// Divide and conquer sort.
	public void sort()
	{
		sort(true);
	}
	
	@SuppressWarnings("unchecked")
	private void sort(boolean msort)
	{
		// Create new array.
		Comparable<E>[] temp = ge_e.Comparable_Array(size);
		
		// Copy the non null data.
		for(int i = 0; i < size; i++)
		{
			// Cast each element.
			temp[i] = (Comparable<E>) get(i);
		}
		
		if(msort)
		{
			Sort.msort(temp);
		}
		else
		{
			Sort.isort(temp);			
		}
		
		// Revert the offset.
		start = 0;
		
		// Set the data to the new sorted data.
		data = (E[]) temp;
	}
	
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		
		output.append("\nUBA[Size = " + size + ", Limit = " + limit + "]\n\n");
		
		if(size == 0)
		{
			output.append("[Empty]\n");
		}
		
		for(int i = 0; i < size; i++)
		{
			output.append(get(i) + "\n");
		}
		
		output.append("[UBA END]\n\n");
		
		return output.toString();
	}

	@Override
	public Iterator<E> iterator()
	{
		return new iter();
	}
	
	private class iter implements Iterator<E>
	{
		int index;
		
		public iter()
		{
			index = -1;
		}
		
		@Override
		public boolean hasNext()
		{
			return index + 1 < size;
		}

		// Iterates through every element of every chain.
		@Override
		public E next()
		{	
			index++;
			E output = get(index);
			return output;
		}

		@Override
		public void remove()
		{
			if(index == 0)
			{
				deq();
				
				// Index should go to the new first element next.
				index--;
				return;
			}
			
			// Last element.
			if(index == size - 1)
			{
				rem();
				return;
			}
			
			throw new Error("Removal of sandwitched UBA elements is not yet supported.");
			
		}


						
	}// End of iter class.

	// Clears this data structure of all entered data.
	@SuppressWarnings("unchecked")
	public void clear()
	{
		limit = 1;
		data  = (E[]) new Object[limit];
		
		start = 0;
		size =  0;
	}

	@SuppressWarnings("unchecked")
	public void clear(int limit_start)
	{
		// The limit must be a positive integer so that the capacity increases when the limit doubles.
		if(limit_start <= 0)
		{
			throw new Error("The limit must be a positive integer.");
		}
		
		limit = limit_start;

		data = (E[]) new Object[limit];
	}
	
	/*
	 * Linear Operation that deletes the given element and then shifts
	 * the rest of the elements to reindex the array.
	 * Worst case linear time, because we are using a contiguous array.
	 */
	public E delete_and_shift(int index)
	{
		// Deleting the first element. O(1).
		if(index == 0)
		{
			return deq();
		}
		
		// Deleting the last element. O(1);
		if(index == size - 1)
		{
			return rem();
		}
		
		// Retrieve the removed element.
		E output = get(index);
		
		// Shift the rest of the elements.
		for(int i = index; i < size - 1; i++)
		{
			set(i, get(i + 1));
		}
		
		// Remove the last empty slot.
		rem();
		
		return output;
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

	@Override
	// Peep back. Stack interface function.
	public E top()
	{
		return getLast();
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

	@Override
	public E peek()
	{
		return getFirst();
	}
	
	// Moves the front node to the back.
	// Can be used to eliminated excessive garbage collection in looping operations.
	public void frontToBack()
	{
		E elem = deq();
		enq(elem);
	}

	// Ensures that this UBA can hold up to the given number of elements.
	// FIXME : Thinking may be required to make sure this function is reosonably implemented.
	public void ensureCapacity(int number)
	{
		if(number > limit)
		{
			resize(2*number);
			return;
		}
		
		size = number;
	}
	
	// Increases the size of the array to accomodate the given index.
	// the given size and fills each added entry with the given element.
	// if the current size of the array already permits the index, this does nothing.
	public void fillToIndex(int last_index)
	{
		fillToIndex(null, last_index);
	}
	
	public void fillToIndex(E elem, int last_index)
	{
		ensureCapacity(last_index + 1);
		
		while(size <= last_index)
		{
			add(elem);
		}
	}


	
}
