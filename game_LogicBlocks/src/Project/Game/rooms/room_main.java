package Project.Game.rooms;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;

import util.FileIO;
import util.deSerialB;
import util.interfaces.Function;
import BryceGraphs.Algorithms.GraphSerial;
import BryceGraphs.gui_components.gui_TreeViewer;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_output;
import Game_Engine.levelEditor.rootSerial;
import Project.aaTeleportPuzzle;
import Project.Game.Componenets.levelButton;


/*
 * Teleporter Game Main screen.
 * 
 * Seriously implemented on 8 - 20 - 2014 by Bryce Summers.
 */

public class room_main extends Room implements deSerialB
{
	
	levelButton root;
	gui_TreeViewer tree;
	
	final File myFile = FileIO.parseFile("SavedGames", "SavedGame.sav");
	
	// -- Constructors.
	public room_main()
	{
	}

	public room_main(Game_output out)
	{
		super(out);
	}

	@Override
	public void iObjs()
	{
		// Set the Game resolution.
		set_dimensions(aaTeleportPuzzle.GAME_W, aaTeleportPuzzle.GAME_H);
		
		// Create a tree of levels.
		serializeFrom(FileIO.parseFile("SavedGames", "SavedGame.sav"));
	}
	
	
	
	@Override
	public void keyP(int key)
	{
		/* Do nothing much yet */
	}

	// -- This class can parse filegraph files.
	@Override
	public String getExtension()
	{
		return "filegraph";
	}

	@Override
	public void serializeFrom(File input)
	{
		if(!input.exists())
		{
			throw new Error("File Graph file is missing!");
		}
		
		List<String> data = FileIO.readFile(input);
		
		Iterator<String> iter = data.iterator();
		
		// Peel off the level graph header line.
		iter.next();
		
		root = GraphSerial.deserialize(iter, new levelButtonFactory());

		// Construct a tree viewer
		tree = new gui_TreeViewer(0, 0, getW(), getH());
		obj_create(tree);
		
		// View the adjacency nodes.
		tree.editNode(root, root.getW(), root.getH());
		// Recursively fully expand the tree.
		tree.propogateNodes();
	}
	
	// The root of game tree serialization.
	@rootSerial
	public void serializeSavedGame()
	{
		File output = myFile;
		
		FileIO.createFile(output);
		PrintStream stream = FileIO.getStream(output);
		
		// Print the file heading.
		stream.println("Saved Game file. Version 1");
		
		// Serialize this file via recursive descent.
		tree.serializeTo(stream);
		FileIO.closeFile(output);
	}
	
	// LevelButtonFactory.
	private class levelButtonFactory implements Function<Iterator<String>, levelButton>
	{		
		@Override
		public levelButton eval(Iterator<String> input)
		{
			return levelButton.deserialize(input);
		}		
	}
}
