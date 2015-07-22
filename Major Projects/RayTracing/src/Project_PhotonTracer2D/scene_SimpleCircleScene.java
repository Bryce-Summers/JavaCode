package Project_PhotonTracer2D;

import Components.Geometry;
import Components.Light;
import Components.Material;
import Components.photonColor;
import Math.Vector3;
import Project_PhotonTracer2D.Geometries.g_Circle;

/*
 * A Scene for testing my 2D ray Tracer.
 */


public class scene_SimpleCircleScene extends Scene
{

	public scene_SimpleCircleScene(int width, int height, int num_photons_per_light, double exposure)
	{
		super(width, height, num_photons_per_light, exposure);
	}

	@Override
	protected void initialize_geometry()
	{
		geometries = new Geometry[3];
		
		photonColor black = photonColor.BLACK;
		photonColor white = photonColor.WHITE;
				
		// Conservation of energy dictates that these value cannot add to more than 255 in any channel.
		photonColor specular     = white;//new photonColor(255, 0, 0);
		photonColor diffuse      = black;//new photonColor(0, 205, 0);
		photonColor transmission = black;//new photonColor(200, 50, 255);
		
		// 255 - the sum is the coefficient of absorption.
		
		
		Material mat = new Material(1.1, 0.0, diffuse, specular, transmission);
		
		g_Circle circle1 = new g_Circle(mat, new Vector3(500, 500), 490);
		geometries[0] = circle1;
		g_Circle circle2 = new g_Circle(mat, new Vector3(300, 500), 100);
		geometries[1] = circle2;
		g_Circle circle3 = new g_Circle(mat, new Vector3(500, 400), 7);
		geometries[2] = circle3;		
		
		
	}

	@Override
	protected void initialize_lights()
	{
		lights = new Light[1];
		
		int intensity = 100;
		Light light = new Light(new Vector3(600, 500), new photonColor(255, 255, 255), .00000001, .0002);
		
		light.setRadius(50);
		
		lights[0] = light;
	}

}