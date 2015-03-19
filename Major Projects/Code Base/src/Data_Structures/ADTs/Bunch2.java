package Data_Structures.ADTs;

/*
 * bunch 2 data type.
 * 
 * Bunches are unpackaged, unindexed collections of data.
 * 
 * Bunch2 types join 2 data types together into 1 type.
 */

public class Bunch2<T1, T2>
{

	final T1 val1;
	final T2 val2;
	
	public Bunch2(T1 input1, T2 input2) 
	{
		val1 = input1;
		val2 = input2;
	}
	
	public T1 getType1()
	{
		return val1;
	}
	
	public T2 getType2()
	{
		return val2;
	}
	
	// These are here for making code more readable, 
	// but they may be used for evil in the wrong syntactic hands.
	public T1 getKey()
	{
		return val1;
	}
	
	public T2 getVal()
	{
		return val2;
	}

}
