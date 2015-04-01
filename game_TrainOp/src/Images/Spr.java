package Images;

import java.awt.image.BufferedImage;

import BryceImages.Rendering.StartRender;

public class Spr
{
	// Create a renderer that suspends threads until completion.
	private static StartRender R = new StartRender(true);
	
	public static BufferedImage[] full_tracks;
	public static BufferedImage[] tracks_rails;
	public static BufferedImage[] tracks_basalt;
	
	
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
			track = new ccHumpYardTrack(w, h, index);
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
	}
	
	// Returns true iff the given index is an index to a track type.
	public static boolean isCurve(int indice)
	{
		return 2 <= indice && indice < 6;
	}
}
