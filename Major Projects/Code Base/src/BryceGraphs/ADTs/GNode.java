package BryceGraphs.ADTs;

import util.SerialB;
import Data_Structures.Structures.HashingClasses.Dict;

/*
 * Bryce Graph Node.
 * 
 * This interface will allow me to convert Objects that implement them into GNodes.
 * 
 */

public interface GNode extends SerialB
{
	
	// Used for putting titles on the visual gui_GNodes that will call these classes for information.
	public String getGNodeName();
	
	// Can be used to implement fully lazy evaluation.
	// Should return the primitives arrays, 
	// and references that make up this object for static viewing.
	
	// The user will see associations between names and Objects. This should be sufficient for visually displaying 
	// the structure of an in memory object in a way that the user can understand.
	public void getGNodeData(Dict<Object> map);
	
}
