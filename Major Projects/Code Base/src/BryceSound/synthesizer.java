package BryceSound;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class synthesizer implements Runnable
{

	soundCalculator sc;

	
    final int SAMPLING_RATE = 1411;//44100;  // Audio sampling rate
    final int SAMPLE_SIZE   = 2;      // Audio sample size in bytes	
	
    short[] data;
        
	public synthesizer(soundCalculator sc)
	{
		this.sc = sc;
		sc.setSampleRate(SAMPLING_RATE);
		data = null;
		Thread T = new Thread(this);
		T.start();
	}
	
	public synthesizer(short[] sound_data)
	{
		data = sound_data;
		Thread T = new Thread(this);
		T.start();
	}
	
	
	public synchronized void run()
	{
	

      SourceDataLine line = null;
      double fFreq = 440;               // Frequency of sine wave in hz

      //Position through the sine wave as a percentage (i.e. 0 to 1 is 0 to 2*PI)
      double fCyclePosition = 0;        

      //Open up audio output, using 44100hz sampling rate, 16 bit samples, mono, and big 
      // endian byte ordering
      AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 1, true, true);
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

      if (!AudioSystem.isLineSupported(info)){
         System.out.println("Line matching " + info + " is not supported.");
         //throw new LineUnavailableException();
      }

      try
      {
    	  line = (SourceDataLine)AudioSystem.getLine(info);
    	  line.open(format);  
	      line.start();
      } catch (LineUnavailableException e)
      {
    	  // TODO Auto-generated catch block
    	  e.printStackTrace();
      }


      // Make our buffer size match audio system's buffer
      ByteBuffer cBuf = ByteBuffer.allocate(line.getBufferSize());   

      int ctSamplesTotal = SAMPLING_RATE*5;         // Output for roughly 5 seconds

      int sample_number = 0;

      //On each pass main loop fills the available free space in the audio buffer
      //Main loop creates audio samples for sine wave, runs until we tell the thread to exit
      //Each sample is spaced 1/SAMPLING_RATE apart in time
      
      // Produce continuous sound forever.
      while (true || ctSamplesTotal > 0)
      {
    	  double fCycleInc = fFreq/SAMPLING_RATE;  // Fraction of cycle between samples

    	  cBuf.clear();                            // Discard samples from previous pass
	         
    	  // Figure out how many samples we can add
    	  int ctSamplesThisPass = line.available()/SAMPLE_SIZE;
	      for (int i=0; i < ctSamplesThisPass; i++)
	      {
	    	  if(data == null)
	    	  {
	    		  cBuf.putShort(sc.getWaveVal());
	    	  }
	    	  else if(sample_number < data.length)
	    	  {
	    		  cBuf.putShort(data[sample_number]);
	    	  }

	    	  
	    	  fCyclePosition += fCycleInc;
	    	  if (fCyclePosition > 1)
	    		  fCyclePosition -= 1;
	            
	    	  sample_number++;
	      }

	      //Write sine samples to the line buffer.  If the audio buffer is full, this will 
	      // block until there is room (we never write more samples than buffer will hold)
	      line.write(cBuf.array(), 0, cBuf.position());            
	      ctSamplesTotal -= ctSamplesThisPass;     // Update total number of samples written 

	      //Wait until the buffer is at least half empty  before we add more
	      while (line.getBufferSize()/2 < line.available())
			try
	      	{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }


	      //Done playing the whole waveform, now wait until the queued samples finish 
	      //playing, then clean up and exit.
	      line.drain();                                 
	      line.close();
	   }
	}