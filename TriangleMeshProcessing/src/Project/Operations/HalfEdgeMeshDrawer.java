package Project.Operations;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import util.ImageUtil;
import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.IterB;
import Project.HalfEdgeMesh.Edge;
import Project.HalfEdgeMesh.Face;
import Project.HalfEdgeMesh.HEdge;
import Project.HalfEdgeMesh.HalfEdgeMesh;
import Project.HalfEdgeMesh.Vertex;

/**
 * @author Bryce Summers.
 * Written on 12/28/2015.
 * 
 * This class provides functionality for rendering HalfEdgeMesh's to the screen.
 * 
 */

public class HalfEdgeMeshDrawer
{
	
	public int offset_x;
	public int offset_y;
	
	public HalfEdgeMesh mesh;
	
	public int vert_radius = 20;
	public float stroke_width  = 3;
	
	Color stroke_color = Color.BLACK;
	Color fill_color = Color.CYAN;
	Color face_color = Color.LIGHT_GRAY;
	
	// Constructor.
	public HalfEdgeMeshDrawer(HalfEdgeMesh mesh)
	{
		this.mesh = mesh;
	}

	// -- Drawing Functions. Draw sets of components using the current styles.
	
	public void drawVertices(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		ImageUtil.enableAllAntialiasing(g2);
		ImageUtil.setStrokeSize(g2, stroke_width);
		
		
		IterB<Vertex> iter = mesh.getVerticesBegin();
		while(iter.hasNext())
		{
			Vector v = iter.next().position;
			int x = (int)v.getX();
			int y = (int)v.getY();
			
			x += offset_x;
			y += offset_y;
		
			g.setColor(fill_color);
			g.fillOval(x - vert_radius, y - vert_radius,
					   vert_radius*2, vert_radius*2);
			
			g.setColor(stroke_color);
			g.drawOval(x - vert_radius, y - vert_radius,
					   vert_radius*2, vert_radius*2);
		}
	}
	
	public void drawEdges(Graphics g)
	{
		
		Graphics2D g2 = (Graphics2D) g;
		ImageUtil.enableAllAntialiasing(g2);
		ImageUtil.setStrokeSize(g2, stroke_width);		
		g.setColor(stroke_color);
		
		IterB<Edge> iter = mesh.getEdgesBegin();
		while(iter.hasNext())
		{
			HEdge e = iter.next().edge;
			Vector v1 = e.vertex.position;

			/*
			if(e.twin == null)
			{
				continue;
			}*/
			
			Vector v2 = e.twin.vertex.position;
			
			int x1 = (int)v1.getX();
			int y1 = (int)v1.getY();
			
			int x2 = (int)v2.getX();
			int y2 = (int)v2.getY();
			
			x1 += offset_x;
			x2 += offset_x;
			
			y1 += offset_y;
			y2 += offset_y;
			
			g.drawLine(x1, y1, x2, y2);
  
		}
	}
	
	public void drawBoundaryEdges(Graphics g, boolean boundaryOrNotBoundary)
	{
		Graphics2D g2 = (Graphics2D) g;
		ImageUtil.enableAllAntialiasing(g2);
		ImageUtil.setStrokeSize(g2, stroke_width);
		
		IterB<Edge> iter = mesh.getEdgesBegin();
		while(iter.hasNext())
		{
			Edge edge = iter.next();
			
			if(edge.isBoundary() != boundaryOrNotBoundary)
			{
				continue;
			}
			
			HEdge e = edge.edge;
			Vector v1 = e.vertex.position;
			Vector v2 = e.twin.vertex.position;
			
			int x1 = (int)v1.getX();
			int y1 = (int)v1.getY();
			
			int x2 = (int)v2.getX();
			int y2 = (int)v2.getY();
			
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	public void drawFaces(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		ImageUtil.enableAllAntialiasing(g2);
		ImageUtil.setStrokeSize(g2, stroke_width);
		
		g.setColor(face_color);
		
		IterB<Face> iter = mesh.getFacesBegin();
		while(iter.hasNext())
		{
			Face face = iter.next();
			
			int len = face.getSize();
			
			int[] x = new int[len];
			int[] y = new int[len];
			
			HEdge h0 = face.edge;
			HEdge h = h0;
			
			int index = 0;
			do
			{			
				Vector pos = h.vertex.position;
				x[index] = (int) pos.getX() + offset_x;
				y[index] = (int) pos.getY() + offset_y;
				index++;
				h = h.next;
			}while(h != h0);
								
			g.fillPolygon(x, y, len);		
			
		}
	}
}
