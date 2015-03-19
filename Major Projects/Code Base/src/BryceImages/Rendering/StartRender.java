package BryceImages.Rendering;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

import BryceImages.GUI.Display;


/*
 * The StartRender class, written by Bryce Summers on 4 - 6 - 2013
 * 
 * Purpose: Links together my Renderer and Antialiasing classes to create a pipeline that produces images.
 */

public class StartRender extends Thread
{

	Brenderer r;
	Anti_Aliaser a;
	BufferedImage image;
	ColorCalculator cc;
	
	Display d = null;
	
	CountDownLatch display;
	
	public StartRender(boolean wait)
	{
		r = new Brenderer();
		a = new Anti_Aliaser();
		
		Brenderer   .await_completion = wait;
		Anti_Aliaser.await_completion = wait;
	}
	
	
	public BufferedImage render(ColorCalculator cc_in)
	{	
		// Set the correct ColorCalculator.
		cc = cc_in;		
		
		// Tell the Brenderer to render an image from the given Color Calculator.
		image = r.renderImage(cc);
		
		
		// If we are not waiting for completion,
		// then we segment the processes via a thread. 
		if(!Brenderer.await_completion)
		{
			start();

			// Return a pointer to the current image being operated on.
			return image;
		}
		
		
		// Otherwise we perform all operations sequentially.
		// We are assumed to be waiting until completion at each step.
		
		// Alias the image.
		image = a.alias(image, cc);
		
		// Return the final rendered image.
		return image;
	}
	
	public void run()
	{
		// Wait for the Brenderer.
		r.await();

		System.out.println("Initial Rendering Done");
		
		if(cc.getAntiAliasing() < 2)
		{
			return;
		}
		
		// Alias the image.
		image = a.alias(image, cc);
		
		while(d == null){}
		
		if(d != null)
		{
			d.setImage(image);
		}
		
		a.await();
		
		System.out.println("Aliasing done");
		
	}
	
	public void sendUpdates(Display d_in)
	{
		d = d_in;
	}
	
	
	
}
