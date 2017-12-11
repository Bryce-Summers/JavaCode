package geometry;

import Data_Structures.Structures.UBA;
import Data_Structures.Structures.InDevelopment.Heaps.ArrayHeap;
import structures.BVH2D;

/*
Polyline class (also represents polygons)

Written by Bryce Summers on 1 - 4 - 2017.
Adapted to Javascript on 12.06.2017.

Note: Closed Polylines are polygons...
 - So we will put all of our polygon code into this class.

Note: this class assumes that it contains at least 1 point for collision tests.

Note: Polyline <--> polyline intersection tests assume that the polyline is not self intersecting.

FIXME: Return proper point in polyline tests for complemented filled polylines.

*/

public class Polyline
{

	private boolean _isClosed;
	private boolean _isFilled;
	
	private UBA<Point> _points;
	private Box _boundingbox;
	private Object _obj;

	private BVH2D _lineBVH;
	
    // FIXME: Maybe I should use BDS.Point_info's instead.
    // BDS.Point[], bool
    public Polyline(boolean _isClosed, UBA<Point> points_in, boolean _isFilled)
    {
    	this._isClosed = _isClosed;// default false;
        

        // Stores whether this polyline is really a polygon and contains its inner area.
        this._isFilled = _isFilled; // default @_isFilled = @_isClosed

        this._points = points_in.clone(); // default empty array.
        
        _boundingbox = null;
        
        /*
        // These are commented out to save memory for applications that don't need these.
        this._boundingbox = null
        this._lineBVH = null
        this._obj # Associated Obj.
        */
    }
    
    public Polyline(boolean _isClosed, Point ... points_in)
    {
    	this._isClosed = _isClosed;
    	this._isFilled = _isClosed;
    	this._points = new UBA<Point>();
    	
    	if(points_in != null)
    	{
    		this._points.append(this._points);
    	}
    }

    public void appendPoints(Point ... array)
    {

        for(Point p : array)
        {
            this.addPoint(p);
        }

        return;
    }

    public void addPoint(Point p)
    {
        this._points.push(p);

        // Expand the bounding box if it is defined.
        if(_boundingbox != null)
        {
           _boundingbox.expandByPoint(p);
        }

        return;
    }

    public Point removeLastPoint()
    {
    	return _points.pop();
    }

    
    public Point getPoint(int index)
	{
	    return this._points.get(index);
	}

	public int size()
    {
        return this._points.size();
    }

    public boolean isClosed()
    {
    	return _isClosed;
    }

	public boolean isFilled()
	{
	    return _isFilled;
	}

    /*
     * http://math.blogoverflow.com/2014/06/04/greens-theorem-and-area-of-polygons/
     * Computes the area of a 2D polygon directly from the polygon's coordinates.
     * The area will be positive or negative depending on the
     * clockwise / counter clockwise orientation of the points.
     * Also see: https://brycesummers.wordpress.com/2015/08/24/a-proof-of-simple-polygonal-area-via-greens-theorem/
     * Note: This function interprets this polyline as closed.
     #  -> float
     */
	public double computeArea()
    {
        int len = _points.size();
        Point p1  = _points.get(len - 1);

        double area = 0.0;

        // Compute based on Green's Theorem.
        for(int i = 0; i < len; i++)
        {
            Point p2 = _points.get(i);
            area += (p2.x + p1.x)*(p2.y - p1.y);
            p1 = p2; // Shift p2 to p1.
        }

        return -area / 2.0;
    }

    // -> bool
    public boolean isComplemented()
    {	        
        return computeArea() <= 0.0000001;
    }

	public Box ensureBoundingBox()
	{
		if(_boundingbox == null)
	    {
			generateBoundingBox();
	    }	        

	    return _boundingbox;
	}

	public Box generateBoundingBox()
	{
		this._boundingbox = new Box();

	    for(Point pt : this._points)
	    {
	    	this._boundingbox.expandByPoint(pt);
	    }

	    return _boundingbox;
	}

    public Box getBoundingBox()
    {
    	return this._boundingbox;
    }


    public void setAssociatedData(Object obj)
    {
        this._obj = obj;
    }

    public Object getAssociatedData()
    {
        return this._obj;
    }

    /*
    getBVH: () ->
        return @_lineBVH()
    */

    // Returns a list of Polylines, representing all of this polyline's line segments.
    // Returns them in halfedge order, with each segment is stored at the same index as its originating point.
    // output array is optional.
    public UBA<Polyline> toPolylineSegments(UBA<Polyline> output)
    {
        
        if(output == null)
        {
            output = new UBA<Polyline>();
        }

        int len = _points.size();
        for(int i = 0; i < len - 1; i++)// i in [0...len - 1]
        {
        	Point p0 = _points.get(i);
            Point p1 = _points.get(i + 1);
            output.push(new Polyline(false, p0, p1));
        }

        // Add the last point.
        if(_isClosed)
        {
            Point p0 = _points.get(len - 1);
            Point p1 = _points.get(0);
            output.push(new Polyline(false, p0, p1));
        }

        return output;
    }

    // Returns a list of line segments for intersection tests.
    public UBA<Line> _toLineSegments()
    {
        UBA<Line> output = new UBA<Line>();

        int len = _points.size();
        for(int i = 0; i < len - 1; i++)
        {
            Line line = new Line(i, i + 1, _points);
            output.push(line);
        }

        // Add the last point.
        if(_isClosed)
        {
            Line line = new Line(len - 1, 0, _points);
            output.push(line);
        }

        return output;
    }

    public UBA<Ray> toRays(UBA<Ray> output)
    {
    	if(output == null)
    	{
            output = new UBA<Ray>();
    	}

        int len = _points.size();
        for(int i = 0; i < len - 1; i++)
        {
            Point p0 = _points.get(i);
            Point p1 = _points.get(i + 1);
            Point dir = p1.sub(p0);
            output.push(new Ray(p0, dir));
        }

        // Add the last point.
        if(_isClosed)
        {
            Point p0 = _points.get(len - 1);
            Point p1 = _points.get(0);
            Point dir = p1.sub(p0);
            output.push(new Ray(p0, dir));
        }

        return output;
    }

    // Performs a point in Polygon test.
    // Assumes this is a closed polygon.
    // Accelerates this query using the polyline's bvh if present.
    // BDS.Point -> bool
    public boolean containsPoint(Point pt)
    {

        // E[O(log(n))]
        if(_lineBVH != null)
        {
            throw new Error("Implement me Please!");
        }
        else
        {
            // O(n).
            return _point_in_polygon_test(pt);
        }
    }
    
    public boolean _point_in_polygon_test(Point pt)
    {
        
        Ray ray = new Ray(pt, new Point(1.0, 0.0));

        // A Point is inside of a polygon if an arbitrary ray,
        // originating at that point crosses the boundary an even number of times.
        boolean odd = false;

        // count = 0

        UBA<Line> segments = _toLineSegments();
        for(Line segment : segments)
        {
        	if(ray.detect_intersection_with_line(segment))
        	{
                odd = !odd;
                // count++
        	}
        }

        return odd;
    }

    public boolean detect_intersection_with_box(Box box)
    {

        // We will want bounding boxes at this point.
        if(_boundingbox == null)
        {
            this.generateBoundingBox();
        }

        // No intersection if the bounding box doesn't intersect the input box.
        if(!box.intersects_box(this._boundingbox))
        {
            return false;
        }

        // Filled polyline and contains entire box.
        if(this.isFilled() && this.containsPoint(box.min))
        {
            return true;
        }

        // Filled box that contains entire polyline.
        if(box.isFilled() && box.containsPoint(this._points.get(0)))
        {
            return true;
        }

        // No perform a polyline <---> polyline intersection test.
        Polyline polyline = box.toPolyline();

        return detect_intersection_with_polyline(polyline);
    }

    // Returns true iff there is an intersection between a line segment in this polyline
    // and a line segment in the input polyline.
    // We are currently reducing this to line segment set intersection detection. O((smaller + larger)*log(smaller + larger))
    // We may be able to do better if we test the smaller polyline's segments against the larger's bvh. O(smaller*log(larger))
    public boolean detect_intersection_with_polyline(Polyline polyline)
    {
    	throw new Error("implement me please!");
    	/*
        // Convert both polylines to line segments.
        UBA<Line> lines1 = this._toLineSegments();
        UBA<Line> lines2 = polyline._toLineSegments();

        lines1.append(lines2);
        UBA<Line> all_lines = lines1;

        Intersector intersector = new Intersector();

        return intersector.detect_intersection_line_segments_partitioned(all_lines);
        */
    }	
    
    // Returns the polygons formed by moving every point
    // by the given percentage towards its centroid.
    public Polyline shrink(double percentage)
    {
    	Point centroid = this.getVisualCenter(1);
    	
    	Polyline output = new Polyline(this._isClosed, this._points, this._isFilled);
    	
    	int len = output._points.size();
    	for(int i = 0; i < len; i++)
    	{
    		Point pt = output._points.get(i);
    		pt = pt.multScalar(1 - percentage).add(centroid.multScalar(percentage));
    		output._points.set(i, pt);
    	}

    	return output;
    }
    
    public Point getCentroid()
    {
    	Point center = new Point(0, 0);
    	
    	for(Point p : _points)
    	{
    		center = center.add(p);
    	}
    	
    	center = center.divScalar(_points.size());
    	return center;
    }
    
    // https://github.com/mapbox/polylabel/blob/master/polylabel.js
    public Point getVisualCenter(double precision)
    {
    	
    	Point result = new Point(0, 0);
    	double max_dist = 0;
    	Box box = this.generateBoundingBox();
    	for(int i = 0; i < 100; i++)
    	{
    		Point pt = box.getRandomPointInBox();
    		double dist = distanceToPoint(pt); // Distance into the interior.
    		if(dist > max_dist)
    		{
    			max_dist = dist;
    			result = pt;
    		}
    	}
    	
    	return result;
    }
    
    // Alternative.
    public Point getVisualCenter2(double precision)
    {    	
    	
    	precision = 1.0;// default.

  	    // find the bounding box of the outer ring.  	
	  	Box box = ensureBoundingBox();
	  	double minX, minY, maxX, maxY;
	  	minX = box.min.x;
	  	minY = box.min.y;
	  	maxX = box.max.x;
	  	maxY = box.max.y;

	    double width = maxX - minX;
	    double height = maxY - minY;
	    double cellSize = Math.min(width, height);
	    double h = cellSize / 2;

	    // a priority queue of cells in order of their "potential" (max distance to polygon)
	    ArrayHeap<Cell> cellQueue = new ArrayHeap<Cell>(100);

	    if (cellSize == 0.0)
	    {
	    	new Point(minX, minY);
	    }

	    // cover polygon with initial cells
	    for (int x = (int) minX; x < maxX; x += cellSize)
	    for (int y = (int) minY; y < maxY; y += cellSize)
	    {
	    	cellQueue.add(new Cell(x + h, y + h, h, this));
	    }

	    // take centroid as the first best guess
	    Cell bestCell = getCentroidCell();

	    // special case for rectangular polygons
	    Cell bboxCell = new Cell(minX + width / 2, minY + height / 2, 0, this);
	    if (bboxCell.d > bestCell.d) bestCell = bboxCell;

	    //int numProbes = cellQueue.size();

	    while (!cellQueue.isEmpty())
	    {
	        // pick the most promising cell from the queue
	        Cell cell = cellQueue.extract_dominating();

	        // update the best cell if we found a better one
	        if (cell.d > bestCell.d)
	        {
	            bestCell = cell;
	        }

	        // do not drill down further if there's no chance of a better solution
	        if (cell.max - bestCell.d <= precision)
	        {
	        	continue;
	        }

	        // split the cell into four cells.
	        // Quad Tree recursion.
	        h = cell.h / 2;
	        cellQueue.add(new Cell(cell.x - h, cell.y - h, h, this));
	        cellQueue.add(new Cell(cell.x + h, cell.y - h, h, this));
	        cellQueue.add(new Cell(cell.x - h, cell.y + h, h, this));
	        cellQueue.add(new Cell(cell.x + h, cell.y + h, h, this));
	        //numProbes += 4;
	    }

	    return new Point(bestCell.x, bestCell.y);
	}
    
	// signed distance from point to polygon outline (negative if point is inside)
	double distanceToPoint(Point pt)
	{
	    // Returns the signed distance to this polyline.
		boolean inside = containsPoint(pt);
	    
		UBA<Line> lines = _toLineSegments();
		
		double min_distance = Double.MAX_VALUE;
		
		for(Line line : lines)
		{
			double new_dist = line.distanceToPt(pt);
			min_distance = Math.min(min_distance, new_dist);
		}
		
		if(inside)
		{
			return -min_distance;
		}
		else
		{
			return min_distance;
		}
	}

	// get polygon centroid
	Cell getCentroidCell()
	{
		Point p = getCentroid();
	    return new Cell(p.x, p.y, 0, this);
	}

	// get squared distance from a point to a segment
	double getSegDistSq(double px, double py, Point a, Point b)
	{

	    double x  = a.x;
	    double y  = a.y;
	    double dx = b.x - x;
	    double dy = b.y - y;

	    if (dx != 0 || dy != 0)
	    {

	        double t = ((px - x) * dx + (py - y) * dy) / (dx * dx + dy * dy);

	        if (t > 1)
	        {
	            x = b.x;
	            y = b.y;

	        }
	        else if (t > 0)
	        {
	            x += dx * t;
	            y += dy * t;
	        }
	    }

	    dx = px - x;
	    dy = py - y;

	    return dx * dx + dy * dy;
	}
}

class Cell implements Comparable<Cell>
{

	double x, y, h, d, max;
	public Cell(double x, double y, double h, Polyline polygon)
	{
	    this.x = x; // cell center x
	    this.y = y; // cell center y
	    this.h = h; // half the cell size
	    this.d = polygon.distanceToPoint(new Point(x, y)); // distance from cell center to polygon
	    this.max = this.d + this.h * Math.sqrt(2); // max distance to polygon within a cell
	}

	@Override
	public int compareTo(Cell other)
	{
		return (int) (other.max - this.max);
	}
}