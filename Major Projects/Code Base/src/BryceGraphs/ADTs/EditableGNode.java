package BryceGraphs.ADTs;

import BryceGraphs.gui_components.gui_EditableGNode;

/*
 * Defines GNodes that can both display their data, but also allow for modification and user interaction.
 */

public interface EditableGNode extends GNode
{
	
	/** 
	 * Provides inline input Objects that will be displayed inside of gui_EditableNodes.
	 * These input boxes should have their origin in the top left corner and should be of the given height.
	 * References to these input boxes should be retained by the Editable GNodes, so they can perform update logic 
	 * such as querying whether buttons are pressed.
	 * 
	 * @param node
	 * This method allows the gui_EditableGNode to give Editable GNodes a reference to itself.
	 * This will be useful for giving the node rampant control over how the onscreen logic behaves.
	 * Please see the helper functions located inside of the gui_EditableGNode class. 
	 *  
	 */
	public void populateEditField(gui_EditableGNode node, int ROW_H);
	
}
