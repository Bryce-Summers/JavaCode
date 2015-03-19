package Data_Structures.Structures.HashingClasses;

import java.util.Iterator;

import util.Genarics;
import Data_Structures.ADTs.Bunch2;
import Data_Structures.Structures.HashTable;
import Data_Structures.Structures.List;

/*
 * Associated Array. Dictionary. "Map"
 * Written by Bryce Summers on 5 - 23 - 2013.
 * 
 * Purpose : This is an implementation of a dictionary that can use any types for keys and values.
 * 
 * Capabilities :	1. O(1) insertion.
 * 					2. O(1) key -> value lookup. (Average case).
 * 					3. O(1) removal.
 * 
 * Note: The pair class should hash pairs to the hash code of their keys.
 * 
 * These are also known as maps in the data structure literature.
 */

// FIXME : Rename this to "Map".

public class AArray<Key, Value> extends HashTable<Pair<Key, Value>>
{
	
	// -- Constructors

	public AArray()
	{
		super();
	}
	
	public AArray(int starting_size)
	{
		super(starting_size);
	}
	
	public void update(Key key, Value val)
	{
		insert(key, val);
	}
	
	// Removes the given key from the table.
	// Returns true iff the table has been mutated.
	public boolean remove_key(Key key)
	{
		List<Pair<Key, Value>> chain = findChain(key);
		Pair<Key, Value> p = find(chain, key);
		
		// Key not found.
		if(p == null)
		{
			return false;
		}		
		
		// Remove the key from the correct chain.
		remove(chain, key);
		proccessRemoval();
		return true;
	}
	
	// Inserts the given key and value into the hash table.
	// Returns true if the hash table has been changed.
	// REQUIRES : key != null. val can be either null or non null.
	
	public boolean insert(Key key, Value val)
	{
		List<Pair<Key, Value>> chain = findChain(key);
		Pair<Key, Value> p = find(chain, key);
		
		// Key found in the table, update its value.
		if(p != null)
		{
			
			p.updateVal(val);
			return true;
		}

		// Key not found.

		// We also support null values now. The default association is "no association" rather than null.
				
		Pair<Key, Value> input = new Pair<Key, Value>(key, val);
		chain.add(input);
		
		proccessInsertion();
		return true;
	}

	/**
	 * 
	 * @param key the key we are searching for.
	 * @return true iff the given key is associated with a value.
	 */
	public boolean contains_key(Key key)
	{
		return find(key) != null;
	}
	
	/*
	 *  Returns null iff the given key was not found,
	 *  or the value associated with the key is null.
	 */
	public Value lookup(Key key)
	{
		
		// Find the pair.
		Pair<Key, Value> p = find(key);

		if(p != null)
		{
			return p.getVal();
		}

		// Element not found.
		return null;

	}
	
	public List<Key> getKeys()
	{
		List<Key> output = new List<Key>();
		
		for(Pair<Key, Value> p : this)
		{
			if(p == null)
			{
				output.add(null);
				continue;
			}
			
			output.add(p.getKey());
		}

		return output;
	}
	
	// Returns a list of the non null values in this associative array.
	public List<Value> getValues()
	{
		List<Value> output = new List<Value>();
		
		for(Pair<Key, Value> p : this)
		{
			if(p == null)
			{
				output.add(null);
				continue;
			}
			
			output.add(p.getVal());
		}

		return output;
	}
	
	// -- internal helper functions.
	
	private List<Pair<Key, Value>> findChain(Key key)
	{
		// Compute the hash value for this key.
		int index = hash_key(key);
		return table[index];
	}
	
	// Returns a pair with the given key iff it exists in the hash table.
	private Pair<Key, Value> find(Key key)
	{
		List<Pair<Key, Value>> chain = findChain(key);
		return find(chain, key);
	}

	// Returns the pair in the given list that has the given key.
	private Pair<Key, Value> find(List<Pair<Key, Value>> chain, Key key)
	{
		
		for(Pair<Key, Value> p : chain)
		{
			if(keys_equal(p.getKey(), key))
			{
				return p;
			}
		}

		// Not found;
		return null;
	}
	
	private void remove(List<Pair<Key, Value>> chain, Key key)
	{
		Iterator<Pair<Key, Value>> iter = chain.iterator();
		
		while(iter.hasNext())
		{
			Pair<Key, Value> p = iter.next();
			if(p.getKey().equals(key))
			{
				iter.remove();
				return;
			}
		}
		
		// Key not found.
		throw new Error("Removal should not be called in vain.");
	}

	// Note : Pairs should have the same hash codes as their keys.
	private int hash_key(Key key)
	{
		Pair<Key, Value> bogus = new Pair<Key, Value>(key, null);
				
		return super.hash(bogus);
	}

	// Key equality is implemented via the use of the Genarics class.
	Genarics<Key> ge_Key = new Genarics<Key>();
	private boolean keys_equal(Key elem1, Key elem2)
	{
		return ge_Key.xequal(elem1, elem2);
	}
	
	// It is necessary to override the clone method in all convoluted subtypes.
	
	@Override
	public AArray<Key, Value> clone()
	{
		AArray<Key, Value> output = new AArray<Key, Value>(table.length);
		
		for(Pair<Key, Value> elem : this)
		{
			output.insert(elem.clone());
		}
		
		return output;
		
	}
	
	public List<Bunch2<Key, Value>> getPairs()
	{
		List<Bunch2<Key, Value>> output = new List<Bunch2<Key, Value>>();
		
		for(Pair<Key, Value> p : this)
		{
			output.add(new Bunch2<Key, Value>(p.getKey(), p.getVal()));
		}
		
		return output;
	}

}
