package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

// Ball geometry class
// Written by Bryce Summers
// 7/22/2012

public class geom_ball extends Geometry
{

	private double radius;	// Stores this ball's location.
	private Vector z;		// Stores this ball's radius.
	public geom_ball(Vector z, double radius)
	{
		this.z = z;
		this.radius = radius;
	}

	@Override
	public double DE(Vector z)
	{	
		return abs(z.sub(this.z).mag()-radius);
	}


}
