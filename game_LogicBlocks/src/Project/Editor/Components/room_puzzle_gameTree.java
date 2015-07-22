package Project.Editor.Components;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import util.interfaces.Consumer2;
import BryceGraphs.gui_components.gui_GNode;
import BryceGraphs.gui_components.nodes.gnode_file;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.levelEditor.room_GameTree;
import Project.Game.rooms.room_puzzle_level;

public class room_puzzle_gameTree extends room_GameTree
{

	// Constructor.
	public room_puzzle_gameTree(File directory, File saveDir)
	{
		super(directory, saveDir);
	}

	@Override
	protected Consumer2<gui_GNode, PrintStream> getSavedGameContinuation()
	{
		return new func();
	}
	
	// A function for adding saved metadata when serializing gnode_file types in the teleporter game.
	private class func implements Consumer2<gui_GNode, PrintStream>
	{
		// We need to append the user's current best score and the current goal.
		@Override
		public void eval(gui_GNode gui_gnode, PrintStream stream)
		{
			// User's best score is -1, because the level has not yet been attempted.
			stream.println("-1");
			
			// We exract a gnode_file from the gui_gnode.
			// We make this assumption based upon the internal room_gameTree types.
			gnode_file gnode = (gnode_file) gui_gnode.getGNode();
			
			// Search through the level file for the goal amount.
			File file = gnode.getFile();
			List<String> data = util.FileIO.readFile(file);
			Iterator<String> iter = data.iterator();
			while(iter.hasNext())
			{
				String s = iter.next();
				if(s.equals("obj_guiBox"))
				{
					stream.println(iter.next());
				}
			}
			
		}
		
	}

}
