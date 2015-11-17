package Worlds;

import java.awt.Color;
import java.awt.Dimension;

import Components.Camera;
import Components.Geometry;
import Components.Light;
import Components.Material;
import Components.World;
import Components.photonColor;
import Math.Vector3;
import Project3D.Geometries.g_Sphere;
import Project3D.Geometries.g_Triangle;

public class ReflectanceSpheres extends World
{

	public ReflectanceSpheres(Dimension dim)
	{
		super(dim);
		
		antiAliasing = 10;
	}

	@Override
	protected Camera getCamera()
	{
		return new Camera(new Vector3(0, 20, -10), new Vector3(0, 2, 0), new Vector3(0, 1, 0),
				3.5,
				room_width, room_height,
				4);
	}

	@Override
	protected Geometry[] getData()
	{
		// Plane Material.
		Material mat2 = new Material(
				1.0,
				100.0,
				new photonColor(new Color(0, 0, 20)),  // diffuse.
				new photonColor(new Color(0, 0, 255))   // specular.
				);

		Color Ball_specular = new Color(255, 255, 255);

		Material mat_red = new Material(
				1.5,
				100.0,
				new photonColor(Color.RED),
				new photonColor(Ball_specular)
				);
		
		Material mat_green = new Material(
				1.5,
				100.0,
				new photonColor(Color.GREEN), 
				new photonColor(Ball_specular)
				);
		
		Material mat_yellow = new Material(
				1.5,
				100.0,
				new photonColor(Color.YELLOW), 
				new photonColor(Ball_specular)
				);
		
		Material mat_blue = new Material(
				1.5,
				100.0,
				new photonColor(Color.BLUE), 
				new photonColor(Ball_specular)
				);
		
		Geometry[] output = new Geometry[5];
		
		output[0] = new g_Triangle(mat2, new Vector3 (1, 5, 0), new Vector3(1, 0, 1), new Vector3(0, 0, 1), true);
		output[1] = new g_Sphere(mat_red, new Vector3(-2, 0, 0), 1);
		output[2] = new g_Sphere(mat_green, new Vector3(2, 0, 0), 1);
		output[3] = new g_Sphere(mat_yellow, new Vector3(0, -2, 0), 1);
		output[4] = new g_Sphere(mat_blue, new Vector3(0, 2, 0), 1);

				
		return output;
	}

	@Override
	protected Light[] getLights()
	{
		Light[] output = new Light[2];
		
		output[0] = new Light(new Vector3(4, 0, -11),  new photonColor(Color.WHITE).mult(.8), .0001, .002);
		output[1] = new Light(new Vector3(-4, 0, -11), new photonColor(Color.WHITE).mult(.8), .0001, .002);
		
		return output;
	}

	@Override
	public Object clone()
	{
		return new ReflectanceSpheres(dim);
	}

}