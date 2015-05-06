package Project.Logic_Blocks;

import Project.GameGrid.gridSquare;
import Project.interfaces.Logic_Block;

public class block_True implements Logic_Block
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
		return true;
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
	
	@Override
	public String toString()
	{
		return "T";
	}

}
