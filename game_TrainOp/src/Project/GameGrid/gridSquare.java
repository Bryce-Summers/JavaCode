package Project.GameGrid;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import GUI.UI_Button;
import Images.Spr;

/*
 * Written by Bryce Summers on 3/30/2015.
 * 
 * This class controls the behavior for individual squares on the grid.
 * 
 * These squares should extend buttons and represent allow the user to select locations in the game world.
 */

public class gridSquare extends UI_Button
{
	
	private HashSet<Sprite> track_set;

	// The bounding_image is mainly used to specify the 2d width and height of the UI_Button.
	public gridSquare(int x, int y, BufferedImage bounding_image)
	{
		super(x, y, "", bounding_image);

		track_set = new HashSet<Sprite>();
	}

	// REQUIRES: index is the index into the track image arrays.
	// x,y is the location of the sprite's in world upper left bound. 
	public void addTrack(Sprite spr)
	{			
		track_set.add(spr);
	}
	
	public void drawComponents(Graphics g)
	{		
		
		g.clipRect(x, y, getW(), getH());

		// -- Basalt.
		for(Sprite index : track_set)
		{
			index.draw(g, 0);
		}
		
		// -- Rails.
		for(Sprite index : track_set)
		{
			index.draw(g, 1);
		}
		
		
		g.setClip(null);
	}
	
}
