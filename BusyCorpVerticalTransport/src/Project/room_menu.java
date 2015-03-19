package Project;

//import Data_Structures.Structures.UBA;
import java.io.File;

import util.Web;

import BryceMath.Calculations.Colors;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Room;
//import Game_Engine.Engine.Objs.actionLogging.Command;
import Game_Engine.Engine.engine.Game_output;

import Game_Engine.GUI.Components.group_components.Label_Explainer;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.buttons.gui_functionRoomButton;
import Game_Engine.GUI.Components.small.buttons.gui_roomChange;
import Game_Engine.GUI.Components.small.buttons.gui_exit;


/*
 * Main menu class.
 * 
 * Written by Bryce Summers in the year of 2013.
 * Informative comments written on 8 - 13 - 2013.
 * 
 * Purpose : 
 * 	- Provides a list of buttons that allow the user to direct the program to where they want to go.
 */

public class room_menu extends Room
{
	
	
	// The initial room constructor.
	public room_menu(Game_output out)
	{
		super(out);
	
		
	}
	
	public room_menu()
	{
		/* Used to implement generic rooms. */
	}
	
	@Override
	public void iObjs()
	{
			
		
	}
	
	public void update()
	{
		super.update();
	}

}
