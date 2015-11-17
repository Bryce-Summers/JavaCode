package Project;

/*
 * Compressor compress and decompress arrays of bits.
 * 
 * They map groups of 4 bits to groups of 2 bits with associated success / failure flags.
 */

public class Compressor
{

	BitStreamer stream;
	
	
	// NOTE: Bytes are 8 bits long.
	//       Code are 4 bits long.
	final static int SIZE_OF_CODE = 4;

	public Compressor(byte[] data)
	{
		stream = new BitStreamer(data);		
	}
	
	// Returns the number of bytes in the compressor's current stream.
	public int numBytes()
	{
		return stream.reset_stream() / BitStreamer.SIZE_OF_BYTE;
	}
	
	// Returns the number of codes in the compressor's current stream.
	public int numCodes()
	{
		return stream.reset_stream() / SIZE_OF_CODE;
	}
	
	// Compresses sequences of 4 bits to sequences of 2 bits with a leading success bit.
	public void compress()
	{
		int size = stream.reset_stream();
		int code_num = size/SIZE_OF_CODE;
		
		for(int i = 0; i < code_num; i++)
		{
			int code = stream.streamBits(4);
			
			//System.out.println("Code = " + code);
			
			switch(code)
			{
			case 0: stream.writeBits(1, 0, 0);break;// Success bit and then 2 code.
			case 1: stream.writeBits(1, 0, 1);break;
			case 2: stream.writeBits(1, 1, 0);break;
			case 3: stream.writeBits(1, 1, 1);break;

			/*
			case 4: stream.writeBits(1, 1, 1);break;
			case 5: stream.writeBits(1, 1, 1);break;
			case 6: stream.writeBits(1, 1, 1);break;
			case 7: stream.writeBits(1, 1, 1);break;
			case 8: stream.writeBits(1, 1, 1);break;
			case 9: stream.writeBits(1, 1, 1);break;
			case 10: stream.writeBits(1, 1, 1);break;
			case 11: stream.writeBits(1, 1, 1);break;
			case 12: stream.writeBits(1, 1, 1);break;
			case 13: stream.writeBits(1, 1, 1);break;
			case 14: stream.writeBits(1, 1, 1);break;
			case 15: stream.writeBits(1, 1, 1);break;
			*/

			default:
				stream.writeBit(false); // Failure bit.
				for(int i2 = 0; i2 < 4; i2++)
				{
					stream.writeBit(((code >> i2) & 1) != 0);
				}

				break;
			}
			
		}
		
		stream.flipStream();
	}
	
	// Reverses a compression process.
	public void expand()
	{
		
	}

	/**
	 * 
	 * @param code_width - the size of each major input code.
	 * 						For example, the compressor might be converting 0000 -> 100,
	 * 						which has a code width of 4.
	 * 
	 * This function divides this Compressor's sequence of bits into parts of the given code width.
	 * It then counts how many of each type of code are present.
	 * The result is a histogram of codes. 
	 * 
	 * @return
	 */
	public int[] computeStatistics(int code_width)
	{		
		int[] counts = new int[1 << code_width];
		
		// Size in bits.
		int size = stream.reset_stream();
		int code_num = size/SIZE_OF_CODE;
		
		for(int i = 0; i < code_num; i++)
		{
			int code = stream.streamBits(code_width);
			counts[code]++;
		}
		
		return counts;
	}

	// Prints out all of the bits in the compressor's stream right now.
	public void readAllBits()
	{
		System.out.print("Bits in stream : ");
		int bits = stream.reset_stream();
		for(int i = 0; i < bits; i++)
		{
			System.out.print(stream.nextBit() ? "1" : "0");
		}
		
		System.out.println("");
	}
	
}
