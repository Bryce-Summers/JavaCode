package Project;



import java.awt.Dimension;
import java.awt.Toolkit;

import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_output;

/*
 * This class starts up the Spline visualizer.
 *
 * Use the mouse to create nodes, move tangents, etc.
 */

public class aaVisualizer
{

	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args)
	{
		// Set the room directory for this program.
		Room.room_directory = "Projects.";
		
		// Render the GUI images.
		Game_Engine.GUI.SpriteLoader.render1();
		
		// Create an output object for the game.
		Game_output out = new Game_output("Card Frame Renderer", dim);
		
		// Start the game.
		new room_main(out);
	}


	
	
}
