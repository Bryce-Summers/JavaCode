package Project.interfaces;

import Project.GameGrid.gridSquare;

/*
 * The Logic Block interface.
 * 
 * Logic blocks are entities that can reside on the grid,
 * which contain a set of grid locations as inputs
 * and perform behaviors based on the values and other logic
 * blocks located at those locations.
 */

public interface Logic_Block
{
	// Returns the strings denoting the inputs.
	// Returns an array of length 0, if the Logic_Block does not currently accept any inputs.
	public String[] getInputNames();
	
	// Returns the truth value of a logic block.
	public boolean getValue();
	
	// Sets the index'th input to be the given grid square.
	public void setInput(int index, gridSquare square);
	public gridSquare getInput(int index);
	
}
