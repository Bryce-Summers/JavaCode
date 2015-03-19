package Game_Engine.GUI.Components.small;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Interfaces.Pingable;
import Game_Engine.GUI.Sprites.StyleSpec;

/*
 * Scroll bar for my Graphic User Interface.
 * Updated by Bryce Summers 1 - 13 - 2013.
 * Purpose: Provides a visual scroll bar that moves along a given
 * 			fixed track that can communicate what percentage of
 * 			its track that it has currently been moved.
 * 
 *  		This scroll bar has proper capabilities for two dimensions.
 *
 *	Note : 
 *
 *	This class was reduced and transcribed into a gui_window implementation
 *	during beginning of the summer of 2013.
 *
 *  UPDATED : 8 - 21 - 2013.
 *  	- I have added in capabilities for a scrollbar to set itself to a given percentage.
 */


public class gui_scrollbar extends gui_window implements Pingable
{

	// Private data.
	gui_handle bar;
	gui_label background;
	
	// Positioning geometric data.
	private static final int border_size = SpriteLoader.gui_borderSize;
	private static final int min_x = -border_size; //(int)x + border_size;
	private static final int min_y = -border_size; //(int)y + border_size;

	public gui_scrollbar(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_scrollbar(Rectangle r)
	{
		super(r);
	}

	@Override
	public boolean flag()
	{
		return bar.flag();
	}

	@Override
	public void setFlag(boolean flag)
	{
		bar.setFlag(flag);
	}

	@Override
	public void iObjs()
	{
		super.iObjs();
	
		formBar();
		
	}
	
	// Computes the dimensions for all of the bar's components.
	private void formBar() 
	{
		// Compute the width and height of the scrollbar.
		int w = getW();
		int h = getH();
		
		if(w > h)
		{
			w = w/2;
		}
		else
		{
			h = h/2;
		}
		
		if(w < border_size*4)
		{
			w = border_size*4;
		}
		
		if(h < border_size*4)
		{
			h = border_size*4;
		}
		
		if(bar != null)
		{
			bar.kill();
		}
		
		bar = new gui_handle(min_x, min_y, w, h);

		int max_x = getMaxX();
		int max_y = getMaxY();
		
		bar.setMinX(min_x);
		bar.setMinY(min_y);
		bar.setMaxX(max_x);
		bar.setMaxY(max_y);
		bar.setFlag(true);

		bar.setRestingColor(StyleSpec.C_SCROLLBAR_BAR);
		
		obj_create(bar);
		
		// Create the background;
		
		if(background != null)
		{
			background.kill();
		}
		
		background = new gui_label(- border_size, - border_size, getW(), getH());
		background.setDepth(1);
		
		// Window color.
		background.setColor(StyleSpec.C_SCROLLBAR_BACKGROUND);
		obj_create(background);		
	}

	private int getMaxX()
	{
		return getW() - bar.getW() - border_size;
	}
	
	private int getMaxY()
	{
		return getH() - bar.getH() - border_size;
	}
	
	/**
	 * 
	 * @return This function returns the indicated x value of this scrollbar as a percentage from 0 to 1.0
	 */
	public double getXValue()
	{
		return bar.getXPercentage();
	}

	/**
	 * @return This function returns the indicated y value of this scrollbar as a percentage from 0 to 1.0
	 */
	public double getYValue()
	{
		return bar.getYPercentage();
	}

	
	// Allow outside sources to increase or decrease this scrollbar's bar location.
	public void scroll(int amount)
	{
		bar.setY(bar.getY() + amount*getH()/10);
		
		bar.ensureBounds();
		setFlag(true);
	}

	public void setXValue(double percentage)
	{
		bar.setX(min_x + (getMaxX() - min_x)*percentage);
		
		// Indicate to the outside world that the change took place.
		setFlag(true);
	}
	
	// REQUIRES : The percentage of scroll desired.
	public void setYValue(double percentage)
	{
		bar.setY(min_y + (getMaxY() - min_y)*percentage);
		
		// Tell the outside world that the change took place.
		setFlag(true);
	}
	
	
	public void setW(int w)
	{
		double percentage = getYValue();
		super.setW(w);
		formBar();
		setXValue(percentage);
	}
	
	public void setH(int h)
	{
		double percentage = getYValue();
		super.setH(h);
		formBar();
		setYValue(percentage);
	}
}

