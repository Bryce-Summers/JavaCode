package util;

import java.io.File;


/*
 * The deserializing interface.
 * 
 * Written by Bryce Summers on 8 - 20 - 2013.
 * 
 * This class should be implemented by classes that know how to parse a given type of file.
 */

public interface deSerialB
{
	// Returns the type of file that the given serializer has been created for.
	public abstract String getExtension();
	
	// Deserializes a file of the proper extension type into the program.
	public abstract void serializeFrom(File file);
}
