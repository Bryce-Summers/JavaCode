package Projects.Math.LessonLogic.Frames;

import BryceMath.Numbers.Equation;

import BryceMath.Structures.Matrix;
import Data_Structures.Structures.Pair;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.room_functional;
import Game_Engine.GUI.Components.large.gui_matrix;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.buttons.gui_functionRoomButton;
import Projects.Math.LessonLogic.Lesson;
import Projects.Math.LessonLogic.ProblemData;
import Projects.Math.functionalRooms.room_DocumentEditor;

/*
 * The Cofactor frame.
 * 
 * Written by Bryce Summers on 7 - 15 - 2013.
 * 
 * Purpose : This class performs a cofactor expansion on a given matrix by the given row and creates
 * 			 a list of functional room change buttons that will route the user
 * 			 to the smaller recursive cofactored determinant problems.
 * 			 Once the user solves all of these problems,
 * 			 then the frame will add up all of the scalars and the user will be done.
 * 
 * Update : In line with my efforts to eliminate the concept of programatic work validation,
 * 			I have removed the answer segment of this Frame.
 * 
 * 			This class now merely displays all of the minors associated with a given row or column.
 * 
 * FIXME : Further improve this class into a full fledged Matrix expression class.
 */

public class Cofactor_frame extends Frame
{	
	// The sub problem room data buttons.
	UBA<gui_functionRoomButton<ProblemData>> buttons;
	
	// An array of cofactor matrices associated with their scalars.
	UBA<ProblemData> minors;
	
	int cofactorRow;

	public Cofactor_frame(int w, int h, ProblemData initial_state, int cofactorRow,
			Lesson lesson)
	{
		super(w, h, initial_state, lesson, cofactorRow);
		this.cofactorRow = cofactorRow;
	}

	@Override
	protected void iVars(ProblemData initial_state, Lesson lesson, int cofactorRow)
	{
		// Extract a list of cofactor matrices.
	
		// Initialize the unbounded array of room change buttons.
		buttons = new UBA<gui_functionRoomButton<ProblemData>>();
		
		// Start the alignment of objects on the left side.
		int x = 0;
		
		Matrix<Equation> matrix_original = initial_state.getMatrix();
		
		// Compute the number of cofactor matrices,
		// which is the number of columns in the original matrix.
		int colNum = matrix_original.getColNum();
		
		// Compute the height of the resultant cofactor matrices.
		// This will be used to draw beautiful scalars.
		int h = (colNum - 1)*50;
		
		
		minors = new UBA<ProblemData>(colNum);

		// -- Create the larger scalar multiplier.
		gui_label l = new gui_label(x, 40, 0, h);
		l.setText(initial_state.getScalar().toString());
		//l.setTextSize(h/2);
		l.fitToContents();
		x += l.getW();
		obj_create(l);
		
		// Create a large left parentheses.
		l = new gui_label(x, 35, 0, h);
		l.setText("(");
		l.setTextSize(h*3/4);
		l.fitToContents();
		l.setW(l.getW()/2);
		obj_create(l);
		x += l.getW();
		
		// For every cofactor matrix, draw its scalar onto the screen and create a cofactoring button.
		for(int c = 0; c < colNum; c++)
		{
		
			// Compute the proper pieces of data.
			Equation scalar = matrix_original.get(cofactorRow, c);
			
			Pair<Matrix<Equation>, Equation> minor = matrix_original.cofactor(cofactorRow, c);
			
			Matrix<Equation> matrix = minor.getKey();
			scalar = scalar.mult(minor.getVal());
			
			// Add the problem data to the proper array.
			ProblemData sub_problem = new ProblemData(matrix, scalar);
			minors.add(sub_problem);
			
			// Generate graphical clues for the user.
			
			gui_label s = new gui_label(x, 40 + h/2, 0, 0);
			s.setText(scalar.toString());
			s.fitToContents();
			x += s.getW();
			obj_create(s);
			
			gui_matrix<Equation> m = new gui_matrix<Equation>(x, 40, matrix, 50);
			m.setStraightBrackets(true);
			m.disable();
			obj_create(m);
			
			
			// Compute the width of this minor on the screen.
			int w = Math.max(m.getW(), 100);

			// Now handle the creation of the sub rooms.
			room_functional<ProblemData> r = new room_DocumentEditor(matrix);

			gui_functionRoomButton<ProblemData> b = new gui_functionRoomButton<ProblemData>(x, 0, w, 35, r);
			buttons.add(b);
			b.setText("Solve");
			obj_create(b);
			
			// Do not force the user to solve trivial problems.
			if(scalar.eq(0) || matrix.getColNum() == 1)
			{
				b.setVisible(false);
			}
			
			
			// Separate all of the cofactored sub matrices.
			x += w;
			
			if(c < colNum - 1)
			{
				gui_label plus = new gui_label(x, 40 + h/2, 50, 0);
				plus.setText("+");
				plus.fitToContents();
				obj_create(plus);
				x += plus.getW();
			}
		}
		
		// Create a large right parentheses.
		l = new gui_label(x, 35, 0, h);
		l.setText(")");
		l.setTextSize(h*3/4);
		l.fitToContents();
		l.setW(l.getW()/2);
		obj_create(l);
		x += l.getW();
		
	}

	// Handle all of the button flags.
	public void update()
	{
		super.update();
		
		for(gui_functionRoomButton<ProblemData> b : buttons)
		{
			if(b.flag())
			{
				b.call();
			}
		}
	}
}
