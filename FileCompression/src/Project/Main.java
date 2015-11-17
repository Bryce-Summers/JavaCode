package Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * Bit Code Compression Project.
 * 
 * Status: 11-16-2015.
 * The basic routines for the compression algorithm work correctly now.
 * 
 * TODO : I need to calculate some statistics and cultivate some theory for what a good progression of compression steps might be.
 * It might be a good idea to go through and try to flip as many clumps of bits to 1 or 0 as possible.
 * Q? Is is possible to forcibly bias the distribution of bit codes.
 */


public class Main
{

	public static void main(String[] args) throws IOException
	{
		new Main();
	}
	
	
	public Main() throws IOException
	{
		
		Path path = new File("Moon.jpg").toPath();
		System.out.println(path.toAbsolutePath());
		
		byte[] array = //{0, 0,0,0,0,0};
				 Files.readAllBytes(path);
		Compressor c = new Compressor(array);
		
		printStatistics(c, array.length);	
		
		System.out.println("Original String of Bits:");
		//c.readAllBits();
		
		// Compress the data n times. 
		final int numIterations = 50;
		for(int i = 0; i < numIterations; i++)
		{			
			c.compress();
						
			System.out.println("Compression Ratio after " + (i + 1) + " iterations = "
					+ array.length * 1.0 / c.numBytes()*1.0);
			
			// Prints out the current configuration of bits.
			//c.readAllBits();
			
		}
		
		System.out.println("");
		int size_compressed = c.numBytes();
		//printStatistics(c, size_compressed);
		
		
	}
	
	// Size is in bytes.
	// Prints out the frequency distribution for each of the 16 bit possible bit codes.
	void printStatistics(Compressor c, int size)
	{
		// Compute statistics for 4 bit codes.
		int[] counts = c.computeStatistics(4);
		
		int len = counts.length;
		System.out.println("Size in Bytes: " + size);
		for(int i = 0; i < len; i++)
		{
			// We normalize each count frequency to a percentage of all count frequencies,
			// then print in parts per 100.
			System.out.println("Code(" + i + ") = " + counts[i]*1.0 / (size * BitStreamer.SIZE_OF_BYTE/Compressor.SIZE_OF_CODE)*100);			
		}		
	}

}
