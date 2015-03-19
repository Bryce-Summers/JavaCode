package util;

import java.util.Iterator;

import java.lang.reflect.Array;


/*
 * The Genarics class, written by Bryce Summers 12 - 30 - 2012
 * 
 * Purpose: provides functions for doing annoying things
 * like creating generic 2 dimensional arrays.
 */

// NOTE : (T)new Object[size] can be used for non autoboxed classes. For an autoboxed class I still need to use reflection. 

public class Genarics<T>
{
	
	@SuppressWarnings("unchecked")
	public T[] Array(int len, Class<T> clazz)
	{
		T[] array		= (T[]) Array.newInstance(clazz, len);
		
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public T[] Array(int len, T example)
	{
		
		Class<T> elem   = (Class<T>) example.getClass();
		T[] array		= (T[]) Array.newInstance(elem, len);
		
		return array;
	}
	

	public Comparable<T>[] Comparable_Array(int len)
	{
		@SuppressWarnings("unchecked")
		Comparable<T>[] array		= (Comparable[]) Array.newInstance(Comparable.class, len);
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public T[][] Array_2d(int w, int h, T example)
	{
		// First get the class of T
		Class<T> elem		= (Class<T>) example.getClass();
		Class<T> elem_array = (Class<T>) Array.newInstance(elem, 0).getClass();
		T[][] array = (T[][]) Array.newInstance(elem_array, w);
		for(int c = 0; c < w; c++)
		{
			array[c] = (T[]) Array.newInstance(elem, h);
		}
		
		return array;
	}
	
	// A Null - pointer safe equality checking function for types.
	public boolean xequal(T elem1, T elem2)
	{
		boolean n1, n2;

		n1 = elem1 == null;
		n2 = elem2 == null;

		if(n1 && n2)
		{
			return true;
		}

		if(n1 || n2)
		{
			return false;
		}
		return elem1.equals(elem2);
	}
	
	// Returns true if and only if o1.equals(o2).
	public boolean xequal(Iterable<T> o1, Object o2)
	{		
		if(o1 == null && o2 == null)
		{
			return true;
		}
		
		if(o1 == null || o2 == null)
		{
			return false;
		}
		
		if(o1.getClass() != o2.getClass())
		{
			return false;
		}
	
		Iterator<T> iter1 = o1.iterator();
		@SuppressWarnings("unchecked")
		Iterator<T> iter2 = ((Iterable<T>) o2).iterator();
		
		// - Compare all elements for equality.
		
		while(iter1.hasNext())
		{
			if(!iter2.hasNext())
			{
				return false;
			}
			
			T elem1 = iter1.next();
			T elem2 = iter2.next();
			
			if(!elem1.equals(elem2))
			{
				return false;
			}			
		}
		
		if(iter2.hasNext())
		{
			return false;
		}
				
		return true;
		
	}
	
}