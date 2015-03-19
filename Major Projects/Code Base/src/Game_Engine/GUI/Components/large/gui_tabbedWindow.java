package Game_Engine.GUI.Components.large;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.AArray;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * gui_tabbed window
 * 
 * written by Bryce Summers on 6 - 14 - 2013.
 * 
 * Purpose : This class allows us to map a set of buttons to a set of windows.
 * 			 This allows for only one of the windows to be visible at a time.
 */

public class gui_tabbedWindow extends Obj_union
{

	// -- Private data.
	
	// Every tabbed window knows which button's window it is currently showing.
	gui_button current;
	
	// Every tabbed window has a mapping from buttons to window.
	AArray<gui_button, List<Obj>> tabs = new AArray<gui_button, List<Obj>>();
	
	public gui_tabbedWindow(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		iVars();
	}

	public gui_tabbedWindow(Rectangle bounds)
	{
		super(bounds);
		iVars();
	}
	
	private void iVars()
	{
		
	}

	@Override
	public void iObjs(){}
	
	// To add a tab, simply add the mapping and add each of the objects to the union.
	public void addTab(gui_button b, Obj ... objs)
	{
		// Make the window invisible if there are already windows inserted.
		if(tabs.isEmpty())
		{
			current = b;
			b.disable();
		}
		else
		{
			for(Obj o : objs)
			{
				o.setVisible(false);
				o.disable();				
			}
		}
		
		List<Obj> list = new List<Obj>();
		
		list.append(objs);
		
		tabs.insert(b, list);
		
		obj_create(b);
		
		for(Obj o : objs)
		{
			obj_create(o);
		}

	}
	
	public void gotoTab(gui_button b)
	{
		
		// Lookup the next set of components to become visible.
		List<Obj> objs_next = tabs.lookup(b);
		
		// Can't do anything if the tab does not exist.
		if(objs_next == null)
		{
			return;
		}
	
		
		// Make the current set of components invisible.
		List<Obj> objs_current = tabs.lookup(current);
		
		for(Obj o : objs_current)
		{
			o.setVisible(false);
			o.disable();
		}

		// Make the new set of components visible.		
		for(Obj o : objs_next)
		{
			o.setVisible(true);
			o.enable();
		}
		
		// Allow the current button to be clicked on again.
		current.enable();
		
		// Disallow the user to click on the new button.
		b.disable();
		
		// Update the reference to the current button.
		current = b;
		
	}
	
	@Override
	public void update()
	{
		if(!isEnabled())
		{
			return;
		}
		
		super.update();
		
		// Handle button presses.
		
		for(Obj o : this)
		{
			
			if(o instanceof gui_button)
			{
				
				gui_button b = (gui_button) o;
				
				if(tabs.lookup(b) == null)
				{
					continue;
				}
			
				if(b.flag())
				{
					gotoTab(b);
					break;
				}

			}
		}
		
	}// End of update

	public gui_button getCurrentButton()
	{
		return current;
	}
	
}
