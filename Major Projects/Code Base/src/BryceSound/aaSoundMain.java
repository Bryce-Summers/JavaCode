package BryceSound;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import BryceSound.WavFileHelper.WavAudioPlayer;
import BryceSound.WavFileHelper.WavAudioRecorder;
import BryceSound.WavFileHelper.WavData;
import BryceSound.WavFileHelper.WaveSection;
import Game_Engine.Engine.engine.Game_output;


/*
 * This is a main routine that plays my waveforms.
 */


public class aaSoundMain
{
	
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args)
	{
		
		// Generate new Audio from Mathematics.
		//synthesize();
		
		proccess_input();
		System.out.println("DONE, love Bryce");
	}
	
	public static void proccess_input()
	{
	       final String NEWLINE = "\n";
	       //String inputFile = "Toad_Clip.wav";
	       String inputFile = "Toad_Clip_sans_noise.wav";
	        

	        System.out.println("START");
	        try {
	            WavData wavInputData  = new WavData();
	            WavData wavRecordData = new WavData();

	            System.out.println(NEWLINE+"CONVERT WAV FILE TO BYTE ARRAY");
	            wavInputData.read(inputFile);

	            proccessAndPlayWavData(wavInputData);

	            WavAudioPlayer player = new WavFileHelper.WavAudioPlayer(wavInputData);
	            player.playAudio();

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        System.out.println("FINISH");
	}
	
	public static void proccessAndPlayWavData(WavData waveData)
	{
		byte[] data = waveData.getBytes(WaveSection.DATA);
		
		// Do all of the processing of the input data.
		proccessData(data);
		
        
		System.out.println("DATA LENGTH : " + data.length);
        
        // Create an audio input stream from byte array
        AudioFormat audioFormat = waveData.createAudioFormat();
        InputStream byteArrayInputStream  = new ByteArrayInputStream(data);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream,
                audioFormat, data.length / audioFormat.getFrameSize());

        // Write audio input stream to speaker source data line
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
                audioFormat);
        SourceDataLine sourceDataLine;
		try {
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        // Loop through input stream to write to source data line
        byte[] tempBuffer = new byte[10000];
        int cnt;
        while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
        	       	        	
            sourceDataLine.write(tempBuffer, 0, cnt);
        }

        // Cleanup
        sourceDataLine.drain();
        sourceDataLine.close();
        byteArrayInputStream.close();
        
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void proccessData(byte[] data) 
	{
		
		// Initialize a Frequency Spectrum Processor that will be used to process all of the frames.
		SpectralProccessor sound_proc = new SpectralProccessor();
       
        // The size of the windows.
        int size = 512;
        int size_h = size/2;
        
        // We process half phase offset frames of data over the entire input array.]
        // We then combine their results back at the end.
        
        byte[] data_1 = data;
        byte[] data_2 = Arrays.copyOf(data, data.length);
        
        // Process the first window.
        sound_proc.proccess_bryce1(data_1, 0, size);
        for(int i = size; i < data.length - size; i += size)
        {
        	// Process the interleaved pairs of windows in the first and second arrays.
        	sound_proc.proccess_bryce1(data_1, i, i + size);
        	sound_proc.proccess_bryce1(data_2, i - size_h, i + size_h);
        }
        
        // Add the synchopated processed frames back to the original data. 
        for(int i = size_h; i < data.length - size_h; i++)
        {
        	data[i] += data_2[i];
        }
	}

	public static void synthesize()
	{
		// Render the GUI images.
		Game_Engine.GUI.SpriteLoader.render1();
			
		// Create an output object for the game.
		Game_output out = new Game_output("Bryce Organizer!", dim);
		
		// Start the game.
		new room_main(out);
	}	
}