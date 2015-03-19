package BryceGraphs.gui_components;

import java.util.Iterator;

import BryceGraphs.ADTs.EditableGNode;
import BryceGraphs.ADTs.GNode;
import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.Input.gui_DecimalInput;
import Game_Engine.GUI.Components.Input.gui_IntegerInput;
import Game_Engine.GUI.Components.Input.gui_booleanInput;
import Game_Engine.GUI.Components.Input.textBased.gui_StringInput;
import Game_Engine.GUI.Components.small.gui_label;

/**Extends the gui_GNode class to provide dynamic editing capabilities to the nodes.
 * 
 * This is useful for project that wish to edit data rather than only view it.
 */

public class gui_EditableGNode extends gui_GNode
{

	public gui_EditableGNode(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_EditableGNode(Rectangle screen)
	{
		super(screen);
	}
	
	/**populateField extension method for Editable GNodes.
	 * 
	 * regular GNodes will still be displayed statically.
	 * @Override overrides the similar field in the normal gui_GNode class. 
	 * @param node An editable node that who will specify an editing interface.
	 */
	protected void populateField(GNode node)
	{
		if(node instanceof EditableGNode)
		{
			((EditableGNode)node).populateEditField(this, ROW_H);
		}
		else
		{
			super.populateField(node);
		}
	}

	/*
	 *	Primitive Input Box functions.
	 *	FIXME : Makes sure these are as clean as possible before I have too many dependencies on them. 
	 */
	
	/**
	 * 
	 * @param key
	 * @param val
	 * @param bounds
	 * @return
	 */	
	public gui_StringInput String(String key, String val, Rectangle bounds)
	{
		gui_StringInput out = new gui_StringInput(bounds);
		out.populateInput(val);
		
		addField(key, out);
		
		return out;
	}
	
	/**
	 * 
	 * @param key
	 * @param val
	 * @param bounds
	 * @return
	 */
	public gui_IntegerInput Integer(String key, Integer val, Rectangle bounds)
	{
		gui_IntegerInput out = new gui_IntegerInput(bounds);
		out.populateInput(val);
		
		addField(key, out);
		
		return out;
	}
	
	public gui_DecimalInput Double(String key, Double val, Rectangle bounds)
	{
		gui_DecimalInput out = new gui_DecimalInput(bounds);
		out.populateInput(val);
		
		addField(key, out);
		
		return out;
	}
	
	public gui_booleanInput GNode(String key, GNode node, Rectangle bounds)
	{
		node.getGNodeName();
		children.insert(node, null);
		gui_booleanInput b = new expandButton(bounds, node);
		b.setText(node.getGNodeName());
		b.fitToContents();
		addField(key, b);
		
		return b;
	}
	
	
	// -- Non graphic GNode adding functions.
	
	public void addGNode(GNode node)
	{
		children.insert(node, null);
	}
	
	public void addGNodes(Iterable<? extends GNode> iter)
	{
		for (GNode node : iter)
		{
			children.insert(node, null);
		}
	}
	
	// -- Useful helper functions.
	
	// Allows for the bounds of the screen to be recomputed.
	public void updateScrollbars()
	{
		throw new Error("Not yet implemented");
	}
	
	public void addCreationButton()
	{
		throw new Error("Not yet implemented");
	}

	public gui_label addField(String key, Obj o)
	{
		gui_label label = new gui_label(0, Y_MAX, 0, ROW_H);
		label.setText(key);
		label.fitToContents();
		add(label);
		
		addObj(o, label.getX2());
		
		return label;
	}
	
	public void addObj(Obj o)
	{
		addObj(o, 0);
	}
	
	// Just add an single Obj.s
	private void addObj(Obj o, double x)
	{
		o.setX(x);
		o.setY(Y_MAX);
		add(o);
		
		Y_MAX += ROW_H;
		updateXMAX((int)o.getX2());
		
		window.scrollV(Y_MAX);
	}
	
	// Allows the universe to try to update this components X_MAX;
	public void updateXMAX(int x)
	{
		X_MAX = Math.max(X_MAX, x);
		window.scrollH(X_MAX);
	}
	
	/**This overrides the gui_GNode recursive node creation
	 * function so that the child nodes will be Editable instead of static.
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return Returns an Editable GNode of the given dimensions.
	 * 
	 */
	@Override
	protected gui_GNode I_NODE_TYPE(double x, double y, int w, int h)
	{
		return new gui_EditableGNode(x, y, w, h);
	}
}
