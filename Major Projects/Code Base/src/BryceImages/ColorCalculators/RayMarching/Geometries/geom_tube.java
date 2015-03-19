package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

public class geom_tube extends Geometry {

	Vector z;
	double radius, thickness, nMax;

	public geom_tube(Vector z, Vector z2, double r, double thickness) {
		this.z = z;
		this.radius = r;
		this.thickness = thickness;
		Vector normal = z2.sub(z);
		nMax = normal.mag();
		
		// Create a unique coordinate system that is appropiate for this torus.
		createCoordinateSystem(normal);
		
		
		
	}

	@Override
	public double DE(Vector zz) {
		
		double uComp,vComp,nComp;

		// find a diferrence vector that shows that location of zz relative to the Torus's center.		
		Vector diff = zz.sub(z);
		
		// Get the u and v components
		uComp = getComponent(diff,u);
		vComp = getComponent(diff,v);
		nComp = getComponent(diff,n);
		nComp = bound(nComp,0,nMax);
					
		double angle = lineAngle2(uComp,vComp,0,0);// Find the angle of the projected u, and v components inside of the Torus's coordinate system.
		angle = degToRad(angle);
			
		
		// The magic step that finds the nearest location.
		// Essentially the nearest point is the one on the torus at the same angle as the projected u and v components.
		Vector nearest = z.add(u.mult(radius*cos(angle))).add(v.mult(radius*sin(angle))).add(n.mult(nComp));

		// Now perform a simple and efficient sphere distance calcualtion.
		return abs(nearest.sub(zz).mag()-thickness);

	}

}
