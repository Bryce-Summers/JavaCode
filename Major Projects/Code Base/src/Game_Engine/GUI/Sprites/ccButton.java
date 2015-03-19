package Game_Engine.GUI.Sprites;

import java.awt.Color;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.Calculations.Colors;
import BryceMath.DoubleMath.Vector;

public class ccButton extends Image_vectorGeometry
{
	private int mode;
	public static final int MODE_RESTING = 0;
	public static final int MODE_MOUSE_OVER = 1;
	public static final int MODE_PRESSED = 2;
	
	public ccButton(int w, int h, int mode)
	{
		// Wait for a manual initialization.
		super(w, h, true);
		
		this.mode = mode;
		i_geoms();
	}

	@Override
	public void i_geoms()
	{
		Color greyMid, grey12, grey23, grey34, grey41;
		
		
		if(mode == MODE_RESTING)
		{
			greyMid	= Colors.Color_hsv(0, 0, 90);
			grey12	= Colors.Color_hsv(0, 0, 95);
			grey23	= Colors.Color_hsv(0, 0, 85);
			grey34	= Colors.Color_hsv(0, 0, 70);
			grey41	= Colors.Color_hsv(0, 0, 87);	
		}
		else
		{
			greyMid	= Colors.Color_hsv(0, 0, 80);
			grey12	= Colors.Color_hsv(0, 0, 85);
			grey23	= Colors.Color_hsv(0, 0, 75);
			grey34	= Colors.Color_hsv(0, 0, 60);
			grey41	= Colors.Color_hsv(0, 0, 77);	
		}		

		int w = room_width;
		int h = room_height;
		
		int weight = 1;
		
		// The size of the side panels on the key.
		int size = 6;
		
		Vector v1 = v(size - 1, size);
		Vector v2 = v(w - size, size);
		Vector v3 = v(w - size, h - size);
		Vector v4 = v(size, h - size);
		
		// The middle rectangle.
		i_line(weight, v1, v2, v3, v4, v1);
		
		Vector c1 = v(weight, weight);
		Vector c2 = v(w - weight, weight);
		Vector c3 = v(w - weight, h - weight);
		Vector c4 = v(weight, h - weight);
		
		// The connections between the corners and the middle rectangle.
		i_round_rect(v1, c1, weight);
		i_round_rect(v2, c2, weight);
		i_round_rect(v3, c3, weight);
		i_round_rect(v4, c4, weight);
		
		// The outer rectangle.
		i_line(weight, c1, c2, c3, c4, c1);
		
		Vector mid1 = v1.add(v4).div(2);
		Vector mid2 = v2.add(v3).div(2);
		
		set_color(greyMid);
		i_rect(mid1, mid2, h/2 - size);
		
		
		// --  Color each side.
	
		// Side 1 - 2.
		set_color(grey12);
		mid1 = v(size, size/2);
		mid2 = v(w - size, size/2);
		
		i_rect(mid1, mid2, size/2);
		i_triangle(c1, v1.add(v(1, 0)), v(v1.getX() + 1, c1.getY()));
		i_triangle(c2, v2, v(v2.getX(), c2.getY()));
		
		// Side 2 - 3.
		set_color(grey23);
		mid1 = v(w - size/2, size);
		mid2 = v(w - size/2, h - size);
		
		i_rect(mid1, mid2, size/2);
		i_triangle(c2, v2, v(c2.getX(), v2.getY()));
		i_triangle(c3, v3, v(c3.getX(), v3.getY()));
		
		// Side 3 - 4.
		set_color(grey34);		
		mid1 = v(size, h - size/2);
		mid2 = v(w - size, h - size/2);
		
		i_rect(mid1, mid2, size/2);
		i_triangle(c3, v3, v(v3.getX(), c3.getY()));
		i_triangle(c4, v4, v(v4.getX(), c4.getY()));
		
		// Side 4 - 1.
		set_color(grey41);		
		mid1 = v(size/2, h - size);
		mid2 = v(size/2, size);
		
		i_rect(mid1, mid2, size/2);
		i_triangle(c4, v4, v(c4.getX(), v4.getY()));
		i_triangle(c1, v1, v(c1.getX(), v1.getY()));

	}

}
