package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*
 *
 * ImageObj class.
 *
 * Written by Bryce Summers.
 *
 * before 4/25/2015
 *
 * This is the root of my simple GUI system.
 * It allows the user to specify a rectangular region of space that is capable of
 * displaying colors or images.
 * 
 */

public abstract class ImageObj extends OBJ2D
{

	public static int NO_IMAGE = -1;
	
	private BufferedImage[] images;
	private int image_index = 0;
	
	Color color = null;
	
	int w = -1;
	int h = -1;
	
	int image_offset_x = 0;
	int image_offset_y = 0;
	
	public ImageObj(int x, int y, BufferedImage ... images)
	{
		super(x, y);
		
		this.images = images;
		
		// Trivial image array.
		if(images != null && images.length == 0)
		{
			throw new Error("You probably wanted to use the x, y, w, h constructor.");
		}
		
		image_index = images == null 
						? NO_IMAGE
						: 0;
	}
	
	public ImageObj(int x, int y, int w, int h)
	{
		super(x, y);
		images = null;
		this.w = w;
		this.h = h;
		
		image_index = NO_IMAGE;
	}
	
	@Override
	public void draw(Graphics g)
	{
		
		if(color != null)
		{
			g.setColor(color);
			g.fillRect(x, y, getW(), getH());
		}
		
		if(image_index != NO_IMAGE)
		{
			g.drawImage(images[image_index], x + image_offset_x, y + image_offset_y, null);
		}
		
	}
	
	@Override
	public abstract void update();
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setImage(int index)
	{
		/*
		if(image_index > images.length)
		{
			throw new Error("Illegal ImageArray Index: " + image_index);
		}*/
		
		image_index = index;

	}
	
	public int getW()
	{
		
		if(w != -1)
		{
			return w;
		}
		
		if(image_index != NO_IMAGE)
		{
			return images[image_index].getWidth();
		}
		
		return -1;
	}
	
	public int getH()
	{
		if(h != -1)
		{
			return h;
		}
		
		if(image_index != NO_IMAGE)
		{
			return images[image_index].getHeight();
		}
		
		return -1;
	}
	
	// -- Setter functions.
	public void setW(int w)
	{
		this.w = w;
	}
	
	public void setH(int h)
	{
		this.h = h;
	}
	
	
	public void setImageOffsetX(int x)
	{
		image_offset_x = x;
	}
	
	public void setImageOffsetY(int y)
	{
		image_offset_y = y;
	}
	
}