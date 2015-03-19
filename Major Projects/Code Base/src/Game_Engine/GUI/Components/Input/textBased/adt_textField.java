package Game_Engine.GUI.Components.Input.textBased;

import Data_Structures.Structures.IterB;
import Data_Structures.Structures.Pair;

/*
 * Abstract text Field interface.
 * 
 * Written by Bryce Summers on 2 - 6 - 2014.
 * 
 * Specifies an Abstract Data structure for objects that store and display text.
 */

public interface adt_textField
{
	// Returns the height of the text.
	public int getTextHeight();
	
	// Returns an (x, y) pair for the indicated character in the text field. 
	public Pair<Integer, Integer> getCharacterLocation(int position);
	
	public IterB<Character> getCharLink(int position);
	public IterB<Character> getEndLink();
	
	// Returns the number of characters in the text field.
	public int getTextLength();
	
	public void refreshText();
}
