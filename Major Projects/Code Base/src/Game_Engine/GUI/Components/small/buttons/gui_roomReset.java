package Game_Engine.GUI.Components.small.buttons;

import Game_Engine.GUI.Components.small.gui_button;

/*
 * 6 - 22 - 2014.
 * 
 * Resets the containing room when clicked.
 */

public class gui_roomReset extends gui_button
{

	public gui_roomReset(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	
	public void update()
	{
		super.update();
		
		if(flag())
		{
			getRoom().restart();
		}
	}
	
}
