package Game_Engine.GUI.Components.large;

import java.awt.event.KeyEvent;
import java.io.File;

import util.FileIO;
import BryceMath.Calculations.Colors;
import Data_Structures.Structures.HashingClasses.Set;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.Input.textBased.gui_StringInput;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Interfaces.Pingable;

/*
 * Graphic User interface File chooser list class.
 * 
 *  Written by Bryce Summers on 8 - 14 - 2013.
 *  
 *  Purpose : Allows the user to navigate through the program data tree to file locations.
 *  
 *  - Supports customizable extension finding.
 *  - Supports local directory navigation.
 *  
 *  - Supports the location of children file within directories.
 *  - Allows the user to specify new children file names.
 */

// FIXME : Upgrade this to a full File input class.
// FIXME : implement Input<File>
// FIXME : Create an extension specification box.
public class gui_fileChooser extends Obj_union implements Pingable
{
	// -- Local data.
	
	private File current_dir;
	private String current_child = null;
	
	private File start_dir;
	
	private Set<String> extensions = new Set<String>();
	private String current_extension;
	
	private boolean open_mode = true;

	
	// Tells the user what the filename_input does.
	gui_label	title;
	
	// The visual name that the user will see.
	public final String NAME = "File Navigator";
	
	// -- All of naming and operation components.
	// Lists directory changing buttons to navigate the directory tree.
	gui_list file_locator;
	
	// Tells the user what the filename_input does.
	gui_label	filename_label;
	
	// Allows the user to type in custom child locations.
	gui_StringInput filename_input;
	
	// Tells the user what the filename_input does.
	gui_label	file_extension_label;
	
	// Allows the user to signal to the outside world that an operation should be done.
	
	// External libraries can then extract the proper File. 
	gui_button	op_button;
	gui_button	new_folder_button;
	gui_button	cancel_button;
	
	// Constants.
	public static final int ROW_H = 100;
	public static final int TITLE_H = 50;
	
	// -- Constructors.
	public gui_fileChooser(double x, double y, int w, int h, File file)
	{
		super(x, y, w, h);
		
		if(file.isFile())
		{
			throw new Error("The file chooser should start within a directory.");
		}
		
		current_dir = file;
		start_dir = current_dir;
	}
	
	public gui_fileChooser(double x, double y, int w, int h, String ... path)
	{
		super(x, y, w, h);
		current_dir = FileIO.parseFile(path);
		
		if(current_dir.isFile())
		{
			throw new Error("The file chooser should start within a directory.");
		}
		
		start_dir = current_dir;
	}
	
	@Override
	public void iObjs()
	{
		int w = getW();
		int h = getH();
		
		// -- Create the file chooser heading. This displays the current directory.
		title = new gui_label(getX(), getY(), w, TITLE_H);
		title.setColor(Colors.C_BLUE_HEADING);
		obj_create(title);
		
		// -- Rev up the locator.
		file_locator = new gui_list(getX(), title.getY2(), w, h - ROW_H*2 - TITLE_H);
		file_locator.setColor(Colors.C_GRAY2);
		obj_create(file_locator);
		
		// Refresh the file_locator.
		refresh();
		
		int y2 = (int)getY2();
		
		int input_y = (int)y2 - ROW_H*2;
		
		// -- Create the operation and input components at the bottom.
		filename_label = new gui_label(0, input_y, w, ROW_H);
		filename_label.setText("File Name :");
		filename_label.fitToContents();
		filename_label.setColor(Colors.C_GRAY2);
		obj_create(filename_label);
		
		// -- Create the operation and input components at the bottom.
		file_extension_label = new gui_label(w - filename_label.getW(), input_y, filename_label.getW(), ROW_H);
		file_extension_label.setColor(Colors.C_GRAY2);
		obj_create(file_extension_label);
		
		
		int input_x = (int)filename_label.getX2();
				
		filename_input = new gui_StringInput(input_x, input_y, (int) (file_extension_label.getX() - input_x), ROW_H);
		filename_input.disableTEX();
		obj_create(filename_input);
		
		
		// -- Now create all of the bottom buttons.
		
		// The width of the bottom buttons.
		int button_w = w/3;
		int x = 0;
		int y = y2 - ROW_H;
		
		op_button = new gui_button(x, y, button_w, ROW_H);
		op_button.setText("Open File");
		obj_create(op_button);
		x += op_button.getW();
		
		new_folder_button = new gui_button(x, y, button_w, ROW_H);
		new_folder_button.setText("New Folder");
		obj_create(new_folder_button);
		x += new_folder_button.getW();
		
		cancel_button = new gui_button(x, y, button_w, op_button.getH());
		cancel_button.setText("Cancel");
		obj_create(cancel_button);
		x += cancel_button.getW();
	}
	
	/*
	 *  Refreshes the list to contain directories and
	 *  files with allowed extensions within the current directory.
	 */
	public void refresh()
	{
		// Refresh the Title.
		title.setText("Directory : " + current_dir.getName());
		
		file_locator.clear();
		
		// -- Create the parent button.
		File parent = current_dir.getParentFile();
		
		if(parent != null)
		{
			createListButton("[Parent] ", parent);
		}
		
		// Create a button for every file and directory in this directory.

		// Naturally there will be no sub files within a non directory file.
		if(current_dir.isFile())
		{
			return;
		}

		File[] sub_files = current_dir.listFiles();

		// Handle empty directories.
		if(sub_files == null)
		{
			gui_label l = new gui_label(0, 0, getW(), ROW_H);
			l.setText("[Empty Directory]");
			file_locator.add(l);
			return;
		}
		
		// First make all directories buttons.
		for(File f : sub_files)
		{
			if(f.isDirectory())
			createListButton(f);
		}
		
		// First make directory buttons.
		for(File f : sub_files)
		{
			if(properFile(f))
			createListButton(f);
		}
	}
	
	// Adds given extensions to the list of good extensions
	public void addExtensions(String ... allowed)
	{
		for(String ext : allowed)
		{
			extensions.set_add(ext);
			file_extension_label.setText("." + ext);
		}
	
		refresh();
	}
	
	// Adds given extensions to the list of good extensions
	public void removeExtensions(String ... not_allowed)
	{
		for(String ext : not_allowed)
		{
			extensions.rem(ext);
		}
		
		refresh();
	}
	
	/*
	 *  Returns true if and only if the given file has an extension that has been allowed through the allowed extensions list.
	 *	NOTE : This is reminiscent of the saveImage function in ImageUtil;
	 */		
	private boolean properFile(File file)
	{
		String extension = FileIO.getExtension(file);
		
		// The only supported extension.
		
		return extensions.includes(extension);
		
	}
	
	public void createListButton(File file)
	{
		createListButton(null, file);
	}
	
	public void createListButton(String prefix, File file)
	{
		int borderSize = SpriteLoader.gui_borderSize;
		
		fileButton b = new fileButton(0, 0, getW() - borderSize*2, ROW_H);
		file_locator.add(b);
		
		b.setData(file);
		
		if(prefix != null)
		{
			// Prepend the given prefix.
			b.setText(prefix + b.getText());
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(op_button.flag())
		{
			current_child = filename_input.getInput();
			
			// Emit an error if we try to open a non existent file or we try to create a file with no name.
			if(open_mode && !getFile().exists())
			{
				filename_input.ERROR("Error : Cannot find File!");
				return;
			}
			
			if(current_child.length() == 0)
			{
				filename_input.ERROR("Please enter a file Name!");
				return;
			}
			
			// Proper file.
			setFlag(true);
			return;
		}
		
		if(new_folder_button.flag())
		{
			current_child = filename_input.getInput();
			
			if(current_child.equals(""))
			{
				filename_input.ERROR("Invalid Folder Name");
				filename_input.setText("");
				return;
			}
			
			File file = getDirectory();
			
			if(file.exists())
			{
				filename_input.ERROR("Folder Already Exists");
				filename_input.setText("");
				return;
			}
			
			// Create this new directory.
			file.mkdirs();
			changeDir(file);
			
			filename_input.clearInput();
			
			return;
		}
	}
	
	/* 
	 * If the user clicks on a button that represents a directory,
	 * then the open room  reinitializes the list for the given sub directory.
	 * 
	 *  If the user clicks on a button that represents a Bryce Document Format file, then the file is opened.
	 */
	
	// -- Local class
	
	private class fileButton extends gui_button
	{
		// -- Local data.
		File myFile;
		
		public fileButton(double x, double y, int w, int h)
		{
			super(x, y, w, h);
			
			disableTEX();
			
		}
		
		public void setData(File file)
		{
			myFile = file;
			
			if(myFile.exists() == false)
			{
				//throw new Error("Something is wrong with my directory searching.");
			}
			
			setText(file.getName());
			
			if(myFile.isDirectory())
			{
				setRestingColor(Colors.C_BLUE_HEADING);
			}
		}

		public void update()
		{
			super.update();
			
			// Do nothing if the user has not clicked on this button.
			if(!flag())
			{
				return;
			}
			
			// Go to the indicated directory.
			if(myFile.isDirectory())
			{
				changeDir(myFile);
				return;
			}

			// We will assume that this is an existing file.
			filename_input.populateInput(FileIO.getChildName(myFile));
			
			
			// Send the signal for the indicated file to be opened, if we are in open mode.
			if(open_mode)
			{
				op_button.setFlag(true);
			}
		}
	}
	
	// Sends this file chooser's locator to a given directory.
	public void changeDir(File dir_new)
	{
		current_dir = dir_new;
		refresh();
		
		return;
	}
	
	// Sends this file chooser's locator to the initial starting directory.
	public void startDir()
	{
		changeDir(start_dir);
	}
	
	// Puts this file chooser into saving mode.
	public void saveMode()
	{
		op_button.setText("Save File");
		open_mode = false;
	}
	
	// Puts this file chooser into saving mode.
	public void openMode()
	{
		op_button.setText("Open File");
		open_mode = true;
	}
	
	public String getChild()
	{
		return current_child;
	}

	// Returns the current directory of this file chooser.
	public File getDir()
	{
		return current_dir;
	}
	
	public File getFile()
	{
		if(current_child == null)
		{
			return null;
		}
		
		// FIXME : Make sure the current_extension string is set properly.
		return FileIO.parseFile(current_dir, current_child + file_extension_label.getText());
	}
	
	public File getDirectory()
	{
		return FileIO.parseFile(current_dir, current_child);
	}

	private boolean flag = false;
	
	// Indicates that the Op button has been pressed.
	@Override
	public boolean flag()
	{
		if(flag)
		{
			flag = false;
			return true;
		}
		
		return false;
	}

	@Override
	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}
	
	public boolean getCancel()
	{
		return cancel_button.flag();
	}

	@Override
	public void keyP(int key)
	{
		
		// Allow the user to easily press enter after typing in a name.
		if(filename_input.isSelected() && key == KeyEvent.VK_ENTER)
		{
			op_button.setFlag(true);
		}
	}
	
	public void setFile(File file)
	{
		current_dir = file.getParentFile();
		current_child = FileIO.getChildName(file);
		
		filename_input.populateInput(current_child);
		
		refresh();
	}
	
}
