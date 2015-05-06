package Project.Logic_Blocks;

import Project.GameGrid.gridSquare;
import Project.interfaces.Logic_Block;

public class block_OR extends block_binary
{

	public block_OR(gridSquare current)
	{
		super(current);
	}

	@Override
	public boolean getValue() 
	{
		return A.getValue() || B.getValue();
	}
	
	public String toString()
	{
		return "V.";
	}
	
}
