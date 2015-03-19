package Game_Engine.GUI.Components.small.boxes;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * gui_boolean class.
 * 
 * Written 6 - 6 - 2013.
 * 
 * Purpose : Provides togglable buttons.
 * 
 * Update : 7 - 23 - 2013 :
 * 		- outsourced the clicked on coloring to the focus boxes.
 */

// FIXME : Maybe I should make this straight into a toggle box.

public class gui_boolean extends gui_button // implements Input<Boolean>
{
	// -- Private data.
	private boolean val = false;
	
	public gui_boolean(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_boolean(Rectangle r)
	{
		super(r);
	}
	
	public void toggle()
	{
		if(val)
		{
			setFalse();
			return;
		}

		val = true;
		
		//setRestingColor(C_BUTTON_PUSHED);
	}
	
	// Allow users to set this to false;
	public void setFalse()
	{
		val	= false;

		//setRestingColor(C_CLICKABLE);
		
		return;
	}

	// Allow external sources to ask questions about the state of this object.
	public boolean query()
	{
		return val;
	}
		
	@Override
	public void update()
	{
		super.update();
	
		// Handle Button clicks.
		if(flag())
		{
			toggle();
		}
	}

}
