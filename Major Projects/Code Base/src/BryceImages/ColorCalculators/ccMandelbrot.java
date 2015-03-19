package BryceImages.ColorCalculators;

/***************************************************************************************
 ** Created by Bryce Summers 6/12/10					 						      **
 ** Purpose:	This is a plug in to render the Mandelbrot set						  **
 ** Ported in an ugly way to my current code base on 2 - 5 - 2014.					  **
 ** A lot more work needs to be done to get this class up to snuff 					  **
 **************************************************************************************/

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Rendering.ColorCalculator;


public class ccMandelbrot extends ColorCalculator 
{
	
	Dimension dim;
	public long mySeed;
	
	int P = 2;//P is the power that the iterative equation is raised to. This should be 2 for the mandelbrot equation
	int max_iteration = 20272;

	double startX, startY, endX, endY;
	
	public static int minIterations = 300;
	
	public ccMandelbrot(Dimension tempDim)
	{
		super(tempDim);
		setupCamera();
		antiAliasing = 3;
	}
	
	// Orients the camera view to the desired region of R^2
	public void setupCamera()
	{
		startX = -2.0;
		startY = -1.15;
		endX   = .5;
		endY   = 1.15;
	}


	// Here is the magic formula insert place;
	public Color getColor(double c,double r)
	{
	
		double x0 = startX+(c/(room_width-1)*(endX-startX)); //co-ordinate of pixel
		double y0 = startY+(r/(room_height-1)*(endY-startY)); //co-ordinate of pixel

		
		//change these variables to modify the zoom!!
		/*
		x0/=16;
		y0/=16;
		x0-=1.35;
		y0-=.1;
		*/  
		  
		double  x = 0;
		double  y = 0;
		
		int iteration = 0;
		  		    // Only add this to the Mandelbrot fractal
		///////////////////////////////////////////////////////////////////////////////////////
		////  this is where the magic happens, I will have to study this more in depth later////
		/////////////////////////////////////////////////////////////////////////////////////////*/
		  boolean escaped=false;
		  
		  int exInter=0;//to enable smooth shading the fractal has to keep on iterating for a bit
		  while (exInter<6) //This shows the equation of the circle in which the set resides
		  {
		  //x=abs(x);//use these to produce the burning ship fractal
		  //y=abs(y);
		    double xtemp = (x*x) - (y*y) + x0;
		    y = P*x*y + y0; 
		            //z_n+1_ = zn^2 + c

		    x = xtemp;

		    iteration++;

		    if(!escaped && !(x*x + y*y <=(2*2)&&iteration < max_iteration)){
		    	escaped=true;
		    }
		    else if(escaped){
		    	exInter++;
		    }
		    	
		    
		  }//end of the while loop


		  if ( iteration >= max_iteration ) 
		    {
		      return Color.white;
		    }
		  else
		  {
			  int n = iteration;
			  double v = n + 1 - (Math.log10 (Math.log10  (BryceMath.distance(0,0,x,y))) / Math.log10(2));
			  //double v= n-(ln(distance(0,0,x,y)/ln(max_iteration)))/ln(P);//Coloring algorithm, for continuous colors
			  
			  
			  //System.out.println(v);
			  
			  //return  Color_hsv(1000/v,100,100);
			 			  
			  double hue = -100*Math.log(8+v-0);
			  
			  // used for my Brot my HEart shirt.
			  double val = BryceMath.bound(100 - (400 + hue), 0, 100);
			  
			  return  Color_hsv(0,0,100, val);
			  //return  Color_hsv(1000/(8+v-Global.minIterations),100,100);

		  }

		
		
		
	

	}//end of Mandelbrot color calculation

}//end of Mandelbrot class;
