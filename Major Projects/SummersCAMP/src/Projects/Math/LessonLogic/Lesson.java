package Projects.Math.LessonLogic;

import java.io.File;
import java.io.PrintStream;

import util.FileIO;
import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.large.gui_tree;
import Game_Engine.levelEditor.rootSerial;
import Projects.Math.Spr;

/*
 * Lesson class.
 * 
 * Written by Bryce Summers on 6 - 26 - 2013.
 * 
 * Methodology.
 * 
 * A lesson is a graphical list frames that represents the steps a user goes through to solve a problem.
 * 
 * Each frame stores a step in the problem solving process and implements all of the possible operations for forward progress.
 * 
 * A lesson contains a function that returns true iff a given state object of type T is a correct solution to the lesson.
 * 
 * The parameter T will be an object that represents a state.
 * 
 */

// An example of an advanced gui_list in the form of a gui_tree.
public class Lesson extends gui_tree
{	

	public Lesson(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		
		// Never hides the vertical scrollbar. 
		setDynamicVScroll(false);

		// Highlight the text boxes with a grey background.
		setColor(Spr.backgroundColor);
	}

	public Lesson(Rectangle screen)
	{
		super(screen);

		// Never hides the vertical scrollbar.
		setDynamicVScroll(false);

		// Highlight the text boxes with a grey background.
		setColor(Spr.backgroundColor);
	}
	
	public void stepBack()
	{
		if(size() == 1)
		{
			return;
		}

		rem();
	}
	
	// The magic function that saves a room instance via serialization.
	public void saveData(File file_dir, String ... filename)
	{
		File file = FileIO.parseFile(file_dir, filename);
		
		saveData(file);
	}
	
	// The magic function that saves a room instance via serialization.
	public void saveData(String ... filename)
	{
		File file = FileIO.parseFile(filename);
		
		saveData(file);
	}
	
	// The root serialization method for .bdf files.
	// look at room_blankProblem.class for the corresponding deSerialization method.
	@rootSerial
	public void saveData(File file)
	{		
		// Create the save file.
		FileIO.createFile(file);
				
		// Extract the print stream.
		PrintStream stream = FileIO.getStream(file);
		
		// A message to potential readers.
		stream.println("A Math save file for use with the Summers Computer Aided Math program, written by Bryce Summers");
		
		// Print the version number.
		stream.println("Version : 1");
		
		// Print a blank space for good measure.
		stream.println();

		// Serialize this tree.
		serializeTo(stream);

		// Close the save file. This is somewhat important for safety reasons.
		FileIO.closeFile(file);
	}
}
