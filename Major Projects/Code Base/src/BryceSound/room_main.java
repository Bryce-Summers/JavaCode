package BryceSound;

import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_output;
import Game_Engine.GUI.Components.small.gui_button;

public class room_main extends Room
{

	// -- GUI fields.
	public gui_button play_again;
	public gui_button new_button;
	
	sequencer s;
	
	public room_main(Game_output out)
	{
		super(out);
	}

	@Override
	public void iObjs()
	{
		s = new sequencer();
		
		soundCalculator sc = s;//new scSine();
				
		new synthesizer(sc);
		
		
		// -- gui initialization.
		
		play_again = new gui_button(getW()/4, getH()/4, getW()/2, getH()/2);
		play_again.setText("Play Again");
		obj_create(play_again);
		
		new_button = new gui_button(getW()/4, 3*getH()/4, getW()/2, getH()/4);
		new_button.setText("New Rhythm.");
		obj_create(new_button);
	}

	public void update()
	{
		super.update();
		
		if(play_again.flag())
		{
			s.playAgain();
		}
		
		if(new_button.flag())
		{
			s.generate_random_rythm();
		}
		
	}
	
}
