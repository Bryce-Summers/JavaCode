package Game_Engine.GUI.Components.sets;

import java.awt.image.BufferedImage;

import Data_Structures.ADTs.Pairable;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.boxes.gui_boolean;
import Game_Engine.GUI.Components.small.boxes.gui_checkBox;

/*
 * Secondary Button Set.
 * 
 * Written by Bryce Summers on 7 - 8 - 2013.
 * 
 * Purpose : This class allows for a boolean button to be clicked be clicked on.
 */

public class Secondary_button_set extends Obj_union
{
	
	UBA<gui_checkBox> primary_buttons = new UBA<gui_checkBox>();
	UBA<gui_button>  secondary_buttons = new UBA<gui_button>();
	
	BufferedImage secondary_image = null;

	int matrixH, matrixW;
	
	private boolean secondary_visible = true;
	
	private int swap1;
	
	public Secondary_button_set(double x, double y, int w, int h, int matrixW, int matrixH)
	{
		super(x, y, w, h);
		
		if(matrixH < 1 || matrixW < 1)
		{
			throw new Error("Buttons sets cannot have trivial sizes.");
		}
		
		this.matrixH = matrixH;
		this.matrixW = matrixW;
	}

	@Override
	public void iObjs()
	{
		for(int r = 0; r < matrixH; r++)
		for(int c = 0; c < matrixW; c++)
		{
			gui_checkBox b = new gui_checkBox((int)(getX() + getW()* 1.0 *c / matrixW),
											  (int)(getY() + getH()* 1.0 *r / matrixH),
											  getW()/matrixW, getH()/matrixH);
			obj_create(b);
			primary_buttons.add(b);
			
			gui_button b2 = new gui_button(b.getX(), b.getY(), b.getW(), b.getH());
			secondary_buttons.add(b2);
			obj_create(b2);
			
			b2.setImage(secondary_image);
		}
		
		hide_secondary_buttons();
		
	}
	
	public void update()
	{
		int len = primary_buttons.size();
		
		for(int i = 0; i < len; i++)
		{
			gui_boolean b = primary_buttons.get(i);
			
			if(b.query())
			{
				swap1 = i;
				
				if(!secondary_visible)
				{
					show_secondary_buttons(i);
				}
				
				return;
			}
		}
		
		// If none were found . . .

		swap1 = -1;
		
		if(secondary_visible)
		{
			hide_secondary_buttons();
		}
	}
	
	public void show_secondary_buttons(int skip_index)
	{
		
		secondary_visible = true;
		
		// Hide the extra primary buttons.
		for(gui_boolean b : primary_buttons)
		{
			b.setVisible(false);
		}

		// Show the secondary buttons.
		for(gui_button b : secondary_buttons)
		{
			b.setVisible(true);
		}

		// Make the button that is over the selected boolean invisible.
		primary_buttons.get(skip_index).setVisible(true);
		
		// Make the button that is over the selected boolean invisible.
		secondary_buttons.get(skip_index).setVisible(false);
	}

	private void hide_secondary_buttons()
	{
		secondary_visible = false;
		
		for(gui_button b : secondary_buttons)
		{
			b.setVisible(false);
		}
		
		for(gui_boolean b : primary_buttons)
		{
			b.setVisible(true);
		}
		
	}
	
	public void reset()
	{
		for(gui_boolean b : primary_buttons)
		{
			b.setFalse();
		}

		hide_secondary_buttons();
	}
	
	// Returns the two indicated swaps if they exist, returns null otherwise.
	public Pairable<Integer> getSwaps()
	{
		int i = 0;
		for(gui_button b : secondary_buttons)
		{
			if(b.flag())
			{
				List<Integer> output = new List<Integer>();
				output.append(swap1, i);
				return output;
			}
			i++;
		}
		
		return null;
	}
	
	public int getSwap1()
	{
		return swap1;
	}
	
	public void setMessages(String primary1, String primary2, String secondary)
	{
		
		if(!initialized)
		{
			initialize();
		}
		
		for(gui_checkBox b: primary_buttons)
		{
			b.setMessages(primary1, primary2);
		}
		
		for(gui_button b : secondary_buttons)
		{
			b.setText(secondary);
		}
	}
	
	public void set_image_secondary(BufferedImage image)
	{
		secondary_image = image;
	}
	
	public UBA<gui_checkBox> get_primary_buttons()
	{
		return primary_buttons;
	}
	
	public UBA<gui_button> get_secondary_buttons()
	{
		return secondary_buttons;
	}
	
}
