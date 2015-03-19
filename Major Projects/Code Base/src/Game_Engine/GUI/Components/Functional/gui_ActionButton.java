package Game_Engine.GUI.Components.Functional;

import util.interfaces.Action;
import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * Action button.
 * 
 * Written by Bryce Summers on 9 - 14 - 2014.
 */

public class gui_ActionButton extends gui_button
{

	final Action action;
	
	// Constructors.
	public gui_ActionButton(double x, double y, int w, int h, Action a)
	{
		super(x, y, w, h);
		action = a;
	}
	
	public gui_ActionButton(Rectangle r, Action a)
	{
		super(r);
		action = a;
	}
	
	// Update event polling.
	public void update()
	{
		super.update();
		
		if(flag())
		{
			action.action();
		}
	}

}
