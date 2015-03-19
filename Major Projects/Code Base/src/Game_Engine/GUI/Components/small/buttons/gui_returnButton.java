package Game_Engine.GUI.Components.small.buttons;

import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.room_functional;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * gui_returnButton class.
 * 
 * Written by Bryce Summers on 7 - 15 - 2013.
 * 
 * Purpose : The gui return button class triggers a given room to return to a calling room passing the last returned value.
 * 			 If such a room was never called,
 * 			 but is instead the root room,
 * 			 then this button disables itself.
 */

public class gui_returnButton<T> extends gui_button
{
	
	// The room that this returnButton desires to return.
	room_functional<T> room;

	public gui_returnButton(double x, double y, int w, int h, room_functional<T> myRoom)
	{
		super(x, y, w, h);
		handleRoom(myRoom);
	}

	public gui_returnButton(Rectangle r, room_functional<T> myRoom)
	{
		super(r);
		handleRoom(myRoom);
	}

	public void handleRoom(room_functional<T> myRoom)
	{
		room = myRoom;
		}
	
	public void update()
	{
		if(!isEnabled())
		{
			return;
		}

		// We cannot return from the root button.
		if(!room.wasCalled())
		{
			disable();
			return;
		}
			
		if(flag())
		{
			room.Return();
		}
	}

}
