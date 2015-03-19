package Game_Engine.Engine.Objs;

import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;
import Data_Structures.Structures.HashingClasses.Set;
import Game_Engine.Engine.Objs.actionLogging.Command;
import Game_Engine.Engine.Objs.actionLogging.obj_cursor;

/*
 * Container Class, written by Bryce Summers on 1 - 4 - 2012.
 * Purpose: Provides a superclass for objects that have many sub objects.
 * Because Obj_Container extends an Obj,
 * Sub worlds can be added to any game by extending this class.
 * 
 * This class is intended to create an hierarchical organization of objects and is not intended
 * to be the location for implementations of Physics and collisions.
 * Projects should manage physics and efficient algorithmic work separately.
 */

// FIXME : Clean up all of the visibility, enabled, and collidable functionalities.

public abstract class Obj_Container extends Obj implements Iterable<Obj>
{

	// -- Each container defines an independent game world.
	
	// Game worlds have A list of objects that belong to them.
	private List<Obj> Objs = new List<Obj>();
	private Set<Obj> objs_hash = new Set<Obj>(100);
	
	// They also provide a visual window into the game world.
	protected List<View> views;
	
	// These variables keep track of the top left coordinate of the
	// current visible region of the container's game world.
	protected int viewX;
	protected int viewY;
	
	// Every container can contain a proxy cursor that can simulate a user's actions in the program at any level.
	public obj_cursor proxy_cursor;
	public static obj_cursor global_proxy_cursor;
	protected boolean keyLog = false;
	public static boolean globalLog = false;
	
	private int super_mouse_x;
	private int super_mouse_y;
	
	int w, h;
	
	// Constructor for treating this like a room.
	public Obj_Container()
	{
	}
	
	// Constructor used for Treating this like an object.
	public Obj_Container(double x, double y, int w, int h)
	{
		super(x, y);
		
		this.w = w;
		this.h = h;
		
		iVars();
	}
	
	// Constructor used for Treating this like an object.
	public Obj_Container(Rectangle r)
	{
		super(r.getX(), r.getY());
		
		this.w = r.getW();
		this.h = r.getH();
		
		redraw();
		
		iVars();
	}

	// Initializes this Obj_Container's variables.
	public void iVars()
	{
		// Initialize The list of objects.
		restartObjsList();

		// Initialize this container's views.
		iViews();
		
		iObjs();
	}
	
	// Restarts the list of all Objects in this container.
	public void restartObjsList()
	{
		// FIXME : This should be changed to eliminate the allocation of memory.
		objs_hash = new Set<Obj>(Objs.size() + 1);
		Objs.clear();
	}
	
	// Returns a set representing all objects in this container.
	public Set<Obj> getObjSet()
	{
		return objs_hash.clone();
	}
	
	public    abstract void iObjs();
	protected abstract void iViews();
	
	@Override
	public void draw(ImageB i, AffineTransform AT, CountDownLatch calling_latch)
	{
		// Do nothing if this container is invisible.
		if(!isVisible())
		{
			calling_latch.countDown();
			return;
		}
		
		drawThread t = new drawThread(i, AT, calling_latch);
		t.run();
	}
	
	
	// Containers should always be called with calling latches,
	// because they are part of the parallelized calling tree.
	@Override
	public void draw(ImageB i, AffineTransform AT)
	{
		throw new Error("Obj_Container: draw(image, AT) : please do not ever call this function!");
	}	
	
	private class drawThread extends Thread
	{
		// Local data.
		ImageB image;
		AffineTransform AT;
		CountDownLatch calling_latch;
		
		// Constructor.
		private drawThread(ImageB i_in, AffineTransform AT_in, CountDownLatch latch_in)
		{
			// Initialize data.
			image			= i_in;
			AT				= AT_in;
			calling_latch	= latch_in;
		}
		
		// Thread execution.
		public void run()
		{
			
			// Create a latch to wait for this sequence of parallel views to render.
			CountDownLatch latch = new CountDownLatch(views.size());
			
			// Draw each of the views onto this section of screen.
			for(View v: views)
			{
				v.draw(image, Objs, latch, AT);
			}
			
			
			// Wait for all of the views to finish rendering.
			try
			{
				latch.await();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			// Signal the calling latch that this container's work is done.
			calling_latch.countDown();
			
		}
	}
	
	// Returns the width of the smallest rectangle that contains every one of the views.
	public Rectangle getBounds()
	{
		Rectangle output = views.getFirst().getBounds();
		
		for(View v : views)
		{
			output = output.union(v.getBounds());
		}
		
		return output;
	}

	@Override
	public void keyP(int key)
	{
		if(keyLog)
		{
			Command.logMove(super_mouse_x, super_mouse_y);
			Command.logKeyP(key);
		}
				
		Iterator<Obj> I = Objs.getIter();
		
		while(I.hasNext())
		{
			Obj o = I.next();

			// Do not send key strokes to disabled elements.
			if(!o.isEnabled())
			{
				continue;
			}
			
			// Pressing any key will kill this container's sub proxy cursors.
			if(o instanceof Obj_Container)
			{
				Obj_Container o_container = (Obj_Container)o;
				
				if(o_container.proxy_cursor != null)
				{
					o_container.proxy_cursor.keyPSpecial(key);
					continue;
				}
			}
			
			o.keyP(key);
			
			if(o.getCeaseIteration())
			{
				break;
			}
			
			
		}		
	}

	@Override
	public void keyR(int key)
	{
		if(keyLog)
		{
			Command.logMove(super_mouse_x, super_mouse_y);
			Command.logKeyR(key);
		}
		
		Iterator<Obj> I = Objs.getIter();
		
		while(I.hasNext())
		{
			Obj o = I.next();
			
			if(!o.isEnabled())
			{
				continue;
			}
			
			o.keyR(key);
			if(o.getCeaseIteration())
			{
				break;
			}
			
			if(o.getCeaseIteration())
			{
				break;
			}
			
		}		
	}
	
	// FIXME: Translate mouse input to the view that it intersects.
	// FIXME : Remove this message, if and only if this has been fixed.

	// -- Mouse Input.
	
	@Override
	public void mouseD(int mx, int my)
	{
		updateSuperMouseCoordinates(mx, my);
		
		// Translate these coordinates to match the world of this Container.
		mx -= (int)getX() - getView().getViewX();
		my -= (int)getY() - viewY;
		
		// Reset all of Sub Object's mouse Flags.
		for(Obj o: Objs)
		{
		 	o.resetMouseFlags();
		}
		
		IterB<Obj> I = Objs.getTailIter();
		
		while(I.hasPrevious())
		{
			Obj o = I.previous();
			
			// Do not send mouse events to uncollidable objects.
			if(!o.isCollidable())
			{
				continue;
			}
			
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			
			if(o.mouseCollision(mx, my))
			{
				o.mouseD(mx, my);
				break;
			}
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}		

	}

	@Override
	public void mouseM(int mx, int my)
	{
		
		updateSuperMouseCoordinates(mx, my);
	
		// Translate these coordinates to match the world of this Container.
		mx -= (int)getX() - viewX;
		my -= (int)getY() - viewY;
		
		// Reset all of Sub Object's mouse Flags.
		for(Obj o: Objs)
		{
			o.resetMouseFlags();
		}
		
		IterB<Obj> I = Objs.getTailIter();
		
		while(I.hasPrevious())
		{
			Obj o = I.previous();
			
			// Do not send mouse events to uncollidable objects.
			if(!o.isCollidable())
			{
				continue;
			}
			
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			
			if(o.mouseCollision(mx, my))
			{
				o.mouseM(mx, my);
				break;
			}
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}		
		
	}

	@Override
	public void mouseP(int mx, int my)
	{
		// Log the inputs.
		if(keyLog)
		{			
			Command.logMove(mx, my);
			Command.logMouseP();
		}
		
		updateSuperMouseCoordinates(mx, my);
		
		// Translate these coordinates to match the world of this Container.
		mx -= (int)getX() - viewX;
		my -= (int)getY() - viewY;

		IterB<Obj> I = Objs.getTailIter();
		
		while(I.hasPrevious())
		{
			Obj o = I.previous();
			
			// Do not send mouse events to uncollidable objects.
			if(!o.isCollidable())
			{
				continue;
			}

			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			
			if(o.mouseCollision(mx, my))
			{
				o.mouseP(mx, my);
				break;
			}
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}		

	}

	@Override
	public void mouseR(int mx, int my)
	{
		// Log the inputs.
		if(keyLog)
		{			
			Command.logMove(mx, my);
			Command.logMouseR();
		}
		
		updateSuperMouseCoordinates(mx, my);
		
		// Translate these coordinates to match the world of this Container.
		mx -= (int)getX() - viewX;
		my -= (int)getY() - viewY;
		
		IterB<Obj> I = Objs.getTailIter();
		
		while(I.hasPrevious())
		{
			Obj o = I.previous();
			
			// Do not send mouse events to uncollidable objects.
			if(!o.isCollidable())
			{
				continue;
			}
			
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			
			if(o.mouseCollision(mx, my))
			{
				o.mouseR(mx, my);
				break;
			}
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}		
	}

	// -- Global Mouse input.
	
	// Global Mouse Pressed.
	@Override
	public void global_mouseP()
	{
		// Perform the global action on all world objects.
		for(Obj o: Objs)
		{
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			o.global_mouseP();
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}
	}
	
	// Global Mouse Released.
	@Override
	public void global_mouseR()
	{
		// Perform the global action on all world objects.
		for(Obj o: Objs)
		{
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			o.global_mouseR();
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}
	}
	
	// Global Mouse moved.
	@Override
	public void global_mouseM(int mx, int my)
	{
	
		// Translate these coordinates to match the world of this Container.
		mx -= (int)getX() - viewX;
		my -= (int)getY() - viewY;
		
		// Perform the global action on all world objects.
		for(Obj o: Objs)
		{
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			o.global_mouseM(mx, my);
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}
	}
	
	// Global Mouse Dragged.
	@Override 
	public void global_mouseD(int mx, int my)
	{		
		// Translate these coordinates to match the world of this Container.
		mx -= (int)getX() - viewX;
		my -= (int)getY() - viewY;
		
		// Perform the global action on all world objects.
		for(Obj o: Objs)
		{
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			o.global_mouseD(mx, my);
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}
	}
	
	// -- Scroll wheel.
	
	@Override
	public void global_mouseScroll(int amount)
	{
		// Perform the global action on all world objects.
		for(Obj o: Objs)
		{
			// Do not send input to sub containers that are being controlled by a proxy_cursor.
			if(o instanceof Obj_Container && ((Obj_Container)o).proxy_cursor != null)
			{
				continue;
			}
			
			o.global_mouseScroll(amount);
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}
	}
	
	@Override
	public void update()
	{
		// Perform no update if this container is disabled.
		if(!isEnabled())
		{
			return;
		}
		
		// FIXME : Upgrade this to intelligent insertion sort or an only as needed sort.
		// The order of the objects matters for drawing.
		Objs.sort();
		
		Iterator<Obj> I = Objs.getIter();
		
		while(I.hasNext())
		{
			Obj o = I.next();
			
			// Update the object.
			if(isEnabled())
			{
 				o.ensureUpdate();
			}

			// After the first update, we claim that the object has been initialized.
			//o.setInitialized(true);

			// Remove the object if it is freshly dead.
			if(o.dead(this))
			{
				I.remove();
				objs_hash.rem(o);
			}
			
			if(o.getCeaseIteration())
			{
				break;
			}
		}
		
		// Process the end steps.
		for(Obj o : Objs)
		{
			if(o.isEnabled())
			{
				if(o.isEnabled())
				o.endStep();
			}
			
			// Prepare all of the objects for updating during the next iteration step.
			o.resetUpdateFlagForNextStep();
		}
			
		View v = views.getFirst();
		
		// Update view variables.
		viewX = v.getViewX();
		viewY = v.getViewY();
	}
	
	// Resets the flags for all of this container's sub objects.
	@Override
	public void resetMouseFlags()
	{
		// Reset all of this container's flags.
		super.resetMouseFlags();
		
		// Reset all of the flags for the objects supported by this container.
		for(Obj o: Objs)
		{
			o.resetMouseFlags();
		}
	}

	// -- Interface methods.
	
	// Allows for the list to be Mutated.
	public List<Obj> getObjList()
	{
		return Objs;
	}
	
	public Obj obj_create(Obj ... objs)
	{
		Obj output = null;
		for(Obj o : objs)
		{
			output = obj_create(o);
		}
		
		return output;
	}
	
	public Obj obj_create(Obj o)
	{
			
		// We do not allow objects to be created more than once.
		if(objs_hash.includes(o))
		{
			return o;
			//throw new Error("Duplicate Object in a container!!!" + o);
		}
		
		objs_hash.set_add(o);
		
		// Initialize the proper fields in the Obj.
		Objs.add(o);
		
		// Give the Object proper container variables.
		o.setContainer(this);

		// Make sure the object doe not have any lingering death flags set.
		o.ressurect();
		
		// Initialize all new objects.
		if(!o.getInitialized())
		{
			o.initialize();
			o.setInitialized(true);
		}
		


		
		if(o instanceof Obj_union)
		{
			for(Obj o2 : (Obj_union)o)
			{
				if(!objs_hash.includes(o2))
				obj_create(o2);
			}
		}
		
		return o;
	}
	
	// -- Proxy cursor methods.
	
	// Any Object container can log its key strokes on demand.
	// FIXME : implement explicit printing to a file.
	public void logKeys()
	{
		if(globalLog)
		{
			throw new Error("Do not log both global and local data in the same session!");
		}
		
		if(keyLog)
		{
			throw new Error("Only log one source at a time.");
		}
		
		keyLog = true;

		// Open the files for logging
		Command.openLogs();
	}
	
	// -- Log a global complete user experience in both condensed data and pure java code.

	public void globalLog()
	{
		if(keyLog)
		{
			throw new Error("Do not log both global and local data in the same session!");
		}
		
		if(globalLog)
		{
			throw new Error("You should have no reason to call this function more than once!");
		}
		
		globalLog = true;
		
		// Open the files for logging
		Command.openLogs();		
	}
	
	protected void createProxyCursor(UBA<Command> program)
	{
		// Create a cursor that will go through all of the correct movements.
		proxy_cursor = new obj_cursor(0, 0);
		
		// Program the proxy cursor.
		proxy_cursor.program(program);

		// Create an all controlling proxy_cursor.
		obj_create(proxy_cursor);
	}
	
	// Destroy the master control program.
	public void killProxyCursor()
	{
		proxy_cursor.kill();
		proxy_cursor = null;
	}
	
	private void updateSuperMouseCoordinates(int mx, int my)
	{
		super_mouse_x = mx;
		super_mouse_y = my;
	}
	
	// FIXME : Perhaps update the view as well.
	public void setW(int w)
	{
		this.w = w;
	}
	
	public void setH(int h)
	{
		this.h = h;
	}
	
	@Override
	public int getW()
	{
		return w;
	}
	
	@Override
	public int getH()
	{
		return h;
	}
	
	public View getView()
	{
		return views.getFirst();
	}
	
	// This should send official kill notifications to all of the sub objects of this container.
	@Override
	public void die()
	{
		super.die();
		
		for(Obj o : Objs)
		{
			o.kill();
		}
	}
		
	@Override
	public Iterator<Obj> iterator()
	{
		return Objs.iterator();
	}

	// Height and width addition and subtraction methods.
	public void addH(int inc)
	{
		setH(getH() + inc);
	}
	
	public void addW(int inc)
	{
		setW(getW() + inc);
	}
	
	public void subH(int inc)
	{
		setH(getH() - inc);
	}
	
	public void subW(int inc)
	{
		setW(getW() - inc);
	}

}