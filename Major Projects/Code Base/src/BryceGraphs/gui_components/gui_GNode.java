package BryceGraphs.gui_components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.PrintStream;
import java.util.Iterator;

import BryceGraphs.ADTs.GNode;
import BryceGraphs.ADTs.Serials.SerialAdjacencyNode;
import BryceMath.Geometry.Rectangle;
import Data_Structures.ADTs.Bunch2;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.AArray;
import Data_Structures.Structures.HashingClasses.Dict;
import Data_Structures.Structures.InDevelopment.Trees.AVL;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.Input.gui_booleanInput;
import Game_Engine.GUI.Components.large.gui_dragWindow;
import Game_Engine.GUI.Components.small.gui_label;

/*
 * Graph Node class.
 * 
 * Written by Bryce Summers before 8 - 19 - 2014.
 * 
 * This class represents Graph nodes visually in the Bryce graphic user interface.
 * 
 * This class acts as the visual counterpart for any object that correctly implements the GNode interface.
 * 
 * NOTE : 
 * Serialization should be performed by using the GraphSerial Algorithms in the Bryce Graphs library. 
 * Serialization of this node only serializes the data associated with this particular node,
 * not the relational adjacency data. 
 */

public class gui_GNode extends gui_dragWindow implements SerialAdjacencyNode<gui_GNode>, Comparable<Obj>
{	
	GNode myGNode;
	
	// -- Private data.
	final int ROW_H = 48;
	
	// The mapping from GNodes to Children.
	AArray<GNode, gui_GNode> children = new AArray<GNode, gui_GNode>();
	AVL<Obj> children_ordered = new AVL<Obj>();
	
	// the y coordinate of the end bottom of the last field.
	protected int Y_MAX = 0;
	protected int X_MAX = 0;
	
	// -- Constructors.
	public gui_GNode(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_GNode(Rectangle screen)
	{
		super(screen);
	}
	
	protected class expandButton extends gui_booleanInput
	{
		// The child node that will be created when this button is pressed.
		private GNode child;
		
		public expandButton(Rectangle r, GNode node)
		{
			super(r);
			child = node;			
		}
		
		@Override
		public void update()
		{
			super.update();
			
			setText(child.getGNodeName());
			fitToContents();
			
			if(input_changed())
			{
				if(query())
				{
					expandNode(child);
				}
				else
				{
					contractNode(child);
				}
				
				return;
			}
			
			// Ensure the button is in the correct state that is represented to the user.
			/*
			boolean pred1 = children.contains_key(child);
			boolean pred2 = query();
			if((pred1 && pred2) || (!pred1 && !pred2))
			{
				// Correct the boolean state.
				soft_toggle();
			}
			*/
			
		}
		
	}
	
	// May only be called once.
	public void setNode(GNode node)
	{
		myGNode = node;
		
		children.clear();
		children_ordered = new AVL<Obj>();

		populateField(node);

		window.scrollH(X_MAX);
		window.scrollV(Y_MAX);

		setHandleText(node.getGNodeName());

		handle.setCollidable(false);
		handle.setDrawBorders(true);
	}
	
	/** The method that specifies the objects that the container will display.
	 *  This should be overriden by sub classes that desire alternative functionality.
	 * 
	 * @require This method must update the values of X_MAX and Y_MAX appropiatly.
	 *  
	 * @param node the Gnode whose data this node should be populated with.
	 */
	protected void populateField(GNode node)
	{
		Dict<Object> map = new Dict<Object>();
		node.getGNodeData(map);
		createRows(map.getPairs());	
	}
	
	/**
	 * 
	 * @param data A list of String keys associated with a GNodes data fields.
	 * 
	 * 		This data will be used to fully describe the characteristics of an object 
	 * 		in terms of primitives and GNodes.
	 * 
	 * @require This method sets the X_MAX and Y_MAX values correctly.
	 */
	private void createRows(List<Bunch2<String, Object>> data)
	{
		X_MAX = 0;
		Y_MAX = 0;
		
		for(Bunch2<String, Object> p : data)
		{
			gui_label key = new gui_label(0, Y_MAX, 0, ROW_H);
			key.setText(p.getKey());
			key.fitToContents();
			add(key);
			
			gui_label val_label;
			Rectangle val_label_bounds = new Rectangle((int)key.getX2(), Y_MAX, 0, ROW_H);
			Object val = p.getVal();
			
			// Perform the potentially overloaded logic for mapping
			// Objects to different types of labels.
			val_label = chooseProperLabel(val, val_label_bounds);
			val_label.fitToContents();
			add(val_label);
			
			X_MAX = (int)Math.max(X_MAX, val_label.getX2());
			Y_MAX += ROW_H;
		}
	
	}

	// Object to Label mapping function.
	// This is overriden in the gui_EditGnode class to create input objects, instead of static labels and buttons.
	protected gui_label chooseProperLabel(Object val, Rectangle val_label_bounds)
	{

		gui_label val_label;
		String val_str;
		
		if(val instanceof GNode)
		{
			GNode node = (GNode)val;
			val_str = node.getGNodeName();
			children.insert(node, null);
			val_label = new expandButton(val_label_bounds, node);
		}
		else
		{
			val_label = new gui_label(val_label_bounds);
			val_str = val.toString();
		}
		
		val_label.setText(val_str);
		return val_label;
		
	}

	// Lazy Tree methods.
	
	// Recursivly expand this tree of nodes.
	public void propogateNodes()
	{
		expandNode();
		for(Obj node_o : children_ordered)
		{
			if(node_o == null)
			{
				continue;
			}
			
			gui_GNode node = (gui_GNode)node_o;
			node.propogateNodes();
		}
	}
	
	// Creates GNodes for every GNode reference inside of this node.
	public void expandNode()
	{
		// Now expand all of the nodes.
		List<GNode> nodes = children.getKeys();
		for(GNode n : nodes)
		{
			expandNode(n);
		}
	}

	// Contract all children node references.
	public void contractNode()
	{
		List<GNode> nodes = children.getKeys();
		contractNode(nodes);
	}
	
	// Contract a set of nodes.
	public void contractNode(List<GNode> nodes)
	{
		for(GNode n : nodes)
		{
			contractNode(n);
		}
	}
	
	// Expands a given GNode in this gui_GNode, does not expand all nodes.
	private void expandNode(GNode node)
	{
		gui_GNode val = children.lookup(node);
		
		if(val != null)
		{
			return;
		}
		
		val = I_CHILD_NODE(node);
		children.insert(node, val);
		children_ordered.add(val);
	}

	// Contracts a given GNode
	private void contractNode(GNode node)
	{
		gui_GNode val = children.lookup(node);
		
		if(val == null)
		{
			return;
		}
		
		val.contractNode();
		children_ordered.remove(val);
		val.kill();
		
		children.remove_key(node);
	
	}
	
	// Positions all of the nodes in the tree with this as the root correctly.
	public void fixHeight()
	{
		// Create list with 1 null element.
		List<gui_GNode> L = new List<gui_GNode>();
				
		// Point an iterator at the first element.
		IterB<gui_GNode> iter = L.getIter();
		iter.insertAfter(null);
		
		// Recursivly fix the heights.
		horizontalAlign(iter, 0);
	}
	
	// Centers this node vertically with all of its descendant GNodes.
	// The tree will branch out horizontally from left ot right.
	// REQUIRES : The iter should be pointing to the gui_GNode with the highest y value in this node's column.
	// Iter should have a current node.
	// ENSURES : Returns without changing the location of the iter.
	private void horizontalAlign(IterB<gui_GNode> iter, double min_y)
	{
		Obj o = iter.current();
		if(o == null)
		{
			setY(min_y);
		}
		else
		{
			setY(Math.max(o.getY2(), min_y));
		}
		
		iter.replace(this);
		
		int child_num = children_ordered.size();
		boolean hasChildren =  child_num != 0;
		
		// FIX children.
		if(hasChildren)
		{
			if(!iter.hasNext())
			{
				iter.insertAfter(null);
			}
			else
			{
				iter.next();
			}
			
			int h = getH();
			min_y = getY() + h/2  - h*child_num/2; 
			
			min_y = Math.max(0, min_y);
			
			for(Obj child_o : children_ordered)
			{
				gui_GNode child = (gui_GNode)child_o;
				if(child != null)
				{
					child.horizontalAlign(iter, min_y);
				}
			}
		
			Obj first_child = children_ordered.getFirst();
			setY((first_child.getY() + iter.current().getY()) / 2);			
			iter.previous();
		}
		
		
	}
	
	// Returns a new node, which should be relocated.
	private gui_GNode I_CHILD_NODE(GNode node)
	{
		int w = getW();
		int h = getH();
		
		double x = getX2() + 64;
		double y = -h;
		
		gui_GNode output = I_NODE_TYPE(x, y, w, h);
		myContainer.obj_create(output);
		output.setNode(node);
		return output;
	}
	
	/**This should be overriden to provide new gui_GNodes of the overriding classes type.
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	protected gui_GNode I_NODE_TYPE(double x, double y, int w, int h)
	{
		return new gui_GNode(x, y, w, h);
	}
	
	// Allows Objects to be sorted by depth.
	public int compareTo(Obj o)
	{
		// Note: Smaller depths mean the object is closer to the viewer.
		if(getY() < o.getY())
		{
			return -1;
		}
		
		if(getY() > o.getY())
		{
			return 1;
		}
		
		return 0;
	}

	@Override
	public void draw(ImageB image, AffineTransform AT)
	{
		super.draw(image, AT);
		
		double x = getX2();
		double y = (getY() + getY2())/2;
		
		List<gui_GNode> childs = children.getValues();
		Graphics2D g = image.getGraphics();
		
		g.setColor(Color.BLACK);
		//g.setStroke(2);
		
		for(gui_GNode child : childs)
		{
			if(child == null)
			{
				continue;
			}
			
			double x2 = child.getX();
			double y2 = (child.getY() + child.getY2())/2;
			drawLine(g, AT, x, y, x2, y2);
		}
		
		g.dispose();
		
	}

	// -- Adjacency Node interface functions.
	@Override
	public Iterable<gui_GNode> getNeighbors()
	{
		List<gui_GNode> output = children.getValues().removeNulls();
					
		return output;
	}
	
	@Override
	public int getNeighborSize()
	{
		return children.size();
	}

	// Set this gui_GNodes outgoing neighbor edges.
	@Override
	public void setNeighbors(Iterable<gui_GNode> neighbors)
	{
		children.clear();
		children_ordered = new AVL<Obj>();
		for(gui_GNode node : neighbors)
		{
			children.insert(node.getGNode(), node);
			children_ordered.add(node);
		}
	}

	// Returns the GNode that this gui_GNode is visualizing.
	public GNode getGNode()
	{
		return myGNode;
	}
	
	// gui_GNodes do not store any information about their geometric location on the screen.
	public void serializeTo(PrintStream stream)
	{
		myGNode.serializeTo(stream);
	}
	
	public String getSerialName()
	{
		return "gui_GNode";
	}

}
