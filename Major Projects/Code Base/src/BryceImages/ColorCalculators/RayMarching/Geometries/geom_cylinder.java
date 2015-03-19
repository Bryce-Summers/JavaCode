package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

public class geom_cylinder extends Geometry {

	Vector z1,z2,dz;
	private double r, denom; // maxN;

	// A Cylinder is composed from all points within a radius of r from a line between z1 and z2.
	public geom_cylinder (Vector z1, Vector z2, double radius)
	{
		this.z1 = z1;
		this.z2 = z2;
		this.r  = radius;
		denom = z2.sub(z1).mag();
		dz = z2.sub(z1);
		
		
		
		//Vector diff = z2.sub(z1);
		
		
		//createCoordinateSystem(diff.norm());
	
		//double maxN = getComponent(diff,num_columns);
	}
	
	public double DE(Vector z, Vector dz)
	{
		return DE(z);
	}
	@Override
	public double DE(Vector z)
	{
		// Formula for the distance between a point and a line from:
		// http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html
		
		Vector d1,d2;
		d1 = z2.sub(z);
		d2 = z1.sub(z);
		
		if(d1.dot(this.dz) <0)
		{
			return max(0,(d1.mag()-r));
		}
		if(d2.dot(this.dz) >0)
		{
			return max(0,(d2.mag()-r));
		}
		
		
		/*
		Vector diff = z.sub(z1);
		

		double zComp = getComponent(diff,num_columns);
				
		Vector out = z1.add(num_columns.mult(bound(zComp,0,maxN)));
		
		return abs(out.sub(z).mag() - r);
		*/
		return max(0,dz.cross(z1.sub(z)).mag()/denom-r);
	}

}

// Alternate implementation.

/*
public class geom_cylinder extends Geometry {

	private Vector z1,z2,dz;
	private double r, maxN;
	// A Cylinder is composed from all points within a radius of r from a line between z1 and z2.
	public geom_cylinder (Vector z1, Vector z2, double radius)
	{
		this.z1 = z1;
		this.z2 = z2;
		this.r  = radius;

		Vector diff = z2.sub(z1);
		
				
		createCoordinateSystem(diff.norm());
	
		maxN = getComponent(diff,num_columns);
		
	}
	
	public double DE(Vector z, Vector dz)
	{
		
		return DE(z);
	}
	@Override
	public double DE(Vector z)
	{

		Vector diff = z.sub(z1);
		

		double zComp = getComponent(diff,num_columns);
				
		Vector out = z1.add(num_columns.mult(bound(zComp,0,maxN)));
		
		return abs(out.sub(z).mag() - r);
		
	}

}
*/

