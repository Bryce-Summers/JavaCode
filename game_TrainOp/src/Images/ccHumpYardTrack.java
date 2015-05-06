package Images;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.Calculations.Colors;

public class ccHumpYardTrack extends Image_vectorGeometry 
{

	boolean curves1 = false;
	boolean curves2 = false;
	boolean curves3 = false;
	boolean curves4 = false;
	
	boolean straight1 = false;
	boolean straight2 = false;
	
	
	boolean draw_rails = true;
	boolean draw_background = true;
	
	
	Color color_basalt = Colors.Color_hsv(0, 0, 70); 
	Color color_outer_rails = Colors.Color_hsv(0, 0, 50); 
	Color color_inner_rails = Colors.Color_hsv(00, 10, 100);
	
	
	public ccHumpYardTrack(Dimension dim)
	{
		super(dim);
	}

	public ccHumpYardTrack(int w, int h)
	{
		super(w, h);
	}
	
	public ccHumpYardTrack(int w, int h, int index)
	{
		super(w, h, true);
		
		switch(index)
		{
			case 0: straight1 = true;
				break;
			case 1: straight2 = true;
				break;
			case 2:
				curves1 = true;
				break;
			case 3:
				curves2 = true;
				break;
			case 4:
				curves3 = true;
				break;
			case 5:
				curves4 = true;
				break;
		}
		
		i_geoms();
	}

	public ccHumpYardTrack(int w, int h,
			boolean c1, boolean c2, boolean c3, boolean c4, boolean s1, boolean s2)
	{
		super(w, h, true);
		
		curves1 = c1;
		curves2 = c2;
		curves3 = c3;
		curves4 = c4;
		
		straight1 = s1;
		straight2 = s2;
		
		i_geoms();
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 4;
		
		double w = room_width;
		double h = room_height;
		
		if(straight1 || straight2)
		{
			w *= 2;
			h *= 2;
		}
		
		double x1 = w/3;
		double x2 = w*2/3;
		
		// The size of the printer cut.
		int cut = 0;//35;

		// Rails.		
		if(draw_rails)
		{
			// Inner rails
			set_color(color_inner_rails);
			set_thickness(w/50);
			drawRails(x1, x2, w, h, cut);
			
			// Outer rails
			set_color(color_outer_rails);
			set_thickness(w/30);
			
			drawRails(x1, x2, w, h, cut);
		}
		
		if(draw_background)
			drawBasalt(w, h, x1, x2, cut);
		
	}

	public void drawRails(double x1, double x2, double w, double h, double cut)
	{
		x1 /= 2;
		x2 /= 2;
		
		if(straight1)
		{
			i_rect(v(x1, 0), v(x1, h/2));
			i_rect(v(x2, 0), v(x2, h/2));
		}
		
		if(straight2)
		{
			i_rect(v(0, x1), v(w/2, x1));
			i_rect(v(0, x2), v(w/2, x2));
		}
		
		
		if(curves1)
		{
			i_curve(v(x1, cut), v(x1, cut + .0001), v(w - cut, h - x1));
			i_curve(v(x2, cut), v(x2, cut + .0001), v(w - cut, h - x2));
		}

		if(curves2)
		{
			i_curve(v(x1, h - cut), v(x1, h - .0001 - cut), v(w - cut, x1));
			i_curve(v(x2, h - cut), v(x2, h - .0001 - cut), v(w - cut, x2));
		}
		
		if(curves3)
		{
			i_curve(v(w - x1, cut), v(w - x1, cut + .0001), v(cut, h - x1));
			i_curve(v(w - x2, cut), v(w - x2, cut + .0001), v(cut, h - x2));
		}

		if(curves4)
		{
			i_curve(v(w - x1, h - cut), v(w - x1, h - .0001 - cut), v(cut, x1));
			i_curve(v(w - x2, h - cut), v(w - x2, h - .0001 - cut), v(cut, x2));
		}
		

	}
	
	public void drawBasalt(double w, double h, double x1, double x2, double cut)
	{
		
		double w_half = w/2;
		double h_half = h/2;
		
		// The basalt.
		set_color(color_basalt);
				
		if(straight1)
			i_rect(v(w_half/2, 0), v(w_half/2, h_half), w_half*.25);
				
		if(straight2)
			i_rect(v(0, h_half/2), v(w_half, h_half/2), w_half*.25);
				
		if(curves1)
			i_curve(v(w_half/2, cut), v(w_half/2, cut + .0001), v(w - cut, h - h_half/2), w_half*.25);
				
		if(curves2)
			i_curve(v(w_half/2, h - cut), v(w_half/2, h - cut - .0001), v(w - cut, h_half/2), w_half*.25);
				
		if(curves3)
			i_curve(v(w - w_half/2, cut), v(w - w_half/2, cut + .0001), v(cut, h -  h_half/2), w_half*.25);
				
		if(curves4)
			i_curve(v(cut, h_half/2), v(cut + .0001, h_half/2), v(w - w_half/2, h - cut), w_half*.25);
	}
	
}
