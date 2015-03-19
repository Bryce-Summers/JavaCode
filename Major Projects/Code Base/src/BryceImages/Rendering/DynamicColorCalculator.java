package BryceImages.Rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;


/*
 * Dynamic Color Calculator.
 * 
 * Written by Bryce Summers on 6 - 1 - 2014.
 * 
 * Purpose : Extends the color calculator functionality by allowing it to specify a scaling factor and
 * 			 a centered position in space.
 * 
 * Should be used with LayerPlayers.
 * 
 * See LayerAnimations Project.
 * 
 */

public abstract class DynamicColorCalculator extends ColorCalculator
{
	
	// -- Local variables.
	double x_scale = 1.0;// Ratio of raw coordinates to rendered coordinates.
	double y_scale = 1.0;
	
	protected double x_pos = 0.0;
	protected double y_pos = 0.0;
	
	protected Color C_BACKGROUND = Color.WHITE;
	
	// -- Constructors.
	
	// Simple constructor with default values.
	public DynamicColorCalculator(Dimension dim)
	{
		super(dim);
	}
	
	public DynamicColorCalculator(int w, int h)
	{
		super(w, h);
	}
	
	public DynamicColorCalculator(double x, double y,
								  double scale_x, double scale_y,
								  Dimension dim)
	{
		super(dim);
		
		x_scale = scale_x;
		y_scale = scale_y;
		
		check_scale_legality();
	
		x_pos = x;
		y_pos = y;
	}

	public DynamicColorCalculator(double x, double y,
			  					  double scale_x, double scale_y,
			  					  int width, int height)
	{
		super(width, height);
		
		x_scale = scale_x;
		y_scale = scale_y;
		
		check_scale_legality();
		
		x_pos = x;
		y_pos = y;
	}

	// -- Rendering data functions.
	
	// Transforms the input coordinates according to the local variables and then queries the raw data.
	@Override
	public Color getColor(double x, double y)
	{
		double final_x = (x - room_width/2.0) /x_scale + x_pos;
		double final_y = (y - room_height/2.0)/y_scale + y_pos;
		
		return getRawColor(final_x, final_y);
	}	
	
	// This function is used to generate the raw data from the color calculator.
	public abstract Color getRawColor(double x, double y);
	
	
	// -- Dynamic Interface functions for animation and other applications.
	
	// Increments the x coordinate by 1 pixel.
	public void incX()
	{
		incX(1);
	}
	
	public void incY()
	{
		incY(1);
	}
	
	// Increments coordinates by the given number of pixels.
	public void incX(int pixels)
	{
		x_pos += x_scale*pixels;
	}
	
	public void incY(int pixels)
	{
		y_pos += x_scale*pixels;
	}
	
	// Mutates the scale factors.
	public void setScale(double x, double y)
	{
		x_scale = x;
		y_scale = y;
		
		check_scale_legality();
	}
	
	private void check_scale_legality()
	{
		if(x_scale == 0 || y_scale == 0)
		{
			throw new Error("Error : Cannot have scales of 0, they are degenerate");
		}
	}
	
	
	// These should be overrided with more efficient methods.
	// These functions allow for a dcc to arbitrarily render a row or column efficiently.
	public BufferedImage getRow(double row)
	{
		BufferedImage output = blankRow();
		
		for(int x = 0; x < room_width; x++)
		{
			output.setRGB(x, 0, getColor(x, row).getRGB());
		}
		
		return output;
	}
	
	// These should be overrided with more efficient methods.
	// These functions allow for a dcc to arbitrarily render a row or column efficiently.
	public BufferedImage getCol(double col)
	{
		BufferedImage output = blankCol();
		
		for(int y = 0; y < room_height; y++)
		{
			output.setRGB(y, 0, getColor(col, y).getRGB());
		}
		
		return output;
	}
	
	// Helper functions for use in the getCol() and getRow() methods.
	// Each of these return a 1 pixel high or wide row.
	protected BufferedImage blankCol()
	{
		return ImageFactory.ColorRect(C_BACKGROUND, 1, room_height);
	}
	
	protected BufferedImage blankRow()
	{
		return ImageFactory.ColorRect(C_BACKGROUND, room_width, 1);
	}
	
}
