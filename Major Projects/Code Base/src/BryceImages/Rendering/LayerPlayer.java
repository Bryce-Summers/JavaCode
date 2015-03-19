package BryceImages.Rendering;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;

/*
 * LayerPlayer. Started by Bryce Summers on 5 - 30 - 2014.
 * 
 * This class is meant to provide a dynamic animation framework for rendering incrementally drawn animations.
 * 
 * This class should implement multiple layered color calculators, function drawing, 
 * incremental stepwise updates via moving the pixes left, right, up, or down and filling in the new regions.
 * 
 * This will be used for my railroad animation in the future.
 * 
 * FIXME : This class might be too slow for practical animation purposes.
 */

public class LayerPlayer extends Obj
{
	// The region of the color calculator being rendered.
	// The region of the screen that the final image will be drawn to.
	private DynamicColorCalculator dcc;
	
	// The pixel velocity, 
	// the number of rows or columns of pixels that the image will be shifted after each frame.
	private double v_x = 0;
	private double v_y = 0;
	
	private double i_x = 0, i_y = 0;
	
	static StartRender rend = new StartRender(true);
	
	BufferedImage buffer;
		
	public LayerPlayer(double x, double y, DynamicColorCalculator dcc)
	{
		super(x, y);	

		this.dcc = dcc;
		
		buffer = ImageFactory.blank(dcc.getWidth(), dcc.getHeight());
		renderAll();
	}

	// Updates the image by 1 step according to the pixel velocity.
	// image updates get rounded to the nearest integers.
	@Override
	public void update()
	{
		//shiftPicture(v_x, v_y);
	}
	
	@Override
	public void draw(ImageB i, AffineTransform AT)
	{
		shiftPicture(v_x, v_y);
		super.draw(i, AT);
	}
	
	public void setImageV(double vx, double vy)
	{
		v_x = vx;
		v_y = vy;
	}
	
	public void setImagePosition(double x, double y)
	{
		shiftPicture(x - i_x, y - i_y);
		
		i_x = x;
		i_y = y;
	}
		
	private void shiftPicture(double x_offset, double y_offset)
	{
		// Compute new floating point coordinates coordinates.
		double x_new = i_x + x_offset;
		double y_new = i_y + y_offset;
		
		// Compute how many pixels have changed when we round it to integral positions.
		int xshift = (int)x_new - (int)i_x;
		int yshift = (int)y_new - (int)i_y;
		
		i_x = x_new;
		i_y = y_new;
		
		dcc.incX(xshift);
		dcc.incY(yshift);

		// Extract current dimensional data.
		int w = sprite.getWidth();
		int h = sprite.getHeight();
		
		// Use parallel rendered if the shifts are rather large.
		if(xshift > w/2 || yshift > h/2)
		{
			renderAll();
			return;
		}
		
		
		// Shift the image via drawing.
		Graphics g = buffer.getGraphics();
		g.drawImage(sprite, -xshift, -yshift, null);

		boolean bottom = yshift > 0;
		
		// These are used to limit the bounds of how much the right and left regions are drawn to eliminate
		// the intersections of the regions from being drawn multiple times.
		int y_start = bottom ? 0 : -yshift;
		int y_end	= bottom ? h - yshift : h;
		
		// -- Compute the new empty data.
		// Right.
		for(int x = w - xshift; x < w; x++)
		{
			BufferedImage col = dcc.getCol(x);
			g.drawImage(col, x, 0, null);
		}
		
		// Left.
		for(int x = 0; x > xshift; x--)
		{
			BufferedImage col = dcc.getCol(-x);
			g.drawImage(col, -x, 0, null);
		}
		
		// bottom
		for(int y = h - yshift; y < h && y >= y_start && y <= y_end; y++)
		{
			BufferedImage row = dcc.getRow(y);
			g.drawImage(row, 0, y, null);
		}
		
		// Top.
		for(int y = 0; y > yshift && y >= y_start && y <= y_end; y--)
		{
			BufferedImage row = dcc.getRow(y);
			g.drawImage(row, 0, -y, null);
		}
		
		// Swap the sprite with the buffer.
		BufferedImage temp = sprite;
		sprite = buffer;
		buffer = temp;
	}
	
	private void renderAll()
	{
		sprite = rend.render(dcc);
	}

}
