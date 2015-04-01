package Project;

import java.awt.Font;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;
import GUI.UI_Button;
import GUI.UI_List;
import Images.Spr;
import Project.GameGrid.Grid;
import SimpleEngine.MainRoom;

/*
 * This class sets up the main game room.
 * 
 * Written by Bryce Summers.
 * 
 * Adapted to my train sorting game on 3/30/2015.
 */

public class aaTrainOp extends MainRoom
{

	public static void main(String[] args)
	{
		// Render the track images.
		Spr.renderTracks();
		
		new aaTrainOp("Train Ops!", 1200, 800);
	}
	
	public aaTrainOp(String gameName, int w, int h)
	{
		super(gameName, w, h);
		
		int wr = 200;
		int hr = 64;
				
		//BufferedImage image = ImageFactory.ColorRect(Colors.Color_hsv(60, 25, 100), wr, hr);
		
		UI_List left = new UI_List(0, 200, wr, hr*5, UI_List.DIR.DOWN);
		addOBJ(left);
		
		UI_Button b;
		Font text_font = new Font("TimesRoman", Font.BOLD, 30);

		Grid world = new Grid(200, 200, w - 200, h - 200, 32);
		addOBJ(world);
		
		
		for(int i = 0; i < 6; i++)
		{
			b = new UI_Button(i*64, 0, "", Spr.full_tracks[i]);
			int index = i;
			b.setAction(() -> world.setTrackType(index));
			addOBJ(b);
			x += 64;
		}
		



	}

}
