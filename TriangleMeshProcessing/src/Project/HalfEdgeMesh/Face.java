package Project.HalfEdgeMesh;

/**
 * 
 * @author Bryce Summers
 * @date 12/23/2015.
 * 
 * Faces define loops of vertices.
 * They come in two flavor's interior faces and exterior faces.
 *
 */

public class Face extends HEMeshElement<Face>
{
	public HEdge edge;
	public boolean isBoundary;
	
	public int getSize()
	{
		HEdge h0 = edge;
		HEdge h = edge;
		
		int count = 0;
		
		do
		{			
			count++;
			h = h.next;
		}while(h != h0);
		
		return count;
	}
}