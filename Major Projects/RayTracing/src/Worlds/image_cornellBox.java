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

public class image_CornellBox extends World
{

	public image_CornellBox(Dimension dim)
	{
		super(dim);
		
		antiAliasing = 5;
	}

	@Override
	protected Camera getCamera()
	{
		return new Camera(new Vector3(0, 3, -.01), new Vector3(0, 0, 0), new Vector3(0, 1, 0),
				1,
				room_width, room_height,
				4);
	}

	@Override
	protected Geometry[] getData()
	{

		Material mat2 = new Material(
				1.0,
				100.0,
				new photonColor(new Color(0, 0, 0)),  // diffuse.
				new photonColor(new Color(0, 0, 255))   // specular.
				);

		Color Ball_specular = new Color(0, 0, 0);

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
				new photonColor(new Color(200, 200, 200)), 
				new photonColor(Ball_specular)
				);
		
		Material mat_sphere1 = new Material(
				1.5,
				100.0,
				new photonColor(new Color(10, 10, 10)), 
				photonColor.BLACK,
				photonColor.WHITE
				);
		
		Material mat_sphere2 = new Material(
				1.5,
				100.0,
				new photonColor(new Color(10, 10, 10)), 
				photonColor.WHITE				
				);
		
		
		Vector3 p111 = new Vector3(-1, -1, -1);
		Vector3 p112 = new Vector3(-1, -1,  1);
		Vector3 p121 = new Vector3(-1,  1, -1);
		Vector3 p122 = new Vector3(-1,  1,  1);
		Vector3 p211 = new Vector3( 1, -1, -1);
		Vector3 p212 = new Vector3( 1, -1,  1);
		Vector3 p221 = new Vector3( 1,  1, -1);
		Vector3 p222 = new Vector3( 1,  1,  1);
		
		Geometry[] output = new Geometry[14];
		
		output[0] = new g_Triangle(mat_red, p112, p111, p122, false);
		output[1] = new g_Triangle(mat_red, p111, p121, p122, false);
		
		output[2] = new g_Triangle(mat_green, p211, p212, p222, false);
		output[3] = new g_Triangle(mat_green, p211, p222, p221, false);
		
		output[4] = new g_Triangle(mat_yellow, p121, p111, p221, false);
		output[5] = new g_Triangle(mat_yellow, p111, p211, p221, false);
		
		output[6] = new g_Triangle(mat_blue, p111, p112, p212, false);
		output[7] = new g_Triangle(mat_blue, p111, p212, p211, false);
		
		output[8] = new g_Triangle(mat_yellow, p112, p122, p222, false);
		output[9] = new g_Triangle(mat_yellow, p212, p112, p222, false);
		
		double radius = .3;
		output[10] = new g_Sphere(mat_sphere1, new Vector3(.5, .4, 1.0 - radius), radius);
		output[11] = new g_Sphere(mat_sphere2, new Vector3(-.3, -.3, 1.0 - radius), radius);
		
		// -- Front Wall.
		output[12] = new g_Triangle(mat_blue, p122, p121, p222, false);
		output[13] = new g_Triangle(mat_blue, p222, p121, p221, false);
		

		return output;
	}

	@Override
	protected Light[] getLights()
	{
		Light[] output = new Light[2];
		
		output[0] = new Light(new Vector3(.1, -.9, 0),  new photonColor(Color.WHITE), .0001, .002);
		output[1] = new Light(new Vector3(-.1, -.9, 0),  new photonColor(Color.WHITE), .0001, .002);
		//output[1] = new Light(new Vector3(-4, 0, -11), new photonColor(Color.WHITE), .0001, .002);
		
		return output;
	}

	@Override
	public Object clone()
	{
		return new image_CornellBox(dim);
	}

}