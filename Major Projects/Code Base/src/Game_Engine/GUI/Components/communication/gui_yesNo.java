package Game_Engine.GUI.Components.communication;

import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * GUI Yes, no buttons.
 * 
 * Written by Bryce Summers on 8 - 30 - 2014.
 * 
 * Purpose : These buttons are meant to 
 */

public class gui_yesNo extends gui_infoBox
{

	// Operation buttons.
	final boolean yesFirst;
	gui_button yes, no;
	
	// -- Constructors.
	public gui_yesNo(double x, double y, int w, int h, String str, Obj target, boolean yesFirst)
	{
		super(x, y, w, h, str, target);
		this.yesFirst = yesFirst;
	}

	public gui_yesNo(Rectangle screen, String str, Obj target, boolean yesFirst)
	{
		super(screen, str, target);
		this.yesFirst = yesFirst;
	}

	@Override
	public void populateContent(int x, int y, int w, int h)
	{
		// Create the paragraph.
		super.populateContent(x, y, w, h);
		
		yes = new gui_button(x, y + h - 50,w/2, 50);
		yes.setText("Yes");
		obj_create(yes);
		
		no = new gui_button(x, y + h - 50,w/2, 50);
		no.setText("No");
		obj_create(no);
		
		// Make one of the buttons on the right.
		int x2 = x + w/2;
		if(yesFirst)
		{
			no.setX(x2);
		}
		else
		{
			yes.setX(x2);
		}
	}
	
	
	// -- Interface functions.
	
	
	// Returns true iff the yes button has been clicked on since the 
	// last time the yes button was flag queried.
	public boolean flagYes()
	{
		return yes.flag();
	}

	// Returns true iff the no button has been clicked on since the 
	// last time the no button was flag queried.
	public boolean flagNo()
	{
		return no.flag();
	}

}
