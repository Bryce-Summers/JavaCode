package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextBox extends ImageObj 
{
	private String str;
	private Color text_color = Color.BLACK;
	private Font text_font = new Font("TimesRoman", Font.BOLD, 60);
	
	// -- Constructor.
	public TextBox(int x, int y, String str, BufferedImage ... images)
	{
		super(x, y, images);
		
		this.str = str;
	}
	
	@Override
	public void draw(Graphics g)
	{
		super.draw(g);
		
				
		g.setColor(text_color);
		g.drawRect(x,  y,  getW(), getH());
		g.setFont(text_font);
		FontDrawing.drawText(g, str, this);
	}

	@Override
	public void update()
	{
		
	}

}
