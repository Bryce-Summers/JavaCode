package Projects.Math.LessonLogic.Frames;

import java.io.PrintStream;

import BryceMath.Calculations.Colors;
import BryceMath.Numbers.Equation;
import BryceMath.Numbers.Expression;
import BryceMath.Structures.Matrix;
import Data_Structures.Structures.List;

import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.Input.textBased.gui_EquationInput;
import Game_Engine.GUI.Components.large.gui_matrix;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.buttons.gui_ObjButton;

import Projects.Math.Dormant;
import Projects.Math.aamathMain;
import Projects.Math.Spr;
import Projects.Math.LessonLogic.Lesson;
import Projects.Math.LessonLogic.ProblemData;
import Projects.Math.LessonLogic.vector_and_encloser;



/*
 * Gaussian frame class. Displays a matrix that is in the process of gaussian elimination.
 * 
 * Written by Bryce Summers on 6 - 5 - 2013.
 * Rewritten by Bryce Summers on 6 - 26 - 2013.
 * Another iteration performed on 7 - 8 - 2013.
 * This class has been torn apart and simplified 8 - 17/18 - 2013
 * 
 * Purpose : This class currently displays a scalar and a Matrix centered on itself to the screen.
 * 			 This class provides an Object selection button for every row, every column,
 *			 every element, and for the matrix.
 *
 *			 This class should be used with the operator_window to display and facilitate the
 *			 creation of problems and their solutions.
 * 
 * 
 */

public class Matrix_frame extends Frame
{	
	// -- Private data.
	
	private List<gui_button> buttons;
	
	// The current configuration of row vectors.
	private gui_matrix<Equation> matrix;
	
	// This acts as a sort of special annotation that displays a scalar in front of a matrix.
	private gui_EquationInput determinant_scalar;
	
	public static int ROW_H = aamathMain.ROW_H;
	
	public static int BANNER_H = aamathMain.BANNER_H;
	
	protected int MATRIX_X;
	protected static int MATRIX_Y = BANNER_H;

	// Stores whether we are showing the determinant borders right now.
	boolean determinant_borders = false;
		
	private gui_button determinant_button;
	private gui_button determinant_scalar_button;// The selection button.
	
	// NOTE : As of 7 - 26 - 2013, I have removed the height requirement from the constructor.
	// THis is because frames are required to be put into gui_lists (lessons).
	
	// -- Constructor.
	public Matrix_frame(int w,
			ProblemData initial_state, 
			Lesson lesson
			)
	{
		super(w, getProperH(initial_state), initial_state, lesson);		
	}
	
	// -- Constructor.
	public Matrix_frame(int w,			
			Matrix<Equation> initial_state, 
			Lesson lesson
			)
	{
		super(w, getProperH(initial_state), new ProblemData(initial_state, Equation.ONE), lesson);
		
	}

	// -- Object instantiation methods.
	@Dormant
	protected void iVars(ProblemData data, Lesson lesson, int extra_val)
	{
		// The createOther labels function needs to know about the lesson.
		this.lesson = lesson;
		
		buttons = new List<gui_button>();
		
		determinant_scalar = new gui_EquationInput(0, 0, 0, ROW_H);
		determinant_scalar.populateInput(data.getScalar().toSerialString());
		determinant_scalar.fitToContents();
		determinant_scalar.hide();
		determinant_scalar.setCollidable(false);
		obj_create(determinant_scalar);
		
		determinant_button = new gui_button(0, 0, 0, BANNER_H);
		determinant_button.setText("Det");
		determinant_button.fitToContents();
		determinant_button.INFO("Toggle Determinant Mode");
		
		// FIXME : If I really do not want this functionality, then I should just remove it.
		//determinant_button.hide();
		
		obj_create(determinant_button);
		
		// Activate the Determinant mode button if the need arises.
		// @Dormant functionality.
		if(data.getShowScalar())
		{
			determinant_button.setFlag(true);
		}
		
				
		// Create the Matrix.
		matrix = new gui_matrix<Equation>(0, MATRIX_Y, data.getMatrix(), ROW_H);
		obj_create(matrix);
		
		// Center the Matrix.
		int data_w = matrix.getW();
		
		// The total size of the extra elements that need to fit on the screen.
		int extra_button_w = ROW_H + left_button.getW()*2;
		
		if(data_w < getW() - extra_button_w)
		{	
			matrix.setX(lesson.getWorld().getW()/2 - data_w/2);
			MATRIX_X = (int) matrix.getX();
		}
		else
		{
			scrollH(data_w + extra_button_w);
			setH(getH() + gui_window.SCROLLBARSIZE);
			MATRIX_X = ROW_H + left_button.getW();
			matrix.setX(MATRIX_X);
		}
		
		//determinant_scalar.setX(MATRIX_X - determinant_scalar.getW());
		determinant_scalar.setX(MATRIX_X + matrix.getW());
		determinant_button.setX(determinant_scalar.getX());
		determinant_scalar.setY(getHalfH() - ROW_H/2 - 1);
		
		// The selection button for the determinant scalar.
		gui_ObjButton b = new gui_ObjButton(0, 0, determinant_scalar.getW(), determinant_scalar.getH());
		determinant_scalar_button = b;
		b.moveTo(determinant_scalar);
		b.setObject(determinant_scalar.getInput());
		b.setDepth(10);
		//b.drawBordersOnlyOnHover();
		b.setRestingColor(Colors.C_GRAY1);
		b.setVisible(false);
		obj_create(b);
		
		
		// Initialize the data selection boxes.
		createSelectionLabels();
		
	}
	
	private int getHalfH()
	{
		return BANNER_H + (getH() - BANNER_H - Spr.gui_borderSize*2)/2;
	}
	
	@Dormant
	private void createScalarLabel(Expression scalar)
	{
		String text = scalar.toCoef();
		
		// Do nothing if the scalar is trivial.
		if(text.length() == 0)
		{
			MATRIX_X = 0;
			return;
		}

		// Create a beautiful scalar label to the left of the Matrix.
		gui_label l = new gui_label(0, BANNER_H, 0, getH() - BANNER_H);
		l.setText(text);
		l.fitToContents();
		obj_create(l);
		
		MATRIX_X = l.getW();
	}
		
	public void createSelectionLabels()
	{
		int x = (int) matrix.getX() - ROW_H;
		
		int row_num = matrix.getData().getRowNum();
		int col_num = matrix.getData().getColNum();

		
		// -- Create the universal selection label.
		gui_ObjButton all_button = new gui_ObjButton(x, 0, ROW_H, BANNER_H);
		all_button.setObject(matrix.getData());
		all_button.setText("All");
		all_button.INFO("Select the entire matrix.");
		buttons.add(all_button);
		obj_create(all_button);
		
		
		// -- Create the individual element expression buttons.
		
		Obj[][] grid = new Obj[row_num][col_num];
		
		for(int r = 0; r < row_num; r++)
		for(int c = 0; c < col_num; c++)
		{
			gui_label box = matrix.getElem(r, c);
			gui_ObjButton b = new gui_ObjButton(MATRIX_X + box.getX(), box.getY(), box.getW(), box.getH());
			b.setObject(matrix.getData().get(r, c));
			
			// Make sure they blend in with the matrix in front of them.
			b.setDepth(10);
			b.drawBordersOnlyOnHover();
			b.setRestingColor(Colors.C_GRAY1);
			obj_create(b);
						
			grid[r][c] = b;
			all_button.addHighlights(b);
			b.INFO("Select the element in row " + r + " and column " + c);
		}

		// Allow mouse clicks to pass through to the buttons.
		matrix.setCollidable(false);
		
		
		// -- Create the Row selection labels.
		
		for(int r = 0; r < row_num; r++)
		{
			gui_ObjButton b = new gui_ObjButton(x, getRowY(r), ROW_H, ROW_H);
			
			// Create enclosed data to aid in elementary column operations.
			vector_and_encloser data = new vector_and_encloser(matrix.getData().getRow(r), true, r, matrix.getData(), determinant_scalar);
			
			// Set this object's data.
			b.setObject(data);
			b.setText("R_{" + r + "}");
			buttons.add(b);
			obj_create(b);
			
			for(int c = 0; c < col_num; c++)
			{
				b.addHighlights(grid[r][c]);
			}
			
			b.INFO("Select the row " + r + " vector");
		}
		
		// -- Create the Col selection labels.
		
		for(int c = 0; c < col_num; c++)
		{
			// Derive geometry form the labels that make up the matrix.
			gui_label box = matrix.getElem(0, c);
			
			gui_ObjButton b = new gui_ObjButton(MATRIX_X + box.getX(), 0, box.getW(), BANNER_H);
			
			// Create enclosed data to aid in elementary row operations.
			vector_and_encloser data = new vector_and_encloser(matrix.getData().getCol(c), false, c, matrix.getData(), determinant_scalar);
			
			// Set this object's data.
			b.setObject(data);
			b.setText("C_{" + c + "}");
			
			// Initialize and group the column_buttons.
			buttons.add(b);
			obj_create(b);

			// Add group highlighting capabilities.
			for(int r = 0; r < row_num; r++)
			{
				b.addHighlights(grid[r][c]);
			}
			
			b.INFO("Select the column " + c + " vector");
		}
		

	}
	
	// Returns the y value of the given Matrix operation row.
	private int getRowY(int rowNum)
	{
		return BANNER_H + rowNum*ROW_H;
	}
	
	// Returns the correct height the this frame should have with the given state.
	public static int getProperH(ProblemData state)
	{
		return getProperH(state.getMatrix().getRowNum());
	}
	
	private static int getProperH(Matrix<Equation> initial_state)
	{
		return getProperH(initial_state.getRowNum());	
	}
	
	// Returns the correct height that this frame should have.
	public static int getProperH(int rownumber)
	{
		return Matrix_frame.BANNER_H + (rownumber*Matrix_frame.ROW_H) + Spr.gui_borderSize*2;
	}
	
	
	// The serialization method for a Matrix frame.
	@Override
	public void serializeTo(PrintStream stream)
	{
		// Generate Problem Data for this frame's current state.
		ProblemData data = new ProblemData(matrix.getData(), determinant_scalar.getInput());
		data.setShowScalar(determinant_borders);
		
		// Serialize the Problem Data.
		data.serializeTo(stream);
		
		stream.println();
	}
	
	@Override
	public String getSerialName()
	{
		return "Matrix Frame";
	}
	
	// -- Handle button display logic.
	
	@Dormant
	public void update()
	{
		super.update();
		
		// Handle button display and button hiding modes.
		//handleButtons();
		
		// Dormant functionality.
		determinant_scalar_button.setW(determinant_scalar.getW());
		if(determinant_button.flag())
		{
			determinant_borders = !determinant_borders;
			matrix.setStraightBrackets(determinant_borders);
			
			if(determinant_borders)
			{
				determinant_scalar.setVisible(true);
				determinant_scalar_button.setVisible(true);
			}
			else
			{
				determinant_scalar.setVisible(false);
				determinant_scalar_button.setVisible(false);
			}
		}
	}
	
	@Dormant
	private void handleButtons()
	{
		// Show all buttons.
		if(mouseInRegion)
		{
			for(gui_button b : buttons)
			{
				b.show();
			}
			
			determinant_button.show();
			
			return;
		}
		
		// Hide all buttons.
		for(gui_button b : buttons)
		{
			b.hide();
		}
		
		determinant_button.hide();
	}
}