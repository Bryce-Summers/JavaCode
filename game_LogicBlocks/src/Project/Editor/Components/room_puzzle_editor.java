package Project.Editor.Components;

import java.io.File;

import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_output;

import Game_Engine.levelEditor.room_GameTree;
import Game_Engine.levelEditor.room_editor;
import Game_Engine.levelEditor.room_level;
import Game_Engine.levelEditor.editor_components.EntitySelector;
import Game_Engine.levelEditor.editor_components.gui_level_editor;
import Project.aaTeleportPuzzle;
import Project.Game.rooms.room_puzzle_level;


/*
 * The concrete implementation of the room_editor class for the Hoth 3.0 project.
 * 
 * Abstracted by Bryce Summers on 6 - 19 - 2014.
 */
public class room_puzzle_editor extends room_editor
{
	
	private enum FileMode{save, load, run;}
	
	// File locating flags.
	FileMode fileMode = FileMode.save;
	boolean calling = false;
	
	// The initial room constructor.
	public room_puzzle_editor(Game_output out)
	{
		super(out);
	}
	
	public room_puzzle_editor()
	{
		/* Used to implement generic rooms. */
	}

	// -- Initializes all of the objects that define this room.
	
	// Note : This program allows for the creation of 1080 by 800 sized game rooms.
	// Uses the remaining space on the left for a selector.
	
	// -- Concrete implementations of the abstract interface.
	
	public gui_level_editor createEditor(double x, double y, int w, int h)
	{
		return new puzzle_editor(x, y, w, h);
	}
	
	@Override
	public EntitySelector getEntitySelector()
	{
		return new Entities();
	}
	
	// Provides the game width to the abstract room_editor.
	@Override
	public int getGameW()
	{
		return aaTeleportPuzzle.GAME_W;
	}
	
	// Provides the game height to the abstract room_editor.
	@Override
	public int getGameH()
	{
		return aaTeleportPuzzle.GAME_H;
	}

	@Override
	protected room_level getRoomLevel(File file)
	{
		return new room_puzzle_level(file);
	}

	@Override
	protected String getVersion()
	{
		return "Version 0";
	}

	// Returns the name of the file type files are saved as.
	@Override
	protected String getLevelFileTypeName()
	{
		// Example Save.
		return "TTT";
	}

	// Returns the name of the folder used to store levels.
	@Override
	protected String getFileRootFolderName()
	{
		return "TeleporterGameLevels";
	}

	@Override
	protected String getSavedFileLocation()
	{
		return "SavedGames";
	}
	
	@Override
	protected room_GameTree getGameTreeRoom(File level_directory, File saved_directory)
	{
		return new room_puzzle_gameTree(level_directory, saved_directory);
	}

	@Override
	protected Room getMainRoom()
	{
		return new Project.Game.rooms.room_main();
	}

}