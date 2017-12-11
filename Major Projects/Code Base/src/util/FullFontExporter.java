package util;

import java.awt.Color;
import java.awt.image.BufferedImage;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import BryceImages.Operations.ImageFactory;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;

public class FullFontExporter
{
	// Exports a nicely styled image containing the entirety of Bryce Font 2.
	
	public static void exportBryceFont2()
	{
		System.out.println("Image Generation has Started");
		
		// Initialize the GUI Library.
		SpriteLoader.render1();
		//gui_button b = new gui_button(0, 0, 300, 50);
		//gui_button b = new gui_button(0, 0, 2048, 1152);
				
		int w = 2000;
		BufferedImage output = ImageFactory.blank(w, 11000);
				
		// We kind of want an even number to have upper and lowercase characters next to each other.
		int chars_per_row = 4;
			
		int space_per_character = w/chars_per_row;
		int size_of_character = (int)(space_per_character*.8);
			
		String str = "ABCDEFGHIJKLMNOPQRSTUVWX(YZ)#01234556789=+-_/\\|><:.;,\'\"?[]{}*^~`";
			
		gui_button title = new gui_button(0, space_per_character/4, w, space_per_character);
		title.setTextSize(size_of_character/2);
		title.setText("Bryce Font^{2}");
		title.setColor(new Color(0x00E0E8));
		title.serializeOntoImage(output);
			
		Character[] array = StringParser.stringToCharArr(str);
		int index = 0;
		for(char c: array)
		{
			
			int x = (index % chars_per_row) * space_per_character/2;
			int y = (index / chars_per_row) * space_per_character/2 + space_per_character;
			gui_button b = new gui_button(x, y, space_per_character, space_per_character);
			
			b.disableTEX();
			b.setText("" + c + Character.toLowerCase(c));
			b.setTextSize(size_of_character/2);
			
			if(c == ' ')
			{
				b.setColor(BryceMath.Color_hsv(60, 10, 100));
			}
					
			b.serializeOntoImage(output);
			index++;
		}
			
		String[] strs = new String[]{"\\l", "\\a", "\\b", "\\g", "\\d", "\\e", "\\t", "\\cdot", "\\(\\)", "\\[\\]", "\\{\\}", "\\frac{a}{b}", "", "x^{2}", "y_{0}", ""};
			
		// Now for the Bryce TEXed characters.
		for(String message : strs)
		{			
			int x = (index % chars_per_row) * space_per_character/2;
			int y = (index / chars_per_row) * space_per_character/2 + space_per_character;
			gui_button b = new gui_button(x, y, space_per_character, space_per_character);
			
			b.enableTEX();
			b.setText(message);
			b.setTextSize(size_of_character/2);
			
			if(str.length() == 0)
			{
				b.setColor(BryceMath.Color_hsv(60, 10, 100));
			}
				
			b.serializeOntoImage(output);
			index++;
		}
			
		/*
		//b.setColor()
		
		b.setText("Letters Made out of Shapes.");
		b.setTextSize(75);
		b.setColor(Colors.Color_hsv(0, 0, 100));
		*/
			
		ImageUtil.saveImage(output, "BryceFont2.png");
		System.out.println("Image Generation is Successful!");
	}
	
	public static void exportBryceFontString(String str) 
	{
		System.out.println("Generating a Bryce font String!");
		
		// Initialize the GUI Library.
		SpriteLoader.render1();
		//gui_button b = new gui_button(0, 0, 300, 50);
		//gui_button b = new gui_button(0, 0, 2048, 1152);

		int w = 9000;
		int h = 1000;
		BufferedImage output = ImageFactory.blank(w, h);

		gui_label b = new gui_button(0, 0, w, h);
		b.setTextSize(300);
		b.setText(str);
		b.setColor(new Color(0xffffff));
		//b.setColor(new Color(0x000000));
		//b.disableTEX();
		b.serializeOntoImage(output);
			
		/*
		//b.setColor()
		
		b.setText("Letters Made out of Shapes.");
		b.setTextSize(75);
		b.setColor(Colors.Color_hsv(0, 0, 100));
		*/
			
		ImageUtil.saveImage(output, "message.png");
		System.out.println("Image Generation is Successful!");
	}
	
}
