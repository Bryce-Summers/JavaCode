package Game_Engine.GUI.Sprites;

import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;

public class ccCheckMark extends Image_vectorGeometry
{

	public ccCheckMark(Dimension dim)
	{
		super(dim);
		// TODO Auto-generated constructor stub
	}

	public ccCheckMark(int w, int h)
	{
		super(w, h);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 4;
		
		double weight = 1.0*room_height / 20;
		
		int w = room_width;
		int h = room_height;
		
		i_line(weight, v(weight*2, h/2), v(w/4, h - weight*2), v(w - weight*2, weight*2));
	}

}
