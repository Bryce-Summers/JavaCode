package Data_Structures.Structures.HashingClasses;

import util.Genarics;

/*
 * Pair class.
 * Written by Bryce Summers on 5 - 23 - 2013.
 * 
 * Purpose : 1. Useful in dictionaries.
 * WARNING!!!! Do not change the hash code function for these pairs, or else Associative arrays will break.
 * 
 * Note : Pairs are mutable, the data inside of them is mutable as well.
 */

// -- package accessible hashing pairs.
// Not intended for external use.
class Pair<Key, Val>
{

	// -- Local data.
	private Key key;
	private Val val;
	
	// -- Constructors.
	public Pair(Key input1, Val input2)
	{
		key = input1;
		val = input2;
	}
	
	public Key getKey()
	{
		return key;
	}
	
	public Val getVal()
	{
		return val;
	}
	
	public void updateVal(Val input)
	{
		val = input;
	}
	
	public int hashCode()
	{
		
		if(key == null)
		{
			return 0;
		}
		
		return key.hashCode();
	}
	
	// Key equality is implemented via the use of the Genarics class.
	Genarics<Key> ge_Key   = new Genarics<Key>();
	Genarics<Val> ge_Value = new Genarics<Val>();
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		
		@SuppressWarnings("unchecked")
		Pair<Key, Val> p = (Pair<Key, Val>)o;
				
		return ge_Key.xequal(key, p.key) && ge_Value.xequal(val, p.val);

	}
	
	public String toString()
	{
		if(key == null)
		{
			if(val == null)
			{
				return "[Empty Pair]";
			}
			
			return val.toString();
		}
		
		if(val == null)
		{
			return key.toString();
		}
		
		return "(" + key.toString() + ", " + val.toString() + ")";
	}
	
	public Pair<Key, Val> clone()
	{
		return new Pair<Key, Val>(key, val);
	}
	
}
