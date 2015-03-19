package BryceImages.Operations;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import BryceMath.Calculations.Colors;


/*
 * The Simple Image generation class.
 * 
 * Written by Bryce Summers on 87 - 11 - 2013.
 * 
 * This class allows for the generation of simple images such as rectangles of a single color.
 * This class also allows for me to standardize which type of memory footprint I use for each one of my images.
 */

public class ImageFactory
{

	public static BufferedImage ColorRect(Color c, int w, int h)
	{
		// Instantiate the image.
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		
		// Do not waste time drawing if the color is clear.
		if(c.equals(Colors.C_CLEAR))
		{
			return output;
		}
		
		// Extract a pointer to the image's drawing surface.
		Graphics g = output.getGraphics();
		
		// Set the Graphics context to the proper color.
		g.setColor(c);
		
		// Draw the background color onto the image.
		g.fillRect(0, 0, w, h);
		
		return output;
	}
	
	public static BufferedImage blank(int w, int h)
	{
		return new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
	}
	
	// http://sanjaal.com/java/269/java-graphics-2d/flipping-an-image-horizontally-and-vertically-in-java/
	public static BufferedImage horizontalReflection(BufferedImage img)
	{
	       int w = img.getWidth();
	        int h = img.getHeight();
	        BufferedImage dimg = new BufferedImage(w, h, img.getType());
	        Graphics2D g = dimg.createGraphics();
	        /**
	         * img - the specified image to be drawn. This method does nothing if
	         * img is null. dx1 - the x coordinate of the first corner of the
	         * destination rectangle. dy1 - the y coordinate of the first corner of
	         * the destination rectangle. dx2 - the x coordinate of the second
	         * corner of the destination rectangle. dy2 - the y coordinate of the
	         * second corner of the destination rectangle. sx1 - the x coordinate of
	         * the first corner of the source rectangle. sy1 - the y coordinate of
	         * the first corner of the source rectangle. sx2 - the x coordinate of
	         * the second corner of the source rectangle. sy2 - the y coordinate of
	         * the second corner of the source rectangle. observer - object to be
	         * notified as more of the image is scaled and converted.
	         *
	         */
	        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
	        g.dispose();
	        return dimg;
	}
	
	// http://sanjaal.com/java/269/java-graphics-2d/flipping-an-image-horizontally-and-vertically-in-java/
	public static BufferedImage verticalReflection(BufferedImage img)
	{
	       int w = img.getWidth();
	        int h = img.getHeight();
	        BufferedImage dimg = new BufferedImage(w, h, img.getColorModel()
	                .getTransparency());
	        Graphics2D g = dimg.createGraphics();
	        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
	        g.dispose();
	        return dimg;
	}
	
	// http://stackoverflow.com/questions/3967731/java-scale-image-best-practice
	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException
	{
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

	    return bilinearScaleOp.filter(
	        image,
	        new BufferedImage(width, height, image.getType()));
	}
	
	/**
	 * Rotates an image. Actually rotates a new copy of the image.
	 * 
	 * @param img The image to be rotated
	 * @param angle The angle in degrees
	 * @return The rotated image
	 */
	public static BufferedImage rotate(BufferedImage img, double angle)
	{
	    double sin = Math.abs(Math.sin(Math.toRadians(angle))),
	           cos = Math.abs(Math.cos(Math.toRadians(angle)));

	    int w = img.getWidth(null), h = img.getHeight(null);

	    int neww = (int) Math.floor(w*cos + h*sin),
	        newh = (int) Math.floor(h*cos + w*sin);

	    BufferedImage bimg = blank(neww, newh);
	    Graphics2D g = bimg.createGraphics();

	    g.translate((neww-w)/2, (newh-h)/2);
	    g.rotate(Math.toRadians(angle), w/2, h/2);
	    g.drawRenderedImage(img, null);
	    g.dispose();

	    return bimg;
	}
}
