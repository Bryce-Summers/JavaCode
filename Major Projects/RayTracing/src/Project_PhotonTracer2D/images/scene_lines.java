package Project_PhotonTracer2D.images;

import Components.Geometry;
import Components.Light;
import Components.Material;
import Components.photonColor;
import Math.Vector3;
import Project_PhotonTracer2D.Geometries.g_Circle;
import Project_PhotonTracer2D.Geometries.g_line;
import Project_PhotonTracer2D.Rendering.Scene;

/*
 * A Scene for testing my 2D ray Tracer.
 */


public class scene_lines extends Scene
{
	int numPhotons;
	double exposure;

	Geometry[] geometries;
	Light[] lights;
	
	public scene_lines(int width, int height, int num_photons_per_light, double exposure)
	{
		super(width, height);
		
		this.numPhotons = num_photons_per_light;
		this.exposure   = exposure;
	}

	@Override
	protected void makeImage()
	{
		initialize_geometry();
		initialize_lights();
		
		for(Light l: lights)
		{
			shoot
		}
	}
	
	@Override
	protected void initialize_geometry()
	{
		geometries = new Geometry[3];
	 
		photonColor black = photonColor.BLACK;
		photonColor white = photonColor.WHITE;
				
		// Conservation of energy dictates that these value cannot add to more than 255 in any channel.
		photonColor specular     = black;//new photonColor(55, 55, 55);
		photonColor diffuse      = black;//new photonColor(255, 255, 255);
		photonColor transmission = new photonColor(255, 0, 0);//black;//new photonColor(255, 255, 255);
		
		// 255 - the sum is the coefficient of absorption.
		
		
		Material mat = new Material(2.0, 0.0, diffuse, specular, transmission);
		/*
		g_Circle circle1 = new g_Circle(mat, new Vector3(500, 500), 490);
		geometries[0] = circle1;
		g_Circle circle2 = new g_Circle(mat, new Vector3(300, 500), 100);
		geometries[1] = circle2;
		g_Circle circle3 = new g_Circle(mat, new Vector3(500, 400), 7);
		geometries[2] = circle3;
		*/
		
		g_line line1;
		
		Vector3 center = new Vector3(500, 500);
		Vector3 bottom = new Vector3(500, 1000);
		
		line1 = new g_line(mat, center, bottom);
		geometries[0] = line1;
		
		line1 = new g_line(mat, center, new Vector3(250, 250));
		geometries[1] = line1;
		
		line1 = new g_line(mat, center, new Vector3(600, 250));
		geometries[2] = line1;
		
	}

	@Override
	protected void initialize_lights()
	{
		lights = new Light[2];
		
		Light light;
		light = new Light(new Vector3(800, 800), new photonColor(255, 255, 255), .00000001, .000002);
		light.setRadius(50);
		lights[0] = light;
		
		
		light = new Light(new Vector3(200, 800), new photonColor(255, 255, 255), .00000001, .000002);
		light.setRadius(20);
		lights[1] = light;
	}

}