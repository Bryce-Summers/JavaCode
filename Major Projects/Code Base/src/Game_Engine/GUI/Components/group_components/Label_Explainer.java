package Game_Engine.GUI.Components.group_components;

import Data_Structures.Structures.HashTable;
import Game_Engine.GUI.Components.Interfaces.TextComponent;
import Game_Engine.GUI.Components.small.gui_label;


/*
 * Label Explainer.
 * 
 * Written by Bryce Summers on 5 - 2 - 2014.
 * 
 * Purpose : This class is used to be a graphical output for a set of Text components that might store text,
 * 			 but not have enough room on the screen to display their data at all times.
 * 
 * If the user wishes this field to display a default message, then they should use the inherited setDefault Text function.
 */


public class Label_Explainer extends gui_label
{

	// Local Data;
	private HashTable<TextComponent> components = new HashTable<TextComponent>(10);
	
	public Label_Explainer(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public void addComponent(TextComponent input)
	{
		components.add(input);
	}
	
	public boolean removeComponent(TextComponent input)
	{
		return components.remove(input);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		// Display the first Active component's text.		
		for(TextComponent t : components)
		{
			if(t.isActive())
			{
				setText(t.getText());
				return;
			}
		}
		
		// Revert the text to the default if no component is active.
		setText(default_text);
	}

	// Allow the user to specify the default text.
	public void setDefaultText(String text)
	{
		default_text = text;
	}
	
}
