package BryceGraphs.gui_components.nodes;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;

import BryceGraphs.ADTs.EditableGNode;
import BryceGraphs.ADTs.Serials.SerialAdjacencyNode;
import BryceGraphs.gui_components.gui_EditableGNode;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.Dict;
import Game_Engine.GUI.Components.Functional.gui_ActionButton;
import Game_Engine.GUI.Components.Input.textBased.gui_StringInput;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.levelEditor.room_fileChooser;

/*
 * GNode Files.
 * 
 * these nodes display the attributes and information regarding files in the file system.
 */

public class gnode_file implements EditableGNode, SerialAdjacencyNode<gnode_file>
{
	private File myFile;
	
	List<gnode_file> children = new List<gnode_file>();

	final room_fileChooser file_chooser;
	
	// Manages the user input of level names.
	gui_StringInput levelName;
	
	// The class that displays the various gui input components.
	private gui_EditableGNode display;
	

	
	// FileChanging button for editing node file properties.
	private class FileChanger extends gui_button
	{
		private boolean calling = false;
		
		public FileChanger(double x, double y, int w, int h)
		{
			super(x, y, w, h);
			
			setText(getGNodeName());
			fitToContents();
			
		}

		public void update()
		{
			super.update();
			
			if(levelName.input_changed())
			{
				// Resize the levelName.
				levelName.fitToContents(levelName.getH());
				// Change the handle description.
				display.setHandleText(levelName.getInput());
			}
			
			// The call.
			if(flag())
			{
				file_chooser.call(getRoom());
				file_chooser.getChooser().refresh();
				file_chooser.openMode();
				calling = true;
				return;
			}
			
			// The Response.
			if(calling)
			{
				calling = false;
				File potential_file = file_chooser.getFile();
				
				if(potential_file == null)
				{
					return;
				}
				
				myFile = potential_file;
								
				String name = myFile.getName();
				setText(name);
				fitToContents();
				// Make sure the user can scroll to view the full button text.
				display.updateXMAX((int) getX2());
			}
		}
	}
	
	// -- Action operations.
	
	private class childAdder implements util.interfaces.Action
	{
		final int h;
		
		public childAdder(int height)
		{
			h = height;
		}
		
		public void action()
		{
			display.GNode("Child", NEW_CHILD(), new Rectangle(0, 0, 0, h));
		}
	}
	
	private class action_edit implements util.interfaces.Action
	{
		public void action()
		{
			/* Do nothing */
		}
	}
	
	private class action_run implements util.interfaces.Action
	{
		public void action()
		{
			/* Do nothing */
		}
	}
	
	// -- Constructor.
	public gnode_file(File file, String name, room_fileChooser room_chooser)
	{
		myFile = file;
		file_chooser = room_chooser;
		
		levelName = new gui_StringInput(0, 0, 0, 20);
		if(name != null)
		{
			levelName.populateInput(name);
		}
		else
		{
			levelName.populateInput("[No Name Yet]");
		}
			
		levelName.fitToContents();

	}

	@Override
	public String getGNodeName()
	{
		return getFileSource();
	}
	
	// Returns a string representing the relative path to the source file.
	private String getFileSource()
	{
		return levelName.getInput();
	}
	
	private String getFileName()
	{
		if(myFile != null)
		{
			return myFile.getName();
		}
		
		return "[Empty Node]";		
	}

	@Override
	public void getGNodeData(Dict<Object> map)
	{
		// We do not need to display the name, 
		// because it is already in the header.		
		map.insert("Source:", getFileName());
		int i = 0;
		
		for(gnode_file gnode: children)
		{
			map.insert("child" + i, gnode);
			i++;
		}
		
	}

	@Override
	public void populateEditField(gui_EditableGNode node, int ROW_H)
	{
		display = node;
		
		// Level name.
		levelName.setH(ROW_H);
		node.addField("Name : ", levelName);
		
		// Source.
		FileChanger b = new FileChanger(0, 0, 0, ROW_H);
		if(myFile != null)
		b.setText(myFile.getName());
		b.fitToContents();
		node.addField("Source : ", b);
		
		
		// -- Children.

		Rectangle bounds = new Rectangle(0, 0, 0, ROW_H);
		
		for(gnode_file file : children)
		{			
			node.GNode("Child", file, bounds);
		}
		
		gui_button child_adder = new gui_ActionButton(bounds, new childAdder(ROW_H));
		child_adder.setText("New Child");
		child_adder.fitToContents();
		node.addObj(child_adder);
		
		gui_button a = new gui_ActionButton(bounds, new action_edit());
		a.setText("Edit Level");
		a.fitToContents();
		node.addObj(a);
		
		// run level.
		a = new gui_ActionButton(bounds, new action_edit());
		a.setText("Run Level");
		a.fitToContents();
		node.addObj(a);
		
		
		//node.addObj(new levelButton());
		
	}
	
	// Returns a new blank child object.
	private gnode_file NEW_CHILD()
	{
		return new gnode_file(null, null, file_chooser);
	}
	
	/*
	private class room_opener_button extends gui_button
	{

		public room_opener_button(String message, int h)
		{
			super(0, 0, 1, h);
			setText(message);
		}
		
		public void update()
		{
			if(flag())
			{
				
			}
		}
	}/*/
	
	public File getFile()
	{
		return myFile;
	}

	@Override
	public String getSerialName()
	{
		return "gnode_file_graph_node";
	}
	
	/** This serializes this node's specific data.
	 *  A graph serialization algorithm is used to serialize the rest of the data.
	 *  
	 *  @param stream the printstream that allows this function to print to the output file.
	 */
	@Override
	public void serializeTo(PrintStream stream)
	{
		stream.println(myFile);
		stream.println(levelName.getInput());
	}
	
	// A corresponding factory for deserializeing serialized data.
	public static gnode_file deserialize(Iterator<String> data, room_fileChooser chooser)
	{
		File file = new File(data.next());
		String name = data.next();
		return new gnode_file(file, name, chooser);
	}
	
	// Any null neighbors will be lost in serialization algorithms.
	@Override
	public Iterable<gnode_file> getNeighbors()
	{
		return children;
	}

	// FIXME : This is very naive.
	// Any null neighbors will be lost in serialization algorithms.
	@Override
	public int getNeighborSize()
	{
		return children.size();
	}

	// Any null neighbors will be lost in serialization algorithms.
	@Override
	public void setNeighbors(Iterable<gnode_file> neighbors)
	{
		children.clear();
		children.append(neighbors);
	}

}
