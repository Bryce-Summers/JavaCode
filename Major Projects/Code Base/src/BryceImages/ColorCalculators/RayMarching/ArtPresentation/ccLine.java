package BryceImages.ColorCalculators.RayMarching.ArtPresentation;

import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometries.geom_cylinder;

/*
 * Line image, lines are a union of spheres.
 */

public class ccLine extends ccSphere
{

	public ccLine(Dimension tempDim)
	{
		super(tempDim);
	}

	public void loadMore()
	{
		CameraDepth = 12;
		
		geom_cylinder g = new geom_cylinder(v(-1, 0, .1), v(1, 0, .1),.1);
		g.setColor(Color_hsv(50, 61, 100));
		g.setReflectivity(.1);
		geoms.add(g);
	}
	
}
