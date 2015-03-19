package Game_Engine.Engine.text.Symbols;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;

// -- Dot symbol class.

/*
 * rewritten by Bryce Summers on 7 - 23 - 2013.
 * 
 * Purpose : This class creates a circular dot image for use in my Mathematics program.
 * 
 * I am trying to make it smaller, because users complained about the enormous size of the original dot.
 */

public class ccDot extends Image_vectorGeometry
{

	public ccDot(Dimension dim)
	{
		super(dim);
	}
	
	public ccDot(int w, int h)
	{
		super(w, h);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 5;
		
		Color c = Color.black;
		
		set_color(c);
		
		i_circle(v(room_width/2, room_height/2), room_height/5);

	}

}
