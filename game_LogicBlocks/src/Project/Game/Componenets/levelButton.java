package Project.Game.Componenets;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;

import util.FileIO;

import BryceGraphs.ADTs.EditableGNode;
import BryceGraphs.ADTs.Serials.SerialAdjacencyNode;
import BryceGraphs.gui_components.gui_EditableGNode;
import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.Dict;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Project.Game.rooms.room_main;
import Project.Game.rooms.room_puzzle_level;
import BryceGraphs.ADTs.GNode;

/*
 * Level Button.
 * 
 * Written by Bryce Summers on 8 - 20 - 2014.
 * 
 * Level buttons can be thought of as simplified static gnode_files.
 * These buttons(nodes) know their file, a nice text description of their file and their successors.
 */

public class levelButton extends gui_button implements SerialAdjacencyNode<levelButton>, EditableGNode
{
	// -- Private data.
	File file;
	List<levelButton> children = new List<levelButton>();

	gui_EditableGNode container;
	
	// Meta data that stores the goal of the level and the best score the player has gotten.
	int best_player_score;
	final int goal_score;
	
	gui_label p_score;
	gui_label g_score;
	
	public levelButton(File file_in, String description_in, int player_score, int goal_score)
	{
		super(0, 0, 300, 200);
		
		file		= file_in;
		setText(description_in);
		
		// Populate the meta data.
		best_player_score = player_score;
		this.goal_score   = goal_score;
		
		p_score = new gui_label(0, 0, 100, 50);
		p_score.setText("" + best_player_score);
		p_score.fitToContents();
		
		g_score = new gui_label(0, 0, 100, 50);
		g_score.setText("" + goal_score);
		g_score.fitToContents();
		
	}
	
	
	@Override
	public void update()
	{
		super.update();
		
		if(flag())
		{
			room_puzzle_level level = new room_puzzle_level(file, this);
			room_goto(level);
		}
		
		if(container != null)
		{
			container.window.unscroll();
		}
	}

		
	// Serializes this nodes internal data. Use the GraphSerial graph serialization algorithm to 
	// serialize the entire graph with its relational data.
	@Override
	public void serializeTo(PrintStream stream)
	{
		stream.println(file);
		stream.println(getText());
		
		// Meta data.
		stream.println(best_player_score);
		stream.println(goal_score);
	}

	@Override
	public String getSerialName()
	{
		return "levelButtonNode";
	}	
	
	// Deserialization from a stream.
	public static levelButton deserialize(Iterator<String> stream)
	{
		File file = new File(stream.next());
		String Description = stream.next();
		
		int player_score = new Integer(stream.next());
		int goal_score = new Integer(stream.next());
		
		return new levelButton(file, Description, player_score, goal_score);
	}
	
	@Override
	public int getNeighborSize()
	{
		return children.size();
	}

	@Override
	public Iterable<levelButton> getNeighbors()
	{
		return children;
	}

	@Override
	public void setNeighbors(Iterable<levelButton> neighbors)
	{
		children.clear();
		children.append(neighbors);
	}


	// GNode interface functions for using this will a tree viewer.
	@Override
	public String getGNodeName()
	{
		return getText();
	}


	// Populates the GNode association data with the children.
	@Override
	public void getGNodeData(Dict<Object> map)
	{
		int child_num = 0;
		for(GNode node : getNeighbors())
		{
			map.insert("Child " + child_num, node);
			child_num++;
		}
	}


	
	@Override
	public void populateEditField(gui_EditableGNode node, int ROW_H)
	{
		// Add the level button and resize it.
		node.add(this);
		setX(0);
		setY(100);
		setW(node.getW() - SpriteLoader.gui_borderSize*2);
		setH(50);
		
		// Tell the graphic tree nodes what this levelButton's children are.
		node.addGNodes(getNeighbors());
		//node.window.unscroll();
		
		container = node;
		
		
		node.addField("Goal : <", g_score);
		node.addField("Player : ", p_score);
		
	}

	// Allow the outside world to update these scores.
	public void newScore(int score)
	{
		if(best_player_score == -1 || score < best_player_score)
		{
			setPlayerScore(score);
		}
	}
	
	// Update the score and save the mutated game state to the file.
	private void setPlayerScore(int score)
	{
		best_player_score = score;
		p_score.setText("" + score);
		p_score.fitToContents();
		
		((room_main)getRoom()).serializeSavedGame();
	}

}
