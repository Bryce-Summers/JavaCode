package Projects.Math.LessonLogic;

import java.io.File;

import util.Web;

import Game_Engine.Engine.Objs.room_functional;

import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.boxes.gui_checkBox;
import Game_Engine.GUI.Components.communication.gui_yesNo;
import Game_Engine.GUI.Components.large.gui_fileChooser;

import Projects.Math.aamathMain;
import Projects.Math.Spr;
import Projects.Math.LessonLogic.Frames.Expression_Frame;
import Projects.Math.LessonLogic.Frames.Matrix_frame;

/*
 * The room lesson class.
 * 
 * Written by Bryce Summers on 7 - 10 - 2013.
 * 
 * Updated 8 - 13 - 2013.
 * 		- I remodeled the menu bar.
 * 		- The tabbed interface was phased out in favor of a streamlined problem specific buttons.
 * 		- The Top banner buttons now do the following :
 * 			[Last room], [Export Bryce Document Format or PNG], [Go to the help archives],
 * 			[Previous Problem], [Next Problem].
 * 
 * Purpose. This class abstracts away the look, feel, and tabbed interface from the lesson classes.
 * 
 * Update 8 - 19 - 2013.
 * 		- I have eliminated hard coded level functions. All levels will now be entirely external.
 */


public abstract class room_lesson extends room_functional<ProblemData>
{
	
	// -- Private data.

	// The graphic list of frames holding generated data.
	protected Lesson lesson;
	
	// The toolbox that he user will use to mutate the frames. 
	private operator_window operator;
	
	private gui_fileChooser file_chooser;
		
	// The Top Ribbon buttons.
	gui_button menu_button;
	gui_yesNo yesNo_box;
	
	// FIXME : I get the feeling that this button could be removed.
	gui_checkBox export_button;
	gui_button help_button;
	gui_button problem_prev, problem_next;
	
	// The beautiful button that sits in the middle of the screen.
	gui_button matrix_create_button;
	
	
	public static final int MENU_H = 50;
	public static final int ANNOTATION_H = 75;
	public static final int OPERATOR_H = operator_window.BANNER_H + Spr.gui_borderSize*2;
	public static final int LESSON_DEPTH = -10;
	

	private static final String new_problem_string = "New-->";
	private static final String next_problem_string = "Next-->";
	
	// Note : There is no constructor for starting in a room of this type.

	// Every lesson needs to specify a solution function.
	
	@Override
	public void iObjs()
	{
		//set_dimensions(1200, 800);

		// First create the top of the screen ribbon.
		createTopBanner(MENU_H);
		
		iPlayScreen();
		
		int w = getW();
		
		// The file chooser will be displayed on top of the ribbon.
		file_chooser = new gui_fileChooser(0, 0, w, getH(), "Saves");
		obj_create(file_chooser);
		file_chooser.hide();
		file_chooser.addExtensions(aamathMain.EXT_SAVE);
		file_chooser.saveMode();
		
	}
	
	private void createTopBanner(int height)
	{
		
		int w = getW();
		
		int x = 0;
		
		// Update this when redesigning the top ribbon buttons.
		int button_w = w/5;
		
		// Create the useful return button.
		/*
		gui_returnButton<ProblemData> b3;
		b3 = new gui_returnButton<ProblemData>(x, 0, w/6, height, this);
		b3.setText("Back");
		obj_create(b3);
		x += b3.getW();
		//*/
		
		// This button will direct the user to the main menu.
		menu_button = new gui_button(x, 0, button_w, height);
		menu_button.setText("Menu");
		menu_button.INFO("Go back to the main menu.");
		obj_create(menu_button);
		x += menu_button.getW();
		
		// Create the precautionary yes No box.
		yesNo_box = new gui_yesNo(getW()/2 - w/2, getH()/2 - 64, w, 190,
				"You will lose all saved work. " +
				"Are you sure you wish to continue?",
				menu_button, false);
		obj_create(yesNo_box);
		yesNo_box.hide();
		yesNo_box.setDepth(-Integer.MAX_VALUE);
		yesNo_box.disableCloseButton();
		
		
		// This button will currently export the data as a Bryce Document Format file.
		export_button = new gui_checkBox(x, 0, button_w, height);
		export_button.setMessages("Export", "Cancel");
		obj_create(export_button);
		export_button.INFO("Save the document as a BDF file or PDF.");
		x += export_button.getW();
		
		// THis button will send the user to the help archives.
		//File saveDir = aamathMain.DIR_HELP;
		//room_open room_help = new room_open(saveDir);
		help_button = new gui_button(x, 0, button_w, height);
		help_button.setText("Help");
		help_button.INFO("View Internet Tutorials.");
		obj_create(help_button);
		x += help_button.getW();
		
		
		// This button will currently export the data as a Bryce Document Format file.
		problem_prev = new gui_button(x, 0, button_w, height);
		problem_prev.setText("<--Previous");
		problem_prev.disable();
		problem_prev.INFO("View the previous problem computations.");
		obj_create(problem_prev);
		x += problem_prev.getW();
		
		// This button will currently export the data as a Bryce Document Format file.
		problem_next = new gui_button(x, 0, button_w, height);
		problem_next.setText(new_problem_string);
		problem_next.disable();
		obj_create(problem_next);
		x += problem_next.getW();

		matrix_create_button = new gui_button(getW()/2 - 150, getH()/2 - 75, 300, 150);
		matrix_create_button.setText("Create Initial Matrix");
		matrix_create_button.setDepth(LESSON_DEPTH - 1);
		matrix_create_button.INFO("Start doing math by creating a Matrix");
		obj_create(matrix_create_button);
	}
	
	public void update()
	{
		super.update();
		
		if(export_button.query())
		{
			handleFileChooser();
		}		
				
		// Handle the export button.
		if(export_button.input_changed())
		{
			if(export_button.query())
			{			
				lesson.hide();
				operator.hide();
				file_chooser.show();
			}
			else
			{
				lesson.show();
				operator.show();
				file_chooser.hide();
			}
			
			return;
		}
	
		// Handle the Previous problem button.
		
		problem_prev.setEnabled(lesson.getCurrentRootBranch() > 0);
		
		// Shift the lessons root left.
		if(problem_prev.flag())
		{
			lesson.shiftLeft(0);
		}
		
		// Handle the Previous problem button.
		
		int root_num = lesson.getRootNum();
		int current_branch = lesson.getCurrentRootBranch();
		
		problem_next.setEnabled(current_branch < root_num);
		
		if(current_branch > root_num - 2)
		{
			problem_next.setText(new_problem_string );
			problem_next.INFO("Begin a new list of computations.");
		}
		else
		{
			problem_next.setText(next_problem_string);
			problem_next.INFO("View the next list of computations.");
		}
		
		// Shift the lessons root left.
		if(problem_next.flag())
		{
			lesson.shiftRight(0);
		}
		
		if(matrix_create_button.flag())
		{
			operator.virtual_click_matrix_create();
			matrix_create_button.disable();
			return;
		}
		
		if(lesson.size() > 0 || operator.isBeingUsed() || file_chooser.isVisible())
		{
			matrix_create_button.hide();
		}
		else
		{
			matrix_create_button.show();
		}
		
		// Youtube link.
		if(help_button.flag())
		{
			Web.viewWebsite("https://www.youtube.com/channel/UCbDKxCh70C4wnAYVjRlw3UA/feed?view_as=public");
		}
		
		// -- Logic for safely exiting the document editing page with a saved work warning.
		if(menu_button.flag())
		{
			yesNo_box.show();
		}
		
		if(yesNo_box.isVisible())
		{
			if(yesNo_box.flagYes())
			{
				room_goto("room_menu");
				return;
			}
			if(yesNo_box.flagNo())
			{
				yesNo_box.hide();
			}
		}
		
	}
	
	// Handles the logic behind the file chooser.
	private void handleFileChooser()
	{
		// -- Handle the File chooser's cancel operation.
		if(file_chooser.getCancel())
		{
			export_button.toggle();
			return;
		}
		
		// -- Handle the File Chooser's file selected operation.
		if(file_chooser.flag())
		{
			File savefile = file_chooser.getFile();
			lesson.saveData(savefile);
			
			// Refresh the file chooser.
			file_chooser.refresh();
			
			export_button.toggle();
			
			return;
		}
	}
	
	protected void iPlayScreen()
	{
		if(lesson != null)
		{
			throw new Error("This should not happen.");
		}

		// Start the lesson.
		createLesson();
	}
	
	protected gui_annotationBox createAnnotationBox()
	{
		return new gui_annotationBox(0, 0, getW() - Spr.gui_borderSize*2, ANNOTATION_H);
	}
	
	public static Matrix_frame createMatrixFrame(ProblemData starting_state, Lesson lesson)
	{
		return new Matrix_frame(lesson.getW() - Spr.gui_borderSize - Lesson.SCROLLBARSIZE, starting_state, lesson);
	}
	
	public static Expression_Frame createExpressionFrame(int h, Lesson lesson)
	{
		return new Expression_Frame(lesson.getW() - Spr.gui_borderSize - Lesson.SCROLLBARSIZE, h);
	}
	
	// Instantiates the lesson document component.
	protected void createLesson()
	{
		// -- The list of Gaussian Elimination Steps.
		lesson = new Lesson(0, MENU_H, getW(), getH() - MENU_H - OPERATOR_H);
		obj_create(lesson);
		
		operator = new operator_window(0, getH() - OPERATOR_H, getW(), OPERATOR_H, lesson);
		obj_create(operator);
		operator.setDepth(LESSON_DEPTH);
	}
	
}
