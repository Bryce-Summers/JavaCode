package Projects.Math.openinganimation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import BryceMath.Calculations.Colors;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;

/*
 * The opening animation class.
 * 
 * Written by Bryce SUmmers on 8 - 25 - 2013.
 * 
 * Purpose : This class creates a simple animation to be displayed in the
 * 			 opening screen of the Summers CAMP (Computer Aided Math Program).
 * 
 * The animation consists of several layers of lines running across the screen.
 */

public class Animation extends Obj
{
	int length;
	
	int magnitude;
	
	int room_h;
	
	// THe current x coordinate of the left most line in layer 1.
	private int x_1 = 0;
	
	// THe data for the evaluations of the function for layer1.
	private int[] layer1;
	
	private int speed_1 = 5;
	
	private int x_2 = 0;
	
	// Layer 2.
	private int[] layer2;
	private int speed_2 = 10;
	
	private int[] layer3;
	private int speed_3 = 15;
	private int x_3 = 0;
	
	// Gray
	private static Color c1 = Colors.Color_hsv(0, 10, 80);
	
	// Blue
	private static Color c2 = Colors.Color_hsv(190, 30, 100);
	
	// Blue
	private static Color c3 = Colors.Color_hsv(260, 10, 100);

	
	public Animation()
	{
		super(0, 0);
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
		
		int w = myContainer.getW();
		int h = myContainer.getH();
		room_h = h;

		// initialize function parameters.
		length = w;
		
		setY(h);
		
		// Initialize the first layer.
		layer1 = new int[length];
		
		for(int x = 0; x < length; x++)
		{
			layer1[x] = foo(x);
		}
		
		// Initialize the second layer.
		layer2 = new int[length];
		
		for(int x = 0; x < length; x++)
		{
			layer2[x] = foo(x);
		}
		
		// Initialize the third layer.
		layer3 = new int[length];
		
		for(int x = 0; x < length; x++)
		{
			layer3[x] = foo(x);
		}
	}
	
	public int foo(double x)
	{
		double y = room_h/6*Math.cos(x/length) + room_h/6;

		return (int)y;
	}

	// The animating part.
	@Override
	public void update()
	{
		/* Constant time animation buffer setting 
		 * proportional to the number of columns that are modified.
		 */
		
		for(int i = 0; i < speed_1; i++)
		{
			layer1[x_1 % length] = foo(x_1 + length + 1);
			x_1++;
		}
		
		for(int i = 0; i < speed_2; i++)
		{
			layer2[x_2 % length] = foo(x_2 + length + 1);
			x_2++;
		}
		
		for(int i = 0; i < speed_3; i++)
		{
			layer3[x_3 % length] = foo(x_3 + length + 1);
			x_3++;
		}
		
	}
	
	public void draw(ImageB image, AffineTransform AT)
	{
		Graphics2D g = image.getGraphics();
		
		for(int x = 0; x < length; x++)
		{
			// Draw the first layer lines.
			int y = room_h - layer1[(x_1 + x) % length];
			g.setColor(c1);
			
			// bottom (room_h) to y;
			drawLine(g, AT, x, room_h, x, y);
			
			// Draw the second layer lines.
			int y2 = y - layer2[(x_2 + x) % length];
			g.setColor(c2);
			
			// y to y2;
			drawLine(g, AT, x, y, x, y2);
			
			// Draw the second layer lines.
			int y3 = y2 - layer3[(x_3 + x) % length];
			g.setColor(c3);
			
			// y2 to y3;
			drawLine(g, AT, x, y2, x, y3);
		}
	}

}
