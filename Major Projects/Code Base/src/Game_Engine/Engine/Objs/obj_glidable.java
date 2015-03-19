package Game_Engine.Engine.Objs;

import Game_Engine.GUI.ImageProccessing.InterpolationVars;

/*
 * Glidable object.
 * 
 * This class extends the built in movement capabilities of Objects and enables
 * them to cleanly and elegantly move from place to place.
 * 
 * Written by Bryce Summers on 6 - 17 - 2013.
 * 
 * 
 * FIXME :	It would be nice if I could remove all of these required data fields, 
 * 			package them into a class, and only initialize them when the user wants an object to have these capabilities.
 */

public class obj_glidable extends Obj
{

	// -- Glidable Objects know how to glide.
	protected InterpolationVars interpolator = new InterpolationVars();
	private double x_current, y_current;
	protected double x_dest, y_dest;
	private boolean glide = false;
	
	public obj_glidable(double x_in, double y_in)
	{
		super(x_in, y_in);
		
		// FIXME : Determine a desired reverting speed.
		interpolator.setMaxTime(5);
		
		x_dest = getX();
		y_dest = getY();
		
	}

	@Override
	protected void update()
	{
		
		if(glide)
		{
			glide = false;
			x_current = getX();
			y_current = getY();
			interpolator.reset();
		}
		
		// Gracefully glide the handle to the desired position.
		if(!interpolator.done())
		{
			double per = interpolator.getPercentage();
			double per2 = 1 - per;
			
			if(per == 1)
			{
				setX(x_dest);
				setY(y_dest);
			}
			else
			{
				setX(x_dest*per + x_current*per2);
				setY(y_dest*per + y_current*per2);				
			}
		}
	}

	// Wraps the glide functionality to glide to a specific object.
	public void glide(Obj o)
	{
		glide(o.getX(), o.getY());
	}
	
	// Tells this object to glide to the given location.
	public void glide(double x, double y)
	{
		if(!interpolator.done())
		{
			return;
		}
		
		glide = true;
		x_dest = x;
		y_dest = y;
	}
	
	private void forceGlide(double x, double y)
	{
		glide = true;
		x_dest = x;
		y_dest = y;
	}

	// Returns true if the action was executed.
	public boolean setInterpolationTime(int time)
	{
		if(!interpolator.done())
		{
			return false;
		}
		
		interpolator.setMaxTime(time);
		
		return true;
	}
	
	@Override
	public void revert()
	{
		if(getX() == x_start && getY() == y_start && interpolator.done())
		{
			return;
		}
		
		if(x_dest != x_start || y_dest != y_start || (interpolator.done()))
		{
			forceGlide(x_start, y_start);
		}
	}
	
	// Exits the current interpolation, sets the interpolations speed to a new value,
	// and then proceeds onward with a new interpolation to the new destination.
	public void setTimeTillDone(int delay_from_here)
	{
		interpolator.stop();
		setInterpolationTime(delay_from_here);
		glide(x_dest, y_dest);
	}
	
}
