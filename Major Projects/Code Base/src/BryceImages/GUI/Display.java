package BryceImages.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;


import util.ImageUtil;

/* 
 * Display
 * Written by Bryce Summers 6/30/2012.
 * Revised : 4 - 6 - 2013.
 * Purpose: To display a given Buffered Image to the screen at a desired frame rate.
 *			
 * Capabilities: - Can handle User input such as the escape key for termination.
 *			     - Can drive an animation by feeding a time step to a drawer.
 */



public class Display extends JPanel
{

	// Standard Eclipse encouraged boiler plate.
	private static final long serialVersionUID = 1L;

	// Target Frames per second, 
	// Note: This will most likely be ignored by the JRE
	//------based on how long it takes to render each row of the image;
	private final int FPS = 30;	
	
	
	//--Local Variables
	private       Dimension dim; //The dimensions of the image being rendered.
	private final Dimension screenDim;
	
	// Image is a pointer to the image being rendered, 
	// screen is used as a buffer to draw things onto so I can draw accurate alpha values and not have any flicker.
	BufferedImage image, screen;
		
	int w, h;// Width, Height of the image to be drawn to the screen.
	
	int time = 0;
	
	public Display(Dimension dim)
	{		
		screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Bounds the width and height variables within the screen, so no pixels that can't be seen are drawn to the screen.
		w = (int) Math.min(screenDim.getWidth() , dim.getWidth ());
		h = (int) Math.min(screenDim.getHeight(), dim.getHeight());
		
		screen = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		// Now Initialize timers
		iTimers();
	}
	
	// Give the Display an Image to keep track of.
	public void setImage(BufferedImage i)
	{
		// Tell this Display what its image is.
		image = i;
		
		dim = new Dimension(i.getWidth(), i.getHeight());
		
		// Bounds the width and height variables within the screen, so no pixels that can't be seen are drawn to the screen.
		w = (int) Math.min(screenDim.getWidth() , dim.getWidth ());
		h = (int) Math.min(screenDim.getHeight(), dim.getHeight());
	}

	// Initializes main loop timer and all user input listeners
	public void iTimers()
	{
		 Timer t;
		 t = new Timer((int)(1000 / FPS),new Stepy());
		 t.start();
		 setFocusable(true);
		 addKeyListener(new KeyInput());
		 requestFocus();	
		 MouseInput mIn=new MouseInput();
		 addMouseListener(mIn);
		 addMouseMotionListener(mIn);
	}// End of setupTimers
	
	// An event loop that will draw the image to the screen properly while preserving the appearance of the correct alpha values.
	// This should be invoked at the desired number of Frames per Second.
	private class Stepy implements ActionListener//Step Event This is where the main update loop will be;
	{
		public void actionPerformed(ActionEvent e)
		{
			// Draw the section of the image that is being rendered that will fit on the screen.
			// Do not waste any time processing parts of the image that are not on the screen.
			// Note: this is because the width and height variables have already been bounded.
		
			Graphics g, g2;
			
			//-Draw a white background, then draw the renderedImage onto the screen buffer.
			g2 = screen.getGraphics();
			g2.fillRect(0, 0,w,h);
			g2.drawImage(image, 0, 0, w, h, 0, 0, w, h, null);
			
			// Now draw the screen buffer onto the JPanel.
			g = getGraphics();
			
			if(g != null)
			{
				g.drawImage(screen, 0, 0, w, h, null);
			}
		}
	}// End of Stepy
	
	// A keyboard listener to handle Keyboard Input
	private class KeyInput extends KeyAdapter//allows for the exiting of the program, and the restoring of the screen pixels
	{
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				System.exit(0);
			}
			else if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				ImageUtil.saveImage(image, "BrendSave");
			}
		}
		//--------------------------------------------------------------
		public void keyReleased(KeyEvent e)
		{
		}
	}// End of KeyInput	 
	
	// A mouse listener to handle mouse input
	private class MouseInput extends MouseAdapter implements MouseMotionListener
	{
		public void mousePressed(MouseEvent e)
		{
		}
		public void mouseReleased(MouseEvent e)
		{
		}
		public void mouseMoved(MouseEvent e)
		{
		}
		public void mouseDragged(MouseEvent e)
		{
		}
	}//End of MouseInput

}
