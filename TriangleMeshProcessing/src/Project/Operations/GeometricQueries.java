package Project.Operations;

import BryceMath.Calculations.MathB;
import BryceMath.DoubleMath.Vector;
import Project.HalfEdgeMesh.Face;
import Project.HalfEdgeMesh.HEdge;
import Project.HalfEdgeMesh.Vertex;
import Project.Operations.QueryStructures.HE_Arc;

public class GeometricQueries
{
	/** 
	 * @param face: Defines a loop of half edges,
	 *    which has a bijection to a set of vertices,
	 *    which has a bijection to a set of positions in 2D space.
	 *    We assume that the face is convex.
	 *    We also assume the exterior face is represented in a consistent orientation.
	 *    We assume that the face has at least 2 edges on it.
	 *    The face can be partitioned into two connected sets of vertices, visible and not visible.
	 *    A vertex v is visible if the edge v.position --> position does not cross an edge in the face.
	 * @param visible: This process is accomplished quite quickly if we can give an example edge that is 
	 *    predetermined to be visible.
	 * @param position: The position by which we are performing the visibility tests against.
	 * @output Return value. an arc representing the set of visible edges.
	 *    The set of non visible edges can be found by
	 *    by interpreting the start and edge vertices in the opposite order and ignoring.
	 *  - I have decided to make this a value instead of a return type,
	 *    because it will allow algorithm designers to reuse the same output structure for sweep line algorithms
	 *    and should eliminate quite a few dynamic memory allocations. 
	 */
	//Returns an arc representing all edges form the given face loop of edges with vertices whose
	// positions are not occluded by the 
	public static void self_occulusion_test(HEdge visible, Vector position, HE_Arc output)
	{
		HEdge end = visible;

		// We will define visibility by a result of >= 0 for a half edge's line side test.
		
		// Search for the last visible vertex.
		// This will be the origin of the first half edge to fail the visibility test.
		while(end.line_side_test(position) >= 0)
		{
			end = end.next;
		}
		
		// Now that a line side test has failed, the origin or end is visible, but the other vertex is not.
		// Therefore end constitutes the first non visible edge in general.
		
		HEdge start = visible.prev;
		
		// Search for the first half edge in the opposite direction to fail the visibility test.
		while(start.line_side_test(position) >= 0)
		{
			start = start.prev;
		}
		
		// First fully visible half edge.
		// start.vertex is the first visible 
		start = start.next;
		
		// Populate the given output structure.
		output.start = start;
		output.end   = end;
	}
	
	// Returns the value of the angle v1, v2, v3, where the pivot is at v2.
	// The value returned is the interior angle.
	public static double angle(Vertex v1, Vertex v2, Vertex v3)
	{
		Vector s1 = v1.position.sub(v2.position);
		Vector s2 = v3.position.sub(v2.position);
		
		s1.data[2] = 0.0;
		s2.data[2] = 0.0;
		
		s1 = s1.norm();
		s2 = s2.norm();
			
		// Use the definition of the dot product.
		return Math.acos(s1.dot(s2));
	}
	
}



