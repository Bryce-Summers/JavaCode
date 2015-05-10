package Project;

import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.List;

public class ccDense extends ccGraph
{
	
	public ccDense(int w, int h, int size, List<Integer> edges) 
	{
		super(w, h, size, edges);	
	}

	// Returns the location of the given vertice.
	public Vector getVertice(int index)
	{
		double w = getWidth();
		double h = getHeight();
				
		double block_size = w/size;
		
		double radius = w/2 - block_size/2;
		
		double angle = 1.0*index/size*2*Math.PI;  
		
		return 	new Vector(w/2 + radius*Math.cos(angle),
						   h/2 + radius*Math.sin(angle));
	}

}
