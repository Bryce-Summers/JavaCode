package Projects.Math.images;

import java.awt.Color;
import java.awt.Dimension;
import BryceImages.Engines.Image_vectorGeometry;

public class ccDivide extends Image_vectorGeometry
{

	public ccDivide(Dimension dim)
	{
		super(dim);
	}
	
	public ccDivide(int w, int h)
	{
		super(w, h);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 5;
		
		Color c = Color.black;
		
		set_color(c);
		
		i_circle(v(room_width/2, room_height/4), room_height/10);
		i_circle(v(room_width/2, room_height*3/4), room_height/10);
		
		i_rect(v(room_width/8, room_height/2), v(room_width*7/8, room_height/2), room_height*1.0/30);
	}

}
