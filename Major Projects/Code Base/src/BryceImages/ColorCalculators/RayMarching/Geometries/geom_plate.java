package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

public class geom_plate extends Geometry {

	Vector z;
	double radius, thickness;
	public geom_plate(Vector z, double r, double thickness, Vector normal) {
		this.z = z;
		this.radius = r;
		this.thickness = thickness;

		createCoordinateSystem(normal);
		
	}
	
	@Override
	public double DE(Vector zz)
	{
		
		double uComp,vComp;

		// find a diferrence vector that shows that location of zz relative to the Torus's center.		
		Vector diff = zz.sub(z);
		
		// Get the u and v components
		uComp = getComponent(diff,u);
		vComp = getComponent(diff,v);
		
			
		double angle = lineAngle2(uComp,vComp,0,0);// Find the angle of the projected u, and v components inside of the Torus's coordinate system.
		angle = degToRad(angle);
			
		double radius = min(this.radius,distance(0,0,uComp,vComp));
		
		// The magic step that finds the nearest location.
		// Essentially the nearest point is the one on the torus at the same angle as the projected u and v components.
		Vector nearest = z.add(u.mult(radius*cos(angle))).add(v.mult(radius*sin(angle)));

		// Now perform a simple and efficient sphere distance calcualtion.
		return abs(nearest.sub(zz).mag()-thickness);

	}

}
