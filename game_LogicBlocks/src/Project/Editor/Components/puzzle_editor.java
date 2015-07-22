package Project.Editor.Components;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;

import util.FileIO;
import util.Serializations;
import util.StringParser;
import Data_Structures.Structures.BitSet;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.Dict;
import Game_Engine.Engine.Objs.Obj_Container;
import Game_Engine.levelEditor.editor_components.gui_level_editor;
import Game_Engine.levelEditor.editor_components.obj_grid;
import Game_Engine.levelEditor.entities.Ent_aaScalable;
import Project.Game.Componenets.guiBox;

/*
 * The Editor implementation for the Teleporting Time Travelers project.
 * 
 * decoupled from the abstract obj_editor type by Bryce Summers on 6 - 19 - 2014.
 */


public class puzzle_editor extends gui_level_editor
{

	// Private flags.
	//private boolean show_lights = true;
	
	// -- Constructor.
	public puzzle_editor(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		
		createHud(this);
	}

	@Override
	public String getExtension()
	{
		return "TTT";
	}
	
	// Specifies how to load a level into the level editor.
	@Override
	public void serializeFrom(File file)
	{
		killEntities();
		
		createHud(this).allowEditing();
		
		// Now parse the input file line by line.
		List<String> input    = FileIO.readFile(file);
		Iterator<String> iter = input.iterator();
		
		// "Game Level"
		iter.next();
		
		// Version Number.
		iter.next();
		
		// Blank Space.
		iter.next();
		
		while(iter.hasNext())
		{
			String entity_name = iter.next();

			/****************
			 * Non Entities!!!
			 ***************/
			
			if(entity_name.equals("obj_guiBox"))
			{
				guiBox.setGoal(new Integer(iter.next()));
				continue;
			}
						
			
			/****************
			 * Entities!!!
			 ***************/

			// All entities have a dictionary of properties.
			Dict<String> dict = Serializations.deserial_dict(iter);
			
			int x = new Integer(dict.lookup("x"));
			int y = new Integer(dict.lookup("y"));
			
			if(entity_name.startsWith("obj_piece"))
			{
				int index = StringParser.getEndDigit(entity_name);
				
				Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.mover[index], entity_name);
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
				o.arg2(x2, y2);
				o.setDirectionsAttributes(new BitSet(dict.lookup("dir")));
				obj_create(o);
				continue;
				
				//FIXME implement editor moving,
				// and resizing.
				// Implement directional properties for game objects.
				// Expand ground square jumping calculations. 
				// Enable blocks to be stuck together. 
				// Allow for blocks to be immovable. (Make colored walls!)
				
			}
			
			if(entity_name.startsWith("obj_teleporterPad"))
			{
				int index = StringParser.getEndDigit(entity_name);
				
				Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.teleporter[index], entity_name);
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
				o.arg2(x2, y2);
				o.setDirectionsAttributes(new BitSet(dict.lookup("dir")));
				obj_create(o);
				continue;
			}			
			
			if(entity_name.startsWith("obj_teleporter_target_mover"))
			{
				int index = StringParser.getEndDigit(entity_name);

				Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.teleporter_target_mover[index], entity_name);
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
				o.arg2(x2, y2);
				o.setDirectionsAttributes(new BitSet(dict.lookup("dir")));
				obj_create(o);
				continue;
			}
			
			if(entity_name.startsWith("obj_teleport_target_teleporter"))
			{
				int index = StringParser.getEndDigit(entity_name);
				
				Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.teleport_target_teleporter[index], entity_name);
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
				o.arg2(x2, y2);
				o.setDirectionsAttributes(new BitSet(dict.lookup("dir")));
				obj_create(o);
				continue;
			}
			
			if(entity_name.startsWith("obj_goal"))
			{
				int index = StringParser.getEndDigit(entity_name);
				
				Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.goal[index], entity_name);
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
				o.arg2(x2, y2);
				o.setDirectionsAttributes(new BitSet(dict.lookup("dir")));
				obj_create(o);
				continue;
			}
			
			if(entity_name.equals("obj_ground_square"))
			{
				Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.ground_square, entity_name);
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
				o.arg2(x2, y2);
				o.setDirectionsAttributes(new BitSet(dict.lookup("dir")));
				o.setDepth(5);
				obj_create(o);
				continue;
			}
			
			// Wall parsing.
			if(entity_name.startsWith("obj_wall"))
			{
				int index = StringParser.getEndDigit(entity_name);

				if(index >= 4)
				{
					Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.wall[index], entity_name);
					obj_create(o);
					continue;
				}
				
				Ent_aaScalable o = new Ent_aaScalable(x, y, Spr.wall[index], entity_name);
				
				Integer x2 = new Integer(dict.lookup("x2"));
				Integer y2 = new Integer(dict.lookup("y2"));
				
				o.arg2(x2, y2);
				obj_create(o);
				continue;

			}

			
		}// End of while loop.
	
		
		// Redraw the GUI engine.
		redraw();

	}

	// -- Specify the grid properties for this game.
	@Override
	public obj_grid getGridObj()
	{
		// No offset, 16 by 16 grid.
		return new obj_grid(32, 16, 64, 64);
	}
	
	// Allows for uniform HUD style.
	public static guiBox createHud(Obj_Container c)
	{
		guiBox b = new guiBox(c.getW() - 256 - 32 - guiBox.getBorderSize()*2, 16, 256, 64);
		b.allowEditing();
		c.obj_create(b);
		return b;
	}

	@Override
	protected void serializeNonEntities(PrintStream stream)
	{
		stream.println("obj_guiBox");
		stream.println(guiBox.getGoal());
	}

}
