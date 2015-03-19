package Game_Engine.GUI;

import java.awt.Color;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.StartRender;
import Game_Engine.GUI.Sprites.StyleSpec;
import Game_Engine.GUI.Sprites.ccCheckMark;
import Game_Engine.GUI.Sprites.ccGlobalCursor;
import Game_Engine.GUI.Sprites.ccGui_corner;
import Game_Engine.GUI.Sprites.ccGui_horiEdges;
import Game_Engine.GUI.Sprites.ccGui_vertEdges;



/*
 * The Sprite Loader class.
 * Purpose: Specifies a set of customized buffered images to be loaded for a vanilla version of Bryce GUI.
 */

public class SpriteLoader
{

	// -- The Game's Images.
	
	// -- This value is immensely important.
	public final static int gui_borderSize = 2;
	
	public static BufferedImage gui_horiEdges;
	public static BufferedImage gui_horiEdges2;
	public static BufferedImage gui_vertEdges;
	public static BufferedImage gui_vertEdges2;
	public static BufferedImage gui_corner;
	
	public static BufferedImage gui_horiEdges_hl;
	public static BufferedImage gui_horiEdges_hl2;
	public static BufferedImage gui_vertEdges_hl;
	public static BufferedImage gui_vertEdges_hl2;
	public static BufferedImage gui_corner_hl;
		
	// FIXME : Create a sprite manager class.
	
	// Mathematics symbols.
	public static BufferedImage exchange_symbol;
	public static BufferedImage divide_symbol;
	public static BufferedImage undo_symbol;
	public static BufferedImage cursor_symbol;
	public static BufferedImage global_cursor_symbol;

	public static BufferedImage check_mark_symbol;
	public static BufferedImage dot_symbol;
	
	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);

	// Functions that load specific BufferedImages at any given time in the game.
	public static void render1()
	{

		Color c1 = StyleSpec.C_BUTTON_UP;
		Color c2 = StyleSpec.C_BUTTON_DOWN;
				
		//StyleSpec.defineBorderColors(Color.BLACK, Color.white, c2);
		StyleSpec.defineBorderColors(Color.black, Color.black, c1);
		
		// Graphic User interface Images.
		gui_horiEdges	= R.render(new ccGui_horiEdges(1, gui_borderSize));
		gui_vertEdges	= R.render(new ccGui_vertEdges(gui_borderSize, 1));
		gui_corner		= R.render(new ccGui_corner(gui_borderSize*4, gui_borderSize*4));
		
		gui_horiEdges2 = ImageFactory.verticalReflection(gui_horiEdges);
		gui_vertEdges2 = ImageFactory.horizontalReflection(gui_vertEdges);
		
		StyleSpec.defineBorderColors(Color.black, c2, c2);
		//StyleSpec.defineBorderColors(c1, Color.BLACK, c1);
		
		
		// Highlighted Graphic User interface Images.
		gui_horiEdges_hl	= R.render(new ccGui_horiEdges(1, gui_borderSize));
		gui_vertEdges_hl	= R.render(new ccGui_vertEdges(gui_borderSize, 1));
		gui_corner_hl		= R.render(new ccGui_corner(gui_borderSize*4, gui_borderSize*4));
		
		gui_horiEdges_hl2 = ImageFactory.verticalReflection(gui_horiEdges_hl);
		gui_vertEdges_hl2 = ImageFactory.horizontalReflection(gui_vertEdges_hl);
		
		global_cursor_symbol = R.render(new ccGlobalCursor(50, 50));

		check_mark_symbol = R.render(new ccCheckMark(50, 50));
	}
	
}
