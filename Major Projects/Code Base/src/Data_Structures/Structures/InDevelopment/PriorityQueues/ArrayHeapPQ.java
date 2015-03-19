package Data_Structures.Structures.InDevelopment.PriorityQueues;

import Data_Structures.ADTs.PriorityQueue;
import Data_Structures.Structures.InDevelopment.Heaps.ArrayHeap;

/**
 * 
 * @author Bryce Summers
 * 
 * Written on 11 - 23 - 2014.
 * 
 * FIXME : I should do some thinking about my organization of various heaps, PQ's and other structures.
 *
 * @param <E> the type of element used by this data structure.
 */


public class ArrayHeapPQ<E> extends ArrayHeap<PQNode<E>> implements PriorityQueue<E, ArrayHeapPQ<E>>
{

	@Override
	public void add(E elem, int priority) 
	{
		add(new PQNode<E>(elem, priority));
	}

	@Override
	public ArrayHeapPQ<E> add_static(E elem, int priority)
	{
		throw new Error("Immutable PQ insertion is not currently supported.");
	}

	@Override
	public E peekMin()
	{
		return super.peek_dominating().getElem();
	}
	
	public int peekMinPriority()
	{
		return super.peek_dominating().getPriority();
	}
	
	@Override
	public E delMin()
	{
		return super.extract_dominating().getElem();
	}
	
	
}
