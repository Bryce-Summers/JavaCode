package Game_Engine.GUI.Components.sets;
import java.util.Iterator;

import Data_Structures.ADTs.Pairable;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_handle;
import Game_Engine.GUI.Interfaces.Pingable;


/*
 * Swap set Object.
 * 
 * Written by Bryce Summers on 6 - 25 - 2013.
 * 
 * Purpose : Manages an array of objects and swaps their positions and indices within the list whenever one of the objects invades another object's home.
 * 			 Only allows one such action to be performed at a time, and subsequent actions will negate the former swaps.
 * 
 * Capabilities : This class allows the user to imply gui component swaps, much like row exchanges in gaussian ellimination.
 * 
 */

public class Indication_Set<T extends Obj> extends Obj implements Iterable<T>, Pingable
{

	UBA<T> data;
	
	int swap1 = -1;
	int swap2 = -1;
	
	private boolean revert = false;
	
	public Indication_Set()
	{
		data = new UBA<T>();
	}
	
	@Override
	public void update()
	{
		handle_swaps();
	}
	
	// FIXME : Determine what criteria is sufficient for determining the active member of the swap set.
	private void handle_swaps()
	{
		gui_handle moving_elem = gui_handle.handle_held;
		
		int moving_index = getIndex(moving_elem);
		
		if(moving_index < 0)
		{
			return;
		}		
		
		int len = data.size();
		
		for(int current_index = 0; current_index < len; current_index++)
		{
			if(current_index == moving_index)
			{
				continue;
			}
			
			Obj o = data.get(current_index);
			
			int bounds_reduce = o.getH()/2;
			
			boolean c1 = o.collision(o.x_start - bounds_reduce, o.y_start - bounds_reduce, moving_elem);
			boolean c2 = o.collision(o.x_start + bounds_reduce, o.y_start + bounds_reduce, moving_elem);
			
			// Swap the locations of the two elements.
			if(c1 && c2)
			{

				// Unswap the previously made swap.
				if(swap1 != -1)
				{
					//Obj o1 = data.get(swap1);
					Obj o2 = data.get(swap2);
					
					swap(swap1, swap2);
					
					o2.revert();
					
					swap1 = -1;
					swap2 = -1;
					
					return;
				}
				
				swap1 = current_index;
				swap2 = moving_index;
				
				swap(current_index, moving_index);
				
				// Tell the stagnate object to glide its new home.
				
				o.revert();
				
				return;
			}
		}
	}
	
	
	// Returns the index of a given object, if it exists inside of this set.
	private int getIndex(Obj input)
	{
		int len = data.size();
		for(int i = 0; i < len; i++)
		{
			Obj o = data.get(i);
			
			if(o == input)
			{
				return i;
			}
			
			if(o instanceof Obj_union && ((Obj_union) o).contains(input))
			{
				return i;
			}
			
		}
		
		return -1;
	}
	
	private void swap(int i1, int i2)
	{
		Obj o1 = data.get(i1);
		Obj o2 = data.get(i2);
		
		data.swap(i1, i2);
		
		double tempX = o1.x_start;
		double tempY = o1.y_start;
		
		o1.x_start = o2.x_start;
		o1.y_start = o2.y_start;
		
		o2.x_start = tempX;
		o2.y_start = tempY;
		
		return;
	}
	
	// REQUIRES : The object o should not intersect any other object in this Swap_Set.
	public void push(T o)
	{
		data.push(o);
	}
	
	public T pop()
	{
		return data.pop();
	}
	
	public UBA<T> getData()
	{
		return data;
	}

	@Override
	public Iterator<T> iterator()
	{
		return data.iterator();
	}

	@Override
	public void global_mouseR()
	{
		if(swap1 != -1)
		{
			revert = true;
		}
	}
	
	@Override
	public void endStep()
	{
		if(!revert)
		{
			return;
		}
		
		revert = false;
		
		if(swap1 == -1)
		{
			return;
		}
			
		Obj o1 = data.get(swap1);
		Obj o2 = data.get(swap2);

		swap(swap1, swap2);

		o1.revert();
		o2.revert();

		swap1 = -1;
		swap2 = -1;
	}
	
	// Returns whether this object is reverting or not.
	@Override
	public boolean flag()
	{
		return revert;
	}

	@Override
	public void setFlag(boolean flag)
	{
		throw new Error("Please do not do this!");
	}
	
	public Pairable<Integer> getSwaps()
	{
		List<Integer> output = new List<Integer>();
		
		output.add(swap1);
		output.add(swap2);
		
		return output;
	}
	
	public int size()
	{
		return data.size();
	}
}
