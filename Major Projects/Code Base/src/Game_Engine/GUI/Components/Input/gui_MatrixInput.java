package Game_Engine.GUI.Components.Input;

import static BryceMath.Factories.MatrixFactory.m_ex_col;
import static BryceMath.Factories.VectorFactory.v;

import java.awt.Color;
import java.awt.event.KeyEvent;

import util.interfaces.Generator;

import BryceMath.Numbers.Equation;
import BryceMath.Structures.Matrix;
import BryceMath.Structures.Vector;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.Input.textBased.gui_EquationInput;
import Game_Engine.GUI.Components.large.gui_grid;
import Game_Engine.GUI.Components.large.gui_list;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.boxes.gui_focusBox;

//-- Useful Number construction functions.

/*
 * The Expression Matrix input class.
 * 
 * This allows for users to input custom problems.
 */

public class gui_MatrixInput extends Obj implements Input<Matrix<Equation>>
{
	
	private static int MAX_ROWS    = 10;
	private static int MAX_COLUMNS = 10;
	
	// -- Private data.
	
	gui_grid<gui_EquationInput>  matrix;
	
	final String DEFAULT_INPUT = "0";
	
	final Matrix<Equation> original_matrix;

	// Modification buttons and their list enclosure.
	
	gui_list button_list;
	gui_grid<gui_EquationInput> matrixGrid;
	
	gui_label col_num_label, row_num_label;
	gui_IntegerInput column_num_box, row_num_box;
	
	// Operation Buttons.
	gui_button transpose_button;
	gui_button clear_button;
	gui_button identity_button;
	gui_button conjugate_button;
	gui_button inverse_button;
	gui_button determinant_button;
	gui_button delete_button;
	
	gui_button shift_left_button;
	gui_button shift_right_button;
	
	
	final private int ROW_H;
	
	public gui_MatrixInput(int x, int y, int row_h)
	{
		super(x, y);
		ROW_H = row_h;
		
		original_matrix = null;

	}
	
	
	public gui_MatrixInput(int x, int y, int row_h, Matrix<Equation> matrix)
	{
		super(x, y);
		ROW_H = row_h;
		original_matrix = matrix;
	}
	
	
	@Override
	public void initialize()
	{
		super.initialize();
		
		
		setCollidable(false);
		setVisible(false);
		
		button_list = new gui_list(getX(), getY(), 250, (int) (myContainer.getH() - getY()));
		obj_create(button_list);
		populateList(button_list);
		button_list.makeTransparent();
		
		matrixGrid = new gui_grid<gui_EquationInput>(button_list.getX2(), button_list.getY(), myContainer.getW() - button_list.getW(), button_list.getH(),
				new BoxCreator(),
				true, ROW_H);
		obj_create(matrixGrid);
				
		
		clearInput();
		
	}
	
	private class BoxCreator implements Generator<gui_EquationInput>
	{

		@Override
		public gui_EquationInput create()
		{
			return I_BOX();
		}
		
	}

	private void populateList(gui_list L)
	{
		int bsize = SpriteLoader.gui_borderSize;
			
		// Row setting.
		gui_window W = new gui_window(0, 0, L.getW() - bsize*2, 50);
		L.add(W);
		gui_label l = new gui_label(-bsize, -bsize, 200, 50);
		l.setText("Row #");
		row_num_label = l;
		l.fitToContents();
		int label_w = l.getW();
		W.obj_create(l);
		
		row_num_box = new gui_IntegerInput(l.getX2(), l.getY(), 50, 50);
		row_num_box.disableNegatives();
		row_num_box.setText("1");
		row_num_box.setMaxLen(1);
		W.obj_create(row_num_box);
		row_num_box.INFO("Set the number of rows in the matrix: [1, 9]");
		
		// Column setting.
		W = new gui_window(0, 0, L.getW() - bsize*2, 50);
		L.add(W);
		l = new gui_label(-bsize, -bsize, 200, 50);
		l.setText("Col #");
		l.setW(label_w);
		col_num_label = l;
		W.obj_create(l);
		
		column_num_box = new gui_IntegerInput(l.getX2(), l.getY(), 50, 50);
		column_num_box.disableNegatives();
		column_num_box.setText("1");
		column_num_box.setMaxLen(1);
		W.obj_create(column_num_box);
		column_num_box.INFO("Set the number of columns in the matrix: [1, 9]");

		// Minor modification buttons.
		clear_button 		= OP_BUTTON("clear_button", L);
		clear_button.INFO("Set every selected element to 0.");

		delete_button		= OP_BUTTON("delete", L);
		delete_button.INFO("Removes the selected elements from the matrix.");
		
		shift_left_button 	= OP_BUTTON("Shift Left", L);
		shift_left_button.INFO("Shift the selected row or column left or up.");
		
		shift_right_button	= OP_BUTTON("Shift Right", L);
		shift_right_button.INFO("Shift the selected row or column right or down.");
		
		
		// Major modification buttons.
		transpose_button 	= OP_BUTTON("Transpose", L);
		transpose_button.INFO("Transpose the current matrix.");
		
		inverse_button 		= OP_BUTTON("Invert", L);
		inverse_button.INFO("Invert the current input if possible.");
		
		determinant_button  = OP_BUTTON("Determinant", L);
		determinant_button.INFO("Compute the Determinant of a square matrix.");
		
		conjugate_button 	= OP_BUTTON("conjugate", L);
		conjugate_button.INFO("Conjugates every seleted element.");
		
		identity_button 	= OP_BUTTON("Identity", L);
		identity_button.INFO("Sets every element on the diagonal to 1 and all other elements to 0.");

	}


	// Construct a button to the right of this one. 
	private gui_button OP_BUTTON(String str, gui_list container)
	{
		gui_button output = new gui_button(0, 0, container.getWorld().getW(), 50);
		output.setText(str);
		container.add(output);
		
		return output;
	}
	
	// Resets this matrix.
	@Override
	public void clearInput()
	{
	
		// Clear to original.
		if(original_matrix != null)
		{
			populateInput(original_matrix);
			getBox(0).toggle();
			return;
		}
		
		// Clear all of the populations.
		for(int r = 0; r < num_rows(); r++)
		for(int c = 0; c < num_columns(); c++)
		{	
			gui_EquationInput box;
			box = getBox(r, c);
			box.clearInput();
			box.setDefaultEquation(Equation.ZERO);
		}
		
		getBox(0).toggle();
	}
	
	// Populate a trivial 1 by 1 matrix from a scalar. 
	public void populateInput(Equation eq)
	{
		Equation[][] data = new Equation[1][1];
		data[0][0] = eq;
		populateInput(data);
	}
	
	// Populate the input from a row major array.
	public void populateInput(Equation[][] data)
	{
		populateInput(new Matrix<Equation>(data));
	}
	
	// Forces this input box to represent an input that would derive the given matrix.
	public void populateInput(Matrix<Equation> matrix)
	{
		// Remove all of the previous data, except for the top left box.
		while(num_columns() > 1)
		{
			matrixGrid.remCol();
		}
		
		while(num_rows() > 1)
		{
			matrixGrid.remRow();
		}
		
		// Update the row and column values.
		int rowNum = matrix.getRowNum();
		int colNum = matrix.getColNum();

		// -- Create new blank boxes.
		
		for(int c = 0; c < colNum - 1; c++)
		{
			matrixGrid.addCol();
		}
		
		for(int r = 0; r < rowNum - 1; r++)
		{
			matrixGrid.addRow();
		}
		
		// Populate all of the boxes with the matrix data.
		for(int r = 0; r < num_rows(); r++)
		for(int c = 0; c < num_columns(); c++)
		{		
			getBox(r, c).populateInput(matrix.get(r, c).toSerialString());
		}

	}

	@Override
	public boolean input_changed()
	{
		throw new Error("Implement Me!");
	}

	@Override
	public boolean query_changed()
	{
		throw new Error("Implement Me!");
	}

	// Align all of the myriad of boxes.
	
	@Override
	public void update()
	{
		// Align and update the visibility of the various boxes.

		// FIXME : I might need to udate the width and the height of these components.
		
		proccessButtons();
	}
	
	private void proccessButtons()
	{
	
		// Handle row setting button.
		if(row_num_box.input_changed())
		{
			int rows = row_num_box.getInput().toInt();
			
			rows = Math.min(rows, MAX_ROWS);
						
			if(rows == 0)
			{
				return;
			}
			
			while(rows < num_rows())
			{
				matrixGrid.remRow();
			}
			
			while(rows > num_rows())
			{
				matrixGrid.addRow();
			}
			return;
		}
		
		// Handle column setting button.
		if(column_num_box.input_changed())
		{
			int col = column_num_box.getInput().toInt();
			
			col = Math.min(col, MAX_COLUMNS);
			
			if(col == 0)
			{
				return;
			}
			
			while(col < num_columns())
			{
				matrixGrid.remCol();
			}
			
			while(col > num_columns())
			{
				matrixGrid.addCol();
			}
			return;
		}
		
		if(transpose_button.flag())
		{
			data_transpose();
			return;
		}
		
		if(clear_button.flag())
		{
			data_clear_selected();
			return;
		}
		
		if(identity_button.flag())
		{
			data_identity();
			return;
		}
		
		if(conjugate_button.flag())
		{
			data_conjugate();
			return;
		}
		
		if(inverse_button.flag())
		{
			data_invert();
			return;
		}
		
		if(determinant_button.flag())
		{
			data_determinant();
			return;
		}
		
		
		if(delete_button.flag())
		{
			data_delete();
			// We do not delete here.
		}
		
		if(shift_left_button.flag())
		{
			if(data_shift_left())
			{
				eraseSelections();
			}
		}
		
		if(shift_right_button.flag())
		{
			if(data_shift_right())
			{
				gui_focusBox.eraseSelections();	
			}
			
		}
	}

	// A helper method that eliminates tediousness.
	private void obj_create(Obj o)
	{
		myContainer.obj_create(o);
	}

	@Override
	public Matrix<Equation> getInput()
	{
		List<Vector<Equation>> col_output = new List<Vector<Equation>>();
		
		for(int c = 0; c < num_columns(); c++)
		{
			col_output.add(getColumnVector(c));
		}
		
		return m_ex_col(col_output.toArray());
	}
	
	private Vector<Equation> getColumnVector(int colNum)
	{
		Equation[] column = new Equation[num_rows()];
		for(int r = 0; r < num_rows(); r++)
		{
			//print(getBox(r, colNum).getInput());
			column[r] = getBox(r, colNum).getInput();
		}
		
		return v(column);
	}
	
	@Override
	public void keyP(int key)
	{
		super.keyP(key);
		
		if(key == KeyEvent.VK_TAB)
		{

			int selection = getSelected();

			
			if(selection == -1)
			{
				getBox(0).toggle();
				return;
			}
			
			if(selection < num_rows()*num_columns() - 1)
			{
				getBox(selection + 1).toggle();
			}
			else
			{
				getBox(0).toggle();
			}
		}
	}

	// Various data manipulation and query functions.
	
	public gui_EquationInput getBox(int row, int col)
	{
		return matrixGrid.getBox(row, col);
	}
	
	private gui_EquationInput getBox(int number)
	{
		return matrixGrid.getBox(number);
	}

	// -- Button factory methods.
	
	// A function that creates fresh input boxes.
	private gui_EquationInput I_BOX()
	{
		gui_EquationInput output = new gui_EquationInput(-500, -500, 10, ROW_H);
		output.setDefaultEquation(Equation.ZERO);
		output.setAutoFit(false);
		output.INFO("Select this number box.");
		return output;
	}
	
	// -- Helpful User operation functions.
	
	private void data_transpose()
	{
		Matrix<Equation> m = getInput();
		populateInput(m.transpose());
	}

	/**
	 * Clears all of the element boxes.
	 */
	private void data_clear_all()
	{
		List<gui_EquationInput> selections = matrixGrid.getAll();
		
		data_clear(selections);	
	}
	
	/**
	 *  Clear the data from the selected boxes.
	 *  Depends on the getSelectioins() function.
	 */
	private void data_clear_selected()
	{
		List<gui_EquationInput> selections = getSelections();
		
		data_clear(selections);
	}

	private void data_clear(List<gui_EquationInput> selections)
	{
		if(selections.isEmpty())
		{
			clear_button.ERROR("Nothing is Selected.");
			return;
		}
		
		for(gui_EquationInput box : selections)
		{			
			box.clearInput();
		}
	}
	
	// Conjugates every box that is selected.
	private void data_conjugate()
	{
		List<gui_EquationInput> selections = getSelections();
		
		// Conjugate and repopulate every box. 
		for(gui_EquationInput box : selections)
		{
			Equation eq = box.getInput();
			box.populateInput(eq.conj().toSerialString());
		}
	
	}
	
	// Inverts the entire matrix.
	private void data_invert()
	{

		// Preinvert.
		Matrix<Equation> m;
		
		try
		{
			m = getInput().inverse();	
		}
		catch(Error e)
		{
			inverse_button.ERROR(e.getMessage());
			return;		
		}		
		
		
		// Check for singularities.
		if(m != null)
		{
			populateInput(m);
			return;
		}
		
		inverse_button.ERROR("Matrix has no inverse!");
	}
	
	private void data_identity()
	{
		data_clear_all();
		int min = Math.min(num_rows(), num_columns());
		
		for(int i = 0; i < min; i++)
		{
			// A Fancy way of setting the box to 1, the multiplicative identity.
			getBox(i, i).populateInput(Equation.ONE.toSerialString());
		}
		
	}
	
	/**
	 * Deletes a selected row or column from the matrix.
	 */
	private void data_delete()
	{
		int row = matrixGrid.getSelectedRow();
		if(row != -1)
		{
			try
			{
				matrixGrid.remRow(row);
			}
			catch(Error e)
			{
				delete_button.ERROR(e.getMessage());
			}
			return;
		}
		
		int col = matrixGrid.getSelectedColumn();
		if(col != -1)
		{
			try
			{
				matrixGrid.remCol(col);
			}
			catch(Error e)
			{
				delete_button.ERROR(e.getMessage());
			}
			return;
		}
		
		delete_button.ERROR("No row or column is currently selected");
	}

	// Allows for this component's color to be manipulated.
	public void setColor(Color c)
	{
		button_list.setColor(c);
		matrixGrid.setColor(c);
	}
	
	private int num_rows()
	{
		return matrixGrid.getNumRows();
	}
	
	private int num_columns()
	{
		return matrixGrid.getNumColumns();
	}
	
	// Get a list of the actual selections, including singletons.
	private List<gui_EquationInput> getSelections()
	{
		// CASE : a column or row vector is selected.
		List<gui_EquationInput> output = matrixGrid.getSelections();
		
		if(output != null)
		{
			return output;
		}
		
		// CASE : 1 input box is selected.
		if(output == null)
		{
			int index = getSelected();
			if(index != -1)
			{
				return new List<gui_EquationInput>(getBox(index));
			}
		}
		
		// CASE : Nothing is selected, return everything.
		return matrixGrid.getAll();
	}

	/**@return  Returns true if and only if this input box is selected.
	 */
	public boolean isSelected()
	{
		return getSelected() != -1;
	}
	
	/**
	 * 
	 * @return the index of the selected element.
	 * 
	 * Returns -1 if the element is not present.
	 */
	private int getSelected()
	{
		for(int r = 0; r < num_rows(); r++)
		for(int c = 0; c < num_columns(); c++)
		{
			if(getBox(r, c).isLastSelection())
			{
				return r*num_columns() + c;
			}
		}

		// Selected box not found in this input box.
		return -1;
	}
	
	/**
	 * Shifts every element in the selected row up or
	 * shifts every element in a selected column left.
	 * 
	 * Displays appropriate error messages.
	 * 
	 * returns true iff the operation was successful.
	 */
	private boolean data_shift_left()
	{
		// Try Rows.
		int rowNum = matrixGrid.getSelectedRow();
		if(rowNum > 0)
		{
			matrixGrid.shiftRowLeft(rowNum);
			return true;
		}
		else if(rowNum == 0)
		{
			shift_left_button.ERROR("Cannot shift the topmost row up!");
			return false;
		}
				
		// Try columns.
		int colNum = matrixGrid.getSelectedColumn();
		if(colNum > 0)
		{
			matrixGrid.shiftColumnLeft(colNum);
			return true;
		}
		else if(colNum == 0)
		{
			shift_left_button.ERROR("Cannot shift the leftmost column left!");
			return false;
		}
		
		delete_button.ERROR("No row or column is currently selected");
		return false;
	}
	
	/**
	 * Shifts every element in the selected row down or
	 * shifts every element in a selected column right.
	 * 
	 * Displays appropriate error messages.
	 * 
	 * returns true iff the operation was successful.
	 */
	private boolean data_shift_right()
	{
		
		// Try Rows.
		int rowNum = matrixGrid.getSelectedRow();
		
		if(rowNum == -1)
		{
			/* No row selected */
		}
		else if(rowNum < matrixGrid.getNumRows() - 1)
		{
			matrixGrid.shiftRowRight(rowNum);
			return true;
		}
		else
		{
			shift_right_button.ERROR("Cannot shift the bottomost row down!");
			return false;
		}
		
		// Try columns.
		int colNum = matrixGrid.getSelectedColumn();
		if(colNum == -1)
		{
			/* No Column Selected */
		}
		else if(colNum < matrixGrid.getNumColumns() - 1)
		{
			matrixGrid.shiftColumnRight(colNum);
			return true;
		}
		else
		{
			shift_right_button.ERROR("Cannot shift the rightmost column right!");
			return false;
		}
		
		shift_right_button.ERROR("No row or column selected!");
		return false;
	}
	
	private void data_determinant()
	{
		
		Matrix<Equation> m = getInput();
		
		if(m.getRowNum() != m.getColNum())
		{
			determinant_button.ERROR("Error: Non Square Matrices have no determinant.");
		}
		
		// Preinvert.
		Equation det = getInput().det();
				
		// Check for singularities.
		if(m != null)
		{
			populateInput(det);
			return;
		}
		
		// this would be ludicrous.
		determinant_button.ERROR("Matrix has no determinant!");
		
	}
	
}
