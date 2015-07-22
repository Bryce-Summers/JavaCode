package Project.Editor.Components;

import java.awt.image.BufferedImage;

import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.large.gui_list;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.levelEditor.editor_components.EntitySelector;
import Game_Engine.levelEditor.editor_components.obj_button;
import Game_Engine.levelEditor.editor_components.obj_entity;

/*
 * Entities class, 
 * 
 * Written by Bryce Summers on 10 - 12 - 2013.
 * 
 * specifies the list of buttons and their parameters for creating entities.
 */

public class Entities extends EntitySelector
{

	// Specify entity creation.
	public void populate_potential_objects(gui_list obj_selector)
	{
		int w = obj_selector.getWorld().getW();
		
		obj_button b;
		

		int button_h = 64 + 4;
		
		// Game components.
		
		// Ground squares.
		b = I_BUTTON(0, 0, w, button_h, Spr.ground_square, "obj_ground_square", Ent_Type.EXPAND);
		obj_selector.add(b);
		
		// Windows for holding objects of various types.
		gui_window W1, W2, W3, W4, W5, W6;
		
		int bSize = SpriteLoader.gui_borderSize;
		
		W1 = new gui_window(0, 0, obj_selector.getW(), 64 + bSize*4);
		W2 = new gui_window(0, 0, obj_selector.getW(), 64 + bSize*4);
		W3 = new gui_window(0, 0, obj_selector.getW(), 128 + bSize*4);
		W4 = new gui_window(0, 0, obj_selector.getW(), 64 + bSize*4);
		W5 = new gui_window(0, 0, obj_selector.getW(), 64 + bSize*4);
		W6 = new gui_window(0, 0, obj_selector.getW(), 64 + bSize*4);
		obj_selector.add(W1);
		obj_selector.add(W2);
		obj_selector.add(W3);
		obj_selector.add(W4);
		obj_selector.add(W5);
		obj_selector.add(W6);
		
		int button_w = obj_selector.getW()/5;
		
		for(int i = 0; i < 5; i++)
		{
			b = I_BUTTON(i*button_w, 0, button_w, button_h, Spr.mover[i], "obj_piece" + i, Ent_Type.EXPAND, "");
			W1.obj_create(b);
			
			b = I_BUTTON(i*button_w, 0, button_w, button_h, Spr.goal[i], "obj_goal" + i, Ent_Type.EXPAND, "");
			W2.obj_create(b);
			
			
			b = I_BUTTON(i*button_w, 0, button_w, button_h, Spr.teleporter[i], "obj_teleporterPad" + i, Ent_Type.EXPAND);
			W4.obj_create(b);
			
			b = I_BUTTON(i*button_w, 0, button_w, button_h, Spr.teleporter_target_mover[i], "obj_teleporter_target_mover" + i, Ent_Type.EXPAND);
			W5.obj_create(b);
			
			b = I_BUTTON(i*button_w, 0, button_w, button_h, Spr.teleport_target_teleporter[i], "obj_teleport_target_teleporter" + i, Ent_Type.EXPAND);
			W6.obj_create(b);
		}
		
		// -- Blocks.
		button_w = obj_selector.getW()/4;
		for(int i = 0; i < 2; i++)
		{
			Ent_Type type = Ent_Type.EXPAND;
			
			for(int row = 0; row < 2; row++)
			{
				/* Create straight wall segments. 
				 * 2 = offset for second row of straights.
				 * */
				int index = i + 2*row;
				b = I_BUTTON(i*button_w, row*64, button_w, button_h, Spr.wall[index], "obj_wall" + index, type);
				W3.obj_create(b);
											
				/* Create curved wall segments. 
				 * index = offset to curves + local offset.*/
				index = 4 + i + 2*row;
				b = I_BUTTON((i + 2)*button_w, row*64, button_w, button_h, Spr.wall[index], "obj_wall" + index, Ent_Type.EXPAND);
				W3.obj_create(b);
			}
			

		}
		
	}

	@Override
	protected obj_entity getSpecialProjectEntity(int x, int y, int w, int h,
			BufferedImage spr, String name, Ent_Type e, String input_text)
	{
		throw new Error("No special Entities exist for the teleporters game!");
	}
}
