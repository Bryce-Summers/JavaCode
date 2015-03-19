package Data_Structures.Structures.InDevelopment;

import java.util.Iterator;

import Data_Structures.ADTs.Stack;
import Data_Structures.Structures.Data_Structure;
import Data_Structures.Structures.List;
import Data_Structures.Structures.SingleLinkedList;
import Data_Structures.Structures.HashingClasses.AArray;

/*
 * Implemented on 11/12/2014 by Bryce Summers.
 * 
 * A Disjoint set structure that allows makeset, union, and find operations in near constant time 
 * (inverse ackerman function time complexity).
 * 
 * This implementation features path compression and minimum tree depth expansion unions.
 * 
 * Users should only use elements that have well thought out and implemented hash functions.
 */

public class UnionFind<E> extends Data_Structure<E>
{
	// Node representation class.
	private class Node
	{
		// Stores data.
		private E elem;
		
		// Allows representatives to be found.
		Node parent = null;
		
		// Allows us to reduce the rank of nodes.
		int rank = 0;
		
		Node(E elem)
		{
			this.elem = elem;
		}
		
		protected Node clone()
		{
			Node output = new Node(elem);
			output.rank = rank;
			output.parent = parent;
			return output;
		}
	}
	
	// -- Represents the set of nodes.
	AArray<E, Node> nodes = new AArray<E, Node>();
	
	// Adds a new element to the set.
	public void makeset(E elem)
	{
		if(nodes.lookup(elem) == null)
		{
			Node n = new Node(elem);
			nodes.insert(elem, n);
		}
	}
	
	// Unions the given two nodes together.
	public void union(E e1, E e2)
	{
		link(findNode(e1), findNode(e2));
	}
	
	// Returns the representative of elem.
	public E find(E elem)
	{
		Node output = findNode(elem);
		return find(output).elem;
	}
	
	public boolean connected(E e1, E e2)
	{
		return find(e1) == find(e2);
	}
	
	private Node findNode(E elem)
	{
		Node n = nodes.lookup(elem);
		
		if(n == null)
		{
			throw new Error("Union Find find() The given input element: " + elem +" was not found.");
		}
		
		return find(n);
	}
	
	private Node find(Node n)
	{
		Stack<Node> S = new SingleLinkedList<Node>();
		
		// Search for the root node.
		while(n.parent != null)
		{
			if(n == n.parent)
			{
				throw new Error("This is Bad");
			}
			
			S.push(n);
			n = n.parent;
		}
		
		Node root = n;
		
		// Path compress the rest of the nodes.
		while(!S.isEmpty())
		{
			S.pop().parent = root;
		}
		
		return root;
	}
	
	// Joins two root nodes together.
	// REQURIES: n1.parent = null and n2.parent = null.
	// Ensures : Points the node of minimal depth to the node with non minimal depth.
	private void link(Node n1, Node n2)
	{
		// Very important.
		if(n1 == n2)
		{
			return;
		}
		
		// N1 Greater depth.
		if(n1.rank > n2.rank)
		{
			n2.parent = n1;
			return;
		}
		// N2 Greater depth.
		else if(n2.rank > n1.rank)
		{
			n1.parent = n2;
			return;
		}
		// Equal depth --> increase rank.
		else
		{
			n2.parent = n1;
			n1.rank++;
		}
	}

	
	// -- Standard Bryce Data Structure Functions.
	
	
	@Override
	public Iterator<E> iterator()
	{
		return nodes.getKeys().iterator();
	}


	@Override
	public void add(E elem)
	{
		makeset(elem);
	}

	@Override
	public String toString()
	{
		
		StringBuilder output = new StringBuilder();
	
		List<List<E>> sets = getSets().getValues().removeDuplicates();
		
		for(List<E> L : sets)
		{
			output.append("{");
			for(E elem :L)
			{
				output.append(elem + ", ");
			}
			
			//output.length();
			//int len = output.toString().length();
			output.replace(output.length() - 2, output.length(), "}");
		}
		
		return output.toString();
	}
	
	// Returns a mapping from elements to the set they are contained in .
	public AArray<E, List<E>> getSets()
	{
		AArray<E, List<E>> output = new AArray<E, List<E>>();
		
		List<Node> keys = nodes.getValues();
		
		// Iteratively form the sets.
		for(Node n: keys)
		{
			Node representative = find(n);
			E root_elem = representative.elem;
			List<E> list = output.lookup(root_elem);
			
			if(list == null)
			{
				list = new List<E>();
				output.insert(root_elem, list);
			}
			
			list.add(n.elem);
			
			// Add an additional pointer to the list to not root nodes.
			if(n != representative)
			{
				output.insert(n.elem, list);
			}
		}
		
		return output;
	}

	@Override
	public Data_Structure<E> clone()
	{
		UnionFind<E> UF = new UnionFind<E>();
		
		// Copy all of the node data.
		List<E> keys = nodes.getKeys();
		
		for(E elem : keys)
		{
			Node n = nodes.lookup(elem);
			UF.nodes.insert(elem, n.clone());
		}
		
		return UF;
	}
	
	public int size()
	{
		return nodes.size();
	}
}
