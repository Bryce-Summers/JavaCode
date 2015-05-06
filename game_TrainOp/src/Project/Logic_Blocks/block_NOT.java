package Project.Logic_Blocks;

import Project.GameGrid.gridSquare;
import Project.interfaces.Logic_Block;

public class block_NOT implements Logic_Block
{
	
	protected gridSquare A;
	
	public block_NOT(gridSquare input)
	{
		A = input.getInput(0);
	}
	
	@Override
	public String[] getInputNames()
	{
		return new String[]{"Input"};
	}

	// True blocks always return true.
	@Override
	public boolean getValue()
	{
		return !A.getValue();
	}

	@Override
	public void setInput(int index, gridSquare square)
	{
		A = square;
		return;
	}

	@Override
	public gridSquare getInput(int index)
	{
		return A;
	}
	
	@Override
	public String toString()
	{
		return "~";
	}

}
