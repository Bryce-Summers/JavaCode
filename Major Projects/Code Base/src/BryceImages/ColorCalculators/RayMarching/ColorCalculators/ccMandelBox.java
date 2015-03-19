package BryceImages.ColorCalculators.RayMarching.ColorCalculators;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.degToRad;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sqr;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class ccMandelBox extends ColorCalculator
{

	double scale = 2;
	public ccMandelBox(Dimension tempDim)
	{
		super(tempDim);			
	}

	@Override
	public Color getColor(double x, double y)
	{
		//formula for the color of any pixel 
		
		double z=0;
		
		x-= room_width/2;
		y-= room_height/2;
	
		x/=1000;
		y/=1000;
		
		double zinc=.01;
		double xinc=x/room_width*zinc*2;
		double yinc=y/room_height*zinc*2;
		x=0;
		y=0; 
		for (z=-20;z<3.5;z+=zinc){
			x+=xinc;
			y+=yinc;
									
			double[] v = new double[3];
			v[0]=x;
			v[1]=y;
			v[2]=z;
			double dist = distance(v[0],v[1],v[2],0,0,0);
			
			//if(dist>4){continue;}
			
		    int max_value = 50;
		    int i=0;
			for(i=0;i<max_value;i++){
		    for (int axis=0; axis<v.length;axis++){
				if (v[axis]>1) {v[axis] = 2-v[axis];}
				else if (v[axis]<-1) {v[axis] = -2-v[axis];}
				}//end of for every axis
				
		    	dist = distance(v[0],v[1],v[2],0,0,0);
				if (dist < 0.5) {
					for(int k=0;k<v.length;k++){
						v[k]*=4;
					}//end of for loop
					}//end of magnitude
				else if (dist < 1) {
					double distS = sqr(dist);
					for(int k=0;k<v.length;k++){
						v[k]/=distS;
					}//end of for loop
				}
				for(int k=0;k<v.length;k++){
					v[k]*=scale;
				}//end of for loop
				v[0]+=x;
				v[1]+=y;
				v[2]+=z;
				dist = distance(v[0],v[1],v[2],0,0,0);
				
				if (dist > 4) break;
		}
		if(i==max_value)
		{
			return Color_hsv(z*100,100,50+50*cos(degToRad(distance(x,y,z,0,0,0))+x+.5*y));}//if in set
		else{continue;}
		}//ray marching loop
		
		
		return Color_hsv(0,0,0);
	}

}
