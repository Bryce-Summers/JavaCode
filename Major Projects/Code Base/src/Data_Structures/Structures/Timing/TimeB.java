package Data_Structures.Structures.Timing;

import Game_Engine.GUI.Interfaces.Pingable;

/*
 * Bryce's Discrete Timer Structure.
 * 
 * Written on a train to North Carolina on 3 - 9 - 2014.
 * 
 * Purpose : This class should be used to replaces the ugly code that
 * 			 I keep writing to signal discrete events.
 * 			 This class provides a flag() function that returns true regularly 
 * 			 after a specified number of calls and returns false at other times.
 */

public class TimeB implements Pingable
{
	int time_between_events;
	int current_time;
	
	public TimeB(int time_in)
	{
		current_time		 = time_in;
		time_between_events  = time_in;
	}

	// This function should be called at discrete steps.
	// It signals events at discrete intervals.
	public boolean flag()
	{
		if(current_time > 1)
		{
			current_time--;
			return false;
		}
		
		restartTime();
		return true;
	}
	
	// Modifies the discrete rate at which this structure signals events.
	public void modify_time(int time_new)
	{
		current_time += time_new - time_between_events;
		time_between_events = time_new;
	}

	@Override
	public void setFlag(boolean flag) 
	{
		current_time = flag ? 0 : time_between_events;
	}
	
	public void setTimeOnce(int time_once)
	{
		current_time = time_once;
	}

	// Restarts the time for this timer.
	public void restartTime()
	{
		current_time = time_between_events;
	}
}
