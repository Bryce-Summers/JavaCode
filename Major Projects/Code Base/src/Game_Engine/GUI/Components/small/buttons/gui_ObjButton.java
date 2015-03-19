package Game_Engine.GUI.Components.small.buttons;
import java.util.function.Predicate;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * 
 * Graphic User Interface Obj selection button.
 * 
 * Written by Bryce Summers on 8 - 18 - 2013.
 * 
 * Purpose : This class allows for various objects to be mapped to buttons,
 * 			 so the user can select from a set of premade Objects.
 * 
 * 		- These should be used with another class such as the operator_window that performs logical operations based on the selections.
 * 
 * 
 * NOTE : Their should only be one component in any given room that deals with these buttons,
 * 		  because otherwise the logic will be helplessly confused.
 */

public class gui_ObjButton extends gui_button
{

	// The objects that have been selected.
	private static List<Object> selected = new List<Object>();
	
	// Data that stores information for assigning messages to certain objects that match a predicate. 
	private static Predicate<gui_ObjButton> predicate;
	private static String message;
	private String original_text = "";
	private boolean message_mode = false;
	
	
	Object data = null;
	
	// These are used to visually communicate the links between Obj buttons to the user, 
	// for instance when making row, column, all, and element selections on a grid. 
	private List<Obj> highlights = new List<Obj>();
	
	public gui_ObjButton(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_ObjButton(Rectangle r)
	{
		super(r);
	}
	
	public void setObject(Object o)
	{
		data = o;
	}
	
	public void addHighlights(Obj ... os)
	{
		highlights.append(os);
	}

	// Allows the outside world to derive the selection.
	public static Object deqSelection()
	{
		if(selected.isEmpty())
		{
			return null;
		}
		
		return selected.deq();
		
	}
	
	// Erases the current selection.
	public static void revertSelection()
	{
		selected.clear();
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(flag())
		{
			// Add this button's data to the selection.
			selected.add(data);
		}
		
		// Highlight all of the linked Objs when this button is highlighted.
		if(mouseInRegion)
		{
			for(Obj o : highlights)
			{
				o.highlighted = true;
			}
		}
		
		handle_predicates();

	}
	
	private void handle_predicates()
	{
			
		// Turn the message off.
		if(predicate == null || !predicate.test(this))
		{
			if(message_mode)
			{
				setText(original_text);
				message_mode = false;
			}
			return;
		}
		
		// Turn the message on.
		if(!message_mode)
		{
			message_mode = true;
			original_text = getText();
			setText(message);
		}
	}

	public static boolean selection_empty()
	{
		return selected.isEmpty();
	}
	
	public static void addSelection(Object o)
	{
		selected.add(o);
	}
	
	// Sets a global predicate.
	public static void setPredicate(Predicate<gui_ObjButton> pred, String message)
	{
		predicate = pred;
		gui_ObjButton.message = message;		
	}
	
	public static void removePredicate()
	{
		predicate = null;
	}
	
	// Returns this buttons data.
	// WARNING : This may be rather dangerous.
	public Object getObject()
	{
		return data;
	}
}
