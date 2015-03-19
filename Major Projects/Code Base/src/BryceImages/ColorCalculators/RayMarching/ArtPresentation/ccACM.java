package BryceImages.ColorCalculators.RayMarching.ArtPresentation;

import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_cylinder;

/*
 * Line image, lines are a union of spheres.
 */

public class ccACM extends ccSphere
{

	public ccACM(Dimension tempDim)
	{
		super(tempDim);
	}

	public void loadMore()
	{
		
		CameraDepth = 12;
		
		
		// A
		i_line(.1, v(-.75, -1, .1), v(0, 1, .1), v(.75, -1, .1));
		i_line(.1, v(-.5625, -.5, .1), v(.5625, -.5, .1));
		
		
		// C
		/*
		geom_simpleCurve g = new geom_simpleCurve(v(0, 0, .1), 1, .1, v(0, 0, 1), 70, 290);
		g.setColor(Color_hsv(50, 61, 100));
		g.setReflectivity(.1);
		geoms.add(g);
		*/
		
		
		// M
		//i_line(.1, v(-.75, -1, .1), v(-.75, 1, .1), v(0, 0, .1), v(.75, 1, .1), v(.75, -1, .1));
		
		
	}
	
	public void i_line(double size, Vector ... va)
	{
		Vector v1;
		
		v1 = null;
		
		for(Vector v : va)
		{
			if(v1 == null)
			{
				v1 = v;
				continue;
			}
			
			geom_cylinder g = new geom_cylinder(v1, v, size);
			g.setColor(Color_hsv(50, 61, 100));
			g.setReflectivity(.1);
			geoms.add(g);
			
			v1 = v;
		}
	}
	
}
