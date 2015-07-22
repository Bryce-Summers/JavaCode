package Components.Geometries.DE;

import static BryceImages.ColorCalculators.RayMarching.BryceMath.arcCos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.arcSin;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.arcTan;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.ln;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.pow;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sin;

import Components.DistanceFunction;
import Math.Vector3;

public class de_MandelBulb implements DistanceFunction
{
	
	final static boolean julia = false;

	@Override
	public double DE(Vector3 v)
	{				
		int maxIterations=9;
		int power = 20;
		double bailout = 5000;
		
		
		double zx = v.getX();//z;// The coordinates of the iterated point
		double zy = v.getY();//x;
		double zz = v.getZ();//y;
		
		double cx,cy,cz;// The coordinates of the c value of iteration
		if(julia){// Constant for julia set
			cx =  .84;
			cy =  .04;
			cz = -.44;
		}
		else{// Dependent on initial location for Mandelbrot.
		
			cx = zx;
			cy = zy;
			cz = zz;
		}
		
		
		int pd = power - 1;			 // power for derivative

		// Convert z to polar coordinates
		double r  = distance(0,0,0,zx,zy,zz);
		double th = arcTan(zy/zx);
		double ph = arcSin(zz / r);

		// Record z orbit distance for ambient occulsion shading
		//if (r < min_dist) min_dist = r;

		double dzx,dzy,dzz;
		double ph_dz = 0.0;
		double th_dz = 0.0;
		double r_dz	 = 1.0;
		double powR, powRsin;

		// Iterate to compute the distance estimator.
		int n = 0;
		for (n = 0; n < maxIterations; n++) {
			// Calculate derivative of
			powR = power * pow(r, pd);
			powRsin = powR * r_dz * sin(ph_dz + (pd*ph) );
			dzx = powRsin * cos(th_dz + (pd*th)) + 1.0;
			dzy = powRsin * sin(th_dz + (pd*th));
			dzz = powR * r_dz * cos(ph_dz + (pd*ph));
			// polar coordinates of derivative dz
			r_dz  = distance(0,0,0,dzx,dzy,dzz);
			th_dz = arcTan(dzy/dzx);
			ph_dz = arcCos(dzz / r_dz);

			// z iteration
			powR = pow(r, power);
			powRsin = sin(power*ph);
			zx = powR * powRsin * cos(power*th);
			zy = powR * powRsin * sin(power*th);
			zz = powR * cos(power*ph);
			
			zx += cx;
			zy += cy;
			zz += cz;

			// The triplex power formula applies the azimuthal angle rotation about the y-axis.
			// Constrain this to get some funky effects
			//if (radiolaria && z.y > radiolariaFactor) z.y = radiolariaFactor;

			r  = distance(0,0,0,zx,zy,zz);
			//if (r < min_dist) min_dist = r;
			if (r > bailout) break;

			th = arcTan(zy/ zx);// + phase.x;
			ph = arcCos(zz / r);// + phase.y;

			}

			// Return the distance estimation value which determines the next raytracing
			// step size, or if whether we are within the threshold of the surface.
		return 0.5 * r * ln(r)/r_dz;
	}

}
