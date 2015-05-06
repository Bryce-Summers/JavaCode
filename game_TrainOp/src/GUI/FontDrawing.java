package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class FontDrawing 
{
	
	public static void drawText(Graphics g, String text, OBJ2D obj)
	{
		drawText(g, text, obj, 0, 0);
	}
	
	public static void drawText(Graphics g, String text, OBJ2D obj, int offset_x, int offset_y)
	{
        
        
        FontMetrics fm = g.getFontMetrics();
        int totalWidth = (fm.stringWidth(text));
        
        int x = obj.x + (obj.getW() - totalWidth) / 2;
        // Baseline
        //int y = obj.y + (obj.getH() - fm.getHeight()) / 2;
        // Absolute
        int y = obj.y + ((obj.getH() - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, x + offset_x, y + offset_y);
	}
	
	public static void drawTextCentered(Graphics g, String text, int x_in, int y_in)
	{        
        
        FontMetrics fm = g.getFontMetrics();
        int totalWidth = (fm.stringWidth(text));
        
        int x = x_in  - (totalWidth/2);
        
        // Baseline
        //int y = obj.y + (obj.getH() - fm.getHeight()) / 2;
        // Absolute
        int y = y_in - (fm.getHeight() / 2) + fm.getAscent();
        g.drawString(text, x, y);
	}
}
