package Projects.Math.images.Main;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.Calculations.Colors;

/*
 * Written on 4 - 30 - 2014 by Bryce Summers.
 * 
 * Purpose : This image is an icon for a new document.
 */

public class ccHelp extends Image_vectorGeometry
{

	public ccHelp(Dimension dim)
	{
		super(dim);
	}

	public ccHelp(int w, int h)
	{
		super(w, h);
	}

	public ccHelp(int w, int h, boolean wait_to_i_geoms)
	{
		super(w, h, wait_to_i_geoms);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 3;

		double w = getWidth();
		double h = getHeight();
		
		double thickness = h/20;
		thickness*=2;
		set_thickness(thickness);

		
		set_color(Colors.C_BLUE_HEADING);
		i_circle(v(w/2, h/2), w/2 - thickness);
		
		set_color(Color.BLACK);
		i_circle(v(w/2, h/2), w/2 - 1);

		
	}

}
