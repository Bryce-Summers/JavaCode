package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_cylinder;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plane;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plate;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_simpleCurve;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_simpleLathe;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_spaceCurve;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_torus;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_tube;

public class ccTrumpet extends PhongLighted3dPicture {

	public ccTrumpet(Dimension tempDim)
	{
		super(tempDim);
		//room_width = 6000;
		//room_height= 6000;		
	}

	@Override
	public Color getColor(Vector z, double light)
	{
		return null;
	}

	@Override
	public void loadGeoms()
	{
		lights = new Vector[3];
		lights[0] = new Vector(5,5,3,.33);
		lights[1] = new Vector(-10,30,2,.33);
		lights[2] = new Vector(20,-16,100,.33);
		

		up = v(0,-1,0);
		
		CameraZ 		= new Vector(20,30,100);
		CameraFocus 	= new Vector(20,0,40);
		antiAliasing 	= 1;
		reflectionDepth	= 3;
		CameraDepth = 1.1;// HD :1.9;
		
		Color c = Color_hsv(55,100,100);// Body

		Geometry g;
		
		
		// The mouthpiece
		g = new geom_simpleLathe(new Vector(-20.5,0,0), 1, .5,.10,new Vector(-1,0,0), 0, 90);
		geoms.add(g);
		g.setColor(c);
		g.setReflectivity(.5);
		
		// The lesser radius part of the mouth piece.
		g = new geom_tube(v(-20.5,0,0), v(-21.5,0,0) ,1.5,.1);
		geoms.add(g);
		g.setColor(c);
		g.setReflectivity(.5);
		
		// The large radius end of the mouth piece
		g = new geom_tube(v(-21.5,0,0), v(-22.5,0,0) ,2,.1);
		geoms.add(g);
		g.setColor(c);
		g.setReflectivity(.5);
		
		g = new geom_plate(v(-21.5,0,0),2,.1, v(1,0,0));
		geoms.add(g);
		g.setColor(c);
		g.setReflectivity(.5);
		// end of mouthpiece.
		
		// The first tube
		g = new geom_cylinder(v(-20,0,0), v(35,0,0), 1);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// Curl 1
			
		g = new geom_simpleCurve(v(35,-5,0),5,1,v(0,0,-1),-90,90);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// decorative curl 1.
		g = new geom_simpleCurve(v(19,2,0),1,.3,v(0,0,-1),90,270);
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		// bangle on curl 1
		g = new geom_torus(v(38,-11,0), .5,.4, v(0,0,1));
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);

		for(int x = 7; x < 16; x+=4)
		{
			g = new geom_cylinder(v(x,-13,-2.5),v(x,.5,-2.5), 1.5);
			geoms.add(g);
			g = new geom_plate(v(x,1.5,-2.5),1,.5,v(0,1,0));
			geoms.add(g);
			g.setColor(Color.yellow);
			g.setReflectivity(.5);
			
			g = new geom_cylinder(v(x,1.5,-2.5), v(x,4.5,-2.5), .4);
			geoms.add(g);
			
			g = new geom_plate(v(x,4.5,-2.5),.7,.4,v(0,1,0));
			geoms.add(g);
			g.setColor(Color.yellow);
			g.setReflectivity(.5);
			
			g = new geom_torus(v(x,-5,-2.5), 1.5,.1,v(0,1,0));
			geoms.add(g);
			g.setColor(Color.yellow);
			g.setReflectivity(.5);
			
			g = new geom_torus(v(x,-12,-2.5), 1.5,.1,v(0,1,0));
			geoms.add(g);
			g.setColor(Color.yellow);
			g.setReflectivity(.5);
			
		}
		
		g = new geom_cylinder(v(35,-10,0), v(17,-10,0),1);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// curl into valve 3.
		g = new geom_simpleCurve(v(16, -10,-1),1,1,v(0,1,0),270,360);
		geoms.add(g);
		//g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		// The right sliding valve.
		g = new geom_cylinder(v(16,-8,-3), v(30,-8,-3),1);
		geoms.add(g);
		g.setReflectivity(.5);
		g = new geom_cylinder(v(16,-12,-3), v(30,-12,-3),1);
		geoms.add(g);
		g.setReflectivity(.5);
		
		
		// curl 2.
		g = new geom_simpleCurve(v(30,-10,-3),2,1,v(0,0,-1),-90,90);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// Decorative torus 1.
		g = new geom_torus(v(22,-5.5,-3), 1.5,.2,v(0,0,1));
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		g = new geom_cylinder(v(27, -8, -3), v(27, -12, -3),.3);
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		// cross dowels between the valves.
		int y = -6;
		g = new geom_cylinder(v(7, y, -2.5), v(16, y, -2.5),.3);
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		y = -9;
		g = new geom_cylinder(v(7, y, -2.5), v(16, y, -2.5),.8);
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		y = -11;
		g = new geom_cylinder(v(7, y, -2.5), v(16, y, -2.5),.4);
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		
		// The out cropping for the hand I presume.
		g = new geom_cylinder(v(11, -8, -2.5), v(7, -8, 1.5),1);
		geoms.add(g);
		//g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		// The out cropping for the hand I presume.
		g = new geom_cylinder(v(11, -12, -2.5), v(7, -12, 1.5),1);
		geoms.add(g);
		//g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		
		// curl 2.
		g = new geom_simpleCurve(v(7, -10, 1.5),2,1,v(1,0,1),-90,90);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// The left slider valve.
		y = -8;
		g = new geom_cylinder(v(-3, y, -.5), v(7, y, -2.5),1);
		geoms.add(g);
		g.setReflectivity(.5);
		
		y = -12;
		g = new geom_cylinder(v(-3, y, -.5), v(7, y, -2.5),1);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// left sliding valve curl.
		g = new geom_simpleCurve(v(-3, -10, -.5),2,1,v(0,0,-1),90,270);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// Decorative torus 2. (on top of left sliding valve.
		g = new geom_torus(v(0,-5.5,-.5), 1.5,.2,v(0,0,1));
		geoms.add(g);
		g.setColor(Color.yellow);
		g.setReflectivity(.5);
		
		
		g = new geom_cylinder(v(-10, -10, -2.5), v(7, -10, -2.5),1);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// left sliding valve curl.
		g = new geom_simpleCurve(v(-10, -5, -2.5),5,1,v(0,0,-1),90,270);
		geoms.add(g);
		g.setReflectivity(.5);
		
		g = new geom_cylinder(v(-10, 0, -2.5), v(45, 0, -2.5),1);
		geoms.add(g);
		g.setReflectivity(.5);
		
		// Bell torus
		g = new geom_torus(v(50,0,-2.5), 3.2,.2,v(1,0,0));
		//geoms.add(g);
		g.setReflectivity(.5);
		
		// The bell
		g = new geom_simpleLathe(new Vector(45,0,-2.5), 8, 6,.10,new Vector(1,0,0), 200, 270);
		geoms.add(g);
		g.setReflectivity(.5);
		
		g = new bell();
		geoms.add(g);
		g.setReflectivity(.5);


		
		
		// Fancy plane to showcase reflections.
		g = new geom_plane(v(0,-17,0), v(0,1,0));
		g.setReflectivity(.7);
		g.setColor(Color_hsv(0,0,100));
		geoms.add(g);
		
	}

	public Vector v(double x, double y, double z)
	{
		return new Vector(x, y, z);
	}

	@Override
	public boolean withinBounds(Vector z) {
		
		return z.mag() < 200;
	}

	private class bell extends geom_spaceCurve
	{
		public bell()
		{
			super();
			// endpoints of parametric curve.
			// represents the bell of this trumpet.
			i1 = -10;
			i2 = 48;
			
		}
		public Vector func1(double i)
		{
			return new Vector(i,0,-2.5);
		}
		public double radius(double i)
		{
			double out =  max(1,(i)*.05);

			
			return out;
			//return 1/(15.5-i);
		}
	}
	
}
