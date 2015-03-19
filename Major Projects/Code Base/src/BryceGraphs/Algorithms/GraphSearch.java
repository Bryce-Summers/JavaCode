package BryceGraphs.Algorithms;

import java.io.PrintStream;
import java.util.function.Consumer;

import util.interfaces.Consumer1;
import util.interfaces.Consumer2;
import util.interfaces.Function;
import BryceGraphs.ADTs.AdjacencyNode;
import Data_Structures.ADTs.Queue;
import Data_Structures.Structures.HashTable;
import Data_Structures.Structures.SingleLinkedList;
import Data_Structures.Structures.Fast.FastQueue;
import Data_Structures.Structures.HashingClasses.AArray;

/*
 * Graph Searching Library.
 * 
 * Written by Bryce Summers on 6 - 22 - 2014.
 */

public class GraphSearch
{
	// Solves the single source shortest paths problem via BFS.
	public static <E extends AdjacencyNode<E>> 
		AArray<E, SingleLinkedList<E>> shortestPaths(E start)
	{
		Queue<E> Q = new FastQueue<E>();
		AArray<E, SingleLinkedList<E>> paths_to_start = new AArray<E, SingleLinkedList<E>>(5);
		
		// Initialize the queue of nodes to visit.
		Q.enq(start);
		SingleLinkedList<E> L = new SingleLinkedList<E>();
		L.add(start);
		paths_to_start.insert(start, L);
		
		while(!Q.isEmpty())
		{
			E node = Q.deq();
			
			SingleLinkedList<E> node_path;
			node_path = paths_to_start.lookup(node);
			
			// Search for non visited nodes, add them to the frontier, and add the found shortest path to the results.
			for(E n: node.getNeighbors())
			{
				// Node not already visited.
				if(paths_to_start.lookup(n) == null)
				{
					SingleLinkedList<E> shortest_path;
					shortest_path = (SingleLinkedList<E>) node_path.push_static(n);
					paths_to_start.insert(n, shortest_path);
					Q.enq(n);
				}
			}
		}
		
		return paths_to_start;
	}
	
	/*
	 * BFS operations.
	 */
	
	/** Performs the given function on a connected component.
	 * 
	 * @param node The Graph is defined as all nodes reachable from the given node.
	 * @param func The given function will be called on all nodes in the Connected component.
	 */
	public static <Node extends AdjacencyNode<Node>> void
		BFS(Node root, Consumer1<Node> func)
	{
		HashTable<Node> visited  = new HashTable<Node>();
		Queue<Node> frontier 	 = new FastQueue<Node>();
		
		// Start the search at the root.
		frontier.enq(root);
		
		// Iteratively search through the frontier.
		while(!frontier.isEmpty())
		{
			Node node = frontier.deq();
			
			// Ignore already processed nodes.
			if(visited.contains(node))
			{
				continue;
			}
			
			// Visit the node.
			func.eval(node);
			visited.add(node);
			
			// Expand the frontier.
			for(Node n : node.getNeighbors())
			{
				frontier.enq(n);
			}
		}
	}
}
