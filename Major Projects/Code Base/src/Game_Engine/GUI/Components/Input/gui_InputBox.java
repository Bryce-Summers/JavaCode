package Game_Engine.GUI.Components.Input;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.boxes.Selectable;
import Game_Engine.GUI.Components.small.boxes.gui_focusBox;

/*
 * gui_InputBox class.
 * 
 * Written by Bryce Summers on 6 - 28 - 2013.
 * 
 * Purpose : This class provides an abstraction for all of the various low level boxes that can receive input.
 *
 *	NOTE : More complicated structures, such as like rationalInputs and ExpressionInput boxes tend to contain a collection of these low level boxes.
 */

public abstract class gui_InputBox<T> extends gui_focusBox implements keyInput<T>, Selectable
{

	public gui_InputBox(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_InputBox(Rectangle r)
	{
		super(r);
	}

	// Input boxes desire the user to input data through key strokes.
	public abstract void forceKeyP(int key);
	
	public void keyP(int key)
	{
		if(isSelected())
		{
			forceKeyP(key);
		}
	}

}
