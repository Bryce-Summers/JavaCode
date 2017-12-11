package structures;

import Data_Structures.Structures.UBA;

public class QueryList<E extends RayQueryable>
{
	
	public UBA<E> list;
	
	public QueryList(UBA<E> array)
	{
		if(array == null)
		{
			array = new UBA<E>();
		}
		list = array;
	}

	public void append(UBA<E> array)
	{
		list.append(array);
	}
	
	public void append(QueryList<E> other)
	{
		list.append(other.list);
	}
	
	// Allows for a list of rayQuerable objects to be queried in aggregate.
	// In general this is inefficient.
	public E rayquery_min(RayQuery rayQuery)
	{
		boolean found = false;
		for(E elem : list)
		{
			found = found || elem.rayQueryMin(rayQuery);
		}
		
		if(found)
		{
			return (E)rayQuery.obj;
		}
		
		return null;
	}
	
	public E getRandomElement()
	{
		int index = (int) (Math.random()*list.size());
		E elem = list.get(index);
		return elem;
	}
}