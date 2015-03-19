package BryceImages.Rendering;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;


/*
 * The Anti Aliaser, Written by Bryce Summers.
 * 
 * Created 12 - 22 - 2012. 
 * 
 * Purpose: To provide provide functionality for efficiently anti aliasing an image.
 * 
 * Capabilities: Edge detection, aliases the edges of a buffered image.
 * 				
 */

class Anti_Aliaser extends Thread
{
	
	// -- Local variables.
	BufferedImage in, out;
	
	int w, h;
	
	// Store the amount of Anti Aliasing that is requested by the color calculator.
	int anti_aliasing;
	
	// Parallel variables.
	volatile static int y = 0;
	
	CountDownLatch latch;
	
	// Allows for the calling program to continue its execution with a partial image.
	// This will make the program unsafe if this class is used to render multiple images while in this mode.
	// This should be set to true if used in a system that does a substantial sequence of preloading.
	public static boolean await_completion = false;
	
	public static ColorCalculator cc;
	
	
	// FIXME: Do some research and figure out what the optimum value for the cuttoff between similar colors should be should be.
	
	// Color differences tolerated.
	//int THRESH = 30;

	// The relative coordinates of subpixels.
	double[] subCoordinates;
	
	// The divisor for the subpixel averaging can be precomputed.
	int normalAveragingDivisor;
	
	boolean done = true;
	
	// -- Returns the aliased version of a buffered image.
	public BufferedImage alias(BufferedImage input, ColorCalculator cc)
	{
		// Do not start again if it is not done yet.

		// FIXME: Perhaps wait on a latch instead!!
		if(!done)
		{
			return null;
		}
		
		Anti_Aliaser.cc = cc;
		
		// Extract the amount of Anti Aliasing from the Color Calculator.
		anti_aliasing = cc.getAntiAliasing();
		
		// Perform no work if the anti aliasing is 1.
		if(anti_aliasing == 1)
		{
			return input;
		}
		
		// We have only just begun.
		done = false;
		
		// This is the most important line in here.
		// This resets the Anti_Aliaser to be ready for a new image
		//  after it has already rendered previous images.
		y = 0;
		
		in  = input;
		w = in.getWidth();
		h = in.getHeight();		
		
		out = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		
		subCoordinates = new double[anti_aliasing];
		for(int k = 0; k < anti_aliasing; k++)
		{
			subCoordinates[k] = 1.0*k/anti_aliasing ;//- 2.0/antiAliasing;
		}
		
		// Pre-computations.
		normalAveragingDivisor  = anti_aliasing*anti_aliasing;
				
		// Start the maximum number of threads this system can handle.
		int threadNum = Runtime.getRuntime().availableProcessors();
		
		// Create the latch before starting the threads.
		latch = new CountDownLatch(threadNum);
		for(int i = 0; i < threadNum; i++)
		{
			renderThread rt = new renderThread();
			rt.start();//Start the rendering Thread.
		}
		
		if(await_completion)
		{
			// Implement if it is desired that all calling threads are suspended untill rendering completes.
			try
			{
				latch.await();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}		

		return out;
	}
	
	private class renderThread extends Thread
	{
		// Give this thread a unique copy of the color calculator.
		ColorCalculator cc = (ColorCalculator) Anti_Aliaser.cc.clone(); 
		
		public void run()
		{
			int THRESH = cc.getAliasingThreshold();
			
			// Surrounding pixel colors. 
			int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
			
			// Current Pixel color.
			int c;
			
			// Flags.
			boolean f1, f2, f3, f4;
	
			// A row of colors.
			int[] rgb_line = new int[w];
			
			// Retrieve the first y value.
			int y = getNextProperY();			
			
			// Churn out anti aliased rows in parallel until 
			// all rows in the image have been completed.
			
			while(y < h)
			{
				for(int x = 0; x < w; x++)
				{
					// Compute the flags.
					f1 = x > 0;
					f2 = x < w - 1;
					f3 = y > 0;
					f4 = y < h - 1;
					
					if(f1){c1 = in.getRGB(x - 1, y);}
					if(f2){c2 = in.getRGB(x + 1, y);}
					if(f3){c3 = in.getRGB(x, y - 1);}
					if(f4){c4 = in.getRGB(x, y + 1);}
					
					c = in.getRGB(x, y);
					
					// If we have exceeded the edge detection tolerance,
					// then	return the aliased pixel in the final image.
					if(	(dif(c, c1) > THRESH && f1)||
						(dif(c, c2) > THRESH && f2)||
						(dif(c, c3) > THRESH && f3)||
						(dif(c, c4) > THRESH && f4))
					{
						rgb_line[x] = get_aliased_color(x, y, cc);
					}
					else // Otherwise return the originally calculated value.
					{
						rgb_line[x] = in.getRGB(x,y);
					}
				}// End x of pixel iteration loops
				
				// Draw the line of pixels onto the output image.
				out.setRGB(0, y, w, 1, rgb_line, 0, w);
				
				// Ascertain the next y coordinate.
				y = getNextProperY();
				
			}// End of y pixel iteration loop.
			
			// Reduce the latch count.
			latch.countDown();
			
			// Tell the outside world that the computation is done.
			if(latch.getCount() == 0)
			{
				done = true;
			}
		}
	}

	// Allows for outside threads to wait for this thread to run to completion.
	// Works regardless of the await_completion flag.
	public void await()
	{
		try
		{
			latch.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	// Returns the total of the differences between the components.
	private int dif(int c1, int c2)
	{
		int r1, r2, g1, g2, b1, b2, a1, a2;
		
		int output = 0;
				
		// Compute the red, green, and blue components of the two colors.
		r1 = c1 >> 16 & 0xFF;
		r2 = c2 >> 16 & 0xFF;
		
		g1 = c1 >> 8 & 0xFF;
		g2 = c2 >> 8 & 0xFF;
		
		b1 = c1 & 0xFF;		
		b2 = c2 & 0xFF;
		
		a1 = c1 >> 24 & 0xFF;
		a2 = c2 >> 24 & 0xFF;
		
		// Compute the total difference between the components.
		output += Math.abs(r1 - r2);
		output += Math.abs(g1 - g2);
		output += Math.abs(b1 - b2);
		output += Math.abs(a1 - a2);
		
		return output;
	}
	
	private int get_aliased_color(int x, int y, ColorCalculator cc)
	{
		
		int red,green,blue,alpha;
		// Initialize component variables 
		red 	= 0;
		green	= 0;
		blue	= 0;
		alpha	= 0;
		
		int averagingDivisor;
		averagingDivisor = 0;//This would be left out if alpha values were not considered.

		Color c;
					
		// Average all of the subpixels together.
		for(int	xx = 0; xx < anti_aliasing; xx++)
		for(int yy = 0; yy < anti_aliasing;	yy++)
		{
			// Optimize using the 1 already calculated value.
			if(x == 0 && y == 0)
			{
				c = new Color(in.getRGB(x,y));
			}
			else
			{
				// Calculate subColor
				c = cc.getColor(x + subCoordinates[xx], y + subCoordinates[yy]);
			}
			
			// Add each component of the Current Color weighted by its alpha value to the proper variable.
			red 	+= c.getRed()	* c.getAlpha();
			green	+= c.getGreen()	* c.getAlpha();
			blue	+= c.getBlue()	* c.getAlpha();
			alpha	+= c.getAlpha();
			
			// Tell the averaging divisor how much of a contribution this component has made.
			averagingDivisor += c.getAlpha();
	
		}// end of subpixel loop.
		
		// Now find the average components for the subpixel;
		if(averagingDivisor != 0)
		{
			red		/= averagingDivisor;
			green	/= averagingDivisor;
			blue	/= averagingDivisor;
			alpha	/= normalAveragingDivisor;
		}
		
		// Return the correct final color.
		return (red << 16) + (green << 8) + (blue) + (alpha << 24);
		
	}
	
	// This method protects the integrity of the y variable and ensures that all of the
	// threads render appropriate lines without repeats or skips.
	private synchronized int getNextProperY()
	{
		// Store the current value.
		int output = y;
		
		// Increment the value internally.
		y += 1;
		
		// Return the original value.
		return output;
	}
	
	// -- Data manipulation methods.
	public void set_anti(int a)
	{
		anti_aliasing = a;
	}
	
	public boolean done()
	{
		return done;
	}
}
