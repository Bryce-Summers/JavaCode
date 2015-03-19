package Game_Engine.levelEditor;

import java.io.File;

import util.FileIO;
import Game_Engine.Engine.Objs.room_functional;
import Game_Engine.GUI.Components.large.gui_fileChooser;

/*
 * File Opening room.
 * 
 * Written by Bryce Summers on 8 - 14 - 2013.
 * 
 * Purpose : This room uses a gui_fileChooser to locate a file to be opened.
 * 
 */

public class room_fileChooser extends room_functional<Boolean>
{
	
	// -- Local data.
	private File root_dir, current_dir = null;
	private gui_fileChooser chooser;
	
	private final String extension;
	
		
	// -- Constructor (s)
	
	public room_fileChooser(String fileType, File start)
	{
		root_dir = start;
		extension = fileType;
	}
	
	public room_fileChooser(String fileType, String ... path)
	{
		root_dir = FileIO.parseFile(path);
		extension = fileType;
	}
	
	@Override
	public void iObjs()
	{
		chooser = new gui_fileChooser(0, 0, getW(), getH(), root_dir);
		chooser.setDepth(1);
		obj_create(chooser);
		
		// Allow the chooser to save the project specific level file.
		chooser.addExtensions(extension);
		
		if(current_dir != null)
		{
			chooser.setFile(current_dir);
		}
	}
	
	// Open a room found by the file chooser.
	@Override
	public void update()
	{
		super.update();

		// Go back to the previous room.
		if(chooser.getCancel())
		{
			Return(false);
			return;
		}
	
		if(chooser.flag())
		{
			Return(true);
			return;
		}
		
	}
	
	// Allows calling rooms to extract the file location.
	public File getFile()
	{
		return chooser.getFile();
	}
	
	public gui_fileChooser getChooser()
	{
		return chooser;
	}
	
	public void saveMode()
	{
		chooser.saveMode();
	}

	public void openMode()
	{
		chooser.openMode();
	}

	public void setFile(File file)
	{
		if(chooser != null)
		{
			chooser.setFile(file);
			return;
		}
		
		current_dir = file;
	}
	
}