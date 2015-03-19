package BryceMath.Calculations;

import BryceMath.DoubleMath.Vector;

/*
 * Geometry class, written 2 - 9 - 2013, by Bryce Summers
 * 
 * Purpose: Provides boolean functions for evaluating whether points are within geometric shapes.
 */
public class Geometry
{
	public static boolean rectangle2(double x,double y,double x1, double y1, double width, double height)
	{
		double x2 = x1 + width;
		double y2 = y1 + height;
		if(x >= x1 && x <= x2 && y >= y1 && y <= y2)
		{
			return true;
		}
		return false;
	}
	
	// Returns an array with the three barycentric components of the point with,
	// regards to the three triangle specifying vertices.
	public static double[] barycentric(Vector P, Vector A, Vector B, Vector C)
	{
		// Compute vectors
		Vector v0 = C.sub(A);
		Vector v1 = B.sub(A);
		Vector v2 = P.sub(A);

		// Compute dot products
		double dot00 = v0.dot(v0);
		double dot01 = v0.dot(v1);
		double dot02 = v0.dot(v2);
		double dot11 = v1.dot(v1);
		double dot12 = v1.dot(v2);

		// Compute barycentric coordinates
		double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
		double w = 1 - u - v;
		
		double[] output = new double[3];
		
		output[0] = u;
		output[1] = v;
		output[2] = w;
		
		return output; 
	}
	
	// Returns true iff the given ray intersects the given line segment.
	public static boolean ray_line_segment_intersect(Vector ray_position, Vector ray_direction, Vector line_p1, Vector line_p2)
	{
		Vector line_dir = line_p2.sub(line_p1);
		
		Vector denom_t = ray_direction.cross(line_dir);
		Vector denom_u = denom_t;
		
		Vector ray_line_connection = line_p1.sub(ray_position);
		
		if(!denom_t.isZero() && !denom_u.isZero())
		{
			// The time for the parametric representation of the ray.
			double t = ray_line_connection.cross(line_dir).div(denom_t);
			
			// The time for the parametric representation of the line.
			double u = ray_line_connection.cross(ray_direction).div(denom_u);

			if(0 <= u && u <= 1 && t > 0)
			{
				return true;
			}
		}
		
		return false;		
	}
	
}
