package Game_Engine.GUI.Components.Input.textBased;

import java.awt.event.KeyEvent;
import java.io.PrintStream;

import util.StringParser;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Operations.Search;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.Pair;
import Game_Engine.Engine.text.TextManager;
import Game_Engine.GUI.Components.Input.gui_InputBox;
import Game_Engine.GUI.Components.small.gui_cursor;

/*
 * Super class for text based input boxes.
 * 
 * Written some time in July of 2013.
 * Author : Bryce Summers.
 * 
 * Updated 8 - 12 - 2013.
 *  - Enables TEX when not in focus, disables TEX when it is the focus or the mouse is over it.
 *  
 * Updated 2 - 6 - 2014 :
 *  - Started implementing cursor editing functionality.
 */

public abstract class gui_textInput<T> extends gui_InputBox<T> implements adt_textField
{
	
	// -- Local data.
	private List<Character> data = new List<Character>();
	
	// The Pointer for the cursor.
	private IterB<Character> iter = data.getIter();
	
	private boolean shift   = false;
	private boolean changed = false;
	
	private static gui_cursor cursor = null;
	
	public gui_textInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		clearInput();
	}

	public gui_textInput(Rectangle r)
	{
		super(r);
		clearInput();
	}
	
	protected List<Character> getData()
	{
		return data;
	}
	
	@Override
	public void forceKeyP(int key)
	{
		
		// Do nothing more if this box is not the one that is selected.
		if(selection != this)
		{
			return;
		}
		
		// Change the shift flag if the shift key is pressed.
		if(key == KeyEvent.VK_SHIFT)
		{
			shift = true;
			return;
		}
		
		// Process the key stroke.
		if(!handleKeyStroke(key))
		{
			return;
		}
			
		if(data.isEmpty())
		{
			setText("");
			return;
		}
		
		changed = true;
		refreshText();	
	}

	/*
	 * Updates the text to reflect the input characters.
	 * They will be displayed to the screen in either TEX or non TEX mode based on this Obj's local flag.
	 */
	@Override
	public void refreshText()
	{
		String str = StringParser.charArrToString(data.toArray());
		if(!getText().equals(str))
		{
			changed = true;
		}
		setText(str);
	}
	
	// Returns true if and only if the key stroke is supported and mutates the data.
	private boolean handleKeyStroke(int key)
	{
		switch(key)
		{

			case KeyEvent.VK_SPACE:
				cursor.type(' ');
				return true;

		}

		if(shift)
		{
			return handleUpperCase(key);
		}
		
		return handleLowerCase(key);
	}
	
	public boolean handleLowerCase(int key)
	{
		switch(key)
		{
			case KeyEvent.VK_BACK_QUOTE:
				cursor.type('`');
				break;
				
			case KeyEvent.VK_DIVIDE:
			case KeyEvent.VK_SLASH :
				cursor.type('/');
				break;
			case KeyEvent.VK_BACK_SLASH :
				cursor.type('\\');
				break;
				
			case KeyEvent.VK_QUOTE:
				cursor.type('\'');
				break;
				
			case KeyEvent.VK_EQUALS:
				cursor.type('=');
				break;
				
			case KeyEvent.VK_SEMICOLON:
				cursor.type(';');
				break;
				
			case KeyEvent.VK_NUMPAD0 :
			case KeyEvent.VK_0:
				cursor.type('0');
				break;
			case KeyEvent.VK_NUMPAD1 :
			case KeyEvent.VK_1:
				cursor.type('1');
				break;
			case KeyEvent.VK_NUMPAD2 :
			case KeyEvent.VK_2:
				cursor.type('2');
				break;
			case KeyEvent.VK_NUMPAD3 :
			case KeyEvent.VK_3:
				cursor.type('3');
				break;
			case KeyEvent.VK_NUMPAD4 :
			case KeyEvent.VK_4:
				cursor.type('4');
				break;
			case KeyEvent.VK_NUMPAD5 :
			case KeyEvent.VK_5:
				cursor.type('5');
				break;
			case KeyEvent.VK_NUMPAD6 :
			case KeyEvent.VK_6:
				cursor.type('6');
				break;
			case KeyEvent.VK_NUMPAD7 :
			case KeyEvent.VK_7:
				cursor.type('7');
				break;
			case KeyEvent.VK_NUMPAD8 :
			case KeyEvent.VK_8:
				cursor.type('8');
				break;
			case KeyEvent.VK_NUMPAD9 :
			case KeyEvent.VK_9:
				cursor.type('9');
				break;
			case KeyEvent.VK_PERIOD:
				cursor.type('.');
				break;
			
			case KeyEvent.VK_SUBTRACT:
			case KeyEvent.VK_MINUS:
				cursor.type('-');
				break;
				
			case KeyEvent.VK_MULTIPLY:
				cursor.type('*');
				break;
				
			case KeyEvent.VK_ADD:
				cursor.type('+');
				break;
				
			case KeyEvent.VK_COMMA:
				cursor.type(',');
				break;
				
			case KeyEvent.VK_OPEN_BRACKET :
				cursor.type('[');
				break;
				
			case KeyEvent.VK_CLOSE_BRACKET :
				cursor.type(']');
				break;
		
			// Handle undercase characters.
			case KeyEvent.VK_A:
				cursor.type('a');
				break;
			case KeyEvent.VK_B:
				cursor.type('b');
				break;
			case KeyEvent.VK_C:
				cursor.type('c');
				break;
			case KeyEvent.VK_D:
				cursor.type('d');
				break;
			case KeyEvent.VK_E:
				cursor.type('e');
				break;
			case KeyEvent.VK_F:
				cursor.type('f');
				break;
			case KeyEvent.VK_G:
				cursor.type('g');
				break;
			case KeyEvent.VK_H:
				cursor.type('h');
				break;
			case KeyEvent.VK_I:
				cursor.type('i');
				break;
			case KeyEvent.VK_J:
				cursor.type('j');
				break;
			case KeyEvent.VK_K:
				cursor.type('k');
				break;
			case KeyEvent.VK_L:
				cursor.type('l');
				break;
			case KeyEvent.VK_M:
				cursor.type('m');
				break;
			case KeyEvent.VK_N:
				cursor.type('n');
				break;
			case KeyEvent.VK_O:
				cursor.type('o');
				break;
			case KeyEvent.VK_P:
				cursor.type('p');
				break;
			case KeyEvent.VK_Q:
				cursor.type('q');
				break;
			case KeyEvent.VK_R:
				cursor.type('r');
				break;
			case KeyEvent.VK_S:
				cursor.type('s');
				break;
			case KeyEvent.VK_T:
				cursor.type('t');
				break;
			case KeyEvent.VK_U:
				cursor.type('u');
				break;
			case KeyEvent.VK_V:
				cursor.type('v');
				break;
			case KeyEvent.VK_W:
				cursor.type('w');
				break;
			case KeyEvent.VK_X:
				cursor.type('x');
				break;
			case KeyEvent.VK_Y:
				cursor.type('y');
				break;
			case KeyEvent.VK_Z:
				cursor.type('z');
				break;
				
			default : return false;
		}
		
		return true;
	}
	
	private boolean handleUpperCase(int key)
	{
		
		switch(key)
		{
			case KeyEvent.VK_BACK_QUOTE:
				cursor.type('~');
				break;
				
			case KeyEvent.VK_SEMICOLON:
				cursor.type(':');
				break;
		
			case KeyEvent.VK_SLASH :
				cursor.type('?');
				break;
			case KeyEvent.VK_BACK_SLASH :
				cursor.type('|');
				break;
			case KeyEvent.VK_QUOTE:
				cursor.type('"');
				break;
				
			case KeyEvent.VK_EQUALS:
				cursor.type('+');
				break;
				
			case KeyEvent.VK_1:
				cursor.type('!');
				break;
				
			case KeyEvent.VK_9:
				cursor.type('(');
				break;
				
			case KeyEvent.VK_0:
				cursor.type(')');
				break;
				
			case KeyEvent.VK_8:
				cursor.type('*');
				break;
				
			case KeyEvent.VK_6:
				cursor.type('^');
				break;
				
			case KeyEvent.VK_OPEN_BRACKET :
				cursor.type('{');
				break;
				
			case KeyEvent.VK_CLOSE_BRACKET :
				cursor.type('}');
				break;
				
			case KeyEvent.VK_A:
				cursor.type('A');
				break;
			case KeyEvent.VK_B:
				cursor.type('B');
				break;
			case KeyEvent.VK_C:
				cursor.type('C');
				break;
			case KeyEvent.VK_D:
				cursor.type('D');
				break;
			case KeyEvent.VK_E:
				cursor.type('E');
				break;
			case KeyEvent.VK_F:
				cursor.type('F');
				break;
			case KeyEvent.VK_G:
				cursor.type('G');
				break;
			case KeyEvent.VK_H:
				cursor.type('H');
				break;
			case KeyEvent.VK_I:
				cursor.type('I');
				break;
			case KeyEvent.VK_J:
				cursor.type('J');
				break;
			case KeyEvent.VK_K:
				cursor.type('K');
				break;
			case KeyEvent.VK_L:
				cursor.type('L');
				break;
			case KeyEvent.VK_M:
				cursor.type('M');
				break;
			case KeyEvent.VK_N:
				cursor.type('N');
				break;
			case KeyEvent.VK_O:
				cursor.type('O');
				break;
			case KeyEvent.VK_P:
				cursor.type('P');
				break;
			case KeyEvent.VK_Q:
				cursor.type('Q');
				break;
			case KeyEvent.VK_R:
				cursor.type('R');
				break;
			case KeyEvent.VK_S:
				cursor.type('S');
				break;
			case KeyEvent.VK_T:
				cursor.type('T');
				break;
			case KeyEvent.VK_U:
				cursor.type('U');
				break;
			case KeyEvent.VK_V:
				cursor.type('V');
				break;
			case KeyEvent.VK_W:
				cursor.type('W');
				break;
			case KeyEvent.VK_X:
				cursor.type('X');
				break;
			case KeyEvent.VK_Y:
				cursor.type('Y');
				break;
			case KeyEvent.VK_Z:
				cursor.type('Z');
				break;
				
			case KeyEvent.VK_PERIOD:
				cursor.type('>');
				break;
				
			case KeyEvent.VK_MINUS:
				cursor.type('_');
				break;
				
			case KeyEvent.VK_COMMA:
				cursor.type('<');
				break;
				
			default : return false;
		}
		
		return true;
	}
	
	@Override
	public void keyR(int key)
	{
		switch(key)
		{
			case KeyEvent.VK_SHIFT :
				shift = false;
				return;
		}
	}
	
	void add(char c)
	{
		cursor.type(c);
	}
	
	public void clearInput()
	{
		setFalse();
		
		// Revert this input box.
		setText(default_text);
		
		data.clear();
		
		// -- Now hand off the new character pointer to the cursor.
		gui_cursor c = cursor;
		
		if(c != null)
		{
			c.giveNewLink(data.getIter(), 0);
		}
		
		disableTEX();
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
	
	
	public void populateInput(String text)
	{
		clearInput();
		
		int len = text.length();
		for(int i = 0; i < len; i++)
		{
			data.add(text.charAt(i));
		}
		
		refreshText();
	}
	
	public void setDefaultText(String str)
	{
		default_text = str;
	}
	
	public abstract String getSerialName();
	
	// The serialization method for a Matrix frame.
	@Override
	public void serializeTo(PrintStream stream)
	{
		// Next serialize the string.
		stream.println(StringParser.charArrToString(data.toArray()));
		
		stream.println();
	}

	/* Takes a position and returns an iterator for the data that starts pointing to the given character.
	 * 
	 * Note that position = 1 is the first character in the data.
	 * 
	 * FIXME : It currently takes linear time to align the iterator.
	 * 
	 * @see Game_Engine.GUI.Components.Input.textBased.adt_textField#getCharLink(int)
	 */
	@Override
	public IterB<Character> getCharLink(int position)
	{
		// Create.
		IterB<Character> link = data.getIter();
		
		// Process.
		for(int i = 0; i < position && link.hasNext(); i++)
		{
			link.next();
		}
		
		// Yield.
		return link;
	}

	// Provides the cursor with correct geometric coordinates.
	// Try not to poll this function, because it is not very efficient.
	// FIXME : the computation of the y coordinate might break.
	@Override
	public Pair<Integer, Integer> getCharacterLocation(int position)
	{
		int x = (int)getTextX();
		int y = (int)(getY() + getH()/2 - getTextHeight()/2);
	
		
		String sub = getText();
		
		if(sub.length() > position)
		{
			sub = getText().substring(0, position);
		}
		
		int offset = TextManager.getLen(sub, false);
		
		return new Pair<Integer, Integer>(x + offset - 2, y);
	}

	@Override
	public int getTextHeight()
	{
		return super.getTextSize();
	}
	
	/*
	 * Overrides the focus box toggling to instantiate a cursor.
	 * @see Game_Engine.GUI.Components.small.boxes.gui_focusBox#toggle()
	 */
	@Override
	public void toggle()
	{
		super.toggle();
		
		
		if(cursor == null)
		{
			createCursor();
		}
		else
		{
			cursor.setHost(this);
		}
			
	}
	
	// Puts the cursor at the correct location when it clicks on a text box.
	@Override
	public void mouseR(int mx, int my)
	{
		super.mouseR(mx, my);
		
		if(cursor == null)
		{
			createCursor();
		}
		
		int[] offsets = TextManager.getCharacterOffsets(getText(), getTextSize());
		
		// Binary search for the nearest index.
		int index = Search.bin_near(offsets, mx - getTextX());

		if(index == -1)
		{
			return;
		}
		
		index = Math.min(offsets.length - 1, index);
				
		cursor.setHost(this);
		cursor.giveNewLink(getCharLink(index), index);
		cursor.setX(getTextX() + offsets[index]);
	}
	
	private void createCursor()
	{
		cursor = new gui_cursor(this, 0);
		myContainer.obj_create(cursor);
		cursor.setDepth(getDepth() - 1);
	}
	
	private void destroyCursor()
	{
		if(cursor != null)
		{
			cursor.kill();
			cursor = null;			
		}
	}
	
	@Override
	public void setFalse()
	{
		super.setFalse();
		if(cursor != null)
		{
			destroyCursor();
		}
	}
	
	// Returns the number of characters in the text field.
	@Override
	public int getTextLength()
	{
		return data.size();
	}
	
	@Override
	public IterB<Character> getEndLink()
	{
		iter = data.getTailIter();
		return iter;
	}
	
	@Override
	protected void die()
	{
		destroyCursor();
		super.die();
	}
}
	

