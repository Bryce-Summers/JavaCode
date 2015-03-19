package Game_Engine.levelEditor.editor_components;

import java.awt.image.BufferedImage;

import Game_Engine.GUI.Components.large.gui_list;
import Game_Engine.levelEditor.entities.Ent_aaScalable;


/*
 * Entity Selector Abstract class.
 * 
 * This provides useful functions for creating entity objects in an editor such as x tiled structure, block, and just normal objects.
 * 
 * Entities are images associated with coordinate data that represent where objects will be initialized during the game.
 * They allow the level designer to design the game without needing to boot up the game engine.
 */

public abstract class EntitySelector
{
	// Can be overridden in subclasses to add more entities.
	protected enum Ent_Type
	{
		EXPAND, NORMAL, ANGLE_NODE;
	}

	// Specify entity creation.
	public abstract void populate_potential_objects(gui_list obj_selector);
	
	// Create a button with no text parameter.
	protected obj_button I_BUTTON(int x, int y, int w, int h, BufferedImage spr, String name, Ent_Type e)
	{
		return I_BUTTON(x, y, w, h, spr, name, e, "");
	}
	
	// Create a button while including a text parameter.
	protected obj_button I_BUTTON(int x, int y, int w, int h, BufferedImage spr, String name, Ent_Type e, String input_text)
	{
		obj_button output = new obj_button(x, y, w, h);
		output.setImage(spr);
		output.setText(input_text);
		output.set_text_and_image(true);
		output.setTextSize(10);
		
		obj_entity ent;
		
		switch(e)
		{
			case EXPAND:
				ent = new Ent_aaScalable(x, y, spr, name, input_text);
				break;
			case ANGLE_NODE:
				ent = new Ent_aaScalable(x, y, spr, name, input_text);
			case NORMAL:
				throw new Error("Not yet Supported");
				
			default:
				ent = getSpecialProjectEntity(x, y, w, h, spr, name, e, input_text);
				break;
				
		}
		
		// Tell the button what its entity is.
		output.setEntity(ent);
		
		return output;
	}
	
	// Allows for specific project like Hoth 3.0 to specify special entities. 
	protected abstract obj_entity getSpecialProjectEntity
									(int x, int y, int w, int h,
									BufferedImage spr, String name, 
									Ent_Type e, String input_text);
	
}

