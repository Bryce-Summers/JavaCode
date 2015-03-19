package BryceSound;

import java.util.Iterator;

import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;

/* Synthesizer class.
 * 
 * Returns successive sample values
 * 
 * This class provides the macroscopic framework for generating tones.
 */


public abstract class soundCalculator
{
	
	protected double volume = Short.MAX_VALUE / 100;
	
	// The input value.
	protected int sample_number = -1;
	
	protected int SAMPLING_RATE = 44100;
	
	protected int sample = -1;
	
	// The current frequency of this sound.
	protected double frequency = 440;// A.
	
	// X is used by the subclass to know where in the tone we are.
	protected double x = -1;
	
	public static UBA<Double> notes = new UBA<Double>();
	
	public soundCalculator()
	{
		fillNotes();
	}
	
	public synchronized void fillNotes()
	{
		if(!notes.isEmpty())
		{
			return;
		}
		
		notes.append(16.35, 17.32, 18.35, 19.45,
				20.60, 21.83, 23.12, 24.50,
				25.96, 27.50, 29.14, 30.87);
		
		for(int i = 12; i < 12*8; i++)
		{
			notes.add(notes.get(i - 12)*2);
		}
		
		for(int i = 0; i < 24; i++)
		{
			notes.pop_front();
		}
		
	}
	
	
	public short getWaveVal()
	{
		sample_number++;
		sample_number = sample_number % SAMPLING_RATE;
		
		if(sample_number % SAMPLING_RATE == 0)
		{
			sample++;
		}
		
		x ++;
		x = x % SAMPLING_RATE;
		
		return getVal();
	}
	
	protected abstract short getVal();

	public void setSampleRate(int rate)
	{
		SAMPLING_RATE = rate; 
	}
	
	
	// Sound Primitives.
	public double sin(double frequency, double x, double volume)
	{
		changeFrequency(frequency);
		return volume * Math.sin(x*Math.PI *2 * frequency/SAMPLING_RATE);
	}
	
	public double square(double frequency, double x, double volume)
	{
		double periodic_size = SAMPLING_RATE/frequency;
		double xx = x % periodic_size;
		
		return (xx < periodic_size/2 ? volume : -volume); 
	}
	
	public double sawtooth(double frequency, double x, double volume)
	{
		double periodic_size = SAMPLING_RATE/frequency;
		double xx = x % periodic_size;
		
		return -volume + 2*volume*xx/periodic_size; 
	}
	
	public double pulse(double frequency, double x, double volume)
	{
		double periodic_size = SAMPLING_RATE/frequency;
		return sawtooth(frequency, x, volume) - 
				sawtooth(frequency, x + periodic_size/2, volume);
	}

	protected void changeFrequency(double new_f)
	{
		x = x*frequency/new_f;
		frequency = new_f;
	}
	
}
