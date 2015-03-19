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

public class ccNew extends Image_vectorGeometry
{

	public ccNew(Dimension dim)
	{
		super(dim);
		// TODO Auto-generated constructor stub
	}

	public ccNew(int w, int h)
	{
		super(w, h);
		// TODO Auto-generated constructor stub
	}

	public ccNew(int w, int h, boolean wait_to_i_geoms)
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
		
		
		// We will be using the following thickness lines.
		double thickness = w/20;
		set_thickness(thickness);
		
		double triangle_size = w*.25;
		
		double right_border = w*.75;
		
		// Draw a star here.
		
		Vector v1 = v(thickness, thickness);
		Vector t1 = v(right_border - triangle_size, thickness);
		Vector t3 = v(right_border, triangle_size);
		Vector v2 = v(right_border, h - thickness);
		Vector v3 = v(thickness, h - thickness);


		for(double angle = 0; angle < Math.PI; angle += Math.PI/3)
		{
			double r = w*.13;
			Vector center = v(w - r*1.1, r*1.1);
						
			Vector displacement = v(r*Math.cos(angle), r*Math.sin(angle));
			
			Vector s1 = center.add(displacement);
			Vector s2 = center.sub(displacement);

			set_color(Colors.C_YELLOW);
			i_rect(s1, s2, thickness*.7);
			
		}
		
		for(double angle = 0; angle < Math.PI; angle += Math.PI/3)
		{
			double r = w*.13;
			Vector center = v(w - r*1.1, r*1.1);
			r = r*1.1;
						
			Vector displacement = v(r*Math.cos(angle), r*Math.sin(angle));
			
			Vector s1 = center.add(displacement);
			Vector s2 = center.sub(displacement);

			set_color(Color.BLACK);
			i_rect(s1, s2, thickness*.9);
		}

		
		set_color(Color.BLACK);
		i_line(v1, t1, t3, v2, v3, v1);

		set_color(Color.WHITE);
		i_polygon(v1, t1, t3, v2, v3, v1);

	}

}
