package Data_Structures.Structures.Fast;

/*
 * Genaric node class for fast data structures.
 * 
 * These nodes will be recycled and used by FastStructures.
 * 
 * Written by Bryce Summers on 5 - 12 - 2014.
 */

public class FastNode
{
	// Object type allows for FastNode to be implemented as a Public
	// class with compatible nodes for generic typed fast structures.
	Object   data;
	FastNode link;
}
