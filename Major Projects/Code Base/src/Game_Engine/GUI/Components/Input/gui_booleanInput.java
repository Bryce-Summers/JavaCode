package Game_Engine.GUI.Components.Input;

import java.awt.Color;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.boxes.gui_boolean;
import Game_Engine.GUI.Sprites.StyleSpec;

public class gui_booleanInput extends gui_boolean
{
	
	// -- Private data.
	protected Color c_true  = StyleSpec.C_BUTTON_DOWN,
					c_false = StyleSpec.C_BUTTON_UP;
	private boolean changed = false;
	
	public gui_booleanInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_booleanInput(Rectangle r)
	{
		super(r);
	}

	// The only method the user has of engaging the change flag.
	@Override
	public void toggle()
	{
		changed = true;
		super.toggle();
		setRestingColor(query() ? c_true : c_false);
	}
	
	// Toggle this input's state without setting off the change flags.
	protected void soft_toggle()
	{
		super.toggle();
		setRestingColor(query() ? c_true : c_false);
	}
	
	// -- input functions.
	
	public boolean input_changed()
	{
		if(changed)
		{
			changed = false;
			return true;
		}
		
		return false;
	}

	//@Override
	public void clearInput()
	{
		setFalse();
	}

	//@Override
	public Boolean getInput()
	{
		return query();
	}

	//@Override
	public boolean query_changed()
	{
		return changed;		
	}
	
	public void setColors(Color true_color, Color false_color)
	{
		c_true  = true_color;
		c_false = false_color;
		
		setRestingColor(query() ? c_true : c_false);
		
	}
}
