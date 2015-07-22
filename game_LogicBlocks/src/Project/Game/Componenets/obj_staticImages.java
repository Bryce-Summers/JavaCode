package Project.Game.Componenets;

import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.Obj_Container;

/*
 * Static image handler.
 * 
 * Written by Bryce Summers on 6 - 21 - 2014.
 * 
 * Purpose : This class maintains a buffered image that represents the aestetic background of a game level.
 * 			 various eye candy images, walls, and other objects that do not change can be added to here for efficient rendering 
 * 			 and to prevent them from wasting space and time in the object list.
 */

public class obj_staticImages extends Obj
{

	public obj_staticImages(Obj_Container world)
	{
		sprite = ImageFactory.blank(world.getW(), world.getH());
	}

	@Override
	protected void update()
	{
		/* not much to do, besides drawing the scenery. */
	}
	
	// Allows for images to be drawn to the scenery buffer.
	public void addImage(BufferedImage image, int x, int y)
	{
		sprite.getGraphics().drawImage(image, x, y, null);
	}

}
