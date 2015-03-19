package Game_Engine.GUI.Components.large;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.Obj;

/*
 * The Graphic user interface tree class.
 * 
 * Written by Bryce Summers on 7 - 25 - 2013.
 * 
 * Purpose : Allows a normal graphic user list to branch at every point in the list,
 * 			 so each vertical step can have an arbitrary number of branches that can be cycled through.
 * 
 * Updated : 8 - 16 - 2013 : The root has been transformed into an UBA of tree nodes, instead of just one tree node.
 * 							 This allows every tree to have an arbitrarily number of completely unique paths,
 * 							 whereas in the old Tnode root representation, the first node could not be changed.
 * 							 The motivation behind this was to enable me to use trees to combine multiple
 *							 problems in Linear Algebra assignments.
 *
 *							 This also has the added benefit of never having the root be null.
 */

// FIXME : The rem() and deq() functions in the list class are currently not validly supported.

public class gui_tree extends gui_list
{
	// Tree nodes.
	private class TNode
	{
		Obj myObj;
		
		UBA<TNode> branches = new UBA<TNode>();
		
		// Branches have access to the gui_list operations list.
		
		// No branch.
		int current_branch = -1;
		
		// -- Constructor.
		private TNode(Obj o)
		{
			myObj = o;
		}
		
		private void add(Obj o)
		{
			branches.add(new TNode(o));
			
			// Update the current branch.
			current_branch = branches.size() - 1;
		}
		
		private TNode getNextInPath()
		{
			if(current_branch == -1 || current_branch == branches.size())
			{
				return null;
			}
			
			return branches.get(current_branch);
		}
		
		// Returns whether this branch is the end of the line.
		private boolean hasNextInPath()
		{
			if(current_branch == -1 || current_branch == branches.size())
			{
				return false;
			}
			
			return true;
		}
		
		// A safe branching mechanism.
		private void branchLeft()
		{
			if(current_branch > 0)
			{
				current_branch--;
			}
		}
		
		// A safe branching mechanism. It can shift as far right as a new fresh null branch.
		private void branchRight()
		{
			if(current_branch < branches.size())
			{
				current_branch++;
			}
		}
		
		private Obj getObj()
		{
			return myObj;
		}
		
		private boolean isLeftNode()
		{
			return current_branch < 1;
		}
		
		private boolean isRightNode()
		{
			return current_branch >= data.size() - 1;
		}
	}
	
	// The root is never null.
	private UBA<TNode> root_nodes = new UBA<TNode>();
	private int current_branch = 0;

	public gui_tree(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public gui_tree(Rectangle r)
	{
		super(r);
	}
	
	// Let the list do the grunt work, while we keep a reference of the objects to store inside of a nifty tree structure.
	public void add(Obj o)
	{
		super.add(o);
		
		// Existing branch.
		if(current_branch < root_nodes.size())
		{
			getCurrentLast().add(o);
		}
		else
		{
			root_nodes.add(new TNode(o));
		}
	}
	
	// Allows users to branch the tree structure to the left.
	public void shiftLeft(int depth)
	{
		// FIXME : These redraws should be further down in the pipeline once we stop updating the object list at every step.
		redraw();
		if(depth == 0)
		{
			current_branch--;
		}
		else
		{
			get(depth - 1).branchLeft();
		}
		updateData(getCurrentPath());

		// Save this data for serialization.
		super.operations.add("ShiftL");
		super.operations.add(depth);
	}
	
	// Allows users to branch the tree structure to the right.
	public void shiftRight(int depth)
	{
		redraw();
		if(depth == 0)
		{
			current_branch++;
		}
		else
		{
			get(depth - 1).branchRight();
		}

		updateData(getCurrentPath());
		
		// Save this data for serialization.
		super.operations.add("ShiftR");
		super.operations.add(depth);
	}
	
	private TNode get(int depth)
	{
		TNode root = getRoot();
		
		if(root == null)
		{
			throw new Error("Cannot retrieve data from Empty Trees");
		}
		
		TNode output = root;
		
		// The root node counts as depth 0.
		// So we start counting at 0.
		for(int i = 0; i < depth; i++)
		{
			output = output.getNextInPath();
			if(output == null)
			{
				throw new Error("Index : " + depth + " is too deep for this location on this tree.");
			}
		}
		
		return output;
	}
	
	private TNode getCurrentLast()
	{
		TNode root = getRoot();

		if(root == null)
		{
			return null;
		}

		TNode output = root; 

		while(output.hasNextInPath())
		{
			output = output.getNextInPath();
		}
		
		return output;
	}
	
	private UBA<Obj> getCurrentPath()
	{
		UBA<Obj> output = new UBA<Obj>();
		
		TNode root = getRoot();
		
		if(root == null)
		{
			return output;
		}
		
		// Insert all of the nodes.
		TNode node = root;
		output.add(node.getObj());
		
		while(node.hasNextInPath())
		{
			node = node.getNextInPath();
			output.add(node.getObj());
		}
		
		return output;
	}
	
	// Looks for a node in the current path.
	// Returns null if there is no last node in the path.
	private TNode getNodeInPath(Obj o)
	{
		TNode root = getRoot();
		
		if(root == null)
		{
			return null;
		}
		
		TNode node = root;
		if(o == root.getObj())
		{
			return root;
		}
		
		while(node.hasNextInPath())
		{
			node = node.getNextInPath();
			if(node.getObj() == o)
			{
				return node;
			}
		}
		
		return null;
	}
	
	public boolean isLeftNode(Obj o)
	{
		TNode onode = getNodeInPath(o);
		
		if(onode == null)
		{
			return false; //throw new Error("An obj that is not in the current path is requesting updates! O Node!");
		}
		
		return onode.isLeftNode();
	}
	
	public boolean isRightNode(Obj o)
	{
		TNode onode = getNodeInPath(o);
		
		if(onode == null)
		{
			return false;
			//throw new Error("An obj that is not in the current path is requesting updates! O Node!");
		}
		
		return onode.isRightNode();
	}
	
	public void clear()
	{
		super.clear();
		
		root_nodes.clear();
	}
	
	// Returns true if the length of the path created by branching right at the indicated depth is greater than 0.
	// This can be used to see if the given branch contains extra data not visible without branching.
	public boolean has_non_trivial_branchRight(int depth)
	{

		TNode node = get(depth);
		
		UBA<TNode> branches = node.branches;
		int current_branch = node.current_branch;
		
		if(current_branch + 1 > branches.size() - 1)
		{
			return false;
		}
		
		return branches.get(current_branch + 1) != null;

	}

	// Returns true if the length of the path created by branching left at the indicated depth is greater than 0.
	// This can be used to see if the given branch contains extra data not visible without branching.
	public boolean has_non_tivial_branchLeft(int depth)
	{
		TNode node = get(depth);
		return !node.isLeftNode();
	}
	
	private TNode getRoot()
	{
		// Current root exists.
		if(current_branch < root_nodes.size())
		{
			return root_nodes.get(current_branch);
		}
		
		// Current root does not exist yet.
		return null;
	}
	
	// Methods to derive information about the current root branching.
	public int getRootNum()
	{
		return root_nodes.size();
	}
	
	public int getCurrentRootBranch()
	{
		return current_branch;
	}
	
	/*
	 * Trees override te get index function from gui_lists,
	 * because we start with an imaginary invisible root node that branches to an arbitrary
	 * number of entirely distinct paths.
	 */
	@Override
	public int getIndex(Obj o)
	{
		return super.getIndex(o) + 1;
	}
	
	@Override
	public void rem()
	{
		throw new Error("Not supported");
	}

	@Override
	public void deq()
	{
		throw new Error("Not supported");
	}
	
}
