package Project.GameGrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Data_Structures.Structures.Timing.TimeB;
import GUI.FontDrawing;
import GUI.UI_Button;
import Images.Spr;
import Project.fonts.FontManager;
import Project.interfaces.Logic_Block;

/*
 * Written by Bryce Summers on 3/30/2015.
 * 
 * This class controls the behavior for individual squares on the grid.
 * 
 * These squares should extend buttons and represent allow the user to select locations in the game world.
 */

public class gridSquare extends UI_Button implements Logic_Block
{
	
	private HashSet<TrackPiece> track_set;
	
	// FIXME : Update this if we need to case upon cars.
	private HashSet<Car> car_set;
	
	private Car car_spawn;	
	private Car_controller car_controller;
				
	// Stores the current logic block associated with this square.
	private Logic_Block myLogicBlock = null;
	private gridSquare[] inputs;
	
	private static final int LEFT	= 1;
	private static final int UP		= 2;
	private static final int RIGHT	= 3;
	private static final int DOWN	= 0;
	
	// -- Logic Block code.
	final static String[] input_names = new String[]{"West", "North", "East", "South"};
	
	
	private int myDirection = -1;
	
	// The bounding_image is mainly used to specify the 2d width and height of the UI_Button.
	public gridSquare(int x, int y, BufferedImage bounding_image, Car_controller car_controller)
	{
		super(x, y, "", bounding_image);

		track_set = new HashSet<TrackPiece>();
		
		this.car_controller = car_controller;
		
		
		inputs = new gridSquare[4];
		inputs[0] = this;
		inputs[1] = this;
		inputs[2] = this;
		inputs[3] = this;
	}

	// REQUIRES: index is the index into the track image arrays.
	// x,y is the location of the sprite's in world upper left bound. 
	public void addTrack(TrackPiece spr)
	{			
		track_set.add(spr);
	}
	
	public void removeTrack(TrackPiece piece)
	{
		if(track_set.contains(piece))
		{
			track_set.remove(piece);
		}
	}
	
	// Returns true iff the track set contains this input piece.
	public boolean containsTrack(TrackPiece piece)
	{
		return track_set.contains(piece);
	}
	
	// Deletes all trackPieces from this grid square.
	// Removes all tracks from this grid square, returns the set of tracks that have been deleted.
	public Set<TrackPiece> deleteAllTracks()
	{
		HashSet<TrackPiece> copy = (HashSet<TrackPiece>) track_set.clone();
		
		for(TrackPiece piece : copy)
		{
			piece.removeTrackPiece();
		}
		
		return copy;
	}
	
	public void drawComponents(Graphics g)
	{

		g.clipRect(x, y, getW(), getH());
		g.fillRect(x, y, getW(), getH());

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

		if(car_spawn != null)
		{
			g.drawImage(Spr.car, x, y + 8, null);
			g.setFont(FontManager.font_12);
			g.setColor(Color.BLACK);
			FontDrawing.drawTextCentered(g, "" +car_spawn.getLoad(),
					x + Spr.car.getWidth()/2,
					y + Spr.car.getHeight()/2 + 8);
		}
		
		if(myLogicBlock != null)
		{
			g.setColor(Color.BLACK);
			g.setFont(FontManager.font_smaller);
			FontDrawing.drawText(g, myLogicBlock.toString(), this);
		}
				
		g.setClip(null);
		
		if(myDirection != -1)
		{
			g.drawImage(Spr.arrow_world[myDirection], x, y, null);
		}
	}
	
	// Functions that modify a gridSquare's ability to spawn cars.
	public void setCarSpawn(Car car)
	{
		car_spawn = car;
	}
	
	public void removeCarSpawn()
	{
		car_spawn = null;
		
	}
	
	// The logical inputs for a grid square.
	gridSquare input1 = null;
	gridSquare input2 = null;
	
	private enum Type{NONE, CAR_SPAWN, AND, OR, NEGATION, NUMBER};


	@Override
	public void update()
	{
		// Handle GUI Button logic.
		super.update();
		
		// Handle the in game logic.
		// Grid squares can be assigned to specific operation parameters.
		
		spawnCar();

	}
	
	TimeB spawn_timer = new TimeB(30);
	
	// Spawns a car if the input 0 is set to true.
	public void spawnCar()
	{
				
		Iterator<TrackPiece> iter = track_set.iterator();
		if(spawn_timer.flag() &&
		   car_spawn != null  && inputs[0] != null && inputs[0].getValue() && iter.hasNext())
		{
			Car car = car_spawn.clone();
			car.setLocation(iter.next());
			car_controller.addCar(car);
		}
	}
	
	public void forceCarSpawn()
	{
		
		Iterator<TrackPiece> iter = track_set.iterator();
		
		// Spawn a car on a track if it exists.
		while(iter.hasNext())
		{
			
			TrackPiece track = iter.next();
			
			if(Spr.hasLeftConnection(track.track_index))
			{
				Car car = car_spawn.clone();
				car.setLocation(track);
				car_controller.addCar(car);
				break;
			}
		}
	}
	
	// This should return the correct track piece
	// @param dir : The direction that the car is coming from.
	// ENSURES : Returns a track piece that the car can reach.
	// returns null if no connected track pieces have been found.
	public TrackPiece getTrackPiece(Dir dir_incoming, int x_in, int y_in)
	{
		Dir desired = getDesiredDir();

		// A backup candidate that is connected, but not necessarily desired.
		TrackPiece candidate = null;
		
		for(TrackPiece track : track_set)
		{
			// Found a candidate.
			if(connectedFromDirection(dir_incoming, track, x_in, y_in))
			{
				candidate = track;
			}
			
			// See if the candidate is desirable.
			if(candidate == track && connectedToDirection(desired, track))
			{
				return track;
			}

		}
		
		// If any candidate was found, then candidate will be non null.
		
		return candidate;
	}
	
	
	// FIXME : We need to handle making sure cars do not travel to curves that intersect a location,
	// but are not connected to it.
	private boolean connectedFromDirection(Dir dir_incoming, TrackPiece track, int x_in, int y_in)
	{
		int index = track.track_index;
		
		switch(dir_incoming)
		{
			case LEFT: return	index == Spr.STRAIGHT_LEFT_RIGHT ||	
								(
									(index == Spr.CURVE_UP_RIGHT  &&
									track.index_y == y_in - 1	  &&
									track.index_x == x_in - 2)
									||
									(index == Spr.CURVE_DOWN_RIGHT  &&
									track.index_y == y_in	  		&&
									track.index_x == x_in - 2)
								);
			
			case UP:   return index == Spr.STRAIGHT_UP_DOWN ||
						(
							(index == Spr.CURVE_DOWN_RIGHT  &&
							track.index_y == y_in - 2	  &&
							track.index_x == x_in)
							||
							(index == Spr.CURVE_DOWN_LEFT  &&
							track.index_y == y_in - 2 		&&
							track.index_x == x_in - 1)
						);
			
			
			case DOWN: return index == Spr.STRAIGHT_UP_DOWN ||
				(
					(index == Spr.CURVE_UP_RIGHT  &&
					track.index_y == y_in + 1	  &&
					track.index_x == x_in)
					||
					(index == Spr.CURVE_UP_LEFT  &&
					track.index_y == y_in + 1 		&&
					track.index_x == x_in - 1)
				);
			
			
			case RIGHT:return index == Spr.STRAIGHT_LEFT_RIGHT ||
					(
						(index == Spr.CURVE_UP_LEFT  &&
						track.index_y == y_in - 1	 &&
						track.index_x == x_in + 1)
						||
						(index == Spr.CURVE_DOWN_LEFT &&
						track.index_y == y_in		  &&
						track.index_x == x_in + 1)
					);
		}
		
		return false;
	}
	
	private boolean connectedToDirection(Dir dir_incoming, TrackPiece track)
	{
		int index = track.track_index;
		
		switch(dir_incoming)
		{
			case LEFT: return Spr.hasLeftConnection(index);
			case UP:   return Spr.hasUpConnection(index);
			case DOWN: return Spr.hasDownConnection(index);
			case RIGHT:return Spr.hasRightConnection(index);
		}
		
		return false;
	}
	
	// Retrieve the correct direction, based on the logical system.
	public Dir getDesiredDir()
	{
		
		// Updated for demo day.
		
		switch(myDirection)
		{
		case LEFT: return Dir.LEFT;
		case UP : return Dir.UP;
		case RIGHT : return Dir.RIGHT;
		case DOWN: return Dir.DOWN;
		default : return Dir.RIGHT;
		}
		
		
		
		/*
		
		
		// FIXME : This should be based on the logical game play, not just an arbitrary choice.
		
		// A precedence is defined to be left, up, right, down. I might want to change this to augment gameplay.
		if(inputs[LEFT] != null && inputs[LEFT].getValue())
		{
			return Dir.LEFT;
		}
		
		if(inputs[UP] != null && inputs[UP].getValue())
		{
			return Dir.UP;
		}
		
		if(inputs[RIGHT] != null && inputs[RIGHT].getValue())
		{
			return Dir.RIGHT;
		}
		
		if(inputs[DOWN] != null && inputs[DOWN].getValue())
		{
			return Dir.DOWN;
		}
		
		// Under defined behavior.
		return Dir.RIGHT;
		*/
	}

	@Override
	public String[] getInputNames() 
	{
		
		// Handle Track Mode.
		
		if(car_spawn != null)
		{
			return new String[]{"Trigger"};
		}
		
		if(!track_set.isEmpty())
		{
			return input_names;
		}
				
		if(myLogicBlock != null)
		{
			return myLogicBlock.getInputNames();
		}
		
		return new String[]{};
		
	}

	@Override
	public boolean getValue()
	{
		
		// FIXME : Major HACK for demo day.
		if(myDirection == last_input)
		{
			return true;
		}
		
		if(car_spawn != null)
		{
			if(inputs[0] != this)
			return inputs[0].getValue();
		}
		
		if(myLogicBlock != null)
		{
			return myLogicBlock.getValue();
		}
		
		// No boolean value is defined universally for a gridSquare.
		// Classes should query more specific information about the grid square if needed.
		return false;
	}

	@Override
	public void setInput(int index, gridSquare square)
	{
		inputs[index] = square;
	}

	int last_input = -1;
	
	@Override
	public gridSquare getInput(int index)
	{
		last_input = index;
		return inputs[index];
	}
	
	public void setLogicBlock(Logic_Block block)
	{
		myLogicBlock = block;
	}
	
	@Override
	public void mouseM(int x, int y)
	{
		super.mouseM(x, y);

		Grid.mouse_x = x;
		Grid.mouse_y = y;	

		updateGridGhostImage();
		
	}
	
	public void mouseR(int x, int y)
	{
		super.mouseR(x, y);
		
		updateGridGhostImage();
	}
	
	// Enable draggable actions.
	public void mouseD(int x, int y)
	{
		super.mouseD(x, y);
		action();
	}
	
	private void updateGridGhostImage()
	{
		int track_index = Grid.track_type;

		// Only have the grid draw ghost images if the track is not yet contained.
		Grid.draw_ghost = true;
		
		for(TrackPiece piece : track_set)
		{
			if(piece.x == this.x && piece.y == this.y && piece.track_index == track_index)
			{
				Grid.draw_ghost = false;
				return;
			}
		}
	}
	
	public boolean containsLogicBlock()
	{
		return myLogicBlock != null;
	}

	public boolean containsCarSpawner()
	{
		return car_spawn != null;
	}
	
	public void setDirection(int direction_index)
	{
		myDirection = direction_index;
	}

	public static String getMessageForTrackIndex(int index)
	{
		switch(index)
		{
		case LEFT: return "Westward";
		case RIGHT: return "Eastward";
		case UP: return "Northward";
		case DOWN: return "Southern";
		default : return "Direction NOT FOUND";
		}
	}

}