package Data_Structures.Structures;

/*
 * Written by Bryce Summers on 6 - 26 - 2014.
 * 
 * Purpose : This  allows integers to represent sets of booleans.
 * 
 * This class is encapsulated so that it can potentially be converted into a class that allows for arbitrary sized bit sets.
 */

public class BitSet
{
	// Private data.
	private int val;
	
	// -- Constructors.
	public BitSet()
	{
		val = 0;
	}
	
	public BitSet(String s)
	{
		val = new Integer(s);
	}
	
	private BitSet(int i)
	{
		val = i;
	}
	
	public void setBit(int index, boolean b)
	{
		if(index >= 32)
		{
			throw new Error("Does not support more than 32 bits!");
		}
	
		// FIXME : FInd a straight line solution.
		int mask = 1 << index;
		
		if(b)
		{
			val = val | mask;
			return;
		}
		
		val -= mask;		
	}
		
	// Given a number 0 through 7, returns whether this entity is associated with it.
	public boolean getBit(int index)
	{
		return (val & (1 << index)) != 0;
	}
	
	public String toString()
	{
		return val + "";
	}
	
	@Override
	public int hashCode()
	{
		return val;
	}
	
	public int toInt()
	{
		return val;
	}

	public BitSet AND(BitSet other)
	{
		return new BitSet(val & other.val);
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Integer)
		{
			return val == (Integer)o;
		}
		
		if(o instanceof BitSet)
		{
			return val == ((BitSet)o).val;
		}
		
		return false;
	}

}
