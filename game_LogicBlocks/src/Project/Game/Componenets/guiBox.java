package Project.Game.Componenets;

import java.io.PrintStream;

import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.Input.gui_IntegerInput;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.buttons.gui_roomReset;
import Project.Editor.Components.Spr;

/*
 * 
 * Written by Bryce Summers on 6 - 21 - 2014.
 * 
 * Purpose : This object specifies the graphical elements that tell the player various data about the game.
 * 
 * 1. Current move count.
 * 2. Goal move count.
 * 3. May contain buttons for the user to navigate away to other screens.
 */

public class guiBox extends gui_window
{

	// -- Private data.
	
	static gui_label goal_label_text;
	static gui_IntegerInput goal_label;
	static gui_label moves_label;
	
	private static int goal;
	private static int score = 0;
	
	private static gui_roomReset reset;
	

	
	// -- Constructor.
	public guiBox(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		
		goal_label_text = new gui_label(0, 0, w, h/2);
		goal_label_text.setText("Goal: ");
		goal_label_text.fitToContents();
		obj_create(goal_label_text);
		
		goal_label = new gui_IntegerInput(goal_label_text.getX2(), 0, w, h/2);
		goal_label.setMaxLen(5);
		goal_label.setCollidable(false);
		obj_create(goal_label);
		
		moves_label = new gui_label(0, h/2, w, h/2);
		moves_label.setText("Score: " + 0);
		moves_label.fitToContents();
		obj_create(moves_label);
		
		score = 0;
		
		int bSize = SpriteLoader.gui_borderSize;
		reset = new gui_roomReset(w - 64, 0, 64 - bSize*2, 64 - bSize*2);
		reset.setImage(Spr.reset);
		obj_create(reset);
	}

	public static void inc_move(int amount)
	{
		score += amount;
		moves_label.setText("Score: " + score);
	}

	public static void setGoal(int val)
	{
		goal = val;
		
		goal_label.populateInput(goal);
	}

	public void allowEditing()
	{
		goal_label.setCollidable(true);
		reset.hide();
	}
	
	public static void disableEditing()
	{
		goal_label.setCollidable(false);
		reset.show();		
	}
	
	@Override
	// Override this to specify the serializing of more specialized objects to a file.
	public void  serializeTo(PrintStream stream)
	{
		stream.println(getSerialName());
		stream.println(getGoal());
	}
	
	public String getSerialName()
	{
		return "obj_guiBox";
	}
	
	public static int getGoal()
	{
		return goal_label.getInput().toInt();
	}
	
	public static int getBorderSize()
	{
		return SpriteLoader.gui_borderSize;
	}

	public static int getScore()
	{
		return score;
	}
}
