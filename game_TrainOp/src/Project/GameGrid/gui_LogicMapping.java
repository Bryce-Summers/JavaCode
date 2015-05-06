package Project.GameGrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import GUI.OBJ2D;
import GUI.UI_Button;
import Project.fonts.FontManager;
import Project.interfaces.Logic_Block;
import SimpleEngine.SimpleRoom;
import SimpleEngine.interfaces.MouseInput;

/*
 * GUI Logic Mapping class.
 *
 * Written by Bryce Summers on 4/20/2015.
 *
 */

public class gui_LogicMapping extends SimpleRoom implements MouseInput
{
	
	public static final int NUM_INPUTS = 4;
	public static final int NO_INPUT = -1;
	
	private UI_Button[] input_buttons = new UI_Button[NUM_INPUTS];

	// An integer for tracking which button the user is currently mapping.
	private int current_button = NO_INPUT;
	
	// The currently selected Logic_block for which the inputs are being mapped.
	private Logic_Block current_block = null;
	
	private final static int h = 62;
	
	// -- Constructor.
	public gui_LogicMapping(int x, int y)
	{
		super(x, y, h*4, h);

		Color c = new Color(255, 255, 255);
		BufferedImage image = ImageFactory.ColorRect(c, h, h);
		
		for(int i = 0; i < NUM_INPUTS; i++)
		{
			UI_Button button = new UI_Button(x + h*i, y, "" + i, image);
			final int index = i; 
			button.setAction(() -> (current_button = index));
			button.setFont(FontManager.font_smaller);
			input_buttons[i] = button;
			addOBJ(button);
		}
	}
	
	// Assigns the input buttons to the given input block.
	public void setCurrentLogicBlock(Logic_Block block)
	{
		current_block = block;
		String[] values = block.getInputNames();
		
		// Set the correct names to as many input buttons as possible.
		int len = Math.min(NUM_INPUTS, values.length);
		for(int i = 0; i < len; i++)
		{
			input_buttons[i].setText(i + ": " + values[i]);
		}
		
		// Clear the Remaining input buttons.
		for(int i = len; i < NUM_INPUTS; i++)
		{
			input_buttons[i].setText("");
		}
		
	}
	
	public Logic_Block getCurrentLogicBlock()
	{
		return current_block;
	}

	// Returns the current input that wants to be mapped.
	public int getCurrentButton()
	{
		return current_button;
	}
	
	public void reset_input()
	{
		current_button = NO_INPUT;
	}
	
	public void setX(int x)
	{
		super.setX(x);
		for(UI_Button b : input_buttons)
		{
			b.setX(x);
			x += h;
		}
	}
	
	public void setY(int y)
	{
		super.setY(y);
		for(UI_Button b : input_buttons)
		{
			b.setY(y);
		}
	}
}
