package Game_Engine.GUI.Components.small;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import util.Enums.Justification;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Data_Structures.Structures.SpicketDropBucket.Bucket;
import Data_Structures.Structures.SpicketDropBucket.Drop;
import Data_Structures.Structures.SpicketDropBucket.Spigot;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.text.TextManager;

/*
 * The Graphic User INterface Paragraph Class.
 * 
 * Written on 3 - 1 - 2014 by Bryce Summers
 * 
 * This class allows for the display of multi line text.
 * 
 */

public class gui_paragraph extends gui_look_and_feel
{
	// Entire Message.
	String text = "";
	
	// Words in the message.
	List<Word> words = new List<Word>();
	
	// The text of sets of words.
	List<String> lines = new List<String>();

	// Currently, we only support uniform sized text.
	private int text_height = TextManager.TEXT_SIZE;
	
	Justification justified = Justification.LEFT;
	
	// -- Private Class.
	
	private class Word implements Drop
	{
		final int screen_width;
		final String text;
		
		private Word(String text)
		{
			this.text = text;
			screen_width = TextManager.getLen(text + " ", getTEX());
		}

		@Override
		public int getSize()
		{
			return screen_width;
		}
		
		public String getText()
		{
			return text;
		}
		
		public String toString()
		{
			return text;
		}
	}

	// -- Constructors.
	public gui_paragraph(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_paragraph(Rectangle r)
	{
		super(r);
	}

	// Allows this paragraph to be modified.
	public void setText(String s)
	{
		// Do nothing if the text has not changed.
		if(text.equals(s))
		{
			return;
		}
		
		text = s;
		
		String[] words_temp = text.split(" ");
		
		words.clear();
		
		// Add all of the words to the word list.
		for(String str : words_temp)
		{
			Word w = new Word(str);
			words.add(w);
		}
		
		resetLineBreaks();
	}
	
	/* Manages the location of line breaks within the paragraph.
	 * Should be called whenever the text region size changes,
	 * or whenever the text itself changes.
	 * 
	 * Uses my Bucket, Drop, Spigot Algorithm.
	 * 
	 * Converts the Words List into the Lines List.
	 */
	private void resetLineBreaks()
	{
		// 4 allows for the text to have a margin
		// the size of the border on each side.
		int w = getW() - 4*getBorderSize();
		
		Spigot s = new Spigot(w);
		s.populate(words.getIter());
		
		System.out.println(words);
		
		lines.clear();
		
		Iterator<Bucket> iter = s.poor_buckets_ordered();
		
		// Iterate through the buckets (lines).
		while(iter.hasNext())
		{
			// Word iterator for each line.
			Iterator<Drop> iter2 = iter.next().get_contents();
			
			// Used for building lines.
			StringBuilder line = new StringBuilder();
			
			// Add the first element.
			if(iter2.hasNext())
			{
				Word wd = (Word)iter2.next();
				line.append(wd.getText());
			}
			
			// Iterate through and add the remaining words.
			while(iter2.hasNext())
			{
				Word wd = (Word)iter2.next();
				line.append(' ');
				line.append(wd.getText());
			}
			
			// Put the computed lines in the line list.
			lines.add(line.toString());
		}
	}
	
	// An override function that allows this Object to talk to the drawing pipeline.
	@Override
	public void draw (ImageB i, AffineTransform AT)
	{
		super.draw(i, AT);
		
		// Get the BufferedImage's Graphics context.
		Graphics2D g = (Graphics2D) i.getGraphics();
		drawMessage(g, AT);
	}
	
	// Draws the lines of text to the screen with the desired justification.
	protected void drawMessage(Graphics2D g, AffineTransform AT)
	{
		double x = getX();
		double y = getY();
		
		int line_height = text_height*3/2;
		
		int line_num = 0;
		
		int border_offset = getBorderSize()*2;
		
		for(String s : lines)
		{
			if(justified == Justification.LEFT)
			{
				drawTextLeft(g, AT, x + border_offset, y + border_offset + line_height*(line_num + .5), s, text_height);
			}
			else // Center. Right becomes Center.
			{
				drawTextCenter(g, AT, x + getW()/2, y + border_offset + line_height*(line_num + .5), s, text_height);
			}
			
			line_num++;
		}
	}
	
	// Modifies the size of the Paragraph's text.
	public void setTextSize(int size)
	{
		if(true)
		{
			throw new Error("Not Correct Yet!");
		}

		// FIXME : I need to update the sizes of the words, the line spacing, the line breaks, and perhaps more.  
		
		if(text_height == size)
		{
			return;
		}

		text_height = size;
		redraw();
	}

}