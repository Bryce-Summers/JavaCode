package BryceGraphs.gui_components;

import BryceMath.Calculations.Colors;
import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.View;
import Game_Engine.GUI.Components.large.gui_window;

/*
 * Exploration Window.
 * 
 * Written by Bryce Summers during the writing of the BryceGraphs library.
 * This comment written on 12 - 18 - 2014 well after the code was written.
 * 
 * Purpose: This class implements a gui_window that represents an 
 *          infinite region that the user can explore by dragging.
 *          
 * Update: 12 - 18 - 2014.
 * 		I have made it so that the user can make
 * 		the window scrollable in only one direction.
 */


public class gui_explorationWindow extends gui_window
{
	
	private int mouseX, mouseY;
	private int worldX, worldY;
	
	private boolean fixX = false, fixY = false;
	
	// -- Constructors.
	public gui_explorationWindow(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_explorationWindow(Rectangle screen, Rectangle sub_world, int worldW,
			int worldH)
	{
		super(screen, sub_world, worldW, worldH);
	}
	
	@Override
	public void iObjs()
	{
		super.iObjs();
		
		background.setColor(Colors.C_GRAY2);
	}
	
	public void mouseP(int mx, int my)
	{
		super.mouseP(mx, my);
		
		mouseX = mx;
		mouseY = my;
		
		View v = world.getView();
		worldX = v.getViewX();
		worldY = v.getViewY();
	}
	
	public void mouseD(int mx, int my)
	{
		super.mouseD(mx, my);
		
		if(!mouse_right())
		{
			return;
		}
		
		View v = world.getView();
		
		if(!fixX)
		{
			v.setViewX(worldX - mx + mouseX);
		}
		
		if(!fixY)
		{
			v.setViewY(worldY - my + mouseY);
		}
	}
	
	public void fixX()
	{
		fixX = true;
	}
	
	public void fixY()
	{
		fixY = true;
	}

}
