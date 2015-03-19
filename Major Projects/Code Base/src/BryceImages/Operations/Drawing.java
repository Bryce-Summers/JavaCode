package BryceImages.Operations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


/*
 * The Drawing class.
 * Written by Bryce Summers 1 - 4 - 2013.
 * Purpose: Provides functionality for drawing pixels onto Buffered images.
 */

public class Drawing
{
	// Draws A source image onto a destination image that is centered and is scaled to cover the maximal area.
	public static int[] draw_scaled(BufferedImage dest, BufferedImage src)
	{
		double percentage = subscribePercentage(dest, src);
		return draw_scaled(dest, src, percentage);
	}
	
	public static int[] draw_scaled(BufferedImage dest, BufferedImage src, double percentage)
	{
		return draw_scaled(dest, src, percentage, true);
	}
	
	// Draws A source image onto a destination image that is centered and is scaled to the given percentage.
	// Returns the coordinates of the left and right corners.
	public static int[] draw_scaled(BufferedImage dest, BufferedImage src, double percentage, boolean draw_black_borders)
	{
		int x_half = dest.getWidth()/2;
		int y_half = dest.getHeight()/2;
		
		int x1 = x_half - (int)(src.getWidth()*percentage/2);
		int y1 = y_half - (int)(src.getHeight()*percentage/2);
		
		int x2 = x_half + (int)(src.getWidth()*percentage/2);
		int y2 = y_half + (int)(src.getHeight()*percentage/2);
		
		Graphics g = dest.getGraphics();
		
		g.drawImage(src,x1, y1, x2, y2, 0, 0, src.getWidth(), src.getHeight(), null);

		if(draw_black_borders)
		{
				
			// Draw black borders.
			g.setColor(Color.black);
			// Top.
	        g.fillRect(0, 0, dest.getWidth(), y1);
	        // Bottom.
	        g.fillRect(0, y2,dest.getWidth(), y1);
	        // Left.
	        g.fillRect(0, 0, x1, dest.getHeight());
	        // Right.
	        g.fillRect(x2,0, x1, dest.getHeight());
		}
        
        
        // Return the top left, and bottom right coordinates of the scaled image.
        int [] result = new int[4];
        
        result[0] = x1;
        result[1] = y1;
        result[2] = x2;
        result[3] = y2;
        
        return result;
	}
	
	public static int[] draw_scaled(Graphics g, int w, int h, BufferedImage src)
	{
		return draw_scaled(g, w, h, src, subscribePercentage(new Dimension(w, h), toDim(src)));
	}
	// Draws A source image onto a destination Graphics context that is centered and is scaled to the given percentage.
	public static int[] draw_scaled(Graphics g, int w, int h, BufferedImage src, double percentage)
	{
		int x_half = w/2;
		int y_half = h/2;
		
		int x1 = x_half - (int)(src.getWidth()*percentage/2);
		int y1 = y_half - (int)(src.getHeight()*percentage/2);
		
		int x2 = x_half + (int)(src.getWidth()*percentage/2);
		int y2 = y_half + (int)(src.getHeight()*percentage/2);
		
		g.drawImage(src,x1, y1, x2, y2, 0, 0, src.getWidth(), src.getHeight(), null);

		// Draw black borders.
		g.setColor(Color.black);
		// Top.
        g.fillRect(0, 0, w, y1);
        // Bottom.
        g.fillRect(0, y2,w, y1);
        // Left.
        g.fillRect(0, 0, x1, h);
        // Right.
        g.fillRect(x2,0, x1, h);
        
        // Return the top left, and bottom right coordinates of the scaled image.
        int [] result = new int[4];
        
        result[0] = x1;
        result[1] = y1;
        result[2] = x2;
        result[3] = y2;
        
        return result;
	}

	// Draws a source image on the given graphics object with the given scalings.
	public static void draw_scaled(Graphics g, AffineTransform AT, int x, int y, BufferedImage src, int x_scale, int y_scale)
	{
		// First update these values correctly.
		x = (int) (x + AT.getTranslateX());
		y = (int) (y + AT.getTranslateY());
		
		draw_scaled(g, x, y, src, x_scale, y_scale);
	}
	
	// Draws a source image on the given graphics object with the given scalings.
	public static void draw_scaled(Graphics g, int x, int y, BufferedImage src, int x_scale, int y_scale)
	{
		/*
		if(true)
		{
			g.drawImage(src, x, y, null);
			return;
		}
		*/
		
		// Source height and widths.
		int sw = src.getWidth();
		int sh = src.getHeight();
		
		// Destination right and bottom bounds.
		int dx2 = x + sw*x_scale;
		int dy2 = y + sh*y_scale;
				
		g.drawImage(src, x, y, dx2, dy2, 0, 0, sw, sh, null);
    }
	
	public static void draw_sized(Graphics g, int x, int y, BufferedImage src, int w, int h)
	{
		g.drawImage(src, x, y, x + w, y + h, 0, 0, src.getWidth(), src.getHeight(), null);
	}
	
	// Helper functions.
	
	// Returns the scalar that will make the src dimension fit inside the dest dimension.
	private static double subscribePercentage(Dimension dest, Dimension src)
	{
		return Math.min(dest.getWidth()/src.getWidth(), dest.getHeight()/src.getHeight());
	}	
	
	// Returns the scalar that will make the dest dimension fit inside the src dimension.
	@SuppressWarnings("unused")
	private static double superscribePercentage(Dimension dest, Dimension src)
	{
		return subscribePercentage(src, dest);
	}
	
	// Returns the scalar that will make the src dimension fit inside the dest dimension.
	private static double subscribePercentage(BufferedImage dest, BufferedImage src)
	{
		return Math.min(dest.getWidth()/src.getWidth(), dest.getHeight()/src.getHeight());
	}	
	
	// Returns the scalar that will make the dest dimension fit inside the src dimension.
	@SuppressWarnings("unused")
	private static double superscribePercentage(BufferedImage dest, BufferedImage src)
	{
		return subscribePercentage(src, dest);
	}
	
	private static Dimension toDim(BufferedImage i)
	{
		int w = i.getWidth();
		int h = i.getHeight();
		return new Dimension(w, h);
	}
	
	// Returns a copy of the given transform that has been translated by the given values.
	public static AffineTransform translate(AffineTransform AT, double tx, double ty)
	{
		// Instantiate a new AF.
		AffineTransform result = new AffineTransform();
		
		// Translate it.
		result.setToTranslation(AT.getTranslateX() + tx, AT.getTranslateY() + ty);
		
		// Return it.
		return result;
	}
	
	// http://stackoverflow.com/questions/2825837/java-how-to-do-fast-copy-of-a-bufferedimages-pixels-unit-test-included?lq=1
	// This is a highly efficient method to "draw" a given src image onto a given destination image.
	public static void copySrcIntoDstAt(final BufferedImage src,
										 final BufferedImage dst,
										 final int dx,
										 final int dy)
	{
	    int[] srcbuf = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
	    int[] dstbuf = ((DataBufferInt) dst.getRaster().getDataBuffer()).getData();
	    int width = src.getWidth();
	    int height = src.getHeight();
	    int dstoffs = dx + dy * dst.getWidth();
	    int srcoffs = 0;
	    for (int y = 0 ; y < height ; y++, dstoffs+= dst.getWidth(), srcoffs += width )
	    {
	        System.arraycopy(srcbuf, srcoffs , dstbuf, dstoffs, width);
	    }
	}
	
	public static void drawTriangle(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3)
	{
		int[] xs = new int[3];
		int[] ys = new int[3];
		
		xs[0] = x1;
		xs[1] = x2;
		xs[2] = x3;
		
		ys[0] = y1;
		ys[1] = y2;
		ys[2] = y3;
		
		g.fillPolygon(xs, ys, 3);
	}
	
	
}
