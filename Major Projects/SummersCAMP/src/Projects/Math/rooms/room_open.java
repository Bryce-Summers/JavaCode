package Projects.Math.rooms;

import java.io.File;

import util.FileIO;

import Game_Engine.Engine.Objs.room_functional;
import Game_Engine.GUI.Components.large.gui_fileChooser;

import Projects.Math.aamathMain;
import Projects.Math.functionalRooms.room_DocumentEditor;

/*
 * File Opening room.
 * 
 * Written by Bryce Summers on 8 - 14 - 2013.
 * 
 * Purpose : This room uses a gui_fileChooser to locate a file to be opened.
 * 
 */

public class room_open extends room_functional<Boolean>
{
	
	// -- Local data.
	private File current_dir;
	private gui_fileChooser chooser;
	
	// -- Constructor (s)
	
	public room_open(File start)
	{
		current_dir = start;
	}
	
	public room_open(String ... path)
	{
		current_dir = FileIO.parseFile(path);
	}
	
	@Override
	public void iObjs()
	{
		/*
		gui_roomChange menu_button = new gui_roomChange(0, 0, getW() - Spr.gui_borderSize, gui_fileChooser.TITLE_H, "room_menu");
		menu_button.setText("Goto Main Menu");
		menu_button.fitToContents();
		menu_button.setDepth(-1);
		obj_create(menu_button);
		*/
		
		chooser = new gui_fileChooser(0, 0, getW(), getH(), current_dir);
		chooser.setDepth(1);
		obj_create(chooser);
		
		// Allow the chooser to find .BDF files (Bryce Document Format).
		chooser.addExtensions(aamathMain.EXT_SAVE);		
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
		}
		
		// Do nothing if the user has not yet selected a proper file location.
		if(!chooser.flag())
		{
			return;
		}
		
		// Open up a new problem room and populate it with the data from the selected file.
		room_DocumentEditor r = new room_DocumentEditor(chooser.getFile());
		room_goto(r);
	}

}
