package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import static BryceImages.ColorCalculators.RayMarching.BryceMath.abs;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.degToRad;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.lineAngle;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.min;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sin;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class ccShamrock extends ColorCalculator {

	public ccShamrock(Dimension tempDim) {
		super(tempDim);
		antiAliasing = 3;
	}


	double rhoInc;
	
	public Color getColor(double x, double y) {
		
		//use these modifiers if the renderer messes up on some horizontal bands.
		//room_width  = 7200;
		//room_height = 7200;
		//y+=5886;
		
		
		x-=room_width/2;
		y-= room_height/2;

		int factor = (int) min(room_width,room_height);
		
		double xinc=x/factor;
		double yinc=y/factor;
		double zinc=.7;
		rhoInc=1;
		
		double temp = distance(0,0,0,xinc,yinc,zinc);
		xinc/=temp;
		yinc/=temp;
		zinc/=temp;
		
		//rhoIncMin*=max(rhoIncMin*distance(xinc,yinc,0,0),.001);
		double z;
		
		/*x=-.65;
		y=.3;
		z=-.8;*/
		
		x = 0;
		y = 0;
		z = -6;
		
				
		
		double prescision = .000001;
		int bound = 6;
		for (;;){
						
			if(abs(x) > bound || abs(y) > bound || abs(z) > bound){break;}//stop and brake out it out of bounds.
			
			rhoInc = 1000;
						
			DE(x,y,z);
			
			if (rhoInc < prescision){

				double e = .00000000001;
				double dx = DE(x+e,y,z)  - DE(x-e,y,z);
				double dy = DE(x,y+e,z)  - DE(x,y-e,z);
				double dz = DE(x,y,z+e)  - DE(x,y,z-e);

				//normalize normal vector
				double dist = distance(0,0,0,dx,dy,dz);
				
				dx/=dist;
				dy/=dist;
				dz/=dist;
				
				//return Color_rgb(128 + (128*dx),128 + (128*dx),128 + (128*dx));
				temp = (50/3.0);
				return Color_hsv(134,100,50+(temp*dx+temp*dy+temp*dz));
								
				
		}
									
				
				x+=xinc*rhoInc;
				y+=yinc*rhoInc;
				z+=zinc*rhoInc;
		}//end of rayMArching loop
		
		
		return Color_hsv(134,0,0,0);
	}
	
	public double DE(double x, double y, double z){//do stuff that sets rhoInc to its maximal step size.
		
		if(z>0){
		rhoInc = 1000;
		return 1000;
		}
		else rhoInc = .1;
		
		sphere(x,y,z,0,0,-.9,1);// a basic sphere
		
		//The clover leaves
		
		if(z<-1.2)
		{
			
			//Left
			double angle = lineAngle((x+2),abs(y),0,0);
			sphere(x,y,z,-2,0,0,(angle/180.0));
			
			//Right
			angle = lineAngle(-(x-2),abs(y),0,0);
			//if(z<-1)
			sphere(x,y,z,2,0,0,(angle/180.0));
		
			//top
			angle = lineAngle((y+2),abs(x),0,0);
			//if(z<-1)
			sphere(y,x,z,-2,0,0,(angle/180.0));
			
			//Bottom
			angle = lineAngle((-y+2),abs(x),0,0);
			//if(z<-1)
			sphere(y,x,z,2,0,0,(angle/180.0));
		}
		
		double angle = lineAngle(x,y,3,3);
		
		if(angle>140&& angle<270)
		torus(x,y,z,3,3,0,3,.1);
		
		return rhoInc;
	}
	
	public void torus(double x, double y, double z, double tx,double ty, double tz,double radius,double thickness){
		
		double angle = lineAngle(x,y,tx,ty);
		angle = degToRad(angle);
		rhoInc = min(distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz)-thickness,rhoInc);
		
	}
	public void sphere(double x, double y, double z, double sx, double sy, double sz, double radius){
		double dist = distance(x,y,z,sx,sy,sz)-radius;
		rhoInc = min(rhoInc,dist);
		
	}

}
