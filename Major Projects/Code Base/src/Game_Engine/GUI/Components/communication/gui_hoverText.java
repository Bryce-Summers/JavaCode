package Game_Engine.GUI.Components.communication;

import java.awt.Color;
import java.awt.image.BufferedImage;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.Timing.TimeB;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_input;
import Game_Engine.GUI.Components.small.gui_label;

/*
 * Hover text class.
 * 
 * Written by Bryce Summers on 7 - 22 - 2014.
 * 
 * Purpose : This element displays a message to the user when they hover over the given element.
 */

public class gui_hoverText extends gui_label
{
	Obj element = this;
	private boolean enoughTime = false;
	TimeB on  = new TimeB(30);
	
	// Constructors.
	public gui_hoverText(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		iVars();
	}
	
	public gui_hoverText(int x, int y, BufferedImage image)
	{
		super(x, y, image);
		iVars();
	}
	
	public gui_hoverText(Rectangle r, Obj element)
	{
		super(r);
		iVars();
	}
	
	private void iVars()
	{
		setDrawBorders(true);
		setColor(Color.white);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(!enoughTime && on.flag())
		{
			enoughTime = true;
		}
		
		if(enoughTime && element != null && element.mouseInRegion &&
			element.isEnabled() && element.isVisible() && element.isCollidable())
		{
			if(!isVisible())
			{
				show();
				updateX();
			}
		}
		else
		{
			hide();
			
			enoughTime = false;
		}
	}
	
	public void setObj(Obj o, String message)
	{
		setText(message);
		fitToContents();
		
		this.element = o;
		setDepth(Integer.MIN_VALUE);
		Room room = o.getRoom();
		room.obj_create(this);
		
		
		int x = Game_input.mouse_x;
		int y = o.getScreenY();

		int my = y + o.getH() + 16;
		
		// Compute where to position the info box.
		if(my > room.getH() - getH())
		{
			my = y - getH() - 16;
		}
				
		int max_x = room.getW() - getW();
		moveTo(Math.min(max_x, x), my);
		
		// Start waiting.
		enoughTime = false;
		on.restartTime();
	}

	public void updateX()
	{
		int x = Game_input.mouse_x;
		int max_x = getRoom().getW() - getW();
		setX(Math.min(x, max_x));
	}
	
	public Obj getOBj() 
	{
		return element;
	}

}
