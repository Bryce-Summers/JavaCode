package Components;

import BryceMath.DoubleMath.Vector;
import Math.Vector3;
import Math.Vector_math;

/*
 * Written on 12 / 27 / 2014.
 * 
 * Purpose : This class represents a light used in a raytracing scene specification.
 */


public class Light
{
	// The position of the Light.
	private final Vector3 position;
	
	final double quad, lin;
	
	photonColor irradiance;
	
	
	double radius = 0.0;
	boolean three_dimensions = false;
	
	/**
	 * 
	 * @param position
	 * @param intensity
	 * @param quad 		The quadratic attenuation of the light.
	 * @param lin  		The linear attenuation of the light.
	 * @param constant	The constant attenuation of the light.
	 */
	public Light(Vector3 position, photonColor irradiance, double quad, double lin)
	{
		this.position = position;
		this.irradiance = irradiance;
		this.quad = quad;
		this.lin  = lin;
	}
	
	// Computes the intensity of a photon emitted from this light source
	// after it has traveled the given distance.
	// FIXME : Have separate components for specular emittance and diffuse emmitance.
	public photonColor specular(double distance)
	{
		double factor = attenuation(distance);
		return irradiance.mult(factor);
	}
	
	public photonColor diffuse(double distance)
	{
		double factor = attenuation(distance);
		return irradiance.mult(factor);
	}
	
	// The percentage of light that is still alive after it has traveled a certain distance.
	public double attenuation(double distance)
	{
		double amount = 1.0 - lin*(distance) - quad*distance*distance;
		
		return Math.max(0, amount);
	}
	
	Vector3 last_dir = null;
	public Vector3 getLocation()
	{		
		Vector3 dir = computeRandomVector();
		last_dir = dir;
						
		// FIXME : Perhaps we really want a strict bound on the effective radius of the light.
		double dist = Math.random()*radius;
		
		return position.add(dir.mult(dist));
	}
	
	// Returns a probability weighted direction.
	// Should only be called after a call to get Location.
	// Tells the user where the original photon is heading.
	public Vector3 getDirection()
	{		
		//return computeRandomHemiVector();
		return computeRandomVector();
	}
	
	private Vector3 computeRandomVector()
	{
		if(three_dimensions)
		{
			return Vector_math.random_dir();
		}
		else
		{
			return Vector_math.random_dir_2D();
		}
	}
	
	private Vector3 computeRandomHemiVector()
	{
		if(three_dimensions)
		{
			return Vector_math.random_hemi(last_dir);
		}
		else
		{
			return Vector_math.random_hemi_2D(last_dir);
		}
	}
	
	public void setRadius(double radius)
	{
		this.radius = radius;
	}
	
	public void set2Dimensions()
	{
		this.three_dimensions = false;
	}
	
	public void set3Dimension()
	{
		this.three_dimensions = true;
	}
	
}
