package BryceGraphs.Algorithms;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;

import util.SerialB;
import util.interfaces.Consumer1;
import util.interfaces.Consumer2;
import util.interfaces.Function;
import BryceGraphs.ADTs.Serials.SerialAdjacencyNode;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;
import Data_Structures.Structures.HashingClasses.AArray;

/**
 * Graph Serial Class.
 * 
 * @author Bryce Summers
 *
 * @date 8/18/2014.
 * 
 * Purpose : This class provides serialization algorithms for Graphs,
 * 			 along with their deserialization counterparts.
 */

public class GraphSerial
{
	
	// -- Adjacency Node serialization functions.
	public static <Node extends SerialAdjacencyNode<Node>>
			 void serialize(PrintStream stream, Node root)
	{		serialize(stream, root, null);
	}
	
	
	/**
	 * Serializes the connected graph reachable by the given root adjacency node. 
	 * The adjacency node should implement the SerialB interface in order to describe exactly how
	 * its non generic data should be serialized.
	 * @param stream the printstream that serialized data strings should be written to.
	 * @param root the root of the graph to be serialized.
	 */
	public static <Node extends SerialAdjacencyNode<Node>> 
			 void serialize(PrintStream stream, Node root, Consumer2<Node, PrintStream> continuation)
	{
		Consumer1<Node> writing = new Serialize<Node>(stream, continuation);
		
		GraphSearch.BFS(root, writing);
		
		// Add an ending statement to allow graph serialization to be nested in larger serialization processes.
		stream.println("GRAPH END");
	}
	
	// The serialization algorithm function.
	// WARNING : Should only be used for one serialization.
	// This function class defines the serialization method for the nodes of the tree.
	private static class Serialize<Node extends SerialAdjacencyNode<Node>>
		implements Consumer1<Node>
	{
		// We need to form an index to ensure that all of the nodes are associated correctly in the file.
		// We associate every node with an integer so that the associations are by integer number in the file.
		int next_index = 0;
		final AArray<Node, Integer> indices = new AArray<Node, Integer>(10);
		
		final PrintStream outputStream;
		
		// Specifies additional serialialization for nodes.
		final Consumer2<Node, PrintStream> continuation;
				
		
		private Serialize(PrintStream stream, Consumer2<Node, PrintStream> cont)
		{
			outputStream = stream;
			continuation = cont;
		}
		
		/** Returns true if and only if the given node has been visited.
		 * This eval function should only be called once for any given input node.
		 * This can be accomplished using correct implementations of search algorithms 
		 * such as breadth first search or depth first search.
		 */
		@Override
		public void eval(Node input)
		{
		
			// Make sure this node is present in the index.
			ensureIndex(input);

			// Compute the index.
			int index = indices.lookup(input);
			
			// Write this node's index.
			write(index);
			
			// Write the number of neighbors it has.
			write(input.getNeighborSize());
			
			// Retrieve an iterator for the neighbors.
			Iterable<Node> neighbors = input.getNeighbors();
			
			// -- Write the list of translated indices.
			for(Node node: neighbors)
			{
				ensureIndex(node);
				index = indices.lookup(node);
				write(index);
			}
			
			// Serialize the adjacency node's internal data.
			((SerialB)input).serializeTo(outputStream);
			
			if(continuation != null)
			{
				continuation.eval(input, outputStream);
			}
			
		}
		
		// Make sure the node is in the index.
		private void ensureIndex(Node node)
		{
			if(indices.contains_key(node))
			{
				return;
			}
			
			indices.insert(node, next_index);
			next_index++;			
		}
		
		private void write(Object o)
		{
			outputStream.println(o.toString());
		}		
	}
	
	/**
	 * REQUIRES : The data_iter must point to a graph structure that was serialized with the serialize function
	 * 			  and same graph node type.
	 * 
	 * @param data_iter the data stream whose next() function must return the beginning of data serialized
	 * 		  by this classes serialize(Printstream, Node) function
	 * @param producer a function for pealing off data strings and converting them to Nodes of the user's preference.
	 * 				The producer must be able to read data serialized by the desired concrete Node type.
	 * @return Returns the root of the graph to the user. The other nodes can be accessed through 
	 * 			the standard Adjacency Node interface.
	 */
	public static <Node extends SerialAdjacencyNode<Node>>
		Node deserialize(Iterator<String> data_iter, Function<Iterator<String>, Node> producer)
	{
		if(!data_iter.hasNext())
		{
			throw new Error("Input Data has ended. Check for blank input.");
		}
		
		// -- Generate the abstract graph.
		
		// mapping from abstract indices to in memory nodes.
		UBA<Node> vertices = new UBA<Node>();
		
		// Mapping from abstract indices to an array of their neighbors.
		UBA<int[]> edges = new UBA<int[]>();
		
		// Generate all of the nodes and abstract relationship data.
		populateAbstractGraph(data_iter, producer, vertices, edges);
		
		// Link all of the nodes from the abstract relationship data.
		return linkNodes(vertices, edges);
	}
	
	/**
	 * instantiates all of the nodes and computes the abstract relational data.
	 * Node will ideally be of the same type that was originally serialized to the file.
	 * 
	 * @param iter the stream of serialized data that will be deserialized to create in memory structures.
	 * @param producer a function that can convert serialized data into concrete nodes of the user's specification.
	 * @param vertices OUT: a container to store the references to the nodes that are created.
	 * @param edges    OUT: a container to store the relational information.
	 */
	private static <Node extends SerialAdjacencyNode<Node>>
		void populateAbstractGraph(
				Iterator<String> iter,
				Function<Iterator<String>, Node> producer,
				UBA<Node> vertices,
				UBA<int[]> edges)
	{
		
		while(iter.hasNext())
		{
			String s = iter.next();
			if(s.equals("GRAPH END"))
			{
				break;
			}
			
			int index = new Integer(s);
			vertices.fillToIndex(index);
			edges.fillToIndex(index);
			
			// Parse the edges.
			int edge_num = new Integer(iter.next());
			int[] edge_indices = new int[edge_num];
			for(int i = 0; i < edge_num; i++)
			{
				edge_indices[i] = new Integer(iter.next());
			}
			
			edges.set(index, edge_indices);
			Node node = producer.eval(iter);
			vertices.set(index, node);
		}
	}
	
	/**
	 * 
	 * @param vertices an array of freshly instantiated adjacency nodes.
	 * @param edges an array that defines a mapping between indices in vertices and
	 * 			the indices of other nodes in vertices.
	 * @return
	 */
	private static <Node extends SerialAdjacencyNode<Node>> 
		Node linkNodes(
			UBA<Node> vertices,
			UBA<int[]> edges)
	{
		// -- Give each adjacency node its edges 
		//    by translating the abstract indices to in memory nodes.
		
		int len = vertices.size();
		for(int i = 0; i < len; i++)
		{
			Node node  = vertices.get(i);
			
			// Translate abstract indices to concrete Adjacency nodes.
			int[] neighbor_indices = edges.get(i);
			int num_neighbors = neighbor_indices.length;
			
			if(num_neighbors == 0)
			{
				continue;
			}
			
			UBA<Node> neighbors = new UBA<Node>(num_neighbors);
			
			// Iterate through all neighbors n of node i.
			for(int n = 0; n < num_neighbors; n++)
			{
				neighbors.add(vertices.get(neighbor_indices[n]));
			}
			
			node.setNeighbors(neighbors);
		}
		
		return vertices.get(0);
	}
	
}
