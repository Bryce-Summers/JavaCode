package Game_Engine.GUI.Components.sets;

/*
 * Button Set. 
 * 
 * This class allows for arbitrary buttons to be related by an ordering such that when each button is pressed, its index is returned through this set.
 * 
 * This enables for button arrays to be easily managed.
 * 
 * This descriptive text written on 4 - 10 - 2014.
 */

import java.util.Iterator;

import Data_Structures.Structures.UBA;
import Game_Engine.GUI.Components.small.gui_button;

public class Button_set implements Iterable<gui_button>
{
	// -- Private data.
	
	UBA<gui_button> data = new UBA<gui_button>();
	
	boolean visible = true;
	boolean enabled = true;
	
	public Button_set()
	{}

	public void add(gui_button b)
	{
		data.add(b);
	}
	
	public gui_button get(int index)
	{
		return data.get(index);
	}
	
	public UBA<gui_button> getData()
	{
		return data;
	}

	// Passes on the errors of removing a non existent row.
	// Allows the user to remove the last button from the association.
	public gui_button rem()
	{
		return data.rem();
	}

	// Returns the index of the button that is pressed, if it exists.
	// Returns -1 otherwise.
	public int flag()
	{		
		int len = data.size();
		
		for(int i = 0; i < len; i++)
		{
			if(data.get(i).flag())
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public void setVisible(boolean flag)
	{
		visible = flag;
		
		for(gui_button b : data)
		{
			b.setVisible(flag);
		}
	}
	
	public boolean getVisible()
	{
		return visible;
	}
	
	public void disable()
	{
		enabled = false;
		
		for(gui_button b : data)
		{
			b.disable();
		}
	}
	
	public void enable()
	{
		
		enabled = true;
		
		for(gui_button b : data)
		{
			b.enable();
		}
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public Iterator<gui_button> iterator()
	{
		return data.iterator();
	}
	
}
