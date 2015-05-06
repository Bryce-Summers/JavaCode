package SimpleEngine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import SimpleEngine.interfaces.Room;

/*
 * Input class, simplified by Bryce Summers on 3/18/2015
 * 
 * Handles routing User input to a user's room.
 */

public class Game_input
{

	// -- Local Variables.
	public static int mouse_x, mouse_y;
	
	public final static int LEFT_MOUSE = MouseEvent.BUTTON1;
	public final static int MIDDLE_MOUSE = MouseEvent.BUTTON2;
	public final static int RIGHT_MOUSE = MouseEvent.BUTTON3;
	
	public static int mouse_button = 0;
	
	private Room data;
	
	private key_input   l_key;
	private mouse_input l_mouse;
	private Game_output output;
	
	public Game_input(Room data_in, Game_output output_in)
	{
		// Set the Object data.
		data = data_in;
		
		// Initialize listeners.
		l_key   = new key_input();
		l_mouse = new mouse_input();
	
		// Initialize the OS JFrame.
		this.output = output_in;
		
		start();
	}

	// Enable's this input class's object stack to receive keyboard inputs.
	public void start()
	{
		output.setFocusable(true);
		output.requestFocus();
		output.addKeyListener(l_key);
		output.addMouseListener(l_mouse);
		output.addMouseMotionListener(l_mouse);
		output.addMouseWheelListener(l_mouse);
		
		//http://stackoverflow.com/questions/8275204/how-can-i-listen-to-a-tab-key-pressed-typed-in-java
		output.setFocusTraversalKeysEnabled(false);
	}
	
	// Stops this stack of objects from receiving input.
	public void stop()
	{
		output.removeKeyListener(l_key);
		output.removeMouseListener(l_mouse);
		output.removeMouseMotionListener(l_mouse);
		output.removeMouseWheelListener(l_mouse);
	}
		
	// Listen for Keyboard inputs.
	private class key_input extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
	    {
			data.keyP(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e)
		{			
			data.keyR(e.getKeyCode());
		}
		
		// FIXME: Determine if we should implement more Key input functionality.
		
	}
	
	// Listen for Mouse inputs.
	private class mouse_input extends MouseAdapter implements MouseMotionListener
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			mouse_button = e.getButton();
			
			computeMouseCoords(e);

			data.mouseP(mouse_x, mouse_y);
			data.global_mouseP();
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			
			mouse_button = e.getButton();
			
			computeMouseCoords(e);

			data.mouseR(mouse_x, mouse_y);
			data.global_mouseR();
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			computeMouseCoords(e);
			
			data.mouseM(mouse_x, mouse_y);
			data.global_mouseM(mouse_x, mouse_y);
		}
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			
			computeMouseCoords(e);
		
			data.mouseD(mouse_x, mouse_y);
			data.global_mouseD(mouse_x, mouse_y);
		}
		
		public void mouseWheelMoved(MouseWheelEvent e)
		{			
			int rotate = e.getWheelRotation();
			data.global_mouseScroll(rotate);
		}
	}
	
	public KeyAdapter getKeyListener()
	{
		return new key_input();
	}
	
	public MouseAdapter getMouseListener()
	{
		return new mouse_input();
	}
	
	public MouseMotionListener getMouseMotionListener()
	{
		return new mouse_input();
	}
	
	public MouseWheelListener getMouseWheelListener()
	{
		return new mouse_input();
	}
	
	// Takes a MouseEvent and computes the coordinates of the mouse in the game.
	private void computeMouseCoords(MouseEvent e)
	{
				
		// Calculate the appropriate x and y coordinates,
		// in relation to the distorted image on the screen.
		mouse_x = e.getX() - output.x1;
		mouse_y = e.getY() - output.y1;
		
		mouse_x = mouse_x * data.getW()/(output.x2 - output.x1);
		mouse_y = mouse_y * data.getH()/(output.y2 - output.y1);
	}	
	
}