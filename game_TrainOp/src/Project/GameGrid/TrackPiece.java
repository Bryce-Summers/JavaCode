package Project.GameGrid;

import java.awt.Graphics;
import java.util.HashSet;

import Images.Spr;
import SimpleEngine.interfaces.OBJ;

/*
 * Written by Bryce Summers on 4/6/2015.
 * 
 * This class controls the display and logic of track pieces.
 */

public class TrackPiece extends Sprite implements OBJ
{
	
	// The set of grid squares that contain this TrackPiece.
	HashSet<gridSquare> myGridSet;
	
	int index_x;
	int index_y;
	
	int track_index;
	
	Grid grid;
	
	// The set of locations this track piece is drawn on.
	public HashSet<gridSquare> locations;
	
		
	// -- Constructor.

	public TrackPiece(int image_x, int image_y, int track_type, Grid grid,
			int index_x, int index_y)
	{
		super(image_x, image_y, Spr.tracks_basalt[track_type], Spr.tracks_rails[track_type]);
		
	
		this.track_index = track_type;
		
		this.index_x = index_x;
		this.index_y = index_y;
		this.grid = grid;
		
		populateLocationSet();
		addTrackPiece();
		
	}
	
	private void populateLocationSet()
	{
		locations = new HashSet<gridSquare>();
				
		if(Spr.isCurve(track_index))
		{

			// Curves are 2 by 2.
			locations.add(grid.getSquare(index_x, index_y));
			locations.add(grid.getSquare(index_x + 1, index_y));
			locations.add(grid.getSquare(index_x, index_y + 1));
			locations.add(grid.getSquare(index_x + 1, index_y + 1));
			return;
			
		}
		
		// Handle Straight pieces.
		locations.add(grid.getSquare(index_x, index_y));
		
	}
	
	// Enables this track piece.
	public void addTrackPiece()
	{
		for(gridSquare square : locations)
		{
			square.addTrack(this);
		}
	}
	
	public void removeTrackPiece()
	{
		for(gridSquare square : locations)
		{
			square.removeTrack(this);
		}
	}
	
	@Override
	public void draw(Graphics g)
	{
		for(gridSquare square : locations)
		{
			square.drawComponents(g);
		}
	
	}
	@Override
	public void update()
	{
		// Mutate the states of the train cars???		
	}
	
	public int getW()
	{
		return images[0].getWidth();
	}
	
	public int getH()
	{
		return images[0].getHeight();
	}
	
	// Functions for getting the next piece.
	
	// For a curved track, we need to compute the offset from the top left corner of the 2 by 2 
	// TrackPiece to the exact location a car will end up if it leaves the curve in the given direction.
	// The direction is enough information to tell which way the car is traveling along the curve.
	
	public TrackPiece getRight()
	{
		gridSquare square = null;
		
		int x_offset = 0;
		int y_offset = 0;
		
		if(track_index == Spr.STRAIGHT_LEFT_RIGHT)
		{
			square = grid.getSquare(index_x + 1, index_y);
		}
		else if(track_index == Spr.CURVE_UP_RIGHT)
		{
			square = grid.getSquare(index_x + 2, index_y + 1);
			x_offset = 1;
			y_offset = 1;
		}
		else if(track_index == Spr.CURVE_DOWN_RIGHT)
		{
			square = grid.getSquare(index_x + 2, index_y);
			x_offset = 1;
			y_offset = 0;
		}
		
		return square.getTrackPiece(Dir.RIGHT, index_x + x_offset, index_y + y_offset);
	}
	
	public TrackPiece getUp()
	{
		gridSquare square = null;
		
		int x_offset = 0;
		//int y_offset = 0;
		
		if(track_index == Spr.STRAIGHT_UP_DOWN)
		{
			square = grid.getSquare(index_x, index_y - 1);
		}
		else if(track_index == Spr.CURVE_UP_LEFT)
		{
			square = grid.getSquare(index_x + 1, index_y - 1);
			x_offset = 1;
		}
		else if(track_index == Spr.CURVE_UP_RIGHT)
		{
			square = grid.getSquare(index_x, index_y - 1);			
		}
		
		return square.getTrackPiece(Dir.UP, index_x + x_offset, index_y);
	}
	
	public TrackPiece getLeft()
	{
		gridSquare square = null;
		
		//int x_offset = 0;
		int y_offset = 0;
		
		if(track_index == Spr.STRAIGHT_LEFT_RIGHT)
		{
			square = grid.getSquare(index_x - 1, index_y);	
		}
		else if(track_index == Spr.CURVE_UP_LEFT)
		{
			square = grid.getSquare(index_x - 1, index_y + 1);
			y_offset = 1;
		}
		else if(track_index == Spr.CURVE_DOWN_LEFT)
		{
			square = grid.getSquare(index_x - 1, index_y);			
		}
		
		return square.getTrackPiece(Dir.LEFT, index_x, index_y + y_offset);
	}
	
	public TrackPiece getDown()
	{
		
		int offset_x = 0;
		int offset_y = 0;
		gridSquare square = null;
		if(track_index == Spr.STRAIGHT_UP_DOWN)
		{
			square = grid.getSquare(index_x, index_y + 1);	
		}
		else if(track_index == Spr.CURVE_DOWN_LEFT)
		{
			square = grid.getSquare(index_x + 1, index_y + 2);
			offset_x = 1;
			offset_y = 1;
		}
		else if(track_index == Spr.CURVE_DOWN_RIGHT)
		{
			square = grid.getSquare(index_x, index_y + 2);
			offset_y = 1;
		}
		
		return square.getTrackPiece(Dir.DOWN, index_x + offset_x, index_y + offset_y);
		
		
	}

	public boolean isCurve()
	{
		return Spr.isCurve(track_index);
	}
	
	// -- Methods to make track Pieces compatible with hash functions.
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof TrackPiece))
		{
			return false;
		}
		
		TrackPiece other = (TrackPiece)obj;
		
		return 
			index_x == other.index_x &&
			index_y == other.index_y &&
			track_index == other.track_index;
	}
	
	@Override
	public int hashCode()
	{
		return index_x + 298087*index_y + track_index*300017;
	}
}
