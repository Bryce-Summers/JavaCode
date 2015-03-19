package Game_Engine.SpriteFactories;

import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.StartRender;
import Data_Structures.Structures.HashingClasses.AArray;
import Game_Engine.levelEditor.editor_components.sprites.ccArrowDown;


/*
 * A factory for producing arrow bufferedimages.
 * Updated on 6 - 27 - 2014.
 * 
 * A member of my standard sprite library.
 * 
 * FIXME : Abstract this into a rotated image production class.
 */

public class ArrowImageFactory
{

	// Arrow glyphs.
	public static AArray<Integer, BufferedImage[]> data = new AArray<Integer, BufferedImage[]>();

	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);

	// Renders the images that will only be used by the level editor.
	public static BufferedImage[] getArrows(int size)
	{
		BufferedImage[] arrows = data.lookup(size);
		
		if(arrows != null)
		{
			return arrows;
		}
		
		arrows = new BufferedImage[8];
		
		// Down arrow first naturally.
		arrows[6] = R.render(new ccArrowDown(size, size));
		arrows[7] = ImageFactory.rotate(arrows[6], -45);
		
		for(int i = 0; i < 6; i++)
		{
			arrows[i] = ImageFactory.rotate(arrows[6], -90 -45*i);			
		}
		
		// Save a copy for future use.
		data.insert(size, arrows);
		
		return arrows;
	}

}