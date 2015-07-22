package Project.Game.rooms;

import util.FileIO;
import util.Serializations;
import util.StringParser;
import util.deSerialB;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;


import Data_Structures.Structures.BitSet;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.Dict;
import Game_Engine.Engine.Objs.Room;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.levelEditor.room_editor;
import Game_Engine.levelEditor.room_level;
import Project.aaTeleportPuzzle;
import Project.Editor.Components.Spr;
import Project.Editor.Components.puzzle_editor;
import Project.Editor.Components.room_puzzle_editor;
import Project.Game.Componenets.guiBox;
import Project.Game.Componenets.levelButton;
import Project.Game.Componenets.obj_staticImages;
import Project.Game.objects.obj_goal;
import Project.Game.objects.obj_groundSquare;
import Project.Game.objects.obj_piece;
import Project.Game.objects.obj_teleport_target_mover;
import Project.Game.objects.obj_teleport_target_teleport;
import Project.Game.objects.obj_teleporter;


/* Level loading and playing room.
 *  
 * Written by Bryce Summers on 10 - 16 - 2013.
 * 
 * The room that specifies the loading of levels and provides the
 * container for the myriad of array of objects that interact to
 * form the game experience.
 * 
 * Updated 5/9/2014 : Wall fills are now centered on the glyph to prevent problems due to numerical errors in wall creation.
 */

public class room_puzzle_level extends room_level implements deSerialB
{
	// Private data.
	
	obj_staticImages scenery;

	gui_button back;
	levelButton l_button = null;
	
	// -- Constructor.
	public room_puzzle_level(File file)
	{
		super(file);
	}
	
	// -- In game call back constuctor.
	public room_puzzle_level(File file, levelButton l_button)
	{
		super(file);
		
		this.l_button = l_button;

	}
	
	
	public void update()
	{
		super.update();
		
		if(back.flag())
		{
			
			if(l_button == null)
			{
				returnToEditor();
				return;
			}
			
			if(levelSolved())
			{
				l_button.newScore(guiBox.getScore());
			}
			
			room_goto(l_button.getRoom());
		
		}
	}
	
	public boolean levelSolved()
	{
		return true;
	}
	
	
	// -- Concrete implementations of the abstract interface.
	
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
	
	// Returns a project particular room_editor;
	@Override
	public room_editor createEditorRoom()
	{
		return new room_puzzle_editor();
	}
	
	// Returns this project's menu room.
	@Override
	public Room createMenuRoom()
	{
		return new room_main();
	}
	
	@Override
	public String getExtension()
	{
		return "Hoth";
	}

	@Override
	public void iObjectHandlers()
	{
		scenery = new obj_staticImages(this);
		obj_create(scenery);
		
		guiBox box = puzzle_editor.createHud(this);
		
		back = new gui_button(box.getX2() - 110, box.getY(), 50, 50);
		back.setText("Done");
		back.setTextSize(12);
		obj_create(back);
		back.setDepth(box.getDepth() - 1);
		
	}
	
	// The Level initiation process.
	@Override
	public void serializeFrom(File file)
	{
		// Now parse the input file line by line.
		List<String> input    = FileIO.readFile(file);
		Iterator<String> iter = input.iterator();
		
		// "Hoth Game Level"
		iter.next();
		
		// Version Number.
		iter.next();
		
		// Blank Space.
		iter.next();
		
		while(iter.hasNext())
		{

		
			String s = iter.next();
			
			/****************
			 * Non Entities!!!
			 ***************/
			
			if(s.equals("obj_guiBox"))
			{
				guiBox.setGoal(new Integer(iter.next()));
				guiBox.disableEditing();
				continue;
			}
						
			/****************
			 * Entities!!!
			 ***************/
			
			// All entities have a dictionary of properties.
			Dict<String> dict = Serializations.deserial_dict(iter);

			
			if(s.startsWith("obj_piece"))
			{
				int index = StringParser.getEndDigit(s);
				
				// Extract data points.
				
				int x1 = new Integer(dict.lookup("x"));
				int y1 = new Integer(dict.lookup("y"));
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
				BitSet dir = new BitSet(dict.lookup("dir"));
			
				obj_piece p = new obj_piece(x1, y1, index, dir);
				p.setArg2(x2, y2);
				obj_create(p);
				
				continue;
			}
			
			if(s.equals("obj_ground_square"))
			{
				int x1 = new Integer(dict.lookup("x"));
				int y1 = new Integer(dict.lookup("y"));
				
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
			
				int grid_w = Spr.ground_square.getWidth();
				int grid_h = Spr.ground_square.getHeight();

				BitSet bs = new BitSet(dict.lookup("dir"));
				
				for(int x = x1; x <= x2; x += grid_w)
				for(int y = y1; y <= y2; y += grid_h)
				{
					obj_create(new obj_groundSquare(x, y, bs));
				}
				
				continue;
			}
			
			// Wall sprites.
			if(s.startsWith("obj_wall"))
			{
				int x1 = new Integer(dict.lookup("x"));
				int y1 = new Integer(dict.lookup("y"));
				
				int index = StringParser.getEndDigit(s);
			
				if(index >= 4)
				{
					scenery.addImage(Spr.wall[index], x1, y1);
					continue;
				}
				
				if(index % 2 == 0)
				{
					int y2 = new Integer(dict.lookup("y2"));
										
					BufferedImage spr = Spr.wall[index];
					
					int inc = spr.getHeight();
					
					for(int y = y1; y <= y2; y += inc)
					{
						scenery.addImage(Spr.wall[index], x1, y);
					}
					
					continue;
				}
				else
				{
					int x2 = new Integer(dict.lookup("x2"));
					
					BufferedImage spr = Spr.wall[index];
					
					int inc = spr.getHeight();
					
					for(int x = x1; x <= x2; x += inc)
					{
						scenery.addImage(Spr.wall[index], x, y1);
					}
					continue;
				}
			}
			
			// Goal objects.

			// Create individual object goals.
			if(s.startsWith("obj_goal"))
			{
				int index = StringParser.getEndDigit(s);
				
				int x1 = new Integer(dict.lookup("x"));
				int y1 = new Integer(dict.lookup("y"));
				
				int x2 = new Integer(dict.lookup("x2"));
				int y2 = new Integer(dict.lookup("y2"));
			
				int grid_w = Spr.goal[index].getWidth();
				int grid_h = Spr.goal[index].getHeight();
				
				for(int x = x1; x <= x2; x += grid_w)
				for(int y = y1; y <= y2; y += grid_h)
				{
					obj_create(new obj_goal(x, y, index));
				}
				
				continue;
			}
			
			if(s.startsWith("obj_teleporter_target_mover"))
			{
				int index = StringParser.getEndDigit(s);
				
				// Extract data points.
				
				int x1 = new Integer(dict.lookup("x"));
				int y1 = new Integer(dict.lookup("y"));
				BitSet dir = new BitSet(dict.lookup("dir"));
				
				obj_create(new obj_teleport_target_mover(x1, y1, index, dir));
				
				continue;
			}
			
			if(s.startsWith("obj_teleporter"))
			{
				int index = StringParser.getEndDigit(s);
				
				// Extract data points.
				
				int x1 = new Integer(dict.lookup("x"));
				int y1 = new Integer(dict.lookup("y"));
			
				obj_create(new obj_teleporter(x1, y1, index));
				
				continue;
			}
			
			if(s.startsWith("obj_teleport_target_teleporter"))
			{
				int index = StringParser.getEndDigit(s);
				
				// Extract data points.
				
				int x1 = new Integer(dict.lookup("x"));
				int y1 = new Integer(dict.lookup("y"));
				BitSet dir = new BitSet(dict.lookup("dir"));
			
				obj_create(new obj_teleport_target_teleport(x1, y1, index, dir));
				
				continue;
			}
		}

		// Post Processing should be performed here.
	}

}
