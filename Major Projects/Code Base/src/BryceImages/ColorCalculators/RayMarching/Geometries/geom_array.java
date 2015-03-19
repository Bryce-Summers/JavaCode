package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

public class geom_array extends Geometry {

	Vector c1;
	
	// FIXME: perhaps allow different spacing in the respective u and v directions.
	double blockSize;
	
	// The geometry that will be duplicated.
	Geometry element;
	public geom_array(Vector c1, double blockSize, Geometry element)
	{
		this.element = element;
		
		n = element.n;
		u = element.u;
		v = element.v;
		
		this.c1 = c1;
		this.blockSize = blockSize;
	}

	@Override
	public double DE(Vector z)
	{
		
		
		
		return element.DE(v(abs(z.x % blockSize) - blockSize/2, abs(z.y % blockSize) - blockSize/2, z.z));
		
		/*
		 
		double uComp, vComp,nComp;
		Vector diff = z.sub(c1);
		
		// Get the u and v components
		uComp = getComponent(diff,u);
		vComp = getComponent(diff,v);
		nComp = getComponent(diff,num_columns);
		
		uComp = abs(uComp);
		vComp = abs(vComp);
		
		// Bound them within this square array's bounds.
		//uComp = bound(uComp,0,10);
		//vComp = bound(vComp,0,10);
		
		uComp = uComp % blockSize - blockSize/2;
		vComp = vComp % blockSize - blockSize/2;
		
		z = c1.add(u.mult(uComp)).add(v.mult(vComp)).add(num_columns.mult(nComp));
		
		return element.DE(z);
		*/
		
	}

}
