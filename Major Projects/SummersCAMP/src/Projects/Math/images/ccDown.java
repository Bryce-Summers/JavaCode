package Projects.Math.images;

import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;

/*
 * Downward arrow. Used to close the operation pane window.
 * 
 * Written by Bryce Summers on 4 - 29 - 2014.
 */

public class ccDown extends Image_vectorGeometry
{

	public ccDown(Dimension dim)
	{
		super(dim);
	}

	public ccDown(int w, int h)
	{
		super(w, h);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 3;
		
		double h = room_height;
		double w = room_width;
		
		i_triangle(v(w/4, h/2), v(w*3/4, h/2), v(w/2, h*3/4));
		
		i_line(w/16, v(w/2, h/2), v(w/2, h/4));

	}

}
