package Game_Engine.GUI.Sprites;

import static BryceMath.Calculations.MathB.min;

import java.awt.Color;

import BryceImages.Rendering.ColorCalculator;

public class ccGui_vertEdges extends ColorCalculator
{
	
	/*
	 * Vertical edge building blocks.
	 * Updated by Bryce Summers 1 - 13 - 2013.
	 * Purpose: Provides a 1 dimensional image that can be scaled with prescision.
	 * 1 - 13 - 2013: Remove lost of floating point errors.
	 */
	
	public static Color color = Color.red;

	public ccGui_vertEdges(int width, int height) {
		super(width, height);
		antiAliasing = 4;
	}

	@Override
	public Color getColor(double x, double y) {

		double val = min(x/room_width, 1);
		
		return StyleSpec.getBorderColor(val);
	}

}
