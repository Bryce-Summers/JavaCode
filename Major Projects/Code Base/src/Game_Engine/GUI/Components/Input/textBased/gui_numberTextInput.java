package Game_Engine.GUI.Components.Input.textBased;

import BryceMath.Geometry.Rectangle;

/*
 * gui_ExpressionInput
 * 
 * Written by Bryce Summers 7 - 21 - 2013.
 * 
 * Purpose : Allows the user to construct numbers from strings of text.
 * Refactored to make room for Equation input boxes on 2 - 5 - 2013.
 * 
 * This is a special String input box that displays expressions in static mode, instead of Bryce TEX.
 */

public abstract class gui_numberTextInput<E> extends gui_textInput<E>
{

	// -- Private data
	private boolean auto_fit = true;
	
	// -- Constructors.
	
	public gui_numberTextInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_numberTextInput(Rectangle r)
	{
		super(r);
	}

	@Override
	public abstract E getInput();

	public void update()
	{
		super.update();
		
		if(auto_fit)
		fitToContents();
		
		// Logic for precise characters mode.
		// The expression input box displays the raw characters that make up the number.
		// To not confuse the user, we only switch when the user selects, instead of on mouse hover.
		if(isSelected())
		{
			if(getTEX())
			{
				disableTEX();
				refreshText();
			}
			
		}
		// Logic for non selected mode.
		// Expression Input Boxes display their interpretation of what expression has been inputed.
		else
		{
			if(!getTEX())
			{
				enableTEX();
				try
				{
					setText(getInput().toString());
				}
				catch(Error e) // Convert Fatal errors to Graphic User Interface Messages.
				{
					ERROR(e.toString());
				}
			}
		}
	}

	@Override
	public abstract String getSerialName();
	
	// Allows outside users to enable/ disable the box fitting behavior.
	public void setAutoFit(boolean b)
	{
		auto_fit = b;
	}


}