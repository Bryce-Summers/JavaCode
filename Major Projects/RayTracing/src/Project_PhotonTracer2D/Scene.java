package Project_PhotonTracer2D;

import java.awt.Color;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Rendering.ColorCalculator;
import Components.Geometry;
import Components.Light;
import Components.Material;
import Components.Material.Bounce_Type;
import Components.photonColor;
import Math.Vector3;
import Math.Vector_math;

/*
 * Describes a 2 Dimensional Scene.
 * 
 * Written by Bryce Summers on 7/16/2015.
 */

public abstract class Scene extends ColorCalculator
{
	final static double epsilon = .00000001;
	
	protected int recursion_limit = 3;
	
	// Bounds for the viewing window.
	int x1, x2, y1, y2;
	
	protected Geometry[] geometries = null;
	protected Light[] lights = null;	
	
	IrradianceCache irradiance;
	
	Light current_light = null;	
	
	public Scene(int width, int height, int num_photons_per_light, double exposure)
	{
		super(width, height);
	
		initialize_geometry();
		initialize_lights();
		
		if(geometries == null)
		{
			throw new Error("ERROR: geometries needs to be initialized.");
		}
		
		if(lights == null)
		{
			throw new Error("ERROR: lights needs to be initialized.");
		}
				
		
		// Calculate the Irradiance.
		irradiance = new IrradianceCache(width, height);
		
		int GRANULARITY = 10000;
		
		int numLights = lights.length;
		for(int light = 0; light < numLights; light++)
		{
			System.out.println("Light " + light);
			
			current_light = lights[light]; 
			for(int j = 0; j < num_photons_per_light; j++)
			{
				if(j % GRANULARITY == (GRANULARITY - 1))
				System.out.println("" + (j + 1)/GRANULARITY + " * " + GRANULARITY + " beams done.");
				
				shootPhoton();
			}
		}
		
		
		irradiance.scale_exposure(exposure);

	}
	
	protected abstract void initialize_geometry();
	protected abstract void initialize_lights();

	@Override
	public Color getColor(double x, double y)
	{
		return irradiance.getColor(x, y);
	}


	// -- Irradiance Calculation functions.

	// Trace the path of a photon emitted from the given light source.
	private void shootPhoton()
	{


		// The three properties of this light.
		Vector3 origin    = current_light.getLocation();
		Vector3 direction = current_light.getDirection();
		
		photonColor photon = current_light.diffuse(0);

		trace(origin, direction, photon, recursion_limit, Geometry.INDICE_VACUUM);

	}


	private void trace(Vector3 src, Vector3 direction, photonColor photon, int recursion_left, double refractive_indice)
	{		
		Geometry geom = Geometry.visible(src,  direction, geometries);
		
		// Going off into space, no collision.
		if(geom == null)
		{
			return;
			//throw new Error("Not possible, because we surround the scene with impermeable walls.");
		}
				
		Vector3 dest = geom.computeIntersectionPoint();
		
		// Add the light.
		double attenuation = add_light_line(src.getX(), src.getY(), dest.getX(), dest.getY(), photon);
	
		// FIXME : Implement reflectance, transmittance / refraction.
		
		// 100 % absorption right now.
		
		Material mat = geom.material;
		
		// Base Case.
		if(recursion_left < 1)
		{
			return;
		}		
		
		// Recursive photon Bouncing.
		
		// Diffuse.
		Vector3 normal = geom.getNormal();
		
		// Make sure the normal is not the wrong direction.
		if(normal.dot(direction) > 0)
		{
			normal = normal.mult(-1);
		}
			
		
		Bounce_Type val = mat.getWeightedBounceEvent();
		
		switch(val)
		{
		case DIFFUSE:
			Vector3 diffuse_bounce_dir = Vector_math.random_hemi_2D(normal);
			photonColor photon_new = photon.mult(attenuation).mult(mat.diffuse);
			trace(dest.add(diffuse_bounce_dir.mult(epsilon)), diffuse_bounce_dir, photon_new, recursion_left - 1, refractive_indice);
			break;
		case SPECULAR:
			photon_new = photon.mult(attenuation).mult(mat.specular);
			Vector3 specular_bounce_dir = geom.getReflectionDirection();
			trace(dest.add(normal.mult(epsilon)), specular_bounce_dir, photon_new, recursion_left - 1, refractive_indice);
			break;
			
		case TRANSMISSION:
			Vector3 refracted_bounce_dir = geom.refract(refractive_indice);
			
			// Total Internal Reflection.
			if(refracted_bounce_dir == null)
			{
				return;
			}
			
			photon_new = photon.mult(attenuation).mult(mat.transmission);
			double bounce_refraction_indice = geom.getNextRefractiveIndex(refractive_indice);
			trace(dest.add(refracted_bounce_dir.mult(epsilon)), refracted_bounce_dir, photon_new, recursion_left - 1, bounce_refraction_indice);
			break;
		}
		

		return;
		
	}
	
	// -- Draw antialiased line of width 1.
	// By draw It means that we are adding irradiance.
	// Returns the attenuation that has transpired along the beam.	
	private double add_light_line(double x1, double y1, double x2, double y2, photonColor photon_start)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		boolean vertical = Math.abs(dy) > Math.abs(dx);
	
		boolean inverted = false;
		
		// Make sure the main coordinate is increasing.
		if((vertical && dy < 0) ||
		   (!vertical && dx < 0))
		{
			double temp_x = x1;
			x1 = x2;
			x2 = temp_x;
			
			double temp_y = y1;
			y1 = y2;
			y2 = temp_y;
			
			inverted = true;
		}
	
		if(vertical)
		{
			return light_line_helper(y1, y2, x1, x2, true, inverted, photon_start);
		}
		else
		{
			return light_line_helper(x1, x2, y1, y2, false, inverted, photon_start);
		}
	}
	
	// Helps to reconfigure the coordinates.
	// Returns the total attenuation.
	private double light_line_helper(double x1, double x2, double y1, double y2, 
				boolean rotated, boolean invert1, photonColor photon_start)
	{

		// Left End point calculations.

		int x1_int = (int) x1;
		int y1_int = (int) y1;

		double per_x1_c = x1 - x1_int;
		double per_y1_c = y1 - y1_int;
		
		double per_x1 = 1 - per_x1_c;
		double per_y1 = 1 - per_y1_c;
		
		double scalar_1 = per_x1 * per_y1;
		irradiance.addIrradiance(x1_int, y1_int, photon_start, scalar_1, rotated);
		scalar_1 = per_x1 * per_y1_c;
		irradiance.addIrradiance(x1_int, y1_int + 1, photon_start, scalar_1, rotated);
 

		// Right End point calculations.

		int x2_int = (int) x2;
		int y2_int = (int) y2;

		double per_x2_c = x2 - x2_int;
		double per_y2_c = y2 - y2_int;
		double per_y2 = 1 - per_y2_c;
		
		double scalar_2 = per_x2_c*per_y2_c;
		irradiance.addIrradiance(x2_int + 1, y2_int + 1, photon_start, scalar_2, rotated);
		scalar_2 = per_x2_c*per_y2;
		irradiance.addIrradiance(x2_int + 1, y2_int, photon_start, scalar_2, rotated);

		
		// Draw the main body.
		
		// ASSERT this is positive.
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		
		double distance = BryceMath.distance(x1, y1, x2, y2);
		
		for(int x = x1_int + 1; x <= x2_int; x++)
		{
			double percentage = (x - x1) / dx;
			double y = y1 + dy*percentage;
			
			int y_int = (int)y;
			double per_y_c = y - y_int;
			double per_y = 1 - per_y_c;
			
			// An within pixel accuracy approximation for the attenuation.
			// For more prescision, use the actual distance.
			double partial_distance = invert1 ? (1 - percentage)*distance
											  : percentage*distance;
			
			double attenuation = current_light.attenuation(partial_distance);
						
			irradiance.addIrradiance(x, y_int, photon_start,     per_y*attenuation, rotated);
			irradiance.addIrradiance(x, y_int + 1, photon_start, per_y_c*attenuation, rotated);
		}
		
		return current_light.attenuation(distance);
	}

	
}