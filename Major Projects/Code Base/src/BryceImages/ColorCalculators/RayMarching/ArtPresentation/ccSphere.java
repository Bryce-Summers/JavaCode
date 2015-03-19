package BryceImages.ColorCalculators.RayMarching.ArtPresentation;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_ball;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plane;

public class ccSphere extends PhongLighted3dPicture
{

	public ccSphere(Dimension tempDim)
	{
		super(tempDim);
	}

	@Override
	public void loadGeoms()
	{

		lights = new Vector[2];
		lights[0] = new Vector(5,5,3,100);
		lights[1] = new Vector(-5,-5,3,100);

		CameraZ 		= new Vector( 0,-30, 20);
		CameraFocus 	= new Vector(0,0,0);
		antiAliasing 	= 3;
		reflectionDepth	= 2;
		CameraDepth = 6;

		geom_plane p1 = new geom_plane(new Vector(0,0,0), new Vector(0,0,1));
		p1.setReflectivity(.5);
		//p1.setColorCalculator(new texture_lines(0,0));
		p1.setColor(Color.white);
		geoms.add(p1);
		
		geom_ball g = new geom_ball(new Vector(0, 0, 0), 99.0);
		g.setColor(Color_hsv(0,0,90));
		//g.setReflectivity(1);
		geoms.add(g);
		
		loadMore();
	}
	
	public void loadMore()
	{
		geom_ball b1 = new geom_ball(new Vector(0,0,1),1);
		b1.setColor(Color_hsv(50, 61, 100));
		b1.setReflectivity(.1);
		geoms.add(b1);
	}
	
	@Override
	public boolean withinBounds(Vector z)
	{
		return z.sub(v(0,0,0)).mag() < 1000;
	}

	@Override
	public Color getColor(Vector z, double light)
	{
		return Color_hsv(0, 0, 100);
	}

	public Vector v(double a, double b, double c)
	{
		return new Vector(a, b, c);
	}
	
}
