package BryceGraphs.ADTs;

/* Specifies the node of an Adjacency list Graph. 
 * 
 * Written by Bryce Summers on 6 - 22 - 2014 
 */

public interface AdjacencyNode<E extends AdjacencyNode<E>>
{
	// Required to retrieve the information from Adjacency nodes.
	public int getNeighborSize();
	public Iterable<E> getNeighbors();

}
