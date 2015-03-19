package BryceImages.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.DoubleMath.Vector;

/*
 * image_vector Geomerty Polygon testing image.
 * 
 * Written by Bryce Summers on 4 - 24 - 2014.
 * 
 * The purpose of this image is to test the new capabilities of my polygonal primitives in the vector geometry engine.
 */

public class ccPolygon extends Image_vectorGeometry
{
	// -- Constructors.
	public ccPolygon(int w, int h)
	{
		super(w, h);
	}
	
	public ccPolygon(Dimension dim)
	{
		super(dim);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 3;
		
		int w = getWidth();
		int h = getHeight();

		/*
		Vector v1, v2, v3;
		
		v1 = v(0, 0);
		v2 = v(w/2, 0);
		v3 = v(0, h/2);
		
		set_color(Color.BLACK);
		i_polygon(v1, v2, v3);
		*/
		
		
		set_color(Color.BLACK);
		
		Vector v1, v2, v3, v4, v5;
		
		v1 = v(0, 0);
		v2 = v(w/2, 0);
		v3 = v(w/2, h/4);
		v4 = v(w, h/5);
		
		v5 = v(w/2, h);
		
		i_polygon(v1, v2, v3, v4, v5);
		
		
	}

}
