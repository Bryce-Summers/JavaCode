package Components;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.DoubleMath.Vector;

public abstract class World extends ColorCalculator
{

	final Camera C;
	final int recursion_depth;
	final Geometry[] data;
	final Light[] lights;
	
	// The fudge factor.
	public static double epsilon = .00001;
	
	// -- Constructors.
	public World(Dimension dim) 
	{
		super(dim);
		C = getCamera();
		recursion_depth = C.getReflectanceDepth();
		
		if(recursion_depth <= 0)
		{
			throw new Error("Please use a positive recursion depth!");
		}
		
		data   = getData();
		lights = getLights();
	
		antiAliasing = 4;
	}

	/*
	public World(int width, int height)
	{
		super(width, height);
		C = getCamera();
		recursion_depth = C.getReflectanceDepth();
		
		if(recursion_depth <= 0)
		{
			throw new Error("Please use a positive recursion depth!");
		}
		
		data   = getData();
		lights = getLights();
	}
	*/

	// Used for sub classes to specify the desired camera.
	protected abstract Camera getCamera();
	protected abstract Geometry[] getData();
	protected abstract Light[] getLights();
	
	public Color getColor(double x, double y)
	{
		Vector z  = C.getPoint(x, y);
		Vector dz = C.getDirection(x, y);
		
		photonColor output = Raytrace(z, dz, recursion_depth);
		
		// FIXME : Add multiple shots.
		return output.toColor();
	}

	// ASSUMES that dz is a normalized vector.
	public photonColor Raytrace(Vector z, Vector dz, int depth)
	{
		Geometry g = findClosest(z, dz);
		
		// If no geometry was hit, then we return the default color.
		if(g == null)
		{
			return photonColor.BLACK;
		}
		
		Material mat = g.material;
		
		// Use the Phong Reflectance model to approximate the rendering equation.
		
		double distance = g.computeDistance(z, dz);
		// FIXME : Perhaps this equation should not be parameterized.
		Vector surface_point = g.computeIntersectionPoint();
		Vector normal = g.getNormal();
		normal = normal.norm();

		Vector reflect_vector = g.getReflectionDirection();
		
		photonColor illumination = photonColor.BLACK;
		
		// Handle light coming along the reflection ray.
		// The reflection light is considered to be specular.
		if(mat.specular.nonZero() && depth > 1)
		{
			illumination = Raytrace(surface_point.add(reflect_vector.mult(epsilon)), reflect_vector, depth - 1);
			illumination = illumination.mult(mat.specular);
			
			if(illumination.blue > 30)
			{
				System.out.println("Blue");
			}
		}
		
		// Start out with just the ambient term.
		// FIXME : Derive the ambient light from global illumination, reflections, and refractions.
		/*if(depth <= 1)
		{
			illumination = ambient_light.mult(mat.coef_ambient);
		}*/
		/*else
		{
			illumination = Raytrace(z.add(reflectance_direction.mult(epsilon)), reflectance_direction, depth - 1);
			illumination.mult(mat.reflectance);
		}*/
		
		Vector toViewer = dz.mult(-1);
		
		for(Light L : lights)
		{
			Vector toLight = L.getLocation().sub(surface_point);
			double toLight_mag = toLight.mag();
			toLight = toLight.div(toLight_mag);
			
			z = surface_point.add(toLight.mult(epsilon));
			dz = toLight;
			
			// Hard shadows due to non visibility.
			Geometry blocking = findClosest(z, dz);
			if(blocking != null && blocking.computeDistance(z, dz) < toLight_mag)
			{
				continue;
			}

			// The direction a particle of light would bounce when it hits this geometry at this point.
			Vector light_reflectance = toLight.mult(-1).reflection(normal);
			double specular_factor_d = Math.max(0.0, light_reflectance.dot(toViewer));
			photonColor specular_factor = mat.specular.mult(Math.pow(specular_factor_d, mat.shininess));
			
			photonColor diffuse  = L.diffuse(distance) .mult(mat.diffuse.mult(Math.max(0.0, toLight.dot(normal))));
			
			photonColor specular = L.specular(distance).mult(specular_factor);
			
			illumination = illumination.add(diffuse, specular);
		}
		
		return illumination.mult(attenuation(distance));
	}
	
	
	double attenuation(double distance)
	{
		double amount = 1.0 - .0001*(distance) - .0004*distance*distance;
		
		return Math.max(0, amount);
	}
	
	// Solves the visibility problem.
	public Geometry findClosest(Vector z, Vector dz)
	{
		double min_dist = Double.MAX_VALUE;
		Geometry min_g = null;
		
		// Trivial bare bones closest geometry loop.
		// FIXME : Add some bounding boxes.
		for(Geometry g : data)
		{
			double dist = g.computeDistance(z, dz);
			if(0 <= dist && dist < min_dist)
			{
				min_dist = dist;
				min_g = g;
			}
		}
		
		return min_g;
	}
	
	public abstract Object clone();
	
	public int getAliasingThreshold()
	{
		return 10;
	}
}
