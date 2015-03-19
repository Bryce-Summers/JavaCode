package Game_Engine.GUI.Components.small.boxes;

import java.awt.Color;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Sprites.StyleSpec;

/*
 * gui_selectionBox.
 * 
 * Written by Bryce Summers on 6 - 6 - 2013.
 * 
 * Purpose : Creates boxes that can have the unique focus.
 * 
 * FIXME The color logic for this class needs to be majorly cleaned up.
 */

public class gui_focusBox extends gui_boolean
{
	
	// Only one focus box in the world can have the selection.
	public static gui_focusBox selection;
	public static gui_focusBox last_selection;

	public gui_focusBox(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_focusBox(Rectangle r)
	{
		super(r);
	}

	public void toggle()
	{
		// Untoggle the current selection,
		// toggle this button,
		// then update the reference to the current selection.
		if(!query())
		{
			if(selection != null)
			{
				selection.setFalse();
			}
			
			super.toggle();
			selection = this;
			last_selection = this;
			setRestingColor(StyleSpec.C_BOX_SELECTED);
			return;
		}
		
		// Do nothing if this box is already the selection.
		return;
		
		
	}
	
	@Override
	protected Color Color_mouse_in_region()
	{
		return StyleSpec.C_BOX_SELECTED;
	}
	
	public void setFalse()
	{
		super.setFalse();
		setRestingColor(StyleSpec.C_BOX_NOT_SELECTED);
	}
	
	// Unfocus this box if the user right clicks elsewhere.
	// FIXME : It might actually be undesirable to unfocus the box.
	public void global_mouseP()
	{
		super.global_mouseP();
		
		if(!mouseInRegion)
		{
			setFalse();
			if(selection == this)
			{
				selection = null;
				last_selection = this;
			}
		}
	}
	
	public void setVisible(boolean flag)
	{
		super.setVisible(flag);
		
		if(flag == false)
		{
			if(selection == this)
			{
				toggle();
			}
		}
	}
	
	// Returns true if and only if this focus box is selected.
	public boolean isSelected()
	{
		return selection == this;
	}

	@Override
	protected void die()
	{
		if(isSelected())
		{
			selection = null;
		}
		
		super.die();
	}
	
	public static void revertSelection()
	{
		if(selection == null)
		{
			return;
		}
		
		selection.setFalse();
		selection = null;
	}
	
	/**This function is useful for selection boxes that are acted on via an external button press.
	 * @return Returns true iff this element is the last selection.
	 */
	public boolean isLastSelection()
	{
		return this == last_selection;
	}
	
	// Erases all state associated with focus Boxes.
	public static void eraseSelections()
	{
		selection = null;
		last_selection = null;
	}
}
