package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.degToRad;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sin;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.v;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plane;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_simpleCurve;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_torus;

public class ccPineapple extends PhongLighted3dPicture
{

	public ccPineapple(Dimension tempDim)
	{
		
		super(tempDim);

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
		lights[2] = new Vector(20,-16,-20,.33);
		

		up = v(0,0,1);
		
		CameraZ 		= new Vector(0, 45, 0);
		CameraFocus 	= new Vector(0, 0,0);
		antiAliasing 	= 3;
		reflectionDepth	= 1;
		CameraDepth = 3;
		
		Color c = Color_hsv(55,100,100);// Body

		Geometry g;
		
		
		// The pine Apple
		g = new pineAppleTorus(v(0,0,0), 1,2,v(1,0,0));
		geoms.add(g);
		g.setColor(c);
		g.setReflectivity(.1);

		for(int j = 1; j <=3 ; j++)
		for(double i = j/2 %1; i < 10; i++)
		{
			g = new geom_simpleCurve(v(j*cos(degToRad(i*36)),j*sin(degToRad(i*36)),- 2),j,.3,v(-sin(degToRad(i*36)),cos(degToRad(i*36)),0),180,300);
			geoms.add(g);
			g.setColor(Color_hsv(118,100,72));
		}
		
		g = new geom_plane(v(0,0,3.2),v(0,1,-1));
		geoms.add(g);
		g.setReflectivity(.5);
		
	}

	@Override
	public boolean withinBounds(Vector z)
	{
		// TODO Auto-generated method stub
		return z.mag() < 200;
	}

	private class pineAppleTorus extends geom_torus
	{

		public pineAppleTorus(Vector z, double r, double thickness,
				Vector normal)
		{
			super(z, r, thickness, normal);
		}
		
		public Color getColor(Vector z,double finalLighting)// Input a geometric coordinate, and an exterior lighting value.
		{
			double x = getComponent(z,u);
			double y = getComponent(z,v); // Coloring from a color calculator.

			x = x%.2;
			y = y%.2;
			
			double dist = distance(0,0,x,y);
			Color c;
			if( dist < .2 && dist > .15)
				c = Color.gray;
			else
			{
				c = Color_hsv(55,100,100);
			}
			
			// Return the appropriate color with an intensity correct for its lighting.
			return weightedAverageColor(Color.black,c,finalLighting);
		}
		
	}
}
