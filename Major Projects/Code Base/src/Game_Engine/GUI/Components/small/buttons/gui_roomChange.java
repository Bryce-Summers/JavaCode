package Game_Engine.GUI.Components.small.buttons;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * A basic button that sends the game to a specified room.
 * Written by Bryce Summers on 6 - 5 - 2013.
 */

public class gui_roomChange extends gui_button
{

	private String room_goto;
	
	public gui_roomChange(int w, int h, String room)
	{
		super(0, 0, w, h);
		iVars(room);
	}
	
	public gui_roomChange(double x, double y, int w, int h, String room)
	{
		super(x, y, w, h);
		iVars(room);
	}

	public gui_roomChange(Rectangle r, String room)
	{
		super(r);
		iVars(room);
	}
	
	private void iVars(String room)
	{
		room_goto = room;
		disableTEX();
		setText(room);
	}
	
	public void update()
	{
		super.update();
		
		// Handle clicks.
		if(flag())
		{
			room_goto(room_goto);
		}
	}
	
	
	// Allow users to specify a new room.
	public void setRoom(String room_new)
	{
		room_goto = room_new;
	}
	

}
