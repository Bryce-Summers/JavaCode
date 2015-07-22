package Math;

import java.util.Random;

public class Vector_math
{

	static Random rand = new Random();
	
		
	public static Vector3 random_dir()
	{
	
		double phi = Math.random()*Math.PI*2;
		double costheta = Math.random()*2 - 1;
		
	    double theta = Math.acos( costheta );
	    
	    double x = Math.sin( theta) * Math.cos( phi );
	    double y = Math.sin( theta) * Math.sin( phi );
	    double z = Math.cos( theta );

	    return new Vector3(x,y,z);
		
	}
	
	public static Vector3 random_dir_2D()
	{
		double theta = Math.random()*Math.PI*2;
		
		double dx = Math.cos(theta);
		double dy = Math.sin(theta);
		
		return new Vector3(dx, dy, 0);
	}

	// Produces a vector in the hemisphere of the input normal. 
	public static Vector3 random_hemi_2D(Vector3 normal)
	{
		Vector3 output = random_dir_2D();
		
		if(output.dot(normal) < 0)
		{
			return output.mult(-1);			
		}
		
		return output;		
	}
	
	// Produces a vector in the hemisphere of the input normal.
	public static Vector3 random_hemi(Vector3 normal)
	{
		Vector3 output = random_dir();
		
		if(output.dot(normal) < 0)
		{
			return output.mult(-1);			
		}
		
		return output;
	}

}
