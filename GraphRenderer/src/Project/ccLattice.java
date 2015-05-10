package Project;

import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.List;

public class ccLattice extends ccGraph
{

	// REQUIRES: w == h.
	public ccLattice(int w, int h, int size, List<Integer> edges)
	{
		super(w, w, size, edges);
	}
		
	// Returns the location of the given vertice.
	public Vector getVertice(int index)
	{
		int row = index / size;
		int col = index % size;
		
		double w = getWidth();
		double h = getHeight();
		
		double block_size = w/size;
		
		return 	new Vector(block_size/2 + row*block_size,
						   block_size/2 + col*block_size);
	}

}
