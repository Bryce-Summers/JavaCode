package FrameSpecifications;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.Calculations.Colors;
import BryceMath.DoubleMath.Vector;

public class ccTrainCar extends Image_vectorGeometry
{

	final int hue, sat;
	final int val;
	
	public ccTrainCar(Dimension dim, int hue, int sat)
	{
		super((int)dim.getWidth(), (int)dim.getHeight(), true);

		this.hue = hue;
		this.sat = sat;
		val = 100;
		
		i_geoms();		
	}

	public ccTrainCar(int w, int h, int hue, int sat)
	{
		super(w, h, true);

		this.hue = hue;
		this.sat = sat;
		val = 100;
		
		i_geoms();
	}
	
	public ccTrainCar(int w, int h, int hue, int sat, int val)
	{
		super(w, h, true);

		this.hue = hue;
		this.sat = sat;
		this.val = val;
		
		
		i_geoms();
	}

	@Override
	public void i_geoms()
	{
		int w = getWidth();
		int h = getHeight();
		
		double thickness = h/16.0;
		
		set_thickness(thickness);
		set_color(Color.BLACK);
		
		Vector p1, p2, p3, p4;
		
		p1 = v(thickness, thickness);
		p2 = v(w - thickness, thickness);
		p3 = v(w - thickness, h - thickness*5);
		p4 = v(thickness, h - thickness*5);
		
		i_line(p1, p2, p3, p4, p1);
		
		// Inside car color.
		set_color(Colors.Color_hsv(hue, sat, val));
		i_rect(p1.add(p4).div(2), p2.add(p3).div(2), h/2 - thickness*2);
		
		// Wheels.
		set_color(Color.BLACK);
		double radius = thickness*5;
		i_circle(v(thickness*6, h - thickness*5), radius);
		i_circle(v(w - thickness*6, h - thickness*5), radius);
		

		

	}

}
