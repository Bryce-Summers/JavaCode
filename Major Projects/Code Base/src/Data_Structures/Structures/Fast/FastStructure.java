package Data_Structures.Structures.Fast;

/*
 * FastStructure SuperClass.
 * 
 * All Fast Structures should extend the FastStructure class.
 * 
 * Written by Bryce Summers on 5 - 16 - 2014.
 * 
 * Purpose : Fast structures try to optimize speed by caching unused nodes and recycling them to prevent extra mallocs.
 * 
 * WARNING : This yard is currently only meant for sequential use, do not try using it in parallel.
 * 			 We could probably remove this stipulation in the future through multiple tracks in the yard.
 * 
 * Dr. Warme recommends having separate pools for each thread. He did not seem to know of a more elegant solution.
 */

public abstract class FastStructure
{
	// -- interface Functions.

	public abstract boolean isEmpty();
	
	// Points to the last node in the yard, a global recycling effort for nodes.
	private static FastNode yard_head = null;
	
	private static int yard_size = 0;
	private static int max_yard_size = 0;
	
	// -- Helper functions.
	
	// Returns a fresh node either from the yard, or from the heap. 
	protected static FastNode newNode()
	{
		if(yard_head == null)
		{
			return new FastNode();
		}
		
		return yard_pop();
	}
	
	protected static FastNode yard_pop()
	{
		if(yard_head == null)
		{
			throw new Error("Cannot Pop from empty yard!");
		}
		
		FastNode output = yard_head;
		yard_head		= yard_head.link;
		
		yard_size--;
		
		return output;
	}

	// Guaranteed to transfer the given node to the yard,
	// and sever all possible links to the outside world.
	protected static void yard_push(FastNode node)
	{
		node.data = null;
		
		// Handle yard full.
		if(yard_size == max_yard_size)
		{
			node.link = null;
			return;
		}
		
		node.link = yard_head;
		yard_head = node;
		
		yard_size++;
	}
	
	
	/* -- Yard management functions. -- */
	
	// Sets the maximum number of nodes that will be stored in the yard.
	// Negative value allows the yard to increase without bound.
	public static void setYardSize(int size)
	{
		max_yard_size = size;
	}
	
	/* Mallocs the input number of nodes or enough to fill the yard.
	 * Input < 0 --> fills the yard the whole way.
	 * Input < 0 and max_yard_size < 0 --> throws an error, because we cannot malloc an infinite amount of memory.
	 */
	public static void fillYard(int size)
	{
		// Normal case.
		if(size >= 0)
		{
			for(int i = 0; i < size && i < max_yard_size; i++)
			{
				yard_push(new FastNode());
			}
			return;
		}
		
		// size < 0 && max_yard_size < 0 case.
		if(max_yard_size < 0)
		{
			throw new Error("Cannot add an infinite number of nodes to the yard!");
		}
		
		// Proceed to fill the yard to the max yard size.
		forceYardToGivenSize(max_yard_size);
	}
	
	// Forces the yard to try to contain the exactly 'size' nodes.
	// Will override the maximum yard limit if neccesary.
	public static void forceYardToGivenSize(int size)
	{
		if(size > max_yard_size)
		{
			max_yard_size = size;
		}
		
		while(yard_size < size)
		{
			yard_push(new FastNode());
		}
		
		while(yard_size > size)
		{
			yard_pop();
		}
	}
	
	// Removes this class's reference t its yard to allow for the yard nodes to be garbage collected.
	public static void emptyYard()
	{
		yard_head = null;
	}
	
	// Returns true iff the yard size is equal to the maximum yard size.
	public static boolean isYardFull()
	{
		return yard_size == max_yard_size;
	}
	
	// Returns true iff the yard size is equal to the maximum yard size.
	public static boolean isYardEmpty()
	{
		return yard_size == 0;
	}
	
}
