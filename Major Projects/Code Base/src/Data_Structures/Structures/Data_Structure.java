package Data_Structures.Structures;

import java.util.Iterator;

import BryceGraphs.ADTs.AdjacencyNode;

import util.Genarics;


/* Data structure interface class.
 * 
 * Written by Bryce Summers on 5 - 20 - 2013.
 * 
 * Purpose : This interface is used in the attempt that it will
 *			 maintain high quality standards for my data structures.
 *
 * Implementation : 1. local helpful utility structures.
 * 					2. transformation functions that convert data structures
 *					   into any other data structures.
 *					Note : all transformation functions are non destructive to the data structures involved.
 *						   The object data involved is persistent though.
 *					3. Appending functionality.
 *					4. Helper functions : isEmpty() and size().
 *					Note : These should be overridden by subclasses with O(1) 
 *						   implementations that are more efficient.
 *					5. Recursive data structure support.
 *					   - data structures should be able to be elements within
 *						 other data structures.
 *					6. Data Structures should be serializable.
 *
 * Methods without the _static suffix are assumed to perform mutations on the data structure.
 */

// TODO : Bullet proof this code against null pointers.
// FIXME : Incorporate BSTs and Heaps.

public abstract class Data_Structure<E> implements Iterable<E>
{
	// -- local data.
	
	// Deals with E elements.
	protected Genarics<E> ge_e = new Genarics<E>(); 
		
	// -- Data structure transformation functions.
	
	public List<E> toList()
	{
		List<E> output = new List<E>();
		
		for(E elem : this)
		{
			output.add(elem);
		}
		
		return output;
	}
		
	@SuppressWarnings("unchecked")
	public E[] toArray()
	{
		if(isEmpty())
		{
			return null;
		}
		
		Iterator<E> iter = iterator();
		
		// We know that this should exist, because the structure is not empty.
		E elem = iter.next();
	
		// The mighty creation of a genaric array.
		
		Class<E> clazz = (Class<E>) getFirst().getClass();
		
		E[] output;
		
		// I will probably need special cases for all Autoboxed classes.
		
		if(clazz == Character.class ||
		   clazz == Integer.class   ||
		   clazz == Long.class      ||
		   clazz == Short.class     ||
		   clazz == Boolean.class   ||
		   clazz == Double.class    ||
		   clazz == Float.class     ||
		   clazz == Byte.class
		)
		{
			output = ge_e. Array(size(), getFirst());
		}
		else
		{
			// FIXME : Try again in the future to eliminate the need for this.
			output = ge_e. Array(size(), getFirst());
			//output = (E[]) new Object[size()];
		}
		
		
		
		output[0] = elem;
			
		// Start at 0...
		int i = 1;

		// Iterate through all of the elements.
		while(iter.hasNext())
		{
			// populate the array.
			output[i] = iter.next();
			i++;
		}
		
		return output;
	}
	
	public UBA<E>  toUBA()
	{
		if(isEmpty())
		{
			return new UBA<E>(1);
		}
		
		// create an output UBA of a starting size that is
		// large enough to contain all of the data elements.
		UBA<E> output = new UBA<E>(size());
						
		// Iterate through all of the elements.
		for(E elem : this)
		{
			// populate the array.
			output.add(elem);
		}

		return output;
	}
	
	public HashTable<E> toHashTable()
	{
		if(isEmpty())
		{
			return new HashTable<E>(1);
		}
		
		HashTable<E> output = new HashTable<E>(size()*100 / HashTable.MAX_LOAD_PERCENTAGE);
		
		// Insert every element in the HashTable.
		for(E elem : this)
		{
			output.insert(elem);
		}
		
		return output;
		
	}
	
	// -- Appending Data.
	
	public abstract void add(E elem);
	
	public void append(E ... input_array)
	{
		for(E elem : input_array)
		{
			add(elem);
		}
	}
	
	public void append(Data_Structure<E> D)
	{
		for(E elem : D)
		{
			add(elem);
		}
	}
	
	public void append(Iterable<E> collection)
	{
		for(E elem : collection)
		{
			add(elem);
		}
	}
	

	// -- helper functions.
	
	// This method should be overridden if a more efficient method is possible.
	public boolean isEmpty()
	{
		Iterator<E> iter = iterator();
		return !iter.hasNext();
	}
	
	// This method should be overridden if a more efficient method is possible.
	// returns 0 if the data structure is empty.
	// O(num_columns)
	public int size()
	{
		Iterator<E> iter = iterator();
		
		int output = 0;
		
		// Iterate through all of the elements and count them.
		while(iter.hasNext())
		{
			output++;
			iter.next();
		}
		
		return output;
	}
	
	// Returns the index of the first occurrence of the given element.
	public int getIndex(E input)
	{
		int index = 0;
		for(E elem : this)
		{
			if(elem.equals(input))
			{
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	// -- Hashing and inclusion should be implemented for every data structure.
	
	// Every data structure should accept any other data structure as elements.
	// These methods override the methods in java -> Object,
	// because we want these functions to be implemented powerfully and deeply.
	public int hashCode()
	{
		int output = 0;
		int index  = 0;
		
		for(E elem : this)
		{
			output += elem.hashCode() + 1199459*index*index;
			index++;
		}
		
		return output;

	}
	
	/* 
	 * This genaric iterable equals function will work for any structures that have a defined ordering to the data.
	 * For example, this should work for Lists, Unbounded arrays, and Binary Search Trees.
	 * This equals function should be overriden for data structures that do not have ordered data.
	 * For example, this function will not work for HashTables, Dictionaries, Sets, ect.
	 */

	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		
		if(this.getClass() != o.getClass())
		{
			return false;
		}
	
		@SuppressWarnings("unchecked")
		Data_Structure<E> other = (Data_Structure<E>) o;
				
		Iterator<E> iter1 = iterator();
		Iterator<E> iter2 = other.iterator();
		
		
		if(size() != other.size())
		{
			return false;
		}
		
		// - Compare all elements for equality.
		
		while(iter1.hasNext())
		{
			
			E elem1 = iter1.next();
			E elem2 = iter2.next();
			
			if(!elem1.equals(elem2))
			{
				return false;
			}			
		}
				
		return true;
		
	}
	
	// Printing
	public abstract String toString();
	
	// -- Serialization.
	//public abstract void serialize(output stream)
	//public abstract Data_Structure<E> unserialize();
	
	// Cloning. Deep copying of structures, shallow copying of data.
	public abstract Data_Structure<E> clone();
	
	public E getFirst()
	{
		for(E elem: this)
		{
			return elem; 
		}
		
		return null;
	}

	/*
	public E getLast()
	{
		E output = null;
		
		for(E elem : this)
		{
			output = elem;
		}
		
		return output;
	}
	*/
	
	// O(size()) element querying.
	// This should be overridden with more efficient methods in classes such as Hash Tables and BSTs.
	public boolean contains(E input)
	{
		if(input == null)
		{
			return false;
		}
		
		for(E i : this)
		{
			if(input.equals(i))
			{
				return true;
			}
		}
		
		return false;
	}

	// Non Destructively creates a new data structure of this type that contains the union of this and another structure's data.
	public Data_Structure<E> meld(Data_Structure<E> other)
	{
		Data_Structure<E> output = clone();
		
		for(E elem : other)
		{
			output.add(elem);
		}
		
		return output;
	}
	
}
