package Game_Engine.Engine.engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import BryceImages.Operations.Drawing;

import Game_Engine.Engine.Objs.Obj_Container;

/*
 * Output class, conceived by Bryce Summer on 12 - 31 - 2012.
 * Purpose:
 * 		This class handles Audio and visual output for my Game engine.
 * 		This class also handles communication with the Operating system.
 *		FIXME: Handle sounds.	
 */

public class Game_output extends JFrame
{
	private static final long serialVersionUID = -70567293598120131L;

	// -- Local Variables
	public static Color C_background = Color.white;
	
	// Coordinates of the scaled image on the screen.
	int x1 = 0, x2 = 1, y1 = 0, y2 = 1;
	
	// -- Constructor
	
	// Top left corner positioned.
	public Game_output(String name, Dimension dim)
	{
		// Let JFrame do its thing.
		super(name);
		
		setSize(dim);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // We do not want to draw OS dependent borders.
        setUndecorated(true);
        setResizable(false);
        
        // Fairly important.
        setVisible(true);
        
        // Create an escape timer, to ensure that escape presses always work.
        addKeyListener(new Escaper());
        
	}
	
	public Game_output(String name, Rectangle r)
	{
		// Let JFrame do its thing.
		super(name);
	
		Dimension dim = new Dimension((int)r.getWidth(), (int)r.getHeight());
		
		setSize(dim);
        setLocation((int)r.getX(), (int)r.getY());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // We do not want to draw OS dependent borders.
        setUndecorated(true);
        setResizable(false);
        
        // Fairly important.
        setVisible(true);
        
        // Create an escape timer, to ensure that escape presses always work.
        addKeyListener(new Escaper());
        
	}

	// Draws final frames onto the screen.
	public void draw(BufferedImage src)
	{
		int[] coords = Drawing.draw_scaled(getGraphics(), getW(), getH(), src);
		
		x1 = coords[0];
		y1 = coords[1];
		x2 = coords[2];
		y2 = coords[3];
	}	
	
	// -- Data access methods.
	
	public int getW()
	{
		return getWidth();
	}
	
	public int getH()
	{
		return getHeight();
	}
	
	public Color getColor()
	{
		return C_background;
	}
	
	public void setColor(Color color)
	{
		C_background = color;
	}

 	// Special escape Looper.
	private class Escaper extends KeyAdapter
	{

		@Override
		public void keyPressed(KeyEvent e)
	    {
			// -- Handle escape key presses.
		 	if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		    {
		 	  System.exit(0);
		    }
		 	
		 	// Special input for global movie proxy cursor.
			if(Obj_Container.global_proxy_cursor != null)
			{
				Obj_Container.global_proxy_cursor.keyPSpecial(e.getKeyCode());
			}
		 	
	    }
	}
	
}
