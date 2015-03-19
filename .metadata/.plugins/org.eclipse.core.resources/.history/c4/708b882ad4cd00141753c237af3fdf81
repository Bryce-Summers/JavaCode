package Projects.Math;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JApplet;

import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_looper;
import Game_Engine.Engine.engine.Game_output;
import Projects.Math.rooms.room_menu;
import util.FileIO;

/*
 * Main class, used to start up my Bryce CAMP mathematics program.
 * 
 * Project started by Bryce Summer in the year of 2013.
 * 
 * Informative comments written on 8 - 13 - 2013.
 * 
 * Purpose :
 * 
 *  	- This program provides an easy way for students to show
 * 		  their work on a computer without the need for paper and
 * 		  also provides functionality for reducing the computational
 *		  workload of students.
 */

public class aamathMain extends JApplet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
	static Room room;
	
	@Override
	public void init()
	{
		aamathMain.main(null);
	}
	
	@Override
	public void start()
	{
		room.start();
	}
	
	@Override
	public void stop()
	{
		room.stop();
	}
	
	
	public static void main(String[] args)
	{
		// Set the room directory for this program.
		Room.room_directory = "Projects.Math.rooms";
		
		// First Render the Game's images.
		Game_Engine.GUI.SpriteLoader.render1();
		Spr.render1();
		
		// For this application, use the winSize as given by the Operating system.
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
		// Test on smaller screens.
		//winSize = new Rectangle(0, 0, 1000, 1000);
		
		// Create an output object for the game.
		Game_output out = new Game_output("Summers CAMP", winSize);
		
		// FIXME : Note GUI mode is not entirely bulletproof yet.
		// Tag this application as a GUI application.
		Game_looper.GUI_DRAW_ENABLED = true;
		
		// Start the game and put the introductory animation screen into ping mode.
		room = new room_menu(out);
		
	}
	
	// -- Project external data directories;
	public static final File DIR_SAVES		= FileIO.parseFile("Saves");
	//public static final File DIR_HELP		= FileIO.parseFile("Help");
	public static final File DIR_PROBLEMS	= FileIO.parseFile("Problems");
	
	// -- Project file types.
	
	// Bryce Document format files, used to store assignments, demonstrations, and help tutorials.
	public static final String EXT_SAVE = "bdf";
	
	// THe dimensions for the look and feel of the program.
	
	// THe height of the rows for each frame.
	public static final int ROW_H = 75;
	
	// The height of the banner headings for each frame.
	public static final int BANNER_H = 50;

}
