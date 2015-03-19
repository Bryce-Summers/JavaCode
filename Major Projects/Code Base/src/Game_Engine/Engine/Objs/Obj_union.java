package Game_Engine.Engine.Objs;

import java.util.Iterator;

import BryceMath.DoubleMath.Vector;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.AArray;

/*
 * gui_union class.
 * 
 * Written by Bryce Summers.
 * 
 * Date : 6 - 3 - 2013.
 * 
 * Purpose : This class allows for new structures to be easily created from the Union of existing structures.
 * 
 * Note : Unlike the Object Containers,
 * 		  This creates a union of structures that all stay within the same world,
 * 		  there is no sub world.
 * 		  This class creates a geometric spacial link between the Objects.
 * 		  This class creates a link between the lives of the objects.
 * 
 * 	This means that this class does not need to keep track of the operation of the subclasses.
 *  It just needs to maintain the positions of the subclasses and kill them all if one of them dies.
 *  
 *  Update 8 - 18 - 2013. I have removed the depth synchronizing capabilities.
 */


// FIXME : Comment all of the functions.
// FIXME : Allow components to have different weights.

// FIXME : Clean up this class.

// FIXME : Strengthen the object updating unity with the new ensureUpdate() function;

public abstract class Obj_union extends Obj implements Iterable<Obj>
{
	
	// -- private data
	
	// I am super excited to be using my Associative Array.
	// We need to keep track of the positions of all of the objects.
	// If one of them is changed, we need to change the positions of all of the object.
	// FIXME : Consider physics and center of mass.
	private AArray<Obj, Vector> initial_positions;
	private AArray<Obj, Vector> positions;
	
	// Keep the constant unioned depth separate from the engine depth.
	// The real depth of the union object itself will be the maximum integer value,
	// because it should be processed last.
	private int obj_depths = 0;
	
	// FIXME Make this next bit a hash table.
	
	// A union is composed of a set of Objects.
	public List<Obj> objs;
	
	// Every Union should know its width and height.
	private int w, h;

	// Every Union behaves just like a normal object,
	// so it can be offset by a 2 dimensional vector.
	public Obj_union(double x, double y, int w, int h)
	{
		super(x, y);
		iVars(w, h);
	}
	
	public Obj_union(Rectangle bounds)
	{
		super(bounds.getX(), bounds.getY());
		iVars(bounds.getW(), bounds.getH());
	}

	private void iVars(int w, int h)
	{
		// Important.
		this.w = w;
		this.h = h;
		
		// Initialize the objs list.s
		objs = new List<Obj>();

		// Initialize the positions Associative array.
		positions = new AArray<Obj, Vector>();
		
		// Initialize the initial_positions.
		initial_positions = new AArray<Obj, Vector>();

		// Perform geometric work on this object.
		collidable = false;
		
	}
	
	// Initialize the objs with full object capability and access to room width and room height.
	@Override
	public void initialize()
	{		
		initialized = true;
		
		iObjs();
		
		// This union object itself must be a object in its array.
		obj_create(this);
	}
	
	// Every Union needs to specify the objects that it is a Union of.
	public abstract void iObjs();

	// Unions let their object container do the all of the work.
	// The union only keeps the objects together.
	public Obj obj_create(Obj o)
	{
		// Most important step.
		objs.add(o);
		
		if(o != this && myContainer != null)
		myContainer.obj_create(o);
		
		Vector orgin = new Vector(x_start, y_start);
		Vector position_new = o.getPositionVector();
		
		Vector offset = getPositionVector().sub(orgin);
		
		// Insert the object into the normal list of positions.
		insert(o, position_new);
		
		// Insert the object into the list of initial positions.
		initial_positions.insert(o, position_new.sub(offset));
		
		if(!isEnabled())
		{
			o.disable();
		}
		
		return o;

	}
	
	// Returns true if this removes the desired object from the union.
	// Returns false if the desired object was not present in the union.
	public boolean remove(Obj o)
	{
		
		if(o == this)
		{
			throw new Error("Unions can not remove themselves from themselves!");
		}
		
		// FIXME : Upgrade the objs to a hash table.
		
		Iterator<Obj> iter = objs.iterator();
			
		while(iter.hasNext())
		{
			Obj o2 = iter.next();
			
			if(o2.equals(o))
			{
				iter.remove();
			}
			
		}
		positions.remove_key(o);
		
		return initial_positions.remove_key(o);
		
	}
	
	// All for one and one for all.
	
	@Override
	public void update()
	{
		/* Do nothing during the normal update. */
	}
	
	@Override
	public void endStep()
	{
		updateDepths();
		
		// Update positions and Death each and every step.
		updatePositions();
		updateDeath();
	}
	
	// Sets all of the objects in the union to the depth of the union object.
	private void updateDepths()
	{
		int depth_new = obj_depths;
		
		// Search for a change in depth.
		for(Obj o : objs)
		{
			int depth = o.getDepth();

			if(depth != obj_depths)
			{
				depth_new = depth;
			}
		}
		
		// Update the depths of all of the objects if necessary.
		if(depth_new == obj_depths)
		{
			return;
		}

		// update the obj_depths.
		obj_depths = depth_new;
		
		// Search for a change in depth.
		for(Obj o : objs)
		{
			o.setDepth(depth_new);
		}
		
	}
	
	// Keeps all of the objects in the Union bounded together.
	private void updatePositions()
	{
		Vector change = getPosChange();
		
		// If there has been a change.
		if(!change.equals(new Vector(0, 0)))
		{
			offsetPositions(change);
		}
	}
	
	// Returns the average amount of position changes that all of the components have gone through.
	// If multiple components have been moved, then their offsets from their previous positions are averaged together.
	private Vector getPosChange()
	{
		// Start at the empty additive universe.
		Vector change = new Vector(0, 0);
		
		// Track the number of changes.
		int num_changes = 0;
		
		// Add up all of the changes and then offset all of the objects by the given amount.
		for(Obj o : objs)
		{
			Vector pos_previous = lookup(o);
			Vector pos_current  = o.getPositionVector();
			
			if(!pos_previous.equals(pos_current))
			{
				Vector sub_change = pos_current.sub(pos_previous);
								
				change = change.add(sub_change);
				
				num_changes++;
			}
		}

		
		// return the Average amount of change.
		if(num_changes > 0)
		{
			return change.div(num_changes);
		}
		
		return change;
	}
	
	/* 
	 * Updates each objects real and stored positions to be relative to the previous
	 * stored positions added to the given offset vector.
	 */
	private void offsetPositions(Vector offset)
	{
		// Now update the sub components.
		
		for(Obj o : this)
		{
			// Lookup the old stored position.
			Vector pos_previous = lookup(o);
			
			// Compute the new position.
			Vector pos_new = pos_previous.add(offset);
			
			// Set the objects coordinates to the new position.
			o.setX(pos_new.get(0));
			o.setY(pos_new.get(1));
			
			// Update the entry for this object.
			update(o);
			
		}
	}
	
	// Should revert all of the objects to their starting locations.
	public void revertPositions()
	{
		for(Obj o : this)
		{
			// Extract the intitial position.
			Vector i_pos = initial_positions.lookup(o);
			
			// update the current position.
			positions.insert(o, i_pos);
			
			// Update the actual object coordinates.
			o.setX(i_pos.getX());
			o.setY(i_pos.getY());
		}
	}
	
	// -- Methods for inserting objects into the union's position tracking.
	/*
	private void insert(Obj o)
	{
		Vector position = o.getPositionVector();
		insert(o, position);
	}
	*/
	
	private void insert(Obj o, Vector position)
	{
		positions.insert(o, position);
	}
	
	protected void update(Obj o)
	{
		
	}
	
	private Vector lookup(Obj o)
	{
		return positions.lookup(o);
	}
	
	private void updateDeath()
	{		
		boolean shouldKill = false;
		
		for(Obj o : objs)
		{
			if(o.dead())
			{
				shouldKill = true;
				break;
			}
		}
		
		// Do not kill anything if nothing is dead.
		if(!shouldKill)
		{
			return;
		}
		
		// If a component has died, then kill all of the components.
		killAll();
	}
	
	@Override
	public void die()
	{
		killAll();
	}
	
	private void killAll()
	{
		// Kill this Obj.
		super.die();
		
		// Kill all of the component Objs.
		for(Obj o : objs)
		{
			if(o == this)
			{
				continue;
			}

			o.die();
		}
	}
	
	// Selectively kill this union within 1 given container.
	@Override
	public void kill(Obj_Container container)
	{
		super.kill(container);
		
		for(Obj o : this)
		{
			if(o == this)
			{
				continue;
			}
			
			o.kill(container);
		}
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
	
	public void setW(int w_in)
	{
		w = w_in;
	}
	
	public void setH(int h_in)
	{
		h = h_in;
	}
	
	@Override
	public Iterator<Obj> iterator()
	{
		return objs.iterator();
	}
	
	@Override
	public void disable()
	{
		super.disable();
		
		for(Obj o : this)
		{
			if(o != this)
			o.disable();
		}
	}
	
	@Override
	public void enable()
	{
		super.enable();
		
		for(Obj o : this)
		{
			if(o != this)
			o.enable();
		}
	}
	
	@Override
	public void setCollidable(boolean flag)
	{
		super.setCollidable(flag);
		
		for(Obj o : this)
		{
			if(o != this)
			o.setCollidable(flag);
		}
	}
	
	public boolean contains(Obj o)
	{
		return objs.contains(o);
	}
	
	@Override
	public void setVisible(boolean flag)
	{
		super.setVisible(flag);
		
		for(Obj o : this)
		{
			if(o != this)
			{
				o.setVisible(flag);
			}
		}			
	}
}
