package Project.Logic_Blocks;

import Project.GameGrid.gridSquare;
import Project.interfaces.Logic_Block;

public class block_False implements Logic_Block
{
	
	@Override
	public String[] getInputNames()
	{
		return new String[]{};
	}

	// True blocks always return true.
	@Override
	public boolean getValue()
	{
		return false;
	}

	@Override
	public void setInput(int index, gridSquare square)
	{
		return;
	}

	@Override
	public gridSquare getInput(int index)
	{
		return null;
	}
	
	public String toString()
	{
		return "F";
	}

}
