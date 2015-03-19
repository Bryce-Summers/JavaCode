package Game_Engine.Engine.text;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.DoubleMath.Vector;


/*
 * The Large brackets class.
 * 
 * Written by Bryce Summers on 8 - 21 - 2013.
 * 
 * Updated : 1 - 19 - 2014 y Bryce Summers : Implemented this class.
 * 
 * Purpose : This class provides the capability for rendering large format
 * 			 parens and brackets of various types : '(, [, {, }, ], )' for use in surrounding larger in line elements,
 * 			 such as fractions, Matrices, and Vectors.
 */

public class ccLargeBrackets extends Image_vectorGeometry
{
	final char val;

	// Weight of the characters, should correspond to ccAlphabet Characters which are twice as short as these.
	// The radius of the lines that make up each character.
	private double weight;
	
	public ccLargeBrackets(int h, char c)
	{
		// Start the image off with a height twice that of its width.
		super(h/4, h, true);// Need to manually call i_geom();
		
		this.val = c;
		
		// Corresponds to ccAlphabetic weight.
		weight = 1.0*h / 40;

		set_thickness(weight);
		
		antiAliasing = 3;
		
		// Manual Call.
		i_geoms();
	}

	@Override
	public void i_geoms()
	{
		double w = room_width;
		double h = room_height;
		
		// Create different geometries based on the inserted variable.
		switch(val)
		{		
			case '(':

				i_curve(v(w - weight, weight), v(weight, h/2.0), v(w - weight, h - weight));
				break;
				
			case ')':
				i_curve(v(weight, weight), v(w - weight, h/2.0), v(weight, h - weight));
				break;
				
			case '[':
				i_line(v(w, weight), v(weight, weight), v(weight, h - weight), v(w, h - weight));
				
				break;
				
			case ']':
				i_line(v(0, weight), v(w - weight, weight), v(w - weight, h - weight), v(0, h - weight));
				break;
				
			case '{':
				Vector v1, v2;
				v1 = v(w/2, w/2);
				v2 = v(w/2, h/2 - w/2);
				
				// The top curve.
				i_curve(v(w, weight), v(w - .001, weight), v1);
				
				// The top middle curve.
				i_curve(v2, v(w/2, h/2 - w/2 + .001), v(0, h/2));
				
				// Connect the two top curves.
				i_rect(v1, v2);

				v1 = v(w/2, h - w/2);
				v2 = v(w/2, h/2 + w/2);
				
				// The bottom middle curve.
				i_curve(v2, v(w/2, h/2 + w/2 - .001), v(0, h/2));
				
				// The bottom curve.
				i_curve(v(w, h - weight), v(w - .001, h - weight), v1);
				
				// Connect the two bottom curves.
				i_rect(v1, v2);
				break;
				
			case '}':
				v1 = v(w/2, w/2);
				v2 = v(w/2, h/2 - w/2);
				
				// The top curve.
				i_curve(v(0, weight), v(.001, weight), v1);
				
				// The top middle curve.
				i_curve(v2, v(w/2, h/2 - w/2 + .001), v(w, h/2));
				
				// Connect the two top curves.
				i_rect(v1, v2);

				v1 = v(w/2, h - w/2);
				v2 = v(w/2, h/2 + w/2);
				
				// The bottom middle curve.
				i_curve(v2, v(w/2, h/2 + w/2 - .001), v(w, h/2));
				
				// The bottom curve.
				i_curve(v(0, h - weight), v(.001, h - weight), v1);
				
				// Connect the two bottom curves.
				i_rect(v1, v2);
				break;
		}
	}

	// -- Helper methods.
	
	/* Most of these methods have be replaced by image_vectorgeometry internal helper functions,
	 * and the weight value was passed in the set_thickness function.
	 * 
	 * sincerely, Bryce 4 - 30 - 2014.
	 */
	
	/*
	private void i_line(Vector ... input)
	{
		i_line(weight, input);
	}
	
	private void i_rect(Vector input1, Vector input2)
	{
		i_rect(input1, input2, weight);
	}

	private void i_round_rect(Vector input1, Vector input2)
	{
		i_round_rect(input1, input2, weight);
	}
	
	private void i_circle(Vector center)
	{
		i_circle(center, weight);
	}
	
	private void i_curve(Vector v1, Vector v2, Vector v3)
	{
		i_curve(v1, v2, v3, weight);
	}
	*/
	
	// A helper function that scales the width dimension to a percentage of the height,
	// while keeping the lines floating point secure and visible.
	protected double scaleW(double per)
	{
		room_width = (int) (room_width * per) + 1;
		return room_width;
	}
	
}
