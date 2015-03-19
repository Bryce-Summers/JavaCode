package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

// cc BioHazard
// Written by Bryce Summers originally for the old render as a demonstration of effectively isolating Distance estimation.
// Inspired by Robert Campion. This is still used as an example of a manual implementation of my 3dPicture engine.
// Originally written some time in the first quarter of 2012.
import static BryceImages.ColorCalculators.RayMarching.BryceMath.abs;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.degToRad;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.lineAngle;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.min;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.pi;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sin;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;

public class ccBioHazard extends PhongLighted3dPicture {

	public ccBioHazard(Dimension tempDim) {
		super(tempDim);
		antiAliasing = 3;
	}

	@Override
	public void loadGeoms() {
		
		//Ensure my 3d rendering settings are correct for this Color Calculator.
		CameraZ = new Vector( 0, .01,15);
		CameraDepth = 3;
		
		lights[0] = new Vector(5,5,5);
		lights[1] = new Vector(-5,-5,-5);
		reflectionDepth = 0;
	}

	double rhoInc;
	double xinc,yinc, zinc;
	public Color getColor(double x, double y) {
				
		x-=room_width/2;
		y-= room_height/2;

		double angle = (lineAngle(x,y,0,0));
		angle=angle-( (angle-45)%120)+45;
		
		angle = degToRad(angle);
		
		return super.getColor(x+room_width/2,y+=room_height/2);
	}
	
	public void torus(double x, double y, double z, double tx,double ty, double tz,double radius,double thickness){
		
		double angle = lineAngle(x,y,tx,ty);
		angle = degToRad(angle);
		rhoInc = min(distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz)-thickness,rhoInc);
		
	}
	public void sphere(double x, double y, double z, double sx, double sy, double sz, double radius){
		double dist = abs(radius-distance(x,y,z,sx,sy,sz));
		rhoInc = min(rhoInc,dist);
	}


	public double DE(Vector v){
		
		double x,y,z;
		x = v.x;
		y = v.y;
		z = v.z;
		
		rhoInc = 1000;
		
		double r1=1.25;
		double r2=r1-.75;
		
		//Top cut circle
		
		double dist1 = distance(x,y,z, 0,-r1,0);
		double dist2 = distance(x,y,z,0,-r2,0);
		//thickness = ;
		torus(x,y,z,0,-r1,0,1,.3*dist1-.177*dist2);
		
		//the bottom right cut circle?
		double angle = pi/6;
		dist1 = distance(x,y,z, r1*cos(angle),r1*sin(angle),0);
		dist2 = distance(x,y,z, r2*cos(angle),r2*sin(angle),0);
		//thickness = ;
		torus(x,y,z,r1*cos(angle),r1*sin(angle),0,1,.3*dist1-.177*dist2);
		
		//THe bottom left cut circle?
		angle = 2*pi/3+pi/6;
		dist1 = distance(x,y,z, r1*cos(angle),r1*sin(angle),0);
		dist2 = distance(x,y,z, r2*cos(angle),r2*sin(angle),0);
		//thickness = ;
		torus(x,y,z,r1*cos(angle),r1*sin(angle),0,1,.3*dist1-.177*dist2);
				
		torus(x,y,z,0,0,0,2.3,.05);//The outer ring
		
		if((lineAngle(x,y,0,0)+60)%(120)<60){//The cut up inner ring
		torus(x,y,z,0,0,0,1.3,.05);
		}
		//torus(x,y,z,0,0,-.2,.2,.05);
		
		return rhoInc;
		
	}

	@Override
	public Color getColor(Vector z, double light) {
		return Color_hsv(0,100,light*100);
	}

	@Override
	public boolean withinBounds(Vector z) {

		return z.mag() < 16;
	}

}
