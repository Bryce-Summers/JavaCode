package Project;


import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import Game_Engine.Engine.Objs.Obj;

/*
 * This class stores the proper image.
 */

public class ImageDisplay extends Obj
{

	// -- Constructor.
	public ImageDisplay(double x, double y, int w, int h)
	{
		super(x, y);
		
		setSprite(ImageFactory.blank(w, h));
		//setW(w);
		//setH(h);
		setDepth(5);
			
	}
	
	@Override
	protected void update()
	{
		// TODO Auto-generated method stub
	}

	public BufferedImage getSprite()
	{
		return sprite;
	}

}
