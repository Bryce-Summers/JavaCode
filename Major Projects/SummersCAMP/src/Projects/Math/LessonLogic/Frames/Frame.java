package Projects.Math.LessonLogic.Frames;

import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_button;
import Projects.Math.Spr;
import Projects.Math.LessonLogic.Lesson;
import Projects.Math.LessonLogic.ProblemData;

/*
 * Frame class.
 * 
 * Written by Bryce Summers on 6 - 26 - 2013.
 * 
 * This class works closely with the Lesson class to ensure proper flow control in attempts on a solution.
 * 
 * Updated 8 - 16 - 2013 :
 * 		- The dynamic and Static modes have been completely phased out.
 */

public abstract class Frame extends gui_window
{
	protected ProblemData data;
	protected Lesson lesson;
	
	// Should be used to specify which labels should not be shown when this frame is non dynamic.
	protected List<Obj> dynamic_only_components = new List<Obj>();
	
	protected gui_button left_button;
	protected gui_button right_button;
	
	private static final String new_try_string  = "Create a new branch at this location.";
	private static final String next_try_string = "Go to the next branch at this location. ->";
	private static final String previous_try_string = "Go back to the older branch at this location.";
	
	public Frame(int w, int h, ProblemData initial_state, Lesson lesson)
	{
		super(0, 0, w, h);
		iVarsLocal(initial_state, lesson, 0);
	}
	
	public Frame(int w, int h, ProblemData initial_state, Lesson lesson, int extra_val)
	{
		super(0, 0, w, h);
		
		iVarsLocal(initial_state, lesson, extra_val);
	}
	
	private void iVarsLocal(ProblemData initial_state, Lesson lesson, int extra_val)
	{
		// Highlight the clickable components with a dark background color.
		setColor(Spr.backgroundColor);
		
		// Store the original matrix.
		data = initial_state;
		
		// Store a reference to this frame's step list.
		this.lesson = lesson;
		
		iBranchButtons();
		
		iVars(data, lesson, extra_val);
				
	}
	
	private void iBranchButtons()
	{
		// The proper height to center the buttons in the frame.
		int button_y = Spr.gui_borderSize;//getH()/2 - 25;
		int button_h = getH() - Spr.gui_borderSize*2;
				
		right_button = new gui_button(0, button_y, 32, button_h);
		obj_gui_create(right_button);
		right_button.setText(">");
		right_button.INFO(new_try_string);
		right_button.setX(getW() - right_button.getW() - Spr.gui_borderSize);
				
		left_button = new gui_button(Spr.gui_borderSize, button_y, 32, button_h);
		obj_gui_create(left_button);
		left_button.INFO(previous_try_string);
		left_button.setText("<");
	}
		
	protected abstract void iVars(ProblemData initial_state, Lesson lesson, int extra_val);

	@Override
	public void update()
	{
		super.update();
		
		// Nothing we can do about this...
		if(lesson == null)
		{
			return;
		}

		// -- Handle the Tree shifting left button.
		
		// Correct the visibility of the left button.
		left_button.setVisible(!lesson.isLeftNode(this));
		
		if(left_button.flag())
		{
			lesson.shiftLeft(lesson.getIndex(this));

			return;
		}

		// -- Handle the Tree shifting right button.
		
		// Correct the visibility of the right button.
		right_button.setVisible(!lesson.isRightNode(this) && this != lesson.getLast());
		
		int index = lesson.getIndex(this);
		
		// Correct the text to indicate whether the user ever went
		// through with a right branch on this frame.
		if(right_button.isVisible())
		{
			updateRightButtonText(index);
		}
		
		if(right_button.flag())
		{
			lesson.shiftRight(index);
		
			return;
		}
	}	
	
	private void updateRightButtonText(int index)
	{
		if(lesson.has_non_trivial_branchRight(index))
		{
			right_button.INFO(new_try_string);
		}
		else
		{
			right_button.INFO(next_try_string);
		}
	}
	
	public ProblemData getData()
	{
		return data;
	}
}
