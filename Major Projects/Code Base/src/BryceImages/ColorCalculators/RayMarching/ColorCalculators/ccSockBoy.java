package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_ball;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_cylinder;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plane;
import BryceImages.ColorCalculators.RayMarching.Textures.texture_stripes;

public class ccSockBoy extends PhongLighted3dPicture
{

	public ccSockBoy(Dimension tempDim)
	{
		super(tempDim);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Color getColor(Vector z, double light)
	{
		return null;
	}

	@Override
	public void loadGeoms()
	{
		lights = new Vector[1];
		lights[0] = new Vector(25,25,25,1);


		CameraZ 		= new Vector(0,100,0);
		CameraFocus 	= new Vector(0,0,0);
		antiAliasing 	= 1;
		reflectionDepth	= 3;
		CameraDepth = 1;
		
		Color cb = Color_hsv(0,100,100);
		
		Geometry g;
		
		Vector head = new Vector(0,-2,10);
		double headRadius	= 4;
		double eyeRadius	= .5;
		double mouthRadius	= .4;
		double jointRadius	= headRadius / 5;
		
		g = new geom_ball(head,headRadius);
		g.setColor(cb);
		geoms.add(g);
		
		// Form the eyes.
		Vector eye = new Vector(.5,1,.4).norm();
		g = new geom_ball(head.add(eye.mult(headRadius + eyeRadius)), eyeRadius);
		geoms.add(g);
		
		eye.x *= -1;
		g = new geom_ball(head.add(eye.mult(headRadius + eyeRadius)), eyeRadius);
		geoms.add(g);
		
		// Form the Mouth.
		Vector mouth1 = head.add(new Vector(-.5 ,1,-.4).norm().mult(headRadius + mouthRadius));
		Vector mouth2 = head.add(new Vector(-.25,1,-.6).norm().mult(headRadius + mouthRadius));
		Vector mouth3 = head.add(new Vector( .25,1,-.6).norm().mult(headRadius + mouthRadius));
		Vector mouth4  = head.add(new Vector( .5 ,1,-.4).norm().mult(headRadius + mouthRadius));
		
		g = new geom_cylinder(mouth1, mouth2, mouthRadius);
		geoms.add(g);
		g = new geom_cylinder(mouth2, mouth3, mouthRadius);
		geoms.add(g);
		g = new geom_cylinder(mouth3, mouth4, mouthRadius);
		geoms.add(g);
		
		// First create some helper vectors.
		Vector down  = new Vector(0,0,-1);
		Vector left  = new Vector(-1,0,0);
		Vector right = new Vector( 1,0,0);
		
		// Now start working down to the chest joint.
		Vector chest = head.add(down.mult(headRadius*2));
		
		// Create the chest and connect it with a joint to the head.
		g = new geom_ball(chest,headRadius/2);
		geoms.add(g);
		g = new geom_cylinder(head, chest, jointRadius);
		geoms.add(g);
		
		// Now form the shoulders
		Vector shoulder1, shoulder2;
		shoulder1 = chest.add(left .mult(headRadius*2));
		shoulder2 = chest.add(right.mult(headRadius*2));
		
		// Connect a left shoulder to the chest. 
		g = new geom_ball(shoulder1,headRadius/3);
		geoms.add(g);
		g = new geom_cylinder(shoulder1, chest, jointRadius);
		geoms.add(g);
		
		// Connect a right shoulder to the chest. 
		g = new geom_ball(shoulder2,headRadius/3);
		geoms.add(g);
		g = new geom_cylinder(shoulder2, chest, jointRadius);
		geoms.add(g);
		
		//--Form the bottom
		Vector glutiousMaximus = chest.add(down.mult(headRadius * 2));

		// Connect a right shoulder to the chest. 
		g = new geom_ball(glutiousMaximus, headRadius/2);
		geoms.add(g);
		g = new geom_cylinder(glutiousMaximus, chest, jointRadius);
		geoms.add(g);
		
		// Form the legs.
		Vector leg1,leg2;
		leg1 = glutiousMaximus.add(down.mult(headRadius * 2)).add(left .mult(headRadius));
		leg2 = glutiousMaximus.add(down.mult(headRadius * 2)).add(right.mult(headRadius));
		
		// Connect a left leg to the glutiousMaximus. 
		g = new geom_ball(leg1,headRadius/3);
		g.setColor(Color_hsv(0,0,30));
		geoms.add(g);
		g = new geom_cylinder(leg1,glutiousMaximus, jointRadius);
		g.setColor(Color.blue);
		geoms.add(g);
		
		// Connect a right leg to the glutiousMaximus. 
		g = new geom_ball(leg2,headRadius/3);
		g.setColor(Color_hsv(0,0,30));
		geoms.add(g);
		g = new geom_cylinder(leg2, glutiousMaximus, jointRadius);
		g.setColor(Color.blue);
		geoms.add(g);
		
		Vector sock = glutiousMaximus.add(leg1.mult(2)).div(3);
		g = new geom_cylinder(leg1, sock, jointRadius+.01);
		g.setColor(Color.white);
		geoms.add(g);
		
		
		// Now create an aestetically pleasing backgound;
		
		g = new geom_plane(leg1.add(down.mult(headRadius/3)).add(new Vector(0,-5,0)),new Vector(0,0,1));
		g.cc =  new texture_stripes(0,0);
		g.myReflect = .7;
		geoms.add(g);
		
		g = new geom_plane(leg1.add(down.mult(headRadius/3)),new Vector(0,1,0));
		g.cc =  new texture_stripes(0,0);
		g.myReflect = .7;
		geoms.add(g);
	}

	@Override
	public boolean withinBounds(Vector z) {
		
		return CameraFocus.sub(z).mag() < 200;
	}
}