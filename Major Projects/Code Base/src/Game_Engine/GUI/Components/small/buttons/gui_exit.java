package Game_Engine.GUI.Components.small.buttons;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * A basic button that exits the program.
 * Written by Bryce Summers on 6 - 5 - 2013.
 */

public class gui_exit extends gui_button
{

	public gui_exit(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_exit(Rectangle r)
	{
		super(r);
	}
	
	public gui_exit(int w, int h)
	{
		super(0, 0, w, h);
	}

	public void update()
	{
		super.update();
		
		if(flag())
		{
			System.exit(1);
		}
	}
	
}
