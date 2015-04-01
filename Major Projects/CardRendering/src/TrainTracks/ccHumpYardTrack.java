package TrainTracks;

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
	
	public ccHumpYardTrack(Dimension dim)
	{
		super(dim);
	}

	public ccHumpYardTrack(int w, int h)
	{
		super(w, h);
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
		
		double x1 = w/3;
		double x2 = w*2/3;
		
		// The size of the printer cut.
		int cut = 35;

		
		// Inner rails
		set_color(Colors.Color_hsv(00, 100, 100));
		set_thickness(w/50);
		
		drawRails(x1, x2, w, h, cut);
		
		// Outer rails
		set_color(Colors.Color_hsv(0, 0, 50));
		set_thickness(w/30);
		
		drawRails(x1, x2, w, h, cut);
		
		// The basalt.
		set_color(Colors.Color_hsv(0, 0, 70));
		
		if(straight1)
		i_rect(v(w/2, 0), v(w/2, h), w*.25);
		
		if(straight2)
		i_rect(v(0, h/2), v(w, h/2), w*.25);
		
		if(curves1)
		i_curve(v(w/2, cut), v(w/2, cut + .0001), v(w - cut, h/2), w*.25);
		
		if(curves2)
		i_curve(v(w/2, h - cut), v(w/2, h - cut - .0001), v(w - cut, h/2), w*.25);
		
		if(curves3)
		i_curve(v(w/2, cut), v(w/2, cut + .0001), v(cut, h/2), w*.25);
		
		if(curves4)
		i_curve(v(cut, h/2), v(cut + .0001, h/2), v(w/2, h - cut), w*.25);
		
	}

	public void drawRails(double x1, double x2, double w, double h, double cut)
	{
		if(straight1)
		{
			i_rect(v(x1, 0), v(x1, h));
			i_rect(v(x2, 0), v(x2, h));
		}
		
		if(straight2)
		{
			i_rect(v(0, x1), v(w, x1));
			i_rect(v(0, x2), v(w, x2));
		}
		
		
		if(curves1)
		{
			i_curve(v(x1, cut), v(x1, cut + .0001), v(w - cut, x2));
			i_curve(v(x2, cut), v(x2, cut + .0001), v(w - cut, x1));
		}
		
		if(curves3)
		{
			i_curve(v(x1, cut), v(x1, cut + .0001), v(cut, x1));
			i_curve(v(x2, cut), v(x2, cut +.0001), v(cut, x2));
		}
		
		if(curves4)
		{
			i_curve(v(x1, h - cut), v(x1, h - .0001 - cut), v(cut, x2));
			i_curve(v(x2, h - cut), v(x2, h - .0001 - cut), v(cut, x1));
		}
		
		if(curves2)
		{
			i_curve(v(x1, h - cut), v(x1, h - .0001 - cut), v(w - cut, x1));
			i_curve(v(x2, h - cut), v(x2, h - .0001 - cut), v(w - cut, x2));
		}
	}
	
}
