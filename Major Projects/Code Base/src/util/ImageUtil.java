package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.interfaces.SerialImageB;
import Data_Structures.ADTs.Stack;
import Data_Structures.Structures.List;

/* The Image Ops class, written by Bryce Summers of 4 - 6 - 2013.
 * 
 * Purpose: Provides useful utility functions for doing things
 *          like saving images and making them compatible.
 */

public class ImageUtil
{
	// Allows abstract Serialization functionality for Serializing Images.
	public static void saveImage(SerialImageB obj, String name)
	{
		saveImage(obj.serializeImage(), name);
	}
	
	//-- A Function that takes a buffered Image and a file name and saves it to a local file.
	//-- Candidate image types include, "PNG, "GIF", and "JPG."
	public static void saveImage(BufferedImage image, String name)
	{	
		try
		{
			// Compute the index of the '.' in a standard file name.
			int dotIndex = name.length() - 4;
			
			// Declare the imageType  variable
			String 	imageType; 
			
			// Declare the outputFile variable
			File 	outputfile;
			
			// If a file type has been included in the inputed name,
			// then set the imageType accordingly.
			if(name.charAt(dotIndex)== '.')
			{
				// The three letters after the '.'
				imageType = name.substring(dotIndex + 1).toUpperCase();
				outputfile = new File(name);
			}
			else// Assume no ending file name has been given.
			{
				// Default it PNG.
				imageType = "PNG";
				outputfile = new File(name+"."+imageType.toLowerCase());
			}
			
			// Finally at the end write the image to the file
		    ImageIO.write(image, imageType, outputfile);
			
		}// End of try block 
		catch (IOException e)
		{
			System.out.println("saveImage function has failed in the Brenderer");
		}
	}
	
	
	// This is a fantastic function that I found on the Internet that optimizes buffered images and makes them secure and safe for drawing onto a screen.
	@SuppressWarnings("unused")
	private BufferedImage toCompatibleImage(BufferedImage image)
	{
	        // obtain the current system graphical settings
	        GraphicsConfiguration gfx_config = GraphicsEnvironment.
	                getLocalGraphicsEnvironment().getDefaultScreenDevice().
	                getDefaultConfiguration();

	        /*
	         * if image is already compatible and optimized for current system 
	         * settings, simply return it
	         */
	        if (image.getColorModel().equals(gfx_config.getColorModel()))
	                return image;

	        // image is not optimized, so create a new image that is
	        BufferedImage new_image = gfx_config.createCompatibleImage(
	                        image.getWidth(), image.getHeight(), image.getTransparency());

	        // get the graphics context of the new image to draw the old image on
	        Graphics2D g2d = (Graphics2D) new_image.getGraphics();

	        // actually draw the image and dispose of context no longer needed
	        g2d.drawImage(image, 0, 0, null);
	        g2d.dispose();

	        // return the new optimized image
	        return new_image; 
	}

	// Maps a region of Color c1 to Color c2.
	public static void floodFill(BufferedImage sprite, int x_start, int y_start, Color color_2)
	{
		// Initial Color.
		int c1 = sprite.getRGB(x_start, y_start);
		
		// Processed Color.
		int c2 = color_2.getRGB();
		
		Stack<Integer> S = new List<Integer>();
		
		S.push(x_start);
		S.push(y_start);
		
		while(!S.isEmpty())
		{
			int y = S.pop();
			int x = S.pop();
			
			int c;
			
			// Utilize the JAVA array bounds checks for edge cases.
			try
			{
				c = sprite.getRGB(x, y);
			}
			catch(Exception e)
			{
				continue;				
			}
			
			// Base Case.
			if(c != c1)
			{
				continue;
			}
			
			// Convert the pixel to the second color.
			sprite.setRGB(x, y, c2);

			// Up
			S.push(x);
			S.push(y + 1);

			// Down
			S.push(x);
			S.push(y - 1);
			
			// Left
			S.push(x - 1);
			S.push(y);
			
			// Right
			S.push(x + 1);
			S.push(y);

		}
	}
	
}
