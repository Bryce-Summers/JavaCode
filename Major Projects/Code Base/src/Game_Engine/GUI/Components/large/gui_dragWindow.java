package Game_Engine.GUI.Components.large;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_handle;

/*
 * Graphics User Interface Draggable Window Class.
 * 
 * Written by Bryce Summers.
 * Date : 6 - 3 - 2013.
 * 
 * Purpose : This class creates a union of a handle and a window.
 */



public class gui_dragWindow extends Obj_union
{
	
	// -- Private data.
	public gui_handle handle;
	public gui_window window;
	
	private String handleText = "";
	
	private List<Obj> tempObjs = new List<Obj>();
	
	// -- Constructors.
	public gui_dragWindow(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_dragWindow(Rectangle screen)
	{
		super(screen);
		
	}

	@Override
	public void iObjs()
	{
		
		int handle_h = 50;
		
		handle = new gui_handle(getX(), getY(), getW(), handle_h);
		handle.setText(handleText);
		
		super.obj_create(handle);
		
		window = new gui_window(getX(), getY() + handle_h, getW(), getH() - handle_h);
		
		super.obj_create(window);
		
		// Populate the window with all of the temporary waiting objects.
		for(Obj o : tempObjs)
		{
			add(o);
		}
	}
	
	public void setHandleText(String s)
	{
		
		if(s == null)
		{
			throw new Error("Strings should not be null");
		}
		
		if(handle != null)
		{
			handle.setText(s);
			return;
		}
				
		handleText = s;
	}

	
	public Obj add(Obj o)
	{
		if(window == null)
		{
			tempObjs.add(o);
			return o;
		}
		
		return window.obj_create(o);
	}
	
	// Fits the draggable windows to the text in the handle.
	public void fitToContents(int i)
	{
		handle.fitToContents(i);
		window.setW(handle.getW());				
	}
	
}
