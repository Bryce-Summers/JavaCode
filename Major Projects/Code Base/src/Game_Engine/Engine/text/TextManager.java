package Game_Engine.Engine.text;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import util.StringParser;
import BryceImages.Operations.Drawing;
import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.StartRender;
import BryceMath.Calculations.MathB;
import Data_Structures.Structures.Box;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.AArray;
import Game_Engine.Engine.text.Symbols.ccDot;

/*
 * The text manager class.
 * 
 * Written by Bryce Summers on 6 - 13 - 2013.
 * 
 * Update in 7 - 2013.
 *  - Improved the Bryce TEX Capabilities.
 *  
 * Updated on 8 - 13 - 2013.
 *  - Expanded the Glyph rendering capabilities.
 *  - Added support for fraction line glyphs, square roots, and greek letters. FIXME : Make this true.
 * 
 * Purpose : This class upgrades my text rendering capabilities.
 * 
 * Note : This class is completely static.
 * 
 * This class provides the functionality for rendering, caching and displaying letter and symbol glyphs to the screen.
 * The properties of all of the glyphs are stored as Symbols which are indexes
 * to an associative array that maps Symbols to Buffered images.
 * If a given symbol has no associated value within this array,
 * then it will be rendered by a rendering engine and then stored.
 */

// FIXME : Implement the deletion of all images of a specific size.
// FIXME : Implement methods for size changes.

// FIXME : Improve Implement Bryce Latexing.

// FIXME : Make Bryce TEX completely recursive.
// FIXME : Eliminate duplicate BufferedImage drawing.
// FIXME : Implement the drawing of greek letters and arithmetic symbols like dots, division signs, ect.
// FIXME : Create a 1 pixel sized image for scaling to rectangular regions.

// FIXME : Optimize the calls to populate images, perhaps the user should be able to compute that once 
//         and then access the other functions such as getCharacterOffsets().

// FIXME : Allow glyphs to be drawn in various colors.

public class TextManager
{
	
	// -- Private classes.
	
	// We store all of the rendered characters and symbols as data in the hash table.
	public static class Symbol
	{
		// Private data.
		private final String val;
		private final int height;
		
		public Symbol(char c, int h)
		{
			val = "" + c;
			height = h;
		}
		
		public Symbol(String str, int h)
		{
			val = str;
			height = h;
		}
	
		@Override
		public int hashCode()
		{
			int code = val.hashCode();
			return code + height*code;
		}
		
		/* This function must be correct.
		 * Take special caution when changing the type of values Symbols are built upon.
		 */
		@Override
		public boolean equals(Object o)
		{
			if(!(o instanceof Symbol))
			{
				return false;
			}
			
			Symbol other = (Symbol)o;
			
			return other.val.equals(val) && other.height == height;
		}
		
		// REQUIRES : This string should have been created from
		//			  the local final String constants pool.
		public boolean string_equal(String str)
		{
			return val == str;
		}
		
		public boolean isCharacter()
		{
			return val.length() == 1;
		}
		
		// REQUIRES : Should only be called after isCharacter() returns true.
		public char getChar()
		{
			return val.charAt(0);
		}
		
		public int getH()
		{
			return height;
		}

	}
	
	// We will use this class to bind coordinates that are offset from 0 to BufferedImages.
	// These can them be drawn offset from any desired user location.
	
	private static class LocationImage
	{
		private BufferedImage image;
		private int x_offset;
		private int y_offset;
		
		private int v_scale = 1;
		private int h_scale = 1;
		
		public LocationImage(BufferedImage image, int x, int y)
		{
			this.image = image;
			this.x_offset = x;
			this.y_offset = y;
		}
		
		public int getW()
		{
			return image.getWidth();
		}
		
		/*
		public int getH()
		{
			return image.getHeight();
		}
		
		public BufferedImage getImage()
		{
			return image;
		}*/
	}
	
	// -- Local data.
	
	
	public static AArray<Symbol, BufferedImage> text = new AArray<Symbol, BufferedImage>();
	
	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);
		
	public static final int TEXT_SIZE = 20;
	
	// -- Symbol codes.
	
	// USED for a pixel sized black rectangle used to draw lines for fractions, squareroots, etc.
	public static final String STR_PIXEL = "PIX";
	
	// Greek undercase l character.
	public static final String g_LAMBDA = "\\l";
	public static final String g_ALPHA = "\\a";
	public static final String g_BETA = "\\b";
	public static final String g_GAMMA = "\\g";
	public static final String g_EPSILON = "\\e";
	public static final String g_THETA = "\\t";
	public static final String g_DELTA = "\\d";
	
	
	// Bryce TEX for the "dot" symbol which means multiplication, or other things.
	public static final String DOT = "\\cdot";
	
	// Extra tall Parens.
	public static final String L_PAREN = "\\(";
	public static final String R_PAREN = "\\)";
	public static final String L_SQUARE_PAREN = "\\[";
	public static final String R_SQUARE_PAREN = "\\]";
	public static final String L_CURLY_PAREN = "\\{";
	public static final String R_CURLY_PAREN = "\\}";

	// -- Text Rendering functions.
	// Renders a the given character at the given resolution.
	private static BufferedImage render(Symbol s)
	{
		
		if(text.lookup(s) != null)
		{
			throw new Error("Do not render the same symbols excessive times!");
		}
		
		BufferedImage output;

		// --  Render the image.
		
		// Symbol case.
		if(s.isCharacter())
		{
			output = R.render(new ccAlphabet(s.getChar(), s.getH()));
		}
		else if(s.string_equal(STR_PIXEL))
		{
			output = ImageFactory.ColorRect(Color.black, 1, 1);
		}
		
		// Greek letter + Symbol rendering.
		else if(s.string_equal(g_LAMBDA))
		{
			output = R.render(new ccGreekAlphabet('l', s.getH()));
		}
		else if(s.string_equal(g_ALPHA))
		{
			output = R.render(new ccGreekAlphabet('a', s.getH()));
		}
		else if(s.string_equal(g_BETA))
		{
			output = R.render(new ccGreekAlphabet('b', s.getH()));
		}
		else if(s.string_equal(g_GAMMA))
		{
			output = R.render(new ccGreekAlphabet('g', s.getH()));
		}
		else if(s.string_equal(g_DELTA))
		{
			output = R.render(new ccGreekAlphabet('d', s.getH()));
		}
		else if(s.string_equal(g_EPSILON))
		{
			output = R.render(new ccGreekAlphabet('e', s.getH()));
		}
		else if(s.string_equal(g_THETA))
		{
			output = R.render(new ccGreekAlphabet('t', s.getH()));
		}	
		else if(s.string_equal(DOT))// In line Dot.
		{
			output = R.render(new ccDot(s.getH(), s.getH()));
		}
		else if(s.string_equal(L_PAREN))// ( Big.
		{
			output = R.render(new ccLargeBrackets(s.getH(), '('));
		}
		else if(s.string_equal(R_PAREN))// ) Big.
		{
			output = R.render(new ccLargeBrackets(s.getH(), ')'));
		}
		else if(s.string_equal(L_SQUARE_PAREN))// [ Big.
		{
			output = R.render(new ccLargeBrackets(s.getH(), '['));
		}
		else if(s.string_equal(R_SQUARE_PAREN))// ] Big.
		{
			output = R.render(new ccLargeBrackets(s.getH(), ']'));
		}
		else if(s.string_equal(L_CURLY_PAREN))// { Big.
		{
				output = R.render(new ccLargeBrackets(s.getH(), '{'));
		}
		else if(s.string_equal(R_CURLY_PAREN))// } Big.
		{
			output = R.render(new ccLargeBrackets(s.getH(), '}'));
		}
		else
		{
			throw new Error(" The String:   " + s.val + "   is not supported yet!");
		}

		// Insert the symbol - image pair into the associative array.
		text.insert(s, output);
		
		return output;
	}
	
	// Draws the text Left justified horizontally and centered vertically at the given coordinates.
	public static void drawTextLeft(Graphics2D g, AffineTransform AT, double x, double y, String str, boolean should_tex)
	{
		drawTextLeft(g, AT, x, y, str, TEXT_SIZE, should_tex);
	}
	
	// Draws the text Left justified horizontally and centered vertically at the given coordinates.
	public static void drawTextLeft(Graphics2D g, AffineTransform AT, double x, double y, String str, int text_size, boolean should_tex)
	{
		// Do not even attempt to process trivial strings.
		if(str.length() < 1)
		{
			return;
		}
		
		// The user wants the text to be centered vertically as well.
		int textX = (int)x;
		int textY = (int)y - text_size/2;
		
		// Populate the list of images
		List<LocationImage> images = populateImageList(str, text_size, should_tex);
		
		// Draw the list of images.
		drawText(g, AT, textX, textY, images, text_size);
		
	}
	
	// Draws the text center justified horizontally and centered vertically at the given coordinates.
	public static void drawTextCenter(Graphics2D g, AffineTransform AT, double x, double y, String str, boolean should_tex)
	{		
		drawTextCenter(g, AT, x, y, str, TEXT_SIZE, should_tex);
	}
	
	/* Draws the text center justified horizontally and centered vertically at the given coordinates.
	 * RETURNS : returns the x coordinate that the left side of the text is drawn to.
	 */
	public static int drawTextCenter(Graphics2D g, AffineTransform AT, double x, double y, String str, int text_size, boolean should_tex)
	{		
		// Do not even attempt to process trivial strings.
		if(str.length() < 1)
		{
			return (int)x;
		}
		
		// Create the list of images to draw to the screen
		List<LocationImage> images = populateImageList(str, text_size, should_tex);
		
		// Compute the length of the message.
		int textW = getLen(images, text_size);
		
		// Offset the textX by the appropriate half amount.
		int textX = (int)x - textW/2;
		
		// The user wants the text to be centered vertically as well.
		int textY = (int)y - text_size/2;
		
		drawText(g, AT, textX, textY, images, text_size);
		return textX;
	}
	
	// Draws the text center justified horizontally and centered vertically at the given coordinates.
	public static void drawTextRight(Graphics2D g, AffineTransform AT, double x, double y, String str, boolean should_tex)
	{		
		drawTextCenter(g, AT, x, y, str, TEXT_SIZE, should_tex);
	}
	
	// Draws the text right justified horizontally and centered vertically at the given coordinates.
	public static void drawTextRight(Graphics2D g, AffineTransform AT, double x, double y, String str, int text_size, boolean should_tex)
	{		
		// Do not even attempt to process trivial strings.
		if(str.length() < 1)
		{
			return;
		}
		
		// -- Create a list of offset images.
		List<LocationImage> images = populateImageList(str, text_size, should_tex);
		
		// Compute the length of the message.
		int textW = getLen(images, text_size);
		
		// Offset the textX by the appropriate half amount.
		int textX = (int)x - textW;
		
		// The user wants the text to be centered vertically as well.
		int textY = (int)y - text_size/2;
				
		drawText(g, AT, textX, textY, images, text_size);
		
	}
	
	private static void drawText(Graphics2D g, AffineTransform AT, int textX, int textY, List<LocationImage> images, int text_size)
	{
		// First translate the x_offset and y_offset coordinates correctly.
		if(AT != null)
		{
			textX = (int)(textX + AT.getTranslateX());
			textY = (int)(textY + AT.getTranslateY());
		}
		
		// Draw all of the characters to the screen in accordance to their offsets.
		for(LocationImage c : images)
		{
			if(c.image == null)
			{
				continue;
			}
			//g.drawImage(c.image, textX + c.x_offset, textY + c.y_offset, null);
			Drawing.draw_scaled(g, textX + c.x_offset, textY + c.y_offset, c.image, c.h_scale, c.v_scale);
		}
	}
	
	// Allows for easy computation of how wide a given message computed from a string will be on screen.
	public static int getLen(String str, boolean should_tex)
	{
		return getLen(populateImageList(str, TEXT_SIZE, should_tex), TEXT_SIZE);
	}
	
	// Allows for easy computation of how wide a given message computed from a string will be on screen.
	public static int getLen(String str, int text_size, boolean should_tex)
	{
		return getLen(populateImageList(str, text_size, should_tex), text_size);
	}
	
	/* Returns the length of the populated message,
	 * which is the farthest right hand side x_offset coordinate amongst all of the images that have been populated,
	 * This exploits the requirement that the image populator always includes a bogus image specifying the width at the end.
	 */	
	// REQUIRES : The list must have been populated from this class's image populator.
	// ENSURES : Returns the length of the given graphical string on screen.
	private static int getLen(List<LocationImage> images, int text_size)
	{
		return images.getLast().x_offset;
	}
	
	// Returns the current size of the gap between letters.
	public static int getSpacing(int text_size)
	{
		return text_size/10;
	}
	
	// Returns a populated list offset from 0, 0; This is the method that should be called.
	private static List<LocationImage> populateImageList(String str, int size, boolean should_tex)
	{
		return populateImageList(0, 0, str, size, should_tex);
	}
	
	// Takes a string and returns a list of Images that each have meta data specifying their offsets.
	// Also includes a bogus last Location image that specifies the width.
	private synchronized static List<LocationImage> populateImageList(int x, int y, String str, int size, boolean should_tex)
	{

		// First populate a list of the images to be drawn.
		List<LocationImage> output = new List<LocationImage>();
		
		IterB<Character> iter = StringParser.createIterator(str);

		// Do not lose data while parsing commands.
		char last = ' ';
		
		while(iter.hasNext())
		{
			char c = iter.next();
		
			// This is where the Bryce TEXing happens...
			if(should_tex)
			switch(c)
			{
				// Subscripts.
				case '_' :
					x = proccessSmall(x, y, iter, output, size, true);
					continue;
					
				// Superscripts.
				case '^' :
					x = proccessSmall(x, y, iter, output, size, false);
					continue;

				// FIXME : Implement more commands.
				// Commands. These take the form \[Command_name] or \[Name]{parameter}{parameter}... 
				// There can be spaces between the name and the left parentheses.
				case '\\':

					StringBuilder sub_exp = new StringBuilder();
					
					if(!iter.hasNext())
					{
						break;
					}
					
					c = iter.next();
					
					boolean reachedEnd = false;
					
					// 1. Read in the command name until we reach a character that
					// should not be in a command name.
					// Note that some commands will also be comprised of 1 non alphanumeric character.
					while(StringParser.isAlphaNumericCharacter(c))
					{
						sub_exp.append(c);
						
						if(iter.hasNext() == false)
						{
							reachedEnd = true;
							break;
						}
						
						c = iter.next();
					}
					
					last = c;

					int BigY = y - size/2 + 1;
					
					// -- Now case based upon which command was entered.
					String command = sub_exp.toString();
					
					// Handle escaped characters such as \{ or \\.
					int len = command.length();
					if(len == 0)
					{
						command = "" + last;
					}
					
					if(command.equals("frac"))
					{
						x = parseFraction(x, y, c, iter, output, size);
					}
					
					// The greek character lambda. '\l'.
					else if(command.equals("l"))
					{	
						Symbol s = new Symbol(g_LAMBDA, size);
						x = proccessNormal(x, y, s, output);			
					}
					// @GREEK.
					else if(command.equals("a"))
					{	
						Symbol s = new Symbol(g_ALPHA, size);
						x = proccessNormal(x, y, s, output);			
					}
					else if(command.equals("b"))
					{	
						Symbol s = new Symbol(g_BETA, size);
						x = proccessNormal(x, y, s, output);			
					}
					else if(command.equals("g"))
					{	
						Symbol s = new Symbol(g_GAMMA, size);
						x = proccessNormal(x, y, s, output);			
					}
					else if(command.equals("d"))
					{	
						Symbol s = new Symbol(g_DELTA, size);
						x = proccessNormal(x, y, s, output);			
					}
					else if(command.equals("t"))
					{	
						Symbol s = new Symbol(g_THETA, size);
						x = proccessNormal(x, y, s, output);			
					}
					else if(command.equals("e"))
					{	
						Symbol s = new Symbol(g_EPSILON, size);
						x = proccessNormal(x, y, s, output);			
					}
					else if(command.equals("cdot"))
					{
						Symbol s = new Symbol(DOT, size);
						x = proccessNormal(x, y, s, output);
					}
					else if(command.equals("("))
					{
						Symbol s = new Symbol(L_PAREN, size*2);
						x = proccessNormal(x, BigY, s, output);
					}
					else if(command.equals(")"))
					{
						Symbol s = new Symbol(R_PAREN, size*2);
						x = proccessNormal(x, BigY, s, output);
					}
					else if(command.equals("["))
					{
						Symbol s = new Symbol(L_SQUARE_PAREN, size*2);
						x = proccessNormal(x, BigY, s, output);
					}
					else if(command.equals("]"))
					{
						Symbol s = new Symbol(R_SQUARE_PAREN, size*2);
						x = proccessNormal(x, BigY, s, output);
					}
					else if(command.equals("{"))
					{
						Symbol s = new Symbol(L_CURLY_PAREN, size*2);
						x = proccessNormal(x, BigY, s, output);
					}
					else if(command.equals("}"))
					{
						Symbol s = new Symbol(R_CURLY_PAREN, size*2);
						x = proccessNormal(x, BigY, s, output);
					}
					
					// Handle reading non command symbols.
					if(last == ' ' ||(len > 0 && !reachedEnd && !command.equals("frac")))
					{
						// Back the iterator up to prevent the loss of trailing characters.
						iter.previous();
					}
					
					// Continue, because these are not normal processes.
					continue;
			}
			
			// Process the perfunctory character.
			Symbol s = new Symbol(c, size);
			x = proccessNormal(x, y, s, output);
			
		}
		
		LocationImage bogus = new LocationImage(null, x, y);
		
		output.add(bogus);
		
		return output;
	}

	private static int proccessNormal(int x, int y, Symbol s, List<LocationImage> output)
	{			
		return proccessNormal(x, y, 1, 1, s, output);
	}
	
	private static int proccessNormal(int x, int y, int h_scale, int v_scale, Symbol s, List<LocationImage> output)
	{			
		// Lookup the character's memoized image.
		BufferedImage c_image = text.lookup(s);
		
		// Render a new image if it has not already been rendered.
		if(c_image == null)
		{
			c_image = render(s);
		}

		LocationImage image_new = new LocationImage(c_image, x, y);
		image_new.v_scale = v_scale;
		image_new.h_scale = h_scale;
		
		output.add(image_new);

		return x + image_new.getW() + getSpacing(s.getH());
	}

	// Returns the X coordinate of the right of the parsed expression.
	private static int proccessSmall(int x, int y, Iterator<Character> iter, List<LocationImage> data, int size, boolean sub)
	{
		char c = '_';
		
		// 1. Search for the start of the subExp.
		while(c != '{' && iter.hasNext())
		{
			c = iter.next();
		}
		
		String sub_exp = parseBracketedObject(iter, '{', '}');
		
		// Create the sub image list depending on whether we are in super script of subscript mode.
		List<LocationImage> sub_images;
		
		if(sub)
		{
			sub_images = populateImageList(x + getSpacing(size), y + size/3 + 1, sub_exp, size*2/3, true);
		}
		else
		{
			sub_images = populateImageList(x + getSpacing(size), y, sub_exp, size*2/3, true);			
		}
		
		// Update the x coordinate from the width of the sub expression.
		// Also remove the bogus ending term.
		int output = sub_images.pop().x_offset;
		
		data.append(sub_images);
		
		return output;
	}
	
	// Returns the X coordinate of the right of the parsed expression.
	// Fractions are created from BryceTEX string components that are the same size as the calling string.
	// The numerator and denominator are centered and aligned on to and bottom of a newly created middle line that is in line with the calling BryceTEX string.
	private static int parseFraction(int x, int y, char last_char, Iterator<Character> iter, List<LocationImage> output, int size)
	{
		// Move the iterator to the start of the numerator.
		if(last_char != '{')
		{
			StringParser.scanFor(iter, '{');
		}
		
		String numerator = parseBracketedObject(iter, '{', '}');
		
		// Move the iterator to the start of the denominator.
		StringParser.scanFor(iter, '{');
		
		String denominator = parseBracketedObject(iter, '{', '}');
		
		int len_num		= getLen(numerator, size, true);
		int len_denom	= getLen(denominator, size, true);
		
		int len = MathB.max(len_num, len_denom);
		
		// Populate the characters on the top and bottom of the fraction.
		
		// -- Numerator.
		int num_text_x = x + len/2 - len_num/2;
		int num_text_y =  y - size*6/10 - 1;
		List<LocationImage> output_part = 
					populateImageList(	num_text_x, num_text_y,
										numerator, size, true);
		
		// FIXME : Perhaps refactor this duplicate code with the code below into a new Glyph list concatenation function.
		
		// Remove the size meta data.
		output_part.pop();
		
		// add the glyphs to the collection.
		output.append(output_part);
		
		// -- Denominator.
		int denom_text_x = x + len/2 - len_denom/2;
		int denom_text_y = y + size*6/10 + 1;
		output_part = populateImageList(denom_text_x, denom_text_y,
										denominator, size, true);
		
		// Remove the size meta data.
		output_part.pop();

		// -- Add the line between the fractions.
		proccessNormal(x, denom_text_y - size/5 - 1, len,
					   size/8 + 1, new Symbol(STR_PIXEL, size), output_part);
		
		// add the glyphs to the collection.
		output.append(output_part);
		
		return x + len;
	}
	
	// FIXME : This function should be made even more generic and be used as a scanfor function.
	
	// A helper function that takes and Iterator that has just iterated to/past the left bracket and returns the string enclosed by the brackets.
	// If the string ends with no right bracket, then it returns a string containing all of the data.
	// NOTE : This function also mutates the Iterator, so the calling method's iterator will be in the proper place.
	// This function should properly parse nested bracketing constructs as well.
	private static String parseBracketedObject(Iterator<Character> iter, char left_bracket, char right_bracket)
	{
		int parens_depth = 0;
		
		// -- Parse the sub Exp.
		StringBuilder subExp = new StringBuilder();
		
		while(iter.hasNext())
		{
			char c = iter.next();
			
			if(c == left_bracket)
			{
				parens_depth++;
			}
			
			if(c == right_bracket)
			{
				if(parens_depth == 0)
				{
					break;
				}
				else
				{
					parens_depth--;
				}
			}

			subExp.append("" + c);
		}

		return subExp.toString();
	}
	
	// Returns an array with the x offsets for each character in the message.
	// This array is inclusive of the left and right extreme edges.
	// Note : Only supports NON-TEX right now.
	public static int[] getCharacterOffsets(String s, int size)
	{
		List<LocationImage> glyphs = populateImageList(s, size, false);
		
		int[] output = new int[glyphs.size()];
		
		int i = 0;
		
		for(LocationImage image : glyphs)
		{
			output[i] = image.x_offset;
			i++;
		}
		
		return output;
	}
	
	public static BufferedImage getGlyph(char c, int size, Box<Integer> h_offset)
	{
		List<LocationImage> glyphs = populateImageList(c+"", size, false);
		
		LocationImage letter = glyphs.getFirst();
		
		h_offset.val = letter.y_offset;

		return letter.image;
	}
}