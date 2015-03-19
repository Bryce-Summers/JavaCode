package Game_Engine.GUI.ImageProccessing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Game_Engine.GUI.SpriteLoader;

public class drawing_gui
{

	// FIXME : These images should be written directly without mallocing.
	public static BufferedImage drawBorders(int w, int h)
	{
		if(w <= 0 || h <= 0)
		{
			throw new Error("Width (" + w + ") and height ( " + h + ") must be greater than 0)");
		}
		
		// Compile all of the GUI image pieces into one image;
		BufferedImage sprite = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		
		// All of these images are current as of the instantiation of this gui_label.
		// All of these constructive images and values can be changed, because this label constructs its own local image to represent itself.
		int borderSize = SpriteLoader.gui_borderSize;
		
		Graphics g = sprite.getGraphics();
		
		BufferedImage i_corner = SpriteLoader.gui_corner;
		
		// Corners.
		g.drawImage(i_corner, 0, 0, borderSize*2, borderSize*2, 0, 0, borderSize*2, borderSize*2, null);
		g.drawImage(i_corner, w - borderSize*2, 0, w, borderSize*2, borderSize*2, 0, borderSize*4, borderSize*2, null);
		g.drawImage(i_corner, 0, h - borderSize*2, borderSize*2, h, 0, borderSize*2, borderSize*2, borderSize*4, null);
		g.drawImage(i_corner, w - borderSize*2, h - borderSize*2, w, h, borderSize*2, borderSize*2, borderSize*4, borderSize*4, null);

		
		// Horizontal bands.
		g.drawImage(SpriteLoader.gui_horiEdges, borderSize*2, 0,	w - borderSize*4, borderSize, null);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(SpriteLoader.gui_horiEdges2, borderSize*2, h - borderSize, w - borderSize*4, borderSize, null);


		// Vertical Bands
		g.drawImage(SpriteLoader.gui_vertEdges, 0, borderSize*2, borderSize, h - borderSize*4, null);
		g.drawImage(SpriteLoader.gui_vertEdges2, w - borderSize, borderSize*2, borderSize, h - borderSize*4, null);
		
		// Draw the image to the screen.
		return sprite;
	}
	
	public static BufferedImage drawBorders_highlight(int w, int h)
	{
		// Compile all of the GUI image pieces into one image;
		BufferedImage sprite = new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
		
		// All of these images are current as of the instantiation of this gui_label.
		//All of these constructive images and values can be changed, because this label constructs its own local image to represent itself.
		int borderSize = SpriteLoader.gui_borderSize;
		
		Graphics g = sprite.getGraphics();
		
		
		// Corners.
		g.drawImage(SpriteLoader.gui_corner_hl, 0, 0, borderSize*2, borderSize*2, 0, 0, borderSize*2, borderSize*2, null);
		g.drawImage(SpriteLoader.gui_corner_hl, w - borderSize*2, 0, w, borderSize*2, borderSize*2, 0, borderSize*4, borderSize*2, null);
		g.drawImage(SpriteLoader.gui_corner_hl, 0, h - borderSize*2, borderSize*2, h, 0, borderSize*2, borderSize*2, borderSize*4, null);
		g.drawImage(SpriteLoader.gui_corner_hl, w - borderSize*2, h - borderSize*2, w, h, borderSize*2, borderSize*2, borderSize*4, borderSize*4, null);


		// Horizontal bands.
		g.drawImage(SpriteLoader.gui_horiEdges_hl, borderSize*2, 0,	w - borderSize*4, borderSize, null);
		g.drawImage(SpriteLoader.gui_horiEdges_hl2, borderSize*2, h - borderSize, w - borderSize*4, borderSize, null);


		// Vertical Bands
		g.drawImage(SpriteLoader.gui_vertEdges_hl, 0, borderSize*2, borderSize, h - borderSize*4, null);
		g.drawImage(SpriteLoader.gui_vertEdges_hl2, w - borderSize, borderSize*2, borderSize, h - borderSize*4, null);
		
		// Draw the image to the screen.
		return sprite;
	}
	
}
