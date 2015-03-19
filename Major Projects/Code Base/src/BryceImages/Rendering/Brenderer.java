package BryceImages.Rendering;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;


// Brenderer class
// Written by, Bryce Summers 6/29/2012
// Purpose: This class takes a ColorCalculator that specifies a mathematical space,
//---------and a function for the colors within.
//---------Brenderer then creates a picture composed of a number of pixels equal
//---------to the dimensions of the mathematical space mathematical space.
// Capabilities:
//			Full Anti Aliasing support
//			Save file parsing.
//			Support for Alpha values.
//			full Multithreading support, gives each of the threads an equal load of processing to do.


// FIXME : I should probably rewrite this class with modern style.
/* FIXME : I should include options for rendering grayscale images and mono images so that computation 
 * 			time can be improved for technical sprites such as the perlin noise texture for my Hoth game.
 */

class Brenderer
{

	// Local Variables
	static BufferedImage imageOut;
	
	int w,h;
	
	//Controls the assignment of y values to threads.
	volatile static int y = 0;
	
	static ColorCalculator ccc;// Current Color Calculator.
	
	boolean showCompleteRows = false; // This flag controls whether to draw completed rows to the screen, or to draw incomplete Rows.
	
	CountDownLatch latch; // A latch to prevent the rest of a program from executing until the rendering has completed.
	
	
	// -- Modifyables
	
	// Set this to true here if you want the calling threads to be suspended until the completion of the rendering.
	// Set this to false, if you want to be able to use the partial image for other things such as displaying the progress onto a screen.
	// Warning: if this is false, then it may be unsafe to call this multiple times, because safety precautions have not yet been implemented.
	public static boolean await_completion = false;
	
	//--Constructor
	public Brenderer()
	{
	}
	
	private class renderThread extends Thread
	{
		public synchronized void run()
		{
			// First a local copy of the current ColorCalculator 
			// is created to serve this unique thread.
			ColorCalculator ccc = (ColorCalculator) Brenderer.ccc.clone();
			
			// A data array to store an array of rgb values representing a line of pixels to be sent to the image after completion.
			int[] rgb_line = new int[w];

			// Retrieve the y coordinate of the next row that should be rendered.
			int y = getNextProperY();
			
			// Iterate throughout the Color Calculator's mathematical space,
			//-setting each pixel of the image to the appropriate Color
			//-defined by the color calculator.
			while(y < h)
			{
				for(int x = 0; x < w; x++)
				{
				
					// Set the rgb_line values correctly based on the color calculator formula.
					rgb_line[x] = ccc.getColor(x, y).getRGB();
				}
				
				//Transfer the completed calculated row rgb information to the image.
				imageOut.setRGB(0,y,w,1,rgb_line,0,w);
				
				// Retrieve the next row coordinate.
				y = getNextProperY();
			}
			
			// This only is needed in the version for applications.
			latch.countDown();
		}// end of run
	}
	
	private synchronized int getNextProperY()
	{
		// Store the current value.
		int output = y;
		
		// Increment the value internally.
		y += 1;
		
		// Return the original value.
		return output;
	}
	
	//--Starts the rendering of an image from the current local variables.
	public BufferedImage renderImage(ColorCalculator c)
	{
		// This is the most important line in here.
		// This resets the Brenderer to be ready for a new image
		//  after it has already rendered previous images.
		y = 0;
		
		//Set local variables for the Thread;
		w = c.getWidth();
		h = c.getHeight();
		Brenderer.ccc = c;
		
		//Create the final output image.
		imageOut = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		
		// Start the maximum number of threads this system can handle.
		int threadNum = Runtime.getRuntime().availableProcessors();
		
		// Create the latch before starting the threads.
		latch = new CountDownLatch(threadNum);
		
		for(int i = 0; i < threadNum; i++)
		{
			renderThread rt = new renderThread();
			rt.start();//Start the rendering Thread.
		}
		
		// Allows the user to specify whether the partially rendered image is visible,
		// or whether to wait for the image to be finished before returning it. 
		if(await_completion)
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
		
		//Save the image when rendering has completed.
		//saveImage(imageOut,"BrendSave");
			
		//* Implement in the version for game engine Applications.
		// Note: Surprisingly the toCompatibleImage function is very important in ensuring the images are drawn fast and correctly without annoying artifacts.
		//return toCompatibleImage(imageOut);
		
		return imageOut;
		
	}
	
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
	
	public boolean done()
	{
		return latch.getCount() == 0;
	}
	
	
}//end of Brenderer Class
