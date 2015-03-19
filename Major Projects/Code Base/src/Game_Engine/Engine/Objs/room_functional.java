package Game_Engine.Engine.Objs;

import util.Genarics;

/*
 * Functional room.
 * 
 * Written by Bryce Summers on 7 - 10 - 2013.
 * 
 * Purpose : Specifies a room object that can recursively create a calling stack.
 */


// FIXME : Complete the full specification for Input<T>
public abstract class room_functional<T> extends Room
{
	
	@Override
	public abstract void iObjs();

	private Room calling;
	public T return_data;
	
	private boolean changed = false;
	
	// REQUIRES : This room must have been called from a super room.
	public void Return(T data_in)
	{
		// Test for null safe equality.
		// Do not change the returned data, if our data has not changed.
		Genarics<T> ge_T = new Genarics<T>();
		if(ge_T.xequal(data_in, return_data))
		{
			Return();
			return;
		}
		
		// if we return new data, then we assume that it has changed.
		changed = true;
		return_data = data_in;
		goto_room(calling);
	}

	// This will return the last given properly created data component.
	// If none exist, then this will return null.
	public void Return()
	{
		goto_room(calling);
	}
	
	public boolean wasCalled()
	{
		return calling != null;
	}
	
	public T getReturnData()
	{
		return return_data;
	}
	
	// Should be called by the calling room e.g. room_to_goto.call(this);
	public void call(Room calling)
	{
		calling.goto_room(this);
		this.calling = calling;
	}
	
	public boolean input_changed()
	{
		if(changed)
		{
			changed = false;
			return true;
		}
		
		return false;
	}
	
	public boolean query_changed()
	{
		return changed;
	}
}
