package BryceSound;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import BryceSound.Waveforms.scSine;
import Data_Structures.Structures.UBA;
import Data_Structures.Structures.InDevelopment.PriorityQueues.ArrayHeapPQ;
import Data_Structures.Structures.InDevelopment.PriorityQueues.PQNode;
import Game_Engine.Engine.Objs.Obj_Container;
import Game_Engine.Engine.Objs.actionLogging.Command;

public class sequencer extends soundCalculator
{

	int time = -1;
	
	private ArrayHeapPQ<soundCalculator> PQ = new ArrayHeapPQ<soundCalculator>();
	
	
	int index = 0;
	UBA<Integer> starts    = new UBA<Integer>();
	UBA<Integer> durations = new UBA<Integer>();
	UBA<Double> pitches   = new UBA<Double>();
	
	public sequencer()
	{
		generate_random_rythm();
	}
	
	public void generate_random_rythm()
	{
		starts.clear();
		durations.clear();
		pitches.clear();
		
		index = 0;
		time  = -1;

		int unit_duration = 10000;
		
		for(int i = 0; i < 4; i++)
		{
			starts.add(i*unit_duration*2);
			durations.add(unit_duration);
			pitches.add(440.0);
		}
		
		
		int t = 16;
		
		int final_end = t + 16;
		
		while(t < final_end)
		{
			int begin = t;
			
			int duration = (int)(4*Math.random()) + 1;

			System.out.print(duration + " ");
			
			int end = Math.min(begin + duration, final_end);
			
			starts.add(begin*unit_duration);
			durations.add((end - begin)*unit_duration - unit_duration/2);
			
			int pitch = (int)(4*Math.random());
			pitches.add(notes.get(20 + 2*pitch));
			
			t += duration;
		}
		
		System.out.println();
	}
	
	// Returns the composite sound.
	@Override
	protected short getVal()
	{
		time++;
		
		// Remove all sounds that have expired.
		while(!PQ.isEmpty() && PQ.peekMinPriority() <= time)
		{
			PQ.delMin();
		}
		
		
		// Add all of the currently running sounds together.
		short output = 0;
		
		for(PQNode<soundCalculator> n : PQ)
		{
			soundCalculator sc = n.getElem();
			output += sc.getWaveVal();
		}
		
		if(index < starts.size())
		{
			if(time == starts.get(index))
			{
				addSample(new scSine(pitches.get(index)), durations.get(index));
				index++;
			}
		}
		
		return output;
	}
	
	
	// -- Interface functions for forming sequenced tracks.
	public void addSample(soundCalculator sc, int duration)
	{
		PQ.add(sc, time + duration);
	}

	public void playAgain()
	{
		index = 0;
		time = -1;
	}
}
