package Data_Structures.ADTs;

// Written by Bryce Summers on 12 - 22 - 2013.

// Related to Priority Queues. Specifies structures that maintain the heap property.

public interface Heap<E extends Comparable<E>>
{
	/* http://en.wikipedia.org/wiki/Heap_(data_structure).
	create-heap: create an empty heap
	heapify: create a heap out of given array of elements
	find-max or find-min: find the maximum item of a max-heap or a minimum item of a min-heap, respectively (aka, peek)
	delete-max or delete-min: removing the root node of a max- or min-heap, respectively
	increase-key or decrease-key: updating a key within a max- or min-heap, respectively
	insert: adding a new key to the heap
	merge: joining two heaps to form a valid new heap containing all the elements of both.
	meld(h1,h2): Return the heap formed by taking the union of the item-disjoint heaps h1 and h2. Melding destroys h1 and h2.
	size: return the number of items in the heap.
	isEmpty(): returns true if the heap is empty, false otherwise.
	buildHeap(list): builds a new heap from a list of keys.
	ExtractMin(): Returns the node of minimum value after removing it from the heap
	Union(): Creates a new heap by joining two heaps given as input.
	Shift-up: Move a node up in the tree, as long as needed (depending on the heap condition: min-heap or max-heap)
	Shift-down: Move a node down in the tree, similar to Shift-up
	*/
}
