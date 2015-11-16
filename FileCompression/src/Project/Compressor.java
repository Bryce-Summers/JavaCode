package Project;

public class Compressor
{

	BitStreamer stream;
	
	public Compressor(byte[] data)
	{
		stream = new BitStreamer(data);		
	}
	
	public int numBytes()
	{
		return stream.reset_stream() / BitStreamer.SIZE_OF_BYTE;
	}
	
	// Compresses sequences of 4 bits to sequences of 2 bits with a leading success bit.
	public void compress()
	{
		int size = stream.reset_stream();
		int bytes = size/BitStreamer.SIZE_OF_BYTE;
		
		for(int i = 0; i < bytes; i++)
		{
			int code = stream.streamBits(4);
			
			System.out.println("Code = " + code);
			
			switch(code)
			{
			case 0: stream.writeBits(1, 0, 0);break;// Success bit and then 2 code.
			case 1: stream.writeBits(1, 0, 1);break;
			case 2: stream.writeBits(1, 1, 0);break;
			case 3: stream.writeBits(1, 1, 1);break;
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
		
		System.out.println("");
	}
	
	public void expand()
	{
		
	}

	public int[] computeStatistics(int code_width)
	{
		
		int[] counts = new int[1 << code_width];
		
		int size = stream.reset_stream();
		int bytes = size/BitStreamer.SIZE_OF_BYTE;
		
		for(int i = 0; i < bytes; i++)
		{
			int code = stream.streamBits(code_width);
			counts[code]++;
		}
		
		return counts;		
	}

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
