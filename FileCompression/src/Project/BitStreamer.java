package Project;

import Data_Structures.Structures.UBA;

/*
 * Written by Bryce Summers on 9/25/2015.
 * 
 * Allows for a sequence of bits to be read, then written.
 * 
 * Uses two arrays for each pass and alternates between the two.
 * 
 * Note: bytes are made up on 8 bits.
 */

public class BitStreamer
{

	final static byte SIZE_OF_BYTE = 8;
	
	private byte[] data;
	
	UBA<Byte> data1;
	UBA<Byte> data2;
	
	// Pointers to the data buffers.
	UBA<Byte> src;
	UBA<Byte> dest;
	
	// Indices for the current bit.
	int byte_index;
	int bit_index;
	
	int dest_bit_index;
	
	public BitStreamer(byte[] data_in)
	{				
		data1 = new UBA<Byte>();
				
		for(byte b : data_in)
		{
			data1.append(b);	
		}
		
		data2 = new UBA<Byte>();
		
		src  = data1;
		dest = data2;
		
		byte_index     = 0;
		bit_index      = 0;
		dest_bit_index = 0;
	}
	
	// Reset's the stream, flips the data array pointers,
	// Returns the size of the stream in bits.
	public int reset_stream()
	{
		byte_index     = 0;
		bit_index      = 0;
		dest_bit_index = 0;
		
		dest.clear();
		
		return src.size()*SIZE_OF_BYTE;		
	}
	
	// Converts the dest stream to the src stream.
	public void flipStream()
	{
		// We need to pad dest with bits so that all bits are left justified inside bytes.
		padDest();		
		UBA<Byte> temp = src;
		src  = dest;
		dest = temp;
	}
	

	public int streamBits(int numBits)
	{
		int output = 0;
		
		for(int i = 0; i < numBits; i++)
		{
			output = nextBit()
				   ? (output << 1) + 1
				   : (output << 1) + 0;					
		}
		
		return output;
	}
	
	public boolean nextBit()
	{
		// Out of bounds.
		if(byte_index > src.size())
		{
			return false;
		}
		

		//System.out.println("byte_index = " + byte_index + ", bit_index = " + bit_index);
		
		boolean result = (src.get(byte_index) >>> (SIZE_OF_BYTE - 1 - bit_index) & 1) != 0;
					
		// Move to the next bit.
		bit_index++;
		if(bit_index >= SIZE_OF_BYTE)
		{
			bit_index = 0;
			byte_index++;
		}
		
		return result;		
	}
	
	// Writes a sequence of bits, represented by integers 0 and 1.
	public void writeBits(int ... bits)
	{
		for(int i = 0; i < bits.length; i++)
		{
			writeBit(bits[i] != 0);			
		}
		
	}
	
	public void writeBit(boolean bit)
	{		
				
		byte b = (byte) (bit ? 1 : 0);
		
		//System.out.println("Bit : " + b);
		
		// Add a new byte every time we reach the beginning of a bit.
		if(dest_bit_index == 0)
		{			
			dest.add(b);
			dest_bit_index++;
			return;
		}
		
		byte last = dest.get(dest.size() - 1);
		last = (byte) ((last << 1) | b);
		
		//printByte(last);
		
		dest.set(dest.size() - 1, last);
		dest_bit_index = (dest_bit_index + 1) % SIZE_OF_BYTE;	
	}
	
	private void padDest() {
	
		while(dest_bit_index != 0)
		{
			writeBit(false);
		}
		
	}
	
	private void printByte(byte b)
	{
		for(int i = 0; i < 8; i++)
			System.out.print((b >>> (8 - 1 - i) & 1) != 0 ? "1" : "0");
		System.out.println("");
		//System.out.println(Integer.toBinaryString(b));
	}
	

	
}
