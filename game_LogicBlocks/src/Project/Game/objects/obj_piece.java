package Project.Game.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import Data_Structures.Structures.BitSet;
import Game_Engine.Engine.Objs.ImageB;
import Project.Editor.Components.Spr;

/* 
 * Written by Bryce Summers on 6 - 21 - 2014.
 * 
 * Purpose : Pieces need to find a goal of their color.
 */

public class obj_piece extends obj_mover
{
	// -- Constructor.
	public obj_piece(double x_in, double y_in, int index, BitSet prop)
	{
		super(x_in, y_in, index, prop);
		sprite = Spr.mover[index];
		
		setDepth(-20);
	}

	public void setArg2(int x2, int y2)
	{
		int sw = sprite.getWidth();
		int sh = sprite.getHeight();
		
		int w = (int)(x2 - getX()) + sw;
		int h = (int)(y2 - getY()) + sh;
		
		setW(w);
		setH(h);
		
		// Handle non unit sprites.
		if(w != sw || h != sh)
		{
			sprite = null;
		}
		
	}
	
	
	public void draw(ImageB i, AffineTransform AT)
	{

		
		if(sprite != null)
		{
			super.draw(i, AT);
			return;
		}
		
		Graphics2D g = i.getGraphics();
		
		g.setColor(Spr.colors_primary[index]);
		
		fillRect(g, AT, getX() + 8, getY() + 8, getW() - 16, getH() - 16);
		g.setColor(Color.BLACK);
		drawRect(g, AT, getX() + 8, getY() + 8, getW() - 16, getH() - 16);
		
		g.dispose();
		
		super.draw(i, AT);
	}
}
