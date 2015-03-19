package Game_Engine.GUI.Components.small;

import static BryceMath.Calculations.MathB.cos;
import static BryceMath.Calculations.MathB.rad;
import static BryceMath.Calculations.MathB.sin;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.text.TextManager;

/*
 * Floating message class.
 * 
 * Transcribed by Bryce Summers on 6 - 3 - 2013.
 * 
 * Purpose : This class provides floating messages that rapidly expire,
 * 			 like in Roller Coaster Tycoon.
 * 
 * Updated 3 - 9 - 2014 : Added width and height knowledge to allow this class to play nice with the optimized drawing functions.
 */


// FIXME : Clean up this outdated class.
// FIMXE : Perhaps incorporate this class with the gui_look_and_feel pipeline.


public class gui_float extends Obj
{
	String myMessage;
	int decay;// The time that this gui element will exist for.
	int angle = 0;
	int text_size;

	public gui_float(double x, double y, String message, int decayTime)
	{
		super(x,y);
		setDepth(-1);
		collidable = false;// Don't allow collisions with this object.
		this.decay = decayTime;
		this.myMessage = message;
		
		text_size = TextManager.TEXT_SIZE;
		
		int text_width = TextManager.getLen(message, false) + 12;
		int box_height = 60;
		setW(text_width);
		setH(box_height);
		setX(x - text_width/2);
		setY(y - box_height);
	}

	@Override
	public void update()
	{
		setX(getX() + 1);
		setY(getY() - 1);
		
		angle+=10;
				
		decay--;
		
		if(decay<=0)
		{
			die();
		}
		
	}

	@Override
	public void draw (ImageB i, AffineTransform affineTransform)
	{
		if (!isVisible()){return;}//don't draw if the draw variable is false.

		Graphics2D g = (Graphics2D) i.getGraphics();

		drawTextCenter(g, affineTransform, getX() + getW()/2 + 5*cos(rad(angle)), getY() + getH()/2 - 5*sin(rad(angle)), myMessage, text_size);
	}
	
	public void setTextSize(int size)
	{
		text_size = size;
	}
	
}
