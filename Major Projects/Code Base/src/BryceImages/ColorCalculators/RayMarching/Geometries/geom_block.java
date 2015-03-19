package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

// A block is a geometric object that is comprised of a box with slanted by the inputted "normal" up position
//		that is subscribed inside the cartesian box given by the two inputted coordinates.

public class geom_block extends Geometry {

	private Vector c1;
	double maxU, maxV, maxN;
	double r;
	public geom_block(Vector c1, Vector c2, Vector normal, double cornerSize)
	{
		// First create this Geometry's coordinate system.
		createCoordinateSystem(normal);
		
		this.c1 = c1;
		this.r = cornerSize;
		
		// Now determine the maximum bounds for this block, so that the proper maximum values are observed in the u,v,num_columns system to be translated into x,y,z
		Vector diff = c2.sub(c1);// Note: C2 is only needed to form maximum bounds on u and v.
		maxU = getComponent(diff,u);
		maxV = getComponent(diff,v);
		maxN = getComponent(diff,n);
	}

	@Override
	public double DE(Vector zz)
	{

		Vector diff = zz.sub(c1);
		
		double xComp = getComponent(diff,u);
		double yComp = getComponent(diff,v);
		double zComp = getComponent(diff,n);
				
		Vector out = c1.add(u.mult(bound(xComp,0,maxU))).add(v.mult(bound(yComp,0,maxV))).add(n.mult(bound(zComp,0,maxN)));
		
				
		
		return abs(out.sub(zz).mag() - r);
		
	}
}
