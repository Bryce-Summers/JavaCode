package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

/*	
 * A plane is defined by a point and a normal vector, 
 * I have devised a pretty efficient and accurate distance estimation for a plane,
 * with an added improvement when given the ray direction.
*/


public class geom_plane extends Geometry {

	private Vector point;
	
	public geom_plane(Vector point, Vector normal)
	{
		// First create this Geometry's coordinate system.
		createCoordinateSystem(normal);
		this.point = point;
		
		
	}

	@Override
	public double DE(Vector zz, Vector dz)
	{

		if(dz.dot(n)>0)
		{
			return Double.MAX_VALUE;
		}
		
		Vector diff = zz.sub(point);
					
		double zComp = abs(getComponent(dz,n));
		
		// Same as in a bow, except it is unbound and the z (num_columns) component remains on the plane
		Vector out = zz.sub(n.mult(getComponent(diff,n)));		
				
		// This contains no r value, planes have no depth.
		return abs(out.sub(zz).mag())/zComp;
		
	}
	
	@Override
	public double DE(Vector zz)
	{

		Vector diff = zz.sub(point);
		
		// Same as in a bow, except it is unbound and the z (num_columns) component remains on the plane
		Vector out = zz.sub(n.mult(getComponent(diff,n)));		
				
		// This contains no r value, planes have no depth.
		return abs(out.sub(zz).mag());
		
	}
}
