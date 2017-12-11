package structures;

import java.util.Arrays;
import java.util.Comparator;

import Data_Structures.Structures.UBA;
import geometry.Box;
import geometry.Point;
import geometry.Polyline;
import geometry.Ray;

// Allows for efficient ray queries of a set of simple (line segment, circle), geometries.
// Used to accelerate the performance of 2D raytracing.


/*
# 2D Axis Aligned Bounding Volume Hierarchy.
# Written by Bryce Summers on 12/6/2016.
# Adapted, simplified, and improved by Bryce on 1 - 5 - 2017.
# Ported to JAVA on 12.05.2017
#
# Purpose: This set partitioning structure may be used to speed up
#          certain geometric queries, such as collisions between polylineal
#          objects and point scene intersection queries.
#          If may also be used to rapidly detect non-collisions.
*/

public class BVH2D
{

	final private static int MAX_OBJECTS_PER_LEAF = 4;

    private boolean _leaf_node;
    private int     _size;
    Box _AABB;// Axis Aligned Bounding Box.
    private UBA<Polyline> _leafs;
    BVH2D _left;
    BVH2D _right;
	
    /* Constructed from the tree rooted at the given THREE.Object3D node.
     * polylines is a BDS.Polyline()
     * xy = {val: 'x' or 'y'}
     */
    public BVH2D(UBA<Polyline> polylines)
    {
        // Array of THREE.mesh objects.
    	_leafs     = null;
        _leaf_node = false;
        _size      = polylines.size();

        // Ensure that all of these polylines have bounding boxes.
        _ensure_bounding_boxes(polylines);
        _AABB = _compute_AABB(polylines);

        // Base case, less than 4 polylines get put into a collection of leaf nodes.
        if(polylines.size() < BVH2D.MAX_OBJECTS_PER_LEAF)
        {
            _leaf_node = true;
            _leafs = polylines.clone();
            
            return;
        }
        
        // 2 dimensional BVH does not use third dimension of bounding boxes.
        _AABB.min.z = -1;
        _AABB.max.z = +1;

        // Tries x and y partitions, uses minimum surface area heuristic.
        UBA<Polyline> polylines_x = _sort_list(polylines, 'x');
        UBA<Polyline> polylines_y = _sort_list(polylines, 'y');
        
        // Uses the minimum surface area heuristic to find a splitting point for both x and y,
        // then uses the dimension with minimum split surface area as the partition.
        UBA<Polyline>[] partitions = _partition_by_SA(polylines_x, polylines_y);
        UBA<Polyline> left_partition  = partitions[0];
        UBA<Polyline> right_partition = partitions[1];

        _left  = new BVH2D(left_partition);
        _right = new BVH2D(right_partition);
    }

    // Used in things like tree compression and rebalancing.
    // BDS.BVH2D -> copies all fields into this node.
    public void _copy_from(BVH2D bvh)
    {
        _leaf_node = bvh._leaf_node;
        _size      = bvh._size;
        _AABB      = bvh._AABB;
        _leafs     = bvh._leafs;
        _left      = bvh._left;
        _right     = bvh._right;
    }

    /*
     - Private Construction Methods. -----------------------
     */

    private class AABB_Comparator implements Comparator<Centroid_Index>
    {
    	char xy;
    	AABB_Comparator(char xy)
    	{
    		this.xy = xy;
    	}
    	
    	public int compare(Centroid_Index a, Centroid_Index b)
    	{
    		if(xy == 'x')
    		{
    			return (int)(a.centroid.x - b.centroid.x);
    		}
    		else if(xy == 'y')
    		{
    			return (int)(a.centroid.y - b.centroid.y);
    		}
    		
    		throw new Error("XY not x or y");
    	}
    }
    
    private class Centroid_Index
    {
    	Point centroid;
    	int index;
    	public Centroid_Index(Point centroid, int index)
    	{
    		this.centroid = centroid;
    		this.index    = index;
    	}
    }
    
    // Sorts the given polyline list by its centroid x position.
    public UBA<Polyline> _sort_list(UBA<Polyline> polyline_list, char xy)
    {
    	Centroid_Index[] centroid_index_list = _centroid_index_list(polyline_list);

    	// Sort list.
        AABB_Comparator comp_func = new AABB_Comparator(xy);
        Arrays.sort(centroid_index_list, comp_func);
        
        int len = polyline_list.size();
        UBA<Polyline> output = new UBA<Polyline>(len);
        for(int i = 0; i < len; i++)
        {
            int polyline_index = centroid_index_list[i].index;
            output.push(polyline_list.get(polyline_index));
        }

        return output;
    }

    // Converts a polyline list into a centroid node list that contains indices.
    Centroid_Index[] _centroid_index_list(UBA<Polyline> polyline_list)
    {
        
        int len = polyline_list.size();
        Centroid_Index[] output = new Centroid_Index[len];
        for(int i = 0; i < len; i++)
        {
            Centroid_Index centroid_index_node;

            Point centroid = _computeCentroid(polyline_list.get(i));
            int index = i;
            
            centroid_index_node = new Centroid_Index(centroid, index); 
            
            output[i] = centroid_index_node;
        }

        return output;
    }

    // Computes an appropriate centroid for the given polyline.
    Point _computeCentroid(Polyline polyline)
    {
        Box _AABB = polyline.getBoundingBox();

        return _AABB.getCentroid();
    }

    
    // Returns [left_AABB, right_AABB],
    // where the split is determined by minimizing the surface area heuristic.
    // ASSUMPTION: mesh_list.length >= 1
    // Chooses the split dimension with the lowest total surface area heuristic.
    private class Partition_Info
    {
    	double min_sah;
    	int    min_index;
    }
    
    UBA<Polyline>[] _partition_by_SA(UBA<Polyline> polyline_list_x, UBA<Polyline> polyline_list_y)
    {
        
    	// Minimization info values.
    	Partition_Info info = new Partition_Info();
    	info.min_sah = Double.MAX_VALUE;
        info.min_index = -1;

        // Search for minimum partition in the x and y lists.
        // Stores the values in the Partition_Info object.
        _update_partition_info(info, polyline_list_x);
        
        double min_sah = info.min_sah;
        
        _update_partition_info(info, polyline_list_y);
        
        // Changed
        boolean useY = info.min_sah < min_sah;
        UBA<Polyline> polyline_list;
        if(useY)
        {
        	polyline_list = polyline_list_y;
        }
        else
        {
        	polyline_list = polyline_list_x;
        }
        
        // Now we will return the left and right partitions corresponding to the minimum SAH.
        // ASSUMPTION: min_index >= 1
        int min_index = info.min_index;
        int left_size  = info.min_index;
        int right_size = polyline_list_x.size() - info.min_index; 
        UBA<Polyline> left  = new UBA<Polyline>(left_size);
        UBA<Polyline> right = new UBA<Polyline>(right_size);
        for(int i = 0; i < left_size; i++) // [0, min_index)
        {
            left.push(polyline_list.get(i));
        }
        for(int i = 0; i < right_size; i++) // [min_index, len]
        {
        	right.push(polyline_list.get(i + min_index));
        }

        @SuppressWarnings("unchecked")
		UBA<Polyline>[] output = (UBA<Polyline>[])new Object[2];//new UBA<Polyline>[2];
        output[0] = left;
        output[1] = right;
        
        return output;
    }
    
    // Updates info with appropriate minimum index values for partitioning the given list.
    // Won't update if the info contains a SAH from an alternate list
    // that is lower than any partition achievable from this list.
    void _update_partition_info(Partition_Info info, UBA<Polyline> polyline_list)
    {

    	UBA<Polyline> left  = new UBA<Polyline>();
    	UBA<Polyline> right = new UBA<Polyline>();
    	
        // Left starts out including the 1st item.
        left.push(polyline_list.get(0));

        // We populate the right partition in backwards order,
        // so that we can sequentially pop/push items to the left.
        // This saves us array movement time.
        int i0 = polyline_list.size() - 1;
        for(int i = i0; i >= 1; i--)// i in [i0..1] #2 dots imply inclusive of 0.
        {
            right.push(polyline_list.get(i));
        }

        // Minnimize over all possible partitions.
        for(int i = 1; i <= i0; i++) // i in [1..i0] // [1, len - 1] All possible partitions.
        {
            Box left_AABB  = _compute_AABB(left);
            double sah_left = _compute_SA(left_AABB);

            Box  right_AABB = _compute_AABB(right);
            double sah_right = _compute_SA(right_AABB);

            double sah = Math.max(sah_left, sah_right);

            if(sah < info.min_sah)
            {
                info.min_sah   = sah;
                info.min_index = i;
            }

            // Iterate partition choice.
            left.push(right.pop());
        }
    }

    // Ensures that all geometries have a current valid Bounding Box.
    void _ensure_bounding_boxes(UBA<Polyline> polyline_list)
    {        
        for(Polyline polyline : polyline_list)
        {
            polyline.generateBoundingBox();
        }
    }

    // Computes the axis aligned bounding box minnimally bounding the given
    // list of meshes.
    // Output will be represented by {min: THREE.Vector3, max: THREE.Vector3}
    Box _compute_AABB(UBA<Polyline> polyline_list)
    {

    	// bounding box.
        Box output = new Box();

        for(Polyline polyline : polyline_list)
        {
            Box _AABB = polyline.getBoundingBox();
            output = output.union(_AABB);
        }
        
        return output;
    }

    // Returns the surface area for the given bounding box.
    double _compute_SA(Box _AABB)
    {
        Point min = _AABB.min;
        Point max = _AABB.max;

        double dx = max.x - min.x;
        double dy = max.y - min.y;
        double dz = max.z - min.z;

        double sxy = dx*dy;
        double sxz = dx*dz;
        double syz = dy*dz;

        return sxy + sxz + syz; // Note: There is not need to multiply this by 2 for a heuristic measure.
    }

    // Returns the first closed polyline that is found.
    // ignores unclosed polylines.
    // returns null otherwise.
    // It is advisable that any meshes used for queries be used with ways of getting
    // to the classes that you are interested in, such as a @model attribute.
    Polyline query_point(Point pt)
    {
    	
        // Check leaf nodes, narrow-phase collision detection.
        if(_leaf_node)
        {        
            for(Polyline polyline : _leafs)
            {
                if (polyline.isClosed() && polyline.containsPoint(pt))
                {
                    return polyline;
                }
            }
            
            return null;
        }
        
        // Check children.
        if(_AABB.containsPoint(pt))
        {
        	Polyline result;
            result = _left.query_point(pt);
            if(result != null)
            {
            	return result;
            }

            result = _right.query_point(pt);
            return result;
        }

        // Broad phase no-collision.
        return null;
    }

    // returns a list of all closed geometries at the given location on the 2D plane.
    // ignores unclosed polylines.
    // returns null otherwise.
    // It is advisable that any meshes used for queries be used with ways of getting
    // to the classes that you are interested in, such as a @model attribute.
    UBA<Polyline> query_point_all(Point pt, UBA<Polyline> output_list)
    {

        if(output_list == null)
        {
            output_list = new UBA<Polyline>();
        }

        // Check leaf nodes, narrow-phase collision detection.
        if(_leaf_node)
        {
            for(Polyline polyline : _leafs)
            {
                if(polyline.isClosed() && polyline.containsPoint(pt))
                {
                    output_list.push(polyline);
                }
            }

            return output_list;
        }

        // Check children.
        if(_AABB.containsPoint(pt))
        {
            _left.query_point_all(pt, output_list);
            _right.query_point_all(pt, output_list);
        }

        return output_list;
    }


    // Returns all polylines that intersect the given box.
    // output_list is optional.
    UBA<Polyline> query_box_all(Box query_box, UBA<Polyline> output_list)
    {
        if(output_list == null)
        {
            output_list = new UBA<Polyline>();
        }

        if(_leaf_node)
        {
            for(Polyline polyline : _leafs)
            {
            	// Output the polyline if it intersects the given box.
                if(polyline.detect_intersection_with_box(query_box))
                {
                    output_list.push(polyline);
                }
                continue;
            }

            return output_list; // Base Case.
        }

        // Check children.
        if(_AABB.intersects_box(query_box))
        {
            _left.query_box_all(query_box,  output_list);
            _right.query_box_all(query_box, output_list);
        }

        return output_list;
    }

    // query_circle_all --> reduces to query box all, then filter via narrow pass.

    // Returns a complete list of Polylines, representing the bounding boxes of this Bounding Volume Hiearchy.
    // () -> BDS.Polyline[]
    public UBA<Polyline> toPolylines()
    {
        // Create a list of all line geometries.
    	UBA<Polyline> polylines = new UBA<Polyline>();
        _toPolylines(polylines);
        return polylines;
    }


    // Appends to the given list Line Geometries representing the all of the bounding boxes for this AABB hierarchy.
    private void _toPolylines(UBA<Polyline> output)
    {

        // First create a polyline for this node's box.
        Point min = _AABB.min;
        Point max = _AABB.max;

        double min_x = min.x;
        double min_y = min.y;

        double max_x = max.x;
        double max_y = max.y;

        Point p0 = new Point( min_x, min_y, 0 );
        Point p1 = new Point( max_x, min_y, 0 );
        Point p2 = new Point( max_x, max_y, 0 );
        Point p3 = new Point( min_x, max_y, 0 );
        
        Polyline polyline = new Polyline(true, p0, p1, p2, p3); // Closed polyline.

        output.push(polyline);

        // If we are not a leaf node, add left and right child nodes.
        if(!_leaf_node)
        {
            _left._toPolylines(output);
            _right._toPolylines(output);
        }

        return;
    }


    // BVH dynamic editing functions.
    // Optimizes the bvh by rebalancing the tree.
    public void optimize()
    {
    	// Not currently implemented.
    }

    // Adds the given polyline to the bvh.
    public void add(Polyline polyline)
    {

        polyline.ensureBoundingBox();

        // BASE CASE:
        // Add the polyline to a leaf node.
        if(_leaf_node)
        {
            _leafs.push(polyline);
            _AABB = _AABB.union(polyline.getBoundingBox());
            _size++;
            return;
        }
        
        // Compute the potential bounding boxes that this polyline will create.
        Box potential_bb_left  = _left._AABB.union(polyline.getBoundingBox());
        Box potential_bb_right = _right._AABB.union(polyline.getBoundingBox());

        // Determine which side the polyline may enter that will create the smallest disturbance.
        double sa_diff_left  = _compute_SA(potential_bb_left)  - _compute_SA(_left._AABB);
        double sa_diff_right = _compute_SA(potential_bb_right) - _compute_SA(_right._AABB);

        // Recursion.
        if(sa_diff_left < sa_diff_right)
        {
            _left.add(polyline);
        }
        else
        {
            _right.add(polyline);
        }

        // Update the bounding box for this node.
        _AABB = _left._AABB.union(_right._AABB);

        // Update the size variable.
        _size++;

        return;
    }

    // Removes the given polyline from the bvh.
    // BDS.Polyline -> bool
    // Return indicates whether the polyline has been successfully removed.
    public boolean remove(Polyline polyline)
    {

        polyline.ensureBoundingBox();
        Box polyline_bb = polyline.getBoundingBox();

        // Skip this branch, because the polyline is not in its bounding box.
        if(!polyline_bb.intersects_box(_AABB))
        {
            return false;
        }

        // BASE CASE:
        // Add the polyline to a leaf node.
        if(_leaf_node)
        {

            // Rebuild this leaf node, shirinking it to not include the removed polyline.
            Box _AABB = new Box();

            UBA<Polyline> old_lines = _leafs;
            _leafs = new UBA<Polyline>();

            boolean removed = false;

            for(Polyline old_line : old_lines)
            {

                // Don't add the polyline to be removed.
                if(polyline == old_line)
                {
                    removed = true;
                    _size--; // Decrease this node's size.
                    continue;
                }

                _AABB = _AABB.union(old_line.getBoundingBox());
                _leafs.push(old_line);
            }

            return removed;
        }

        // Try left.
        boolean removed = _left.remove(polyline);

        // Try right, if necessary.
        if(!removed)
        {
        	removed = _right.remove(polyline);	
        }
        

        // No change.
        if(!removed)
        {
            return false;
        }
        
        // From here on out, we assume that a polyline has been removed.


        // Reduce this node's size.
        _size--;

        // Both left and right are depleted.
        // This could happen if this is the root of the tree and the very last object has been removed.
        if(_size == 0)
        {
            _leaf_node = true;
            _leafs = new UBA<Polyline>();
            _left  = null;
            _right = null;
            return removed;
        }
            
        // Left node is depleted, compress the right branch onto this node.
        if(_left._size == 0)
        {
            // Compress the right branch onto this node.
            _copy_from(_right);

            // Note: after this point this could now be a leaf node.
            return true;
        }

        // right node is depleted, compress the left branch onto this node.
        if(_right._size == 0)
        {            
            _copy_from(_left);

            // Note: after this point this could now be a leaf node.
            return true;
        }

        // If neither branch is depleted, then we update this parent node's bounding box.
        _AABB = _left._AABB.union(_right._AABB);

        return true;
    }

    public Polyline query_ray(Ray query)
	{
		// TODO Auto-generated method stub
		return null;
	}
}

    