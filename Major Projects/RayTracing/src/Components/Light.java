package Components;

import BryceMath.DoubleMath.Vector;

/*
 * Written on 12 / 27 / 2014.
 * 
 * Purpose : This class represents a light used in a raytracing scene specification.
 */


public class Light
{
	// The position of the Light.
	private final Vector position;
	
	final double quad, lin;
	
	photonColor irradiance;
	
	
	/**
	 * 
	 * @param position
	 * @param intensity
	 * @param quad 		The quadratic attenuation of the light.
	 * @param lin  		The linear attenuation of the light.
	 * @param constant	The constant attenuation of the light.
	 */
	public Light(Vector position, photonColor irradiance, double quad, double lin)
	{
		this.position = position;
		this.irradiance = irradiance;
		this.quad = quad;
		this.lin  = lin;
	}
	
	// Computes the intensity of a photon emitted from this light source
	// after it has traveled the given distance.
	// FIXME : Have seprate components for specular emittance and diffuse emmitance.
	photonColor specular(double distance)
	{
		double factor = attenuation(distance);
		return irradiance.mult(factor);
	}
	
	photonColor diffuse(double distance)
	{
		double factor = attenuation(distance);
		return irradiance.mult(factor);
	}
	
	double attenuation(double distance)
	{
		double amount = 1.0 - lin*(distance) - quad*distance*distance;
		
		return Math.max(0, amount);
	}
	
	
	Vector getLocation()
	{
		
		Vector dir = Vector_math.random_dir();
		
		// FIXME : Perhaps we really want a strict bound on the effective radius of the light.
		double dist = Math.random();
		
		return position.add(dir.mult(dist));
	}
	

}
