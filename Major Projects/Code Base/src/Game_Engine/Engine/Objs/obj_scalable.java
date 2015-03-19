package Game_Engine.Engine.Objs;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


/*
 * Scalable Object.
 * 
 * Written by Bryce Summers on 10 - 20 - 2013.
 * 
 * Purpose : Provides an abstraction for objects that wish to duplicate or tile their sprite in Cartesian cardinal directions.
 * 
 * 	- For instance, a ladder might have 1 image that it wished to draw multiple times vertically based on its height.
 */


public abstract class obj_scalable extends Obj
{
	public obj_scalable(double xIn, double yIn)
	{
		super(xIn, yIn);
	}

	public obj_scalable(){}

	// Dynamic Drawing Capability.
	// Assumes that the v_scale and h_scale variables have been set properly.
	public void draw(ImageB i, AffineTransform AT)
	{
		// Vertical variables.
		int y1 = (int)getY();
		int y2 = y1 + getH();
		int yinc = sprite.getHeight();
		
		// Horizontal variables.
		int x1 = (int)getX();
		int x2 = x1 + getW();
		int xinc = sprite.getWidth();
		
		int w = xinc;
		int h = yinc;
				
		Graphics2D g = i.getGraphics();
		
		
		// We need these to account for negative scales.
		int x_start = Math.min(x1, x2);
		int x_end   = Math.max(x1, x2);
		
		int y_start = Math.min(y1, y2);
		int y_end   = Math.max(y1, y2);
		
		for(int y = y_start; y < y_end; y += yinc)
		for(int x = x_start; x < x_end; x += xinc)
		{
			drawImage(g, AT, x, y, sprite);
			tileDraw(i, AT, x, y, w, h);
		}

		
		// FIXME : Draw all sub images.
	}
	
	// Enables custom drawing for every tile in the obj_scalable.
	public abstract void tileDraw(ImageB image, AffineTransform AT, int x, int y, int w, int h);
}
