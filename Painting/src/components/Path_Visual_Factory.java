package components;

import Data_Structures.Structures.UBA;
import geometry.Point;
import geometry.Spline;
import geometry.Curve;

/*

Written on Oct.07.2017 by Bryce Summers
Purpose: Creates sets of triangles representing the extent of a path.
*/

public class Path_Visual_Factory
{

    public Path_Visual_Factory()
    {
    }

    public static Curve newCurve()
    {
      Spline spline = new Spline();
      return new Curve(spline);
    }

    // Max length is the maximum length per segment.
    // the curve is the spline that defines the center of the path.
    // width is the width of the path.
    // max_length indicates the precision of the discretization.
    public void getVisual(double max_length, Curve curve, double width, photonColor start_photonColor, photonColor end_photonColor,
    		Material mat,
    		UBA<FillTriangle> triangles_out, UBA<Geometry> left_geometry_out, UBA<Geometry> right_geometry_out)
    {
        double offset_amount = width/2;
        curve.updateDiscretization(max_length);

        // -- Compute various lines for the path.
        
        UBA<Double>   times_left  = new UBA<Double>(); 
        UBA<Double>   times_right = new UBA<Double>();
        UBA<Point> verts_left  = new UBA<Point>();
        UBA<Point> verts_right = new UBA<Point>();

        verts_left  = curve.getOffsets(max_length,  offset_amount, times_left);
        verts_right = curve.getOffsets(max_length, -offset_amount, times_right);

        // Compute fill, using time lists to determine indices for the faces.
        getFillTriangles(verts_left, verts_right, times_left, times_right, curve, triangles_out);
        
        getLineGeometries(verts_left,  times_left,  mat, curve, left_geometry_out, false);
        getLineGeometries(verts_right, times_right, mat, curve, right_geometry_out, true);
        
        for(FillTriangle tri : triangles_out)
        {
          tri.setphotonColors(start_photonColor, end_photonColor);
        }
        
        // FIXME: Add attributes to lines, including variable color, tangent, etc.
        
        //return output;
    }

    // Creates a list of Fill triangles based on the given boundary descriptions for a path.
    // Due to line curvature, vertices at higher curvature regions will exhibit higher degrees in this polygonalization.
    // Assumes each list of times ends on the same time, the times increase strictly monototically.
    private void getFillTriangles(UBA<Point> left_verts, UBA<Point> right_verts, UBA<Double> times_left, UBA<Double> times_right, Curve curve, UBA<FillTriangle> output)
    {
        int l_len = left_verts.size();
        int r_len = right_verts.size();

        int l_index = 0;
        int r_index = 0;

        // 1 of the indices is not at the end of the path.
        while(l_index < l_len - 1 || r_index < r_len - 1)
        {
            double left_time  = times_left.get(l_index);
            double right_time = times_right.get(r_index);

            boolean big_left  = false;
            big_left  = left_time  < right_time;

            // Break tie using by comparing the next couple of points.
            if(left_time == right_time)
            {
                big_left  = times_left.get(l_index + 1) < times_right.get(r_index + 1);
            }


            // Determined indexes based on whether the left or right side is leading.
            int i1, i2, i3;
            
            Point p1, p2, p3;
            double t1, t2, t3;
            //Point tan1, tan2, tan3;

            // Use 2 left vertices and 1 right vertex.
            if(big_left)
            {
                i1 = l_index;
                i2 = l_index + 1;
                i3 = r_index;
            
                p1 = left_verts.get(i1);
                p2 = left_verts.get(i2);
                p3 = right_verts.get(i3);
                
                t1 = times_left.get(i1);
                t2 = times_left.get(i2);
                t3 = times_right.get(i3);
                
                l_index += 1;
            }
            else // Big right otherwise.
            {
                i1 = r_index;
                i2 = r_index + 1;
                i3 = l_index;
                
                p1 = right_verts.get(i1);
                p2 = right_verts.get(i2);
                p3 = left_verts.get(i3);
                
                t1 = times_right.get(i1);
                t2 = times_right.get(i2);
                t3 = times_left.get(i3);
                
                r_index += 1;
            }
            
            Point tan1 = curve.tangent(t1).normalize();
            Point tan2 = curve.tangent(t2).normalize();
            Point tan3 = curve.tangent(t3).normalize();
            
            FillTriangle tri = new FillTriangle(p1, p2, p3, tan1, tan2, tan3);
            tri.setTimes(t1, t2, t3);
            if(big_left)
            {
            	tri.setSignedOffsetVals(-1, -1, 1);
            }
            else
            {
            	tri.setSignedOffsetVals(1, 1, 1);
            }

            // We use a model to allow collision queries to pipe back to this road object with time domain knowledge.
            output.add(tri);
            continue;
        }

        // THREE.Geometry
        return;
    }
    
    void getLineGeometries(UBA<Point> points, UBA<Double> times, Material mat, Curve curve, UBA<Geometry> output, boolean invert)
    {    	
    	
    	int len = points.size();
    	for(int i = 0; i < len - 1; i++)
    	{
    		int i1 = i;
    		int i2 = i + 1;
    		
    		Point pt1 = points.get(i1);
    		Point pt2 = points.get(i2);
    		
    		// FIXME: Put tangent attributes here. use times and curve.
    		g_line line;
    		if(invert)
    		{
    			line = new g_line(mat, pt2, pt1);
    		}
    		else
    		{
    			line = new g_line(mat, pt1, pt2);
    		}
    		
    		output.push(line);
    	}
    }
}