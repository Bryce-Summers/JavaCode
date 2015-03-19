package Game_Engine.Engine.text;

import java.awt.Color;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.DoubleMath.Vector;


/* Latin alphabet and miscellaneous keyboard symbols.
 * 
 * Elegant Vector Alphabet. This is the second alphabet that I have written.
 * 
 * Written by Bryce Summers on 6 - 12 - 2013.
 * 
 * Updated 1 - 19 - 2014 : Improved some documentation for the weight.
 */

/*
 * Note the user can specify the height, and this class will produce characters of appropriate width.
 */

// NOTE : .75 height is the height of lowercase letters.

// Letters such as W and M take up the entire square, shorter letters such as 'a', l, 1, and others take up less width.

public class ccAlphabet extends Image_vectorGeometry
{

	// Private data.
	
	// The val controls which character gets rendered.
	final char val;
	
	// The weight controls how thick each character is.
	// the radius of the lines that make up each character.
	final double weight;
	
	public static Color color = null;
	
	public ccAlphabet(char c, int h)
	{
		// Start the image off square.
		super(h, h, false);// Need to manually call i_geom();
		
		if(color != null)
		{
			set_color(color);
		}
		
		val = c;		
		weight = 1.0*h / 20;
		i_geoms();
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
			case 'A':
		
				Vector v1, v2, v3, v4, v5, v6;
				
				w = scaleW(.8);
				
				// The top line.
				i_line(v(weight, h), v(w/2, weight), v(w - weight, h));
				
				// The cross line.
				i_rect(v(w*.2, h*.7), v(w*.8, h*.7));
		
				break;
				
			case 'a':

				w = scaleW(.65);
								
				double h2 = h*.65 - weight;
				
				v6 = v(w - weight, h2);
				i_rect(v(w - weight, h), v6);
				
				// the top of the bottom left curve.
				v1 = v((h - h2 + weight)/2, h2);
				
				i_rect(v(w - weight, h2), v1);
				
				v2 = v(v1.getX(), h - weight);
				
				i_curve(v1, v1.add(v(-.0001, 0)), v2);
				
				i_curve(v2.sub(v(.0001, 0)), v2.add(v(.00001, 0)), v(w - weight, h*.75) );
				
				
				i_curve(v6, v6.add(v(0, -.00001)), v(weight, h *.35 + weight));
				
				break;
				
				/*

				w *= .75;
				room_width *= .75;
				
				// The middle of the first circle.
				v1 = v(w/4, h*3/8);
				i_circle(v1, weight*2);
				
				v2 =	v(w*3/4, h*3/8);
				
				// The top of the character.
				i_weighted_curve(v1, up.mult(.001), v2, weight*2, weight);
				
				// The bottom of the right spine.
				v3 = v(v2.get(0), h*6/8);
				
				// The right spine.
				i_inclined_rect(v2, v3, weight, weight*3/2);
				
				// The end of the right serif.
				v4 = v(w + weight, v3.get(1));
				
				i_weighted_curve(v3, down.mult(.001), v4, weight*3/2, weight/2);
				
				// The point at the end of the bottom left buldge.
				v5 = v(w*3/16, h*3/4);
				
				i_weighted_curve(v5, up.mult(.001), v3, weight, weight/2);
				
				v6 = v(w/2, h - weight*1.13);
				
				i_weighted_curve(v5.add(up.mult(.01)), down.mult(.001), v6, weight, weight/2);
				
				i_curve(v6, v6.add(v_dir(30).mult(.001)), v3, weight/2);
				
				break;
				
				//*/
				
			case 'B':
				
				w = scaleW(.75);
								
				v1 = v(0, weight);
				v2 = v(0, h - weight);
				
				double b_height = h*.44;
				double curve_x = w - (h - b_height)/2 - weight/2;
				
				double leftX = weight;
				
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
				
				break;
				
			case 'b':
				
				w = scaleW(.73);
				
				// A temporary hack. 7 - 23 - 2013.
				//room_width = (int) (room_width*.86);
				
				i_rect(v(weight, 0), v(weight, h));
				
				v1 = v(weight, h/2);
				
				i_curve(v1, v1.add(v_dir(67.5).mult(.001)), v(weight, h*.75) );
				
				break;
				
			// Backwards 'b'.
			case 'd':
				
				w = scaleW(.639);
				
				i_rect(v(w - weight - 1, 0), v(w - weight - 1, h));
				
				v1 = v(w - weight/2, h*.52833);
				
				i_curve(v1, v1.add(v_dir(180 - 65).mult(.001)), v(w - weight/2, h*.77833) );
				
				break;
				
			case 'D':
				
				// Left spine.
				i_rect(v(weight/2, 0), v(weight/2, h));
				
				i_rect(v(0, weight), v(w/4, weight));
				
				i_rect(v(0, h - weight), v(w/4, h - weight));
				
				i_curve(v(w/4, weight), v(w - weight - w/4, h/2), v(w/4, h - weight));
				
				room_width -= w/4;
				
				break;
				
			case 'q':

				w = scaleW(.639);

				
				i_rect(v(w - weight - 1, h*.25), v(w - weight - 1, h*1.5));
				
				v1 = v(w - weight/2, h*.52833);
				
				i_curve(v1, v1.add(v_dir(180 - 65).mult(.001)), v(w - weight/2, h*.77833) );
				
				room_height *= 1.5;
				
				break;
				
			case 'c':

				w = scaleW(.75);
	
				
			case 'C':
				
			
				// Top curve.
				Vector center = v(w/2, h - w/2);
				double radius = w/2 - weight;
				
				v1 = center.add(v_dir(30).mult(radius));
				v2 = center.add(v_dir(180) .mult(radius));
				v3 = center.add(v_dir(-30).mult(radius));
				
				i_curve(v1, v2, v3);
				
				break;				
			
			case 'G':
				
				v1 = v(w/2, h - weight/2);
				
				center = v(w/2, h/2);
				
				// The big top curve.
				i_curve(center.add(v_dir(45).mult(w/2 - weight)), v(weight, h/2), v1);
				
				i_curve(v1, v1.add(v_dir(0).mult(.001)), v(w - weight, h*7/8));
				
				// The right straight pieces.
				v2 = v(w - weight, h/2);
				i_round_rect(v(w - weight, h*7/8), v2);
				
				i_rect(v(w/2, h/2), v2);

				break;
				
			case 'g':
				
				w = scaleW(.5);
				
				v1 = v(w/2, h/4 + w/2);
				i_circle_outline(v1, w/2, weight*2);
				
				i_rect(v(w/2, h/4 + weight), v(w, h/4 + weight));
				
				v2 = v(w/2, h + w/4);
				i_circle_outline(v2, w/2, weight*2);

				// Compute the two coordinates for the connection piece.
				v3 = v1.add(v_dir(230).mult(w/2 - weight));
				v4 = v2.add(v_dir(130).mult(w/2 - weight));
				
				i_curve(v3, v3.add(v_dir(225).mult(.001)), v4);
				
				room_height *= 1.4;
				h *= 1.4;
				
				break;
			case 'o':

				w = scaleW(.75);
				
			case 'O':
				
				i_circle_outline(v(w/2, h - w/2), w/2, weight*2);
				break;
			
			case 'Q':
				i_circle_outline(v(w/2, h - w/2), w/2, weight*2);
				i_rect(v(w/2, h/2), v(w, h));
				break;
				
			case 'F':
				
				w = scaleW(.5);
								
				i_line(v(weight, h), v(weight, weight), v(w, weight));
				
				i_rect(v(0, h/2), v(w, h/2));
				
				break;
				
			case 'f':
				
				room_width = (int)(weight*7 + 1);
				w = room_width;
				
				v1 = v(w/2, w/2);
				i_rect(v(w/2, h), v1, weight);
				
				i_rect(v(0, h/4 + weight*2), v(w, h/4 + weight*2), weight);
				
				i_curve(v1, v1.add(up.mult(.001)), v1.add(v(w/2 + weight, 0)), weight);
				
				break;
				
			case 'L':

				w = scaleW(.5);
				
				i_line(v(weight, weight*2), v(weight, h - weight), v(w - weight, h - weight));
				break;
			
			case 'l':
				room_width = (int)(weight*2 + 1);
				w = room_width + 1;
				
				// Fill the entire region.
				i_rect(v(weight, 0), v(weight, h));
				break;
				
				// Like 'l', except shorter.
			case 'I':

				room_width = (int)(weight*1.5 + 1);
				w = room_width + 1;
				
				// Fill almost the entire region.
				i_round_rect(v(weight, weight*2), v(weight, h));
				break;
				
			case 'i':
				room_width = (int)(weight*2 + 1);
				w = weight*2 + 1;
				
				i_circle(v(w/2, weight*3), weight);
				
				// Fill almost the entire region.
				i_round_rect(v(w/2, h/4 + weight), v(w/2, h), weight);
				break;
				
			case '!':

				room_width = (int)(weight*4 + 1);
				w = room_width;
				
				i_circle(v(w/2, h - weight*2), weight*2);
				
				// Fill almost the entire region.
				i_round_rect(v(w/2, h - weight*6), v(w/2, 0), weight);
				break;
				
			case 'j':
				
				room_width = (int)(weight*4 + 1);
				w = room_width;
				
				i_circle(v(w*3/4, weight*2), weight);
				
				// Fill almost the entire region.
				v1 = v(w*3/4, h);
				i_round_rect(v(w*3/4, weight*6), v1, weight);
				
				
				// Like 'p'
				room_height *= 1.25;
				h *= 1.25;
				
				i_curve(v1, v1.add(down.mult(.001)), v(weight, h - weight), weight);
				
				break;
				
			case 'J':
				room_width = (int)(weight*5 + 1);
				w = room_width;
				
				v1 = v(w - weight, h - 5*weight);
				
				i_rect(v(w - weight, weight*2), v1);
				
				i_curve(v1, v1.add(down.mult(.001)), v(0, h - weight));
				
				break;
				
			case 'U':
				v1 = v(weight, h/2);
				v2 = v(w - weight, h/2);
				
				i_rect(v(weight, 0), v1);
				i_rect(v(w - weight, 0), v2);
				
				i_curve(v1, v(w/2, h - weight), v2);
				break;
				
			case 'u':
				
				w = scaleW(.75);
				
				v1 = v(weight, h - w/2);
				v2 = v(w - weight, h - w/2);
				
				i_rect(v(weight, h*.25), v1);
				i_rect(v(w - weight, h*.25), v(w - weight, h));
				
				i_curve(v1, v(w/2, h - weight), v2);
				
				break;
			
			case 'z':

				w = scaleW(.75);
				
			case 'Z':
				
				w = scaleW(.75);
				
				i_line(v(0, h - w*4/3 + weight), v(w - weight, h - w*4/3 + weight), v(weight, h - weight), v(w, h - weight));
				break;
				
			case 's':
				
				w = scaleW(.75);
								
				w = scaleW(.6);
				
								
				double angle = 90.01;
				radius = h*.275*.75*1.029 - weight;
				
				// The amount of curve on the top and bottom.
				double angle2 = 0;
				
				// The upper left curve.
				Vector center1 = v(radius + weight, h/4 + radius + weight);
				i_curve(center1, radius, angle - angle2, angle + 1, angle + 180);
				
				// The lower right curve.
				Vector center2 = v(w - radius - weight, h - radius - weight);
				i_curve(center2, radius, angle, angle - 1, angle - 180 - angle2);
				
				v1 = center1.add(v_dir(angle + 180).mult(radius));
				v2 = center2.add(v_dir(angle).mult(radius));
				
				// The connection between the curves.
				i_rect(v1, v2);
				
				// Connect the center section to the sides of the image.
				v1 = center1.add(v_dir(angle - angle2).mult(radius));
				v2 = center2.add(v_dir(angle - angle2 + 180).mult(radius));				
				
				v3 = v1.add(v_dir(angle - angle2 - 90).mult(w*.75));
				v4 = v2.add(v_dir(angle + 90 - angle2).mult(w*.75));	
				
				i_rect(v1, v3);
				i_rect(v2, v4);
				
				break;

								
			case 'S':
				
				w = scaleW(.6);
				
				angle = 90.01;
				radius = h*.275 - weight;
				
				// The amount of curve on the top and bottom.
				angle2 = 0;
				
				// The upper left curve.
				center1 = v(radius + weight, radius + weight);
				i_curve(center1, radius, angle - angle2, angle + 1, angle + 180);
				
				// The lower right curve.
				center2 = v(w - radius - weight, h - radius - weight);
				i_curve(center2, radius, angle, angle - 1, angle - 180 - angle2);
				
				v1 = center1.add(v_dir(angle + 180).mult(radius));
				v2 = center2.add(v_dir(angle).mult(radius));
				
				// The connection between the curves.
				i_rect(v1, v2);
				
				// Connect the center section to the sides of the image.
				v1 = center1.add(v_dir(angle - angle2).mult(radius));
				v2 = center2.add(v_dir(angle - angle2 + 180).mult(radius));				
				
				v3 = v1.add(v_dir(angle - angle2 - 90).mult(w*.75));
				v4 = v2.add(v_dir(angle + 90 - angle2).mult(w*.75));	
				
				i_rect(v1, v3);
				i_rect(v2, v4);
				
				break;
				

				/*
				angle = 90;
				radius = h*.2125 - weight;
				
				// The amount of curve on the top and bottom.
				angle2 = 0;
				
				// The upper left curve.
				center1 = v(radius + weight, radius + weight + h/4);
				i_curve(center1, radius, angle - angle2, angle + 1, angle + 180);
				
				// The lower right curve.
				center2 = v(w - radius - weight, h - radius - weight);
				i_curve(center2, radius, angle, angle - 1, angle - 180 - angle2);
				
				v1 = center1.add(v_dir(angle + 180).mult(radius));
				v2 = center2.add(v_dir(angle).mult(radius));
				
				// The connection between the curves.
				i_rect(v1, v2);
				
				// Connect the center section to the sides of the image.
				v1 = center1.add(v_dir(angle).mult(radius));
				v2 = center2.add(v_dir(angle + 180).mult(radius));				
				
				v3 = v1.add(v_dir(angle - angle2 - 90).mult(w*.75));
				v4 = v2.add(v_dir(angle + 90 - angle2).mult(w*.75));	
				
				i_rect(v1, v3);
				i_rect(v2, v4);
				
				break;
			*/
			case 'T':
				
				w = scaleW(.75);
								
				i_rect(v(0, weight), v(w, weight));
				i_rect(v(w/2, weight), v(w/2, h));
				break;
			
			case 't':
				
				w = scaleW(.5);
				
				v1 = v(w/2 - weight, h - w/2 - weight);
				
				i_rect(v(w/2 - weight, 0), v1);
				
				// The curve.
				v2 = v(w - weight, h - weight);
				i_curve(v1, v1.add(down.mult(.0001)), v2);
				
				i_rect(v2, v(w, v2.get(1)));
				
				// The cross piece.
				i_rect(v(0, h*5/16), v(w, h*5/16), weight);
				
				break;

				
			case 'w': 
				w = scaleW(.75);
			case 'W':

				i_line(v(weight, h - w), v(w/4, h - weight), v(w/2, h - w + weight), v(w * 3/4, h - weight), v(w - weight, h - w));
				break;
				
			case 'M':

				i_line(v(weight, h), v(weight, weight), v(w/2, h - weight), v(w - weight, weight), v(w - weight, h));
				break;
				
			case 'm':
				
				double x1 = weight;
				double x2 = w/2;
				double x3 = w - weight;

				// The left spine.
				i_rect(v(x1, h/4), v(x1, h));
				
				// The middle spine.
				i_rect(v(x2, h/2), v(x2, h));

				// The end spine.
				i_rect(v(x3, h/2), v(x3, h));

				double y = h/2;
				v1 = v(x1, y);
				v2 = v(x2, y);
				v3 = v(x3, y);
				
				Vector upSmall = up.mult(.001);
				
				// The first curve.
				i_curve(v1, v1.add(upSmall), v2);
				
				// The second curve.
				i_curve(v2, v2.add(upSmall), v3);
				
				break;
				
			case 'H':
				
				w = scaleW(.75);
				
				// The left side.
				i_rect(v(weight, 0), v(weight, h));
				
				// The right side.
				i_rect(v(w - weight, 0), v(w - weight, h));
				
				// The middle rod.
				i_rect(v(0, h/2), v(w, h/2));
				break;
				
			case 'h':

				w = scaleW(.75);
								
				// The left side.
				i_rect(v(weight, 0), v(weight, h));
				
				// The right straight part.
				v1 = v(w - weight, h/4 + w/2 + weight/2);
				i_rect(v1, v(w - weight, h), weight);
				
				i_curve(v1, v1.add(up.mult(.001)), v(weight/2, v1.get(1)) );
				
				break;
				
			case 'Y':
				
				w = scaleW(.75);
								
				center = v(w / 2, h * .6);
				
				i_line(v(weight, 0), center, v(w - weight, 0));
				
				i_rect(center, v(center.get(0), h));
				
				break;
				
			case 'y':
				
				w = scaleW(.75);
								
				center = v(w / 2, h);
				
				i_line(v(weight, h/4), center, v(w - weight, h/4));
				
				i_round_rect(center, v(w/4, h*1.5));
				
				room_height *= 1.5;
				
				break;
				
			case 'N':
				
				w = scaleW(.75);
				
				i_line(v(weight, h), v(weight, weight), v(w - weight, h - weight), v(w - weight, 0));
				break;
				
			case 'n':
				
				w = scaleW(.75);

				// The left spine.
				i_rect(v(weight, h/4), v(weight, h));

				double n_height = h*.65;
				
				// The right leg.
				i_rect(v(w - weight, n_height), v(w - weight, h));
				
				// The curve.
				i_curve(v(weight, n_height), v(weight, n_height - .0001), v(w - weight, n_height));
				
				break;
				
			case 'V':
				
				w = scaleW(.75);
				
				i_line(v(weight, weight), v(w/2, h - weight), v(w - weight, weight));
				
				break;
				
			case 'v':
				
				w = scaleW(.75);
				
				i_line(v(weight, h/4), v(w/2, h - weight), v(w - weight, h/4));
				
				break;
				
			case 'E':

				w = scaleW(.5);
				
				i_line(v(w - weight, weight), v(weight, weight), v(weight, h - weight), v(w - weight, h - weight));
				
				i_round_rect(v(weight, h/2), v(w - weight, h/2));
				
				break;
				
			case 'e':
				w = scaleW(.65);
				
				
				v1 = v(weight, h - w/2 - w*.1);
				v2 = v(w - weight, h - w/2 - w*.1);
				
				// The top bow.
				i_round_rect(v1, v2, weight*.75);
				i_curve(v1, v(w/2, h - w + weight - w*.1), v2);
				
				// The bottom curve.
				v2 = v(w/2, h - weight);
				i_curve(v1, v1.add(down.mult(.001)), v2.add(right.mult(1)));
				
				//i_rect(v2, v2.add(right.mult(w/4)));
				
				i_curve(v2.add(left.mult(.001)), v2.add(right.mult(.001)), v(w - weight*.6, h - w*.25));
				
				break;
				
			case 'r':
				
				w = scaleW(.5);
								
				i_rect(v(weight, h), v(weight, h*.25));
				
				v1 = v(weight, h*.75);
				
				i_curve(v1, v1.add(up.mult(.001)), v(w, v1.get(1) - (w - weight)) ) ;
				
				break;
		
			case 'x':
				
				w = scaleW(.75);
				
			case 'X':
				
				double temp = weight/Math.sqrt(2);
				
				i_rect(v(temp, h - w + temp), 		v(w - temp, h - temp));
				i_rect(v(temp, h - temp), 	v(w - temp, h - w + temp));
				break;
				
			case 'p':
				
				
				w = scaleW(.75);
								
				room_height *= 1.5;
				h *= 1.5;
				
				i_rect(v(weight, h/6), v(weight, h*.9));
				
				v1 = v(weight, h*.3);
				
				i_curve(v1, v1.add(v_dir(64).mult(.001)),  v(weight, h*.5));
				
				break;
				
			case 'P':
				
				w = scaleW(.75);
								
				v1 = v(weight, 0);
				
				i_rect(v(weight, h), v1);
				
				double w2 = w*.564;
				
				v1 = v(w2, weight);
				i_rect(v(weight, weight), v1);

				h2 = h *.6;
			 
				v2 = v(w2, h2);
				i_rect(v(weight, h2), v2);
				
				i_curve(v1, v1.add(right.mult(.001)), v2);
				
				break;
				
			case 'R':
								
				w = scaleW(.5);
				
				i_rect(v(weight, 0), v(weight, h));
				
				v1 = v(w/2, weight);
				i_rect(v(0, weight), v1);
				
				v2 = v(0, h/2 - weight);
				v3 = v(w/2, h/2 - weight);
				
				i_rect(v2, v3);
				
				i_curve(v1, v1.add(right.mult(.001)), v3);
				
				
				// The bottom leg.
				i_rect(v(w/2, h/2 - weight), v(w - weight/Math.sqrt(2), h + weight/Math.sqrt(2)));
				
				break;
				
			case 'K':
				
				w = scaleW(.5);
								
				i_rect(v(weight, 0), v(weight, h));
				
				i_line(v(w - weight, 0), v(weight*3, h/2), v(w - weight, h) );
				
				break;
				
			case 'k':
				
				w = scaleW(.5);
				
				i_rect(v(weight, 0), v(weight, h));
				
				i_line(v(w - weight, h/4), v(weight*3, h*5/8), v(w - weight, h) );
				
				break;
				
			case '1':

				w = scaleW(.5);
								 
				i_line(v(weight, h/4), v(w/2, weight), v(w/2, h) );
				
				i_rect(v(0, h - weight), v(w, h - weight));
				
				break;
				
			case '2':
				
				w = scaleW(.5);
								
				center = v(w/2, h/4);
				radius = w/2 - weight;
				
				v1 = center.add(v_dir(155).mult(radius));
				v2 = center.add(v_dir(90) .mult(radius));
				v3 = center.add(v_dir(-34).mult(radius));
				
				i_curve(v1, v2, v3);
				
				i_line(v3, v(0, h - weight), v(w, h - weight));
				
				break;
				
			case '3':
				
				w = scaleW(.5);

				// Top curve.
				center = v(w/2 - weight/2, h/4 + weight/2);
				radius = w/2 - weight/2;
				
				v1 = center.add(v_dir(155).mult(radius));
				v2 = center.add(v_dir(90) .mult(radius));
				v3 = center.add(v_dir(-90).mult(radius));
				
				i_curve(v1, v2, v3);
				
				// Bottom curve.
				
				center = v(w/2 - weight/2, h * .75 - weight/2);
				radius = w/2 - weight/2;
				
				v1 = center.add(v_dir(-155).mult(radius));
				v2 = center.add(v_dir(-90) .mult(radius));
				v3 = center.add(v_dir( 90).mult(radius));
				
				i_curve(v1, v2, v3);
				
				// Extend the center rod a bit.
				i_rect(v3, v(w/4, h/2));
				
				break;
				
			case '4':
				
				w = scaleW(.75);
				
				i_line(weight, v(w, h * 2/3), v(weight, h*2/3), v(w*2/3, weight), v(w*2/3, h));
				
				break;
				
			case '5':
				
				w = scaleW(.6);
				
				y = h - w/2 - weight/2;
				
				// Bottom curve.
				center = v(w/2 - weight/2, y);
				radius = w/2 - weight/2;
				
				v1 = center.add(v_dir(90).mult(radius));
				v2 = center.add(v_dir(0) .mult(radius));
				v3 = center.add(v_dir(-145).mult(radius));

				// The top lines.
				i_line(weight, v(w * .8, weight), v(weight, weight), v(weight, v1.get(1)), v(w/2, v1.get(1)));
				
				i_curve(v1, v2, v3);
				break;
				
			case '6':
				
				w = scaleW(.6);
				
				i_circle_outline(v(w/2, h - w/2), w/2, weight*2);
				
				v1 = v(weight,h - w/2);
				v2 = v(w * .25, h*.2);
				
				i_curve(v1, v1.add(up.mult(.001)), v2);
				
				i_curve(v2, v2.add(v_dir(67.40).mult(.0001)), v(w - weight, h/8));
				
				break;
				
			case '7':
				
				w = scaleW(.5);
				
				i_line(weight, v(0, weight), v(w - weight, weight), v(weight, h));
				
				break;
				
			case '8':
				
				w = scaleW(.5);
				
				// Top curve.
				center = v(w/2, h/4);
				radius = w/2 - weight;
				
				angle = 37;
				
				v1 = center.add(v_dir(-90 - angle).mult(radius));// 180 + 45;
				v2 = center.add(v_dir(90) .mult(radius));
				v3 = center.add(v_dir(-90 + angle).mult(radius));
				
				i_curve(v1, v2, v3);
				
				// Bottom curve.
				center = v(w/2, h*3/4);
				radius = w/2 - weight;
				
				v4 = center.add(v_dir(90 + angle).mult(radius));// 180 + 45;
				v5 = center.add(v_dir(-90) .mult(radius));
				v6 = center.add(v_dir(90 - angle).mult(radius));
				
				i_curve(v4, v5, v6);
				
				// Connect the two curves.
				i_rect(v1, v6);
				i_rect(v3, v4);
				
				break;
				
			// And upside down 6.
				
			case '9':
				
				w = scaleW(.6);
				
				i_circle_outline(v(w/2, w/2), w/2, weight*2);
				
				v1 = v(w - weight, w/2);
				v2 = v(w/4, h + weight);
				
				i_curve(v1, v1.add(down.mult(.001)), v2);
				
				//i_curve(v2, v2.add(v_dir(180 + 67.40).mult(.0001)), v(weight, h - h/8));
				
				break;
				
			case '0':
				
				// Code partially stolen from '8'.
				
				w = scaleW(.5);
				
				// Top curve.
				center = v(w/2, h/4);
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
				
				break;
				
				
				
			case '=':
				
				w = scaleW(.75);
				
				double displacement = 2.5;
				
				i_rect(v(0, h/2 - weight*displacement), v(w, h/2 - weight*displacement) );
				i_rect(v(0, h/2 + weight*displacement), v(w, h/2 + weight*displacement) );
				
				break;

			case '+':

				w = scaleW(.75);
				
				i_rect(v(w/2, h/2 - w/2), v(w/2,   h/2 + w/2));
				i_rect(v(0, h/2), v(w, h/2));
				break;
				
			case '-':

				w = scaleW(.75);
				
				i_rect(v(0, h/2), v(w, h/2));
				break;
				
			case '_':

				w = scaleW(.75);
				
				i_rect(v(0, h - weight), v(w, h - weight));
				break;
				
			case '/':
				
				w = scaleW(.5);
				
				i_rect(v(w - weight, 0), v(weight, h - weight));
				break;
				
			case '\\':
		
				w = scaleW(.5);
				
				i_rect(v(0, 0), v(w - weight, h - weight));
				break;
				
			case '|':
				room_width = (int) (weight*2 + 1);
				w = room_width;
				
				room_height *= 1.25;
				h *= 1.25;
				
				i_rect(v(w/2, 0), v(w/2, h));
				
				break;
				
			case '>':
				
				w = scaleW(.5);
				
				i_line(v(0, h/4), v(w - weight, h/2), v(0, h * .75));
				break;
				
			case '<':
				
				w = scaleW(.5);
				
				i_line(v(w, h/4), v(weight, h/2), v(w, h * .75));
				break;

			case ':':
				room_width = (int)(weight*3 + 1);
				w = room_width;
				
				i_circle(v(w/2, h/4), weight*2);
				
			case '.':
				room_width = (int)(weight*5 + 1);
				w = room_width;
				
				i_circle(v(w/2, h - w/2), weight*2);
				break;

			// Render an empty image of the correct weight.
			case ' ':
				room_width = (int)(weight*4 + 1);
				w = room_width;
				break;
				
			case ';':

				room_width = (int)(weight*6 + 1);
				w = room_width;
				
				i_circle(v(w/2, h/4), weight*2);
				
			case ',':
				room_width = (int)(weight*6 + 1);
				w = room_width;
				
				room_height += weight*3;
				h = room_height;
				
				i_curve(v(0, h - weight*2), v(w - weight, h - weight*4), v(0, h - weight*8), weight);
				break;			
			
			case '\'':
				room_width = (int)(weight*2 + 1);
				w = room_width;
				
				i_rect(v(w/2, 0), v(w/2, h/4));
				i_triangle(v(0, h/4), v(w, h/4), v(w/2, h/4 + weight));
				
				break;
				
			case '"':
				room_width = (int)(weight*2 + 1);
				w = room_width;
				
				i_rect(v(w/2, 0), v(w/2, h/4));
				i_triangle(v(0, h/4), v(w, h/4), v(w/2, h/4 + weight));
				
				room_width = (int)(weight*6 + 1);
				w = room_width;
				
				i_rect(v(w*5/6, 0), v(w*5/6, h/4));
				i_triangle(v(w*4/6, h/4), v(w*6/6, h/4), v(w*5/6, h/4 + weight));
				break;
				
			case '?':
				w = scaleW(.5);
				
				i_circle(v(w/2, h - weight*1.5), weight*1.5);
				
				i_rect(v(w/2, h - weight*4), v(w/2, h/2));
				
				center = v(w/2, h/4);
				
				v1 = center.add(v_dir(270).mult(w/2 - weight));
				v2 = center.add(v_dir(0)  .mult(w/2 - weight));
				v3 = center.add(v_dir(145).mult(w/2 - weight));
				
				i_curve(v1, v2, v3);
				
				i_circle(v1, weight);
				
				break;
				
			case '[':
				w = scaleW(.25);
				
				room_height*= 1.25;
				h *= 1.25;
				
				i_line(v(w, weight), v(weight, weight), v(weight, h - weight), v(w, h - weight));
				break;
				
			case ']':
				w = scaleW(.25);

				room_height*= 1.25;
				h *= 1.25;
				
				i_line(v(0, weight), v(w - weight, weight), v(w - weight, h - weight), v(0, h - weight));
				break;
				
			case '{':
				w = scaleW(.5);

				room_height*= 1.25;
				h *= 1.25;
				
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
				w = scaleW(.5);

				room_height*= 1.25;
				h *= 1.25;
				
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
				
			case '(':

				w = scaleW(.25);
				
				room_height *= 1.25;
				h *= 1.25;
				
				i_curve(v(w, 0), v(weight, h/2), v(w, h));
				
				break;
				
			case ')':

				w = scaleW(.25);
				
				room_height *= 1.25;
				h *= 1.25;
				
				i_curve(v(0, 0), v(w - weight, h/2), v(0, h));
				
				break;
				
			case '*':
				
				w = scaleW(.5);
				room_height = room_width;
				
				w = room_width;
				h = room_height;
				
				i_rect(v(weight, h/8), v(w - weight, h*7/8));
				i_rect(v(w - weight, h/8), v(0, h*7/8));
				i_rect(v(w/2, 0), v(w/2, h));
				break;
				
			case '^':
				
				w = scaleW(.5);
				room_width = (int)w + 1;

				
				i_rect(v(weight, h/2), v(w/2, weight));
				i_rect(v(w - weight, h/2), v(w/2, weight));
				break;
				
			case '~':
				
				w = scaleW(.5);
				room_width = (int)w + 1;

				v1 = v(0,   h*.4);
				v2 = v(w/2, h*.5);
				v3 = v(w,   h*.6);
				
				i_curve(v1, v1.add(v(1,  2).norm().div(10000)), v2);
				i_curve(v2, v2.add(v(1, -2).norm().div(10000)), v3);
				
				break;
				
			case '#':
				
				w = scaleW(.5);
				room_width = (int)w + 1;

				// Offset.
				v1 = v(-w/8, h);
				
				v2 = v(w/3, 0);
				v3 = v(2*w/3, 0);
				
				i_line(v2, v2.add(v1));
				i_line(v3, v3.add(v1));
				
				i_line(v(0, h/3), v(w, h/3));
				i_line(v(0, 2*h/3), v(w, 2*h/3));
				
				break;
				
			case '`':
				
				room_width = (int)(weight*4 + 1);
				w = room_width;
				
				i_line(v(0, 0), v(weight*4, weight*4));
				
				break;
			
			case '\n': throw new Error("Bryce has evidently not yet implemented line breaks.");
			
			default : throw new Error("Character '" + val + "' is not supported");
			
		}
	}
	
	// -- Helper methods.
	public void i_line(Vector ... input)
	{
		i_line(weight, input);
	}
	
	public void i_rect(Vector input1, Vector input2)
	{
		i_rect(input1, input2, weight);
	}

	public void i_round_rect(Vector input1, Vector input2)
	{
		i_round_rect(input1, input2, weight);
	}
	
	public void i_circle(Vector center)
	{
		i_circle(center, weight);
	}
	
	public void i_curve(Vector v1, Vector v2, Vector v3)
	{
		i_curve(v1, v2, v3, weight);
	}
	
	// Transforms a curve of weight 2 at location 1 into a curve of length 1 at location 3.
	
	public void i_weighted_curve(Vector v1, Vector dir, Vector v3, double weight1, double weight2)
	{
		
		// Compute the 2d inverse perpendicular slope.
		Vector perp = dir.perp2d().norm().mult(weight1 - weight2);

		dir = dir.norm().mult(.0001);
		
		Vector v2A = v1.sub(perp);
		Vector v2B = v1.add(perp);
		
		i_curve(v2A, v2A.add(dir), v3, weight2);
		i_curve(v2B, v2B.add(dir), v3, weight2);
		
		// Add an interior curve if two will not be enough.
		if(weight1 - weight2*2 > 2)
		{
			i_curve(v1, v1.add(dir), v3, weight2);			
		}
	}
	
	public void i_curve(Vector center, double radius, double angle1, double angle2, double angle3)
	{
		Vector v1 = center.add(v_dir(angle1).mult(radius));
		Vector v2 = center.add(v_dir(angle2).mult(radius));
		Vector v3 = center.add(v_dir(angle3).mult(radius));
		
		i_curve(v1, v2, v3, weight);
	}
	
	protected double scaleW(double per)
	{
		room_width = (int) (room_width * per) + 1;
		return room_width;
	}
	
}