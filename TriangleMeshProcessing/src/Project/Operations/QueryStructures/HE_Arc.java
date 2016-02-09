package Project.Operations.QueryStructures;

import Project.HalfEdgeMesh.HEdge;

/**
 * @author Bryce Summers
 * @date 12/23/2015.
 * 
 * A short data structure used to represent sub loops of HalfEdge Faces.
 * The arc contains all vertices from the origin of the start edge to 
 * the origin of the end edge along a particular face.
 * 
 * this might for example be used to answer visibility queries for a
 * position and a loop.
 * 
 * Here is a typical usage of arcs.
 * 
 * HE_Arc arc;
 * for(HEdge edge - arc.start; edge != arc.end; edge = edge.next)
 * {
 * 	...
 * }
 * 
 */

public class HE_Arc
{
	public HEdge start;
	public HEdge end;
}
