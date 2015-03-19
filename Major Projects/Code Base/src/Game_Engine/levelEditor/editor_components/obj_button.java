package Game_Engine.levelEditor.editor_components;

import BryceMath.Calculations.Colors;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * 10 - 1 - 2013.
 * Object creation buttons for the level editor.
 */

public class obj_button extends gui_button
{
	// The global reference entity for entity creation.
	public static obj_entity current_entity;
	
	private obj_entity my_entity;

	public obj_button(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	
	public void setEntity(obj_entity input)
	{
		my_entity = input;
	}
	
	// Buttons update the current entity creation when selected.
	public void update()
	{
		super.update();
		
		if(flag())
		{
			current_entity = my_entity;
		}
		
		// Highlight the selected button.
		if(current_entity == my_entity)
		{
			setColor(Colors.C_BLUE_HEADING);
		}
	}

}
