package Game_Engine.Engine.Objs;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import BryceMath.Calculations.Colors;
import BryceMath.Geometry.Rectangle;



/*
 * The Bryce Image class.
 * 
 * written on 7 - 2 - 2013.
 * 
 * Purpose : This class will be used to aid in the efficient rendering of images for animations.
 * 
 * Capabilities : This class specifies an image and a drawable area.
 * 		Given these two pieces of data, this class specifies drawing functions that efficiently only draw to the image within the given bounds.
 * 
 *
 */

public class ImageB
{
	// -- Private Data.
	
	// A Java Image.
	private final BufferedImage image;
	
	// A Bryce rectangle.
	private final Rectangle bounds;
	
	// -- Constructors
	public ImageB(BufferedImage image_in)
	{
		image = image_in;
		bounds = getBounds(image);
	}
	
	public ImageB(BufferedImage image_in, Rectangle bounds_in)
	{
		image = image_in;
		
		// Do not allow bounds to be outside of the actual image.
		Rectangle full_bounds = getBounds(image);
		bounds = full_bounds.intersection(bounds_in);
	}
	
	// -- Data access methods.
	
	// Returns a tighter contsrained version of this Bryce Image.
	// Returns 0 if the resulting image has trivial bounds.
	public ImageB getSubImage(Rectangle sub_bounds)
	{
		sub_bounds = bounds.intersection(sub_bounds);
		
		// Return null if the given image has no available drawing area.
		if(sub_bounds.getW() < 1 || sub_bounds.getH() < 1)
		{
			return null;
		}
		
		return new ImageB(image, sub_bounds);
	}
	
	public Rectangle getBounds()
	{
		return (Rectangle) bounds.clone();
	}
	
	// -- Helper methods
	private Rectangle getBounds(BufferedImage image)
	{
		return new Rectangle(0, 0, image.getWidth(), image.getHeight());
	}
	
	public Graphics2D getGraphics()
	{
		Graphics2D g = (Graphics2D)image.getGraphics();
		
		// This is the amazing function that clips the drable area of a buffered image.
		g.setClip(new java.awt.Rectangle(bounds.getX(), bounds.getY(), bounds.getW(), bounds.getH()));
		return g;
	}

	// RGB retrieval with bounds checking.
	public int getRGB(int x, int y)
	{
		if(x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
		{
			return Colors.C_CLEAR.getRGB();
		}
		
		return image.getRGB(x, y);
	}
	
	public void setRGB(int x, int y, int c)
	{
		if(x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
		{
			throw new Error("Bad input coordinates!");
		}
		
		image.setRGB(x, y, c);
	}

	public Dimension getDimension()
	{
		return new Dimension(image.getWidth(), image.getHeight());
	}
	
	public int getW()
	{
		return image.getWidth();
	}
	
	public int getH()
	{
		return image.getHeight();
	}
	
}
