package Game_Engine.GUI.Components.communication;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.buttons.gui_destroyButton;

/*
 * Written by Bryce Summers on 7 - 21 - 2014.
 * 
 * 
 */

public abstract class gui_closableContainer extends gui_window
{

	// -- Private data.
	
	// Triggers the death sequence. 
	// Comes with preset close "X" marking.
	gui_destroyButton button_close;
	
	// -- Constructors.
	public gui_closableContainer(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_closableContainer(Rectangle screen)
	{
		super(screen);
	}
	
	// Extend the initialization of objects.
	@Override
	public void iObjs()
	{
		super.iObjs();
		
		button_close = new gui_destroyButton(world.getW() - 32, 0, 32, 32);
		obj_create(button_close);
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
		populateContent(0, 0, world.getW() - 32, world.getH());
	}
	
	// Should be implemented to populate the informational components of this container to a proper area.
	public abstract void populateContent(int x, int y, int w, int h);
	
	@Override
	public void update()
	{
		super.update();
		
		if(button_close.dead(this))
		{
			kill();
		}
	}
	
	// Disables the user from clicking and using the close button.
	public void disableCloseButton()
	{
		button_close.disable();		
	}
}
