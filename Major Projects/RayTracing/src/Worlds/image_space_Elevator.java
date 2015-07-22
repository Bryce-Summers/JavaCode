package Worlds;

import java.awt.Color;
import java.awt.Dimension;

import Components.Camera;
import Components.Geometry;
import Components.Light;
import Components.Material;
import Components.World;
import Components.photonColor;
import Components.Geometries.DE.de_Sphere;
import Components.Geometries.DE.g_DistanceFunction;
import Components.Geometries.DE.de_MandelBulb;
import Math.Vector3;
import Project3D.Geometries.g_Sphere;
import Project3D.Geometries.g_Triangle;

public class image_space_Elevator extends World
{

	public image_space_Elevator(Dimension dim)
	{
		super(dim);
		
		antiAliasing = 3;
	}

	@Override
	protected Camera getCamera()
	{
		return new Camera(new Vector3(0, 3, .01), new Vector3(0, 0, 0), new Vector3(0, 1, 0),
				1,
				room_width, room_height,
				4);
	}

	@Override
	protected Geometry[] getData()
	{

		Material mat_clouds = new Material(
				1.0,
				100.0,
				new photonColor(new Color(200, 200, 200)),  // diffuse.
				new photonColor(new Color(255, 255, 255))  	// specular.
				);
		
		Material mat_sky = new Material(
				1.0,
				100.0,
				new photonColor(new Color(0, 100, 200)),  // diffuse.
				new photonColor(new Color(0, 0, 0))  	// specular.
				);
		
		Geometry[] output = new Geometry[2];
		

		output[0] = new g_DistanceFunction(mat_clouds, new de_MandelBulb());
		output[1] = new g_Sphere(mat_sky, new Vector3(0, 0, 0), 10);

		return output;
	}

	@Override
	protected Light[] getLights()
	{
		Light[] output = new Light[1];
		
		output[0] = new Light(new Vector3(5, 5, 5),  new photonColor(Color.WHITE), .0001, .002);
		//output[1] = new Light(new Vector3(-4, 0, -11), new photonColor(Color.WHITE), .0001, .002);
		
		return output;
	}

	@Override
	public Object clone()
	{
		return new image_space_Elevator(dim);
	}

}