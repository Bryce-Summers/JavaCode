package BryceImages.ColorCalculators.RayMarching.ArtPresentation;

import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometries.geom_simpleCurve;

/*
 * Line image, lines are a union of spheres.
 */

public class ccCurve extends ccSphere
{

	public ccCurve(Dimension tempDim)
	{
		super(tempDim);
	}

	public void loadMore()
	{
		
		geom_simpleCurve g = new geom_simpleCurve(v(0, 0, .1), 2, .1, v(0, 0, 1), 0, 270);
		g.setColor(Color_hsv(50, 61, 100));
		g.setReflectivity(.1);
		geoms.add(g);
	}
	
}
