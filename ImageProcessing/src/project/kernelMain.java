package project;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

import util.ImageUtil;
import BryceImages.GUI.Display;


/* 
 * Basic Main Class, Written and maintained by Bryce Summers
 * Updated: 12 - 21 - 2012:
 * 		- This file has been cleaned up to streamline my code base.
 * 		- This file was modified to lend itself to the new Render
*/

public class kernelMain
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
		
		BufferedImage image = ImageUtil.loadImage(new File("image.png"));
		
		dim = new Dimension(image.getWidth(), image.getHeight());
		
		Display panel = newDisplay(dim, image);

		// Start the Operating system dependent Graphic user interface with the given display.
		iOSGUI(title, panel, dim);
		
		
		// Box Blur.
		{
			int N = 11;
			
			double[][] mask = new double[N][N];
			
			for(int i = 0; i < N; i++)
			{ 
				mask[0] = new double[N];
			}
			
			double total = 0;
			for(int x = 0; x < N; x++)
			for(int y = 0; y < N; y++)
			{
								
				total += 1;
				
				mask[x][y] = 1; 
			}

			// Inputs : image, mask, centering parameters for the mask, a normalizing coefficient.
			BufferedImage output = kernelProcessor.proccessImage(image, mask, N/2, N/2, 1.0/total);

			ImageUtil.saveImage(output, "BoxBlur.png");
		}
		
		// NxN Gaussian
		{
			int N = 11;
			
			double[][] mask = new double[N][N];
			
			for(int i = 0; i < N; i++)
			{ 
				mask[0] = new double[N];
			}
			
			double total = 0;
			for(int x = 0; x < N; x++)
			for(int y = 0; y < N; y++)
			{
				double x_val = kernelProcessor.evaluateGaussian(x, N/2, N/3); 
				double y_val = kernelProcessor.evaluateGaussian(y, N/2, N/3);
				
				double val = x_val*y_val;
				
				total += val;
				
				mask[x][y] = val; 
			}
			

			BufferedImage output = kernelProcessor.proccessImage(image, mask, N/2, N/2, 1.0/total);
			
			ImageUtil.saveImage(output, "Gaussian.png");
		}
		
		// Gaussian x
		{
			double[][] mask = new double[1][7];
			
			mask[0] = new double[7];
			
			double total = 0.0;
			
			for(int i = 0; i < 7; i++)
			{
				double val = kernelProcessor.evaluateGaussian(i, 3,  2); 
				total += val;
				mask[0][i] = val;
			}
			
			BufferedImage output = kernelProcessor.proccessImage(image, mask, 3, 0, 1.0 / total);
			
			ImageUtil.saveImage(output, "GaussianX.png");
		}
		
		// Gaussian y
		{
			double[][] mask = new double[7][1];
			
			double total = 0.0;
			
			for(int i = 0; i < 7; i++)
			{
				double val = kernelProcessor.evaluateGaussian(i, 3,  2); 
				total += val;
				mask[i] = new double[]{val};
			}
			
			BufferedImage output = kernelProcessor.proccessImage(image, mask, 0, 3, 1.0 / total);
			
			ImageUtil.saveImage(output, "GaussianY.png");
		}
		
		// Sharpening.
		{
			double[][] mask = new double[3][3];
			
		
			mask[0] = new double[]{0, -1, 0};
			mask[1] = new double[]{-1, 5, -1};
			mask[2] = new double[]{0, -1, 0};
			
			BufferedImage output = kernelProcessor.proccessImage(image, mask, 1, 1, 1.0);
			
			ImageUtil.saveImage(output, "Sharpen.png");
		}

		// Sobel X
		{
			double[][] mask = new double[3][3];
			
		
			mask[0] = new double[]{-1, 0,  1};
			mask[1] = new double[]{-2, 0,  2};
			mask[2] = new double[]{-1, 0,  1};
			
			BufferedImage output = kernelProcessor.proccessImage(image, mask, 1, 1, 1.0);
			
			ImageUtil.saveImage(output, "SobelX.png");
		}
		
		// Sobel Y
		{
			double[][] mask = new double[3][3];
			
		
			mask[0] = new double[]{-1, -2, -1};
			mask[1] = new double[]{ 0,  0,  0};
			mask[2] = new double[]{ 1,  2,  1};
			
			BufferedImage output = kernelProcessor.proccessImage(image, mask, 1, 1, 1.0);
			
			ImageUtil.saveImage(output, "SobelY.png");
		}
		
		System.out.println("Done!");
		
		// 		
		//panel.setImage(output);

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
	
	public static Display newDisplay(Dimension dim, BufferedImage image)
	{
		
		// Create a JPanel inside of the Frame.
		Display panel = new Display(dim);
				
		panel.setImage(image);
		
		return panel;
	}
}

