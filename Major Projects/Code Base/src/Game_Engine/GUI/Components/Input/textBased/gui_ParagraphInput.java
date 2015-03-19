package Game_Engine.GUI.Components.Input.textBased;

/* A fractured partial class that has yet to be fully implemented.
 * 
 */

// FIXME : This class is not complete.
// Think about using the spigot, drop, bucket data structure.


import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.StringParser;
import util.Enums.Justification;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;

public class gui_ParagraphInput extends gui_StringInput
{
	
	// -- Private Data.
	List<Character> message;

	public gui_ParagraphInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_ParagraphInput(Rectangle rect)
	{
		super(rect);
	}
	
	// -- We need to override the appropriate message 
	//    handling code in the gui_label class.
	@Override
	public void setText(String in)
	{
		message.clear();
		message = StringParser.stringToCharList(in);
	}
	
	@Override
	public String getText()
	{
		return StringParser.charStructureToString(message);
	}
	
	@Override
	protected void drawMessage(Graphics2D g, AffineTransform AT)
	{
		if(message.isEmpty())
		{
			return;
		}

		// Extract Useful Data.
		Justification j = getJustification();
		IterB<Character> iter = message.getIter();
		
		throw new Error("Please Implement Me!");
	}

}
