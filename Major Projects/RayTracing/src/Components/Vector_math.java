package Components;

import java.util.Random;

import BryceMath.DoubleMath.Vector;

public class Vector_math
{

	static Random rand = new Random();
	
	public static Vector random_dir()
	{
		double x = rand.nextGaussian();
		double y = rand.nextGaussian();
		double z = rand.nextGaussian();
		
		Vector dir = new Vector(x, y, z);
		dir = dir.norm();
		
		return dir;
	}

}
