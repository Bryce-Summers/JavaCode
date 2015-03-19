package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import static BryceImages.ColorCalculators.RayMarching.BryceMath.v;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_array;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_ball;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_block;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plane;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plate;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_simpleCurve;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_simpleLathe;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_torus;
import BryceImages.ColorCalculators.RayMarching.Textures.texture_stripes;

public class ccGeomTesting extends PhongLighted3dPicture {

	public ccGeomTesting(Dimension tempDim) {
		super(tempDim);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void loadGeoms() {

		lights 	= new Vector[2];
		lights[0] = new Vector(5,5,2,.5);
		lights[1] = new Vector(3,0,20,.5);

		CameraZ 		= new Vector(0,.01,25);
		CameraFocus 	= new Vector(0,0,0);
		antiAliasing 	= 1;
		reflectionDepth	= 1;
		CameraDepth = 3;
		
		Geometry g;
		
		// Torus works
		// Plate works
		// Block works (I think); 
		// Ball works of course!!
		// I believe that simple curve works now as well.
		// Simple lathe works.
		// geom_arrays work, except very slowly.
		
		// First create background plane	
		g = new geom_plane(new Vector(0,0,0), new Vector(0,0,1));
		g.cc = new texture_stripes(0,0);
		//geoms.add(g);
		g.setReflectivity(.3);
		
		// Outside ball works!!
		g = new geom_ball(new Vector(0,0,0), 50);
		//geoms.add(g);
		//g.setReflectivity(.7);
			
		g = new geom_plate(new Vector(0,0,0), 2,.1 ,new Vector(0,1,-1));
		//geoms.add(g);
		
		g = new geom_torus(new Vector(0,0,0), 2,.1 ,new Vector(0,1,-1));
		//geoms.add(g);
		
		g = new geom_simpleCurve(new Vector(0,0,0), 2, .1 ,new Vector(0,1,-1),190,400);
		//geoms.add(g);
		
		
		g = new geom_block(new Vector(0,0,0),new Vector(2,1,1), new Vector(-1,2,1),.1);
		//geoms.add(g);

		
		g = new geom_simpleLathe(new Vector(0,0,0), 2.1, 2,.10,new Vector(0,0,1), 0,270);
		//geoms.add(g);
		
		double size = .3;
		
		g = new geom_block(new Vector(-size, -size,0),new Vector(size, size,0), v(0,0,1), .1);
		//geoms.add(g);
		
		g = new geom_array(v(0, 0, 0), size*2+.2, g);
		geoms.add(g);

	}
	@Override
	public boolean withinBounds(Vector z)
	{		
		return z.mag() < 100;
	}


	@Override
	public Color getColor(Vector z, double light)
	{
		return Color_hsv(0,0,0);
	}

}
