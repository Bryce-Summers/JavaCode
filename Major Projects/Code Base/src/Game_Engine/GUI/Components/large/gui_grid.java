package Game_Engine.GUI.Components.large;

import util.interfaces.Generator;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.boxes.gui_focusBox;

/*
 * Gui_grid class.
 * 
 * Written by Bryce Summers on 7 - 24 - 2014.
 * 
 * This class is responsible for managing a grid of gui elements and abstract away 
 * the various operations necessary to operate on them.
 * 
 * This has been elegantly abstracted from the gui_MatrixInput class.
 * 
 * This class also provides a set of selector buttons that allow the user to define which set of elements they wish to operate on.
 */

public class gui_grid<T extends gui_label> extends gui_window
{

	// The element creator that can generate new blank elements.
	Generator<T> creator;
	
	// Controls whether the selector boxes are available.
	private boolean show_selectors = true;
	
	// Controls whether the row and column add buttons are displayed.
	private boolean show_creators = true;
	
	// The representation of the data elements.
	// 	return boxes.get(col).get(row);
	private UBA<UBA<T>> boxes;
	
	// Selector buttons.
	private gui_focusBox selector_all;
	private UBA<gui_focusBox> selector_row;// FIXME : Change these to gui_focusBox;
	private UBA<gui_focusBox> selector_column; // FIXME : Consider 
	
	private gui_button row_add;
	private gui_button column_add;
	
	private int num_rows = 1;
	private int num_columns = 1;
	private final int ROW_H;
	
	private static final String selector_row_string = "Select this row";
	private static final String selector_column_string = "Select this column";
	
	// -- Constructors.
	public gui_grid(double x,
					double y,
					int w,
					int h,
					Generator<T> creator,
					boolean show_selectors,
					int ROW_H)
	{
		super(x, y, w, h);
		
		this.creator = creator;
		this.show_selectors = show_selectors;
		this.ROW_H = ROW_H;
		
		populateDataWindow();
	}
	
	private void populateDataWindow()
	{
		
		boxes = new UBA<UBA<T>>();
		boxes.add(new UBA<T>());
		
		// Create the first input box.
		T first_box = I_BOX();
		boxes.get(0).add(first_box);
		
		selector_all = new gui_focusBox(0, 0, 50, 50);
		selector_all.setText("All");
		obj_create(selector_all);
		
		// - Initialize the row and column deletion buttons.
		selector_row    = new UBA<gui_focusBox>();
		selector_column = new UBA<gui_focusBox>();
		
		gui_focusBox col_del_0 = I_SELECTOR_BUTTON();
		col_del_0.INFO(selector_column_string);
		obj_create(col_del_0);
		selector_column.add(col_del_0);
		
		gui_focusBox row_del_0 = I_SELECTOR_BUTTON();
		row_del_0.INFO(selector_row_string);
		obj_create(row_del_0);
		selector_row.add(row_del_0);
		
		column_add = new gui_button(50, 0, 50, ROW_H);
		row_add    = new gui_button(0, ROW_H*2, 200, 50);
		
		column_add.setText("+");
		row_add.setText("+");
		
		row_add.fitToContents();

		obj_create(column_add);
		obj_create(row_add);
		
		column_add.INFO("Add a new column to the matrix.");
		row_add.INFO("Add a new row to the matrix.");
	}
		
	// -- On screen visual alignment logic.
	
	public void update()
	{
		super.update();
		
		align();
		
		proccessButtons();
	}
	
	private void align()
	{
		// We need extra room for the deletion buttons.
		int x = (int)50;
				
		// Align row modification boxes.
		row_add.setX(x);
		row_add.setY(50 + num_rows*ROW_H);
				
		world.setH((int)row_add.getY2());
		column_add.setH((int) (world.getH() - 50 - row_add.getH()));
				
		for(UBA<T> column : boxes)
		{
			int x2 = x;
			int y = 50;
			for(T box : column)
			{
				box.setX(x);
				box.setY(y);
						
				y = (int) box.getY2();
						
				// Autofit to gauge maximum row spans.
				box.fitToContents();
				int potential_x2 = (int) box.getX2();
				if(potential_x2 > x2)
				{
					x2 = potential_x2;
				}
			}
					
			int w_new = x2 - x;
					
			// Update the widths to uniform on the second a pass.
			for(T box : column)
			{
				box.setW(w_new);
			}
					
			x = x2;
		}
				
		column_add.setX(x);
		column_add.setY(50);
		row_add.setW((int)(column_add.getX() - row_add.getX()));
				
		// Update the scrolling region of this component.
		scrollH((int) column_add.getX2());
		scrollV((int) row_add.getY2());
				
		world.setW((int) column_add.getX2());
				
				
		// Align and format all of the column deletion buttons.
		for(int c = 0; c < num_columns; c++)
		{
			gui_button col_del = selector_column.get(c);
			Obj c_0 = boxes.get(c).get(0);
			col_del.setX(c_0.getX());
			col_del.setY(c_0.getY() - 50);
			col_del.setW(c_0.getW());
		}
				
		// Align and format all of the column deletion buttons.
		for(int r = 0; r < num_rows; r++)
		{
			gui_button row_del = selector_row.get(r);
			Obj r_0 = boxes.get(0).get(r);
			row_del.setX(r_0.getX() - 50);
			row_del.setY(r_0.getY());
			row_del.setH(r_0.getH());
		}
	}
	
	
	private void proccessButtons()
	{
		// Add an element to every column.
		if(row_add.flag())
		{
			addRow();
			return;
		}
		
		// Add a column
		if(column_add.flag())
		{
			addCol();
			return;
		}
				

	}
	
	
	// Row and column mutations.
	
	// Add a row on the right of the grid.
	public void addRow()
	{
		for(UBA<T> column : boxes)
		{
			T box_new = I_BOX();
			//box_new.setDefaultText(DEFAULT_INPUT);
			column.add(box_new);
		}

		// Handle deletion buttons.
		gui_focusBox row_del = I_SELECTOR_BUTTON();
		selector_row.add(row_del);
		row_del.INFO(selector_row_string);
				
		// Update the dimensions of this input box.
		num_rows++;
		return;
	}
	
	// Delete row r on the grid.
	public void remRow()
	{
		remRow(num_rows - 1);
	}
	
	public void remRow(int row_index)
	{
		if(num_rows == 1)
		{
			throw new Error("Cannot remove the last row");
		}
		
		for(UBA<T> column : boxes)
		{
			column.delete_and_shift(row_index).kill();
		}
		
		// Destroy a row deletion button.
		selector_row.rem().kill();
		
		// Update the dimensions of this input box.
		num_rows--;
		return;
	}
	
	// Add a column onto the right on the grid.
	public void addCol()
	{
		
		UBA<T> column_new = new UBA<T>();
		
		for(int i = 0; i < num_rows; i++)
		{
			T box_new = I_BOX();
			//box_new.setDefaultText(DEFAULT_INPUT);
			column_new.add(box_new);
		}

		boxes.add(column_new);
		
		// Handle deletion buttons.
		gui_focusBox col_del = I_SELECTOR_BUTTON();
		col_del.INFO(selector_column_string);
		selector_column.add(col_del);
		
		// Increase the column count.
		num_columns++;
		return;
	}
	
	// Delete column c on the grid.
	public void remCol()
	{
		remCol(num_columns - 1);
	}
	
	// Removes the given column from the Matrix array.
	// Warning : This operation is of order O(m*n),
	// because we are using arrays to represent the data, so removal requires shifting.
	public void remCol(int col_index)
	{
		if(num_columns == 1)
		{
			throw new Error("Cannot remove the last column.");
		}
		
		if(col_index < 0 || col_index >= boxes.size())
		{
			throw new Error("Illegal removal column : " + col_index);
		}
		
		// Remove the last column.
		UBA<T> removables = boxes.get(col_index);
			
		// Kill all of the boxes.
		for(T box : removables)
		{
			box.kill();
		}
		
		// Destroy a column deletion button.
		selector_column.rem().kill();

		// Update the dimensions of this input box.
		num_columns--;
		
		// -- Delete the Column Array.
		boxes.delete_and_shift(col_index);
			
		return;
	}

	/**
	 * @param row the index of the row that will be shifted left 1 row.
	 * 		REQUIRES : row > 0;
	 */
	public void shiftRowLeft(int row)
	{
		swapRows(row, row - 1);
	}

	/**
	 * @param row the index of the row that will be shifted right 1 row.
	 * 		REQUIRES : row < num_rows;
	 */
	public void shiftRowRight(int row)
	{
		swapRows(row, row + 1);
	}
	
	/**
	 * @param col the index of the column that will be shifted left 1 column.
	 * 		REQUIRES : col > 0;
	 */
	public void shiftColumnLeft(int col)
	{
		swapColumns(col, col - 1);
	}

	/**
	 * @param col the index of the column that will be shifted right 1 column.
	 * 		REQUIRES : col < num_columns;
	 */
	public void shiftColumnRight(int col)
	{
		swapColumns(col, col + 1);
	}
	
	/**
	 * 
	 * @param c1 the smallest index in the shift region.
	 * @param c2 the largest index in the shift region.
	 * 
	 * REQUIRES : 0 <= c1 < c2 < num_columns.
	 * ENSURES  : Columns [c1, cX, ..., c2] --> [cX, ..., c2, c1].
	 */
	public void cycleColumnsLeft(int c1, int c2)
	{
		for(int c = c1; c < c2; c++)
		{
			swapColumns(c, c + 1);
		}
	}

	/**
	 * 
	 * @param c1 the smallest index in the shift region.
	 * @param c2 the largest index in the shift region.
	 * 
	 * REQUIRES : 0 <= c1 < c2 < num_columns.
	 * ENSURES : Columns [c1, ..., cX, c2] --> [c2, c1, ..., c2].
	 */
	public void cycleColumnsRight(int c1, int c2)
	{
		for(int c = c2; c > c1; c--)
		{
			swapColumns(c, c - 1);
		}
	}

	/**
	 * 
	 * @param r1 the smallest index in the shift region.
	 * @param r2 the largest index in the shift region.
	 * 
	 * REQUIRES : 0 <= r1 < r2 < num_rows
	 * ENSURES : Rows [r1, rX, ..., r2] --> [rX, ..., r2, r1].
	 */
	public void cycleRowsLeft(int r1, int r2)
	{
		for(int r = r1; r < r2; r++)
		{
			swapRows(r, r + 1);
		}
	}

	/**
	 * 
	 * @param r1 the smallest index in the shift region.
	 * @param r2 the largest index in the shift region.
	 * 
	 * REQUIRES : 0 <= r1 < r2 < num_rows 
	 * ENSURES : Rows [r1, ..., rX, r2] --> [r2, r1, ..., r2].
	 */
	public void cycleRowsRight(int r1, int r2)
	{
		for(int r = r2; r > r1; r--)
		{
			swapColumns(r, r - 1);
		}
	}
	
	/** Swaps rows, feeds potential errors up from the UBA.
	 * 
	 * @param c1 the first column.
	 * @param c2 the second column.
	 */
	public void swapColumns(int c1, int c2)
	{
		boxes.swap(c1, c2);
	}
	
	/** Swaps rows, feeds potential errors up from the UBA.
	 * 
	 * @param r1 the first row.
	 * @param r2 the second row.
	 */
	public void swapRows(int r1, int r2)
	{
		for(UBA<T> col : boxes)
		{
			col.swap(r1, r2);
		}
	}

	
	// -- Helper functions.
	
	private T I_BOX()
	{
		T output = creator.create();
		obj_create(output);
		return output;
	}
	
	// Instantiates Selector buttons.
	private gui_focusBox I_SELECTOR_BUTTON()
	{
		gui_focusBox output = new gui_focusBox(-700, -700, 50, 50);
		output.setText("O");
		obj_create(output);
		return output;
	}	
	
		
	// Returns the given element.
	public T getBox(int row, int col)
	{
		return boxes.get(col).get(row);
	}
	
	/**Returns a globally indexed box.
	 * 
	 * The user must define this functionality externally, because we do not know what type of gui_labels will be inputted.
	 * 
	 * @param number the index of the given box,
	 * a user can iterate via indices and this function to tab through the matrix or apply settings to the boxes.
	 * @return returns the box associated with the given indice.
	 */
	public T getBox(int number)
	{
		return boxes.get(number % num_columns).get(number / num_columns);
	}
	
	// Returns the number of rows in this grid.
	public int getNumRows()
	{
		return num_rows;
	}
	
	// Returns the number of columns in this grid.
	public int getNumColumns()
	{
		return num_columns;
	}
	
	/** Provides a list of the selected elements.
	 * 
	 * @return a list of the selected elements. returns null if no column, row, or all is selected.
	 * Does not account for selected elements, 
	 * because it only knows that they are buttons.
	 * Classes using this will have to write a wrapper function if they want to take singletons into account.
	 */
	public List<T> getSelections()
	{
		
		// Try to find a selected row.
		int row = getSelectedRow();
		if(row != -1)
		{
			return getRow(row);
		}
		
		// Try to find the selected Column.
		int col = getSelectedColumn();
		if(col != -1)
		{
			return getCol(col);
		}		


		// The select all button serves to invalidate all of the previous selections.
		
		return null;
	}
	
	/**
	 * @return the row selector that is selected. returns -1 if nothing is selected.
	 */
	public int getSelectedRow()
	{
		int row = 0;
		for(gui_focusBox b : selector_row)
		{
			if(b.isLastSelection())
			{
				return row;
			}
			
			row++;
		}
		
		return -1;
	}
	
	/**
	 * @return the column selector that is selected. Returns -1 if nothing is selected.
	 */
	public int getSelectedColumn()
	{
		int col = 0;
		for(gui_focusBox b : selector_column)
		{
			if(b.isLastSelection())
			{
				return col;
			}
			
			col++;
		}
		
		return -1;
	}
	
	/** Returns a list of the elements form a column.
	 *  
	 * @param col : The source column.
	 * @return returns a list of all elements in col.
	 */
	public List<T> getCol(int col)
	{
		return boxes.get(col).toList();
	}
	
	/**Returns a list of elements that represent a column in this grid.
	 * 
	 * @param  row the row number.
	 * @return a list containing every element of the desired row.
	 */
	public List<T> getRow(int row)
	{
		List<T> output = new List<T>();
		
		for(UBA<T> col : boxes)
		{
			output.add(col.get(row));
		}
		
		return output;
	}
	
	/**Produces a list of all elements.
	 * 
	 * @return a list that contains every element in this grid.
	 */
	public List<T> getAll()
	{
		List<T> output = new List<T>();
		
		int len = num_columns*num_rows;
		
		for(int i = 0; i < len ; i++)
		{
			output.add(getBox(i));
		}
		
		return output;
	}
	
}
