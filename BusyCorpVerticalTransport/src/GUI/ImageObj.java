package GUI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class ImageObj extends OBJ2D
{

	public static int NO_IMAGE = -1;
	
	private BufferedImage[] images;
	private int image_index = 0; 
	
	public ImageObj(int x, int y, BufferedImage ... images)
	{
		super(x, y);
		
		this.images = images;
		
		image_index = images == null 
						? NO_IMAGE
						: 0;
	}
	
	@Override
	public void draw(Graphics g)
	{
		if(image_index != NO_IMAGE)
		g.drawImage(images[image_index], x, y, null);
	}

	@Override
	public abstract void update();
	
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
		return images[image_index].getWidth();
	}
	
	public int getH()
	{
		return images[image_index].getHeight();
	}
}
