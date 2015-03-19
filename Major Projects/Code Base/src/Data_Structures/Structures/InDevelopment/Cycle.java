package Data_Structures.Structures.InDevelopment;

import util.interfaces.Function;

/*
 * The Cycle structure.
 * 
 * Conceived by Bryce Summers on 7 - 28 - 2013.
 * 
 * Purpose : Cycle classes should specify a state and a function that specifies how o transform a state into another state.
 * 
 * This class defines cycles of numbers. The operations should be exhibit closure under a computationally finite domain, such as the Java 32 bit integers.
 * 
 * Uses :
 * 		It should be used to create pseudo random number generators.
 * 
 * 		This class should implement the Tortoise and the Hare algorithm to determine the length of the cycle.
 * 
 *		This Data structure can also be used to find steady states, Markov chains, and all sorts of other cool stuff.
 *
 *		This class is also remarkably type safe, because no arrays or other data needs to be generated internally.
 *
 *
 *		This class could be extended to provided support for Special Bryce Lists that loop, or have multiple paths.
 *
 *		This class can be used to with number theory with modular arithmetic cycles and other stuff.
 *
 *		I could extend this to a bidirectional cycle that has a reversible state mutation function.
 */

public class Cycle<T>
{
	
	// -- Local data.

	// The current state of a cycle.
	private T state;

	// A function that maps a state to a new state.
	private Function<T, T> next;
	
	// -- Cached data.
	Integer size = null;

	
	public Cycle(Function<T, T> f, T initial_state)
	{
		next = f;
		state = initial_state;
	}
	
	
	// -- Data retrieval (with some mutation side effects) methods.
	
	public T getState()
	{
		return state;
	}

	public T getNextState()
	{
		cycle();
		return state;
	}
	
	// -- Mutation methods.
	
	public void cycle()
	{
		state = next.eval(state);
	}
	
	// Evaluates the next state for any given input state.
	public T cycle(T state_in)
	{
		return next.eval(state_in);
	}
	
	// Evaluates the next state for any given input state.
	public T cycle(T state_in, int times)
	{
		// We cannot cycle backwards, so negative values are taboo.
		if(times < 0)
		{
			throw new Error("Cycles do not know how to go backwards yet!");
		}
		
		// Start at the given state.
		T output = state_in;
		
		// Iterate the correct number of times.
		for(int i = 0; i < times; i++)
		{
			output = next.eval(output);
		}
		
		// Return the output. :)
		return output;
	}
	
	// Returns the size of this cycles path, until the cycle loops.
	public int size()
	{
		if(size == null)
		{
			size = computeSize(state);
		}
		
		return size;
	}
	
	// -- Cycle computation provided by the Tortoise and the Hare algorithm.
	// ENSURES : Updates the size variable to the size the path until this cycle goes back onto itself.
	// FIXME : Update wording, and also think about whether this algorithm works in all cases.
	private int computeSize(T state)
	{		
		int size = 0;
		
		T tortoise = state;
		T hare = state;
		
		/* We know the size will be at least 1...
		 * ... so we will use a do, while loop.
		 */
		
		do
		{
			tortoise = cycle(tortoise, 2);
			hare	 = cycle(hare);
			size++;
		}
		while(!tortoise.equals(hare));
		
		return size;
	}

}
