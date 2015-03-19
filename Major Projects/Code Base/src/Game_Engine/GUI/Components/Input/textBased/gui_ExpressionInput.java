package Game_Engine.GUI.Components.Input.textBased;

import BryceMath.Factories.ExpressionFactory;
import BryceMath.Geometry.Rectangle;
import BryceMath.Numbers.Expression;

/*
 * gui_ExpressionInput
 * 
 * Written by Bryce Summers 7 - 21 - 2013.
 * 
 * Refactored to make room for Equation input boxes on 2 - 5 - 2013.
 * 
 * Purpose : Allows the user to construct an expression from an inputed text expression.
 */

public class gui_ExpressionInput extends gui_numberTextInput<Expression>
{

	public gui_ExpressionInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_ExpressionInput(Rectangle r)
	{
		super(r);
	}

	@Override
	public Expression getInput()
	{
		// Safeguard the integrity of the input.
		refreshText();
		return ExpressionFactory.createExpression(getText());
	}

	@Override
	public String getSerialName()
	{
		return "gui_ExpressionInput";
	}




}