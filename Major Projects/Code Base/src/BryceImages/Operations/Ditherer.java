package BryceImages.Operations;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class Ditherer {

	public void dither(BufferedImage input)
	{
		
		
		int h = input.getHeight();
		int w = input.getWidth();
		
		// Note we can improve performance by switching the indexing of x and y.
		int[][] pixel = new int[w][h];
		
		// Get values.
		for(int y = 0; y < h; y++)
		for(int x = 0; x < w; x++)
		{
			Color c = new Color(input.getRGB(x, y));
			double red   = c.getRed()*.2126;
			double green = c.getGreen()*.7152;
			double blue	 = c.getBlue()*.0722;
			
			// Color to grayscale convertion.
			pixel[x][y] = (int)(red + green + blue);			
		}
		
		// Get values.
		for(int y = 0; y < h; y++)
		for(int x = 0; x < w; x++)
		{
		    int oldpixel  = pixel[x][y];
		    int newpixel  = 255*grayscale_to_monochrome(oldpixel);
		    int val = newpixel;
		    input.setRGB(x, y, new Color(val, val, val).getRGB());
		    
		    //int pixel[x][y]  := newpixel
		    	
		    int quant_error = oldpixel - newpixel;
		    
		    if(x < w-1)
		    pixel[x+1][y  ] = pixel[x+1][y  ] + quant_error * 7/16;
		    
		    if(y < h-1)
		    {
		    	if(x > 0)
		    	pixel[x-1][y+1] = pixel[x-1][y+1] + quant_error * 3/16;
		    	pixel[x  ][y+1] = pixel[x  ][y+1] + quant_error * 5/16;
		    	if(x < w-1)
		    	pixel[x+1][y+1] = pixel[x+1][y+1] + quant_error * 1/16;
		    }
		    
		    
		}
		

	}
	
	private int grayscale_to_monochrome(int val)
	{
		return val <= 50 ? 0 : 1;
	}

}
