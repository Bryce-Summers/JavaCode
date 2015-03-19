package util;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;


// Generates fresh key events.

// Written by Bryce Summers on 6 - 17 - 2013.


public class KeyEventFactory
{

	public static KeyEvent getEvent(int keyCode)
	{
		JTextField inputField = new JTextField();
		
		return new KeyEvent(inputField, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, keyCode, 'Z');
		
	}

}
