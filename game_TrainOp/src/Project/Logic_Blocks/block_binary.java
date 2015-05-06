package Project.Logic_Blocks;

import Project.GameGrid.gridSquare;
import Project.interfaces.Logic_Block;

public abstract class block_binary implements Logic_Block
{

	protected gridSquare A, B;
	
	public block_binary(gridSquare current_square)
	{
		this.A = current_square.getInput(0);
		this.B = current_square.getInput(1);
	}
	
	@Override
	public String[] getInputNames()
	{
		return new String[]{"input1", "input2"};
	}

	@Override
	public void setInput(int index, gridSquare square)
	{
		if(index == 0)
		{
			A = square;
			return;
		}
		
		if(index == 1)
		{
			B = square;
			return;
		}
		
		return;
	}

	@Override
	public gridSquare getInput(int index)
	{
		if(index == 0)
		{
			return A;
		}
		
		if(index == 1)
		{
			return B;
		}
		
		return null;
	}

	
}
