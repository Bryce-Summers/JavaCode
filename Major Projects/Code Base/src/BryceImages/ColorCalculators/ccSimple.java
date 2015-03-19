package BryceImages.ColorCalculators;
import static BryceMath.Calculations.MathB.distance;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

/*
 * A Simple Color Calculator, written by Bryce Summers.
 */

public class ccSimple extends ColorCalculator
{

	// -- Constructors.
	public ccSimple(Dimension dim)	{super(dim);}
	public ccSimple(int w, int h)	{super(w,h);}


	public Color getColor(double x, double y)
	{
		
		// The best way to learn how to write color Calculators
		// is to play_button around with programming them!
		
		// Modify the below code to create different images.
		
		double dist = distance(x, y, room_width/2, room_height/2);
		return Color_hsv(dist, 100, 100);
		
	}
}
