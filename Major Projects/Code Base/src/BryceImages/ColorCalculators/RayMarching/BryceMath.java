package BryceImages.ColorCalculators.RayMarching;

import java.awt.Color;
import java.util.Random;



public class BryceMath {

	//-Math Variables for ColorCalculation
	Random random = new Random();//this is the random generator object!!!
	public final long mySeed=(long) random(Integer.MAX_VALUE);
	
	public static final double pi=Math.PI;
	static final double e=Math.E;
	
	// Vectors.
	public static Vector v(double x, double y, double z)
	{
		return new Vector(x, y, z);
	}
	
	public static Vector v(double x, double y)
	{
		return new Vector(x, y);
	}
	
	//==========================TRIG========================================================
	public static double degToRad(double degrees)
	{
	return Math.toRadians(degrees);
	}
	public static double radToDeg(double radians)
	{
	return Math.toDegrees(radians);
	}
	public static double cos(double input)
	{
		return Math.cos(input);
	}
	public static double arcCos(double input)
	{
		return Math.acos(input);
	}
	public static double sin(double input)
	{
		return Math.sin(input);
	}
	public static double arcSin(double input)
	{
		return Math.asin(input);
	}
	public static double tan(double input)
	{
		return Math.tan(input);
	}
	public static double arcTan(double input)
	{
		return Math.atan(input);
	}
	public static double ln(double input){
		return Math.log(input);
	}
	public double log10(double input){
		return Math.log10(input);
	}
	public static double logA(double input,int base){//Finds the log to the power specified.
		return ln(input)/ln(base);//This is the log conversion rule!!!
	}
	//======================================================================================
	public static double sqrt(double input)
	{
		return Math.sqrt(input);
	}
	public static double sqr(double input)
	{
		return input*input;
	}
	public static double pow(double number, double p)
	{
		return Math.pow(number,  p);
	}
	public static double pow(double number, int p){
		double output = number;
		for(int i = 0; i < p; i++)
		{
			output*=number;			
		}
		return output;
	}
	public static int pow(int number, int p){
		int output = number;
		for(int i = 0; i < p; i++)
		{
			output*=number;			
		}
		return output;
	}
	//==================Randoms==============================================================
	
	public double random()
	{
		return random.nextDouble();
	}
	public double random(int n)
	{
		
		return n*random.nextDouble();
	}
	public double random(double n)
	{
		return n*random.nextDouble();
	}
	public double random(int f,int l)//returns a random double that is in the range first to last (cannot be the last one).
	{
		return (l-f)*random.nextDouble()+f  ;
	}
	public void random_set_seed()
	{
		
	random.setSeed(mySeed);

	}
	public void random_set_seed(int seed)
	{
		
	random.setSeed(seed);

	}
	public static int round(double n)
	{
	return (int)Math.round(n);	
	}
	public static int ceil(double n)
	{
	return (int)Math.ceil(n);	
	}
	public static int floor(double n)
	{
	return (int)Math.floor(n);	
	}
	public static double max(double n1, double n2){
		return Math.max(n1,n2);
	}
	public static double min(double n1, double n2){
		return Math.min(n1,n2);
	}
	public static int max(int n1, int n2){
		return Math.max(n1,n2);
	}
	public static int min(int n1, int n2){
		return Math.min(n1,n2);
	}
	
	
	// Bounds an inputted value.
	public static double bound(double in, double min, double max)
	{		
		return(max(min,min(in,max)));
	}
	
	public static int sign(double in){
		if(in<0){
			return -1;
		}
		else if(in>0){
			return 1;
		}
		return 0;
	}

	//--Geometry---------------------
	
	public static double sqrDistance(double x1,double y1,double x2, double y2)	//Can be used to speed up distance calculations
	{																	//because of the absence of the square root step.
	return sqr(x1-x2)+sqr(y1-y2);
	}
	public static double distance(double x1,double y1,double x2, double y2)
	{
	return sqrt(sqrDistance(x1,y1,x2,y2));
	}
	public static double distance(double x1,double y1,double z1, double x2,double y2,double z2){
		return sqrt(sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2));
	}
	public static double sqrDistance(double x1,double y1,double z1, double x2,double y2,double z2){//See note for the sqrDistance function above.
		return sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2);
	}
	public static int abs(int n)
	{
		if(n<0)
			n*=-1;
		return n;
	}
	public static double abs(double n )
	{
		if(n<0)
			n*=-1.0;
		return n;
	}

	//--Color math------------------------
	
	public static Color weightedAverageColor(Color c1, Color c2, double pC2){
		// Percent C2 (pC2) is an input.
		double pC1 = 1 - pC2;// Percent C2;
		
		
		double red 		= c1.getRed()	*pC1 + c2.getRed()	*pC2;
		double green 	= c1.getGreen()	*pC1 + c2.getGreen()*pC2;
		double blue 	= c1.getBlue()	*pC1 + c2.getBlue()	*pC2;
		double alpha	= c1.getAlpha()	*pC1 + c2.getAlpha()*pC2;
		
		return new Color((int)red,(int)green, (int)blue, (int) alpha);
		
	}
	
	public static Color Color_rgb(double r, double g,double b,double alpha){
		
		Color temp=Color_rgb(r,g,b);
		return new Color(temp.getRed(),temp.getGreen(),temp.getBlue(),(int)alpha);
	}

	public static Color Color_rgb(double red, double green,double blue) {//this provides a script that will always return a legal rgb Color;
			//going up
			red = red % 256;
			if(red<0){red+=256;}
			green = green % 256;
			if(green<0){green+=256;}
			blue = blue % 256;
			if(blue<0){blue+=256;}
			return new Color((int)red,(int)green,(int)blue);
	}
	public static Color Color_hsv(double h, double s,double v,double alpha){
		
		Color temp=Color_hsv(h,s,v);
		return new Color(temp.getRed(),temp.getGreen(),temp.getBlue(),(int)alpha);
	}
	public static Color Color_hsv(double h, double s,double v) {
		double r, g, b;
		int i;
		double f, p, q, t;
	 //going up
		h=h%360;
		if(h<0){h+=360;}//these attribute the correct color to this; colors will wrap around if they exceed the allowed marks;
		s=s%101;
		if(s<0){s+=101;}
		v = v%101;
		if(v<0){v+=101;}
		
		h = min(h,360);
		s = min(s,100);
		v = min(v,100);
 	 	 
		// We accept saturation and value arguments from 0 to 100 because that's
		// how Photoshop represents those values. Internally, however, the
		// saturation and value are calculated from a range of 0 to 1. We make
		// That conversion here.
		s /= 100;
		v /= 100;
	 
		if(s == 0) {
			// Achromatic (grey)
			r = g = b = v;
		  return new Color(round(r * 255),round(g * 255),round(b * 255));
		}
	 
		h /= 60; // sector 0 to 5
		i = floor(h);
		f = h - i; // factorial part of h
		p = v * (1 - s);
		q = v * (1 - s * f);
		t = v * (1 - s * (1 - f));
	 
		switch(i) {
			case 0:
				r = v;
				g = t;
				b = p;
				break;
	 
			case 1:
				r = q;
				g = v;
				b = p;
				break;
	 
			case 2:
				r = p;
				g = v;
				b = t;
				break;
	 
			case 3:
				r = p;
				g = q;
				b = v;
				break;
	 
			case 4:
				r = t;
				g = p;
				b = v;
				break;
	 
			default: // case 5:
				r = v;
				g = p;
				b = q;
		}
	 
		return new Color(round(r * 255),round(g * 255),round(b * 255));
}
	
	//these are the 2d shape functions

	public static boolean circleOutline(double x,double y,double cX,double cY,double radius, double thickness){
		
		if(abs(cX-x)>radius||abs(cY-y)>radius)//make the renderer ignore costly operation most of the time;
			return false;
		
		double tempDist=distance(x,y,cX,cY);
		if(tempDist<radius&&tempDist>radius-thickness)
			return true;//in the circleOutline
			return false;
	}
	
	public static boolean circle(double x,double y,double cX,double cY,double radius){
		if(abs(cX-x)>radius||abs(cY-y)>radius)//make the renderer ignore costly operation most of the time;
		return false;
		double tempDist=sqrDistance(x,y,cX,cY);
		if(tempDist<radius*radius)//&&tempDist>radius-thickness)
			return true;//in the circle
		return false;
	}	
	public static boolean ellipse(double x,double y,double cX,double cY,double width,double height){
		return circle( (x-cX)/width*height+cX,y,cX,cY,height);
	}
	
	//a complex shape than will aid in the mathematical representation of curved objects like roads;
	//angle measures should be in degrees
	public static boolean sector(double x,double y,double cX,double cY,double radius,
						  double thickness, double angleMin,double angleMax){
		
		if(circleOutline(x,y,cX,cY,radius,thickness))
		{
			double tAngle=lineAngle(x,y,cX,cY);
		
			//Now check to see if it is in the correct range;
			if(angleMin<0){
				tAngle-=angleMin;
				tAngle=tAngle%360;
				angleMax-=angleMin;
				angleMin=0;
			}
			if(tAngle>=angleMin&&tAngle<=angleMax)
				return true;//Inside of the sector
		}
		return false;
			
	}
	
	// +x = east, -y = North
	public static double lineAngle(double x, double y, double focusX, double focusY){
	
		// The Java.math functions use different theta orientations than I do, so reversing the x and y values is able to correctly fix this.
		// Note the y is also negated so that the orientation has east, north, west, and south the same as on a computer screen.
		return (radToDeg(Math.atan2(x - focusX, y - focusY)) + 270) % 360;
		
	}
	
	// Same as above, but relates exactly with positive y being north, and positive x being east.
	public static double lineAngle2(double x, double y, double focusX, double focusY){
		
		// The Java.math functions use different theta orientations than I do, so reversing the x and y values is able to correctly fix this.
		// Note the y is also negated so that the orientation has east, north, west, and south the same as on a computer screen.
		return radToDeg(Math.atan2(y - focusY, x - focusX));
		
	}
	
	//y=a*x+b FIXME: This requires some more thought.
	public static boolean line(double x, double y,double a,double b,double thickness){
		//for vertical lines interchange the x, and y values in the call and for some reason you need to divide the thickness by 2 to make vertical lines work;
		//thickness/=2;
		if(y>a*x+b-thickness&&y<a*x+b+thickness)
			return true; //in the line
			return false;
	}
	public static boolean rectangle(double x,double y,double x1,double x2,double y1,double y2){
		if(x>=x1&&x<=x2&&y>=y1&&y<=y2)
			return true;
		return false;
	}
	public static boolean rectangle2(double x,double y,double x1, double y1, double width, double height){
		double x2 = x1 + width;
		double y2 = y1 + height;
		if(x>=x1&&x<=x2&&y>=y1&&y<=y2)
			return true;
		return false;
	}
	
	public static boolean inTile(double x,double y,double tW,double tH, double cS, double oS)
	{//This is the code that calculates tiles;
		double tileW,tileH,curveSize,offSet;
		
		tileW=tW;//Change these variables to affect the tile rendering;
		tileH=tH;
		offSet=oS;//the distance between tiles;
		curveSize=cS;
		double tempX=x%(tileW+offSet);//the offset is added to space the tiles out from each other
		double tempY=y%(tileH+offSet);
		if( rectangle(tempX,tempY,0,tileW,curveSize,tileH-curveSize)||		//horizontal rectangle
			rectangle(tempX,tempY,curveSize,tileW-curveSize,0,tileH)||		//vertical   rectangle
			circle(tempX,tempY,curveSize,curveSize,curveSize)||				//upperLeft  border circle
			circle(tempX,tempY,tileW-curveSize,curveSize,curveSize)||		//upperRight border circle
			circle(tempX,tempY,tileW-curveSize,tileH-curveSize,curveSize)||	//lowerRight border circle
			circle(tempX,tempY,curveSize,tileH-curveSize,curveSize)			//lowerLeft  border circle
		   ){
				return true;
			}
		else
			return false;
	}
	

	
}
