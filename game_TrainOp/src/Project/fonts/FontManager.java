package Project.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FontManager
{
	//static String font_name = "TimesRoman" 
	static String font_name = "Century Schoolbook T";
	
	public static final Font font_block = deriveFont(Font.BOLD, 60);
	public static final Font font_gridBlock = deriveFont(Font.BOLD, 30);
	public static final Font font_smaller = deriveFont(Font.BOLD, 15);
	public static final Font font_12 = deriveFont(Font.BOLD, 12);
	public static final Font font_15 = deriveFont(Font.BOLD, 15);
	
	public static final Font font_24 = deriveFont(Font.BOLD, 24);
	
	
	public static final Font font_LOGIC_BLOCK = deriveFont(Font.PLAIN, 24);
	public static  Font block_label = deriveFont(Font.BOLD, 20);//new Font(font_name, Font.BOLD, 20);
	
	
	
	public static Font deriveFont(int style, int size)
	{
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("Century Schoolbook T..ttf"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		font = font.deriveFont(style, size);
		return font;
		
		/*
		fontSmall = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("font.ttf"));
		fontSmall.deriveFont(16F);
		*/
	}
	
}
