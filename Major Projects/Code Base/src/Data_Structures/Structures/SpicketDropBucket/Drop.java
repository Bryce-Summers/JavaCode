package Data_Structures.Structures.SpicketDropBucket;

/* Spigot class, written by Bryce Summers on 1 - 12 - 2014.
 * 
 * A Component of my Spigot, drop, Bucket
 * information processing abstraction.
 * 
 *  Drops are objects that can characterize themselves by their Size.
 *  Buckets can only hold so much mass, so drops are assigned to 
 *  buckets based on maximizing the amount each bucket is filled. 
 */

public interface Drop
{
	public int getSize();
}
