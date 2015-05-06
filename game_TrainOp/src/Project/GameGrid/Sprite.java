package Project.GameGrid;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*
 * An object that associates images with locations in space.
 */

public class Sprite
{

	int x, y;
	
	BufferedImage[] images;
	
	// -- Constructor.
	public Sprite(int x, int y, BufferedImage ... image)
	{
		this.x = x;
		this.y = y;
		this.images = image;
	}
	
	public void draw(Graphics g, Integer ... indices)
	{
		for(int index : indices)
		{
			g.drawImage(images[index], x, y, null);
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Sprite))
		{
			return false;
		}
		
		Sprite s = (Sprite)o;
		
		int len = images.length;
		for(int i = 0; i < len; i++)
		{
			if(images[i] != s.images[i])
			{
				return false;
			}
		}
		
		return s.x == x && s.y == y;
	}
}
