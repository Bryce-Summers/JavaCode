package Game_Engine.GUI.Components.sets;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_handle;


/*
 * Swap set Object.
 * 
 * Written by Bryce Summers on 6 - 25 - 2013.
 * 
 * Purpose : Manages an array of objects and swaps their positions and indices within the list whenever one of the objects invades another object's home.
 * 
 * Capabilities : This class can provide swapping functionality or insertion functionality to create visual data sets that can be mutated.
 * 
 */

public class Swap_Set extends Obj
{

	UBA<Obj> data;
	
	public Swap_Set()
	{
		data = new UBA<Obj>();
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
				swap(current_index, moving_index);
				
				// Tell the stagnate object to glide its new home.
				
				o.revert();
				
				return;
			}
		}
	}
	
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
	public void add(gui_handle o)
	{
		data.add(o);
	}
}
