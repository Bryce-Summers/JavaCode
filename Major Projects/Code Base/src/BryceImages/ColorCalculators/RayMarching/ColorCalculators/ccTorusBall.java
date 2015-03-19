package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_torus;

public class ccTorusBall extends PhongLighted3dPicture {

	public ccTorusBall(Dimension tempDim) {
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


		CameraZ 		= new Vector(10,10,10);
		CameraFocus 	= new Vector(0,0,0);
		antiAliasing 	= 3;
		reflectionDepth	= 3;
		CameraDepth = 3;
		
		Geometry g;
		
		g = new geom_torus(new Vector(1,0,0),1.2,.1,new Vector(0,0,1));
		g.setColor(Color.red);
		g.setReflectivity(.4);
		geoms.add(g);
		
		g = new geom_torus(new Vector(0,1,0),1.2,.1,new Vector(1,0,0));
		g.setColor(Color.blue);
		g.setReflectivity(.4);
		geoms.add(g);
		
		g = new geom_torus(new Vector(0,0,1),1.2,.1,new Vector(0,1,0));
		g.setColor(Color.YELLOW);
		g.setReflectivity(.4);
		geoms.add(g);
		
		
		
	}

	@Override
	public boolean withinBounds(Vector z) {
		
		return CameraFocus.sub(z).mag() < 200;
	}
}