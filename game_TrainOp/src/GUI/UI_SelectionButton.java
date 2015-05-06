package GUI;

import java.awt.image.BufferedImage;

/*
 * UI_SelectionButton class.
 * 
 * Written by Bryce Summers on 4/25/2015
 * 
 * Purpose : This class allows for the user to pick between a distinct set of boxes, where only one has the focus.
 * 
 */

public class UI_SelectionButton extends UI_Button
{

	public static UI_SelectionButton focus = null;
	
	// -- Constructor.
	public UI_SelectionButton(int x, int y, String str, BufferedImage ... images)
	{
		super(x, y, str, images);
	}
	
	public void mouseR(int x, int y)
	{
		super.mouseR(x, y);
		
		if(focus != null)
		{
			focus.resetColor();
		}
				
		focus = this;
		setSelectedColor();
	}
	
	public void select()
	{
		mouseR(x + 1, y + 1);
	}

}
