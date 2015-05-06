package Project.GameGrid;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import GUI.FontDrawing;
import Images.Spr;
import Project.fonts.FontManager;
import SimpleEngine.interfaces.OBJ;

public class Car implements OBJ
{
	
	public enum Dir {LEFT, UP, RIGHT, DOWN};
	private Dir myDir = Dir.RIGHT;
	
	boolean moving = false;
	
	// Stores the current load of this car.
	private int load;
	
	final double straight_per_inc = .05;
	final double curve_per_inc = .025;
	
	// The variables that specify where the logical location of the train car is.
	TrackPiece myLocation;
	double percentage = .5;
	
	// The variables that specify where the car is explicitly on the screen.
	int x, y;
	int angle = 0; // In Degrees.
		
	
	// -- Constructor.
	public Car(int load_in)
	{
		load = load_in;
	}
	
	public Car clone()
	{
		Car output = new Car(load);
		return output;
	}
	
	// Sets the location of this car.
	// We will assume that cars start on a right facing straight segment of track.
	public void setLocation(TrackPiece track)
	{
		myLocation = track;
				
		moving = true;
		
		x = myLocation.x + myLocation.getW()/2;
		y = myLocation.y + myLocation.getH()/2;
		
		percentage = .5;
		angle = 0;// Starts facing right.
		
	}
	
	// Only create this once, so it is not garbage collected.
	private AffineTransform AT = new AffineTransform();
	
	@Override
	public void draw(Graphics g)
	{
		
		// FIXME : Draw the train car at the specific x and y coordinates with the current angle.
		
		// Compute these values in the handlers 
		// and then handle the visual interpretation of them in this draw function.
		
		// Create an Affine transform and use it to draw the car using my scheme.
		
		AT.setToIdentity();
		
		AT.rotate(Math.toRadians(angle), x, y);

		AT.translate(x - Spr.car.getWidth()/2, y - Spr.car.getHeight()/2);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.drawImage(Spr.car, AT, null);
		
		g2.setFont(FontManager.font_12);
		FontDrawing.drawTextCentered(g2, "" + load, x, y);
		
	}

	@Override
	public void update()
	{
		if(!moving)
		{
			return;
		}
			
		int track_index = myLocation.track_index;
			
		// Handle the Right functions.
		if(myDir == Dir.RIGHT)
		{
			if(track_index == Spr.STRAIGHT_LEFT_RIGHT)
			{
				handle_right_straight();
			}
			else if(track_index == Spr.CURVE_UP_LEFT)
			{
				handle_right_curve_up();
			}
			else if(track_index == Spr.CURVE_DOWN_LEFT)
			{
				handle_right_curve_down();
			}			
		}
		else if(myDir == Dir.DOWN)
		{
			// Handle the Down functions.
			if(track_index == Spr.STRAIGHT_UP_DOWN)
			{
				handle_down_straight();
			}
			else if(track_index == Spr.CURVE_UP_LEFT)
			{
				handle_down_curve_left();
			}
			else if(track_index == Spr.CURVE_UP_RIGHT)
			{
				handle_down_curve_right();
			}				
		}
		else if(myDir == Dir.LEFT)
		{
			// Handle the left functions.
			if(track_index == Spr.STRAIGHT_LEFT_RIGHT)
			{
				handle_left_straight();
			}
			else if(track_index == Spr.CURVE_DOWN_RIGHT)
			{
				handle_left_curve_down();
			}
			else if(track_index == Spr.CURVE_UP_RIGHT)
			{
				handle_left_curve_up();
			}				
		}
		else if(myDir == Dir.UP)
		{
			// Handle the Up functions.
			if(track_index == Spr.STRAIGHT_UP_DOWN)
			{
				handle_up_straight();
			}
			else if(track_index == Spr.CURVE_DOWN_RIGHT)
			{
				handle_up_curve_right();
			}
			else if(track_index == Spr.CURVE_DOWN_LEFT)
			{
				handle_up_curve_left();
			}				
		}
		
	}
	
	/*
	 * Functions for handling the movement of cars along tracks.
	 */
	
	private void try_transition(Dir dir)
	{
		// No transition yet.
		if(percentage < 1 - straight_per_inc)
		{
			return;
		}
		
		TrackPiece newLocation = null;
		
		switch(dir)
		{
		case LEFT: newLocation = myLocation.getLeft();
			break;
		case UP:   newLocation = myLocation.getUp();
			break;
		case DOWN: newLocation = myLocation.getDown();
			break;
		case RIGHT:newLocation = myLocation.getRight();
			break;
		}
			
		if(newLocation == null)
		{
			handle_end_of_track();
		}
		else
		{
			myLocation = newLocation;
			percentage = 0;
			
			// Make sure we keep track of which direction the car is currently going in.
			myDir = dir;
		}
		
	}
	
	boolean alive = true;
	
	private void handle_end_of_track()
	{
		moving = false;
		alive = false;
	}

	// Interpolates the car from left to right on the track.
	private void handle_right_straight()
	{
		percentage += straight_per_inc;
		
		angle = 0;
		
		x = myLocation.x + (int)(myLocation.getW()*percentage);
		y = myLocation.y + myLocation.getH()/2;
		
		try_transition(Dir.RIGHT);		
				
	}
	
	private void handle_right_curve_up()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(-90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + radius*Math.sin(theta));
		y = (int) (myLocation.y + radius*Math.cos(theta));
		
		try_transition(Dir.UP);
	}
	
	private void handle_right_curve_down()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + radius*Math.sin(theta));
		y = (int) (myLocation.y + myLocation.getH() - radius*Math.cos(theta));
		
		
		try_transition(Dir.DOWN);
	}
	
	private void handle_up_straight()
	{
		percentage += straight_per_inc;
		
		angle = 90;
		
		x = myLocation.x + myLocation.getW()/2;
		y = myLocation.y + myLocation.getH() - (int)(myLocation.getH()*percentage);
		
		try_transition(Dir.UP);
	}
	
	private void handle_up_curve_left()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(90 - 90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + radius*Math.cos(theta));
		y = (int) (myLocation.y + myLocation.getH() - radius*Math.sin(theta));
		
		
		try_transition(Dir.LEFT);
	}
	
	private void handle_up_curve_right()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(-90 + 90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + myLocation.getW() - radius*Math.cos(theta));
		y = (int) (myLocation.y + myLocation.getH() - radius*Math.sin(theta));
		
		
		try_transition(Dir.RIGHT);
	}
	
	private void handle_left_straight()
	{
		percentage += straight_per_inc;
		
		angle = 0;
		
		x = myLocation.x + myLocation.getW() - (int)(myLocation.getH()*percentage);
		y = myLocation.y + myLocation.getH()/2;
		
		try_transition(Dir.LEFT);
	}
	
	private void handle_left_curve_up()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + myLocation.getW() - radius*Math.sin(theta));
		y = (int) (myLocation.y + radius*Math.cos(theta));
		
		
		try_transition(Dir.UP);
	}
	
	private void handle_left_curve_down()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(-90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + myLocation.getW() - radius*Math.sin(theta));
		y = (int) (myLocation.y + myLocation.getH() - radius*Math.cos(theta));
		
		
		try_transition(Dir.DOWN);
	}
	
	private void handle_down_straight()
	{
		percentage += straight_per_inc;
		
		angle = 90;
		
		x = myLocation.x + myLocation.getW()/2;
		y = myLocation.y + (int)(myLocation.getH()*percentage);
		
		try_transition(Dir.DOWN);
	}
	
	private void handle_down_curve_left()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(-90 + 90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + radius*Math.cos(theta));
		y = (int) (myLocation.y + radius*Math.sin(theta));
		
		try_transition(Dir.LEFT);
	}
	
	private void handle_down_curve_right()
	{
		percentage += curve_per_inc;
		
		int radius = myLocation.getW()*3/4;
		
		angle = (int)(90 - 90*percentage);
		
		// Angle along the curve.
		double theta = Math.toRadians(90*percentage);
		
		x = (int) (myLocation.x + myLocation.getW() - radius*Math.cos(theta));
		y = (int) (myLocation.y + radius*Math.sin(theta));
		
		try_transition(Dir.RIGHT);
	}
	
	public int getLoad()
	{
		return load;
	}
	

}
