package BryceSound.Waveforms;

import BryceSound.soundCalculator;

public class scSine extends soundCalculator
{

	public scSine(double frequency)
	{
		changeFrequency(frequency);
	}
	
	@Override
	public short getVal()
	{
		
		double output = sin(frequency, x, volume);
			
		
		return (short) output;

	}
}
