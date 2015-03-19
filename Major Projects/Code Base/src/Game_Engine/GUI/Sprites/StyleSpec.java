package Game_Engine.GUI.Sprites;

import java.awt.Color;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceMath.Calculations.Colors;


/*
 * The StyleSpec class.
 * 
 * Written on a train to North Carolina by Bryce Summers on 3 - 9 - 2014.
 * 
 * This class should provide helpful functions for modifying the appearance of the Graphic User Interface.
 * 
 * 1. Defines a function that maps locations within the border to colors.
 * 2. Defines the distribution of colors within the GUI components.
 */

public class StyleSpec
{
	
	int global_hue = 180;//146;//60.
	
	// Current values for rendering button colors.
	public final static Color C_BUTTON_UP   = Colors.Color_hsv(146, 5, 99);
	public final static Color C_BUTTON_DOWN = Colors.Color_hsv(146, 0, 100);
	public static final Color C_BOX_SELECTED 		= Colors.Color_hsv(146, 0, 100);
	public static final Color C_BOX_NOT_SELECTED    = Colors.Color_hsv(146, 5, 100);
	
	// Scrollbar colors.
	public static final Color C_SCROLLBAR_BACKGROUND = Colors.Color_hsv(0,5, 80);
	public static final Color C_SCROLLBAR_BAR = Colors.Color_hsv(0,5, 99);
	public static final Color C_DRAGGED = Colors.Color_hsv(0,5, 97);;
	
	// Dynamically changed colors.
	public static Color c_out		= Color.red;
	public static Color c_middle_out= Color.black;
	public static Color c_middle_in = Color.black;
	public static Color c_inside	= Color.red;
	
	public StyleSpec()
	{

	}

	public static void setOuterColor(Color outside)
	{
		c_out    = outside;
	}

	// Sets all of the composite border colors.
	public static void defineBorderColors(Color outside, Color middle, Color inside)
	{
		c_out    = outside;
		c_middle_out = middle;
		c_middle_in = middle;
		c_inside = inside;
	}
	
	// Sets all of the composite border colors.
	public static void defineBorderColors(Color outside, Color middle_out, Color middle_in, Color inside)
	{
		c_out    = outside;
		c_middle_out = middle_out;
		c_middle_in  = middle_in;
		c_inside = inside;
	}

	/* Input : 0.0 - 1.0.
	 * Output, an interpolated color within the border
	 */
	public static Color getBorderColor(double d)
	{
		d = BryceMath.bound(d, 0, 1);
		
		if(d < .5)
		{
			return Colors.weightedAverageColor(c_out, c_middle_out, d*2);
		}
		
		return Colors.weightedAverageColor(c_middle_in, c_inside, (d - .5)*2);		
		
	}
}
