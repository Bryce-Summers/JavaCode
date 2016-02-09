package Project.HalfEdgeMesh;

import Data_Structures.Structures.IterB;

// A Parent class for Half Edge Meshes.
// Written by Bryce Summers on 12/23/2015.

public abstract class HEMeshElement<E extends HEMeshElement<E>>
{
	public IterB<E> iter;
	
	// Uses the iterator to remove this element from the Half Edge Mesh.
	public void delete()
	{
		iter.remove();
	}
}
