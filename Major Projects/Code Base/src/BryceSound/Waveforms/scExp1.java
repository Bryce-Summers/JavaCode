package BryceSound.Waveforms;

import BryceSound.soundCalculator;

/*
 * Experimental sound 1.
 * 
 * Written by Bryce Summers on 8/23/2015.
 */

public class scExp1 extends soundCalculator
{

	scSine[] sines;
	int harmonics = 4;
	
	public scExp1(double frequency)
	{
		changeFrequency(frequency);
		
		sines = new scSine[harmonics];
		 
		for(int i = 1; i <= harmonics; i++)
		{
			sines[i - 1] = new scSine(frequency*i);
		}
		 
	}
	
	@Override
	public short getVal()
	{
		short output = 0;
		
		for(int i = 1; i <= harmonics; i++)
		{
			scSine sound = sines[i - 1];
			output += sound.sin(sound.getFrequency()+sin(10, x + 0, 1), x, volume/i/i);
			output += sound.sin(sound.getFrequency()+sin(10, x + 3000, 1), x + 3000, volume/i/i/2);
			output += sound.sin(sound.getFrequency()+sin(10, x + 6000, 1), x + 6000, volume/i/i/3);
			output += sound.sin(sound.getFrequency()+sin(10, x + 9000, 1), x + 6000, volume/i/i/4);
		}
		
		//System.out.println(output);
		return (short) output;
	}
}
