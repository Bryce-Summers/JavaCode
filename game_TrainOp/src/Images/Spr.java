package Images;

import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.StartRender;

public class Spr
{
	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);
	
	public static BufferedImage[] full_tracks;
	public static BufferedImage[] tracks_rails;
	public static BufferedImage[] tracks_basalt;
	
	public static BufferedImage car;
	
	public static BufferedImage car_icon;
	
	// Descriptive constants for the various pieces of track.
	public final static int STRAIGHT_LEFT_RIGHT = 1;
	public final static int STRAIGHT_UP_DOWN    = 0;
	
	public final static int CURVE_UP_RIGHT   = 2;
	public final static int CURVE_UP_LEFT    = 4;
	public final static int CURVE_DOWN_RIGHT = 3;
	public final static int CURVE_DOWN_LEFT  = 5;
	
	
	public static BufferedImage arrow_icon[] = new BufferedImage[4];
	public static BufferedImage arrow_world[] = new BufferedImage[4];
	
	
	public static void renderTracks()
	{
		full_tracks   = new BufferedImage[6];
		tracks_rails  = new BufferedImage[6];
		tracks_basalt = new BufferedImage[6];
		
		ccHumpYardTrack track;
		
		int w = 64;
		int h = 64;
		
		for(int index = 0; index < 6; index++)
		{
			
			if(isCurve(index))
			{
				track = new ccHumpYardTrack(w, h, index);
			}
			else
			{
				track = new ccHumpYardTrack(w/2, h/2, index);
			}
			
			full_tracks[index] = R.render(track);
			
			track.draw_rails = false;
			track.clear();
			track.i_geoms();
			tracks_basalt[index] = R.render(track);
			
			track.draw_rails = true;
			track.draw_background = false;
			track.clear();
			track.i_geoms();
			tracks_rails[index] = R.render(track);
		}
		
		car = R.render(new ccTrainCar(w/2, h/4, 0, 100));
		
		car_icon = R.render(new ccTrainCarIcon(w - 16, h/2, 0, 100, 100));
		
		// Arrow.
		
		arrow_icon[0]  = R.render(new ccDown(50, 50));
		arrow_world[0] = R.render(new ccDown(32, 32));
		
		arrow_icon[1] = ImageFactory.rotate(arrow_icon[0], 90);
		arrow_world[1] = ImageFactory.rotate(arrow_world[0], 90);
		
		arrow_icon[2] = ImageFactory.rotate(arrow_icon[0], 180);
		arrow_world[2] = ImageFactory.rotate(arrow_world[0], 180);
		
		arrow_icon[3]  = ImageFactory.rotate(arrow_icon[0],  270);
		arrow_world[3] = ImageFactory.rotate(arrow_world[0], 270);
		
		
	}
	
	// Returns true iff the given index is an index to a track type.
	public static boolean isCurve(int indice)
	{
		return 2 <= indice && indice < 6;
	}
	
	// Input : a track index.
	public static boolean hasRightConnection(int index)
	{
		return	index == STRAIGHT_LEFT_RIGHT ||
				index == CURVE_UP_RIGHT ||
				index == CURVE_DOWN_RIGHT;
	}
	
	// Input : a track index.
	public static boolean hasUpConnection(int index)
	{
		return	index == STRAIGHT_UP_DOWN ||
				index == CURVE_UP_RIGHT ||
				index == CURVE_UP_LEFT;
	}
	
	// Input : a track index.
	public static boolean hasLeftConnection(int index)
	{
		return	index == STRAIGHT_LEFT_RIGHT ||
				index == CURVE_UP_LEFT ||
				index == CURVE_DOWN_LEFT;
	}
	
	// Input : a track index.
	public static boolean hasDownConnection(int index)
	{
		return	index == STRAIGHT_UP_DOWN ||
				index == CURVE_DOWN_RIGHT ||
				index == CURVE_DOWN_LEFT;
	}
}
