package Game_Engine.Engine.Objs;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Game_Engine.Engine.engine.Game_input;
import Game_Engine.Engine.engine.Game_looper;
import Game_Engine.Engine.engine.Game_output;

/*
 * Room class, Written by Bryce Summers on 1 - 3 - 2013.
 * Purpose: Defines a Scene.
 */

// FIXME: Deal with persistent objects and rooms.

public abstract class Room extends Obj_Container
{

	// Components of a game.
	private Game_looper looper;
	private Game_input  input;
	private Game_output output;

	// -- Store whether this room is running.
	private boolean running = true;
	
	// Rooms know the project directory that contains all of the rooms.
	public static String room_directory;
	
	private boolean first_time = true;
	
	protected enum Animation_preference{FULL, GUI, NULL;}
	
	protected Animation_preference animation_preference = Animation_preference.NULL;
	
	// -- Constructor.
	
	// Internal constructor for a room.
	public Room()
	{
	}
	
	public Room(Game_output out)
	{
		output = out;
		iVars();
	}
	
	@Override
	public abstract void iObjs();
	
	public void iVars()
	{
		first_time = false;
		
		// Set the default room dimensions.
		set_dimensions(output.getW(), output.getH());
		
		// A room's container is itself.
		setContainer(this);
		
		// Initialize the container variables.
		super.iVars();
		
		// Start this room's source timers.
		looper = new Game_looper(this, 60, output);
		input  = new Game_input (this, output);

		running = true;
	}
	
	protected void iViews()
	{
		
		int density = getViewDensity();
		
		int half_w = getW()/density;
		int half_h = getH()/density;
		
		// Add the views to the views list.
		views = new List<View>();
		
		for(int x = 0; x < density; x++)
		for(int y = 0; y < density; y++)
		{
			Rectangle r1 = new Rectangle(half_w*x, half_h*y,	 half_w, half_h);
			views.add(new View(r1, r1, half_w, half_h));
		}
		
	}
	
	// Can be overriden to specify alternative view densities.
	protected int getViewDensity()
	{
		return 2;
	}
	
	public void restart()
	{
		stop();
		iVars();
	}
	
	// Starts the room's scene.
	public void start()
	{
		looper.start();
		input.start();
		running = true;
	}
	
	public void stop()
	{
		looper.stop();
		input.stop();
		running = false;
	}
	
	public void disableInput()
	{
		input.stop();
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void set_dimensions(int w, int h)
	{
		// Set room dimensions.
		setW(w);
		setH(h);
	}
	
	// Shift the game to a new room scene.
	public void goto_room(String name)
	{
		
		Room r;
		try
		{
			
			stop();
			
			// Instantiate a room.
			r = (Room)Class.forName(room_directory + "." + name).newInstance();

			// Give it this room's data.
			r.output = output;
			
			// Stop the current threads, and begin new ones.
			r.iVars();
			
			if(global_proxy_cursor != null)
			{
				r.obj_create(global_proxy_cursor);
				r.disableInput();
			}

		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	// Shift the game to a new room scene.
	public void goto_room(Room r)
	{
		stop();
		
		// Give it this room's data.
		r.output = output;

		if(global_proxy_cursor != null)
		{
			r.obj_create(global_proxy_cursor);
			r.disableInput();
		}
		
		
		// Start this room for the first time using just in time initialization.
		// Start from where we left off if this room has been initialized before.
		if(r.first_time)
		{
			r.iVars();
		}
		else
		{
			r.start();
		}

		// Handle drawing mode options.
		boolean gui;
		
		switch(r.animation_preference)
		{
			case NULL:
				gui = Game_looper.GUI_DRAW_ENABLED;
				break;
			case GUI:
				gui = true;
			case FULL:
			default:
				gui = false;
		}
		
		if(gui)
		{
			enterGuiDrawingMode();
		}
		else
		{
			enterFullAnimationMode();
		}
	}
	
	// Allow applications to enter optimized drawing mode.
	public void enterGuiDrawingMode()
	{
		looper.enterGuiDrawingMode();
	}
	
	public void enterFullAnimationMode()
	{
		looper.enterFullAnimationMode();
	}
	
	@Override
	public void addDrawingRegion(Rectangle rect)
	{
		looper.addDrawingRegion(rect);
	}
}
