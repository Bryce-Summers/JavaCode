package BryceImages.ColorCalculators;
import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.DoubleMath.Vector;
//import BryceMath.DoubleMath.Vector;

/*
 * ccExchange, written by Bryce Summers
 * 
 * Purpose: A tree image rendered for my mother.
 */


public class ccGraphCurves extends Image_vectorGeometry
{

	public ccGraphCurves(Dimension dim)
	{
		super(dim);
		antiAliasing = 9;
	}

	@Override
	public void i_geoms()
	{
		// -- Change Me!!!
		double resolution = 50;
		
		
		// -- Don't touch!!!
		double h = room_height / 2.0;
				
		double size = 1.0*h / resolution;
		
		Vector down = v(0, .00001);
		Vector up = v(0, -.00001);
		
		boolean parity = false;
		boolean p2 = false;
		
		for(double i = 0; i < room_width; i += room_width / resolution)
		{
						
			parity = !parity;
			p2 = parity ? p2 : !p2;
			
			double offset1 = Math.abs(i - room_width/4 + room_width);
			double offset2 = Math.abs(i - 3*room_width/4 + room_width);
			double offset = Math.min(offset1, offset2);
			
			Vector v = v(i, h);
			Vector v2 = v(i + offset, h);
			Vector v3 = parity ? v.add(up) : v.add(down);
			
			if(p2)
			{
				set_color(Color.BLUE);
			}
			else
			{
				set_color(Color.BLACK);
			}
			
			i_curve(v, v3, v2, size);
			
		}
		
		
	}
	
}// End of Class.
