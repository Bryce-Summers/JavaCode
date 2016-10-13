package BryceSound;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

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
		
		soundCalculator sc = s;//new scSine(1000);

		//short[] data = importSoundData();
		new synthesizer(sc); //(data);
		
		
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
	
	private short[] importSoundData() 
	{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AudioInputStream in;
		try {
			in = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("Bassoon.wav")));
		
			int read;
			byte[] buff = new byte[1024];
			while ((read = in.read(buff)) > 0)
			{
			    out.write(buff, 0, read);
			}
			out.flush();
			byte[] audioBytes = out.toByteArray();
			
			short [] shorts = new short[audioBytes.length];
			
			for(int i = 0; i < audioBytes.length; i++)
			{
				shorts[i] = (short) (256 * audioBytes[i]);
			}
			
			return shorts;
		
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedAudioFileException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}