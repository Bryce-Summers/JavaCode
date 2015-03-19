package Data_Structures.Operations;

/*
 * Search Capabilities.
 * Written by Bryce Summers, 12 - 23 - 2012.
 * 
 * Purpose: Allows my data structures to be searchable.
 * 
 * http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/Arrays.java#Arrays.binarySearch%28java.lang.Object%5B%5D%2Cint%2Cint%2Cjava.lang.Object%29
 * 
 * Left inclusive, right exclusive for all functions.
 * 
 */

public class Search
{

	// Linear search an entire array with proper bounds.
	public static int lin(int[] data, int val)
	{
		return lin(data, val, 0, data.length);
	}
	
	// Linear search, Returns the index of the element if found.
	private static int lin(int[] data, int val, int start_index, int end_index)
	{
		// [Start_index, end_index).
		for(int i = start_index; i < end_index; i++)
		{
			if(data[i] == val)
			{
				return i;
			}
		}
	
		// Not Found.
		return -1;
	}

	// Returns the index of the value nearest to the given value.
	public static int bin_near(int[] data, int val)
	{
		return bin(data, val, 0, data.length, true);
	}
	
	public static int bin(int[] data, int val)
	{
		return bin(data, val, 0, data.length, false);
	}
	
	// Binary search for integers.
	// Returns the index of the nearest value if the given flag is true.
	private static int bin(int[] data, int val, int start_index, int end_index, boolean return_nearest)
	{
		int lower 	= start_index;
		int upper 	= end_index;
		
		
		while(lower < upper)
		{
		
			int mid = (lower + upper)/2;
			
			if(data[mid] == val)
			{
				return mid;
			}
			
			if( data[mid] > val)
			{
				upper = mid;
			}
			else
			{
				lower = mid + 1;
			}
		}
		
		if(return_nearest)
		{
			if( lower > start_index && lower < end_index &&
				val - data[lower - 1] < data[lower] - val)
			{
				return lower - 1;
			}
			return lower;
		}
		return -1;
	}
	
	// Binary search for an Object that has the desired key.
	public static int bin(Object[] data, Object key)
	{
		return bin(data, key, 0, data.length);
	}
	
	// Binary search for Objects that implement comparable.

	@SuppressWarnings("unchecked")
	public static int bin(Object[] data, Object key, int start_index, int end_index)
	{
		int lower 	= start_index;
		int upper 	= end_index;
	
		
		while(lower < upper)
		{
			int mid			  = (lower + upper)/2;
			
			// Compare the keys of the current Object with the key.
			@SuppressWarnings("rawtypes")
			Comparable midVal = (Comparable)data[mid];
			int compare 	  = midVal.compareTo(key);
			
			if(compare == 0)
			{
				return mid;
			}
			
			if( compare > 0)
			{
				upper = mid;
			}
			else
			{
				lower = mid + 1;
			}
			
		}
		
		return -1;
	}
	
}
