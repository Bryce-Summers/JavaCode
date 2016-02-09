package Project.HalfEdgeMesh;

import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.IterB;

// These represent directed edges.

public class HEdge extends HEMeshElement<HEdge>
{

	public Vertex vertex; // The origin of the directed edge.
	public HEdge  next;
	public HEdge  prev;
	public HEdge  twin;
	public Edge edge;
	public Face face;

	
	public boolean isBoundary()
	{
		return face.isBoundary; 
	}
	
	// -- Internal function to a Line with 2 points: p1 and p2.
	// Returns < 0 on one side of the line.
	// Returns = 0 if the point is on the line.
	// Returns > 0 if the point is on the other side of the line.
	public double line_side_test(Vector c)
	{
		Vector p1 = vertex.position;
		Vector p2 = next.vertex.position;
		return ((p2.getX() - p1.getX())*(c.getY() - p1.getY()) - 
				(p2.getY() - p1.getY())*(c.getX() - p1.getX()));
	}
	
	// Prints this half edge's cycle of edges.
	public void printCycle()
	{
		HEdge h = this;
		
		System.out.println("Half Edge Loop:");
		
		do
		{
			System.out.print(h.hashCode() + ", ");
			h = h.next;
		}while(h != this);
		
		System.out.println("");
		
	}
}
