package Game_Engine.GUI.Components.small;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

import util.Enums.Justification;
import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.text.TextManager;
import Game_Engine.GUI.Components.Interfaces.TextComponent;

/*
 * gui_label class.
 * 
 * Written by Bryce Summers.
 * 
 * Majorly updated on 6 - 3 - 2013.
 * 
 * Purpose : This class allows for the creation of look and feels that can display text.
 */

// FIXME : Implement multi line and Latex functionality.

public class gui_label extends gui_look_and_feel implements TextComponent
{

	// -- Local Variables.
	private String myMessage = "";// FIXME : Change to a List of Characters.
	private int text_size = TextManager.TEXT_SIZE;
	protected String default_text = "";
	
	private Justification justified = Justification.CENTER;
	
	private int textX = 0;
	
	private boolean text_and_sprite = false;
	
	public gui_label(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		iVars();
	}
	
	public gui_label(Rectangle r)
	{
		super(r);
		iVars();
	}
	
	public gui_label(int x, int y, BufferedImage image)
	{
		super(x, y, image.getWidth(), image.getHeight());
		setImage(image);
		setDrawBorders(false);
	}

	private void iVars()
	{
		setDrawBorders(false);
		myMessage = "";
		textX = (int)getX() + getW()/2;
	}

	@Override
	public void draw (ImageB i, AffineTransform AT)
	{
		if (!isVisible()){return;}//don't draw if the draw variable is false.

		// Draw the Look and feel components.
		super.draw(i, AT);
		
		// Get the BufferedImage's Graphics context.
		Graphics2D g = (Graphics2D) i.getGraphics();
		
		if(sprite == null || text_and_sprite)
		{
			drawMessage(g, AT);
		}
		
	}
	
	// FIXME : Out source this functionality to a text processing and displaying class.
	
	protected void drawMessage(Graphics2D g, AffineTransform AT)
	{
		double x = getX();
		double y = getY();
		
		if(myMessage.length() > 0)
		{
			if(justified == Justification.LEFT)
			{
				drawTextLeft(g, AT, x, y, myMessage, text_size);
				textX = (int)x;
			}
			else // Center. Right becomes Center.
			{
				textX = drawTextCenter(g, AT, x + getW()/2, y + getH()/2, myMessage, text_size);
			}
		}
		else
		{
			if(justified == Justification.LEFT)
			{
				drawTextLeft(g, AT, x, y, default_text, text_size);
			}
			else // Center. Right becomes Center.
			{
				drawTextCenter(g, AT, x + getW()/2, y + getH()/2, default_text, text_size);
			}
		}
	}
	
	// -- Data Access methods.
	
	// FIXME : Make these only redraw when mutations occur.
	public void setText(String in)
	{
		if(in == null)
		{
			throw new Error("Labels cannot support null strings");
		}
		
		if(myMessage.equals(in))
		{
			return;
		}
		
		myMessage = in;
		redraw();
	}
	
	public void setTextSize(int size)
	{
		if(text_size == size)
		{
			return;
		}
		
		text_size = size;
		redraw();
	}
	
	// Returns the size of the text.
	public int getTextSize()
	{
		return text_size;
	}
	
	public String getText()
	{
		return myMessage;
	}
	
	public void setJustification(Justification j)
	{
		if(justified.equals(j))// I hope this works for enumerated types.
		{
			return;
		}
				
		justified = j;
		
		redraw();
	}
	
	// returns true if left justified, false if center justified.
	public Justification getJustification()
	{
		return justified;
	}
	
	/**
	 * Fits this label's width to contain all of the text.
	 */
	public void fitToContents()
	{
		fitToContents(0);		
	}
	
	/** Returns the proper width of a box containing a string of the given size;
	 * 
	 * @param min the minimum width that this label must have.
	 */
	 
	public void fitToContents(int min)
	{
		int w_image = 0;
		if(sprite != null)
		{
			w_image = sprite.getWidth();
		}
		
		int w_new = TextManager.getLen(getText(), text_size, getTEX()) + TextManager.getSpacing(text_size)*8;
		
		// Ensure the minimum is met.
		w_new = Math.max(w_new, min);
		
		// Make sure the width is greater or equal to the height.
		int h = getH();
		
		if(w_new < h)
		{
			w_new = h;
		}		
		
		if(w_new > w_image)
		{
			setW(w_new);
		}
		else
		{
			setW(w_image);
		}
	}
	
	@Override
	public void serializeTo(PrintStream stream)
	{
		// Serial version 1, I might want to change the serialization for labels.
		stream.println(myMessage);
		
		// Note : labels with an image should have messages that can tell a deserializer how to reconstruct the image.
	}
	
	@Override
	// The serialization identifier.
	public String getSerialName()
	{
		return "gui_label";
	}
	
	// Returns the x leftmost x coordinate that this label is drawing at.
	protected int getTextX()
	{
		return textX;
	}

	@Override
	public boolean isActive()
	{
		return mouseInRegion;
	}
	
	// Allows the user to tell the label to draw both the image and the text.
	public void set_text_and_image(boolean flag)
	{
		text_and_sprite = flag;
	}

}
