package Game_Engine.levelEditor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;

import util.FileIO;
import util.interfaces.Function;
import BryceMath.Calculations.Colors;
import Data_Structures.Structures.BitSet;
import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_input;
import Game_Engine.Engine.engine.Game_output;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.Input.gui_booleanInput;
import Game_Engine.GUI.Components.large.gui_array;
import Game_Engine.GUI.Components.large.gui_list;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.SpriteFactories.ArrowImageFactory;
import Game_Engine.levelEditor.editor_components.EntitySelector;
import Game_Engine.levelEditor.editor_components.gui_level_editor;
import Game_Engine.levelEditor.editor_components.obj_button;


/*
 * The main room for my Key locating utility.
 * 
 * Written by Bryce Summers on 8 - 13 - 2013.
 */
public abstract class room_editor extends Room
{
	// -- Constants.

	int game_w = getGameW();
	int game_h = getGameH();

	// The level to be modified.
	gui_level_editor level;

	// Various Utility Functions.
	gui_button grid_toggle;
	gui_button lights_toggle;

	gui_button save_level;
	gui_button load_level;
	
	gui_button run_level;

	static room_fileChooser chooser_room;

	gui_button clearRoom;

	gui_label label_mouse_x, label_mouse_y;
	
	gui_booleanInput edit_mode;
	
	private enum FileMode{save, load, run;}
		
	// File locating flags.
	FileMode fileMode = FileMode.save;
	boolean calling = false;
	
	// Arrow attributes.
	public static gui_array<gui_booleanInput> directions;
	
	
	// Level Graph interface.
	room_GameTree room_level_tree;
	gui_button button_level_tree;
	
	gui_button button_run_game;
	
	
	// -- The initial room constructor.
	public room_editor(Game_output out)
	{
		super(out);
	}
	
	public room_editor()
	{
		/* Used to implement generic rooms. */
	}

	// -- Initializes all of the objects that define this room.
	
	// Note : This program allows for the creation of 1080 by 800 sized game rooms.
	// Uses the remaining space on the left for a selector.
	
	@Override
	public void iObjs()
	{

		// Make sure these are accurate.
		game_w = getGameW();
		game_h = getGameH();
		
		// Compute dimensions.
		int w = getW();
		int h = getH();
		
		// The object selector list. 		
		gui_list obj_selector = new gui_list(0, 0, w - game_w, h);
		obj_create(obj_selector);
		
		// The options panel
		gui_window create_options = new gui_window(w - game_w, game_h, game_w, h - game_h);
		
		populate_option_buttons(create_options);
		
		obj_create(create_options);
		
		int border_size = SpriteLoader.gui_borderSize;
		
		// The Level display screen.
		level = createEditor(w - game_w - 2*border_size, 0, game_w + 2*border_size, game_h + 2*border_size);
		obj_create(level);
		
		// Import the specifications for the object list.
		EntitySelector selector_spec = getEntitySelector();
		
		selector_spec.populate_potential_objects(obj_selector);

	}
	
	// -- Abstract interface.
	
	public abstract gui_level_editor createEditor(double x, double y, int w, int h);

	public abstract EntitySelector getEntitySelector();

	// Provides the game width to the abstract room_editor.
	public abstract int getGameW();
	
	// Provides the game height to the abstract room_editor.
	public abstract int getGameH();
	
	// Should return the name desired of the folder that 
	// will store the save files and other generate data.
	// E.G. "Hoth_levels"
	protected abstract String getFileRootFolderName();

	// Should return the name after the dot for the file type of saved levels.
	// E.G. "Hoth"
	protected abstract String getLevelFileTypeName();
	
	// Returns a project specific game Tree room.
	protected abstract room_GameTree getGameTreeRoom(File level_directory, File save_location);
	
	protected abstract String getSavedFileLocation();
	
	protected abstract Room getMainRoom();
	
	// Define buttons that the user can use to change the display settings.
	public void populate_option_buttons(gui_window W)
	{
		grid_toggle = new gui_button(0, 0, 128, 64);
		grid_toggle.setText("Grid");
		W.obj_create(grid_toggle);
		
		lights_toggle = new gui_button(128, 0, 128, 64);
		lights_toggle.setText("Lights");
		W.obj_create(lights_toggle);
		
		// Import and export buttons.
		save_level = new gui_button(128, 64, 128, 64);
		save_level.setText("Save");
		W.obj_create(save_level);
		
		load_level = new gui_button(0, 64, 128, 64);
		load_level.setText("Open");
		W.obj_create(load_level);
		
		run_level = new gui_button(0, 128, 256, 64);
		run_level.setText("Run Level");
		run_level.setRestingColor(Colors.C_YELLOW);
		W.obj_create(run_level);
		
		chooser_room = new room_fileChooser(getLevelFileTypeName(), getFileRootFolderName());
		
		clearRoom = new gui_button(0, 192, 128, 64);
		clearRoom.setText("Clear");
		clearRoom.setRestingColor(Colors.C_ERROR);
		W.obj_create(clearRoom);
		
		label_mouse_x = new gui_label(256, 64, 128, 64);
		label_mouse_y = new gui_label(256 + 128, 64, 128, 64);
		W.obj_create(label_mouse_x);
		W.obj_create(label_mouse_y);
		
		// Saving reminder.
		/*
		gui_label b = new gui_label(256, 0, 512, 192);
		b.setText("Remember to Save!");
		b.setColor(Colors.C_ERROR);
		
		W.obj_create(b);
		*/

		createDirectionButtons(W);
		
		// Create A deselection Button.
		obj_button temp = new obj_button(128*3, 0, 128, 64);
		temp.setText("Deselect");
		W.obj_create(temp);
		
		// -- Create the edit mode button.
		edit_mode = new gui_booleanInput(128*2, 0, 128, 64);
		edit_mode.setText("Edit");
		edit_mode.setColors(Colors.C_GREEN, Colors.C_RED);
		W.obj_create(edit_mode);
		
		// Create the Game Tree.
		File level_file = new File(getFileRootFolderName());
		File saved_file = new File(getSavedFileLocation());
		room_level_tree = getGameTreeRoom(level_file, saved_file);
		button_level_tree = new gui_button(128, 192, 128, 64);
		button_level_tree.setText("Graph");
		W.obj_create(button_level_tree);
		
		
		button_run_game = new gui_button(256, 192, 128, 64);
		button_run_game.setText("Run Game");
		W.obj_create(button_run_game);
	}
	
	private void createDirectionButtons(gui_window W)
	{
		directions = new gui_array<gui_booleanInput>(512, 0, 64, 64, 4, 4, new foo());
		W.obj_create(directions);
		
		gui_booleanInput b;
		b = directions.getElem(0, 0);
		b.setText("All");
		
		BufferedImage[] arrows = ArrowImageFactory.getArrows(64);
		
		// Row buttons.
		for(int i = 1; i < 4; i++)
		{
			b = directions.getElem(i, 0);
			b.setImage(arrows[0]);
		}
		
		// Col buttons.
		for(int i = 1; i < 4; i++)
		{
			b = directions.getElem(0, i);
			b.setImage(arrows[6]);
		}
		
		// Direction arrows.
		b = directions.getElem(1, 1);
		b.setImage(arrows[3]);
		b = directions.getElem(1, 2);
		b.setImage(arrows[2]);
		b = directions.getElem(1, 3);
		b.setImage(arrows[1]);
		b = directions.getElem(2, 3);
		b.setImage(arrows[0]);
		b = directions.getElem(3, 3);
		b.setImage(arrows[7]);
		b = directions.getElem(3, 2);
		b.setImage(arrows[6]);
		b = directions.getElem(3, 1);
		b.setImage(arrows[5]);
		b = directions.getElem(2, 1);
		b.setImage(arrows[4]);
	}
	
	private class foo implements Function<Boolean, gui_booleanInput>
	{
		Color c1 = Colors.Color_hsv(126, 100, 100);
		Color c2 = Colors.Color_hsv(0, 100, 100);
		
		@Override
		public gui_booleanInput eval(Boolean input)
		{
			gui_booleanInput box = new gui_booleanInput(0, 0, 1, 1);
			box.setColors(c1, c2);
			return box;
		}
		
	}
	
	@Override	
	public void update()
	{
		super.update();
		
		label_mouse_x.setText("X: " + (Game_input.mouse_x - (int)level.getX())/16*16);
		label_mouse_y.setText("Y: " + (Game_input.mouse_y - (int)level.getY())/16*16);
		
		
		// Grid Action.
		if(grid_toggle.flag())
		{
			level.toggleGrid();
		}
		
				
		// Clear Action.
		if(clearRoom.flag())
		{
			level.killEntities();
		}		

		// --  Room change call switcher. 
		// Please See below the if statements below it for buttons that setup these calls.
		if(calling && chooser_room.getReturnData())
		{
			calling = false;
			
			File file = chooser_room.getFile();

			switch(fileMode)
			{
				case save:
					saveLevel(file);
					break;
				case load:
					openLevel(file);
					break;
				case run:
					saveLevel(file);
					room_level level = getRoomLevel(file);
										
					// Very important. Enables full animation.
					room_goto(level);
					level.enterFullAnimationMode();
			}
			
			return;
		}
		
		// Sets up a call to the dispatcher in the above code.
		if(save_level.flag())
		{			
			fileMode = FileMode.save;
			chooser_room.call(this);
			chooser_room.getChooser().refresh();
			chooser_room.saveMode();
			calling = true;
			return;
		}

		// Sets up a call to the dispatcher in the above code.
		if(load_level.flag())
		{
			fileMode = FileMode.load;
			chooser_room.call(this);
			chooser_room.getChooser().refresh();
			chooser_room.openMode();
			calling = true;
			return;
		}
		
		// Sets up a call to the dispatcher in the above code.
		if(run_level.flag())
		{
			// Set flags, find file, then setup the  room change call switch below in this function.
			
			fileMode = FileMode.run;
			chooser_room.call(this);
			chooser_room.getChooser().refresh();
			// Save the level first.
			chooser_room.saveMode();
			calling = true;
			return;
		}
		
		// Sets up a call to the dispatcher in the above code.
		// Changes to the global level graph view.
		if(button_level_tree.flag())
		{
			room_level_tree.call(this);
		}
		
		for(int i = 1; i < 4; i++)
		{
			synchRow(i);
			synchCol(i);
		}
		
		synchAll();
		
		
		if(edit_mode.input_changed())
		{
			level.setEditMode(edit_mode.query());
		}
		
		// Running the game button.
		if(button_run_game.flag())
		{
			room_goto(getMainRoom());
			return;
		}
	}
	
	public void keyR(int key)
	{
		super.keyR(key);
		
		if(key == KeyEvent.VK_SPACE)
		{
			edit_mode.toggle();
		}		
	}
	
	private void synchRow(int r)
	{
		gui_booleanInput b = directions.getElem(r, 0);
		
		if(b.input_changed())
		{
			for(int c = 1; c < 4; c++)
			{
				directions.getElem(r, c).toggle();
			}
		}
	}
	
	private void synchCol(int c)
	{
		gui_booleanInput b = directions.getElem(0, c);
		
		if(b.input_changed())
		{
			for(int r = 1; r < 4; r++)
			{
				directions.getElem(r, c).toggle();
			}
		}
	}
	
	// Toggle all for the all button.
	private void synchAll()
	{
		gui_booleanInput b = directions.getElem(0, 0);
		
		if(b.input_changed())
		{
			for(int r = 1; r < 4; r++)
			for(int c = 1; c < 4; c++)
			{
				directions.getElem(r, c).toggle();
			}
		}
	}
	
	// Interface for receiving a concrete level implementation room.
	protected abstract room_level getRoomLevel(File file);

	protected abstract String getVersion();

	
	// The Root serialization function for implementation file types.
	// See the obj_levelDisplay class for the corresponding deserialization function.
	@rootSerial
	public void saveLevel(File file)
	{		
		// Create the save file.
		FileIO.createFile(file);
				
		// Extract the print stream.
		PrintStream stream = FileIO.getStream(file);
		
		// A message to potential readers.
		stream.println(level.getSerialName());
		
		// Print the version number.
		stream.println(getVersion());

		// Print a blank space for good measure.
		stream.println();

		// Serialize the level.
		level.serializeTo(stream);

		// Close the save file. This is somewhat important for safety reasons.
		FileIO.closeFile(file);
	}

	// The pointer function to the level opening function.
	public void openLevel(File file)
	{
		level.serializeFrom(file);
		setFile(file);
	}
	
	// Sets the corresponding file for the file chooser.
	private void setFile(File file)
	{
		chooser_room.setFile(file);
	}
	
	// Converts the boolean buttons into direction flag attributes.
	public static BitSet getDirectionAttribute()
	{
		BitSet output = new BitSet();
		
		for(int i = 0; i < 8; i++)
		{
			int r, c;
			
			switch(i)
			{
			default:
				case 0: r = 2; c = 3;break;
				case 1: r = 1; c = 3;break;
				case 2: r = 1; c = 2;break;
				case 3: r = 1; c = 1;break;
				case 4: r = 2; c = 1;break;
				case 5: r = 3; c = 1;break;
				case 6: r = 3; c = 2;break;
				case 7: r = 3; c = 3;break;
			}
			
			
			if(directions.getElem(r, c).query())
			{
				output.setBit(i, true);
			}
		}
		
		return output;
		
	}

}