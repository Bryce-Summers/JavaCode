package Data_Structures.Structures.InDevelopment.PriorityQueues;

public class PQNode<E> implements Comparable<PQNode<E>>
{

	private int priority;
	private E elem;
	
	public PQNode(E elem, int p)
	{
		this.elem = elem;
		this.priority = p;
	}
	
	@Override
	public int compareTo(PQNode<E> o)
	{
		return priority - o.priority;
	}
	
	public E getElem()
	{
		return elem;
	}
	
	public int getPriority()
	{
		return priority;
	}

}
