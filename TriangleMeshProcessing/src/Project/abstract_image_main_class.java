package Project;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import BryceImages.GUI.Display;
import BryceImages.Operations.ImageFactory;

/* An abstract version of my standard main method code.
 * Written by Bryce Summers on 12/28/2015.
 * 
 * Adopted from another of my main classes on 12/28/2015.
 * 
 * - Starts up a visual window with an image that can be manipulated.
 * 
 */

public abstract class abstract_image_main_class
{
	public BufferedImage image;
	
	protected void startNormal()
	{
		// -- Local Variables.
		
		String title = "Half Edge Mesh Project";
		
		// Store the dimensions of the user's computer screen.
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Set the GUI widow to a custom dimension.
		
		this.image = ImageFactory.blank((int)dim.getWidth(), (int)dim.getHeight());
		
		dim = new Dimension(image.getWidth(), image.getHeight());
		
		Display panel = newDisplay(dim, image);

		// Start the Operating system dependent Graphic user interface with the given display.
		iOSGUI(title, panel, dim);

		
	}
	
	// -- Setup the basic Operating system windows.
	private void iOSGUI(String title, Display panel, Dimension dim)
	{
	
		// Create the Frame.
		JFrame frame = new JFrame(title);
		frame.setUndecorated(true);
		frame.setLocation(0, 0);
		
		// Determines if the widow can be resized for decorated mode.
		frame.setResizable(false);
		frame.setSize(dim);
		
		// Allows the close button to terminate the program in decorated mode.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Tell the Frame that it will be populated by the JPanel
		frame.setContentPane(panel);
		
		//The frame must be reset to visible after its content pane is changed.
		frame.setVisible(true);
	}
	
	private Display newDisplay(Dimension dim, BufferedImage image)
	{		
		// Create a JPanel inside of the Frame.
		Display panel = new Display(dim);
				
		panel.setImage(image);
		
		return panel;
	}
}