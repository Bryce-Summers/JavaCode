package Game_Engine.GUI.ImageProccessing;

import BryceMath.functions.Interpolator;

/*
 * The Interpolation vars class.
 * This class handles the storing of necessary data for the interpolation of objects geometrically.
 * 
 * Written by Bryce Summers on 6 - 12 - 2013.
 * Purpose: Many Objects want to store an alarm.
 */

public class InterpolationVars
{
	// We need to have an Interpolator.
	private static final Interpolator interpolator = new Interpolator(1);

	
	private int maxTime = 1;
 	private int time = 2;

 	
 	// Returns the current interpolated percentage and increments the time.
 	public double getPercentage()
 	{
 		if(maxTime == 0)
 		{
 			time = 1;
 			return 1;
 		}
 		
 		double output = interpolator.eval(time*1.0/maxTime);
 		
 		if(time <= maxTime)
 		{
 			time++;
 		}
 		
 		return output;
 	}
 	
 	// Sets the maximum time.
 	public void setMaxTime(int i)
 	{
 		if(i < 0)
 		{
 			throw new Error("Max time must be non negative.");
 		}
 		
 		maxTime = i;
 		time = maxTime + 1;
 	}
 	
 	public int getMaxTime()
 	{
 		return maxTime;
 	}
 	 	
 	public boolean done()
 	{
 		return time > maxTime;
 	}
 	
 	// Restarts the interpolator.
 	public void reset()
 	{
 		time = 0;
 	}
 	
 	public void stop()
 	{
 		time = maxTime + 1;
 	}

}
