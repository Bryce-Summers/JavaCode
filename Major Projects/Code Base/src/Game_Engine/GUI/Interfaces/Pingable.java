package Game_Engine.GUI.Interfaces;

/*
 * The Pingable class.
 * written by Bryce Summers on 6 - 2 - 2013.
 * 
 * Purpose : This class is an attempt to standardize the ways 
 * 			 in which my interactive components communicate their updates.
 * 
 * 			1. Buttons must be acted on when clicked.
 * 			2. Scrollbars must communicate that their locations have changed.
 */

public interface Pingable
{

	// This method should be called to see if the component has changed.
	// This method should only return true once for every event.
	public abstract boolean flag();
	public abstract void setFlag(boolean flag);
	
}
