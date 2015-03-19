package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

//to render text you have to set the textX and textY variables to the 
//locations that you want the text to be rendered at;

/*
 * Original Bryce Text 1. This is messy code that has been deprecated with Bryce text 2.
 * This information written 1 - 19 - 2014, several years after the code was written.
 * 
 * Please Ignore this class, as it is quite ugly to look at.
 */

import java.awt.Color;
import java.util.Random;

import BryceImages.Rendering.ColorCalculator;

public class ccAlphebet extends ColorCalculator
{

	public static int len = 57;//update this every time I add characters to this class.
	
	Color c;
	
	public ccAlphebet(int width, int height,Color c)
	{
		super(width,height);
		this.c = c;
		antiAliasing = 4;
	}
	@Override
	public Color getColor(double x, double y) {

	
		Color b = Color_hsv(0,0,0,0); //background (can be transparent if desired);
		int letter = (int) (x/room_height); 
		x = x%room_height;
		
		double r=room_height/2.0;// Radius of the letter boxes.
		double thickness = r/4.0; // The thickness of the letters Modify this for letters of a different thickness.
		
		x-=r;
		y-=r;
		
		
		
		
		
		
		
		
		switch (letter)
		{
		
		//--Numbers!-----------------------------------------------------------------------------------------------
		case 0://0
			if(sector(x,y,0,-r/2,r/2,thickness,0  ,180)){return c;}
			if(sector(x,y,0,+r/2,r/2,thickness,180,360)){return c;}
			if(abs(x)<r/2&&abs(x)>r/2-thickness&&abs(y)<=r/2){return c;}
			return b;

		case 1://1
			if(sector(x,y,-r,-r,r,thickness,-90,0)){return c;}
			if(abs(x)<thickness/2){return c;}
			if(y>r-thickness){return c;}
			return b;
		case 2://2
			if(sector(x,y,0,-r*1/3,r*2/3,thickness,-90,180)){return c;}
			//if(y>-.5*x+thickness&&y<-.5*x+thickness*2){return c;}
			if(sector(x,y,0, r*2/3-thickness/2,r/3+thickness/2,thickness,90,270)){return c;}
			if(y>r-thickness){return c;}
			return b;
		case 3://3
			if(sector(x,abs(y),0,r/2-thickness/4,r/2+thickness/4,thickness,-130,130)){return c;}
			return b;
		case 4://4
			if(abs(x)<thickness/2){return c;}
			if(abs(y)<thickness/2){return c;}
			if(x<-r+thickness&&y<=0)    {return c;}
			return b;
		case 5://5
			if(y<-r+thickness&&x>=0){return c;}
			if(sector(x,y,0,r/2-thickness/2,r/2+thickness/2,thickness, -180,90)){return c;}
			if(y<0&&x>-thickness&&x<=0)    {return c;}
			return b;
		case 6://6
			if(sector(x,y,0,0,r,thickness,45,270)){return c;}
			if(abs(y)<thickness/2&&x<=0){return c;}
			if(sector(x,y,0,r/2-thickness/4,r/2+thickness/4,thickness,-90,90)){return c;}
			return b;
		case 7://7
			if(y<-r+thickness){return c;}
			if(y<-x+thickness*3/4&&y>-x-thickness*3/4){return c;}
			return b;
		case 8://8
			if(sector(x,y,0,-r/2+thickness/4,r/2+thickness/4,thickness,0,360)){return c;}
			if(sector(x,y,0, r/2-thickness/4,r/2+thickness/4,thickness,0,360)){return c;}
			return b;
		case 9://9
			if(sector(x,y,0,-r/2+thickness/4,r/2+thickness/4,thickness,0,360)){return c;}
			if(rectangle(x,y,r/2-3*thickness/4,r/2+1*thickness/4,-r/2+thickness/4,r)){return c;}
			return b;
	
		case 10:	//a
				if (y>2*abs(x)-r)
				if(y<2*abs(x)-r+thickness*2||(y > 0 && y < thickness)  ){return c;}
				
				return b;
		case 11://b
				if (x<-r+thickness){return c;}
				if (abs(y)>r-thickness&&x<=0){return c;}
				if (sector(x,abs(y),-thickness/2,r/2-(thickness/4),r/2+(thickness/4),thickness,-90,90)){return c;}
				if (abs(y)<thickness/2&&x<0){return c;}
				return b;
		case 12: //c
				if(sector(x,y,0,0,r,thickness,70,290)){return c;}
				return b;
		case 13: //d
				if(x<-r+thickness){return c;}
				if(sector(x,y,-r+thickness,0,r,thickness,-90,90)){return c;}
				return b;
		case 14: //e
				if(abs(y)>r-thickness||abs(y)<thickness/2||x<-r+thickness){return c;}
				return b;
		case 15: //f
				if(y<-r+thickness||abs(y)<thickness/2||x<-r+thickness){return c;}
				return b;
		case 16: //g
			if(		sector(x, y,0,0,r,thickness, 70,360)){return c;}
			if(  rectangle2(x, y, 0, -thickness, r, thickness)){return c;}
			return b;
		case 17: //h
			if(abs(x)>r-thickness||abs(y)<thickness/2){return c;}
			return b;
		case 18: //i
			if(abs(y)>r-thickness||abs(x)<thickness/2){return c;}
			return b;
		case 19: //j
			if(y<-r+thickness||(abs(x)<thickness/2&&y<=0)){return c;}
			if(sector(x,y,-r+thickness/2,0,r,thickness,-90,0)){return c;}
			return b;
		case 20://k
			if(x<-r+thickness){return c;}
			if(x<abs(y)-r+thickness && x>abs(y)-r-1.0/2*thickness){return c;}
			return b;
		case 21: //l
			if(y>r-thickness||x<-r+thickness){return c;}
			return b;
		case 22://num_rows
			if(abs(x)>r-thickness){return c;}
			if(-y>abs(x)-thickness&&-y<abs(x)+thickness){return c;}
			return b;
		case 23://num_columns
			if(abs(x)>r-thickness){return c;}
			if(y<x+thickness&&y>x-thickness){return c;}
			return b;
		case 24://o
			if(circle(x,y,0,0,r)&&!circle(x,y,0,0,r-thickness)){return c;}
			return b;
		case 25://p
			if(x<-r+thickness){return c;}
			if(x<=0&&( abs(y)<thickness/2||y<-r+thickness  ) ){return c;}
			if(sector(x,y,0,-r/2+thickness/4,r/2+thickness/4,thickness, -90,90)){return c;}
			return b;
		case 26: //q
			if(circle(x,y,0,0,r)&&!circle(x,y,0,0,r-thickness)){return c;}
			if(x>0&&y>x-thickness&&y<x+thickness){return c;}
			return b;
		case 27://r
			if(x<-r+thickness){return c;}
			if(x<=0&&( abs(y)<thickness/2||y<-r+thickness  ) ){return c;}
			if(sector(x,y,0,-r/2+thickness/4,r/2+thickness/4,thickness, -90,90)){return c;}
			if(y>x-2*thickness+r&&y<x-1/4.0*thickness+r){return c;}
			return b;
		case 28://s
			if(sector(x,y,-r/2+thickness/4,-r/2+thickness/4,r/2+thickness/4,thickness,90,270) ){return c;}
			if(sector(x,y, r/2-thickness/4, r/2-thickness/4,r/2+thickness/4,thickness,-90,90) ){return c;}
			if(x>=-r/2+thickness/4 && y<-r+thickness){return c;}
			if(x<=r/2-thickness/4 && y> r-thickness){return c;}
			if(abs(y)<thickness/2&&x<=r/2-thickness/4&&x>=-r/2+thickness/4){return c;}
			return b;
		case 29://t
			if(y<-r+thickness){return c;}
			if(abs(x)<thickness/2){return c;}
			return b;
		case 30://u
			if(abs(x)>r-thickness&&y<=0){return c;}
			if(sector(x,y,0,0,r,thickness,180,360)){return c;}
			return b;
		case 31://v
			if (-y>2*abs(x)-r&&-y<2*abs(x)-r+(thickness*2)) {return c;}
			return b;
		case 32://w
			if(abs(x)>r-thickness){return c;}
			if(y<abs(x)+thickness&&y>abs(x)-thickness){return c;}
			return b;
		case 33://x
			if(y< x+2/3.0*thickness&&y> x-2/3.0*thickness){return c;}
			if(y<-x+2/3.0*thickness&&y>-x-2/3.0*thickness){return c;}
			return b;
		case 34://y
			if(y<=0){
			if(y< x+2/3.0*thickness&&y> x-2/3.0*thickness){return c;}
			if(y<-x+2/3.0*thickness&&y>-x-2/3.0*thickness){return c;}
			}
			else if(abs(x)<thickness/2.0){return c;}
			return b;
		case 35://z
			if(abs(y)>r-thickness){return c;}
			if(-x<y+thickness&&-x>y-thickness){return c;}
			return b;
		
		//--Punctuation -----------------------------------------------------------------
		case 36://Period '.'
			if(circle(x,y,-r*3/4,r*3/4,r/4)){return c;}
			return b;
		case 37://Exclamation point '!'
			if(circle(x,y,0,r*3/4,r/4)){return c;}//the bottom point
			if(circle(x,y,0,r/4,r/8)){return c;}
			if(circle(x,y,0,-3*r/4,r/8)){return c;}
			if(rectangle2(x,y,-r/8,-3*r/4,r/4,r)){return c;}
			return b;
		case 38://Question mark '?'
			if(circle(x,y,0,r*3/4,r/4)){return c;}//the bottom point
			
			if(sector(x,y,r/2,r/2,r/2+thickness/2,thickness, 90,180)){return c;}
			if(sector(x,y,r/2,-r/2+thickness/2,r/2,thickness, -90,180)){return c;}
			//if(sector)
			return b;
		case 39:// Colon ':', this should be rather easy...
			if(distance(x,abs(y),0,r/2.0)<thickness){return c;}
			return b;
		
		//Characters added for use in Code Breaker Game---------------------------------
		case 40://Right Curly Brace '}'
			if(sector(x,abs(y),-r/2+thickness/2,r/2,r/2,thickness,-95,0)){return c;}
			if(sector(x,abs(y), r/2-thickness/2,r/2,r/2,thickness,90,180)){return c;}
			return b;
		case 41://Left Curly Brace '{'
			x*=-1;//opposite of left Curly brace
			if(sector(x,abs(y),-r/2+thickness/2,r/2,r/2,thickness,-95,0)){return c;}
			if(sector(x,abs(y), r/2-thickness/2,r/2,r/2,thickness,90,180)){return c;}
			return b;
		case 42://right Parenthesis ')'
			if(sector(x,y,-r/2,0,r,thickness,-70,70 )){return c;}
			return b;
		case 43://left Parenthesis '('
			x*=-1;
			if(sector(x,y,-r/2,0,r,thickness,-70,70 )){return c;}
			return b;
		case 44://Equals sign '='
			if(rectangle2(x,abs(y),-r/2,r/3-thickness/2,r,thickness)){return c;}
			return b;	
		case 45:// plus sign '+'
			if(abs(x)<r/2&&abs(y)<r/2&&(abs(x)<thickness/2||abs(y)<thickness/2)  ){return c;}
			return b;	
		case 46:// minus sign '-'
			if(abs(x)<r/2&&abs(y)<thickness/2){return c;}
			return b;
		case 47:// slash sign '/'
			if(y>-2*x-thickness*1.25&&y<-2*x+thickness*1.25){return c;}
			return b;
		case 48:// backslash slash sign '\'
			x*=-1;//opposite of slash sign.
			if(y>-2*x-thickness*1.25&&y<-2*x+thickness*1.25){return c;}
			return b;			
		case 49:// Star sign *
			double dist = distance(0,0,x,y);
			double angle = lineAngle(0,0,x,y)+24;
			if(dist<3*r/4&&angle%72<=12){
				return c;
			}
			return b;
		case 50:// less than sign '<'
			if(x > 2*abs(y)-r-thickness && x < 2*abs(y)-r+thickness){return c;}
			return b;
		case 51:// greater than sign '>'
			x*=-1;//opposite of less than sign. 
			if(x > 2*abs(y)-r-thickness && x < 2*abs(y)-r+thickness){return c;}
			return b;
		case 52://underscore '_'
			if(y>r-thickness){return c;}
			return b;
		case 53:// ';' semi colon
			if(circle(x,y,0,-r/3,thickness)||sector(x,y,-r/3,r/2,r/2,thickness,-90,0)){return c;}
			return b;
		case 54:// ',' semi comma
			if(sector(x,y,-r,r/2,r/2,thickness,-90,90)){return c;}
			return b;
		case 55:// "'" single quote
			if(rectangle2(x,y,-thickness/2,-r,thickness, r)){return c;}
			return b;
		case 56:// "'" single quote
			if(rectangle2(abs(x),y,thickness/4,-r,thickness, r)){return c;}
			return b;	
		}//end of switch
		
	
		
		
		return Color_hsv(0,0,0);
	}
	
	
	
	/* 
	 * Rubbish imported from Bryce Math.
	 */
	//-Math Variables for ColorCalculation
	Random random=new Random();//this is the random generator object!!!
	public final long mySeed=(long) random(Integer.MAX_VALUE);
	
	final double pi=Math.PI;
	final double e=Math.E;
	
	
	//==========================TRIG========================================================
	public double degToRad(double degrees)
	{
	return Math.toRadians(degrees);
	}
	public double radToDeg(double radians)
	{
	return Math.toDegrees(radians);
	}
	public double cos(double input)
	{
		return Math.cos(input);
	}
	public double arcCos(double input)
	{
		return Math.acos(input);
	}
	public double sin(double input)
	{
		return Math.sin(input);
	}
	public double arcSin(double input)
	{
		return Math.asin(input);
	}
	public double tan(double input)
	{
		return Math.tan(input);
	}
	public double arcTan(double input)
	{
		return Math.atan(input);
	}
	public double ln(double input){
		return Math.log(input);
	}
	public double log10(double input){
		return Math.log10(input);
	}
	public double logA(double input,int base){//Finds the log to the power specified.
		return ln(input)/ln(base);//This is the log conversion rule!!!
	}
	//======================================================================================
	public double sqrt(double input)
	{
		return Math.sqrt(input);
	}
	public double sqr(double input)
	{
		return input*input;
	}
	public double pow(double number, double p)
	{
		return Math.pow(number,  p);
	}
	public double pow(double number, int p){
		double output = number;
		for(int i = 0; i < p; i++)
		{
			output*=number;			
		}
		return output;
	}
	public int pow(int number, int p){
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
	public int round(double n)
	{
		return (int)Math.round(n);	
	}
	public int ceil(double n)
	{
	return (int)Math.ceil(n);	
	}
	public int floor(double n)
	{
	return (int)Math.floor(n);	
	}
	public double max(double n1, double n2){
		return Math.max(n1,n2);
	}
	public double min(double n1, double n2){
		return Math.min(n1,n2);
	}
	public int max(int n1, int n2){
		return Math.max(n1,n2);
	}
	public int min(int n1, int n2){
		return Math.min(n1,n2);
	}
	
	
	// Bounds an inputted value.
	public double bound(double in, double min, double max)
	{		
		return(max(min,min(in,max)));
	}
	
	public int sign(double in){
		if(in<0){
			return -1;
		}
		else if(in>0){
			return 1;
		}
		return 0;
	}

	//--Geometry---------------------
	
	public double sqrDistance(double x1,double y1,double x2, double y2)	//Can be used to speed up distance calculations
	{																	//because of the absence of the square root step.
	return sqr(x1-x2)+sqr(y1-y2);
	}
	public double distance(double x1,double y1,double x2, double y2)
	{
	return sqrt(sqrDistance(x1,y1,x2,y2));
	}
	double distance(double x1,double y1,double z1, double x2,double y2,double z2){
		return sqrt(sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2));
	}
	double sqrDistance(double x1,double y1,double z1, double x2,double y2,double z2){//See note for the sqrDistance function above.
		return sqr(x1-x2)+sqr(y1-y2)+sqr(z1-z2);
	}
	public int abs(int n)
	{
		if(n<0)
			n*=-1;
		return n;
	}
	public double abs(double n )
	{
		if(n<0)
			n*=-1.0;
		return n;
	}

	//--Color math------------------------
	
	public Color weightedAverageColor(Color c1, Color c2, double pC2){
		// Percent C2 (pC2) is an input.
		double pC1 = 1 - pC2;// Percent C2;
		
		
		double red 		= c1.getRed()	*pC1 + c2.getRed()	*pC2;
		double green 	= c1.getGreen()	*pC1 + c2.getGreen()*pC2;
		double blue 	= c1.getBlue()	*pC1 + c2.getBlue()	*pC2;
		
		return new Color((int)red,(int)green, (int)blue );
		
	}
	
	public Color Color_rgb(double r, double g,double b,double alpha){
		
		Color temp=Color_rgb(r,g,b);
		return new Color(temp.getRed(),temp.getGreen(),temp.getBlue(),(int)alpha);
	}

	public Color Color_rgb(double red, double green,double blue) {//this provides a script that will always return a legal rgb Color;
			//going up
			red = red % 256;
			if(red<0){red+=256;}
			green = green % 256;
			if(green<0){green+=256;}
			blue = blue % 256;
			if(blue<0){blue+=256;}
			return new Color((int)red,(int)green,(int)blue);
	}
	
	//these are the 2d shape functions

	public boolean circleOutline(double x,double y,double cX,double cY,double radius, double thickness){
		
		if(abs(cX-x)>radius||abs(cY-y)>radius)//make the renderer ignore costly operation most of the time;
			return false;
		
		double tempDist=distance(x,y,cX,cY);
		if(tempDist<radius&&tempDist>radius-thickness)
			return true;//in the circleOutline
			return false;
	}
	
	public boolean circle(double x,double y,double cX,double cY,double radius){
		if(abs(cX-x)>radius||abs(cY-y)>radius)//make the renderer ignore costly operation most of the time;
		return false;
		double tempDist=sqrDistance(x,y,cX,cY);
		if(tempDist<radius*radius)//&&tempDist>radius-thickness)
			return true;//in the circle
		return false;
	}	
	public boolean ellipse(double x,double y,double cX,double cY,double width,double height){
		return circle( (x-cX)/width*height+cX,y,cX,cY,height);
	}
	
	//a complex shape than will aid in the mathematical representation of curved objects like roads;
	//angle measures should be in degrees
	public boolean sector(double x,double y,double cX,double cY,double radius,
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
	public double lineAngle(double x, double y, double focusX, double focusY){
	
		// The Java.math functions use different theta orientations than I do, so reversing the x and y values is able to correctly fix this.
		// Note the y is also negated so that the orientation has east, north, west, and south the same as on a computer screen.
		return (radToDeg(Math.atan2(x - focusX, y - focusY)) + 270) % 360;
		
	}
	
	// Same as above, but relates exactly with positive y being north, and positive x being east.
	public double lineAngle2(double x, double y, double focusX, double focusY){
		
		// The Java.math functions use different theta orientations than I do, so reversing the x and y values is able to correctly fix this.
		// Note the y is also negated so that the orientation has east, north, west, and south the same as on a computer screen.
		return radToDeg(Math.atan2(y - focusY, x - focusX));
		
	}
	
	//y=a*x+b FIXME: This requires some more thought.
	public boolean line(double x, double y,double a,double b,double thickness){
		//for vertical lines interchange the x, and y values in the call and for some reason you need to divide the thickness by 2 to make vertical lines work;
		//thickness/=2;
		if(y>a*x+b-thickness&&y<a*x+b+thickness)
			return true; //in the line
			return false;
	}
	public boolean rectangle(double x,double y,double x1,double x2,double y1,double y2){
		if(x>=x1&&x<=x2&&y>=y1&&y<=y2)
			return true;
		return false;
	}
	public boolean rectangle2(double x,double y,double x1, double y1, double width, double height){
		double x2 = x1 + width;
		double y2 = y1 + height;
		if(x>=x1&&x<=x2&&y>=y1&&y<=y2)
			return true;
		return false;
	}
	
	public boolean inTile(double x,double y,double tW,double tH, double cS, double oS)
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
