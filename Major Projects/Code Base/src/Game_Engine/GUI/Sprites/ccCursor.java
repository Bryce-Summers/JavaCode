package Game_Engine.GUI.Sprites;
import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;

/* Cursor image.
 * 
 * Written by Bryce Summers on 6 - 17 - 2013..
 * 
 * Purpose : This image will be used in tutorials that will demonstrate the functionality of my programs.
 */

public class ccCursor extends Image_vectorGeometry
{

	public ccCursor(Dimension dim)
	{
		super(dim);
	}

	public ccCursor(int width, int height)
	{
		super(width, height);
	}

	@Override
	public void i_geoms()
	{
		double w = room_width;
		double h = room_height;

		// Construct an inner cursor.
		
		double borderSize = .1;
		
		set_color(Color.WHITE);
		i_triangle(v(w*borderSize, h*borderSize), v(w*(1 - borderSize*2), h*borderSize), v(w*borderSize, h*(1 - borderSize*2)));
		double weight = w /10;
		i_rect(v(w*.25, h*.25), v(w - weight*3, h - weight*3), weight);
		
		
		// Construct borders.
		set_color(Color.BLUE);
		i_triangle(v(0, 0), v(w, 0), v(0, h));
		weight = w / 5;
		i_rect(v(w*.25, h*.25), v(w - weight, h - weight), weight);
		
	}
	
}
