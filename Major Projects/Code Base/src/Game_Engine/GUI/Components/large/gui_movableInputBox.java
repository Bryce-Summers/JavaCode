package Game_Engine.GUI.Components.large;

import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.Input.textBased.gui_StringInput;
import Game_Engine.GUI.Components.small.gui_handle;

/*
 * Movable Input box.
 * 
 * Attaches a handle to an input box.
 * 
 * Written by Bryce Summer on 6 - 24 - 2013.
 */

public class gui_movableInputBox extends Obj_union
{

	// -- Local data.
	gui_StringInput body;
	
	
	public gui_movableInputBox(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_movableInputBox(Rectangle bounds)
	{
		super(bounds);
	}
	
	@Override
	public void iObjs()
	{
		gui_handle handle = new gui_handle(getX(), getY(), 50, getH());
		obj_create(handle);
		
		body = new gui_StringInput(getX() + 50, getY(), getW() - 50, getH());
		obj_create(body);
	}
	
	public void revert()
	{
		body.revert();
		
		setX(x_start);
		setY(y_start);
	}

	public void clearInput()
	{
		body.clearInput();
	}
	
}
