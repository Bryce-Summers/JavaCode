package BryceImages.Rendering;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;


/*
 * The Compositor class.
 * 
 * Written by Bryce Summers on 4 - 24 - 2014.
 * 
 * Purpose : The compositor class will provide methods to render images by combining source images.
 * 
 * - See http://docs.oracle.com/javase/7/docs/api/java/awt/AlphaComposite.html for Apha_composite codes.
 */

public class Compositor
{
	// -- Different flavors of compositing.

	// REQUIRES : two buffered images.
	// ENSURES : Returns a new bufferedimage that is the result of drawing the top buffered image to the bottom bufferedimage.
	public static BufferedImage getImage(BufferedImage top, BufferedImage bottom)
	{
		throw new Error("Implement Me please!");
	}
	
	public static BufferedImage getImage(BufferedImage top, BufferedImage bottom, int Apha_composite_code)
	{
		throw new Error("Implement Me please!");
	}
	
	public static BufferedImage getImage(BufferedImage top, ColorCalculator bottom)
	{
		throw new Error("Implement Me please!");
	}
	
	public static BufferedImage getImage(ColorCalculator top_cc, int x, int y, BufferedImage bottom)
	{
		// Render the top color calculator.
		StartRender R = new StartRender(true);
		BufferedImage top = R.render(top_cc);
		
		// Create the output image from a copy of the bottom image.
		BufferedImage output = (BufferedImage) copyImage(bottom);
		Graphics g = output.getGraphics();
		
		// Draw the top image to the desired location on the output.
		g.drawImage(top, x, y, null);
		
		return bottom;
	}
	
	public static BufferedImage getImage(ColorCalculator top, ColorCalculator bottom)
	{
		throw new Error("Implement Me please!");
	}
	
	
	// -- Helper methods 
	// http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	static BufferedImage copyImage(BufferedImage bi)
	{
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
		
	
}
