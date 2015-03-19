package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

// A simple lather is a simple curve rotated around a line.

public class geom_simpleLathe extends Geometry {

	Vector z0, z,  r;
	double radius1, radius2, thickness;
	double angle1, angle2;
	
	public geom_simpleLathe(Vector z0, double r1, double r2, double thickness, Vector normal, double angle1, double angle2)
	{
		this.z0 = z0;
		this.radius1 = r1;
		this.radius2 = r2;
		this.thickness = thickness;
		
		// Create a unique coordinate system that is appropiate for this torus.
		createCoordinateSystem(normal);
		
		this.angle1 = angle1;
		this.angle2 = angle2;
		
	}

	@Override
	public double DE(Vector zz) {
		
		double uComp,vComp;
		double angle;

		// find a diferrence vector that shows that location of zz relative to the Torus's center.		
		Vector diff = zz.sub(z0);
		
		// Get the u and v components
		uComp = getComponent(diff,u);
		vComp = getComponent(diff,v);
		
		// Find the angle for the point on the center of lathe rotation.
		angle = lineAngle2(uComp,vComp,0,0);
		angle = degToRad(angle);
		
		Vector p1 = nearestSphere1(z0,radius1, angle);
		
		Vector n = nearestSphere1(z0,radius1, angle - .00000001).sub(p1).norm();
		
		Vector uTemp = u.mult(uComp).add(v.mult(vComp)).norm();
		Geometry g = new geom_simpleCurve(p1, radius2, thickness, n, uTemp, angle1, angle2);
		
		g.v = g.u.cross(n);
		//Geometry g = new geom_ball(p1,.1);
		
		// Calculate center of the lathed curve, that resides along a torus from z0;
		return g.DE(zz);

	}
	
	public Vector nearestSphere1(Vector z, double r, double angle)
	{
		return z.add(u.mult(r*cos(angle))).add(v.mult(r*sin(angle)));
	}
	

}
