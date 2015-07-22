package Project.Editor.Components;

import java.awt.Color;
import java.awt.image.BufferedImage;

import BryceImages.Rendering.StartRender;
import BryceMath.Calculations.Colors;
import Game_Engine.SpriteFactories.ArrowImageFactory;
import Project.Editor.sprites.ccGoal;
import Project.Editor.sprites.ccMover;
import Project.Editor.sprites.ccSquare;
import Project.Editor.sprites.ccTeleportTarget_Mover;
import Project.Editor.sprites.ccTeleportTarget_teleporter;
import Project.Editor.sprites.ccTeleporter;
import Project.Editor.sprites.ccUndo;
import Project.Editor.sprites.ccWall;
import Project.Editor.sprites.ccWallCorner;

/*
 * The Sprite Loader class for the Teleporters project.
 * Started 10 - 1 - 2013.
 * 
 * Updated on 4 - 24 - 2014.
 *  - Decomposed rendering into sub functions so that images that do not 
 *  - appear in the editor or the game will not be rendered unless needed.
 */

public class Spr
{

	// Textures
	public static BufferedImage ground_square, reset;

	public static BufferedImage[] wall = new BufferedImage[8];
	
	public static BufferedImage mover[] = new BufferedImage[5];
	public static BufferedImage goal[]  = new BufferedImage[5];
	
	public static BufferedImage arrow[]  = new BufferedImage[8];
	
	public static BufferedImage teleporter[] = new BufferedImage[5];
	public static BufferedImage teleport_target_teleporter[] = new BufferedImage[5];
	public static BufferedImage teleporter_target_mover[] = new BufferedImage[5];
	
	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);

	public static BufferedImage ground_square_move, ground_square_drag, ground_square_in_path;
	
	public static BufferedImage arrows[]  = new BufferedImage[8];

	public static Color[] colors_primary = new Color[5];

	// Renders the images that will be used in both the level editor and the game.
	public static void editor_and_game()
	{
		
		// Ground square images.
		ground_square = R.render(new ccSquare(64, 64, 28));// Orange brown.
		ground_square_move = R.render(new ccSquare(64, 64, 67));// Lemonade yellow.
		ground_square_drag = R.render(new ccSquare(64, 64, 173));// cyan
		ground_square_in_path = R.render(new ccSquare(64, 64, 124));// green kind of aqua. 
		
		// Straight wall pieces.
		for(int i = 0; i < 4; i++)
		{
			wall[i] = R.render(new ccWall(64, 64, i));
		}
		
		// Curved wall corners.
		for(int i = 4; i < 8; i++)
		{
			wall[i] = R.render(new ccWallCorner(64, 64, i - 4));
		}
		
		for(int i = 0; i < 5; i++)
		{
			colors_primary[i] = Colors.Color_hsv(i*72, 100, 100);
			mover[i] = R.render(new ccMover(64, 64, i));
			goal [i] = R.render(new ccGoal(64, 64, i));
			teleporter[i] = R.render(new ccTeleporter(64, 64, i));
			teleporter_target_mover[i] = R.render(new ccTeleportTarget_Mover(64, 64, i));
			teleport_target_teleporter[i] = R.render(new ccTeleportTarget_teleporter(64, 64, i));
		}
		
		reset = R.render(new ccUndo(64, 64));
		arrow = ArrowImageFactory.getArrows(32);

	}

	// Renders the images that will only be used by the level editor.
	public static void editor_only()
	{
		
	}
	
	// Renders the images that will only be used in the game.
	public static void game_only()
	{

	}
}