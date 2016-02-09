package Project;

import java.awt.Graphics;

import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.UBA;
import Project.HalfEdgeMesh.HalfEdgeMesh;
import Project.Operations.HalfEdgeMeshDrawer;

public class mesh_main extends abstract_image_main_class
{
	public static void main(String[] args)
	{
		new mesh_main();
	}
	
	public mesh_main()
	{
		// Starts up the window and populates the public fields.
		startNormal();		
		
		int vert_radius = 20;
		
		UBA<Vector> points = generateRandomScreenPoints(300, vert_radius);
		
		HalfEdgeMesh mesh = new HalfEdgeMesh(true, points.toArray());
		
		System.out.println("Mesh_main: Done Initializing the HalfEdge");
		
		HalfEdgeMeshDrawer draw = new HalfEdgeMeshDrawer(mesh);
		draw.vert_radius = vert_radius;
		
		Graphics g = image.getGraphics();		
		
		System.out.println("Mesh_main: Drawing Geometetry.");
		
		draw.drawFaces(g);
		draw.drawEdges(g);
		draw.drawVertices(g);
		
		/*
		System.out.println("Drawing 2");
		mesh.makeDelauney();
		
		draw.offset_x = image.getWidth()/2;
		
		draw.drawFaces(g);
		draw.drawEdges(g);
		draw.drawVertices(g);
		*/
	}
	
	// Generates a set of random points on the screen.
	private UBA<Vector> generateRandomScreenPoints(int num, int border)
	{
		UBA<Vector> points = new UBA<Vector>(num);
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		// FIXME: Remove these lines.
		//w /= 3;
		//h /= 3;
		
		w -= border*2;
		h -= border*2;
		
		for(int i = 0; i < num; i++)
		{
			points.add(new Vector(Math.random()*w + border, Math.random()*h + border, 0));
		}
		
		return points;
	}
	
	
}
