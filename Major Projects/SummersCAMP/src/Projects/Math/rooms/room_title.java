package Projects.Math.rooms;

import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_output;
import Game_Engine.GUI.Components.small.gui_label;
import Projects.Math.openinganimation.Animation;

/*
 * The title animation and routing room.
 * 
 * Written by Bryce Summers on 8 - 25 - 2013.\
 * 
 * Purpose :
 * 		- Displays the name of the program.
 * 		- Displays a pleasing complementary animation.
 * 		- Routes the user to the main menu.
 */

public class room_title extends Room
{

	public room_title()
	{
		animation_preference = Animation_preference.FULL;
	}

	public room_title(Game_output out)
	{
		super(out);
		
		animation_preference = Animation_preference.FULL;
		
		enterFullAnimationMode();
	}

	@Override
	public void iObjs()
	{
		int w = getW();
		int h = getH();
		
		gui_label title = new gui_label(0, 0, w, h/2);
		title.setText("Summers CAMP");
		title.makeTransparent();
		title.setTextSize(w/11);
		obj_create(title);
		
		gui_label instructions = new gui_label(0, h/2, w, h/2);
		instructions.setText("Click Anywhere!");
		instructions.setTextSize(w/26);
		instructions.makeTransparent();
		obj_create(instructions);
		
		Animation anim = new Animation();
		anim.setDepth(5);
		obj_create(anim);
	}

	// The main purpose of this room is to route the user to the main menu room.
	private void action()
	{
		room_goto("room_menu");
	}
	
	@Override
	public void keyP(int key)
	{
		action();
	}
	
	@Override
	public void global_mouseP()
	{
		action();		
	}

}
