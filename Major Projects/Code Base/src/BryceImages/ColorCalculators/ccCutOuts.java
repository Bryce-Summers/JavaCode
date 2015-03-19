package BryceImages.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.Calculations.Colors;

/*
 * image_vector Geomerty cutout testing image.
 * 
 * Written by Bryce Summers on 4 - 24 - 2014.
 * 
 * The purpose of this image is to test the new capability of subtractive synthesis of geometric shapes.
 */

public class ccCutOuts extends Image_vectorGeometry
{
	// -- Constructors.
	public ccCutOuts(int w, int h)
	{
		super(w, h);
	}
	
	public ccCutOuts(Dimension dim)
	{
		super(dim);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 3;
		
		int w = getWidth();
		int h = getHeight();

		// Fill the cut outs with a blue circle.
		set_color(Colors.C_BLUE_HEADING);
		i_circle(v(w*3/4, h/4), w/5);
		i_circle(v(w/2, h/2), w/9);
		
		// Create two cutouts.
		set_color(Colors.C_CLEAR);
		i_circle(v(w*3/4, h/4), w/4);
		i_circle(v(w/2, h/2), w/8);
		
		// Draw the background.
		set_color(Color.black);
		i_circle(v(w/2, h/2), w/2);
		
		set_color(Colors.C_DIVIDER);
		i_circle(v(w/2, h/2), w/2.3);
		
	}

}
