package Game_Engine.GUI.Components.large;

import BryceMath.Calculations.Colors;
import BryceMath.Numbers.Number;
import BryceMath.Structures.Vector;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.small.gui_handle;

/*
 * Draggable vector class.
 * 
 * Written by Bryce Summers on 7 - 17 - 2013.
 * 
 * Purpose : This class adds a handle to a vector, allowing the vector to be dragged.
 */

// FIXME : I should genarisize these draggable classes.

public class gui_dragVector<T extends Number<T>> extends gui_vector<T>
{
	
	// -- Private data.
	gui_vector<T> vector;
	gui_handle handle;

	// -- Identical constructors to a gui_vector.
	public gui_dragVector(Vector<T> data, int size, boolean hori)
	{
		super(data, size, hori);
	}
	
	// -- Another copied constructor.
	public gui_dragVector(double x, double y, Vector<T> data, int size, boolean hori)
	{
		super(x, y, data, size, hori);
	}
	
	// Used to construct vectors with a precomputed uniform width to the components.
	public gui_dragVector(double x, double y, Vector<T> data, UBA<Integer> widths, int w, int h)
	{
		super(x, y, data, widths, w, h);
	}

	// The object initialization function.
	
	@Override
	public void iObjs()
	{
		super.iObjs();
		
		for(Obj o : this)
		{
			o.setCollidable(false);
		}
		
		handle = new gui_handle(getX(), getY(), getW(), getH());
		handle.setDragColor(Colors.Color_hsv(0, 0, 70, 30));
		handle.setRestingColor(Colors.Color_hsv(0, 0, 0, 0));
		obj_create(handle);
	}
	
	// -- Handle functionality.
	
	public void setMinX(double x_min)
	{
		handle.setMinX((int)x_min);
	}
	
	public void setMaxX(double x_max)
	{
		handle.setMaxX((int)x_max);
	}
	
	public void setMinY(double y_min)
	{
		handle.setMinY((int)y_min);
	}

	public void setMaxY(double y_max)
	{
		handle.setMaxY((int)y_max);
	}
	
	public void fixX()
	{
		fixX(getX());
	}
	
	public void fixY()
	{
		fixY(getY());
	}

	public void fixX(double x_start)
	{
		setMinX(x_start);
		setMaxX(x_start);
	}
	
	public void fixY(double y_start)
	{
		setMinY(y_start);
		setMaxY(y_start);
	}
	
	public gui_handle getHandle()
	{
		return handle;
	}
	
	public void setHoming(boolean flag)
	{
		handle.setHoming(flag);
	}
	
	public void setInterpolationTime(int time)
	{
		handle.setInterpolationTime(time);
	}

}
