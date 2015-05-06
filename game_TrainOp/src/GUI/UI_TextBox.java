package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import BryceMath.Calculations.Colors;

/*
 * Text Box.
 * 
 * Text Boxes are basic GUI objects that display a rectangular region and text.
 * 
 */

public class UI_TextBox extends ImageObj 
{
	private String str;
	private Color text_color = Color.BLACK;
	private Font text_font = new Font("TimesRoman", Font.BOLD, 60);
	
	private Color border_color = Color.BLACK;
	private boolean draw_borders = true;
	
	private int text_offset_x = 0;
	private int text_offset_y = 0;
	
	// -- Constructor.
	public UI_TextBox(int x, int y, String str, BufferedImage ... images)
	{
		super(x, y, images);
		
		this.str = str;
		setColor(Colors.Color_hsv(0, 0, 80));
	}
	
	public UI_TextBox(int x, int y, int w, int h, String str)
	{
		super(x, y, w, h);
		
		this.str = str;
		
		setColor(Colors.Color_hsv(0, 0, 80));
	}
	
	@Override
	public void draw(Graphics g)
	{
		super.draw(g);
		
		if(draw_borders)
		{
			g.setColor(border_color);
			g.drawRect(x,  y,  getW() - 1, getH() - 1);
		}
		
		g.setColor(text_color);
		g.setFont(text_font);
		FontDrawing.drawText(g, str, this, text_offset_x, text_offset_y);
	}

	@Override
	public void update()
	{
		
	}
	
	public void setFont(Font f)
	{
		text_font = f;
	}
	
	public void setTextColor(Color c)
	{
		text_color = c;
	}

	public void setText(String message)
	{
		this.str = message;
	}
	
	public void setDrawBorders(boolean draw)
	{
		draw_borders = draw;
	}
	
	public void setTextOffset(int x, int y)
	{
		text_offset_x = x;
		text_offset_y = y;		
	}

}
