package Game_Engine.GUI.Components.Input;

/*
 * A sub interface representing Input classes that ask the user to input key strokes.
 * 
 * Written by Bryce Summers on 7 - 25 - 2013.
 */

public interface keyInput<T> extends Input<T>
{

	// Should call forceKeyP(key) if isSelected returns true.
	public void keyP(int key);
	
	// Should specify the logic.
	public void forceKeyP(int key);
	
	// Should return true, if this input component has the focus.
	public boolean isSelected();

}
