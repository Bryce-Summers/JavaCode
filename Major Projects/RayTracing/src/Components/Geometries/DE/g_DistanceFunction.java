package Components.Geometries.DE;

import Components.DistanceFunction;
import Components.Geometry;
import Components.Material;
import Components.photonColor;
import Math.Vector3;

// Enables The Ray Tracer to trace distance fields.

public class g_DistanceFunction extends Geometry
{
	final static double DIST_MIN = .000001;
	final static double DIST_MAX = 10000;
	DistanceFunction[] functions;

	public g_DistanceFunction(Material mat, DistanceFunction... functions)
	{
		super(mat);
		
		this.functions = functions;
	}


	@Override
	protected double sub_getIntersection(Vector3 point, Vector3 dir)
	{
		double dist = 0.0;
		
		double dist_inc;
		do
		{
			// The distance increment.
			dist_inc = DE(point);
			point = point.add(dir.mult(dist_inc));
			dist += dist_inc;
			
		}while(DIST_MIN < dist_inc && dist < DIST_MAX);
		
		if(dist >= DIST_MAX)
		{
			return -1;// DIVERGENCE implies no collision.
		}
		
		return dist - .00001;
	}
	
	// The composite Distance Estimation of the union of objects.
	private double DE(Vector3 point)
	{
		double minDist = Double.MAX_VALUE;
		
		for(DistanceFunction foo : functions)
		{
			double dist_new = foo.DE(point);
			minDist = Math.min(minDist, dist_new);
			
		}
		
		return minDist;
	}

	@Override
	public photonColor getColor() {
		
		return photonColor.WHITE;
	}

	@Override
	protected Vector3 sub_getNormal(Vector3 z) {

		double e = .00001;
		
		double d1, d2;
		
		// x		// The first DE() ensures that the correct tempGeomPointer is used.
		d1 = DE(new Vector3(z.getX() + e, z.getY(), z.getZ()));
		d2 = DE(new Vector3(z.getX() - e, z.getY(), z.getZ()));
		double out_x = (d1 - d2)/(2*e);// The average slope over this region.
		
		// y
		d1 = DE(new Vector3(z.getX(), z.getY() + e, z.getZ()));
		d2 = DE(new Vector3(z.getX(), z.getY() - e, z.getZ()));
		double out_y = (d1 - d2)/(2*e);// The average slope over this region.
		
		// z
		d1 = DE(new Vector3(z.getX(), z.getY(), z.getZ() + e));
		d2 = DE(new Vector3(z.getX(), z.getY(), z.getZ() - e));
		double out_z = (d1 - d2)/(2*e);// The average slope over this region.
		
		return new Vector3(out_x, out_y, out_z).norm();		
	}

}
