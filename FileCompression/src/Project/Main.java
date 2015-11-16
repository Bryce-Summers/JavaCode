package Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
		
		byte[] array = {0, 0};// = Files.readAllBytes(path);
		Compressor c = new Compressor(array);
		
		printStatistics(c, array.length);	
		
		
		// Compress the data n times. 
		final int numIterations = 5;
		for(int i = 0; i < numIterations; i++)
		{
			System.out.println("Compression Ratio after " + i + " iterations = "
								+ array.length * 1.0 / c.numBytes()*1.0);
			c.readAllBits();
			c.compress();
		}
		
		System.out.println("");
		int size_compressed = c.numBytes();
		printStatistics(c, size_compressed);
		
		
	}
	
	// Size is in bytes.
	void printStatistics(Compressor c, int size)
	{
		// Compute statistics for 4 bit codes.
		int[] counts = c.computeStatistics(4);
		
		int len = counts.length;
		System.out.println("Size in Bytes: " + size);
		for(int i = 0; i < len; i++)
		{
			System.out.println("Code(" + i + ") = " + counts[i]*1.0 / (size*2)*100);			
		}		
	}

}
