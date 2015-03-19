package BryceImages.ColorCalculators;
import java.awt.Dimension;

import BryceImages.Engines.Image_vectorGeometry;
//import BryceMath.DoubleMath.Vector;

/*
 * ccExchange, written by Bryce Summers
 * 
 * Purpose: A tree image rendered for my mother.
 */


public class ccTree extends Image_vectorGeometry
{

	public ccTree(Dimension dim)
	{
		super(dim);
	
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 9;
		//i_inclined_rect(v(50, 50), v(room_width - 50, room_height - 50), 50, 10);
		
		//i_circle(v(300, 600), 78);
		
		//i_triangle(v(800, 50), v(900,100), v(1850, 150));
		
		int x = 100;
		int y = 100;

		//int angle_start = 0;
				
		for(int angle_start = 10; angle_start < 360; angle_start += 45)
		{
			double angle_mid = angle_start +.001 ;
			
			int angle_end = angle_start - 2;
			
			i_curve(v(x + 50*Math.cos(Math.toRadians(angle_start)), y - 50*Math.sin(Math.toRadians(angle_start))),
					v(x + 50*Math.cos(Math.toRadians(angle_mid  )), y - 50*Math.sin(Math.toRadians(angle_mid  ))),
					v(x + 50*Math.cos(Math.toRadians(angle_end  )), y - 50*Math.sin(Math.toRadians(angle_end  ))),
					10);
			
			x += 120;
		}
		
		for(int i = 0; i < 100; i++)
		{
			/*
			Vector v1 = Vector.randV(0, room_width, 0, room_height);
			Vector v2 = Vector.randV(0, room_width, 0, room_height);
			Vector v3 = Vector.randV(0, room_width, 0, room_height);
			*/
			
			
			set_color(Color_hsv(Math.random()*360, 100, 100, 50));
			
			//i_curve(v1, v2, v3, Math.random()*9 + 1);
			
		}
		
						
		//i_curve(v(x + 50, y), v(x + 100, y + 50), v(x, y + 50), 10);		
		
		
		//i_curve(v(100, 500), v(500, 600), v(1000, 500), 10);
	}
	
}// End of Class.
