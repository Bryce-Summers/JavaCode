package util;

import java.awt.event.KeyEvent;


/* Key parsing class.
 * 
 * Written by Bryce SUmmers on 6 - 29 - 2013.
 * 
 *  Purpose : This class is intended to help in maintaining the functionality of my key logged programs for the future,
 *  		  when java may change the key codes.
 *  
 *  The java KeyEvent class represents all of the key's via various integers.
 *  	The integers may change, but the static labels will not.
 *  	Therefore, this class provides a function to convert from key code to the string that represents the label.
 */

public class KeyParser
{
	public static String getKeyText(int keyCode)
	{
		
		// Handle java idiosyncratic cases.
		switch (keyCode)
		{
			case KeyEvent.VK_BACK_SPACE :
				return "KeyEvent.VK_BACK_SPACE";
			case KeyEvent.VK_DIVIDE :
				return "KeyEvent.VK_DIVIDE";
			case KeyEvent.VK_MULTIPLY :
				return "KeyEvent.VK_MULTIPLY";
			case KeyEvent.VK_SUBTRACT :
				return "KeyEvent.VK_SUBTRACT";
			case KeyEvent.VK_ADD :
				return "KeyEvent.VK_ADD";
			case KeyEvent.VK_DECIMAL:
				return "KeyEvent.VK_DECIMAL";
			case KeyEvent.VK_PRINTSCREEN:
				return "KeyEvent.VK_PRINTSCREEN";
		}
		
		String raw_text = KeyEvent.getKeyText(keyCode);
		
		String output;
		
		// Transform all spaces into underscores
		output = raw_text.replaceAll(" ", "_").toUpperCase().replace("-", "");
		
		return "KeyEvent.VK_" + output; 
	}
	
	public static int getKeyEvent(char c)
	{
		// Handle java idiosyncratic cases.
		switch (c)
		{
		
		// Alphabetic characters.
		case 'A' :
		case 'a':
			return KeyEvent.VK_A;
		case 'B' :
		case 'b':
			return KeyEvent.VK_B;
		case 'C' :
		case 'c':
			return KeyEvent.VK_C;
		case 'D' :
		case 'd':
			return KeyEvent.VK_D;
		case 'E' :
		case 'e':
			return KeyEvent.VK_E;
		case 'F' :
		case 'f':
			return KeyEvent.VK_F;
		case 'G' :
		case 'g':
			return KeyEvent.VK_G;
		case 'H' :
		case 'h':
			return KeyEvent.VK_H;
		case 'I' :
		case 'i':
			return KeyEvent.VK_I;
		case 'J' :
		case 'j':
			return KeyEvent.VK_J;
		case 'K' :
		case 'k':
			return KeyEvent.VK_K;
		case 'L' :
		case 'l':
			return KeyEvent.VK_L;
		case 'M' :
		case 'm':
			return KeyEvent.VK_M;
		case 'N' :
		case 'n':
			return KeyEvent.VK_N;
		case 'O' :
		case 'o':
			return KeyEvent.VK_O;
		case 'P' :
		case 'p':
			return KeyEvent.VK_P;
		case 'Q' :
		case 'q':
			return KeyEvent.VK_Q;
		case 'R' :
		case 'r':
			return KeyEvent.VK_R;
		case 'S' :
		case 's':
			return KeyEvent.VK_S;
		case 'T' :
		case 't':
			return KeyEvent.VK_T;
		case 'U' :
		case 'u':
			return KeyEvent.VK_U;
		case 'V' :
		case 'v':
			return KeyEvent.VK_V;
		case 'W' :
		case 'w':
			return KeyEvent.VK_W;
		case 'X' :
		case 'x':
			return KeyEvent.VK_X;
		case 'Y' :
		case 'y':
			return KeyEvent.VK_Y;
		case 'Z' :
		case 'z':
			return KeyEvent.VK_Z;
			
		// -- Symbolic characters.
		case '+' :
			return KeyEvent.VK_PLUS;
		case '-' :
			return KeyEvent.VK_MINUS;

		// Numeric Characters.
		case '0' : return KeyEvent.VK_0;
		case '1' : return KeyEvent.VK_1;
		case '2' : return KeyEvent.VK_2;
		case '3' : return KeyEvent.VK_3;
		case '4' : return KeyEvent.VK_4;
		case '5' : return KeyEvent.VK_5;
		case '6' : return KeyEvent.VK_6;
		case '7' : return KeyEvent.VK_7;
		case '8' : return KeyEvent.VK_8;
		case '9' : return KeyEvent.VK_9;
		}
		
		throw new Error("Character '" + c + "' is not yet supported.");
	}
	
	public static Integer getNumpadEvent(int i)
	{
		switch(i)
		{
			// Numeric Characters.
			case 0 : return KeyEvent.VK_NUMPAD0;
			case 1 : return KeyEvent.VK_NUMPAD1;
			case 2 : return KeyEvent.VK_NUMPAD2;
			case 3 : return KeyEvent.VK_NUMPAD3;
			case 4 : return KeyEvent.VK_NUMPAD4;
			case 5 : return KeyEvent.VK_NUMPAD5;
			case 6 : return KeyEvent.VK_NUMPAD6;
			case 7 : return KeyEvent.VK_NUMPAD7;
			case 8 : return KeyEvent.VK_NUMPAD8;
			case 9 : return KeyEvent.VK_NUMPAD9;
		}
		
		// The key does not exist.
		return null;
	}
}
