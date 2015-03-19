package BryceGraphs.gui_components;

import java.io.PrintStream;

import util.SerialB;
import util.interfaces.Consumer2;

import BryceGraphs.ADTs.AdjacencyNode;
import BryceGraphs.ADTs.EditableGNode;
import BryceGraphs.ADTs.GNode;
import BryceGraphs.ADTs.Serials.SerialAdjacencyNode;
import BryceGraphs.Algorithms.GraphSerial;

import BryceMath.Geometry.Rectangle;


/* Node Viewer class.
 * Written by Bryce Summers on 7 - 12 - 2014.
 *  
 * Defines a plane, that the user can navigate through to view GNodes.
 * 
 * Visually shows the user a lazy horizontal tree.
 */

public class gui_TreeViewer extends gui_explorationWindow implements SerialB
{
	
	// -- Root.
	gui_GNode root;
	
	public gui_TreeViewer(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_TreeViewer(Rectangle screen, Rectangle sub_world, int worldW,
			int worldH)
	{
		super(screen, sub_world, worldW, worldH);
	}
	
	// FIXME : I should add options to have the trees pre expanded, or add buttons!
	
	// Opens a tree in view mode.
	public void viewNode(GNode node, int w, int h)
	{
		world.restartObjsList();
		
		root = new gui_GNode(0, 0, w, h);
		obj_create(root);
		root.setNode(node);
	}
	
	// Opens a tree in edit mode.
	public void editNode(EditableGNode node, int w, int h)
	{
		world.restartObjsList();
		
		root = new gui_EditableGNode(0, 0, w, h);
		obj_create(root);
		root.setNode(node);
	}
	
	// Recursively expand this node.
	public void propogateNodes()
	{
		root.propogateNodes();
	}
	
	@Override
	public void update()
	{
		super.update();
		if(root != null)
		{
			root.fixHeight();
		}
	}
	
	
	// -- Serialization methods.
	
	// This is essentially an object's proffered type descriptor.
	public String getSerialName()
	{
		return "Tree Viewer Directed Acyclic Graph";
	}

	// Standard abstract file graph serialization.
	public void serializeTo(PrintStream stream)
	{
		GraphSerial.serialize(stream, root);
	}
	
	// Serialize the nodes with a continuation that allows for game specific metadata to be extracted.
	public <Node extends SerialAdjacencyNode<Node>>
		   void serializeTo(PrintStream stream, Consumer2<Node, PrintStream> cont)
	{
		GraphSerial.serialize(stream, (Node)root, cont);
	}

}
