package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class FontDrawing 
{
	public static void drawText(Graphics g, String text, OBJ2D obj)
	{
        
        
        FontMetrics fm = g.getFontMetrics();
        int totalWidth = (fm.stringWidth(text));
        
        int x = obj.x + (obj.getW() - totalWidth) / 2;
        // Baseline
        //int y = obj.y + (obj.getH() - fm.getHeight()) / 2;
        // Absolute
        int y = obj.y + ((obj.getH() - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, x, y);
	}
}
