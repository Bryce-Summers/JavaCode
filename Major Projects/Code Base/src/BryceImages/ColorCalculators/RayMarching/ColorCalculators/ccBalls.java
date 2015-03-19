package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import static BryceImages.ColorCalculators.RayMarching.BryceMath.abs;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;

public class ccBalls extends PhongLighted3dPicture {

	public ccBalls(Dimension tempDim) {
		super(tempDim);
	}
	
	@Override
	public void loadGeoms()
	{

		antiAliasing = 1;
		
		lights = new Vector[1];
		lights[0] = new Vector(5,5,5);
		//lights[1] = new Vector(-5,-5,5);
		CameraZ = new Vector( 0,-10, 5);
		CameraFocus = new Vector (0,0,0);
		defaultRefelectivity = .5;
		CameraDepth = 3;
	}
	@Override
	public double DE(Vector v) {
		double x,y,z;
		x = v.x;
		y = v.y;
		z = v.z;
		
		

		x+=2000;
		y+=2000;
		
		x = x%.5;
		y = y%.5;
		
		return abs(distance(0,0,0,x-.25,y-.25,z)-.25);
		/*
		x = x%2;
		y = y%2;
		
		return abs(distance(0,0,0,x-1,y-1,z)-1);
		*/
	}

	@Override
	public Color getColor(Vector z, double light) {

		//Vector num_columns = computeNormal(z,.0000001);
		return Color_hsv(0,100,light*100);
		//return Color_hsv(round(z.x+z.y)*20,100,light*100);
	}

	@Override
	public boolean withinBounds(Vector z) {
		return z.mag()<41;
	}



}
