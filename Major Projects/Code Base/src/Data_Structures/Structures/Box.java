package Data_Structures.Structures;

/*
 * The box class.
 * 
 * Written by Bryce Summers on 4 - 30 - 2014.
 * 
 * Purpose : This class allows for different types to be passed inside of objects.
 * 
 * This allows for variables to be passed by reference into functions and the mutations will be visible to the calling function.
 * This allows for the equivalent behavior of passing a stack variable &temp into a function in C.'
 * 
 * Added a null constructor on 5 - 15 - 2014.
 */

public class Box<T>
{
	public T val = null;
	
	public Box(T val_input)
	{
		val = val_input;
	}
	
	// Null constructor.
	public Box()
	{
		
	}
	
}
