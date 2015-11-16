package Project_PhotonTracer2D;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import BryceImages.GUI.Display;
import BryceImages.Rendering.ColorCalculator;
import BryceImages.Rendering.StartRender;




/* 
 * Basic Main Class, Written and maintained by Bryce Summers
 * Updated: 12 - 21 - 2012:
 * 		- This file has been cleaned up to streamline my code base.
 * 		- This file was modified to lend itself to the new Render
*/

public class imageMain
{
	
	public static void main(String[] args)
	{
		// Render an image in a JPanel using my rendering code.
		startNormal();
		
		// Directly create and save an image using my Graphic User Interface Rendering pipeline.
		//generateGUIImage();
		
	}
	
	public static void startNormal()
	{
		// -- Local Variables.
		
		String title = "Ray Tracer by Bryce Summers";
		
		// Store the dimensions of the user's computer screen.
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Set the GUI widow to a custom dimension.
		dim = new Dimension(1000, 1000);

		// 100 thousand photons is a good number for testing.
		ColorCalculator cc = new scene_lines(1000, 1000, 300000, 30.0);

		Display panel = startRenderPanel(dim, cc);

		// Start the Operating system dependent Graphic user interface with the given display.
		iOSGUI(title, panel, dim);
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
	
	public static Display startRenderPanel(Dimension dim, ColorCalculator cc)
	{
		// -- Tell the renderer to start rendering the image.
		StartRender r = new StartRender(false);
		
		// Create a JPanel inside of the Frame.
		Display panel = new Display(dim);
		r.sendUpdates(panel);
		
		BufferedImage image = r.render(cc);
		panel.setImage(image);
		
		return panel;
	}
}

