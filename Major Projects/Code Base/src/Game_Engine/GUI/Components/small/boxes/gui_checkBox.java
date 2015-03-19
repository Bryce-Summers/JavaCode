package Game_Engine.GUI.Components.small.boxes;

import java.awt.image.BufferedImage;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.Input.gui_booleanInput;

/*
 * gui_checkBox class.
 * 
 * Written by Bryce Summers during the summer of 2013.
 * 
 * Purpose : This class provides support for a box that maintains two
 * 			 separate sets of state information for its "true" and
 * 			 "false" states.
 */

public class gui_checkBox extends gui_booleanInput
{

	protected BufferedImage primary_image;
	protected BufferedImage secondary_image = SpriteLoader.check_mark_symbol;
	
	private String primary_message = "";
	private String secondary_message = "";
	
	public gui_checkBox(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_checkBox(Rectangle r)
	{
		super(r);
	}

	@Override
	public void toggle()
	{
		super.toggle();

		if(!query())
		{
			return;
		}
		
		if(secondary_message.equals(""))
		{
			setImage(secondary_image);
		}
		else
		{
			setText(secondary_message);
		}
		
	}
	
	@Override
	public void setFalse()
	{
		super.setFalse();

		setText(primary_message);
		setImage(primary_image);
	}
	
	public void setMessages(String primary, String secondary)
	{
		primary_message   = primary;
		secondary_message = secondary;
		
		if(query())
		{
			setText(secondary_message);
			setImage(null);
		}
		else
		{
			setText(primary_message);
		}
	}
	
	public void setImages(BufferedImage primary, BufferedImage secondary)
	{
		primary_image = primary;
		secondary_image = secondary;
		
		if(query())
		{
			setImage(secondary_image);
		}
		else
		{
			setImage(primary_image);
		}
	}	
	
}
