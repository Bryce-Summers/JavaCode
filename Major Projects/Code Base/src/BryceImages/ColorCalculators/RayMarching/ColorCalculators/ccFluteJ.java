package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import static BryceImages.ColorCalculators.RayMarching.BryceMath.arcTan;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.cos;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.degToRad;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.distance;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.lineAngle;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.max;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.min;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.pi;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.radToDeg;
import static BryceImages.ColorCalculators.RayMarching.BryceMath.sin;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;

public class ccFluteJ extends ColorCalculator {

	public ccFluteJ(Dimension dim) {
		super(dim);
		// TODO Auto-generated constructor stub
	}

	public ccFluteJ(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Color getColor(double x, double y) {
		//use these modifiers if the renderer messes up on some horizontal bands.
		//room_width  = 1640*2;
		//room_height = 600;
		//y+=5886;
		
		double z=-5;
		
		x-=room_width/2;
		y-= room_height/2;
	
		double rhoIncMin=.001;// <------ - - - - - - -
		double rhoIncMax=0;

		int factor = (int) max(room_width,room_height)*400/1600;
		
		double xinc=x/factor;
		double yinc=y/factor;
		double zinc=.7;
		double rhoInc=1;
		
		double temp = distance(0,0,0,xinc,yinc,zinc);
		xinc/=temp;
		yinc/=temp;
		zinc/=temp;
						
		//rhoIncMin*=max(rhoIncMin*distance(xinc,yinc,0,0),.001);
		x=0;
		y=0;
		
		for (z=-2.5;z<2;z+=zinc*rhoInc){

			rhoIncMax=1000;//some large number
			
			//if(y<-.6||y>1.3||abs(x)>2||z>.3)
				//return Color_hsv(0,0,0);
			
			
			//the main tube==================================================================
			double dist  = distance(0,0,z,y);
			double angle;
			double width = 6;
			if( dist< .3 && x<width&&x>-width){//a bell that extends in x [-.5,1] 
				angle = arcTan(z/y);
				angle=radToDeg(angle);
				if(y<0){angle+=180;}
				return Color_hsv(angle,100,100);
			}
			if(x<width&&x>-width){
				rhoIncMax=min(rhoIncMax,dist - .3    ); 
			}

			//the lip plate ==================================================================
			dist  = distance(-.1,0,z,y);
			// angle;
			
			if( dist< .3 && x<-4.7&&x>-5.2){//a bell that extends in x [-.5,1] 
				angle = arcTan((z+.1)/y);
				angle=radToDeg(angle);
				if(y<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(x>-5.5){
				rhoIncMax=min(rhoIncMax,dist - .3    ); 
			}
			
			//the mouth hole==================================================================
			dist  = distance(-.2,0,-4.95,z,y,x);
			// angle;
			
			if( dist< .22 ) {
				return Color_hsv(0,100,0);
			}
			rhoIncMax=min(rhoIncMax,dist - .22    ); 
						

			//the rivet 1 ==================================================================
			dist  = distance(0,0,z,y);
			// angle;
			
			if( dist< .31 && x<-3.5&&x>-3.51){//a bell that extends in x [-.5,1] 
				angle = arcTan(z/y);
				angle=radToDeg(angle);
				if(y<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(x>-3.55){
				rhoIncMax=min(rhoIncMax,dist - .31    ); 
			}

			
			//the rivet 2 ==================================================================
			dist  = distance(0,0,z,y);
			// angle;
			
			if( dist< .31 && x<-3&&x>-3.01){//a bell that extends in x [-.5,1] 
				angle = arcTan(z/y);
				angle=radToDeg(angle);
				if(y<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(x>-3.05){
				rhoIncMax=min(rhoIncMax,dist - .31    ); 
			}
			
			//the body Cylinder 2 ==================================================================
			dist  = distance(-.4,.3,z,y);
			// angle;
			
			if( dist< .1 && x<2.9&&x>-2){//a bell that extends in x [-.5,1] 
				angle = arcTan((z+.4)/(y-.3));
				angle=radToDeg(angle);
				if((y-.3)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(x>-2&&x<2.9){
				rhoIncMax=min(rhoIncMax,dist - .1    ); 
			}
			
			//The connection from body cylinder 2 to the Foot Cylinder
			
			dist  = distance(-.4,2.9,z,x);
			// angle;
			
			if( dist< .04 && y<.3&&y>-.3){//a bell that extends in x [-.5,1] 
				angle = arcTan((z+.4)/(x-2.9));
				angle=radToDeg(angle);
				if((x-2.9)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>-.3){
				rhoIncMax=min(rhoIncMax,dist - .04    ); 
			}
			
			
			//the Foot Cylinder ==================================================================
			dist  = distance(-.4,-.3,z,y);
			// angle;
			
			if( dist< .1 && x<5&&x>2.9){//a bell that extends in x [-.5,1] 
				angle = arcTan((z+.4)/(y+.3));
				angle=radToDeg(angle);
				if((y+.3)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(x<5){
				rhoIncMax=min(rhoIncMax,dist - .1    ); 
			}
			
			
			
			// -------------------------------------------------------------------------------------------------------------------------
			
			
			
			
			
			//Keys
			
			
			if(x<-2.3){
				rhoInc=max(rhoIncMin,rhoIncMax);//this is the part that tells the loops to increment based on the maximum value!
				x+=xinc*rhoInc;
				y+=yinc*rhoInc;
				continue;
			}
				
			
			
			
			//---------------------------------------------------------------------------------------------------------------------------
			
			
			
	//Key 1
			double tx = -2;
			double ty = -.2;
			double tz = -.4;
			double radius=0.1;
			double thickness = .025;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			double phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .02 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .02    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
	
			
			
	//Key 2
			tx = -1.1;
			ty = -.2;
			tz = -.4;
			radius=0.1;
			thickness = .025;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .02 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .02    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
	//Key 3
			tx = -.65;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
			
			
			
	//Key 4
			tx = -.2;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,00);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
			
			
			
	//Key 5
			tx = .25;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,00);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
			
			
			
			
			
	//Key 6
			tx =  .7;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 	
			
			
			
			
			
			
	//Key 7
			tx = 1.15;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
	//Key 8
			tx = 1.6;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,00);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
			
			
	//Key 9
			tx = 2.05;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,00);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
	//Key 10
			tx = 2.5;
			ty = -.2;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<.3&&y>ty){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<.3&&y>ty){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,00);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
			
	//Key 11
			tx = 3.5;
			ty = .1;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<ty&&y>-.3){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<ty&&y>-.3){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
			
			
	//Key 12
			tx = 4.2;
			ty = .1;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<ty&&y>-.3){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<ty&&y>-.3){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			
			
			
	//Key 13
			tx = 4.9;
			ty = .1;
			tz = -.4;
			radius=0.175;
			thickness = .05;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness ){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//the connector  ==================================================================
			dist  = distance(tz,tx,z,x);
			// angle;
			
			if( dist< .049 && y<ty&&y>-.3){//a bell that extends in x [-.5,1] 
				angle = arcTan((z-tz)/(x-tx));
				angle=radToDeg(angle);
				if((x-tx)<0){angle+=180;}
				return Color_hsv(100+angle,100,100);
			}
			if(y<ty&&y>-.3){
				rhoIncMax=min(rhoIncMax,dist - .05    ); 
			}
			
			//a key filling ==================================================================
			dist  = distance(tx,ty,tz,x,y,z);
			// angle;
			
			if( dist< radius-thickness &&z>tz-thickness-.01) {
				return Color_hsv(100+500*((tx-x)+(ty-y)+(tz-z)),50,100);
			}
			rhoIncMax=min(rhoIncMax,dist - radius+thickness    ); 
			
			
			
			//thumb rest
			tx = .21;
			ty = -.2;
			tz = -.2;
			radius=0.4;
			thickness = .06;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness&&angle<pi/2 ){
				return Color_hsv(radToDeg(phi)+100,100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			//thumb rest part 2
			tx = .21;
			ty = -.6;
			tz = -.2;
			radius=0.06;
			thickness = .06;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness){
				return Color_hsv(radToDeg(phi)+100,100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			
			
			//another thumb rest part 1
			tx = -1.2;
			ty = 1.5;
			tz = -.2;
			radius=1.2;
			thickness = .1;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness&&angle<3*pi/4&&angle>pi/2){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			
			
			//another thumb rest part 2
			tx = -2;
			ty = .5;
			tz = -.2;
			radius=.1;
			thickness = .1;
			//key1
			angle = lineAngle(x,y,tx,ty);
			angle = degToRad(angle);
			//angle+= pi/2;
			//System.out.println(angle);
			dist = distance(x,y,z,radius*cos(angle)+tx,-radius*sin(angle)+ty,tz);
			
			temp = distance(x,y,radius*cos(angle)+tx,-radius*sin(angle)+ty);
			phi = arcTan((z-tz)/temp);
			if(temp<0){
				phi+=pi;
			}
			if(dist<thickness){
				return Color_hsv(radToDeg(phi),100,100);
			}
			rhoIncMax=min(rhoIncMax,dist-thickness);
			
			
			
			
			rhoInc=max(rhoIncMin,rhoIncMax);//this is the part that tells the loops to increment based on the maximum value!
			x+=xinc*rhoInc;
			y+=yinc*rhoInc;
		}//end of rayMArching loop
		
		
		return Color_hsv(0,0,0);
	}

}
