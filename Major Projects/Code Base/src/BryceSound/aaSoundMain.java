package BryceSound;

import java.awt.Dimension;
import java.awt.Toolkit;

import Game_Engine.Engine.engine.Game_output;


/*
 * This is a main routine that plays my waveforms.
 */


public class aaSoundMain
{
	
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args)
	{
		
		// Render the GUI images.
		Game_Engine.GUI.SpriteLoader.render1();
			
		// Create an output object for the game.
		Game_output out = new Game_output("Bryce Organizer!", dim);
		
		// Start the game.
		new room_main(out);
	}
	
}
