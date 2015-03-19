package Game_Engine.levelEditor.editor_components;

import java.io.PrintStream;

import util.SerialB;
import util.deSerialB;
import Data_Structures.ADTs.Bunch2;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.levelEditor.room_editor;

/* Handles the contents of levels.
 * 
 * 1. Allows for the user to insert objects.
 * 
 * 2. Handles the rendering of the objects inside of the game world.
 * 
 * 3. Saves the data into level files for use inside of the game.
 * 
 * 4. Handles the Editor's deserialization of Hoth level files. 
 * Allows for the creation of entities through 'arguments',
 * which currently are 2 dimensional Euclidean coordinates.
 */


public abstract class gui_level_editor extends gui_window implements SerialB, deSerialB
{
	
	static obj_grid grid_drawer;
	
	static obj_entity last = null;
	
	private boolean addMode = true;
	
	public gui_level_editor(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		
		grid_drawer = getGridObj();
		obj_create(grid_drawer);
		grid_drawer.setDepth(Integer.MIN_VALUE + 1);
	}
	
	public abstract obj_grid getGridObj();
	
	public void toggleGrid()
	{
		if(grid_drawer.isVisible())
		{
			grid_drawer.setVisible(false);
			return;
		}
		
		grid_drawer.setVisible(true);
	}
	
	// Entity creation.
	public void mouseP(int mx, int my)
	{
		super.mouseP(mx, my);
		
		// Only create from left clicks.
		if(!mouse_left())
		{
			return;
		}
		
		if(!addMode)
		{
			return;
		}
	
		mx = (int)(mx - getX() - 2);
		my = (int)(my - getY() - 2);
		
		Bunch2<Integer, Integer> snapCoords = grid_drawer.snapToGrid(mx, my);

		last = create_entity(snapCoords.getType1(), snapCoords.getType2());
		
	}
	
	// Mouse dragging allows for entities to be expanded.
	public void mouseD(int mx, int my)
	{
		super.mouseD(mx, my);
		
		// Do not do anything if there is no valid last entity.
		if(last == null)
		{
			return;
		}
		
		// Only create from left unclicks.
		if(!mouse_left())
		{
			return;
		}
		
		if(!addMode)
		{
			return;
		}
	
		mx = (int)(mx - getX() - 2);
		my = (int)(my - getY() - 2);
		
		Bunch2<Integer, Integer> snapCoords = grid_drawer.snapToGrid(mx, my);
		
		last.arg2(snapCoords.getType1(), snapCoords.getType2());
	}
	
	// Process initial arguments.
	private obj_entity create_entity(int mx, int my)
	{
		
		obj_entity e = obj_button.current_entity;
		
		// We cannot do a thing about null entities.
		if(e == null)
		{
			return null;
		}
		
		e = e.clone(mx, my);
		e.setDirectionsAttributes(room_editor.getDirectionAttribute());
		obj_create(e);
		
		redraw();
		
		return e;
		
	}

	// Serialization Functions.

	// Serialize all of their entities into level description files.
	@Override
	public void serializeTo(PrintStream stream)
	{
		List<Obj> Objs = world.getObjList();
		
		for(Obj o : Objs)
		{
			if(o instanceof obj_entity)
			{
				o.serializeTo(stream);
			}
		}
		
		serializeNonEntities(stream);
	}
	
	// Allow concrete classes to define arbitrary serialization of non entities.
	protected abstract void serializeNonEntities(PrintStream stream);

	
	@Override
	public String getSerialName()
	{
		return "Game Level";
	}


	public void killEntities()
	{
		List<Obj> Objs = world.getObjList();
		
		// First kill of all of the pre existing entities.
		for(Obj o : Objs)
		{
			if(o instanceof obj_entity)
			{
				o.kill();
			}
		}
	}
	
	// 
	public void setEditMode(boolean b)
	{
		obj_entity.setShowKnobs(b);
		
		addMode = !b;
	}

}
