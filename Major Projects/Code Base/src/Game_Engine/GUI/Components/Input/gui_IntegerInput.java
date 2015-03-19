package Game_Engine.GUI.Components.Input;

import java.awt.event.KeyEvent;
import java.util.Iterator;

import util.StringParser;
import BryceMath.Geometry.Rectangle;
import BryceMath.Numbers.IntB;
import Data_Structures.Structures.List;

/*
 * gui_IntegerInput.
 * 
 * Written by Bryce Summers 6 - 6 - 2013.
 * Rewritten on 6 - 28 - 2013.
 * 
 * Purpose : input boxes are used to allow users to input specific data.
 * 
 * 
 * Edited : 3 - 29 - 2014. 
 * 
 *  - Added a parameter for max_len = maximum number of characters the data will hold.
 *  FIXME : Perhaps this functionality should be characteristic of all text boxes and
 *   I should compile all of my textbox classes into a proper hierarchy.
 */

public class gui_IntegerInput extends gui_InputBox<IntB>
{	
	// -- Private data.
	private boolean positive = true;
	private boolean changed = false;
	
	private boolean allow_negatives = true;
	
	// Every input box recalls its own default_text;
	private String default_text = "0";
	
	// A list of characters representing numbers. (16 bits a piece.)
	private List<Character> data = new List<Character>();
	
	private int max_len = -1;
	
	// -- Constructors.
	public gui_IntegerInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		refreshText();
	}

	public gui_IntegerInput(Rectangle r)
	{
		super(r);
		refreshText();
	}
	
	public void forceKeyP(int key)
	{
		
		switch(key)
		{
			case KeyEvent.VK_NUMPAD0 :
			case KeyEvent.VK_0:
				// Do not push leading zeros onto the input.
				if(data.size() > 0)
				data.push('0');
				break;
			case KeyEvent.VK_NUMPAD1 :
			case KeyEvent.VK_1:
				data.push('1');
				break;
			case KeyEvent.VK_NUMPAD2 :
			case KeyEvent.VK_2:
				data.push('2');
				break;
			case KeyEvent.VK_NUMPAD3 :
			case KeyEvent.VK_3:
				data.push('3');
				break;
			case KeyEvent.VK_NUMPAD4 :
			case KeyEvent.VK_4:
				data.push('4');
				break;
			case KeyEvent.VK_NUMPAD5 :
			case KeyEvent.VK_5:
				data.push('5');
				break;
			case KeyEvent.VK_NUMPAD6 :
			case KeyEvent.VK_6:
				data.push('6');
				break;
			case KeyEvent.VK_NUMPAD7 :
			case KeyEvent.VK_7:
				data.push('7');
				break;
			case KeyEvent.VK_NUMPAD8 :
			case KeyEvent.VK_8:
				data.push('8');
				break;
			case KeyEvent.VK_NUMPAD9 :
			case KeyEvent.VK_9:
				data.push('9');
				break;
				
			// Allow the user to revert old inputs.
			case KeyEvent.VK_BACK_SPACE:
				int size = data.size();
				
				if(size > 0)
				{
					data.pop();
				}
				
				if(!positive && size == 0)
				{
					positive = true;
				}
				
				break;
				
			case KeyEvent.VK_MINUS:

				// negate the sign of this number.
				if(allow_negatives)
				{
					positive = !positive;
				}
				break;
				
			default:
				return;
		}
		
		// Remove the oldest character if we have exceeded our maximum_text length.
		if(max_len > 0 && data.size() > max_len)
		{
			data.deq();
		}
		
		refreshText();			
	}
	
	public void clearInput()
	{
		positive = true;
		data = new List<Character>();
		refreshText();
	}

	// Input boxes return a number, if and only if their input is well formed.
	public IntB getInput()
	{

		String output = getText();
		
		// Do not return bad input.
		if(output.equals("-"))
		{
			return new IntB(-1);
		}

		// NOTE : returns 0 for the empty string.
		return new IntB(output);
		
	}

	public void refreshText()
	{
		setText(toString());
		changed = true;
	}
	
	// Returns the string representation of this boxes' input.
	public String toString()
	{
		// Handle null data.
		if(data.isEmpty())
		{
			if(positive)
			return default_text;
			else
			return "-";
		}
		
		if(positive)
		{
			return StringParser.charArrToString(data.toArray());
		}
		
		return "-" + StringParser.charArrToString(data.toArray());
	}

	@Override
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
	
	public int getInputSize()
	{
		return data.size();
	}
	
	void addChar(char c)
	{
		data.add(c);
	}
	
	public void update()
	{	
		super.update();
		
		fitToContents();
	}
	
	public void setDefaultText(String str)
	{
		default_text = str;
		changed = true;
		refreshText();
	}
	
	// Converts this integer input box into a natural number input box.
	public void disableNegatives()
	{
		allow_negatives = false;
		positive = true;
	}
	
	public void setMaxLen(int len)
	{
		max_len = len;
		
		// Elliminate potential future bugs.
		if(max_len < 0)
		{
			max_len = -1;
			return;
		}
		
		// Remove excess characters from the data stream.
		while(data.size() > max_len)
		{
			data.deq();
		}
	}
	
	// Loads the given integer into this input class's internal representation.
	public void populateInput(int i)
	{
		String str = "" + i;
		Iterator<Character> iter = StringParser.createIterator(str);
		populateInput(iter);
	}
	
	public void populateInput(IntB i)
	{
		String str = "" + i;
		Iterator<Character> iter = StringParser.createIterator(str);
		populateInput(iter);
	}
	
	// Populates this class's input form the given list of characters.
	// Rather internal wouldn't you say.
	private void populateInput(Iterator<Character> iter)
	{
		clearInput();
		
		while(iter.hasNext())
		{
			data.push(iter.next());
		}
		
		refreshText();
		
	}
}
