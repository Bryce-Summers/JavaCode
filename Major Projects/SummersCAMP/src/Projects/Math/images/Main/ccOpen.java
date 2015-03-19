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

public class ccOpen extends Image_vectorGeometry
{

	public ccOpen(Dimension dim)
	{
		super(dim);
		// TODO Auto-generated constructor stub
	}

	public ccOpen(int w, int h)
	{
		super(w, h);
		// TODO Auto-generated constructor stub
	}

	public ccOpen(int w, int h, boolean wait_to_i_geoms)
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
		
		Vector v1, v2, v3, v4, v5, v6, v7, v8, v9;
		
		double thickness = h/20;
		set_thickness(thickness);
		
		double center_line_h = w/3;
		
		
		v1 = v(thickness, h - thickness);
		v2 = v(w/4, center_line_h);
		v3 = v(w - thickness, center_line_h);
		v4 = v(w - w/4, h - thickness);
		
		v5 = v(thickness, thickness);
		v6 = v(w/3, thickness);
		v7 = v(w/2, h/8);
		v8 = v(w*3/4, h/8);
		v9 = v(w*3/4, center_line_h);
		
		set_color(Color.BLACK);
		// The bottom folder portion.
		i_line(v1, v2, v3, v4, v1);
		// The top topological space.
		i_line(v1, v5, v6, v7, v8, v9);
		
		// The convex hull polygon :)
		set_color(Colors.C_YELLOW);
		i_polygon(v1, v5, v6, v7, v8, v9, v3, v4);
		
	}

}
