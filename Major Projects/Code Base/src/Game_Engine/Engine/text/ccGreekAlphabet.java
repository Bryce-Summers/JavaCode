package Game_Engine.Engine.text;

import BryceMath.DoubleMath.Vector;


/* Greek characters.
 * 
 * Elegant Vector Greek ALphabet.
 * 
 * This is the third alphabet that I have written.
 * 
 * Written by Bryce Summers on 9 - 4 - 2013.
 */

/* Note :	For the helper functions and implementation information look at the superclass : ccAlphabet.
 * 			This class simply overrides the geom initialization function in the super class,
 *			so that we can reuse the character space.
 *
 *		
 *	The characters exist in a rectangular region from(0, 0) in the left hand corner,
 *	and (room_width, room_height) in bottom right corner.
 */

public class ccGreekAlphabet extends ccAlphabet
{
	public ccGreekAlphabet(char c, int h)
	{
		super(c, h);
	}

	@Override
	public void i_geoms()
	{
		antiAliasing = 4;
		
		double w = room_width;
		double h = room_height;
		
		Vector up 	 = v(0, -1);
		Vector right = v(1, 0);
		Vector down  = v(0, 1);
		Vector left  = v(-1, 0);
		
		// Create different geometries based on the inserted variable.
		switch(val)
		{
			// Lambda.
			case 'l':
		
				Vector v1, v2, v3, v4, v5, v6, v7;
				
				// The point of intersection in a lambda character.
				v1 = v(w/2 - weight, h/4);
				
				// The top curve.
				i_curve(v(weight, h/4), v(w/4, weight), v1);
				
				// The left leg
				i_curve(v1, v1.add(down.mult(.001)), v(0, h));
				
				// The right leg
				i_curve(v1, v1.add(down.mult(.001)), v(w, h));
				
				break;
				
			// Alpha.
			case 'a':
			
				//, v2, v3, v4;
					
				// The left point on an alpha character.
				v1 = v(weight, h/2);

				// top.
				v2 = v(w/2, weight);
				
				// Bottom.
				v3 = v(w/2, h - weight);
				
				// Top left curve.
				i_curve(v1.add(down.mult(.0001)), v1.add(up.mult(.0001)), v2);
				
				// Bottom Left curve.
				i_curve(v1.add(up.mult(.0001)), v1.add(down.mult(.0001)), v3);
				
				double x_offset = .81;
				double y_offset = .25;
				
				// Right up curve.
				v6 = v(w*(x_offset) - weight, h*(1 - y_offset));
				i_curve(v3.add(left.mult(.0001)), v3.add(right.mult(.0001)), v6);
				
				// Right down curve.
				v7 = v(w*(x_offset) - weight, h*y_offset);
				i_curve(v2.add(left.mult(.0001)), v2.add(right.mult(.0001)), v7);
				
				// THe final connections.
				i_rect(v6, v(w - weight, weight));
				i_rect(v7, v(w - weight, h - weight));
				
				break;
				
			case 'b':
				
				w = scaleW(.8);
				

				
				double b_height = h*.44;
				double curve_x = w - (h - b_height)/2 - weight/2;
				
				double leftX = h/6;

				v1 = v(leftX, weight);
				v2 = v(leftX, h - weight);
				
				// Draw the left straight pieces of a B.
				i_rect(v(leftX, 0), v(leftX, h));
				i_rect(v(leftX, v1.get(1)), v(curve_x, v1.get(1)));
				i_rect(v(leftX, v2.get(1)), v(curve_x, v2.get(1)));
				
				/*
				
				// Curve the sharp edges.
				v3 = v(0, - weight);
				i_curve(v(w/4, h/4 - weight), v(w/4, h/4 - weight - .001), v(0, -weight)); 
				// Curve the sharp edges.
				v3 = v2.add(left.mult(weight*3/2));
				i_curve(v(w/4, h*3/4 + weight), v(w/4, h*3/4 + weight + .001), v(0,h + weight)); 
				
				*/
				
				// Form the b's curves.
				
				v1 = v(curve_x, weight);
				v2 = v(curve_x, h - weight);
								
				v3 = v(curve_x, b_height);
				v4 = v(curve_x, b_height);
				
				i_curve(v1, v1.add(right.mult(.001)), v3, weight);
				
				i_curve(v2, v2.add(right.mult(.001)), v4, weight);
				
				i_rect(v(leftX, b_height), v(curve_x, b_height), weight);
				
				double old_height = h;
				
				room_height *= 1.4;
				h *= 1.4;
				
				// Form the tail curve.
				i_curve(v(leftX, old_height), v(leftX, old_height + .001), v(0, h));
				
				
				break;
				
			// Gamma. (Like an upside down Lambda.)
			case 'g':
			
				x_offset = w*.15;
				y_offset = h*.8;
			
				// The point of intersection in a lambda character.
				v1 = v(w/2 - x_offset, y_offset);
				v3 = v(w/2 + x_offset, y_offset);
				
				// The bottom loop point.
				v2 = v(w/2, h - weight);
				
				// The bottom loop left.
				i_curve(v2.sub(left.mult(.001)), v2.add(left.mult(.001)), v1.add(up.mult(.001)));
				
				// right.
				i_curve(v2.sub(right.mult(.001)), v2.add(right.mult(.001)), v3.add(up.mult(.001)));
				
				
				// The left leg
				i_curve(v3, v3.add(up.mult(.001)), v(0, 0));
				
				// The right leg
				i_curve(v1, v1.add(up.mult(.001)), v(w, 0));
				
				break;

			// DELTA
			case 'd':
				
				double radius = w*.4;
				// center.
				v1 = v(radius, h - radius);
				
				i_circle_outline(v(radius, h - radius), radius, weight*2);
				// The intersection of the circle and the curve.
				v2 = v1.add(v(1, -1).norm().mult(Math.sqrt(2)/2*(radius + weight*2)));
				i_curve(v2, v2.add(v(-1, -1).norm().mult(.0001)), v(w - weight, h/3));
				break;
				
			case 'e':
				
				w = scaleW(.6);
				
				double center_y = h*.56;
				
				// Center.
				v1 = v(w*.5, center_y);
				
				y_offset = h*.2;
				x_offset = w*.82;
				
				v2 = v(x_offset, center_y - y_offset);
				v3 = v(x_offset, center_y + y_offset);
				
				i_curve(v1, v1.add(left.mult(.001)), v2);
				i_curve(v1, v1.add(left.mult(.001)), v3);
				
				i_rect(v1, v1.add(right.mult(h/8)));
				break;
				
			case 't':
				
				// Code partially stolen from '8'.
				
				w = scaleW(.5);
				
				// Top curve.
				Vector center = v(w/2, h/4);
				radius = w/2 - weight;
				
				v1 = center.add(v_dir(0).mult(radius));// 180 + 45;
				v2 = center.add(v_dir(90) .mult(radius));
				v3 = center.add(v_dir(180).mult(radius));
				
				i_curve(v1, v2, v3);
				
				// Bottom curve.
				center = v(w/2, h*3/4);
				radius = w/2 - weight;
				
				v4 = center.add(v_dir(0).mult(radius));// 180 + 45;
				v5 = center.add(v_dir( -90) .mult(radius));
				v6 = center.add(v_dir(180).mult(radius));
				
				i_curve(v4, v5, v6);
				
				// Connect the two curves.
				i_rect(v1, v4);
				i_rect(v3, v6);
				
				
				i_curve(v(0, h/3), v(w/2, h*.5), v(w, h/3));
				
				break;
			
			default : throw new Error("Greek Character '" + val + "' is not yet supported");
			
		}
	}
	
}