package math;

import java.util.Random;

import geometry.Point;

public class Direction_math
{

	static Random rand = new Random();
	
		
	public static Point random_dir()
	{
	
		double phi = Math.random()*Math.PI*2;
		double costheta = Math.random()*2 - 1;
		
	    double theta = Math.acos( costheta );
	    
	    double x = Math.sin( theta) * Math.cos( phi );
	    double y = Math.sin( theta) * Math.sin( phi );
	    double z = Math.cos( theta );

	    return new Point(x,y,z);
		
	}
	
	public static Point random_dir_2D()
	{
		double theta = Math.random()*Math.PI*2;
		
		double dx = Math.cos(theta);
		double dy = Math.sin(theta);
		
		return new Point(dx, dy, 0);
	}

	// Produces a vector in the hemisphere of the input normal. 
	public static Point random_hemi_2D(Point normal)
	{
		Point output = random_dir_2D();
		
		if(output.dot(normal) < 0)
		{
			return output.multScalar(-1);			
		}
		
		return output;		
	}
	
	// Produces a vector in the hemisphere of the input normal.
	public static Point random_hemi(Point normal)
	{
		Point output = random_dir();
		
		if(output.dot(normal) < 0)
		{
			return output.multScalar(-1);			
		}
		
		return output;
	}

}
