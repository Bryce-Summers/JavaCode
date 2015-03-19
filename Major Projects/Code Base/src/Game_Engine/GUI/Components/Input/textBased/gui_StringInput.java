package Game_Engine.GUI.Components.Input.textBased;

import BryceMath.Geometry.Rectangle;

/* Written during the SUMMER of 12 - 31 - 2013 by Bryce Summers.
 * 
 * Purpose : The canonical input element for getting a user to enter a String.
 */

public class gui_StringInput extends gui_textInput<String>
{
	public gui_StringInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_StringInput(Rectangle r)
	{
		super(r);
	}
	
	public String getInput()
	{
		return getText();
	}
	
	public void update()
	{
		super.update();
		
		// Logic for precise characters mode.
		// The string input box displays the non TEXED copy of the exact characters inputed by the user.
		if(isSelected() || mouseInRegion)
		{
			if(getTEX())
			{
				disableTEX();
			}
		}
		// Logic for non selected mode.
		// The String input box displays a pretty TEXED version of the string.
		else
		{
			if(!getTEX())
			{
				enableTEX();
			}
		}
	}

	@Override
	public String getSerialName()
	{
		return "gui_StringInput";
	}
}
