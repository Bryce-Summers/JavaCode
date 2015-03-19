package Data_Structures.Structures;

import util.Genarics;

/*
 * Pair class.
 * Written by Bryce Summers on 5 - 23 - 2013.
 * 
 * Purpose : 1. Useful in dictionaries.
 * 
 * 
 * Note : Pairs are mutable, the data inside of them is mutable as well.
 */


// FIXME : Integrate this with the hashing pairs and Pairable.
// To integrate them, probably the only thing that needs to be done is a change in hash value.
// I think this might have been done.

public class Pair<Key, Val>
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
	
	// An efficient hash code.
	public int hashCode()
	{
		int h1;
		
		if(key == null)
		{
			h1 = 0;
		}
		else
		{
			h1 = key.hashCode();
		}
		
		int h2;
		if(val == null)
		{
			h2 = 0;
		}
		else
		{
			h2 = val.hashCode();
		}
		
		// The Magic number is a large prime number within the range of a java integer.
		// Review number theory and the Euler Tortient function to find out about why this generates a good spread of values.
		return h1 + h2 * 372463307;
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
	
	protected void setVal(Val v_new)
	{
		val = v_new;
	}
}
