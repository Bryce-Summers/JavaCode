package Projects.Math.LessonLogic;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.function.Predicate;

import BryceMath.Factories.MatrixFactory;
import BryceMath.Numbers.Equation;
import BryceMath.Numbers.Expression;
import BryceMath.Structures.Matrix;
import BryceMath.Structures.Vector;

import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.Pair;
import Data_Structures.Structures.UBA;

import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.View;

import Game_Engine.GUI.Components.Input.textBased.gui_EquationInput;
import Game_Engine.GUI.Components.Input.textBased.gui_numberTextInput;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.boxes.gui_focusBox;
import Game_Engine.GUI.Components.small.buttons.gui_ObjButton;
import Game_Engine.GUI.Components.small.buttons.gui_functionRoomButton;

import Projects.Math.Dormant;
import Projects.Math.aamathMain;
import Projects.Math.Spr;
import Projects.Math.LessonLogic.Frames.Matrix_frame;
import Projects.Math.LessonLogic.Frames.Expression_Frame;
import Projects.Math.functionalRooms.room_matrixEnter;

import Game_Engine.GUI.Components.large.gui_matrix;

/*
 * The Frame operator class.
 * 
 * Written by Bryce Summers on 8 - 17 - 2013.
 * 
 * Purpose : This is currently envisioned as a very specific class that performs
 * 			 various mathematical operations to create frames in an associated lesson.
 * 
 * 	- This class supports a tabbed menu of buttons that take in Mathematical elements selected by the user
 * 	- and produce new frames as the result of a user defined arithmetic computation described by the elements.
 * 
 * IDEA : This could be genaricized to provide a template for other classes, but this is just a thought;
 * 
 * 
 * Algorithmic Parts : 
 * 
 * 	1. This class handles the geometric management of user input boxes that allow the user to specify arithmetic expressions.
 * 
 *  2. This class then parses the given expressions into mathematical objects.
 *  
 *  3. THis class adds the created data and a representation of the calculation to the main frame screen.
 */

public class operator_window extends gui_window
{
	
	// -- Private Data.
	
	// The lesson that this class is operating upon.
	private Lesson lesson;
	
	// -- Buttons
	
	List<gui_button> primary_buttons = new List<gui_button>();
	List<gui_button> secondary_buttons = new List<gui_button>();
	
	
	// A Button that allows new ProblemData Expression Matrices and scalar components to be created.
	private gui_functionRoomButton<Matrix<Equation>> matrix_create_button;
	private gui_button expression_create_button;
	private gui_button evaluate_button;
	private gui_button cofactor_button;
	private gui_button combine_rows_button;
	private gui_button combine_columns_button;
		
	// Flushes out all of the accumulated operation data.
	private gui_button undo_button;
	private gui_button cancel_button;
	
	// True if in primary mode.
	// False if in secondary mode.
	private boolean primary_mode = true;
	
	// -- Other stuff.
	
	// A list of gui_Matrixs, gui_Vectors, Expressions, and gui_label connectives that compose an expression.
	private List<Obj> elems = new List<Obj>();
	
	public static int ROW_H = aamathMain.ROW_H;
	public static int BANNER_H = aamathMain.BANNER_H;
	
	private int h_initial;
	
	// Stores the first encloser of any given operation.
	private vector_and_encloser enclosing = null;

	private int should_redraw = 0;
	
	private class connective_button extends gui_button
	{
		public connective_button(double x, double y, int w, int h)
		{
			super(x, y, w, h);
		}		
		
		public void update()
		{
			super.update();
			
			if(flag())
			{
				gui_ObjButton.addSelection(getText());
				mode_secondary();
			}
			
			// Handle predicate removal.
			if(elems.isEmpty() || elems.size() > 1)
			{
				gui_ObjButton.removePredicate();
			}
		}
		
		@Override
		public void keyR(int key)
		{
			// -- Keyboard shortcuts.
			if(gui_focusBox.selection != null)
			{
				return;
			}
			
			String text = getText();

			// Handle addition.
			if(key == KeyEvent.VK_ADD || key == KeyEvent.VK_EQUALS)
			{
				if(text.equals("+"))
				{
					setFlag(true);
				}
				return;
			}
			
			// Handle subtraction.
			if(key == KeyEvent.VK_SUBTRACT || key == KeyEvent.VK_MINUS)
			{
				if(text.equals("-"))
				{
					setFlag(true);
				}
				return;
			}
			
			// Handle Division.
			if(key == KeyEvent.VK_SLASH || key == KeyEvent.VK_DIVIDE)
			{
				if(text.equals("/"))
				{
					setFlag(true);
				}
				return;
			}
		
			// Handle Multiplication
			if(key == KeyEvent.VK_8 || key == KeyEvent.VK_MULTIPLY)
			{
				if(text.equals("*"))
				{
					setFlag(true);
				}
				return;
			}
			
			// Handle Left Parens
			if(key == KeyEvent.VK_9)
			{
				if(text.equals("("))
				{
					setFlag(true);
				}
				return;
			}
			
			// Handle Right Parens
			if(key == KeyEvent.VK_0)
			{
				if(text.equals(")"))
				{
					setFlag(true);
				}
				return;
			}
			
			// Handle comma.
			if(key == KeyEvent.VK_COMMA)
			{
				if(text.equals(","))
				{
					setFlag(true);
				}
				return;
			}
		}
	}
	
	// A type of button that replaces 
	private class Replacer extends gui_functionRoomButton<Matrix<Equation>>
	{
		// Private iterator pointer.
		
		IterB<Obj> iter;
		
		// REQUIRES : m must have been obj_created already().
		public Replacer(gui_matrix<Equation> m)
		{
			super(m.getX(), m.getY(), m.getW(), m.getH(), new room_matrixEnter(m.getData()));
			
			// Store a pointer to this Object's place inside of the list.
			iter = elems.getTailIter();
			iter.previous();
			
			setDepth(3);
			
			m.setCollidable(false);
		}
		
		// Poll and provide alignment, resizing, and replacement functionality.
		@Override
		public void update()
		{
			super.update();
			
			if(flag())
			{
				call();
			}
			
			// Replace the corresponding matrix with the new matrix.
			if(input_changed())
			{
				Matrix<Equation> matrix = getInput();
				iter.current().kill();
				
				gui_matrix<Equation> replacement = new gui_matrix<Equation>(getX(), getY(), matrix, ROW_H);
				obj_create(replacement);
				replacement.setCollidable(false);

				iter.replace(replacement);
			}
			
			// maintain proper geometry relative to the element this replaces.
			Obj o = iter.current();
			setX(o.getX());
			setY(o.getY());
			setW(o.getW());
			setH(o.getH());
			
			// Do not allow any lingering replacers.
			if(o.dead(myContainer))
			{
				die();
			}
		}
	}
	
	// Public constructors.

	public operator_window(double x, double y, int w, int h, Lesson lesson_in)
	{
		super(x, y, w, h);
		
		lesson = lesson_in;
		
		iButtons();
		
		h_initial = h;
	}

	private void iButtons()
	{
		int y = Spr.gui_borderSize;
		
		//  -- Create the cancel button.
		cancel_button = new gui_button(0, y, 0, 50);
		cancel_button.fitToContents();
		cancel_button.setX(getW() - cancel_button.getW() - Spr.gui_borderSize - SCROLLBARSIZE);
		cancel_button.setImage(Spr.down_arrow_symbol);
		cancel_button.hide();
		secondary_buttons.add(cancel_button);

		// Make sure the cancel button always works undo button always works.
		cancel_button.setDepth(-5);
		obj_gui_create(cancel_button);

		// -- Create the undo button.
		undo_button = new gui_button(0, y, 0, 50);
		undo_button.fitToContents();
		undo_button.setX(cancel_button.getX() - undo_button.getW());
		undo_button.setImage(Spr.undo_symbol);
		undo_button.hide();
		undo_button.INFO("Delete the last number or matrix.");
		secondary_buttons.add(undo_button);

		// Make sure the undo button always works.
		undo_button.setDepth(-5);
		obj_gui_create(undo_button);
		
		int x = Spr.gui_borderSize;
		
		// Create the Frame creation button.
		matrix_create_button = new gui_functionRoomButton<Matrix<Equation>>(x, y, 100, BANNER_H, new room_matrixEnter());
		matrix_create_button.setText("New [M]atrix");
		matrix_create_button.fitToContents();
		matrix_create_button.INFO("Create a new Matrix.");
		x += matrix_create_button.getW();
		obj_gui_create(matrix_create_button);
		
		// Create the Frame creation button.
		
		expression_create_button = new gui_button(x, y, 0, BANNER_H);
		expression_create_button.setText("New [S]calar");
		expression_create_button.fitToContents();
		expression_create_button.INFO("Create a new scalar: e.g 1, 3.14, i + 21/7, x^2 + 6, etc");
		x += expression_create_button.getW();
		obj_gui_create(expression_create_button);
		
		// -- Now create all of the connective and symbol buttons.

		// Left Paren.
		x = createConnectiveButton("\\(", x, y, false);
		
		// Right Paren.
		x = createConnectiveButton("\\)", x, y, false);

		// Minus
		x = createConnectiveButton("-", x, y, false);
		
		// Plus
		x = createConnectiveButton("+", x, y, true);
		
		// Multiply
		x = createConnectiveButton("*", x, y, true);
		
		// Divide
		x = createConnectiveButton("/", x, y, true);
		
		// Divide
		x = createConnectiveButton(",", x, y, true);

		
		// Instantiate the evaluation button.
		evaluate_button = new gui_button(x, y, 0, BANNER_H);
		evaluate_button.setText("[E]valuate");
		evaluate_button.fitToContents();
		secondary_buttons.add(evaluate_button);
		evaluate_button.setVisible(false);
		evaluate_button.INFO("Compute a result matrix by evaluating the Expression, obeying the order of operations.");
		x += evaluate_button.getW();
		obj_gui_create(evaluate_button);

		// Instantiate the cofactoring button.		
		cofactor_button = new gui_button(x, y, 0, BANNER_H);
		cofactor_button.setText("Cofactor");
		cofactor_button.fitToContents();
		secondary_buttons.add(cofactor_button);
		cofactor_button.setVisible(false);
		cofactor_button.INFO("Performs a cofactor expansion via the chosen matrix and vector.");
		x += cofactor_button.getW();
		obj_gui_create(cofactor_button);
		
		// Instantiate the row combine button.
		x = (int) cofactor_button.getX();
		combine_rows_button = new gui_button(x, y, 0, BANNER_H);
		combine_rows_button .setText("row combine");
		combine_rows_button .fitToContents();
		secondary_buttons.add(combine_rows_button);
		combine_rows_button .setVisible(false);
		combine_rows_button .INFO("Combines a list of elements rowWise in a ne matrix.");
		x += combine_rows_button .getW();
		obj_gui_create(combine_rows_button);
		
		// Instantiate the column combine button.
		combine_columns_button = new gui_button(x, y, 0, BANNER_H);
		combine_columns_button .setText("Column Combine");
		combine_columns_button .fitToContents();
		secondary_buttons.add(combine_columns_button);
		combine_columns_button .setVisible(false);
		combine_columns_button .INFO("Combines components seperated by the , operator.");
		x += combine_columns_button .getW();
		obj_gui_create(combine_columns_button);
	
	}
	
	// Creates a binary operator button for the user to press.
	private int createConnectiveButton(String s, int x, int y, boolean hide)
	{
		// Plus
		gui_button l;
		l = new connective_button(x, y, 0, BANNER_H);
		l.setText(s);
		if(s.equals("/"))
		{
			l.setImage(Spr.divide_symbol);
		}
		else if(s.equals("*"))
		{
			l.setImage(Spr.dot_symbol);
		}
		l.fitToContents();
		if(hide)
		{
			secondary_buttons.add(l);
			l.setVisible(false);
		}
		
		obj_gui_create(l);
		
		return x + l.getW();
	}
	
	// The update loop that drives the operator_window logic.
	@Override
	public void update()
	{
		super.update();
	
		try
		{
			alignElems();
			
			handleMatrixCreationButton();
			handleScalarButton();
			handleUndoButton();
			
			/* 
			 * Selections come from the Connective buttons,
			 * and data buttons in the lesson's frames.
			 */
			handleSelections();
			handleCofactorButton();
			
			// -- The all powerful button.
			handleEvaluation();
			
			if(combine_rows_button.flag())
			{
				handleCombine(true);				
			}
			
			if(combine_columns_button.flag())
			{
				handleCombine(false);				
			}
				
			// Hack for drawing the frame 2 steps after it has been updated.
			if(should_redraw == 1)
			{
				redraw();
				should_redraw = 0;
			}
			else if (should_redraw > 0)
			{
				should_redraw --;
			}
		}
	
		catch(Error e)
		{
			ERROR(e.getMessage());
			return;
		}
	}
	
	// Line up all of the elems and correct the height dynamically.
	private void alignElems()
	{
		int x = 0;
		
		int h = 0;
		
		for(Obj o : elems)
		{
			o.setX(x);
			x += o.getW();
			
			h = Math.max(h, o.getH());
		}
		
		h += BANNER_H + Spr.gui_borderSize*2;
		
		if(h == getH())
		{
			return;
		}
		
		splitHeight(h);		
	}
	
	// -- The logic behind the Matrix create button.
	private void handleMatrixCreationButton()
	{
		// Do nothing if the matrix has not been changed.
		if(!matrix_create_button.input_changed())
		{
			return;
		}
		
		Matrix<Equation> m_new = matrix_create_button.getInput();
		
		gui_ObjButton.addSelection(m_new);
	}
	
	private void handleScalarButton()
	{
		if(expression_create_button.flag())
		{
			// Create an empty unpopulated box.
			gui_EquationInput b = new gui_EquationInput(getElemX2(), getHalfH() - ROW_H/2, 0, ROW_H);
			b.fitToContents();
			elems.add(b);
			obj_create(b);
			
			mode_secondary();
		}
	}
	
	private void handleUndoButton()
	{
		if(undo_button.flag())
		{
			if(!elems.isEmpty())
			{
				elems.rem().kill();
			}
			
			if(elems.isEmpty())
			{
				mode_primary();
			}
		}
				
		
		// Clear all elements.
		if(cancel_button.flag())
		{
			clearElems();
		}
		
	}
	
	// The logic for creating visual elements inside of the operator_window.
	// This is where the matrices, vectors, and scalars get added to the operator window.
	@SuppressWarnings("unchecked")
	private void handleSelections()
	{
		Object o = gui_ObjButton.deqSelection();
		
		// Handle the case in which nothing has happened.
		if(o == null)
		{
			return;
		}

		mode_secondary();
		
		/*
		 * Handle various types of expressions,
		 * these will eventually be evaluated in much the same way that my
		 * Equation input boxes evaluate data,
		 * following the order of operations.
		 */
		
		if(o instanceof Equation)
		{
			Equation exp = (Equation)o;
			
			gui_numberTextInput<Equation> b = new gui_EquationInput(getElemX2(), getHalfH() - ROW_H/2 - 1, 0, ROW_H);

			b.populateInput(exp.toSerialString());
			b.fitToContents();
			elems.add(b);
			obj_create(b);
		}

		// Transform Characters into strings.
		if(o instanceof Character)
		{
			o = "" + (Character) o;
		}
		
		if(o instanceof String)
		{
			String s = (String)o;
			gui_label l = new gui_label(getElemX2(), getHalfH() - ROW_H/2 - 1, 0, ROW_H);
			
			l.setText(s);
			if(s.equals("/"))
			{
				l.setImage(Spr.divide_symbol);
			}
			else if(s.equals("*"))
			{
				l.setImage(Spr.dot_symbol);
			}
			// Makes sure set operations do not have elementary row operation side effects.
			else if(s.equals(","))
			{
				enclosing = null;
			}
			l.fitToContents();
			elems.add(l);
			obj_create(l);
		}
		
		if(o instanceof Matrix<?>)
		{
			Matrix<Equation> matrix = (Matrix<Equation>) o;
			gui_matrix<Equation> m = new gui_matrix<Equation>(getElemX2(), BANNER_H, matrix, ROW_H);
			elems.add(m);
			obj_create(m);
			m.setCollidable(false);
			
			// Create a replacer to allow for matrix modification.
			Replacer r = new Replacer(m);
			obj_create(r);
		}
		
		// NOTE : This code is only read when the user clicks on a row or column selection button.
		if(o instanceof vector_and_encloser)
		{
			vector_and_encloser data = (vector_and_encloser) o;

			// Detect possible swaps and notify the user.
			/*
			 * We use predicates to specify which boxes should change their messages under specific conditions.
			 * In this case, we want them to notify the user of the possibility of a row exchange.
			 */
			
			if(elems.isEmpty())
			{
				gui_ObjButton.setPredicate(new swapPredicate(data), "Swap");
			}
			
			// Handle exchanges.
			if(shouldExchange(data))
			{
				execute_row_exchange(data);
				return;
			}
			
			gui_matrix<Equation> v;
			
			// Handle row vector.
			if(data.isRow())
			{
				v = new gui_matrix<Equation>(getElemX2(), BANNER_H, new Matrix<Equation>(data.getVector()), ROW_H);
			}
			// Handle column vector.
			else
			{
				v = new gui_matrix<Equation>(getElemX2(), BANNER_H, new Matrix<Equation>(true, data.getVector()), ROW_H);
			}
			
			// -- Handle enclosing.
			
			// Cancel the enclosing if both rows and columns are used.
			if(enclosing != null)
			{
				if(enclosing.isRow() != data.isRow())
				{
					enclosing = null;
				}
			}	
			
			else if(elems.isEmpty())
			{
				enclosing = data;
			}
			
			elems.add(v);
			obj_create(v);
			Replacer R = new Replacer(v);
			obj_create(R);
			
		}
		
		handleDimension();
		
	}
	
	private class swapPredicate implements Predicate<gui_ObjButton>
	{
		// Representative vector and encloser.
		vector_and_encloser target;
		
		private swapPredicate(vector_and_encloser target)
		{
			this.target = target;
		}
		
		@Override
		public boolean test(gui_ObjButton t)
		{
			Object o = t.getObject();
			if(!(o instanceof vector_and_encloser))
			{
				return false;
			}
			
			vector_and_encloser other = (vector_and_encloser)o;
			
			return other.getMatrix() == target.getMatrix() &&
				   other.isRow()     == target.isRow();
			
		}

	}
	
	// Returns true if and only if this frame should perform a row exchange with the given data input.
	private boolean shouldExchange(vector_and_encloser data)
	{
		// The user should have already chosen a vector that defines the enclosing.
		// The user should not have entered any intermediate symbols between the first selection.
		// Both vectors should both be row vectors or both be column vectors.
		// The two vectors should not be the same.
		
		return	enclosing != null && elems.size() == 1 &&
				data.getMatrix()  == enclosing.getMatrix() &&
				enclosing.isRow() == data.isRow() && 
				enclosing.getNumber() != data.getNumber();
	}
	
	// Perform window geometry and display logic.
	private void handleDimension()
	{
		// Do nothing if no elements are present.
		if(elems.isEmpty())
		{
			return;
		}
		
		should_redraw = 2;
		
		// Make sure all of the elements in this window are visible vertically.
		int h_last = elems.getLast().getH() + Spr.gui_borderSize*2 + BANNER_H;
		
				
		// Increase the Operator Frame's height if a new element is too tall.
		if(h_last > getH())
		{
			int max_h = lesson.getH()/2;
			if(h_last < max_h)
			{
				setH(h_last);
			}
			else
			{
				setH(max_h);
				scrollV(h_last - Spr.gui_borderSize*2);
			}
			
			// This should push give the operator frame more space on the screen,
			// but it should only take up at most half of the screen.
			if(scrollH == null)
			{
				splitHeight(getH());
			}
						
			updateLessonHeight();
			
			for(Obj o : elems)
			{
				if(o instanceof gui_label)
				{
					o.setY(getHalfH() - o.getH()/2);
				}
			}
			
		}
		
		// Update the world width and allow scrolling if the user has added more data than we can display on the screen.
		int x2 = getElemX2();
		View v = world.getView();
		if(x2 > v.getScreenW())
		{
			v.setWorldW(x2);
			scrollH();
			ySub(gui_window.SCROLLBARSIZE);
			addH(gui_window.SCROLLBARSIZE);
			
			
			//splitHeight(getH() + gui_window.SCROLLBARSIZE);
			// FIXME : I could refactor this class with addH and subH functions.
			/*
			int dh = gui_window.SCROLLBARSIZE;
			setY(getY() - dh);
			setH(getH() + dh);
			
			updateLessonHeight();
			*/
		}
		
		
	}
	
	private void updateLessonHeight()
	{
		lesson.setH((int)(getY() - lesson.getY()));
	}
	
	private void splitHeight(int h)
	{
		int container_h = myContainer.getH(); 
		setY(container_h - Math.min(container_h/2, getH()));
		
		setH(h);
		setY(myContainer.getH() - h);
		lesson.setH((int) (getY() - lesson.getY()));
	}
	

	private int getElemX2()
	{
		if(elems.isEmpty())
		{
			return 0;
		}
		else
		{
			return (int) elems.getLast().getX2();
		}
	}
	
	private int getHalfH()
	{
		int output = BANNER_H + (getH() - BANNER_H - Spr.gui_borderSize*2)/2;
		return output;
	}
	
	// Reverts this operator frame to button pressing mode.
	private void clearElems()
	{		
		// Kill all of the elements
		for(Obj o : elems)
		{
			o.kill();
		}
		
		elems.clear();
		
		mode_primary();
		
		//unscrollH();

	}
	
	private void mode_secondary()
	{
		primary_mode = false;
		
		for(gui_button b : primary_buttons)
		{
			b.hide();
		}
		
		for(gui_button b : secondary_buttons)
		{
			b.show();
		}
		
		handleDimension();
		//matrix_create_button.setGlow(false);
		evaluate_button.flash(5);
	}
		
	private void mode_primary()
	{
		// Revert the color (Do not allow prolonged error messages).
		setColor(Color.WHITE);
		
		primary_mode = true;
		
		for(gui_button b : primary_buttons)
		{
			b.show();
		}
		
		for(gui_button b : secondary_buttons)
		{
			b.hide();
		}
		
		// Eliminate any accidental bars.
		unscrollV();
		unscrollH();
		
		setY(myContainer.getH() - h_initial);
		setH(h_initial);
		lesson.setH((int)(getY() - lesson.getY()));
		
		enclosing = null;
		
		//matrix_create_button.setGlow(true);
		evaluate_button.setGlow(false);
	}
	
	// Allows outside objects to determine if the user is currently
	// crafting an expression in the Operator window.
	public boolean isBeingUsed()
	{
		return !primary_mode;
	}
	
	// Handle Key Board shortcuts.
	public void keyP(int key)
	{
		super.keyP(key);

		if(gui_focusBox.selection != null)
		{
			return;
		}

		if(key == KeyEvent.VK_BACK_SPACE)
		{
			undo_button.setFlag(true);
		}
	
		if(key == KeyEvent.VK_S)
		{
			expression_create_button.setFlag(true);
		}
		
		if(key == KeyEvent.VK_M)
		{
			matrix_create_button.setFlag(true);
		}
		
		if(key == KeyEvent.VK_E && evaluate_button.isVisible())
		{
			evaluate_button.setFlag(true);
		}
		
		if(key == KeyEvent.VK_C)
		{
			cofactor_button.setFlag(true);
		}
	}
	
	// -- Cofactor handling.
	
	// FIXME : This should not jeopardize the integrity of the determinant calculation.
	//	Put negations outside of sub determinant calculations.
	
	
	private void handleCofactorButton()
	{
		if(enclosing == null)
		{
			cofactor_button.hide();
			
			if(!primary_mode)
			{
				combine_rows_button.show();
				combine_columns_button.show();
			}
		}
		else
		{
			cofactor_button.show();
			combine_rows_button.hide();
			combine_columns_button.hide();
		}
		
		// Do not cofactor without a proper enclosing or a button press.
		if(cofactor_button.flag() == false)
		{
			return;
		}
		
		Matrix<Equation> data = enclosing.getMatrix();
		Equation scalar = enclosing.getScalarBox().getInput();
		
		int length = enclosing.getVector().length;
		
		// Handle row cofactoring case.
		if(enclosing.isRow())
		{
			int rowNum = enclosing.getNumber();
			
			clearElems();
			
			for(int c = 0; c < length; c++)
			{
				Pair<Matrix<Equation>, Equation> cofactor = data.cofactor(rowNum, c); 
				
				ProblemData step_new = new ProblemData(cofactor.getKey(), cofactor.getVal().mult(scalar).mult(data.get(rowNum, c)));
				step_new.setShowScalar(true);
								
				step(step_new, null, "A_{" + rowNum + ", " + c + "}");
			}
			
			return;
		}
		
		
		// Handle column vector cofactoring.
		int colNum = enclosing.getNumber();
		
		clearElems();
		
		for(int r = 0; r < length; r++)
		{
			Pair<Matrix<Equation>, Equation> cofactor = data.cofactor(r, colNum); 
			
			ProblemData step_new = new ProblemData(cofactor.getKey(), cofactor.getVal().mult(scalar).mult(data.get(r, colNum)));
			step_new.setShowScalar(true);
			
			step(step_new, null, "A_{" + r + ", " + colNum + "}");
		}
		
		return;
	}
	
	private void handleEvaluation()
	{
		// Only proceed by the command of the evaluate button.
		if(!evaluate_button.flag())
		{
			return;
		}
		
		// Evaluate the results;
		List<Matrix<Equation>> results = getEvaluatedResults();
		
		// -- Now form the next frames.
		
		ProblemData next;
		
		// If we are dealing with a set of operations.
		if(results.size() > 1)
		{
			for(Matrix<Equation> exp : results)
			{
				next = new ProblemData(exp, Equation.ONE);
				step(next, elems);
			}
			
			clearElems();
			return;
		}
		
		// Special cases for dealing with 1 operation sets.
		
		if(enclosing == null || elems.size() == 1)
		{
			next = new ProblemData(results.getFirst(), Equation.ONE);
		}
		else // Special single row or column mutations.
		{
			// Outsource the stepping and operating code.
			execute_matrix_mutation(results.getFirst());
			return;
		}

		step(next, elems);
		
		// Revert the operations frame to its previous state.
		clearElems();
		
	}
	
	private void handleCombine(boolean rows_wanted)
	{
		
		// Parse the resulting expression from the converted_data.
		List<Matrix<Equation>> results = getEvaluatedResults();
		
		if(results.isEmpty())
		{
			throw new Error("Nothing to combine");
		}
		
		// -- Now attempt to combine the results.
		
		// Compute whether we should combine the matrices rowWise or columnWise.
		boolean rowWise = true;
		boolean columnWise = true;
		
		if(rows_wanted)
		{
			columnWise = false;
		}
		else
		{
			rowWise = false;
		}
		
		Iterator<Matrix<Equation>> iter = results.getIter();
		
		// Compute candidate dimensions to check for consistency.
		Matrix<Equation> first = iter.next();
		int M = first.getRowNum();		
		int N = first.getColNum();
		
		while(iter.hasNext())
		{
			Matrix<Equation> matrix = iter.next();
			int m_check = matrix.getRowNum();
			int n_check = matrix.getColNum();
			
			if(m_check != M)
			{
				columnWise = false;
			}
			
			if(n_check != N)
			{
				rowWise = false;
			}
			
			if(!(columnWise || rowWise))
			{
				throw new Error("Dimensions do not agree.");
			}
		}
		
		// Combine all of the rows in order from the input.
		if(rowWise)
		{
			List<Vector<Equation>> rows = new List<Vector<Equation>>();
			
			for(Matrix<Equation> matrix : results)
			{
				rows.append(matrix.rows());
			}
			
			// Row constructor.
			Matrix<Equation> output = new Matrix<Equation>(rows.toArray());
			step(new ProblemData(output, Equation.ONE), elems);
			clearElems();
			return;
		}
		
		// Combine all of the columns in order form the input.
		if(columnWise)
		{
			List<Vector<Equation>> columns = new List<Vector<Equation>>();
			
			for(Matrix<Equation> matrix : results)
			{
				columns.append(matrix.columns());
			}
			
			// Column constructor.
			Matrix<Equation> output = new Matrix<Equation>(true, columns.toArray());
			step(new ProblemData(output, Equation.ONE), elems);
			clearElems();
			return;
		}
		
		throw new Error("ERROR : Combine method should never get here");		
		
	}
	
	// Transforms the entire set of Expressions into a set of Matrix results.
	// This is where all evaluation gets done.
	private List<Matrix<Equation>> getEvaluatedResults()
	{
		// Parse the resulting expression from the converted_data.
		List<Matrix<Equation>> results = new List<Matrix<Equation>>();
		
		// Populate the list of results.
		List<List<Object>> raw = gui_to_BMath(elems);
			
		for(List<Object> l : raw)
		{
			results.add(MatrixFactory.evaluateExpression(l));
		}
		
		return results;
	}
	
	
	//	Converts from this classes graphical representation of the elements
	//	to a pure form that can be parsed by the MatrixFactory class.
	@SuppressWarnings("unchecked")
	private List<List<Object>> gui_to_BMath(List<Obj> elems)
	{
		
		List<List<Object>> output = new List<List<Object>>();
		
		// the current result
		List<Object> result = new List<Object>();
		
		for(Obj o : elems)
		{
			
			// Matrices.
			if(o instanceof gui_matrix<?>)
			{
				gui_matrix<Expression> m = (gui_matrix<Expression>) o;
				
				result.add(m.getData());
				continue;
			}
			
			// Scalars.
			if(o instanceof gui_numberTextInput)
			{
				gui_numberTextInput<Equation> box = (gui_numberTextInput<Equation>)o;
				result.add(box.getInput());
				continue;
			}
			
			// Connectives (Make sure this is last and that there are continue statements above it.)
			if(o instanceof gui_label)
			{
				gui_label l = (gui_label)o;

				// Use the last character. "\\(" --> '(', "/" --> "/" 
				String s = l.getText();
				char c = s.charAt(s.length() - 1);

				// Start parsing a new partition of inputs.
				if(c == ',')
				{
					output.add(result);
					result = new List<Object>();
					continue;
				}

				result.add(c);
				continue;
			}
		}
		
		if(!result.isEmpty())
		{
			output.add(result);
		}
		
		return output;
	}

	
	// -- Stepping functions.
	// -- Frame stepping functions.

	// The should exchange function should be used to quarantine bad data that does not follow this function's assumptions.
	private void execute_row_exchange(vector_and_encloser enclosing2)
	{
		int e1 = enclosing.getNumber();
		int e2 = enclosing2.getNumber();
		
		gui_EquationInput scalarBox = enclosing.getScalarBox();
	
		// -- Handle row exchanges.
		if(enclosing.isRow())
		{
			UBA<Vector<Equation>> rows_new = new UBA<Vector<Equation>>();
			rows_new.append(enclosing.getMatrix().rows());
		
			// Mutate the data with the exchange.
			rows_new.swap(e1, e2);
	
			Matrix<Equation> m_new = new Matrix<Equation>(rows_new.toArray());
	
			// Perform the appropriate correction for the determinant.
			ProblemData data_new = new ProblemData(m_new, scalarBox.getInput().neg());

			// Send the new frame into determinant mode, if necessary.
			data_new.setShowScalar(scalarBox.isVisible());

			
			// Erase any side effects.
			clearElems();
			
			step(data_new, elems, "R_{" + e1 + "} <--> " + "R_{" + e2 + "}");
			return;
		}
		
		// -- Handle column exchanges.
		UBA<Vector<Equation>> columns_new = new UBA<Vector<Equation>>();
		columns_new.append(enclosing.getMatrix().columns());
	
		// Mutate the data with the exchange.
		columns_new.swap(e1, e2);

		Matrix<Equation> m_new = new Matrix<Equation>(true, columns_new.toArray());
		
		// Perform the appropriate correction for the determinant.
		ProblemData data_new = new ProblemData(m_new, scalarBox.getInput().neg());
		
		// Send the new frame into determinant mode, if necessary.
		data_new.setShowScalar(scalarBox.isVisible());
				
		// Erase any side effects.
		clearElems();
		
		step(data_new, elems, "C_{" + e1 + "} <--> " + "C_{" + e2 + "}");
		return;
	}

	// Mutates the given enclosing by the given result, which should be a vector of proper size.
	// REQUIRES : A single row, or single column matrix corresponding to the result of a row or column mutation.
	private void execute_matrix_mutation(Matrix<Equation> result)
	{
		// The box associated with the matrix frame that controls the premutated data.
		gui_EquationInput scalarBox = enclosing.getScalarBox();
		Equation scalar_new = scalarBox.getInput();
		
		// Handle row operations.
		if(enclosing.isRow())
		{
			Vector<Equation>[] matrix_new = enclosing.getMatrix().rows();
			
			int rowNum = enclosing.getNumber();
			//ASSERT(result.getRowNum() == 1);
			
			// Derive the old and new states for the mutation.
			Vector<Equation> r_old = matrix_new[rowNum];
			Vector<Equation> r_new = result.getRow(0);
			
			Equation mult_scalar =  getLinearScalar(r_old, r_new);

			// Perform the mutation.			
			matrix_new[rowNum] = r_new;
			
			String scalar_string = null;
			
			if(mult_scalar != null && !mult_scalar.eq(Equation.ZERO))
			{
				scalar_new = scalar_new.mult(mult_scalar);
				scalar_string = mult_scalar.mult_inverse().toCoef();
			}
			
			// Perform the appropriate correction for the determinant.
			ProblemData output = new ProblemData(new Matrix<Equation>(matrix_new), scalar_new);
			
			// Send the new frame into determinant mode, if necessary.
			output.setShowScalar(scalarBox.isVisible());
			
			String rowString = "R_{" + rowNum + "}";
			
			// Finalize the operation.
			if(scalar_string != null)
			{
				clearElems();
				step(output, null, rowString +  " --> " + scalar_string + " " + rowString);
				return;
			}
			
			step(output, elems, rowString +  " --> " + rowString);
			clearElems();
			return;
		}

		// -- Handle column operations.
		
		Vector<Equation>[] matrix_new = enclosing.getMatrix().columns();
		
		int colNum = enclosing.getNumber();
		
		// Derive the old and new states for the mutation.
		Vector<Equation> c_old = matrix_new[colNum];
		Vector<Equation> c_new = result.getCol(0);
		
		Equation mult_scalar = getLinearScalar(c_old, c_new);

		// Perform the mutation.			
		matrix_new[colNum] = c_new;
		
		String scalar_string = null;
		
		if(mult_scalar != null && !mult_scalar.eq(Equation.ZERO))
		{
			scalar_new = scalar_new.div(mult_scalar);
			scalar_string = mult_scalar.mult_inverse().toCoef();
		}
		
		// Perform the appropriate correction for the determinant.
		// Notice the column constructor.
		ProblemData output = new ProblemData(new Matrix<Equation>(true, matrix_new), scalar_new);
		
		// Send the new frame into determinant mode, if necessary.
		output.setShowScalar(scalarBox.isVisible());
		
		String colString = "C_{" + colNum + "}";
		
		// Finalize the operation.
		if(scalar_string != null)
		{
			clearElems();
			step(output, null, colString +  " --> " + scalar_string + " " + colString);
			return;
		}
		
		step(output, elems, colString +  " --> " + colString);
		clearElems();
		return;
	
	}
	
	/*
	
	private Vector<Expression> getRowVector(int rowNum)
	{
		return matrix.getData().getRow(rowNum);
	}
	
	// row1 = row1 + scalar*row2.
	private void execute_combination(int row1, Expression scalar, int row2)
	{
		Vector<Expression>[] matrix_new = data.getMatrix().rows();

		matrix_new[row1] = getRowVector(row1).add(getRowVector(row2).mult(scalar));
		
		// Perform the appropriate correction for the determinant.
		ProblemData data_new = new ProblemData(new Matrix<Expression>(matrix_new), data.getScalar());
		
		String connective;
		if(scalar.isNegative())
		{
			scalar = scalar.neg();
			connective = " - ";
		}
		else
		{
			connective = " + ";
		}
		
		String operation_description = "R_{" + row2 + "} --> " + "R_{" + row2 + "}" + connective + scalar.toCoef() + "R_{" + row1 + "}";
		
		step(data_new, operation_description);
		
	}
	*/
	
	// FIXME : Perhaps factor this out into some sort of vector computations library
	
	// Returns a scalar factor if the given old vector has been scaled linearly.
	// Returns null if such a scalar does not exist.
	private Equation getLinearScalar(Vector<Equation> r_old, Vector<Equation> r_new)
	{
		// Differing dimensions cannot have a well defined scalar multiple.
		if(r_old.length != r_new.length)
		{
			return null;
		}
		
		int len = r_old.length;
		
		Equation result = null;
		
		for(int r = 0; r < len; r++)
		{
			Equation e_new = r_new.get(r);
			Equation e_old = r_old.get(r);
			
			boolean z_new = e_new.eq(Equation.ZERO);
			boolean z_old = e_old.eq(Equation.ZERO);
			
			// We can derive no information from components that are both zero.
			if(z_new && z_old)
			{
				continue;
			}

			// 0 * x = !0 cannot exist, so we return null; 
			if(z_new && !z_old)
			{
				return null;
			}
			
			// Note : it is guaranteed that e_new != 0.
			Equation check = e_old.div(e_new);
			
			// First check.
			if(result == null)
			{
				result = check;
				continue;
			}
			
			// Everything has been scaled linearly so far...
			if(result.eq(check))
			{
				continue;
			}
			
			// Not all of the components have been scaled linearly.
			return null;
		}
		
		
		return result;
	}

	// The problem data is a result, and the work is the list of gui elements
	//	used in the computation to arrive at the result.
	public void step(ProblemData input, List<Obj> work)
	{
		step(input, work, "");
	}

	@Dormant
	public void step(ProblemData input, List<Obj> work, String description)
	{
		// -- Add a new Annotation Box to the list.
		gui_annotationBox b = new gui_annotationBox(0, 0, world.getW(), room_lesson.ANNOTATION_H);
		b.populateLastBox(description);
		//b.toggle();
		lesson.add(b);
			
		// We have removed the show work functionality, because we think the 
		// information is superfluous and could confuse the user.
		// Perhaps this could be added back in with some way of allowing the user to show the work at will.
		if(work != null && work.size() > 1)
		{
			// Dormant Feature.
			//gui_window w = createWorkFrame(work);
			//lesson.add(w);
		}
		
		// -- Add the next Frame to the list.
		Matrix_frame frame_new = room_lesson.createMatrixFrame(input, lesson);
		
		lesson.add(frame_new);
	}
	
	@Dormant
	public Expression_Frame createWorkFrame(List<Obj> elems)
	{
		
		// Add a frame to show the work.
		Expression_Frame w = room_lesson.createExpressionFrame(getH() - BANNER_H, lesson);
		
		int objs_x_offset = getW()/2 - (int)getElemX2()/2;
		
		while(elems.size() > 0)
		{
			Obj o = elems.deq();
			
			// Transform Expression input boxes into stagnate boxes.
			// FIXME : Make them selection boxes instead.
			if(o instanceof gui_numberTextInput)
			{
				@SuppressWarnings("unchecked")
				gui_numberTextInput<Equation> o1 = (gui_numberTextInput<Equation>)o;
				o1.kill();
				
				gui_label o2;
				o2 = new gui_label(o.getX(), o.getY(), o.getW(), o.getH());
				
				// FIXME : Implement correct element layout managment so that we can print the actual input work with proper widths.
				// Result printing.
				o2.setText(o1.getInput().toString());
				
				// Exact input printing.
				//o2.setText(o1.getText());
				o2.fitToContents();
				o = o2;
			}
			
			o.kill(world);
			// Kill the object for this container only.
			o.setY(o.getY() - BANNER_H);
			o.setX(o.getX() + objs_x_offset);
			w.add(o);
		}	
				
		return w;
	}
	
	// Send a virtual click to the matrix_create_button.
	public void virtual_click_matrix_create()
	{
		matrix_create_button.setFlag(true);
	}
	
}
