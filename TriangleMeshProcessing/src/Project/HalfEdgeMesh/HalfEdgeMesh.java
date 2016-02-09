package Project.HalfEdgeMesh;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import BryceMath.DoubleMath.Vector;
import Data_Structures.ADTs.Queue;
import Data_Structures.Structures.HashTable;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.SingleLinkedList;
import Project.Operations.GeometricQueries;
import Project.Operations.QueryStructures.HE_Arc;

/*
 * This is a half edge mesh Data structure for Geometric Processing.
 * Written By Bryce Summers.
 * 
 * I have currently been focusing on the representation of a set of Vector locations via a triangulation.
 * 
 * - Triangulation of 2D sets of points.
 * - Conversion of an arbitrary triangulation to a delauney triangulation.
 * - This can be extended to a 2D surface in 3D by ignoring the z coordinates, which is already being done anyways.
 * 
 * - TODO : Triangles to HalfEdgeMesh Conversion.
 * 
 * IDEAS : Arbitrary addition of vertices to the mesh, via extension of exterior,
 *         or triangulation of an interior polygon.
 *         
 *         User can specify their preferences on maintaining Delauney criteria.
 */

public class HalfEdgeMesh
{
	// -- Data Fields.
	
	private List<Edge>   edges    = new List<Edge>();
	private List<HEdge>  hEdges   = new List<HEdge>();
	private List<Vertex> vertices = new List<Vertex>();
	private List<Face>   faces    = new List<Face>();
	
	public int face_size = 3;
	
	// -- Constructors
	
	// Initializes this halfedge mesh as a 2D triangulation of the given set of points.
	public HalfEdgeMesh(boolean delauney, Vector ... points)
	{
		System.out.println("Initializing Points");
		initTriangulation(points);
		
		System.out.println("Making the Mesh Delauney!");
		if(delauney){ makeDelauney(); }
	}
	
	// -- Standard Component Creation functions.

	
	// Creates a fresh edge structure for this mesh.
	public Edge newEdge()
	{
		Edge e = new Edge();
		edges.add(e);
		
		IterB<Edge> iter = edges.getTailIter();
		
		// Move the iterator on top of the last element.
		iter.previous();
		
		e.iter = iter;
		
		//iter.current() now returns the correct element.
		
		return e;
	}
	
	public HEdge newHEdge()
	{
		HEdge he = new HEdge();
		hEdges.add(he);
		
		IterB<HEdge> iter = hEdges.getTailIter();
		
		// Move the iterator on top of the last element.
		iter.previous();
		
		//iter.current() now returns the correct element.
		he.iter = iter;
		
		return he;
	}
	
	public Vertex newVertex()
	{
		Vertex v = new Vertex();
		vertices.add(v);
		
		IterB<Vertex> iter = vertices.getTailIter();
		
		// Move the iterator on top of the last element.
		iter.previous();
		
		//iter.current() now returns the correct element.
		v.iter = iter;
		
		return v;
	}
	
	public Face newFace()
	{
		Face f = new Face();
		faces.add(f);
		
		IterB<Face> iter = faces.getTailIter();
		
		// Move the iterator on top of the last element.
		iter.previous();
		
		// iter.current() now returns the correct element.
		f.iter = iter;
		
		return f;
	}
	
	// -- Iteration Functions for the various fields.
	public IterB<Edge> getEdgesBegin()
	{
		return edges.getIter();
	}

	public IterB<HEdge> getHEdgesBegin()
	{
		return hEdges.getIter();
	}
	
	public IterB<Vertex> getVerticesBegin()
	{
		return vertices.getIter();
	}

	public IterB<Face> getFacesBegin()
	{
		return faces.getIter();
	}
	
	// Sorts vectors in order of increasing x coordinate and increasing y coordinate.
	private class VectorComparator implements Comparator<Vector>
	{

		@Override
		public int compare(Vector v1, Vector v2)
		{
			double diffX = v1.getX() - v2.getX();
			
			if(diffX < 0)
			{
				return -1;
			}
			
			if(diffX > 0)
			{
				return 1;
			}
			
			double diffY = v1.getY() - v2.getY();
			
			if(diffY < 0)
			{
				return -1;
			}
			
			if(diffY > 0)
			{
				return 1;
			}
			
			System.out.println("Duplicate x, y point in HalfEdgeMesh.");
			throw new Error("HalfEdgeMesh Problem, duplicate (x,y) vector location.");
			//return 0;
		}
		
		
	}
	
	// -- Construction methods.
	
	private void initTriangulation(Vector[] points)
	{
		int len = points.length;

		if(len < 3)
		{
			throw new Error("Error: We cannot handle half edge meshes with fewer than 3 vertices.");	
		}

		// First we sort all of the vectors by x component.
		// (Ties are broken by y component.)
		Arrays.sort(points, new VectorComparator());

		HEdge visible_exterior_edge;
		
		// We form a halfEdgeMesh from the first 2 points.
		visible_exterior_edge = addInitialEdge(points[0], points[1]);
		
		// Allocate an output structure only once.
		HE_Arc helpful_output_structure = new HE_Arc();
		
		// Add the remainder of the points to the mesh via extension.
		for(int i = 2; i < len; i++)
		{
			visible_exterior_edge =	addExteriorPositionToTriangulation(points[i],
																	   visible_exterior_edge,
																	   helpful_output_structure);
		}
	}
	
	
	// Performs a sequence of edge flips for portions of triangle that do not fulfill the delauney property.
	// This takes at most O(n^2) time for each component,
	// where n is the number of connected vertices in this mesh.
	// ENSURES: The mesh forms delauney triangulations afterwards.
	public void makeDelauney()
	{
		Queue<Edge> Q = new SingleLinkedList<Edge>();
		HashTable<Edge> H = new HashTable<Edge>();
		
		// Check all non boundary initial edges. 
		IterB<Edge> iter = getEdgesBegin();
		while(iter.hasNext())
		{
			Edge e = iter.next();
			if(!e.isBoundary())
			{
				Q.enq(e);
				H.add(e);
			}
		}
		
		// Iteratively flip non delauney edges until all edges are delauney.
		while(!Q.isEmpty())
		{
			Edge e = Q.deq();
			H.remove(e);
			
			if(!isDelauney(e))
			{
				edge_flip(e);
				
				HEdge he1 = e.edge;
				HEdge he2 = he1.twin;
				
				Edge e1 = he1.next.edge;
				Edge e2 = he1.prev.edge;
				Edge e3 = he2.next.edge;
				Edge e4 = he2.prev.edge;
								
				
				// Make sure the neighbors are checked at least one more time.
				if(!e1.isBoundary() && H.insert(e1)){Q.enq(e1);}
				if(!e2.isBoundary() && H.insert(e2)){Q.enq(e2);}
				if(!e3.isBoundary() && H.insert(e3)){Q.enq(e3);}
				if(!e4.isBoundary() && H.insert(e4)){Q.enq(e4);}
				
								
				
			}
		}
		
	}

	
	// Adds a self contained halfedge group for the given triangle of points.
	// This will be used at the beginning of construction methods to form a valid triangle mesh.
	// with a well defined interior and exterior.
	// @returns the Exterior face associated with this new branch of the mesh.
	/**
	 * Assumes p1.x <= p2.x, if p1.x == p2.x, then p1.y < p2.y
	 * @return Returns the edge p2 --> p1 ,
	 * 		   because p2 is guaranteed to be visible to any points further along
	 *         in our total ordering of the vertices. This will be useful when building the convex hull and
	 *         triangulation.
	 */
	private HEdge addInitialEdge(Vector p1, Vector p2)
	{

		// Declare and initialize the elements.
		Face exterior_face = newFace();

		Vertex v1 = newVertex();
		Vertex v2 = newVertex();

		// v1 --> v2;
		HEdge he1 = newHEdge();
		
		// v2 --> v1;
		HEdge he2 = newHEdge();

		Edge edge = newEdge();


		// -- Link up the various elements.

		// Since there is only 1 edge right now, there is only 1 face which is the infinite
		// exterior of this edge.
		// This face also defines the boundary that encloses the half edge mesh thus far.
		exterior_face.isBoundary = true;
		exterior_face.edge = he1;

		v1.edge = he1;
		v1.position = p1;

		v2.edge = he2;
		v2.position = p2;

		he1.edge = edge;
		he1.next = he2;
		he1.prev = he2;
		he1.twin = he2;
		he1.vertex = v1;
		he1.face = exterior_face;

		he2.edge = edge;
		he2.next = he1;
		he2.prev  = he1;
		he2.twin = he1;
		he2.vertex = v2;
		he2.face = exterior_face;

		edge.edge = he1;

		return he2;
	}
	
	// Creates a new Vertex at the given position and connects it with triangular faces to the current mesh.
	// This assumes that the point is outside of the given exterior face.
	/**
	 * 
	 * @param pos is the position of the new vertex to be added to the mesh.
	 * @param visible_exterior_edge an edge e such that e.vertex is the previous vertex in the sweepline
	 *        total order. e should is also a boundary edge.
	 * @param arc a utility structure that will be used to store information on the visible portion
	 *        of the convex hull of the points already added to the mesh.
	 *        This function extends the convex hull by adding the given position as a vertex in this mesh
	 *        and triangulating the interior of the polygon formed from the visible boundary vertices
	 *        of the previous convex hull and the new vertex at the given position.
	 * @return the exterior HEdge starting from pos. 
	 */
	private HEdge addExteriorPositionToTriangulation(Vector pos, HEdge visible_exterior_edge, HE_Arc arc)
	{
		// Find the first and last points on the exterior of the face that are visible to the given position.
		GeometricQueries.self_occulusion_test(visible_exterior_edge, pos, arc);
		
		Vertex new_vertex = newVertex();
		new_vertex.position = pos;
		
		Face exterior_face = visible_exterior_edge.face;
		
		// -- Here we have link all of the visible vertices along the previous convex hull to this new vertex.
		
		// Let us begin by creating the exterior Edge from arc.start.vertex to new_vertex.
		Edge edge = newEdge();
		HEdge trailing_edge = newHEdge();
		
		// Link of the Full Edge.
		edge.edge = trailing_edge;
		
		// Link up as much information as possible for the outpointing half edge.
		trailing_edge.vertex = arc.start.vertex;
		//trailing_edge.next = null;// Not determined yet.
		trailing_edge.prev = arc.start.prev;
		
		// FIXME : I need to put this link in a different place to prevent segfaults.
		trailing_edge.prev.next = trailing_edge;
		//trailing_edge.twin = null; // Not yet defined.
		trailing_edge.edge = edge;
		trailing_edge.face = exterior_face;
		
		// This will be used at the end.
		HEdge first_trailing_edge = trailing_edge;
		
		// Every edge on the arc lies on a triangle that we can form with the new position.
		// We will iteratively form these triangles and add the information to the mesh.
		HEdge arc_edge_next = null;
		for(HEdge arc_edge = arc.start; arc_edge != arc.end; arc_edge = arc_edge_next)
		{
			// Remember this value so we can transverse the arc properly.
			arc_edge_next = arc_edge.next;
			
			// Every arc edge defines a face with vertices (arc_edge.vertex, arc_Edge.next.vertex, and new_vertex)
			// The face has the loop of 3 half edges: arc_edge --> new_hedge --> trailing_edge_twin -->
			Face new_face = newFace();
			
			Edge new_edge = newEdge();
			HEdge new_hedge = newHEdge();
			HEdge trailing_edge_twin = newHEdge();

			new_face.isBoundary = false; // Internal face.
			new_face.edge = arc_edge;

			new_edge.edge = new_hedge;

			// This will become the trailing half edge in the next iteration.
			new_hedge.vertex = arc_edge.next.vertex; // The origin of the directed edge.
			new_hedge.next = trailing_edge_twin;
			new_hedge.prev = arc_edge;
			// new_hedge.twin = null; // Not yet defined.
			new_hedge.edge = new_edge;
			new_hedge.face = new_face;

			// Now we will setup the trailing half edge's twin.
			trailing_edge_twin.vertex = new_vertex; // The origin of the directed edge.
			trailing_edge_twin.next = arc_edge;
			trailing_edge_twin.prev = new_hedge;
			trailing_edge_twin.twin = trailing_edge;
			trailing_edge_twin.edge = trailing_edge.edge;
			trailing_edge_twin.face = new_face;
			
			// Fixed the undefined twin of the trailing edge.
			trailing_edge.twin = trailing_edge_twin;
			
			// Fix and update the arc_edge
			arc_edge.face = new_face;
			arc_edge.prev = trailing_edge_twin;
			arc_edge.next = new_hedge;
			
			
			// -- Prepare for the next iteration.
			trailing_edge = new_hedge;
		}
		
		// Now we need to form the final trailing edge's twin and connect it to the first trailing edge.
		
		HEdge trailing_edge_twin = newHEdge();
		trailing_edge_twin.vertex = new_vertex; // The origin of the directed edge.
		trailing_edge_twin.next = arc.end;
		trailing_edge_twin.prev = first_trailing_edge;
		trailing_edge_twin.twin = trailing_edge;
		trailing_edge_twin.edge = trailing_edge.edge;
		trailing_edge_twin.face = exterior_face;
		
		trailing_edge.twin = trailing_edge_twin;
		
		first_trailing_edge.next = trailing_edge_twin;
		
		// Finish the new vertex only once at the end.
		new_vertex.edge = trailing_edge_twin;
		
		
		// -- Make the final fixes.
		
		// The exterior face's reference may no longer be on the convex hull.
		exterior_face.edge = first_trailing_edge;

		// This will be used to launch subsequent mesh extension operations.
		return trailing_edge_twin;
	}
	
	// Assumes that this is a triangle mesh.
	public boolean isDelauney(Edge edge)
	{
		// Find the relevant 4 vertices.
		HEdge he1 = edge.edge;
		HEdge he2 = he1.twin;
		
		// Inner vertices on the edge.
		Vertex i1 = he1.vertex;
		Vertex i2 = he2.vertex;
		
		// outer vertices.
		Vertex o1 = he1.prev.vertex;
		Vertex o2 = he2.prev.vertex;
		
		// We don't really care about the orientation, because the problem is symmetrical.
				
		// An edge is delauney if its exterior triangle angles sum up to less than 180 degrees.
		return GeometricQueries.angle(i1, o1, i2) +
			   GeometricQueries.angle(i1, o2, i2)  <= Math.PI; 
	}
	
	
	// -- Local operations.
	public void edge_flip(Edge edge)
	{
		if(edge.isBoundary())
		{
			return;
		}
		
		// -- Locate all relevant features.
		
		HEdge he1 = edge.edge;
		HEdge he2 = he1.twin;
		
		Vertex v1 = he1.vertex;
		Vertex v2 = he2.vertex;
		
		HEdge e1, e2, e3, e4;
		
		e1 = he2.next;
		e2 = he1.next;
		e3 = he1.prev;
		e4 = he2.prev;
		
		Face f1 = he1.face;
		Face f2 = he2.face;
				
		// -- Now in fix them in a dignified order.
		
		he1.next = e4;
		he1.prev = e2;
		he2.next = e3;
		he2.prev = e1;
		
		he1.vertex = e3.vertex;
		he2.vertex = e4.vertex;
		
		v1.edge = e1;
		v2.edge = e2;
		
		e1.next = he2;
		e1.prev = e3;

		e2.next = he1;
		e2.prev = e4;
		
		e3.next = e1;
		e3.prev = he2;
		e3.face = f2;
		
		e4.next = e2;
		e4.prev = he1;
		e4.face = f1;
		
		f1.edge = he1;
		f2.edge = he2;

		
	}
	
	public void edge_collapse(Edge edge)
	{
		throw new Error("Please Implement Me!");
	}
	
	public void edge_split(Edge edge)
	{
		throw new Error("Please Implement Me!");
	}
	
	public void edge_double(Edge edge)
	{
		throw new Error("Please Implement Me!");
	}
	
}
