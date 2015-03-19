package BryceMath;

import static util.Print.print;
import static util.testing.ASSERT;
import BryceMath.Calculations.Geometry;
import BryceMath.DoubleMath.Vector;
//- Useful testing functions.
// -- Useful Number construction functions.

/* BryceMath testing Class.
 * 
 * Rewritten by Bryce Summers 5 - 30 - 2013
 * 
 * Purpose : These unit tests are used to imply the correctness
 * 			 of my Exact math and other mathematical structures.
 * 
 * Tests :	1. Numbers
 */

public class test_structures
{
	
	static final int TEST_SIZE = 10000;
	
	public test_structures()
	{
		test_geometry();
		//print("Geometry Passed!");
		test_Matrices();
		test_vectors();
		
		print("All numeric tests passed!");
	}
		
	private void test_Matrices()
	{
		throw new Error("Please Implement me!");
	}
	
	private void test_vectors()
	{
		throw new Error("Please Implement me!");		
	}

	private void test_geometry()
	{
		Vector ray     = new Vector(0, 0);
		Vector ray_dir = new Vector(1, 0);
		
		Vector p1 = new Vector(1, -1);
		Vector p2 = new Vector(1,  1);
		
		ASSERT(Geometry.ray_line_segment_intersect(ray, ray_dir, p1, p2));
		
		
		ray = v(1, 5);
		ray_dir = v(-1, -1);
		
		p1 = v(2, 3);
		p2 = v(1, 1);
		
		ASSERT(!Geometry.ray_line_segment_intersect(ray, ray_dir, p1, p2));
	
		
		ray = v(0, 0);
		ray_dir = v(1, 0);
		p1 = v(0, -1);
		p2 = v(3, -1);
		
		ASSERT(!Geometry.ray_line_segment_intersect(ray, ray_dir, p1, p2));
		
		ray = v(900, 59);
		ray_dir = v(1, 0);
		
		p1 = v(500, 0);
		p2 = v(0, 500);
		
		ASSERT(!Geometry.ray_line_segment_intersect(ray, ray_dir, p1, p2));
	}
	
	private Vector v(double ... input)
	{
		return new Vector(input);
	}
	
	// - Internal testing functions.

}