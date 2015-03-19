package Game_Engine.GUI.Components.Input.textBased;

import BryceMath.Factories.ExpressionFactory;
import BryceMath.Geometry.Rectangle;
import BryceMath.Numbers.Equation;

/*
 * gui_ExpressionInput
 * 
 * Written by Bryce Summers 7 - 21 - 2013.
 * 
 * Refactored to make room for Equation input boxes on 2 - 5 - 2013.
 * 
 * Purpose : Allows the user to construct an expression from an inputed text expression.
 */

public class gui_EquationInput extends gui_numberTextInput<Equation>
{

	private Equation default_eq = Equation.ONE;
	
	public gui_EquationInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_EquationInput(Rectangle r)
	{
		super(r);
	}

	@Override
	public Equation getInput()
	{
		// Safeguard the integrity of the input.
		refreshText();
		
		String s = getText();

		if(s.length() == 0)
		{
			return default_eq;
		}
		
		return ExpressionFactory.createEquation(getText());
	}

	@Override
	public String getSerialName()
	{
		return "gui_ExpressionInput";
	}

	public void setDefaultEquation(Equation e)
	{
		default_eq = e;
	}
	
	// Allow the box to be populated by equations themselves.
	public void populateInput(Equation eq)
	{
		populateInput(eq.toSerialString());
	}


}