package Game_Engine.GUI.Components.small;

import java.awt.Color;
import java.awt.event.KeyEvent;

import BryceMath.Calculations.Colors;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.Pair;
import Data_Structures.Structures.Timing.TimeB;
import Game_Engine.GUI.Components.Input.textBased.adt_textField;

/*
 * gui_cursor Class.
 * Written by Bryce Summers on 2 - 6 - 2014.
 * 
 * This class provides a blinking cursor on the screen that is attached to a
 * host modifiable text component.
 * 
 * Cursors know how to provide visual modification functionality.
 * 
 */

public class gui_cursor extends gui_label
{
	
	// -- Local data.

	private adt_textField host_field;
	
	private int character_position = 0;
	private IterB<Character> character_iterator;
	
	TimeB blink_timer;
	
	private static Color C_OFF = Color.BLACK;
	private static Color C_ON  = Colors.C_CLEAR;
	
	// -- Constructors.
	public gui_cursor(adt_textField t, int position)
	{
		super(0, 0, 2, 1);

		host_field = t;

		// Initialize the character position.
		character_position = position;
		character_iterator = t.getCharLink(position);

		blink_timer = new TimeB(12);
		
		setH(t.getTextHeight());
		
		// Put the cursor at the correct point in space.
		updatePosition();
			
	}

	@Override
	public void update()
	{
		if(blink_timer.flag())
		{
			blink();
		}
		
	}
	
	private void blink()
	{
		if(getColor() == C_OFF)
		{
			setColor(C_ON);
		}
		else
		{
			setColor(C_OFF);
		}
	}
	
	// Handle navigational key presses.
	@Override
	public void keyP(int key)
	{
		switch(key)
		{
			case KeyEvent.VK_END:
				character_position = host_field.getTextLength() + 1;
				character_iterator = host_field.getEndLink();
				character_iterator.previous();
				break;

			case KeyEvent.VK_HOME:
				
				if(host_field.getTextLength() == 0)
				{
					break;
				}
				
				character_position = 0;
				character_iterator = host_field.getCharLink(0);
				character_iterator.next(); // important.
				break;
		
			// Allow the user to revert old inputs.
			// Backspaces delete the current character that the iterator is pointing to.
			case KeyEvent.VK_BACK_SPACE:

				if(character_iterator.hasCurrent())
				{
					character_iterator.remove();
										
					if(character_iterator.hasPrevious())
					{
						character_iterator.previous();
					}
					
					host_field.refreshText();
					character_position--;
				}
				break;
		
			// Delete deletes the next character that the iterator is pointing to.
			case KeyEvent.VK_DELETE:

				if(character_iterator.hasNext())
				{
					// Perform the Deletion.
					character_iterator.next();
					character_iterator.remove();
					host_field.refreshText();
					
					// Go back to the previous character if it exists.
					if(character_iterator.hasPrevious())
					{
						character_iterator.previous();
					}
					break;
				}
				
				if(character_position == 0 && character_iterator.hasCurrent())
				{
					character_iterator.remove();
					host_field.refreshText();
				}
				
				break;
			case KeyEvent.VK_LEFT:
				
				if(character_iterator.hasPrevious())
				{
					character_iterator.previous();
					character_position--;
				}
				else
				{
					character_position = 0;
				}
				break;
			case KeyEvent.VK_RIGHT:
				
				if(character_iterator.hasNext())
				{
					character_iterator.next();
					character_position++;
				}
				else
				{
					character_position = host_field.getTextLength();
					character_iterator = host_field.getEndLink();
				}
				break;
			// FIXME : Implement Up and Down.
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
				return;
				
		}
	
		updatePosition();
	}
	
	// Update the cursor's position upon key release.
	@Override
	public void keyR(int key)
	{
		updatePosition();
	}
	
	// Inserts the given character into the text field at the point of the cursor.
	public void type(char c)
	{
		if(character_position == 0)
		{
			character_iterator.insertBefore(c);
		}
		else
		{
			character_iterator.insertAfter(c);
		}
		
		character_position++;
	}
	
	// FIXME : Instead of pushing characters and popping them, I should share an iterator between the cursor and the text field.
	
	public void giveNewLink(IterB<Character> new_link, int position)
	{
		character_iterator = new_link;
		
		character_position = position;
	}

	public void setHost(adt_textField field)
	{
		host_field = field;
		blink();
	}

	// Moves the cursor to the correct point in space that it is representing.
	private void updatePosition()
	{
		Pair<Integer, Integer> coords = host_field.getCharacterLocation(character_position);
		setX(coords.getKey());
		setY(coords.getVal());
	}
}
