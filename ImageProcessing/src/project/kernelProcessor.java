package project;

import java.awt.Color;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.MathB;
import BryceMath.DoubleMath.Vector;

public class kernelProcessor 
{

	public static BufferedImage proccessImage(BufferedImage image, double[][] mask,
				int center_x, int center_y, double scalar)
	{
				
		int w = image.getWidth();
		int h = image.getHeight();

		BufferedImage output = ImageFactory.blank(image.getWidth(), image.getHeight());

		int mask_h = mask.length;
		int mask_w = mask[0].length;
		
		// Print out the mask.
		System.out.println("\n Normalizing scalar = " + scalar);
		for(int y = 0; y < mask_h; y++)
		{
			System.out.print("[");
			for(int x = 0; x < mask_w; x++)
			{
				System.out.print(mask[y][x] + ", ");
			}
			System.out.println("]");
		}
		System.out.println();
		

		for(int r = 0; r < h; r++)
		for(int c = 0; c < w; c++)
		{

			Vector color = new Vector(0, 0, 0);
			
			for(int x_in = 0; x_in < mask_w; x_in++)
			for(int y_in = 0; y_in < mask_h; y_in++)
			{
				color = color.add(getColor(image,   r + y_in - center_y,
													c + x_in - center_x).mult(mask[y_in][x_in]*scalar)
								);
			}
			
			setColor(output, r, c, color);
		}
		
		return output;

	}


	// Row and col can be out of bounds if desired, they are clamped into bounds.
	public static Vector getColor(BufferedImage image, int row, int col)
	{
		int w = image.getWidth();
		int h = image.getHeight();
		
		row = MathB.bound(row, 0,  h - 1);
		col = MathB.bound(col, 0,  w - 1);
		
		Color c = new Color(image.getRGB(col, row));
		
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		
		return new Vector(r, g, b);		
	}
	
	
	// row and column need to be in bounds.
	public static void setColor(BufferedImage image, int row, int col, Vector c)
	{	
		int r = (int)c.getX();
		int g = (int)c.getY();
		int b = (int)c.getZ();
		
		//System.out.println(r + " " + g + " " + b);
		
		r = MathB.bound(r,  0,  255);
		g = MathB.bound(g,  0,  255);
		b = MathB.bound(b,  0,  255);
		
		Color color = new Color(r, g, b);
		
		image.setRGB(col, row, color.getRGB());
	}
	
	public static double evaluateGaussian(double x, double mean, double s_dev)
	{
		double a = 1/(s_dev*Math.sqrt(2*Math.PI));
		
		double num = x-mean;
				
		return a * Math.exp(-num*num/(2*s_dev*s_dev));
	}
	
}
