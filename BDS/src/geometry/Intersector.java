package geometry;

import java.util.Comparator;

import Data_Structures.ADTs.Heap;
import Data_Structures.Structures.UBA;
import Data_Structures.Structures.InDevelopment.Heaps.ArrayHeap;

/*

Intersector

Written by Bryce Summers on 1 - 2 - 2017.

features: Efficient Line Segment Intersection.

*/

/*
public class Intersector
{

	class BDS.Intersector

	    constructor: () ->

	    /*
	    Calls the Line.intersect method on all intersecting lines.
	    Does not treat lines that intersect at common points as intersecting.
	    # takes arrays of BDS.Line objects.
	    BDS.Line[] -> () [intersection sideeffects]
	    */
		/*
	    public void intersectLineSegments(UBA<Line> lines)
	    {

	        # Stores all of the line enter and exit events.
	        event_queue = new BDS.Intersector.EventPQ(lines)

	        # Stores all of the lines currently spanning the sweep line.
	        # We can use a heap for intersection tests across all of the lines and easy deletion of the exiting events.
	        # or we can use a Binary search tree where we can furthur bound the possible intersections in a second dimension.
	        # I am currently using a simpler heap approach, which is easier to implement.
	        # We will assign tuples based on exit locations.
	        sweepSet   = new BDS.Intersector.LineSweepSet()

	        len = event_queue.size()

	        #while(!event_queue.isEmpty())
	        # Process every entrance and exit event.
	        for i in [0...len] by 1#(int i = 0; i < len; i++)

	            event = event_queue.delMin()

	            switch (event.type)
	            
	                when BDS.Intersector.Event.ENTER
	                    line = event.line
	                    sweepSet.intersect_with_line(line)
	                    sweepSet.add(event.twin)# Add the cooresponding exit event.
	                    continue

	                when BDS.Intersector.Event.EXIT
	                    sweepSet.remove(event)
	                    continue
	        return
	    }

	    /*
	    Returns true iff there is at least one valid intersection detected in the input set of polylines.
	    Does not treat lines that intersect at common points as intersecting.
	    
	    non_intersection_indices demarcate ranges of indices that should not be intersected against each other.
	    Assumes that the indices are in rted order.
	    */
/*
	    boolean detect_intersection_line_segments_partitioned(lines, partition_indices)
	    {

	        // Stores all of the line enter and exit events.
	        event_queue = new BDS.Intersector.EventPQ(lines);

	        // Stores the exit events cooresponding to lines currently spanning the sweep line.
	        sweepSet   = new BDS.Intersector.LineSweepSet()

	        len = event_queue.size()

	        //while(!event_queue.isEmpty())
	        // Process every entrance and exit event.
	        for i in [0...len] by 1#(int i = 0; i < len; i++)

	            event = event_queue.delMin()

	            switch (event.type)
	            
	                when BDS.Intersector.Event.ENTER
	                    line = event.line

	                    # Return true as there is a valid intersection that is detected.
	                    if sweepSet.detect_intersection_with_line_partitioned(line, partition_indices)
	                        return true

	                    sweepSet.add(event.twin)
	                    continue

	                when BDS.Intersector.Event.EXIT
	                    sweepSet.remove(event)
	                    continue

	        return false
	    }

	    /*
	    Slower, but more robust version of intersect.
	    Naive N^2 Intersection Algorithm.
	    */
/*
	    void intersect_brute_force (lines)
	    {
	        numLines = lines.length;

	        for a in [0 ...numLines] by 1#(int a = 0; a < numLines; a++)
	            for b in [a + 1 ...numLines] by 1#(int b = a + 1; b < numLines; b++)

	                lines[a].intersect(lines[b])

	        return;
	    }


	/*
	Event Priority Queue methods.
	 */
    // FIXME: I will probably want to make these guys private classes.
/*
	private class EventPQ
	{

	    public EventPQ(UBA<Line> lines)
	    {
	        events = new UBA<Line>();
	        len = lines.length;

	        for (int i = 0; i < len; i++)
	        {
	        
	            line = lines[i];

	            // Events.
	            enter = new BDS.Intersector.Event();
	            exit  = new BDS.Intersector.Event();

	            // Points.
	            p1 = line.p1;
	            p2 = line.p2;

	            // Sort into enter and exit events.
	            // Enter at least x coordinate.
	            // Exit at greatest x coordinate.
	            // We are assuming that there are no vertical lines.
	            // Also sort with lower y coordinates entering to higher y coordinates.
	            if(p1.x < p2.x || (p1.x == p2.x && p1.y < p2.y))
	            {
	                this._populateEvent(enter, exit, p1, p2, line, i);
	            }
	            else
	            {
	                this._populateEvent(enter, exit, p2, p1, line, i);
	            }
	            events.push(enter);
	            events.push(exit);
	        }

	        this.PQ = new BDS.Heap(events, BDS.Intersector.Event_Comparator);

	        //cout << "ENTER EVENT Generated : " << enter.x << ", " << enter.y << endl;
	        //cout << "EXIT EVENT Generated : "  << exit.x  << ", " << exit.y  << endl;
	    }


	    // event, event, Point, Point, Line -> void (all BDS)
	    void _populateEvent(Event enter, Event exit, Point p1, Point p2, Line line, int id)
	    {
	    
	        enter.type = Event.ENTER;
	        exit.type  = Event.EXIT;

	        // Enter events get the entrance location.
	        enter.x = p1.x;
	        enter.y = p1.y;

	        // Exit events get the exit location.
	        exit.x = p2.x;
	        exit.y = p2.y;

	        enter.line = line;
	        exit.line  = line;

	        enter.id = id;
	        exit.id  = id;

	        enter.twin = exit;
	        exit.twin  = enter;
	    }
	    
	    Event delMin()
	    {
	        return this.PQ.dequeue();
	    }

	    // () -> bool
	    boolean isEmpty()
	    {	    
	        return PQ.isEmpty();
	    }

	    int size()
	    {
	        return this.PQ.size();
	    }
	}

	// Returns true if e1 <= e2 (Occurs before e2)
	private class Event_Comparator implements Comparator<Event>
	{

	}

	    

	// Represents the set up tuples currently crossing a sweepline. Intersection routines are handled within this class.
	private class LineSweepSet
	{
		Heap heap;
	    public LineSweepSet constructor()
	    {
	        this.heap = new ArrayHeap<Event>([]);
	    }

	    void add(Event event)
	    {
	        this.heap.add(event);
	    }

	    Event remove(Event event_to_be_removed)
	    {
	        Event my_event = this.heap.dequeue();

	        if(my_event != event_to_be_removed)
	        {
	            err = new Error("ERROR: line_tuple exit ordering is messed up!");
	            throw err;
	        }

	        return my_event;
	    }

	    // Calls BDS.Line.intersect() on every possible line in this set that could intersect the input_line.
	    void intersect_with_line(input_line)
	    {
	        
	        int len = this.heap.size();
	        for(int i = 0; i < len; i++)
	        {
	            Event event = heap.getElem(i);
	            Line line_crossing_sweep = event.line;
	            input_line.intersect(line_crossing_sweep);
	        }
	    }

	    // Returns truee if their is an intersection between two lines from seperate point lists.
	    // Partition is done based on pointer equality for the point's lists.
	    boolean detect_intersection_with_line_partitioned(Line input_line)
	    {
	        int len = this.heap.size();
	        for(int i = 0; i < len; i++)
	        {
	            Event event = this.heap.getElem(i);
	            Line line_crossing_sweep = event.line;
	            
	            // Same Partition, ignore this pair.
	            if(line_crossing_sweep.points == input_line.points)
	            {
	                continue;
	            }

	            // Return detected intersections from lines in seperate partitions.
	            if(input_line.detect_intersection(line_crossing_sweep))
	            {
	                return true;
	            }
	        }

	        return false;
	    }

	    boolean detect_intersection_with_line(Line input_line)
	    {

	        int len = heap.size();
	        for(int i = 0; i < len; i++)
	        {
	            Event event = this.heap.getElem(i);
	            line_crossing_sweep = event.line;

	            if(input_line.detect_intersection(line_crossing_sweep))
	            {
	                return true;
	            }
	        }

	        return false;
	    }
	}

	// These objects represent events along the sweep line.
	private class Event extends Comparable<Event>
	{

	    public static final int ENTER = 0;
	    public static final int EXIT  = 1;
	    public static final int UNDEFINED;

	    int type;
	    double x, y;
	    int id;
	    Event twin;
	    Line line;
	    
	    public Event()
	    {
	    
	        type = Event.UNDEFINED;

	        x = 0;
	        y = 0;

	        id = -1;
	        twin = null; // Counterpart enter or exit event.
	        line = null;
	    }
	    
		public boolean compare(Event e1, Event e2)
		{
			// Note: tuples are only used for equality and id's, not for position data.

		    // Equal.
		    if(e1.line == e2.line &&
		       e1.type == e2.type)
		        return true;

		    // Equal, but opposite events.
		    // Put the enter event first.
		    if(e1.line == e2.line)
		    {
		        return e1.type == Event.ENTER;
		    }

		    // Differentiate by x location, then y location.
		    if (e1.x < e2.x)
			{
				return true;
			}
			if (e1.x > e2.x)
			{
				return false;
			}
			if (e1.y < e2.y)
			{
				return true;
			}
			if (e1.y > e2.y)
			{
				return false;
			}

		    // Events occur at the same location.

		    // Exit events before entrance events at identical locations.
		    if((e1.type == Event.EXIT) && (e2.type == Event.ENTER))
		    {		    
		        return true;
		    }

		    if((e1.type == ENTER) && (e2.type ==  Event.EXIT))
		    {
		        return false;
		    }

		    // If we have to enter or exit events at the same location, then we differentiate by arbitrary id.
		    if((e1.id) <= (e2.id))
		    {
		        return true;
		    }

		    return false;
		}
	}
}*/