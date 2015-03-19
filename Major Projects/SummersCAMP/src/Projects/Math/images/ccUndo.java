package Projects.Math.images;

import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;

public class ccUndo extends Image_vectorGeometry {

	public ccUndo(Dimension dim) {
		super(dim);
		// TODO Auto-generated constructor stub
	}

	public ccUndo(int w, int h)
	{
		super(w, h);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 3;
		
		double h = room_height;
		double w = room_width;
		
		i_triangle(v(w/8, h/2), v(w*3/8, h/2), v(w/4, h*3/4));
		
		i_curve(v(w/2, h*3/4), v(w*3/4, h/2), v(w/4, h/2), w/16);

	}

}
