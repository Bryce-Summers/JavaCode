package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

// A simple curve is basically just a partial torus that has balls on the ends.

public class geom_simpleCurve extends Geometry {

	Vector z;
	double radius, thickness;
	double angle1, angle2;

	int axis = 2; // Naturally z is excluded by default, with a torus in the xy plane;
	public geom_simpleCurve(Vector z, double r, double thickness, Vector normal, double angle1, double angle2) {
		this.z = z;
		this.radius = r;
		this.thickness = thickness;
		
		// Create a unique coordinate system that is appropiate for this torus.
		createCoordinateSystem(normal);
		
		this.angle1 = degToRad(angle1);
		this.angle2 = degToRad(angle2);
		
	}
	
	public geom_simpleCurve(Vector z, double r, double thickness, Vector normal, Vector u, double angle1, double angle2) {
		this.z = z;
		this.radius = r;
		this.thickness = thickness;
		
		// Create a unique coordinate system that is appropiate for this torus.
		this.n = n.norm();
		this.u = u.norm();
		this.v = this.u.cross(this.n);
		
		this.angle1 = degToRad(angle1);
		this.angle2 = degToRad(angle2);
		
	}

	@Override
	public double DE(Vector zz) {
		
		double uComp,vComp;

		// find a fiferrence vector that shows that location of zz relative to the Torus's center.		
		Vector diff = zz.sub(z);
		
		// Get the u and v components
		uComp = getComponent(diff,u);
		vComp = getComponent(diff,v);
		
			
		double angle = lineAngle2(uComp,vComp,0,0);// Find the angle of the projected u, and v components inside of the Torus's coordinate system.
		
		if(angle < 0)
			angle+=360;		
		if(angle1 < 0 && angle > 180)
		{
			angle-=360;
		}
		
		angle = degToRad(angle);
		
		// If this location is not at an angle within the curve, return the minimum of the distance to each of the two endpoints.
		boolean check;
		check = angle >= angle2 || angle <= angle1;

				
		if(check)
		{
			return min(torus(zz,angle1),torus(zz,angle2));
		}
		
		// Else return normal torus code.
		return torus(zz, angle);

	}
	
	private double torus(Vector zz, double angle)
	{
		// The magic step that finds the nearest location.
		// Essentially the nearest point is the one on the torus at the same angle as the projected u and v components.

		Vector nearest = z.add(u.mult(radius*cos(angle))).add(v.mult(radius*sin(angle)));

		// Now perform a simple and efficient sphere distance calcualtion.
		return abs(nearest.sub(zz).mag()-thickness);
		
	}

}
