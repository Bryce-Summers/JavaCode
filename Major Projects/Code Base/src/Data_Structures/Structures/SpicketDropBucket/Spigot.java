 package Data_Structures.Structures.SpicketDropBucket;

import java.util.Iterator;

import Data_Structures.Structures.SingleLinkedList;

/* 
 * Spigot class, written by Bryce Summers on 1 - 12 - 2014.
 * 
 * A Component of my Spigot, drop, Bucket
 * information processing abstraction.
 */

public class Spigot
{
	// -- Private data.
	private SingleLinkedList<Drop> liquid = new SingleLinkedList<Drop>();
	
	private int bucket_size;
	
	// -- Constructor.
	
	public Spigot(int bucket_size)
	{
		this.bucket_size = bucket_size; 
	}
	
	// -- Public interface methods.
	public void populate(Iterator<? extends Drop> iter)
	{
		liquid.clear();
		append_liquid(iter);
	}
	
	public void append(Iterator<? extends Drop> iter)
	{
		append_liquid(iter);
	}

	/* 
	 * Pours the buckets in the order that they were given to the Spigot,
	 * filling each bucket up as much as possible and then moving on.
	 */
	public Iterator<Bucket> poor_buckets_ordered()
	{
		SingleLinkedList<Bucket> output = new SingleLinkedList<Bucket>();
		Iterator<Drop> drops = liquid.iterator();
		
		Bucket current = new Bucket(bucket_size);
		
		while(drops.hasNext())
		{
			Drop d = drops.next();
			
			if(d.getSize() > bucket_size)
			{
				throw new Error("Drop is bigger than the bucket.");
			}
			
			// Drop fits...
			if(current.add_drop(d))
			{
				continue;
			}
			
			// Drop does not fit.
			output.add(current);
			current = new Bucket(bucket_size);
			current.add_drop(d);// ASSERT(true).
		}
		
		// Add the final bucket to the output.
		output.add(current);
		
		return output.iterator();
	}
	
	// Ensures the minimum number of buckets are poured to contain all of the liquid.
	// Perhaps approach this problem as a napsack problem.
	public Iterator<Bucket> poor_buckets_optimal()
	{
		throw new Error("Implement me Please!");
	}
	
	// -- Private Helper functions.
	private void append_liquid(Iterator<? extends Drop> iter)
	{
		while(iter.hasNext())
		{
			Drop d = iter.next();
			liquid.add(d);
		}	
	}
	
}
