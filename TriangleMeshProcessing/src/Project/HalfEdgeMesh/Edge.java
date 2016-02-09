package Project.HalfEdgeMesh;

public class Edge extends HEMeshElement<Edge>
{
	public HEdge edge;
	
	public boolean isBoundary()
	{
		return edge.isBoundary() || edge.twin.isBoundary(); 
	}
	
	//		System.out.println("Edge: (" + x1 + ", " + y1 + ") <---> " + "(" + x1 + ", " + y1 + ")");
	// public String toString()
}
