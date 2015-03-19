package Game_Engine.levelEditor;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;

import util.FileIO;
import util.deSerialB;
import util.interfaces.Consumer2;
import util.interfaces.Function;
import BryceGraphs.ADTs.AdjacencyNode;
import BryceGraphs.ADTs.GNode;
import BryceGraphs.Algorithms.GraphSerial;
import BryceGraphs.gui_components.gui_EditableGNode;
import BryceGraphs.gui_components.gui_GNode;
import BryceGraphs.gui_components.gui_TreeViewer;
import BryceGraphs.gui_components.nodes.gnode_file;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.room_functional;
import Game_Engine.GUI.Components.small.gui_button;


/*
 * Game Tree. Fleshed out on 7 - 15 - 2014 by Bryce Summers.
 * 
 * Purpose : Provides a user friendly means of linking level dependencies together.
 * 
 * The room_GameTree defines the GNode factory, because it is the class that makes the 
 * decision to use gnode_files.
 * 
 * 
 * This class maintains a graph saved file that stores the abstract relationships between levels and 
 * also generates a saved game file for use in the game that may contain project specific metaData.
 */

public abstract class room_GameTree extends room_functional<File> implements deSerialB
{
	
	static int number = 0;
	
	// The location that stores the game tree.
	private File myDir, myFile;		// myDir = the location of the level graph data.
	private File mySaveDir, mySaveFile;  // myFile, the name of the file.
									// mySaveDir = the directory that the saved game file will be deposited.
	private final int ROW_H = 64;
	private gui_button back_button;
	
	gui_TreeViewer tree = null;
	gnode_file root = null;
	
	public room_GameTree(File directory, File saveDir)
	{
		myDir = directory;
		myFile = new File(myDir, "LevelGraph" + "." + getExtension());
		
		serializeFrom(myFile);
		
		mySaveDir = saveDir;
		mySaveFile = new File(mySaveDir, "SavedGame" + ".sav");
	}
	
	@Override
	public void iObjs()
	{
		gui_TreeViewer W = new gui_TreeViewer(0, ROW_H, getW(), getH() - ROW_H);
		obj_create(W);
		tree = W;
		
		W.editNode(getRootFile(), 350, 256);
		W.propogateNodes();
				
		
		back_button = new gui_button(0, 0, getW(), ROW_H);
		back_button.setText("Go Back to the Level Editor");
		obj_create(back_button);
	}
	
	public void update()
	{
		super.update();
		
		if(back_button.flag())
		{
			serializeGameTree(myFile);
			serializeSavedGame(mySaveFile);
			Return();
		}
	}

	
	// -- Serialization methods.
	
	
	// Graph files will be stored as .graph.
	// This class serializes and deserializes .filegraph files.
	// -- This class can parse filegraph files.
	@Override
	public String getExtension()
	{
		return "filegraph";
	}

	// Serialize data from the level graph file, if it exists.
	@Override
	public void serializeFrom(File input)
	{
		if(!input.exists())
		{
			return;
		}
		
		List<String> data = FileIO.readFile(input);
		
		Iterator<String> iter = data.iterator();
		
		// Peel off an test the first line.
		util.testing.ASSERT(iter.next().equals(getSerialName()));
		
		root = GraphSerial.deserialize(iter, new FileNodeFactory());
	}
	
	private gnode_file getRootFile()
	{
		if(root != null)
		{
			return root;
		}
		
		return new gnode_file(null, null, room_editor.chooser_room);
	}
	
	/* The room_GameTree defines the GNode factory, because it is the class that makes the
	 * decision to use gnode_files.
	 * 
	 * This factory knows how to deserialize gnode_files.
	 */	
	private class FileNodeFactory implements Function<Iterator<String>, gnode_file>
	{

		@Override
		public gnode_file eval(Iterator<String> data_stream)
		{
			return gnode_file.deserialize(data_stream, room_editor.chooser_room);
		}
		
	}
	
	// This name should be used during the serialization and deserialization
	// process for objects that could have an ordering of data in non predetermined types.
	
	// This is essentially an object's proffered type descriptor.
	public String getSerialName()
	{
		return "Game Tree File";
	}
	
	// The root of game tree serialization.
	@rootSerial
	private void serializeGameTree(File output)
	{
		FileIO.createFile(output);
		PrintStream stream = FileIO.getStream(output);
		
		// Print the file heading.
		stream.println(getSerialName());
		
		// Serialize this file via recursive descent.
		tree.serializeTo(stream);
		FileIO.closeFile(output);
	}
	
	// The root of game tree serialization.
	@rootSerial
	private void serializeSavedGame(File output)
	{
		FileIO.createFile(output);
		PrintStream stream = FileIO.getStream(output);
		
		// Print the file heading.
		stream.println("Saved Game file. Version 1");
		
		// Serialize this file via recursive descent.
		tree.serializeTo(stream, getSavedGameContinuation());
		FileIO.closeFile(output);
	}

	// Game projects need to give this class a function that allows abstract files to be
	// populated with game specific information such as player statistics and progress.
	protected abstract Consumer2<gui_GNode, PrintStream> getSavedGameContinuation();

}
