package BryceGraphs.ADTs.Serials;

import util.SerialB;
import BryceGraphs.ADTs.AdjacencyNode;

/*
 * An interface for objects that implement both AdjacencyNode and SerialB.
 * 
 * 
 */

public interface SerialAdjacencyNode<E extends AdjacencyNode<E>> extends AdjacencyNode<E>, SerialB
{
	/* Nothing much goes on here. */
	
	// -- Required for AdjacencyNode graph serialization and creation through generic algorithms.
	public void setNeighbors(Iterable<E> neighbors);
}
