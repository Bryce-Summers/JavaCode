package Data_Structures;

import static util.Print.print;
import static util.testing.ASSERT;

import java.util.Iterator;

import util.Genarics;
import Data_Structures.ADTs.Queue;
import Data_Structures.ADTs.Stack;
import Data_Structures.Operations.Sort;
import Data_Structures.Structures.Data_Structure;
import Data_Structures.Structures.HashTable;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.SingleLinkedList;
import Data_Structures.Structures.UBA;
import Data_Structures.Structures.Fast.FastQueue;
import Data_Structures.Structures.Fast.FastStack;
import Data_Structures.Structures.Fast.FastStructure;
import Data_Structures.Structures.HashingClasses.Dict;
import Data_Structures.Structures.HashingClasses.Set;
import Data_Structures.Structures.InDevelopment.UnionFind;
import Data_Structures.Structures.InDevelopment.Heaps.ArrayHeap;
import Data_Structures.Structures.InDevelopment.Trees.AVL;
import Data_Structures.Structures.InDevelopment.Trees.Treap;
// - Useful testing functions.

/*
 * Data Structure testing class.
 * 
 * Written by Bryce Summers on 5 - 24 - 2013.
 * 
 * Purpose : Provides unit tests to recommend the correctness
 *			 of the data structures in my library.
 *
 *			 Provides unit tests to recommend the correctness
 *			 of my searching and sorting functions.
 *
 *	Methodology : Test, test, test.
 *
 *	Test implementation specific features and abstract data types.
 *
 * 5 - 24 - 2014 : Implemented Treap Specification testing.
 */

// FIXME : Test with null data.
public class aatesting
{
	// -- local constants.
	private final int TEST_SIZE = 1000;

	public aatesting()
	{
		test_List();
		test_UBA();
		
		// -- Hash Table classes.
		test_HashTable();
		test_AssociativeArrays();
		test_set();
		
		// -- Sort and Search.
		test_sort_and_search();
		
		test_Trees();
		
		test_singleLinkedList();
		
		// Abstract DataType testing.
		test_set(new AVL<Integer>());
		test_set(new HashTable<Integer>());
		
		test_fast_structures();
		
		test_heap();
		
		test_UnionFind();
		
		test_Treap();


		
		print("All Tests Passed!");
	}
	
	// -- Test the implementation of Treaps.
	
	private void test_Treap()
	{
		Treap<Integer> T_EMPTY, T_LEAF0, T_Current, T_last;
		
		// Empty Tree. should never change.
		T_EMPTY = new Treap<Integer>();
		Tree_Empty(T_EMPTY);
		
		// Leaf containing 0. Should never change.
		T_LEAF0 = T_EMPTY.add_static(0);
		Tree_Leaf0(T_LEAF0);
		
		// Check that Immutability is maintained!
		Tree_Empty(T_EMPTY);
		
		// -- Test removal Edge Cases.
		
		/*      0
		 *     / \
		 *   -2   2
		 *    \   /
		 *    -1 1
		 */
		
		Treap<Integer> T = T_EMPTY;
		T = T.add_static( 0, 3);
		T = T.add_static(-2, 2);
		T = T.add_static( 2, 2);
		T = T.add_static(-1, 1);
		T = T.add_static( 1, 1);
		
		// Correctness means it is a treap and it contains all of the specified elements.
		ASSERT(T.isTreap());
		ASSERT(T.contains(0));
		ASSERT(T.contains(-2));
		ASSERT(T.contains(2));
		ASSERT(T.contains(-1));
		ASSERT(T.contains(1));
		ASSERT(T.contains(100) == false);
		
		Treap<Integer> T2;
		
		// Remove nothing.
		T2 = T.remove_static(100);
		ASSERT(T2.isTreap());
		ASSERT(T2.contains(0));
		ASSERT(T2.contains(-2));
		ASSERT(T2.contains(2));
		ASSERT(T2.contains(-1));
		ASSERT(T2.contains(1));
		
		// Remove right leaf.
		T2 = T.remove_static(1);
		ASSERT(T2.isTreap());
		ASSERT(T2.contains(0));
		ASSERT(T2.contains(-2));
		ASSERT(T2.contains(2));
		ASSERT(T2.contains(-1));
		ASSERT(T2.contains(1) == false);

		// Remove left leaf.
		T2 = T.remove_static(-1);
		ASSERT(T2.isTreap());
		ASSERT(T2.contains(0));
		ASSERT(T2.contains(-2));
		ASSERT(T2.contains(2));
		ASSERT(T2.contains(-1) == false);
		ASSERT(T2.contains(1));
		
		// Remove right path.
		T2 = T.remove_static(-2);
		ASSERT(T2.isTreap());
		ASSERT(T2.contains(0));
		ASSERT(T2.contains(-2) == false);
		ASSERT(T2.contains(2));
		ASSERT(T2.contains(-1));
		ASSERT(T2.contains(1));
		
		// Remove left path.
		T2 = T.remove_static(2);
		ASSERT(T2.isTreap());
		ASSERT(T2.contains(0));
		ASSERT(T2.contains(-2));
		ASSERT(T2.contains(2) == false);
		ASSERT(T2.contains(-1));
		ASSERT(T2.contains(1));

		
		// Remove center.
		T2 = T.remove_static(0);
		ASSERT(T2.isTreap());
		ASSERT(T2.contains(0) == false);
		ASSERT(T2.contains(-2));
		ASSERT(T2.contains(2));
		ASSERT(T2.contains(-1));
		ASSERT(T2.contains(1));

		
		T_Current = T_EMPTY;
		T_Current = T_Current.add_static(0, 0);
		T_Current = T_Current.add_static(1, -1);
		T_Current = T_Current.add_static(2,  1);
		ASSERT(T_Current.isTreap());
		
		// -- Now test add(). (And contains()!).
		T_Current = T_EMPTY;
		T_last = T_Current;
		Tree_Empty(T_EMPTY);
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(T_last.contains(i) == false);
			T_Current = T_last.add_static(i);
			
			// Is Treap is Order n^2, but is O(1) when called on recursively
			// shared treaps do to dynamic programming.
			ASSERT(T_Current.isTreap());
			
			// Correctness.
			ASSERT(T_Current.contains(i) == true);
			// immutability.
			ASSERT(T_last.contains(i) == false);
			T_last = T_Current;
		}		
		
		// -- Test remove().
		
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			ASSERT(T_last.contains(i) == true);
			T_Current = T_last.remove_static(i);
			ASSERT(T_Current.isTreap());
			
			// Correctness.
			ASSERT(T_Current.contains(i) == false);
			// immutability.
			ASSERT(T_last.contains(i) == true);
			
			T_last = T_Current;
		}
		
		// Make sure the removal of non contained elements is done properly.
		ASSERT(T_Current.remove_static(5) == T_Current);
		ASSERT(T_Current.remove_static(0) == T_Current);
		
		if(true)
		throw new Error("split, merge, etc have not yet been tested");
		
		throw new Error("We need to test shallow copying and static deque operations for all of the stack, queue implementations.");
	}

	// -- Tree Immutability testing helper functions.
	private void Tree_Empty(Treap<Integer> T)
	{
		ASSERT(T.isEmpty());
		ASSERT(T.isTreap());
	}
	
	private void Tree_Leaf0(Treap<Integer> T)
	{
		ASSERT(T.isLeaf());
		ASSERT(T.isEmpty() == false);
		ASSERT(T.contains(0));
		ASSERT(T.contains(1) == false);
		ASSERT(T.isTreap());
	}

	private void test_List()
	{
		
		/*
		 * List Capabilities tested:
		 *	1.	Add()
		 *	2.	Rem()
		 *	3.	toArray()
		 *	4.	pop()
		 *	5.	push()
		 *	6.	enq()
		 *	7.	deq()
		 *	8.	size()
		 *	9.	isEmpty()
		 *	10.	clone()
		 *	11.	append()
		 *	12.	equals()
		 *	13.	iteration
		 *	14.	backwards iteration
		 *	15.	sort()
		 *	16. getFirst()
		 *	17. getLast()
		 *	18. Contains().
		 *
		 *	19. IterB functionality.
		 *		- current()
		 *		- hasCurrent()
		 *		- hasNext();
		 *		- hasPrevious();
		 *		- insertAfter();
		 *		- insertBefore();
		 *		- next();
		 *		- previous();
		 *		- remove();
		 *		- replace();
		 *		- reversal();
		 *
		 *	20. list.remove(elem).
		 *
		 *	21. list.destructiveAppend(list2).
		 */

		List<Integer> L = new List<Integer>();
		
		// -- Test Initial List State.
		ASSERT(L.isEmpty());
		ASSERT(L.size() == 0);
		
		// -- Add() - Rem().
		
		Integer[] A = new Integer[TEST_SIZE];
		
		// Test invariants and add().
		for(int i = 0; i < TEST_SIZE; i++)
		{
			L.add(i);
			A[i] = i;
			
			// Check first.
			ASSERT(0  == L.getFirst());

			// check last.
			ASSERT(i  == L.getLast());
			
			// check size.
			ASSERT(L.size() == i + 1);
			
			ASSERT(!L.isEmpty());
		}
		
		// Test toArray(), inclusion, and ordering.
		Integer[] LA = L.toArray();
				
		ASSERT(LA != null);
		ASSERT(L.size() == TEST_SIZE);
		
		// Check the Arrays for equality.
		ASSERT(equal(LA, A));
		
		// Test Removal.
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			ASSERT(i == L.getLast());
			ASSERT(0 == L.getFirst());
			
			ASSERT(!L.isEmpty());
			ASSERT(L.rem().equals(i));
			
			ASSERT(i == L.size());			
		}

		// Check that the list has reverted to its initial state.
		ASSERT(L.isEmpty() && L.size() == 0);
		
		for(Integer i : L)
		{
			i = i + 1;
			throw new Error("There should not be any iteration in an empty list.");
		}

		// -- STACK.
		
		// - Pushing and invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			L.push(i);
			A[i] = i;
			
			// Check first.
			ASSERT(0  == L.getFirst());

			// check last.
			ASSERT(i  == L.getLast());
			
			// check size.
			ASSERT(L.size() == i + 1);
			
			ASSERT(!L.isEmpty());
		}
		
		// Test toArray(), inclusion, and ordering.
		LA = L.toArray();
		
		ASSERT(LA != null);
		ASSERT(L.size() == TEST_SIZE);
		
		// Check the Arrays for equality.
		ASSERT(equal(LA, A));
		
		// - Pop() and Invariants.
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			ASSERT(i == L.getLast());
			ASSERT(0 == L.getFirst());
			ASSERT(!L.isEmpty());
			ASSERT(L.pop().equals(i));
			ASSERT(i == L.size());			
		}
		
		// Check that the list has reverted to its initial state.
		ASSERT(L.isEmpty() && L.size() == 0);

		// -- QUEUE.
		
		// - enqueing and invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			L.enq(i);
			A[i] = i;
			
			// Check first.
			ASSERT(0  == L.getFirst());

			// check last.
			ASSERT(i  == L.getLast());
			
			// check size.
			ASSERT(L.size() == i + 1);
			
			ASSERT(!L.isEmpty());
		}
		
		// Test toArray(), inclusion, and ordering.
		LA = L.toArray();
		
		ASSERT(LA != null);
		ASSERT(L.size() == TEST_SIZE);
		
		// Check the Arrays for equality.
		ASSERT(equal(LA, A));
		
		// - deq() and Invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(TEST_SIZE - 1 == L.getLast());
			ASSERT(i	== L.getFirst());
			
			ASSERT(!L.isEmpty());
			ASSERT(L.deq().equals(i));
			
			ASSERT(TEST_SIZE - 1 - i == L.size());			
		}
		
		// Check that the list has reverted to its initial state.
		ASSERT(L.isEmpty() && L.size() == 0);
		
		
		// -- Test Clone
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			L.add(i);
		}
		
		List<Integer> L2 = L.clone();
		
		Integer[] A2;
		
		A  = L .toArray();
		A2 = L2.toArray();
		
		ASSERT(equal(A, A2));
		ASSERT(A != A2);
		
		ASSERT(L.equals(L2));
		
		L.add(13);
		L2.add(13);
		
		ASSERT(L.equals(L2));
		
		L.rem();
		
		L2 = new List<Integer>();
		
		ASSERT(L2.isEmpty());
		ASSERT(!L.equals(L2));
		
		L2.append(L);
		ASSERT(L.equals(L2));
		
		A = new Integer[3];
		
		A[0] = 5;
		A[1] = 4;
		A[2] = 3;
		
		L.append(A);
		
		L2.add(5);
		L2.add(4);
		L2.add(3);
		
		ASSERT(L.equals(L2));
		
		// -- Test Contains().
		
		L = new List<Integer>();
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			L.push(i);
		}
		
		// This should be very slow, because it takes O(1) time scan the array.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(L.contains(i));
		}
		
		// -- Test Iteration.
		
		int i = 0;
		Iterator<Integer> iter  = L.iterator();
		Iterator<Integer >iter2 = L.getIter();
		
		while(iter.hasNext())
		{
			ASSERT(i == iter.next());
			i++;
		}
		
		i = 0;
		while(iter2.hasNext())
		{
			ASSERT(i == iter2.next());
			i++;
		}
		
		i = TEST_SIZE - 1;
		IterB<Integer> iter_backwards = L.getTailIter();
		while(iter_backwards.hasPrevious())
		{
			ASSERT(i == iter_backwards.previous());
			i--;
		}		
		
		L = new List<Integer>();
		
		// -- Test null iteration.
		
		iter  = L.iterator();
		iter2 = L.getIter();
		iter_backwards = L.getTailIter();
		
		ASSERT(L.isEmpty());
		ASSERT(!(iter.hasNext() || iter2.hasNext()));
		ASSERT(!iter_backwards.hasNext() && !iter_backwards.hasPrevious() );
		
		// -- Test List Sorting.
		for(i = 0; i < TEST_SIZE; i++)
		{
			L.add((int)(Math.random()*TEST_SIZE));
		}
		
		// Much more efficient than contains.
		L.sort();
		A = L.toArray();
		
		ASSERT(isSorted(A));
		
		// -- Test the multiple element constructor for lists.
		
		Integer[] A3 = {5,7,23,8,4,7,4};
		L = new List<Integer>(A3);
		
		ASSERT(equal(L.toArray(), A3));
		
		L = new List<Integer>(5, 7, 23, 8, 4, 7, 4);
		ASSERT(equal(L.toArray(), A3));
		
		
		/* -- Thoroughly test the IterB iteration capabilities.
		 *	IterB functionality.
		 *		- current()
		 *		- hasCurrent()
		 *		- hasNext();
		 *		- hasPrevious();
		 *		- insertAfter();
		 *		- insertBefore();
		 *		- next();
		 *		- previous();
		 *		- remove();
		 *		- replace();
		*/
		
		// Also make sure that iterators do not ever iterate through.
		L = new List<Integer>(1,2);
		
		IterB<Integer> iter3 = L.getIter();
		
		ASSERT(iter3.hasNext());
		ASSERT(iter3.next() == 1);
		ASSERT(iter3.hasPrevious() == false);
		iter3.insertBefore(0);
		ASSERT(iter3.current() == 0);
		ASSERT(iter3.hasPrevious() == false && L.getFirst() == 0);
		ASSERT(iter3.hasNext());
		ASSERT(iter3.next() == 1);
		ASSERT(iter3.current() == 1);
		iter3.replace(9);
		ASSERT(iter3.current() == 9 && iter3.hasPrevious() && iter3.hasNext());
		ASSERT(iter3.previous() == 0);
		iter3.next();
		ASSERT(iter3.next() == 2);
		iter3.insertAfter(10);
		ASSERT(iter3.current() == 10 && iter3.hasNext() == false && iter3.hasPrevious());
		ASSERT(L.getLast() == 10);
		ASSERT(iter3.previous() == 2);
		ASSERT(iter3.next() == 10);
		iter3.remove();
		ASSERT(iter3.current() == null);
		ASSERT(iter3.hasNext() == false && iter3.hasPrevious());
		ASSERT(iter3.previous() == 2);
		
		// Now clear the list and ensure that iterators from both sides do not discover reentrant data.
		L.clear();
		
		ASSERT(L.isEmpty());
		iter3 = L.getIter();
		
		ASSERT(iter3.hasNext() == false);
		//ASSERT(iter3.next() == null);
		
		iter = L.getTailIter();
		ASSERT(iter.hasNext() == false);
		//ASSERT(iter.next() == null);
	
		// Test List Reversal.
		L = new List<Integer>(1, 2, 3, 4, 5, 6, 7, 8, 9);
		L.reverse();
		
		Integer[] A4 = {9,8,7,6,5,4,3,2,1};
		ASSERT(equal(L.toArray(), A4));
		
		L.remove(4);
		L.remove(9);
		L.remove(1);
		L.remove(3);
		
		Integer[] A5 = {8,7,6,5,2};
		ASSERT(equal(L.toArray(), A5));
		
		
		// 20. destructive append.
		
		L  = new List<Integer>(1,2,3,4);
		L2 = new List<Integer>(5,6,7);
		
		// Any access to L2 will now be undefined.
		L.destructiveAppend(L2);
		
		ASSERT(L.size() == 7);
		ASSERT(L.getFirst() == 1);
		ASSERT(L.getLast() == 7);
		
		L  = new List<Integer>();
		L2 = new List<Integer>(1,2,3);
		
		L.destructiveAppend(L2);
		
		ASSERT(L.size() == 3);
		ASSERT(L.getFirst() == 1);
		ASSERT(L.getLast() == 3);

		L  = new List<Integer>(1,2,3);
		L2 = new List<Integer>();
		L.destructiveAppend(L2);
		
		ASSERT(L.size() == 3);
		ASSERT(L.getFirst() == 1);
		ASSERT(L.getLast() == 3);
		
		
	}
	
	private void test_UBA()
	{
		/*
		 * List Capabilities tested:
		 *	1.	Add()
		 *	2.	Rem()
		 *	3.	toArray()
		 *	4.	pop()
		 *	5.	push()
		 *	6.	enq()
		 *	7.	deq()
		 *	8.	size()
		 *	9.	isEmpty()
		 *	10.	getFirst()
		 *	11. getLast()
		 *	12. iteration.
		 *	13.	get()
		 *	14.	set()
		 *	15.	Both Constructors have been used.
		 *	16. Equality.
		 *
		 *	17. clear()
		 *	18. clear(int limit);
		 *
		 *  19. delete_and_shift(). 
		 */
		
		UBA<Integer> uba = new UBA<Integer>(1);
		
		// -- Test Initial List State.
		ASSERT(uba.isEmpty());
		ASSERT(uba.size() == 0);
		
		// -- Add() - Rem().
		
		Integer[] A = new Integer[TEST_SIZE];
		
		// Test invariants and add().
		for(int i = 0; i < TEST_SIZE; i++)
		{
			uba.add(i);
			A[i] = i;
			
			// Check first.
			ASSERT(0  == uba.getFirst());

			// check last.
			ASSERT(i  == uba.getLast());
			
			// check size.
			ASSERT(uba.size() == i + 1);
			
			ASSERT(!uba.isEmpty());
		}
		
		// Test toArray(), inclusion, and ordering.
		Integer[] uba_a = uba.toArray();
		
		ASSERT(uba_a != null);
		ASSERT(uba.size() == TEST_SIZE);
		
		// Check the Arrays for equality.
		ASSERT(equal(uba_a, A));
		
		// Test Removal.
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			ASSERT(i == uba.getLast());
			ASSERT(0 == uba.getFirst());
			
			ASSERT(!uba.isEmpty());
			ASSERT(uba.rem().equals(i));
			
			ASSERT(i == uba.size());			
		}

		// Check that the list has reverted to its initial state.
		ASSERT(uba.isEmpty() && uba.size() == 0);
		
		uba = new UBA<Integer>(50);
		
		// -- STACK.
		
		// - Pushing and invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			uba.push(i);
			A[i] = i;
			
			// Check first.
			ASSERT(0  == uba.getFirst());

			// check last.
			ASSERT(i  == uba.getLast());
			
			// check size.
			ASSERT(uba.size() == i + 1);
			
			ASSERT(!uba.isEmpty());
		}
		
		// Test toArray(), inclusion, and ordering.
		uba_a = uba.toArray();
		
		ASSERT(uba_a != null);
		ASSERT(uba.size() == TEST_SIZE);
		
		// Check the Arrays for equality.
		ASSERT(equal(uba_a, A));
		
		// - Pop() and Invariants.
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			ASSERT(i == uba.getLast());
			ASSERT(0 == uba.getFirst());
			ASSERT(!uba.isEmpty());
			ASSERT(uba.pop().equals(i));
			ASSERT(i == uba.size());			
		}
		
		// Check that the list has reverted to its initial state.
		ASSERT(uba.isEmpty() && uba.size() == 0);
		
		// -- QUEUE.
		
		// - enqueing and invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			uba.enq(i);
			A[i] = i;
			
			// Check first.
			ASSERT(0  == uba.getFirst());

			// check last.
			ASSERT(i  == uba.getLast());
			
			// check size.
			ASSERT(uba.size() == i + 1);
			
			ASSERT(!uba.isEmpty());
		}
		
		// Test toArray(), inclusion, and ordering.
		uba_a = uba.toArray();
		
		ASSERT(uba_a != null);
		ASSERT(uba.size() == TEST_SIZE);
		
		// Check the Arrays for equality.
		ASSERT(equal(uba_a, A));
		
		ASSERT(uba != null);
		
		// - deq() and Invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{

			if(uba.getLast() == null)
			{
				print(i);
				print(uba);
			}
			
			ASSERT(TEST_SIZE - 1 == uba.getLast());
			ASSERT(i	== uba.getFirst());
			
			ASSERT(!uba.isEmpty());
			ASSERT(uba.deq().equals(i));

			ASSERT(TEST_SIZE - 1 - i == uba.size());
		}

		// Check that the uba has reverted to its initial state.
		ASSERT(Empty(uba));
		
		// -- Test Iteration, get, and set.
		uba = new UBA<Integer>();
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			uba.add(i);
		}
		
		int index = 0;
		for(int i : uba)
		{
			ASSERT(uba.get(index) == i);
			uba.set(index, i + 5);
			ASSERT(uba.get(index) == i + 5);
			index++;
		}

		// Make sure that the UBA throws proper exceptions.
		boolean flag = true;
		try
		{
			uba.get(-1);
			flag = false;
		}
		catch(Error e){}
		
		try
		{
			uba.set(-1, null);
			flag = false;
		}
		catch(Error e){}
		
		try
		{
			uba.get(uba.size());
			flag = false;
		}
		catch(Error e){}

		try
		{
			uba.set(uba.size(), null);
			flag = false;
		}
		catch(Error e){}
		
		ASSERT(flag);
		
		uba  = new UBA<Integer>();
		UBA<Integer > uba2;
		uba2 = new UBA<Integer>();
		
		ASSERT(Empty(uba));
		ASSERT(Empty(uba2));
		
		ASSERT(equal(uba, uba2));
		
		uba.append(5, 7, 8, 9);
		
		ASSERT(!equal(uba, uba2));
		
		uba2.add(5);
		
		ASSERT(!equal(uba, uba2));
		
		uba2.append(6, 8, 9);
		
		ASSERT(!equal(uba, uba2));
		
		uba2.set(1, 7);
		
		ASSERT(equal(uba, uba2));
		
		ASSERT(!uba.equals(null));
		
		uba = new UBA<Integer>(8);
		uba.append(9, 7, 4);
		uba.append(3, 4, 6);
		ASSERT(uba.deq() == 9);
		
		try
		{
			uba.clear(0);
			ASSERT(false);
		}
		catch(Error e)
		{
			// This is correct.
		}
		
		uba.clear();
		
		ASSERT(uba.isEmpty() && uba.size() == 0);
		
		try
		{
			uba.rem();
			ASSERT(false);
		}
		catch(Error e)
		{
			// Good job.
		}
		
		
		// -- Test Delete and Shift.
		
		for(int i = 0; i < 20; i++)
		{
			uba.add(i/2);
		}
		
		for(int i = 19; i >= 0; i -= 2)
		{
			uba.delete_and_shift(i);
		}
		
		for(int i = 0; i < 10; i++)
		{
			ASSERT(uba.get(i) == i);
		}
		
		
		// Make sure the UBA does not have any bugs with sorting 
		// itself when the limit is larger than the current number of elements.
		uba = new UBA<Integer>(10);
		uba.add(3);
		uba.add(4);
		uba.add(1);
		
		uba.sort();
		
		ASSERT(uba.get(0) == 1);
		ASSERT(uba.get(1) == 3);
		ASSERT(uba.get(2) == 4);
	}
	
	private void test_HashTable()
	{
		
		/*
		 * Capabilities tested.
		 * 1. Insertion().
		 * 2. contains().
		 * 3. remove().
		 * 4. size().
		 * 5. isEmpty().
		 * 6. Negative hashcodes.
		 * 7. equals().
		 */
		
		HashTable<Integer> HT = new HashTable<Integer>(); 

		ASSERT(Empty(HT));
		
		// -- Test basic Insertion and contains.
		ASSERT(!HT.contains(0));
		
		HT.insert(0);
		
		ASSERT(HT.contains(0));
		ASSERT(!HT.isEmpty());
		ASSERT(HT.size() == 1);
		
		// -- Test basic Removal.
		HT.remove(0);
		ASSERT(Empty(HT));
		
		// - These tests have repetitions,
		// because duplicates should not be possible.
		
		// -- More intensive test.
		for(int rep = 0; rep < 2; rep++)
		for(int i = 0; i < TEST_SIZE; i += 2)
		{
			HT.insert(i);
			ASSERT(HT.contains(i));
			ASSERT(!HT.contains(i - 1));
			ASSERT(rep == 1 || HT.size() == i / 2 + 1);
		}
		
		ASSERT(HT.size() == TEST_SIZE / 2);
		
		// Test for consistency and soundness.
		for(int rep = 0; rep < 2; rep++)
		for(int i = 0; i < TEST_SIZE; i += 2)
		{
			ASSERT(HT.contains(i));
			ASSERT(!HT.contains(i - 1));
			HT.remove(i - 1);
			ASSERT(HT.size() == TEST_SIZE / 2);
		}
		
		// -- Test clone.
		ASSERT(equal(HT, HT.clone()));
		
		// Test removal.
		for(int i = 0; i < TEST_SIZE; i += 2)
		{
			ASSERT(!HT.isEmpty());
			HT.remove(i);
			ASSERT(!HT.contains(i));
			ASSERT(HT.size() == TEST_SIZE/2 - i/2 - 1);
		}

		ASSERT(Empty(HT));

		// -- Test input with negative hash values.
		ASSERT(!HT.remove(-8));
		HT.add(-90000);
		HT.add(-678);
		ASSERT(!HT.remove(-45));
		
		ASSERT(HT.contains(-90000));
		ASSERT(HT.contains(-678));
		
		ASSERT(HT.remove(-90000));
		ASSERT(HT.remove(-678));
		
		ASSERT(Empty(HT));
		
		// -- Test equality.		
		HashTable<Integer> HT2 = new HashTable<Integer>();
		
		HT.append(56, 34, 4);
				
		ASSERT(Empty(HT2));
		
		ASSERT(!equal(HT, HT2));
		
		HT2.add(56);
		
		ASSERT(!equal(HT, HT2));
		
		HT2.append(34, 3);
				
		ASSERT(!equal(HT, HT2));
		
		HT2.remove(3);
		HT2.add(4);
		
		ASSERT(equal(HT, HT2));

		ASSERT(!HT.equals(null));
	}
	
	private void test_AssociativeArrays()
	{
		/*
		 * Tests
		 * 
		 * Insert, lookup, removal.
		 * Sorting functions for UBAs and conversions.
		 */
		
		Dict<Integer> dict = new Dict<Integer>();
		
		List<String>  LS = dict.getKeys();
		
		ASSERT(LS != null);
		ASSERT(Empty(LS));
				
		List<Integer> LI = dict.getValues();
		
		ASSERT(LI != null);
		ASSERT(Empty(LI));
		
		dict.insert("Bryce", 10);
		dict.insert("Summers", -6);
		
		ASSERT(!Empty(dict));
		ASSERT(dict.size() == 2);
		
		// Test keys.
		LS = dict.getKeys();
				
		ASSERT(LS != null);
		ASSERT(LS.contains("Bryce"));
		ASSERT(LS.contains("Summers"));
		
		// Test Value.
		LI = dict.getValues();
		
		ASSERT(LI != null);
		ASSERT(LI.contains(10));
		ASSERT(LI.contains(-6));
		
		// Test Lookup.
		ASSERT(10 == dict.lookup("Bryce"));
		ASSERT(-6 == dict.lookup("Summers"));
		
		dict.update("Bryce", 5);
		
		ASSERT(dict.size() == 2);
		ASSERT(5 == dict.lookup("Bryce"));
		
		dict.remove_key("Bryce");
		dict.remove_key("Summers");
		
		ASSERT(Empty(dict));
		
		// More intensive testing.
		
		Dict<Integer> dict2 = new Dict<Integer>(5);
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			dict.insert("" + i, i);
		}
		
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			dict2.insert("" + i, i);
		}
		
		// Test for equality.
		ASSERT(equal(dict, dict2));
			
		ASSERT(dict.size() == TEST_SIZE);
		ASSERT(!Empty(dict));
		
		// -- Test clone and equality.
		ASSERT(equal(dict, dict.clone()));
		
		// Extract the values.
		UBA<Integer> vals = dict.getValues().toUBA();
		
		// Sort the keys and values.
		vals.sort();
		
		ASSERT(isSorted(vals.toArray()));
		
		// Test the Sort library's internal comparable is sorted function.
		ASSERT(Sort.is_sorted(vals.toArray()));
		
		// Check for proper inclusion.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(vals.get(i) == i);
			ASSERT(dict.lookup("" + i) == i);
		}
		
		// Test removal via key_remove. (Null insertion is now supported.)
		for(int i = 0; i < TEST_SIZE / 2; i++)
		{
			dict.remove_key("" + i);
		}
		
		// Test removal via key_remove.
		for(int i = TEST_SIZE / 2; i < TEST_SIZE; i++)
		{
			dict.remove_key("" + i);
		}
		
		ASSERT(Empty(dict));

	}
	
	private void test_set()
	{
		Set<Integer> set = new Set<Integer>();
		
		ASSERT(Empty(set));
		
		set.set_add(0);
		set.set_add(0);
		set.set_add(-90);
		
		ASSERT(set.size() == 2);
						
		Set<Integer> set2 = new Set<Integer>(0, -90);
		
		ASSERT(equal(set, set2));
		
		set.set_add(set);
		
		ASSERT(set.cardinality() == 3);
		
		set2 = new Set<Integer> (2,3);
		
		set = set.union(set2);
		
		ASSERT(set.cardinality() == 5);
		
		// -- Test clone and equality of recursive sets.
		ASSERT(equal(set, set.clone()));
		
		set = set.intersection(set2);
		
		ASSERT(set.cardinality() == 2);
		
		set = set.intersection(new Set<Integer>());
		
		ASSERT(Empty(set));
	}

	// FIXME : Complete the testing of Trees.
	private boolean test_Trees()
	{

		AVL<Integer> T1 = new AVL<Integer>();
		
		Integer[] A = new Integer[TEST_SIZE];
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			T1.add(i);
			A[i] = i;
		}
		
		Integer[] B = T1.toArray();
		
		ASSERT(equal(A, B));
		
		return true;		
	}
	
	private boolean test_Data_Structure()
	{
		throw new Error("Not yet Implemented!");
	}
		
	private boolean test_UnionFind()
	{
		UnionFind<Integer> UF = new UnionFind<Integer>();
		
		for(int i = 0; i < 10; i++)
		{
			UF.makeset(i);
		}
		
		UF.union(1, 2);
		print(UF);
		
		UF.union(2, 3);
		print(UF);
		
		UF.union(5, 4);
		print(UF);
		
		UF.union(0, 5);
		print(UF);
		
		UF.union(5, 2);
		
		print(UF);
		
		return true;
	}
	
	private void test_sort_and_search()
	{
		/*
		 * 1. insertion sort.
		 * 2. Merge Sort.
		 * 3. Quick Sort.
		 */
		int[] A = {5,6,3,7,2,1};
		int[] B = {5,6,3,7,2,1};
		int[] C = {5,6,3,7,2,1};
		
		int[] sorted = {1,2,3,5,6,7};		
		
		Sort.isort(A);
		Sort.msort(B);
		Sort.qsort(C);
		
		ASSERT(equal(A, sorted));
		ASSERT(equal(B, sorted));
		//ASSERT(equal(C, sorted));

		A = new int[1];
		A[0] = 0;
		
		// Make sure that trivial arrays do not throw errors.
		Sort.isort(A);
		Sort.msort(A);
		Sort.qsort(A);
		
		// Test on larger data sets.
		A = new int[TEST_SIZE];
		B = new int[TEST_SIZE];
		C = new int[TEST_SIZE];
		
		// Generate.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			A[i] = (int)(TEST_SIZE*Math.random());
			B[i] = (int)(TEST_SIZE*Math.random());
			C[i] = (int)(TEST_SIZE*Math.random());
		}

		
		// Sort.
		Sort.isort(A);
		Sort.msort(B);
		// FIXME : Something is wrong.
		Sort.qsort(C);

		
		// Test the Correctness of the sorting functions.
		ASSERT(isSorted(A));
		ASSERT(isSorted(B));
		ASSERT(isSorted(C));
		
		// Test Sort's internal is_Sorted functions.
		ASSERT(Sort.is_sorted(A));
		ASSERT(Sort.is_sorted(B));
		ASSERT(Sort.is_sorted(C));

	}
	
	// FIXME .
	private void test_search()
	{
		
	}
	
	private void test_singleLinkedList()
	{
		SingleLinkedList<Integer> L, L2, L3;
		
		L = new SingleLinkedList<Integer>();
		Integer[] A = new Integer[TEST_SIZE];
		
		// -- STACK.
		
		// - Pushing and invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			L.push(i);
			A[i] = i;
			
			// check size.
			ASSERT(L.size() == i + 1);
			
			ASSERT(!L.isEmpty());
		}
		
		// Test toArray(), inclusion, and ordering.
		Integer[] LA = L.toArray();
		
		ASSERT(LA != null);
		ASSERT(L.size() == TEST_SIZE);
		
		// The arrays will not be equal, because the SingleLinkedList
		// has a different internal ordering than the Doubly Linked List.
		//ASSERT(equal(LA, A));
		
		// - Pop() and Invariants.
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			ASSERT(!L.isEmpty());
			ASSERT(L.pop().equals(i));
			ASSERT(i == L.size());			
		}
		
		// Check that the list has reverted to its initial state.
		ASSERT(L.isEmpty() && L.size() == 0);

		// -- QUEUE.
		
		// - enqueing and invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			L.enq(i);
			A[i] = i;
			
			// Check first.
			ASSERT(0  == L.getFirst());

			// check last.
			ASSERT(i  == L.getLast());
			
			// check size.
			ASSERT(L.size() == i + 1);
			
			ASSERT(!L.isEmpty());
		}

		// -- Iteration.
		int count = 0;
		for(int i : L)
		{
			ASSERT(i == count);
			count++;
		}
		
		// Test toArray(), inclusion, and ordering.
		LA = L.toArray();
		
		ASSERT(LA != null);
		ASSERT(L.size() == TEST_SIZE);
		
		// Check the Arrays for equality.
		ASSERT(equal(LA, A));
		
		// - deq() and Invariants.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(!L.isEmpty());
			ASSERT(L.deq().equals(i));
			
			ASSERT(TEST_SIZE - 1 - i == L.size());			
		}
		
		// Check that the list has reverted to its initial state.
		ASSERT(L.isEmpty() && L.size() == 0);
		
		test_immutableLists(new SingleLinkedList<Integer>(),
							new SingleLinkedList<Integer>());
	}
	
	private void test_set(Data_Structures.ADTs.Sets.SimpleSet<Integer> S)
	{
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(!S.contains(i));
			S.add(i);
			ASSERT(S.contains(i));
		}
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(S.contains(i));
			ASSERT(S.remove(i));
			ASSERT(!S.contains(i));
			ASSERT(!S.remove(i));
		}
		
		for(int i = TEST_SIZE; i > 0; i--)
		{
			S.add(i);
			S.add(i);
			ASSERT(S.remove(i));
			S.add(i);
			ASSERT(S.contains(i));
		}
		
		for(int i = TEST_SIZE; i > 0; i--)
		{
			ASSERT(S.contains(i));
		}
		
		for(int i = TEST_SIZE; i > 0; i--)
		{
			ASSERT(S.remove(i));
		}
		
	}
	
	// -- Unit Testing helper functions.
	
	// FIXME : Perhaps refactor these functions into a separate unit testing library.
	
	private boolean equal(Integer[] A1, Integer[] A2)
	{
		int len = A1.length;
		
		if(len != A2.length)
		{
			return false;
		}
		
		for(int i = 0; i < len; i++)
		{
			if(!A1[i].equals(A2[i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean equal(int[] A1, int[] A2)
	{
		int len = A1.length;
		
		if(len != A2.length)
		{
			return false;
		}
		
		for(int i = 0; i < len; i++)
		{
			if(!(A1[i] == A2[i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static Genarics<Data_Structure<?>> ge_ds = new Genarics<Data_Structure<?>>();
	
	private boolean equal(Data_Structure<?> A, Data_Structure<?> B)
	{
		return ge_ds.xequal(A, B);		
	}

	private boolean isSorted(Integer[] A)
	{
		int len = A.length;
		
		if(len < 2)
		{
			return true;
		}
		
		Integer last = null;
		
		for(int i : A)
		{
			if(last == null)
			{
				last = i;
				continue;
			}
			
			if(i < last)
			{
				return false;
			}
			
			last = i;
			
		}
		
		return true;
		
	}
	
	private boolean isSorted(int[] A)
	{
		int len = A.length;
		
		if(len < 2)
		{
			return true;
		}
		
		Integer last = null;
		
		for(int i : A)
		{
			if(last == null)
			{
				last = i;
				continue;
			}
			
			if(i < last)
			{
				return false;
			}
			
			last = i;
			
		}
		
		return true;
		
	}
	
	private boolean Empty(Data_Structure<?> D)
	{
		return D.isEmpty() && D.size() == 0;
	}
	
	private void test_fast_structures()
	{
		Stack<Integer> S = new FastStack<Integer>();
		
		// Test trivial yard conditions.
		ASSERT(FastStructure.isYardFull());
		ASSERT(FastStructure.isYardEmpty());
		
		// Test trivial stack condition.
		ASSERT(S.isEmpty());
		
		// Increase the yard size.
		FastStructure.setYardSize(TEST_SIZE);
		ASSERT(FastStructure.isYardFull() == false);
		
		// Fill up the yard.
		FastStructure.fillYard(-1);
		ASSERT(FastStructure.isYardFull() == true);
		
		// Now push all of the nodes onto the stack.
		for(int i = 0; i < TEST_SIZE; i++)
		{
			S.push(i);
			ASSERT(S.top() == i);
		}
		
		ASSERT(FastStructure.isYardFull() == false);
		ASSERT(FastStructure.isYardEmpty() == true);
		
		// Now pop all of the nodes back into the yard.
		for(int i = TEST_SIZE - 1; i >= 0; i--)
		{
			ASSERT(S.pop() == i);
		}
		
		ASSERT(S.isEmpty());
		ASSERT(FastStructure.isYardFull()  == true);
		ASSERT(FastStructure.isYardEmpty() == false);
		
		// -- FastQueue.
		Queue<Integer> Q = new FastQueue<Integer>();
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(Q.size() == i);
			Q.enq(i);
		}
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			ASSERT(Q.peek() == i);
			ASSERT(Q.deq() == i);
		}
		
		ASSERT(Q.isEmpty());
		
		
		test_immutableLists(S, Q);
	
	}
	
	// Test a Queue and Stack implementation for immutability.
	private void test_immutableLists(Stack<Integer> S, Queue<Integer> Q)
	{
		// -- Test immutability.
		
		ASSERT(S.isEmpty());
		ASSERT(Q.isEmpty());
		
		UBA<Stack<Integer>> stacks = new UBA<Stack<Integer>>();
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			S =  S.push_static(i);
			stacks.enq(S);
		}
		
		for(int repeat = 0; repeat < 2; repeat++)
		for(int i = 0; i < TEST_SIZE; i++)
		{
			S = stacks.get(i);
			
			for(int k = i; k > 0; k--)
			{
				ASSERT(S.top() == k);
				ASSERT(S.pop_static().getKey() == k);
				ASSERT(S.pop_static().getKey() == k);
				S = S.pop_static().getVal();
			}
		}
		
		/*
		// -- Immutable queues.
		UBA<Queue<Integer>> queues = new UBA<Queue<Integer>>();
		
		for(int i = 0; i < TEST_SIZE; i++)
		{
			Q =  Q.enq_static(i);
			queues.enq(Q);
		}
		
		for(int repeat = 0; repeat < 2; repeat++)
		for(int i = 0; i < TEST_SIZE; i++)
		{
			Q = queues.get(i);
			
			for(int k = 0; k < i; k++)
			{
				ASSERT(Q.peek() == k);
				ASSERT(Q.deq_static().getKey() == k);
				ASSERT(Q.deq_static().getKey() == k);
				Q = Q.deq_static().getVal();
			}
		}*/
	}
	
	private void test_heap() 
	{
		ArrayHeap<Integer> h1 = new ArrayHeap<Integer>();
		
		ASSERT(h1.isEmpty());
		ASSERT(h1.size() == 0);
		
		h1.append(1, 3, 5, 7, 9, 0, 8, 4, 6, 2);
		
		ASSERT(!h1.isEmpty());
		ASSERT(h1.size() == 10);
		
		
		
		for(int i = 0; i < 10; i++)
		{
			System.out.println("Iteration i = " + i);
			System.out.println(h1);
			ASSERT(h1.size() == 10 - i);
			ASSERT(h1.peek_dominating() == i);
			ASSERT(h1.extract_dominating() == i);
		}
		
		ASSERT(h1.isEmpty());		
	}
		
}
