package Game_Engine.GUI.Components.small.buttons;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.gui_button;

/* Written by Bryce Summers.
 * 
 * This button is used trigger the destruction of objects. 
 * It destroys itself upon being clicked upon.
 */

public class gui_destroyButton extends gui_button
{
	// -- Constructors.
	public gui_destroyButton(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		setText("X");
	}
	
	public gui_destroyButton(Rectangle r)
	{
		super(r);
		setText("X");
	}

	// Click to kill this button.
	public void update()
	{
		super.update();
		
		if(flag())
		{
			die();
		}
	}
	
}
