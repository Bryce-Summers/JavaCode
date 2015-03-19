package Game_Engine.GUI.Components.Input;

/* 
 * Input interface, written by Bryce Summers on 6 - 28 - 2013.
 * 
 * Purpose : Specifies a component that can parse user input into an object of the specified type.
 */

public interface Input<T>
{
	// Inputs know how to revert themselves to their initial states, before input was received.
	public abstract void clearInput();

	// Inputs know how to communicate their inputs.
	// Returns null, if the input is currently malformed.
	public abstract T getInput();
	
	// Returns whether the input has since the last time that this was called changed, and should return false when called again.
	public boolean input_changed();
	
	/*
	 
	 if(changed)
	 {
	 	changed = false;
	 	return true;
	 }
	 
	 return false;
	  
	 */

	
	// Returns whether this input has changed since the last time that input_changed() was called.
	public abstract boolean query_changed();

	/*
	 * return changed;
	 */
	
}
