package Game_Engine.GUI.Components.large;

import java.io.PrintStream;

import util.SerialB;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;
import Data_Structures.Structures.HashingClasses.Set;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.SpriteLoader;

/*
 * Gui_list class.
 * 
 * Written by Bryce Summers.
 * Date : 6 - 5 - 2013.
 * 
 * Purpose : This class allows users to specify windows that efficiently
 * 			 render even large graphical lists of data to the screen.
 * 			 This allows blazing speed for large data sets.
 * 
 * Note : This list currently works for the y dimension.
 */

// FIXME : Eliminate the graphical blips.

// FIXME : This class should be perfected.

// FIXME : This class could use some serious work.

// FIXME : The continuous update cycle is a bit clunky and should probably be fixed.

public class gui_list extends gui_window
{
	// The list data and the extra non list object data.
	protected UBA<Obj> data = new UBA<Obj>();
	
	private boolean changed;
	
	// All lists store a list of their operations, for use in the serialization proccess.
	// All operations should be strings, Objs, or other modification functions.
	protected List<Object> operations = new List<Object>();

	// The last coordinate + the height of the last element. 
	
	public gui_list(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_list(Rectangle screen)
	{
		super(screen);
	}
	
	public void add(Obj o)
	{
		// Put the object at the right place.
		o.setX(0);
		o.setY(getMaxH());
		
		// Signal that the dimensions should be recalculated.
		changed = true;

		// Add the object to the data.
		data.add(o);
		
		// Log this mutation of the list.
		operations.add(o);
	}

	public void rem()
	{
		if(data.isEmpty())
		{
			return;
		}

		// Remove the object from the list.
		Obj o = data.rem();
		
		// Signal that the dimensions should be recalculated.
		changed = true;
		
		// Kill the object.
		o.kill();
		
		operations.add("rem()");
	}
	
	public void clear()
	{
		while(!isEmpty())
		{
			rem();
		}
	}
	
	public void deq()
	{
		
		if(data.isEmpty())
		{
			return;
		}

		// Remove the object from the list.
		Obj o = data.deq();
		
		// Signal that the dimensions should be recalculated.
		changed = true;
		
		// Kill the object.
		o.kill();
		
		operations.add("deq()");
	}
	
	public int size()
	{
		return data.size();
	}
	
	public boolean isEmpty()
	{
		return data.isEmpty();
	}
	
	public int getIndex(Obj o)
	{
		return data.getIndex(o);
	}
	
	public Obj getFirst()
	{
		return data.getFirst();
	}
	
	public Obj getLast()
	{
		return data.getLast();
	}

	public void update()
	{
		if(!isEnabled())
		{
			return;
		}
		
		// If this is elsewhere, then the code breaks...
		if(!changed)
		{
			super.update();
			return;
		}
		
		// This does not seems to be a very good algorithmic choice...
		changed = true;
	
		updateObjList();

		super.update();
	
		updateDimensions();

	}
	
	private void updateObjList()
	{
		int startY = world.getView().getViewY();
		
		Set<Obj> current_contents = world.getObjSet();
		
		// Restart the list of objects for the world.
		world.restartObjsList();
		
		int endY = startY + getH();
		
		int current_y = 0;
		
		// FIXME : Improve this with binary search for the first object.
		for(Obj o : data)
		{
			// Update the locations of the objects along the way.
			o.setY(current_y);
			current_y = (int) o.getY2();
			
			if(startY - o.getH() <= o.getY() && o.getY() <= endY)
			{
				obj_create(o);
				
				// Draw new members.
				if(!current_contents.includes(o))
				{
					o.redraw();
				}
			}
		}
		
	}
	
	private void updateDimensions()
	{		
		int current_w = getMaxW(data);
		
		int size = SpriteLoader.gui_borderSize;
		world.getView().setWorldDimensions(current_w - size*2, getMaxH() - size*2);
		
		// Compute the height of the visible portion of the screen.
		int h = getH();
		
		if(scrollH != null)
		{
			h -= scrollH.getH();
		}
		
		// Perhaps I should update this code to work to disable the scrollbars.

		if(getMaxH() > h)
		{
			scrollV();
		}
		else
		{
			unscrollV();
		}
	
		
		if(current_w > getW())
		{
			scrollH();
		}
		else
		{
			unscrollH();
		}

				
	}

	// Returns the maximum x coordinate on the right of an object in the list.
	private int getMaxW(Iterable<Obj> L)
	{
		int w = 0;
		
		for(Obj o : L)
		{
			int w_new = o.getW();
			if(w_new > w)
			{
				w = w_new;
			}
		}
		
		return w;
	}
	
	// Returns the maximum x coordinate on the right of an object in the list.
	public int getMaxH()
	{
		if(data.isEmpty())
		{
			return 0;
		}
		else
		{
			return (int) data.getLast().getY2();
		}
	}
	
	// Allows for sub classes like trees to modify the data.
	protected void updateData(UBA<Obj> data)
	{
		this.data = data;
		updateObjList();
	}
	
	@Override
	public void serializeTo(PrintStream stream)
	{
		for(Object o : operations)
		{
			if(o instanceof String || o instanceof Integer)
			{
				stream.println(o);
				continue;
			}
			
			if(!(o instanceof SerialB))
			{
				throw new Error("Serialization of " + o.getClass() + " is not currently supported.");
			}
			
			SerialB s = (SerialB)o;
			
			// We print the names for root serializations.
			stream.println(s.getSerialName());
			s.serializeTo(stream);
		}
	}

}
