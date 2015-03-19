package Data_Structures.Structures.SpicketDropBucket;

import java.util.Iterator;

import Data_Structures.Structures.SingleLinkedList;


/* Spigot class, written by Bryce Summers on 1 - 12 - 2014.
 * 
 * A Component of my Spigot, drop, Bucket
 * information processing abstraction.  
 */

public class Bucket
{
	// -- Private data.
	
	// The amount of liquid that the bucket can still add.
	private int space_left;
	
	private SingleLinkedList<Drop> contents;
	
	public Bucket(int size)
	{
		space_left = size;
		contents = new SingleLinkedList<Drop>();
	}
	
	// Returns true iff the drop has been added to the bucket.
	public boolean add_drop(Drop d)
	{
		int val = d.getSize();
		
		if(val > space_left)
		{
			return false;
		}
		
		contents.add(d);
		space_left -= val;
		return true;
	}
	
	public Iterator<Drop> get_contents()
	{
		return contents.iterator();
	}
}
