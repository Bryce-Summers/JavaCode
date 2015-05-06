package GUI;

import java.awt.image.BufferedImage;

import SimpleEngine.SimpleRoom;

/*
 * The gui_List class used in my simple Graphic User Interface.
 * 
 * Written by Bryce Summers on 3/19/2015.
 * 
 * Purpose : Allows me to create a list of Buttons.
 */

public class UI_List extends SimpleRoom
{
	int x;
	
	private int y_next;
	
	public enum DIR{DOWN, UP};
	private final DIR myDir;
	
	public UI_List(int x, int y, int w, int h, DIR dir)
	{
		super(w, h);
		
		y_next = y;
		this.x = x;
		
		myDir = dir;
	}
	
	public UI_Button newButton(String str, BufferedImage image)
	{
		UI_Button output = new UI_Button(x, y_next, str, image);
		
		switch(myDir)
		{
			case DOWN: y_next += image.getHeight();
				break;
			case UP: y_next -= image.getHeight();
				break;
		}
		
		
		addOBJ(output);
		return output;
	}



	
}
