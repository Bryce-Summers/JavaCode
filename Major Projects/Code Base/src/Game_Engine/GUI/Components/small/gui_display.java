package Game_Engine.GUI.Components.small;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj_Container;
import Game_Engine.Engine.Objs.View;

/*
 * Graphic User Interface Display
 * 
 * Written by Bryce Summers on 3 - 11 - 2013
 * 
 * Purpose: This acts as an un bordered window.
 * 			It provides a screen window into a sub game world.
 * 
 */

public class gui_display extends Obj_Container
{
	
	public gui_display(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_display(Rectangle r)
	{
		super(r);
	}


	@Override
	public void iObjs()
	{
		// - Displays come with no standard object set.
	}
	
	public void update()
	{
		super.update();
		if(getView().flag())
		{
			myContainer.redraw();
		}
	}

	@Override
	protected void iViews()
	{
		
		// Now initialize the view.
		Rectangle r1, r2;
				
		// Default World
		r1 = new Rectangle(0, 0, getW(), getH());
		
		// Default screen.
		r2 = new Rectangle((int)getX(), (int)getY(), getW(), getH());
		
		// Add the views to the views list.
		views = new List<View>();
		
		// Add the mutable world view.
		views.add(new View(r2, r1, getW(), getH()));
	}

	// FIXME : These methods will probably break if I ever implement scaling.
	@Override
	public int getScreenX()
	{
		View v = getView();
		
		int vx = v.getViewX();
		int sx = v.getScreenX();
		
		return (int)(getX() + sx - vx + myContainer.getScreenX());
	}

	@Override
	public int getScreenY()
	{
		View v = getView();
		
		int vy = v.getViewY();
		int sy = v.getScreenY();
		
		return (int)(getY() + sy - vy + myContainer.getScreenY());
	}	
}