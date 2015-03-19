package Data_Structures.Structures.HashingClasses;

/*
 * Dictionary Class.
 * Written by Bryce Summers on 5 - 23 - 2013.
 * 
 * Purpose: This class is basically a retyping of an associated 
 * 			array class in the most common usage which is a dictionary
 * 			with string keys and some generic value type.
 */

public class Dict<E> extends AArray<String, E>
{

	// This should contain every constructor for Associative arrays.
	
	public Dict()
	{
		super();
	}
	
	public Dict(int starting_size)
	{
		super(starting_size);
	}
	
	// The clone method must be overriden in every subclass.
	
	@Override
	public Dict<E> clone()
	{
		Dict<E> output = new Dict<E>(table.length);
		
		for(Pair<String, E> elem : this)
		{
			output.insert(elem);
		}
		
		return output;
		
	}
	
}
