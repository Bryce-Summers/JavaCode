package Projects.Math.rooms;

import BryceMath.Calculations.Colors;
import Game_Engine.Engine.Objs.Room;

import Game_Engine.GUI.Components.large.gui_list;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.buttons.gui_roomChange;

import Projects.Math.Spr;


public class room_credits extends Room 
{
	
	public static int ROW_H = 75;
	
	private gui_list L;
	
	@Override
	public void iObjs()
	{
		int w = getW();
		
		// Create a menu return button.
		gui_roomChange b1 = new gui_roomChange(0, 0, 0, ROW_H, "room_menu");
		b1.setText("Back to Main Menu");
		b1.fitToContents();
		b1.setDepth(-1);
		obj_create(b1);
		
		// Title
		gui_label title = new gui_label(0, 0, getW(), ROW_H);
		title.setText("Credits");
		title.setColor(Colors.C_BLUE_HEADING);
		title.setDepth(0);
		obj_create(title); 

		// Create a list to store the credits.
		L = new gui_list(0, ROW_H, w, getH() - ROW_H);
		obj_create(L);
		
		// Now list the credits
		addCredit("Disclaimer", true);
		addCredit("", false);
		addCredit("This project was funded by Carnegie Mellon's", false);
		addCredit("Undergraduate Research Office.", false);
		addCredit("These results represent the views of the author", false);
		addCredit(" and not those of Carnegie Mellon University.", false);
		addCredit("", false);
		addCredit("Researcher and Developer", true);
		addCredit("", false);
		addCredit("Bryce Summers", false);
		addCredit("", false);
		addCredit("Mentor", true);
		addCredit("", false);
		addCredit("Professor David Kosbie", false);
		addCredit("", false);
		addCredit("Believer and Supporter", true);
		addCredit("", false);
		addCredit("Professor John MacKey", false);
		addCredit("", false);
		addCredit("My Linear Algebra teachers", true);
		addCredit("", false);
		addCredit("Professor Gheorghiciuc and Dr. Osbourne", false);
		addCredit("", false);
		addCredit("Special Thanks for my Laptop", true);
		addCredit("", false);
		addCredit("Candy Levin", false);
		addCredit("", false);
		addCredit("Design and Experience Help", true);
		addCredit("", false);
		addCredit("Judy Brooks and Meg Richards", false);
		addCredit("", false);
		addCredit("Special Thanks for invaluable feedback", true);
		addCredit("", false);
		addCredit("Countless Anonymous Test Users", false);
		addCredit("", false);
		
	}
	
	private void addCredit(String s, boolean heading)
	{
		int w = getW() - Spr.gui_borderSize*2;
		
		gui_label l = new gui_label(0, 0, w, ROW_H);
		if(heading)
		{
			l.setColor(Colors.C_BLUE_HEADING);
			l.setDrawBorders(true);
		}
		l.setText(s);
		L.add(l);
	}

}
