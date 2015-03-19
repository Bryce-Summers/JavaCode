package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import static BryceImages.ColorCalculators.RayMarching.BryceMath.abs;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.degToRad;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.lineAngle;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sin;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.ccPerlinNoise;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_ball;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plane;

public class ccBall extends PhongLighted3dPicture {

	public ccBall(Dimension tempDim)
	{
		super(tempDim);
	}

	@Override
	public void loadGeoms() {

		lights = new Vector[2];
		lights[0] = new Vector(5,5,3,50);
		lights[1] = new Vector(-5,-5,3,50);

		CameraZ 		= new Vector( 0,-30, 2);
		CameraFocus 	= new Vector(0,0,0);
		antiAliasing 	= 3;
		reflectionDepth	= 3;
		CameraDepth = 3;
		
		for(int i = -3; i <= 3; i += 3)
		{
			geom_ball b1 = new geom_ball(new Vector(i,0,1),1);
			b1.setColor(Color_hsv(180,100,100));
			b1.setReflectivity(.5);
			geoms.add( b1);
		}		
		
		geom_plane p1 = new geom_plane(new Vector(0,0,0), new Vector(0,0,1));
		p1.setReflectivity(.5);
		//p1.setColorCalculator(new texture_stripes(0,0));
		p1.setColorCalculator(new ccPerlinNoise(new Dimension(30,30), 0, .5, 7));
		geoms.add(p1);
	}
	
	/*
	public double DE(Vector v) {
	
		double x,y,z;
		x = v.x;
		y = v.y;
		z = v.z;
		
		// Original Balls
		double dist = 	abs(distance(0,0,1,x,y,z)-1);
		//dist = min(dist,abs(distance( 3,0,1,x,y,z)-1));
		//dist = min(dist,abs(distance(-3,0,1,x,y,z)-1));
		
		// Some neat toruses for good measure.
		dist = min(dist,torus(x,y,z,0,0,.25,1.5,.5));
		dist = min(dist,torus(x,y,z,0,0,1.75,1.5,.5));
		
		// The bottom plane.
		dist = min(dist,abs(z));
				
		return dist;
		//* /
	}
	*/
	public double torus(double x, double y, double z, double tx,double ty, double tz,double radius,double thickness){
		
		double angle = lineAngle(x,y,tx,ty);
		angle = degToRad(angle);
		return abs(distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz)-thickness);
		
	}

	@Override
	public Color getColor(Vector z, double light) {
		z = z.mult(1);
		z.x+=100;
		
		if(z.x%2<1)
		return Color_hsv(z.y,100,100*light);
				
		return Color_hsv(z.y+180,100,100*light);
	}

	@Override
	public boolean withinBounds(Vector z)
	{		
		return z.mag() < 31;
	}

}
