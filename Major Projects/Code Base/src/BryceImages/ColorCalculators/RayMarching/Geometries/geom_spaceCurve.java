package BryceImages.ColorCalculators.RayMarching.Geometries;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.Vector;

public class geom_spaceCurve extends Geometry {


	protected double i1;
	protected double i2;
	double val1, val2,oldVal;
	@Override
	public double DE(Vector z)
	{
		double i2 = this.i2;
		double i1 = this.i1;

		val1 = val(i1,z);
		val2 = val(i2,z);

		/*
		while(i2 - i1 > .0001)
		{
			if(i1<i2)
			{
				if(val((i1+i2)/2,z) < i2)
				{
					i2 = (i1+i2)/2;
				}
				else
					i2 = (i1+i2*2)/3;
			}
			else
			{
				if(val((i1+i2)/2,z) < i1)
				{
					i1 = (i1+i2)/2;
				}
				else
					i1 = (i2+i1*2)/3;
			}
		}*/
	
		
		while(i2 - i1 > .0001)// While the binary search is outside of the tolerance.
		{
			double midI = (i1 + i2) /2;
			// Calculate the derivative in the middle of the two endpoints.
			double dF = der(midI, z);
			
			if(abs(dF) < .00001)
			{
				// If derivative is equal to zero claim this point to be the minimum point.
				i1 = midI;
				i2 = midI;
			}
			if(dF > 0)
			{
				i2 = midI;

			}
			else
			{
				i1 = midI;
			}
		}
			
		
		return abs(func1(i1).sub(z).mag() - radius(i1));
		
	}

	public double der(double i,Vector z)
	{
		double e = .0000001;
		double v1,v2;
		v1 = val(i,z);
		v2 = val(i+e,z);
		
		return (v2 - v1)/e;
		
	}
	public Vector func1(double i)
	{
		//return new Vector(i/100.0,-i/5 ,sqrt(2*i/100.0));
		//return new Vector(i, 2 * cos(degToRad(i*20)) ,0 );
		//return new Vector(degToRad(i)/10.0*cos(degToRad(i)),0 , degToRad(i)/10.0*sin(degToRad(i)));
		return new Vector(i,sqrt(i),i*i/1000);
	}
	
	private double val(double i,Vector z)
	{
		return abs(func1(i).sub(z).mag() - radius(i));
	}
	public double radius(double i)
	{
		return max(.05,sin(degToRad(i)));
	}
	
}
