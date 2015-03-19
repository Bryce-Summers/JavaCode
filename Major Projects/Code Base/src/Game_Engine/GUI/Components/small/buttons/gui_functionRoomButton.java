package Game_Engine.GUI.Components.small.buttons;

import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.room_functional;
import Game_Engine.GUI.Components.Input.Input;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * Function Room buttons.
 * 
 * Written by Bryce Summers on 7 - 15 - 2013.
 * 
 * Purpose : These buttons store a sub functional room that 
 * 			 can be called to perform sub operations that result
 * 			 in the production of a returned object of type T.
 * 
 * This class also acts as a heavy weight user input class for type T.
 */

public class gui_functionRoomButton<T> extends gui_button implements Input<T>
{
	private room_functional<T> sub_room;
	
	public gui_functionRoomButton(double x, double y, int w, int h, room_functional<T> room)
	{
		super(x, y, w, h);

		sub_room = room;
	}
	
	// Constructor for use in lists and the like, where the coordinates are manages externally.
	public gui_functionRoomButton(int w, int h, room_functional<T> room)
	{
		super(0, 0, w, h);

		sub_room = room;
	}

	public gui_functionRoomButton(Rectangle r, room_functional<T> room)
	{
		super(r);

		sub_room = room;
		room.iVars();
	}
	
	public void call()
	{
		sub_room.call(getRoom());
	}
	
	@Override
	public void clearInput()
	{
		sub_room.iVars();
	}

	// Provide direct access to the sub room.
	public room_functional<T> getSubRoom()
	{
		return sub_room;
	}
	
	public void setSubRoom(room_functional<T> new_room)
	{
		sub_room = new_room;
	}
	
	@Override
	public T getInput()
	{
		return sub_room.getReturnData();
	}

	// Returns whether the input has changed since the last time this was called.
	@Override
	public boolean input_changed()
	{
		return sub_room.input_changed();
	}
	
	// Handle button presses.
	public void update()
	{
		super.update();
		
		if(flag())
		{
			call();
		}
	}

	// Returns whether the input has changed since the last time input_changed was called.
	@Override
	public boolean query_changed()
	{
		return sub_room.query_changed();
	}

}
