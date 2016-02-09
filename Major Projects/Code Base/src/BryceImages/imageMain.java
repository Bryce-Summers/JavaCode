package BryceImages;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import util.FullFontExporter;
import util.ImageUtil;
import util.StringParser;
import BryceImages.ColorCalculators.ccCircleAliasing;
import BryceImages.ColorCalculators.ccHeart;
import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.GUI.Display;
import BryceImages.Operations.ImageFactory;
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

public class imageMain
{
	
	public static void main(String[] args)
	{
		// Render an image in a JPanel using my rendering code.
		//startNormal();
		
		// Directly create and save an image using my Graphic User Interface Rendering pipeline.
		generateGUIImage();
		
	}
	
	public static void startNormal()
	{
		// -- Local Variables.
		
		String title = "Brend 3, by Bryce Summers";
		
		// Store the dimensions of the user's computer screen.
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Set the GUI widow to a custom dimension.
		dim = new Dimension(1080, 1080);
		
		// Decide which ColorCalculator we want to render.
		//ColorCalculator cc = new ccAliased(dim);
		//ColorCalculator cc = new ccBioHazard(dim);

		//ColorCalculator cc = new ccBall(dim);
		//ccPerlinNoise cc = new ccPerlinNoise(dim, 0, 1.0/2, 7);
		
		//ColorCalculator cc = new ccCutOuts(dim);
		
		//ColorCalculator cc = new ccNew(dim);
		//ColorCalculator cc = new ccOpen(dim);
		//ColorCalculator cc = new ccPolygon(dim);
		
		//ColorCalculator cc = new ccKellyJoBike(dim);
				
		//ColorCalculator cc = new ccPineapple(dim);
		//ColorCalculator cc = new ccMandelbulb2(dim);
		//ColorCalculator cc = new ccMandelBox(dim);
		
		//ColorCalculator cc = new ccTree(dim);
		//ColorCalculator cc = new ccGraphCurves(dim);
		
		
		//ColorCalculator cc = new ccAlphabet('B', 1080);
		//ColorCalculator cc = new ccGreekAlphabet('t', (int)dim.getHeight());
		
		
		//ColorCalculator cc = new cc2DLighting(dim);
		//ColorCalculator cc = new ccUndo(dim);
		
		//ColorCalculator cc = new ccCursor(500, 1000);
		
			
		//ColorCalculator cc = new ccButton(700, 100);
		
		//ColorCalculator cc = new ccMandelbrot(dim);
		//ColorCalculator cc = new BryceImages.ColorCalculators.ccHeart(dim);
		
		//ColorCalculator cc = new ccSphere(dim);
		//ColorCalculator cc = new ccLine(dim);
		//ColorCalculator cc = new ccACM(dim);
		//ColorCalculator cc = new ccField(dim);
		//ColorCalculator cc = new ccFunction(dim);
		//ColorCalculator cc = new ccHeart(dim);
		// cc = new ccBoring(dim);
		
		//ColorCalculator cc = new ccCurve(dim);
		
		ColorCalculator cc = new ccCircleAliasing(dim);
		
		
		/*
		
 		StartRender r = new StartRender(true);
 		
		ccPerlinNoise cc = new ccPerlinNoise(dim, 0, 1.0/2, 7);
 		
		for(int i = 0; i < 60; i++)
		{
			cc.setTime(i);
			BufferedImage image = r.render(cc);
			ImageUtil.saveImage(image, "PerlinNoiseAnim_" + i);
			System.out.println(i + " Done");
		}
		
		*/
		
		Display panel = startRenderPanel(dim, cc);
		
		// Start the Operating system dependent Graphic user interface with the given display.
		iOSGUI(title, panel, dim);
	}
	
	private static void generateGUIImage()
	{
		FullFontExporter.exportBryceFont2();
		
		
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

