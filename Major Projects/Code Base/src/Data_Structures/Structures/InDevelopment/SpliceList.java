package Data_Structures.Structures.InDevelopment;

import Data_Structures.Structures.List;
import Data_Structures.Structures.InDevelopment.Trees.AVL;

/*
 * This List structure will make it easy to rapidly delete and insert arrays of elements 
 * from arbitrary locations in the list.
 * 
 * It will be implemented with a linked list and a tree of iterators that will allow modification 
 * access to the list.
 */

// See http://stackoverflow.com/questions/10134836/complexity-of-stdlistsplice-and-other-list-containers.

// I may have to consider trade offs between size() and splice() efficiency.

// FIXME : I probably should not implement comparable E.
public class SpliceList<E extends Comparable<E>>
{
	List<E> data;
	AVL<E> iters;
}
