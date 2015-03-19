package Data_Structures.Structures;

import static BryceMath.Calculations.MathB.abs;

import java.util.Iterator;

import util.Genarics;
import Data_Structures.ADTs.Sets.SimpleSet;

/* Generic Hash Table.
 * Written by Bryce Summers on 5 - 19 - 2013.
 * Implemented Set abstract data type on 12 - 29 - 2013.
 * 
 * Purpose : This is a standard data structure that allows elements
 *           to be stored and retrieved from a lookup table efficiently.
 *           
 * Capabilities : O(1) insertion.
 * 				: O(1) contains.
 *              : O(1) deletion.
 *              
 * Implementation: 	This hash table is implemented using a linked list "chain" array that contains
 *                  chains of entries for every possible hash value.
 *                  This has greater space complexity than other implementations.
 *                  
 * 					Hashing : Passed Java Objects will implement hashing by the
 * 							  standard java hashCode() function.
 * 					Element Equality :  Passed Java Objects will implement equality checking
 * 										by overriding the standard java equals() function.
 * 										Null pointer safety is ensured by an implemented equals(e1, e2) function.
 *                  
 * Notes : The chain approach can be replaced with linear probing or
 *         quadratic probing that deal with collisions by moving to
 *         linear "+1" or quadratic "+num_columns^2" neighbors instead of resolving them using chains.
 *         quadratic probing is not guaranteed to always find an open space by number theory.
 */

public class HashTable<E> extends Data_Structure<E> implements SimpleSet<E>
{
	// -- Constants.
	
	// maximum percentage allowed for load / data_table.length  
	public static final int MAX_LOAD_PERCENTAGE = 75;
	
	// -- Private data.
	
	// The array of chains.
	protected List<E>[] table;
	
	// Number of elements stored.
	private int load = 0;

	Genarics <List<E>> ge_chain = new Genarics<List<E>>();
	
	// -- Constructors

	// This should contain every constructor for Hash Tables.
	
	public HashTable()
	{
		// Create a minimal table.
		iTable(1);
	}
	
	public HashTable(int starting_size)
	{
		iTable(starting_size);
	}

	private void iTable(int starting_size)
	{		
		if(starting_size < 1)
		{
			throw new Error("Error : Hash Table starting size is too small!");
		}
		
		List<E> bogus = new List<E>();
		
		table = ge_chain.Array(starting_size, bogus);
		
		for(int i = 0; i < starting_size; i++)
		{
			table[i] = new List<E>();
		}
	}
	
	// -- Overriding efficient functions.
	
	// O(1).
	public boolean isEmpty()
	{
		return load == 0;
	}
	
	// O(1).
	public int size()
	{
		return load;
	}
	
	public int getTableSize()
	{
		return table.length;
	}
	
	// -- Exposed public functions.

	// Inserts the given element, if it is not already in the hash table.
	public void insert(E input)
	{
		int val = hash(input);
		
		List<E> chain = table[val];
		
		// Do not insert duplicates.
		if(chain.contains(input))
		{
			return;
		}
		
		chain.add(input);
		
		proccessInsertion();
		
	}
	
	// Queries whether the given element is in the table.
	public boolean contains(E input)
	{
		return query_and_remove(input, false);
	}
	
	// removes the given element if it is in the table.
	// returns false if the given element is not in the table.
	public boolean remove(E input)
	{
		return query_and_remove(input, true);
	}
	
	// -- Non exposed internal functions.
	
	// Returns true iff and only if the given element is found,
	// This function removes the given element from the hash table if should_remove is true.
	private boolean query_and_remove(E input, boolean should_remove)
	{
		int index = hash(input);
		
		List<E> chain = table[index];
		
		if(!should_remove)
		{
			return chain.contains(input);
		}
		
		Iterator<E> iter_chain = chain.iterator();
		
		while(iter_chain.hasNext())
		{
			E elem = iter_chain.next();
			
			if(equal(input, elem))
			{
				iter_chain.remove();
				proccessRemoval();
				return true;
			}
		}
		
		return false;
		
	}
	
	// Called after every deletion operation in the hash table.
	protected void proccessRemoval()
	{
		load--;
		
		if(under_capacity())
		{
			resize(table.length / 2);
		}
	}
	
	// Increases the load and resizes as necessary.
	protected void proccessInsertion()
	{		
		// Update the load.
		load++;
		
		// Maintain the hash table efficiency.
		if(at_capacity())
		{
			resize(table.length*2);
		}
	}
	
	// FIXME : Consider scaling the hash value by the size of the table. 
	
	// provides the hash for the given element in this table.
	protected int hash(E input)
	{
		if(input == null)
		{
			return 0;
		}
		
		// FIXME : Consider shifting a hashcode forward instead of negating it.
		return abs(input.hashCode()) % table.length;
	}
	
	
	// FIXME : You may have to tweak this a little to get the most reasonable memory management.
	
	// - Functions for Unbounded Array like behavior for the data table during insertion and deletion.
	
	// Returns true iff the load exceeds the maximum load
	// percentage in terms of the size of the data table.
	private boolean at_capacity()
	{
		return table.length*MAX_LOAD_PERCENTAGE / 100 <= load - 1;
	}
	
	private boolean under_capacity()
	{
		return table.length*(100 - MAX_LOAD_PERCENTAGE) / 100 > load;
	}
	
	// Resizes and rehashes the hash table.
	private void resize(int size_new)
	{
		if(size_new < 1)
		{
			throw new Error("Error: Data Table must be of a positive size.");
		}
		
		// Extract the elements.
		E[] temp = toArray();

		// Resize the table.
		table = ge_chain.Array(size_new, table[0]);
		
		// Populate it with empty lists.
		for(int i = 0; i < table.length; i++)
		{
			table[i] = new List<E>();
		}

		// Handle the empty table case.
		if(temp == null)
		{
			return;
		}
		
		// The table is now empty.
		load = 0;

		// Reinsert the elements.
		for(E elem : temp)
		{
			insert(elem);
		}
	}
	
	// A Null - pointer safe equality checking function for elements.
	private boolean equal(E elem1, E elem2)
	{
		return ge_e.xequal(elem1, elem2);
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return new iter();
	}
	
	private class iter implements Iterator<E>
	{
		
		// Stores the number of elements that have been iterated through.
		private int elements_seen;
		
		// Stores the index of the current chain.
		private int index;
		
		// A sub iterator that iterates through the chain at the current index.
		private Iterator<E> iter_chain;
		
		public iter()
		{
			elements_seen = 0;
			
			index = 0;
			
			iter_chain = getSubIterator(0);
		}
		
		private Iterator<E> getSubIterator(int index)
		{
			List<E> chain = table[index];
			return chain.iterator();
		}
		
		// FIXME: There is some sort of subtle bug that causes the assumptions regarding the accurate load invariant to fail.
		
		@Override
		public boolean hasNext()
		{
			return  elements_seen < load;
		}
		// Iterates through every element of every chain.
		@Override
		public E next()
		{	
			while(!iter_chain.hasNext())
			{
				index++;
				iter_chain = getSubIterator(index);
			}
			
			elements_seen++;
			
			return iter_chain.next();
		}

		/* 
		 * Removes the last element iterated.
		 * The iterator will return the same next node,
		 * regardless of whether this function is called.
		 */
		@Override
		public void remove()
		{
			iter_chain.remove();
			proccessRemoval();
		}
						
	}// End of iter class.

	@Override
	public String toString()
	{
		String output = "\n[HashTable, Size = " + size() + "]\n";
		
		for(E elem : this)
		{
			output = output + elem.toString() + "\n";
		}
		
		return output + "[End of Hash Table]\n";
		
		 
	}

	@Override
	public HashTable<E> clone()
	{
		HashTable<E> output = new HashTable<E>(table.length);
		
		for(E elem : this)
		{
			output.insert(elem);
		}
		
		return output;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		
		// FIXME : Ensure that subclasses will also work.
		if(o.getClass() != this.getClass())
		{
			return false;
		}
		
		@SuppressWarnings("rawtypes")
		HashTable input = (HashTable)o;
		
		int s1 = this .size();
		int s2 = input.size();
		
		if(s1 == 0 && s2 == 0)
		{
			return true;
		}
		
		if(s1 != s2)
		{
			return false;
		}
		
		// We know that this must exist,
		// because this Hash Table is not empty.
		E example = getFirst();		
		
		/* 
		 * Check for equal inclusion and equal types for every element.
		 * We know that if every element is included in both structures,
		 * then the Hash Tables are equal, because their sizes are equal
		 * and every element is distinct in this implementation.
		 */
		for(Object elem : input)
		{
			if(elem.getClass() != example.getClass())
			{
				return false;
			}
			
			if(!this.contains((E)elem))
			{
				return false;
			}
			
		}
		
		return true;
		
	}

	@Override
	public void add(E elem)
	{
		insert(elem);
	}
	
	public void clear()
	{
		for(List<E> L : table)
		{
			L.clear();
		}
		
		load = 0;
	}

}
