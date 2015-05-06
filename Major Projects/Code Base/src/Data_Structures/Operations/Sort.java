package Data_Structures.Operations;

import Data_Structures.ADTs.Queue;
import Data_Structures.Structures.List;
import Data_Structures.Structures.InDevelopment.Heaps.ArrayHeap;

/*
 * Various Sorting functions.
 * Written by Bryce Summers, 12 - 23 - 2012.
 * Updated 5 - 29 - 2013 :  Included more documentation.
 * 							Implemented insertion sort.
 * 
 * Purpose : Allows for arrays of data to be sorted efficiently.
 * 			1. qsort (Quick sort).
 * 				- O(1) extra space.
 * 				- Unstable.
 * 				- Blazingly fast num_columns log(num_columns) ++.
 * 			2. msort (Merge sort).
 * 				- O(num_columns) extra space.
 * 				- num_columns log(num_columns) consistent time complexity.
 * 			3. isort (insertion sort).
 * 				- O(1) extra space.
 * 				- O(num_columns) time complexity for almost sorted lists.
 * 				- O(num_columns^2) time complexity in the worst case.
 * 				- Fastest for small arrays.
 * 
 * FIXME : Implement Sleep sort based on calling system calls to sleep.
 * 
 * FIXME : Perhaps I should implement some less efficient algorithms 
 *         like sequential sort in a separate library just for fun.
 * 
 * FIXME : Implement Queue based merge sort: deq, deq, enq;
 */

public class Sort
{
	
	// Denotes the maximum size for arrays that will be insertion sorted.
	public final static int SMALL = 10;

	// -- Exposed Sorting functions.
	// FIXME : This method infinite loops.
	public static void qsort(int[] data)
	{
		qsort(data, 0, data.length);
	}
	
	/* 
	 * My personal implementation of Quick sort.
	 * O(num_columns*log(num_columns)) average running time.
	 * In place. Key order is not guaranteed to be preserved.
	 */
	private static void qsort(int[] data, int start_index, int end_index)
	{
		int len = end_index - start_index;
		
		// Because of local memory hardware architectures,
		// it is faster to insertion sort small arrays.
		if(len < SMALL)
		{
			isort(data, start_index, end_index);
			return;
		}
		
		// What is better than 1 random pivot? 3 random pivots! ~ Rob Simmons.
		int pivot1 = (int)(Math.random()*len + start_index);
		int pivot2 = (int)(Math.random()*len + start_index);
		int pivot3 = (int)(Math.random()*len + start_index);
		
		int pivot = median3(data, pivot1, pivot2, pivot3);
		int val = data[pivot];
		
		// Swap the pivot to the end of the array for now.
		swap(data, pivot, end_index - 1);
		pivot = end_index - 1;
				
		int unsorted_end = pivot - 1;
		int unsorted_beg = start_index;
		
		// Partition the array.
		for(int i = 0; i < len - 1; i ++)
		{
			if(data[unsorted_beg]  <= val)
			{
				unsorted_beg++;
			}
			else
			{
				swap(data, unsorted_beg, unsorted_end);
				unsorted_end--;
			}
		}
		
		// Swap the pivot into the middle.
		swap(data, unsorted_end + 1, pivot);
		pivot = unsorted_end + 1;
		
		// Recursively sort the two partitions.
		qsort(data, start_index, pivot);
		qsort(data, pivot + 1, end_index);
	
	}
	

	@SuppressWarnings("rawtypes")
	public static void qsort(Comparable[] data)
	{
		qsort(data, 0, data.length);
	}
	
	// My personal implementation of Quick sort.
	// O(num_columns*log(num_columns)) average running time.
	// In place. Key order is not guaranteed to be preserved.
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void qsort(Comparable[] data, int start_index, int end_index)
	{
		int len = end_index - start_index;
		
		// Because of local memory hardware architectures,
		// it is faster to insertion sort small arrays.
		if(len < SMALL)
		{
			isort(data, start_index, end_index);
			return;
		}
		
		// What is better than 1 random pivot? 3 random pivots! ~ Rob Simmons.
		int pivot1 = (int)(Math.random()*len + start_index);
		int pivot2 = (int)(Math.random()*len + start_index);
		int pivot3 = (int)(Math.random()*len + start_index);
		
		int pivot = median3(data, pivot1, pivot2, pivot3);
		
		// Swap the pivot to the end of the array for now.
		swap(data, pivot, end_index - 1);
		pivot = end_index - 1;
		
		Comparable val = data[pivot];
		int unsorted_end = pivot - 1;
		int unsorted_beg = start_index;
		
		// Partition the array.
		for(int i = 0; i < len - 1; i ++)
		{
			if(data[unsorted_beg].compareTo(val) < 0)
			{
				unsorted_beg++;
			}
			else
			{
				swap(data, unsorted_beg, unsorted_end);
				unsorted_end--;
			}
		}
		
		// Swap the pivot into the middle.
		swap(data, unsorted_end + 1, pivot);
		pivot = unsorted_end + 1;
		
		// Recursively sort the two partitions.
		qsort(data, start_index, pivot);
		qsort(data, pivot + 1, end_index);

	}
	
	// Public bounds checked version.
	public static void msort(int[] data)
	{
		msort(data, 0, data.length);
	}
	
	// My personal implementation of merge sort.
	// In place, O(num_columns*log(num_columns)) running time.
	// Key order is guaranteed to be preserved. 
	private static void msort(int[] data, int start_index, int end_index)
	{
		int len = end_index - start_index;
		
		// Because of local memory hardware architectures,
		// it is faster to insertion sort small arrays.
		if(len < SMALL)
		{
			isort(data, start_index, end_index);
			return;
		}
		
		int mid_index = (start_index + end_index)/2;
		
		// First sort the fist half.
		msort(data, start_index, mid_index);
		
		// Second sort the second half.
		msort(data, mid_index, end_index);
		
		// Merge the two sorted halves.
		merge(data, start_index, mid_index, end_index);
		
	}
	
	// Public bounds checked version.

	@SuppressWarnings("rawtypes")
	public static void msort(Comparable[] data)
	{
		msort(data, 0, data.length);
	}
	
	// My personal implementation of merge sort.
	// In place, O(num_columns*log(num_columns)) running time.
	// Key order is guaranteed to be preserved. 


	@SuppressWarnings("rawtypes")
	private static void msort(Comparable[] data, int start_index, int end_index)
	{
		int len = end_index - start_index;
		
		// Because of local memory hardware architectures,
		// it is faster to insertion sort small arrays.
		if(len < SMALL)
		{
			isort(data, start_index, end_index);
			return;
		}
		
		int mid_index = (start_index + end_index)/2;
		
		// First sort the fist half.
		msort(data, start_index, mid_index);
		
		// Second sort the second half.
		msort(data, mid_index, end_index);
		
		// Merge the two sorted halves.
		merge(data, start_index, mid_index, end_index);
		
	}
	
	// Exposed bounds checked version.
	public static void isort(int[] data)
	{
		isort(data, 0, data.length);
	}
	
	// Internal arbitrary implementation.
	private static void isort(int[] data, int start_index, int end_index)
	{
		for(int sorted = start_index + 1; sorted < end_index; sorted++)
		{
			// Extract the new element.
			int unsorted_elem = data[sorted];
			
			// Swap the element down into a sorted position.
			for(int replace = sorted - 1; replace >= start_index; replace--)
			{
				int sorted_elem = data[replace];
				
				// If position not yet found.
				if(unsorted_elem < sorted_elem)
				{
					swap(data, replace, replace + 1);
					continue;
				}
				
				// Position has been found.
				break;
			}
		}
	}
	
	// Exposed bounds checked version.
	@SuppressWarnings("rawtypes")
	public static void isort(Comparable[] data)
	{
		isort(data, 0, data.length);
	}
	
	// Internal arbitrary implementation.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void isort(Comparable[] data, int start_index, int end_index)
	{
		for(int sorted = start_index + 1; sorted < end_index; sorted++)
		{
			// Extract the new element.
			Comparable unsorted_elem = data[sorted];
			
			// Swap the element down into a sorted position.
			for(int replace = sorted - 1; replace >= start_index; replace--)
			{
				Comparable sorted_elem = data[replace];
				
				// If position not yet found.
				if(unsorted_elem.compareTo(sorted_elem) < 0)
				{
					swap(data, replace, replace + 1);
					continue;
				}
				
				// Position has been found.
				break;
			}
		}	
	}
	
	// Heap sort. Uses an array heap.
	public static void hsort(int[] data)
	{
		int len = data.length;
		
		ArrayHeap<Integer> heap = new ArrayHeap<Integer>(len);
		for(int i : data)
		{
			heap.add(i);
		}
		
		for(int i = 0; i < len; i++)
		{
			data[i] = heap.extract_dominating();
		}		
	}
	
	/**
	 * This is a solitaire inspired sorting algorithm that can efficiently find the 
	 * largest increasing sub sequence.
	 */
	public static void patienceSort()
	{
		throw new Error("Not yet Implemented");
	}
	
	
	// -- Internal Helper functions.
	
	// A standard function to swap elements inside of an array.
	public static void swap(int[] data, int index_1, int index_2)
	{
		int temp	  = data[index_1];
		data[index_1] = data[index_2];
		data[index_2] = temp;
	}
	
	// A standard function to swap elements inside of an array.
	public static void swap(Object[] data, int index_1, int index_2)
	{
		Object temp	  = data[index_1];
		data[index_1] = data[index_2];
		data[index_2] = temp;
	}
	
	// Returns the median of three integers in an array.
	private static int median3(int[] data, int i1, int i2, int i3)
	{
		int output;
		
		// Find the max of the first two.
		if(data[i1] > data[i2])
		{
			output = i1;
		}
		else
		{
			output = i2;
		}
		
		// Find the minimum of the two greater elements two.
		if(data[output] < data[i2])
		{
			return output;
		}
		else
		{
			return i2;
		}

	}
	
	// Returns the median of three integers in an array.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int median3(Comparable[] data, int i1, int i2, int i3)
	{
		int output;
		
		// Find the max of the first two.
		if(data[i1].compareTo(data[i2]) > 0)
		{
			output = i1;
		}
		else
		{
			output = i2;
		}
		
		// Find the minimum of the two greater elements two.
		if(data[output].compareTo(data[i2]) < 0)
		{
			return output;
		}
		else
		{
			return i2;
		}

	}
	
	// FIXME : This function could be implemented with fewer array lookups.
	
	// Checks whether an array of integers is sorted in O(num_columns) time;
	public static boolean is_sorted(int[] data)
	{
		int end = data.length;
		
		for(int i = 1; i < end; i++)
		{
			if(!(data[i - 1] <= data[i]))
			{
				return false;
			}
		}
		
		// All comparisons are correct,
		// so the array must be sorted.
		return true;
	}
	
	// Checks whether an array is sorted in O(num_columns) time;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean is_sorted(Object[] data)
	{
		int end = data.length;
		
		for(int i = 1; i < end; i++)
		{
			Comparable val1 = (Comparable)data[i - 1];
			Comparable val2 = (Comparable)data[i];
			if(!((val1).compareTo(val2) <= 0))
			{
				return false;
			}
		}
		
		// All comparisons are correct,
		// so the array must be sorted.
		return true;
	}
	
	// Merges two sorted arrays in O(num_columns) time.
	// Uses O(num_columns) memory as well.
	// FIXME: This could be improved.
	public static void merge(int[] data, int start_index, int mid_index, int end_index)
	{
		int len = end_index - start_index;
		
		// Now combine the two sorted halves.
		int[] temp = new int[len];
		
		int p1 = start_index;
		int p2 = mid_index;
		
		for(int i = start_index; i < end_index; i++)
		{
			
			if(p2 == end_index)
			{
				temp[i - start_index] = data[p1];
				p1++;
			}
			else if(p1 == mid_index || data[p1] > data[p2])
			{
				temp[i - start_index] = data[p2];
				p2++;
			}
			else
			{
				temp[i - start_index] = data[p1];
				p1++;	
			}
		}
				
		// Copy the new sorted array into the old array.
		for(int i = start_index; i < end_index; i++)
		{
			data[i] = temp[i - start_index];
		}
		
		return;
	}
	
	// Merges two sorted arrays in O(num_columns) time.
	// Uses O(num_columns) memory as well.
	// FIXME: This could be improved.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void merge(Comparable[] data, int start_index, int mid_index, int end_index)
	{
		int len = end_index - start_index;
		
		// Now combine the two sorted halves.
		Comparable[] temp = new Comparable[len];
		
		int p1 = start_index;
		int p2 = mid_index;
		
		for(int i = start_index; i < end_index; i++)
		{
			
			if((p2 == end_index))
			{
				temp[i - start_index] = data[p1];
				p1++;
			}
			else if((p1 == mid_index) || data[p1].compareTo(data[p2]) > 0)
			{
				temp[i - start_index] = data[p2];
				p2++;
			}
			else
			{
				temp[i - start_index] = data[p1];
				p1++;	
			}
		}
				
		// Copy the new sorted array into the old array.
		for(int i = start_index; i < end_index; i++)
		{
			data[i] = temp[i - start_index];
		}
		
		return;
	}
	
	
	// FIXME : Test this out and think about whether to make it in terms of an abstract data type instead of a list.
	// Think about reimplementing Lists sort function in terms of this function.
	public <E extends Comparable<E>> List<E> msort(Queue<List<E>> Q)
	{
		while(Q.size() > 1)
		{
			Q.enq(merge(Q.deq(), Q.deq()));
		}
		
		return Q.deq();
	}
	
	// Destroys L1 and L2.
	private <E extends Comparable<E>> List<E> merge(List<E> L1, List<E> L2)
	{
		List<E> output = new List<E>();
		
		while(!L1.isEmpty() && !L2.isEmpty())
		{
			E e1 = L1.getFirst();
			E e2 = L2.getFirst();
			
			if(e1.compareTo(e2) <= 0)
			{
				output.add(e1);
				L1.pop_front();
			}
			else
			{
				output.add(e2);
				L2.pop_front();
			}
		}
		
		if(L1.isEmpty())
		{
			output.destructiveAppend(L2);
		}
		else
		{
			output.destructiveAppend(L1);
		}
		
		return output;
	}
	
}
