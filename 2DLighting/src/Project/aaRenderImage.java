package Project;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import util.ImageUtil;
import BryceImages.ColorCalculators.ccHeart;
import BryceImages.GUI.Display;
import BryceImages.Operations.ImageFactory;
import BryceImages.Operations.ImageProccess;
import BryceImages.Rendering.ColorCalculator;
import BryceImages.Rendering.StartRender;
import BryceMath.Calculations.Colors;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.gui_button;


/* 
 * Basic Main Class, Written and maintained by Bryce Summers
 * Updated: 12 - 21 - 2012:
 * 		- This file has been cleaned up to streamline my code base.
 * 		- This file was modified to lend itself to the new Render
*/

public class aaRenderImage
{
	
	public static void main(String[] args)
	{
		// Render an image in a JPanel using my rendering code.
		startNormal();				
	}
	
	public static void startNormal()
	{
		// -- Local Variables.
		
		String title = "Fern Jaws By Bryce Summers";
		
		// Store the dimensions of the user's computer screen.
		Dimension screen_dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		
		// -- The dimension of the final image. 
		Dimension image_dim = new Dimension(1920, 1080);
		
		BufferedImage image = ImageFactory.blank((int)image_dim.getWidth(), (int)image_dim.getHeight());
		
		Display panel = new Display(screen_dim);
		panel.setImage(image);
		
		// Start the Operating system dependent Graphic user interface with the given display.
		iOSGUI(title, panel, screen_dim);
		
		
		// -- Perform the Transformations.
		//ImageProccess proc1 = new FernJaws();
		ImageProccess proc1 = null;
		proc1.proccess(image);
	}
	
	// -- Setup the basic Operating system windows.
	public static void iOSGUI(String title, Display panel, Dimension dim)
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
	
}

