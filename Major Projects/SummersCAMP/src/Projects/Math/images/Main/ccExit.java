package Projects.Math.images.Main;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.Calculations.Colors;
import BryceMath.DoubleMath.Vector;

/*
 * Written on 4 - 30 - 2014 by Bryce Summers.
 * 
 * Purpose : This image is an icon for a new document.
 */

public class ccExit extends Image_vectorGeometry
{

	public ccExit(Dimension dim)
	{
		super(dim);
		// TODO Auto-generated constructor stub
	}

	public ccExit(int w, int h)
	{
		super(w, h);
		// TODO Auto-generated constructor stub
	}

	public ccExit(int w, int h, boolean wait_to_i_geoms)
	{
		super(w, h, wait_to_i_geoms);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 3;

		double w = getWidth();
		double h = getHeight();
		
		double thickness = h/20;
		set_thickness(thickness);
		

		Vector v1, v2, v3, v4, v5, v6;
		
		v1 = v(thickness, thickness);
		v2 = v(w - thickness, thickness);
		v3 = v(w - thickness, h*.75);
		v4 = v(thickness, h*.75);

		// Used for swinging door.
		v5 = v(w*.75, h*.25);
		v6 = v(w*.75, h - thickness);

		set_color(Colors.C_ERROR);
		Vector x1, x2, x3, x4;
		Vector average = Vector.average(v1, v5, v6, v4);
		
		x1 = Vector.average(average, v1);
		x2 = Vector.average(average, v5);
		x3 = Vector.average(average, v6);
		x4 = Vector.average(average, v4);
		
		i_line(x1, x3);
		i_line(x2, x4);
		
		
		// The swinging door.
		set_color(Color.BLACK);
		i_line(v1, v5, v6, v4, v1);

		set_color(Color.GRAY);
		i_polygon(v1, v5, v6, v4);
		
		// Outer frame;
		set_color(Color.BLACK);
		i_line(v1, v2, v3, v4, v1);
		
		set_color(Color.WHITE);
		i_polygon(v1, v2, v3, v4);
		

	}

}
