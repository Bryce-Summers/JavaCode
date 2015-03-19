package Projects.Math.images.Main;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.functions.Interpolator;
import Projects.Math.Dormant;

import static BryceMath.Calculations.MathB.*;

public class ccHeart extends ColorCalculator
{

	public ccHeart(int x, int y)
	{
		super(x, y);
		
		antiAliasing = 4;

	}
	
	public ccHeart(Dimension tempDim)
	{
		super(tempDim);
		
		antiAliasing = 4;

	}

	@Override
	public Color getColor(double x, double y)
	{
		//room_width  = 7200;
		//room_height = 7200;
		//x+=4000;
		//y+=6000;
		//use these modifiers if the renderer messes up on some horizontal bands.
		
		
		x = x*800/room_width;
		y = y*800/room_height;
		
		y+=200;
		
		//if(x>room_width/2){x=room_width-x;};
		double dist = distance(800/2,800/2,x,y);
		double angle = lineAngle(800/2,800/2,x,800-y);
		angle = rad(angle);
		double coef = 800/6;//room_width/6;
		double c = coef*(2 - 2*sin(angle)+sin(angle)*sqrt(abs(cos(angle))) /(sin(angle) + 1.4) )-dist;
		
		if(c > 0)
		{
			// 5 is the neccessary scaling constant for the interior of hearts.
			return Color_hsv(0, 100, g(c / 6));
		}
		
		return Color_hsv(0,0,0,0);
	}
	
	// applies a transformation to the basic function x;
	private double g(double x)
	{
		if(x > 15)
		{
			return 100;
		}
		
		return 0;
	}
	
	Interpolator iterp = new Interpolator(Interpolator.TRIG);
	
	// Shading function.
	@Dormant
	private double f(double x)
	{
		double x_2 = x*x;
		return 3*x_2 - 2*x_2*x;
		
		//return iterp.eval(x);
		//return x;
	}

}
