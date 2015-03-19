package Projects.Math;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.StartRender;
import BryceMath.Calculations.Colors;
import Game_Engine.Engine.text.TextManager;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Sprites.ccCheckMark;
import Game_Engine.GUI.Sprites.ccCursor;
import Game_Engine.GUI.Sprites.ccGlobalCursor;
import Projects.Math.images.ccDot;
import Projects.Math.images.ccDown;
import Projects.Math.images.ccExchange;
import Projects.Math.images.ccDivide;
import Projects.Math.images.ccUndo;
import Projects.Math.images.Main.ccExit;
import Projects.Math.images.Main.ccHeart;
import Projects.Math.images.Main.ccHelp;
import Projects.Math.images.Main.ccNew;
import Projects.Math.images.Main.ccOpen;

/*
 * The Sprite Loader class for the SUMMERS CAMP project.
 */

public class Spr
{

	// -- The Game's Images.
	
	// -- This value is immensely important.
	public final static int gui_borderSize = SpriteLoader.gui_borderSize;
		
	// FIXME : Create a sprite manager class.
	
	// Mathematics symbols.
	public static BufferedImage exchange_symbol;
	public static BufferedImage divide_symbol;
	public static BufferedImage undo_symbol;
	public static BufferedImage down_arrow_symbol;
	public static BufferedImage cursor_symbol;
	public static BufferedImage global_cursor_symbol;

	public static BufferedImage check_mark_symbol;
	public static BufferedImage dot_symbol;
	
	public static BufferedImage icon_new;
	public static BufferedImage icon_open;
	public static BufferedImage icon_example;
	
	public static BufferedImage icon_help;
	public static BufferedImage icon_credits;
	public static BufferedImage icon_exit;
	
	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);

	// Render all of the images used in the Summers CAMP program.
	public static void render1()
	{
		
		exchange_symbol = R.render(new ccExchange(45, 45));
		divide_symbol 	= R.render(new ccDivide(40, 40));
		undo_symbol		= R.render(new ccUndo(50, 50));

		down_arrow_symbol = R.render(new ccDown(50, 50));
		
		cursor_symbol	= R.render(new ccCursor(50, 50));
		dot_symbol		= R.render(new ccDot(50, 50));
		
		global_cursor_symbol = R.render(new ccGlobalCursor(50, 50));

		check_mark_symbol = R.render(new ccCheckMark(50, 50));
		
		icon_new  = R.render(new ccNew (50, 50));
		icon_open = R.render(new ccOpen(50, 50));
		icon_example = ImageFactory.blank(50, 50);
		TextManager.drawTextCenter((Graphics2D)icon_example.getGraphics(), null, 25, 25, "Ex", false);
		
		// -- Help icon is a composite image.
		icon_help = R.render(new ccHelp(50, 50));
		TextManager.drawTextCenter((Graphics2D)icon_help.getGraphics(), null, 25, 25, "?", false);
				
		icon_exit = R.render(new ccExit(50, 50));
		
		icon_credits = R.render(new ccHeart(50, 50));
			
	}
	
	// Designer colors for this Mathematics program.
	
	// A form of grey.
	public static Color backgroundColor = Colors.C_GRAY2;
	
}
