package geometry;

import Data_Structures.Structures.UBA;

/*
Line with associated intersection data.
Written by Bryce Summers on 1 - 2 - 2017.
Adapted from javascript to JAVA on 12.08.2017.

This class is designed for intersection techniques and line segment splitting,
rather than for simply representing a line segment.
Please use Polylines for the geometric representation and drawing of lines.
*/

public class Line
{

		int p1_index, p2_index;
		UBA<Point> points;
		Point p1;
		Point p2;
		
		Point offset;
		
		UBA<Double>  split_points_per;
		UBA<Integer> split_points_indices;
		
		
	    public Line(int start_point_index, int end_point_index, UBA<Point> point_array)
	    {

	        // Point indices.
	        this.p1_index = start_point_index;
	        this.p2_index = end_point_index;

	        // The canonical array of points.
	        // This may also be used externally for partitioning sets of lines for intersection detection.
	        this.points = point_array;

	        this.p1 = this.points.get(this.p1_index);
	        this.p2 = this.points.get(this.p2_index);

	        this.offset = p2.sub(p1);

	        // Float[]
	        // Collection of doubles representing the percentage a point is between p1 and p2.
	        split_points_per = new UBA<Double>();

	        // int[]
	        // The indices of the points.
	        split_points_indices = new UBA<Integer>();
	    }


	    /*
	    intersects the given other_line with this line.
	    Adds a split point if they do intersect.
	    Any created split points are added to the referenced global collection of points.
	    # Line -> bool
	    */
	    boolean intersect(Line other)
	    {
	        // Already Previously Connected.
	        // Connected at a joint in the input polyline.
	        if(this.p1_index == other.p1_index || this.p1_index == other.p2_index ||
	           this.p2_index == other.p1_index || this.p2_index == other.p2_index)
	        {
	            return false;
	        }

	        // No intersection.
	        if(!this.detect_intersection(other))
	        {
	            return false;
	        }
	        
	        // Yes intersection.
	        this._report_intersection(other);
	        return true;
	    }

	    /*
	    Returns a signed floating point number indicating which direction the given point is relative to this line.
	    # Point -> float.
	    */
	    double line_side_test(Point c)
	    {
	        return (this.p2.x - this.p1.x)*(c.y - this.p1.y) - (this.p2.y - this.p1.y)*(c.x - this.p1.x);
	    }

	    /*
	    Appends all of the split set of lines in order to the output vector.
	    Adds itself if it does not contain any split lines.
	    Line pts are oriented along the polyline, such that p1 comes before p2 in the polyline + intersection point ordering.
	    Line[] -> void
	    */
	    void getSplitLines(UBA<Line> lines_out)
	    {

	        // Number of split points.
	        int len = this.split_points_per.size();

	        // Not split points.
	        if(len == 0)
	        {
	            // Saves work.
	            lines_out.push(this);
	            return;
	        }
	        
	        // First sort points.
	        this._sort_split_points();

	        // Make sure the last line is pushed.
	        // This ensures that that initial line will be pushed if this line has no intersections.
	        this.split_points_indices.push(this.p2_index);

	        // Append all of the line's segments to the inputted array.
	        int last_indice = this.split_points_indices.get(0);

	        // The initial line.
	        lines_out.push(new Line(p1_index, last_indice, points));

	        for(int i = 1; i < len; i++)
	        {
	            int next_indice = this.split_points_indices.get(i);
	            lines_out.push(new Line(last_indice, next_indice, this.points));
	            last_indice = next_indice;
	        }

	        // The last line.
	        lines_out.push(new Line(last_indice, this.p2_index, this.points));

	        // Done.
	        return;
	    }

	    /*
	    This function should only be called after a call to intersect has returned true.
	    Returns the last intersection point.
	    this is only guranteed to be valid immediatly after the true return from the intersect function.
	    void -> Point.
	    */
	    Point getLatestIntersectionPoint()
	    {	    
	        return this.points.get(this.points.size() - 1);
	    }
	    
	    /*
	    Internally sorts the split points from the start to the end of this line.
	    */
	    void _sort_split_points()
	    {
	        int len = this.split_points_per.size();

	        // Insertion sort.
	        for(int i = 1; i < len; i++)
	        {
	            for(int i2 = i - 1; i2 >= 0; i2--)
	            {
	                int i1 = i2 + 1;

	                if(this.split_points_per.get(i2) <= this.split_points_per.get(i1))
	                {
	                   break;
	                }

	                // -- Swap at indices i2 and i2 + 1.
	                // Keep the percentage measurements consistent with the indices.
	                double temp_f = this.split_points_per.get(i2);
	                this.split_points_per.set(i2, this.split_points_per.get(i1));
	                this.split_points_per.set(i1, temp_f);

	                int temp_i = this.split_points_indices.get(i2);
	                split_points_indices.set(i2, this.split_points_indices.get(i1));
	                split_points_indices.set(i1, temp_i);
	            }
	        }

	        return;
	    }

	    /*
	    Returns true iff this line segment intersects with the other line segment.
	    Doesn't do any degeneracy checking.
	    Line -> bool.
	    */
	    boolean detect_intersection(Line other)
	    {
	        
	        // float test results.
	        double a1 = this.line_side_test(other.p1);
	        double a2 = this.line_side_test(other.p2);

	        double b1 = other.line_side_test(this.p1);
	        double b2 = other.line_side_test(this.p2);

	        /*
	        The product of two point based line side tests will be negative iff
	        the points are not on strictly opposite sides of the line.
	        If the product is 0, then at least one of the points is on the line not containing the points.
	        */
	        /*
	        epsilon = .001
	        a_on = (Math.abs(a1) < epsilon or Math.abs(a2) < epsilon)
	        b_on = (Math.abs(b1) < epsilon or Math.abs(b2) < epsilon)
	        */

	        boolean a_opposites = a1*a2 <= 0;
	        boolean b_opposites = b1*b2 <= 0;

	        // Avoid collinear intersections.
	        if((a_opposites && b_opposites) && (a1 != 0 || a2 != 0))
	        {
	        	return true;
	        }
	        
	        return false;
	    }

	    /*
	    Line -> void.
	    */
	    void _report_intersection(Line other)
	    {

	        // Find the intersection point.

	        /*
	        u = ((bs.y - as.y) * bd.x - (bs.x - as.x) * bd.y) / (bd.x * ad.y - bd.y * ad.x)
	        v = ((bs.y - as.y) * ad.x - (bs.x - as.x) * ad.y) / (bd.x * ad.y - bd.y * ad.x)
	        Factoring out the common terms, this comes to:
	        

	        dx = bs.x - as.x;
	        dy = bs.y - as.y;
	        det = bd.x * ad.y - bd.y * ad.x
	        u = (dy * bd.x - dx * bd.y) / det
	        v = (dy * ad.x - dx * ad.y) / det
	        */

	        // Extract the relevant points.
	        Point as = this.p1;
	        Point bs = other.p1;
	        Point ad = this.offset;
	        Point bd = other.offset;

	        // floats.
	        double dx = bs.x - as.x;
	        double dy = bs.y - as.y;
	        double det = bd.x * ad.y - bd.y * ad.x;
	        double u = (dy * bd.x - dx * bd.y) / det;
	        double v = (dy * ad.x - dx * ad.y) / det;

	        // if det == 0, then the two lines are collinear.


	        // The intersection is at time coordinates u and v.
	        // Note: Time is relative to the offsets, so p1 = time 0 and p2 is time 1.

	        // u is the time coordinate for this line.
	        this.split_points_per.push(u);

	        // v is the time coordinate for the other line.
	        other.split_points_per.push(v);

	        Point intersection_point = as.add(ad.multScalar(u));

	        // Get the next index that will be used to store the newly created point.
	        int index = this.points.size();
	        this.points.push(intersection_point);

	        this.split_points_indices.push(index);
	        other.split_points_indices.push(index);
	    }

	    // Clears away all intersection data.
	    void clearIntersections()
	    {
	        this.split_points_per.clear();
	        this.split_points_indices.clear();
	    }

	    // Returns the normal pointing in the direction of the given point.
	    Point getNormal(Point pt)
	    {

	        // Rotate the direction to get the normal direction.
	        // Naturally it is still normalized.
	        Point normal = this.offset.normalize();
	        
	        double temp = normal.x;
	        normal.x = -normal.y;
	        normal.y = temp;

	        Point dir = pt.sub(this.p1);

	        // Flip it if it is going the wrong way.
	        if(normal.dot(dir) < 0)
	        {
	            return normal.multScalar(-1);
	        }

	        return normal;
	    }
	    
	    // Returns the normal oriented to the right.
	    Point getRightNormal()
	    {
	    	// Rotate the direction to get the normal direction.
	        // Naturally it is still normalized.
	        Point normal = this.offset.normalize();
	        
	        double temp = normal.x;
	        normal.x = -normal.y;
	        normal.y = temp;
	        
	        return normal;
	    }


		public double distanceToPt(Point pt)
		{
			Point toPt = pt.sub(p1);
			Point toPt2 = pt.sub(p2);
			double perp = toPt.dot(getRightNormal());
			double par = toPt.dot(this.offset.normalize());
			
			if(0 < par && par < this.offset.norm())
			{
				return perp;
			}
			
			return Math.min(toPt.norm(), toPt2.norm());
		}
}