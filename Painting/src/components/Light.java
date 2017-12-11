package components;

import BryceMath.DoubleMath.Vector;
import geometry.Point;
import math.Direction_math;

/*
 * Written on 12 / 27 / 2014.
 * 
 * Purpose : This class represents a light used in a raytracing scene specification.
 * 
 * A light is an object that defines a spectrum of possible photons and samples from it.
 * 
 * A photon is conceptualized as follows:
 *  - Location.
 *  - Direction.
 *  - Maximum Distance to Travel.
 *  - Maximum number of bounces.
 */


public class Light
{
	// The position of the Light.
	private final Point position;
	
	final double quad, lin;
	
	photonColor irradiance;
	
	
	double radius = .1;
	boolean three_dimensions = true;
	
	/**
	 * 
	 * @param position
	 * @param intensity
	 * @param quad 		The quadratic attenuation of the light.
	 * @param lin  		The linear attenuation of the light.
	 * @param constant	The constant attenuation of the light.
	 */
	public Light(Point position, photonColor irradiance, double quad, double lin)
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
	
	Point last_dir = null;
	public Point getLocation()
	{	
		/*
		double size = .3;
		double dx = Math.random()*size - size/2;
		double dy = Math.random()*size - size/2;
		
		return position.add(new Point(dx, dy, 0));
		*/
		
		
		Point dir = computeRandomVector();
		last_dir = dir;
						
		// FIXME : Perhaps we really want a strict bound on the effective radius of the light.
		double dist = Math.random()*radius;
		
		return position.add(dir.multScalar(dist));
		
	}
	
	// Returns a probability weighted direction.
	// Should only be called after a call to get Location.
	// Tells the user where the original photon is heading.
	public Point getDirection()
	{		
		//return computeRandomHemiVector();
		return computeRandomVector();
	}
	
	public int getBounceLimit()
	{
		return 3;// FIXME
	}
	
	private Point computeRandomVector()
	{
		if(three_dimensions)
		{
			//return Vector_math.random_hemi(new Point(0, 0, 1));
			return Direction_math.random_dir();
		}
		else
		{
			return Direction_math.random_dir_2D();
		}
	}
	
	private Point computeRandomHemiVector()
	{
		if(three_dimensions)
		{
			return Direction_math.random_hemi(last_dir);
		}
		else
		{
			return Direction_math.random_hemi_2D(last_dir);
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
