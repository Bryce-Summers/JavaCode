package Projects.Math.images;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.DoubleMath.Vector;

public class ccExchange extends Image_vectorGeometry
{

	public ccExchange(Dimension dim)
	{
		super(dim);
	}
	
	public ccExchange(int w, int h)
	{
		super(w, h);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 5;
		
		Color c = Color.black;
		
		set_color(c);
		
		double h_offset = 1.0*room_height/8;
		
		// -- Create the first triangle.
		// Middle left most point.
		Vector v1 = v(0, room_height/4  + h_offset);
		
		// Top
		Vector v2 = v(room_width/4, 0  + h_offset*1.5);
		// Bottom
		Vector v3 = v(room_width/4, room_height/2 + h_offset*.5);
				
		i_triangle(v1, v2, v3);

		// -- Create the rod extending from it.
		
		v1 = v(room_width/4, 3*room_height/8);
		v2 = v(7.0*room_width/8, 3*room_height/8);
		
		i_rect(v1, v2, room_height/16);
		
		
		// -- Create the second. triangle.
		v1 = v(room_width, 3*room_height/4 - h_offset);
		v2 = v(3*room_width/4, room_height/2 - h_offset*.5);
		v3 = v(3*room_width/4, room_height - h_offset*1.5);
		
		i_triangle(v1, v2, v3);

		// -- Create the rod extending from it.
				
		v1 = v(3*room_width/4, 5*room_height/8);
		v2 = v(room_width/8, 5*room_height/8);
				
		i_rect(v1, v2, room_height/16);
		
	}

}
