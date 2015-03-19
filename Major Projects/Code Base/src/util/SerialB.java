package util;

import java.io.PrintStream;


/*
 * The specification of Objects that can be serialized the Bryce way.
 * 
 * Written by Bryce Summers on 7 - 29 - 2013.
 * 
 * This interface can be used to fully serialize structures without cyclic dependencies,
 * but will not be sufficient for data that references
 * itself and other objects recursively in a cycle of references.
 * 
 */

public interface SerialB
{
	// Serializes an object's data to a file.
	public abstract void serializeTo(PrintStream stream);
	
	// This name should be used during the serialization and deserialization
	// process for objects that could have an ordering of data in non predetermined types.
	
	// This is essentially an object's proffered type descriptor.
	public abstract String getSerialName();
}
