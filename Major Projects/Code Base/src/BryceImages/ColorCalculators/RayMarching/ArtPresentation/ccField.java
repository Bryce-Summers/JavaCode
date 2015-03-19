package BryceImages.ColorCalculators.RayMarching.ArtPresentation;

import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_cylinder;


/*
 * Line image, lines are a union of spheres.
 */

public class ccField extends ccSphere
{

	public ccField(Dimension tempDim)
	{
		super(tempDim);
	}

	public void loadMore()
	{
		
		CameraDepth = 12;
		
		for(int x = -2; x < 2; x++)
		for(int y = -2; y < 2; y++)	
		{
			Vector v1 = location(x, y);
			Vector v2 = location(x + 1, y);
			Vector v3 = location(x, y + 1);
			
			i_line(.1, v1, v2);
			i_line(.1, v2, v3);
		}
		
	}
	
	public Vector location(double x, double y)
	{
		return v(x, y, Math.cos(x) + Math.sin(y));
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
