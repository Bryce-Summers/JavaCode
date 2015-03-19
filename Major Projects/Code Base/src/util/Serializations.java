package util;

import java.io.PrintStream;
import java.util.Iterator;

import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.Dict;

public class Serializations
{
	// REQUIRES : A dict that maps strings to strings.
	// None of the strings should contain new line characters.
	// the given stream should be pointed to a fresh line.
	// ENSURES : Serializes the dict into a sequence of lines that can 
	// be deserialized by the cooresponding deserial_dict method within this class.
	public static void serial_dict(PrintStream stream, Dict<String> dict)
	{
		List<String> keys = dict.getKeys();
		
		stream.println(keys.size());
		
		for(String key : keys)
		{
			String val = dict.lookup(key);
			stream.println(key);
			stream.println(val);
		}
	}
	
	public static Dict<String> deserial_dict(Iterator<String> iter)
	{
		Dict<String> output = new Dict<String>();
		
		int len = new Integer(iter.next());
		
		for(int i = 0; i < len; i++)
		{
			String key = iter.next();
			String val = iter.next();
			output.insert(key, val);
		}
		
		return output;
	}

}
