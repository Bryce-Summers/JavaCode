package Projects.Math.functionalRooms;

import java.awt.event.KeyEvent;

import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.room_functional;
import Game_Engine.GUI.Components.Input.gui_MatrixInput;
import Game_Engine.GUI.Components.communication.gui_infoBox;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.GUI.Components.small.gui_label;
import Game_Engine.GUI.Components.small.boxes.gui_focusBox;
import Projects.Math.Spr;
import BryceMath.Calculations.Colors;
import BryceMath.Numbers.Equation;

import BryceMath.Structures.Matrix;
import Data_Structures.Structures.UBA;

/*
 * A custom made room which has been made specifically for the entering of matrices.
 * 7 - 25 - 2013.
 */

public class room_matrixEnter extends room_functional<Matrix<Equation>>
{
	// -- Private Data.
	
	gui_MatrixInput matrix_data;
	gui_button done_button;
	gui_button help_button;
	
	final Matrix<Equation> original_matrix;
	
	UBA<Obj> INFO = new UBA<Obj>(1);
	
	public room_matrixEnter()
	{
		original_matrix = null;
	}
	
	
	public room_matrixEnter(Matrix<Equation> data)
	{
		original_matrix = data;
	}
	
	@Override
	public void iObjs()
	{
		int y = 0;
		int row_h = 75;
		
		gui_label title = new gui_label(0, y, getW(), row_h);
		title.setText("Matrix Input Screen");
		title.setColor(Colors.C_BLUE_HEADING);
		obj_create(title);
				
		done_button = new gui_button(0, 0, getW(), row_h);
		obj_create(done_button);
		done_button.setText("Done.");
		done_button.fitToContents();
		done_button.flash();
		done_button.INFO("Add the currently constructed matrix to the operation pane.");
		
		help_button = new gui_button(getW(), done_button.getY(), 0, done_button.getH());
		help_button.setImage(Spr.icon_help);
		help_button.fitToContents();
		help_button.xSub(help_button.getW());
		help_button.INFO("Display Help Boxes");
		obj_create(help_button);
		
		y += row_h;
	
		// Create the matrix data.
		matrix_data = new gui_MatrixInput(0, y, row_h, original_matrix);
		obj_create(matrix_data);
		matrix_data.setColor(Spr.backgroundColor);
		
		// Display some helpful text boxes at the start.
		//displayHelp();
	}
	
	public void update()
	{
		super.update();
		
		if(done_button.flag())
		{
			// Return the matrix data to the calling room.
			Return(matrix_data.getInput());
			
			// Revert the input, if this box has no original matrix.
			if(original_matrix == null)
			{
				matrix_data.clearInput();
			}
			
			gui_focusBox.revertSelection();			
		}
		
		if(help_button.flag())
		{
			displayHelp();
		}
	}
	
	public void keyP(int key)
	{
		super.keyP(key);
		
		if(key == KeyEvent.VK_ENTER)
		{
			done_button.setFlag(true);
		}
	}
	
	// Create helpful description boxes on the screen at various locations.
	public void displayHelp()
	{
		// Create various info boxes in order.
		help_main(0);
		
	}
	
	// Make sure the main help box is shown.
	private void help_main(int index)
	{
		if(!shouldCreate(index))
		{
			return;
		}
		
		int w = getW()/3;

		gui_infoBox info1 = new gui_infoBox(getW()/2 - w/2, getH()/2 - 64, w, 190,
				"Type Numbers into these boxes:  e.g 1, 3.14, 6/7, x^2 + 1, etc. " +
				"Press TAB to cycle between boxes. " + 
				"Use the other buttons help mutate the matrix.",
				matrix_data.getBox(0, 0));
		obj_create(info1);
		info1.redraw();
		
		INFO.set(index, info1);
	}
	
	// Returns true iff an info box should be created/recreated.
	// This has the side effect of ensuring the array is initialized
	// to the given index assuming the indices are called in order starting form 0.
	private boolean shouldCreate(int index)
	{
		// Handle the non initialized case.
		if(INFO.size() <= index)
		{
			INFO.add(null);
			return true;
		}
			
		// Handle the dead case.
		Obj o = INFO.get(0);
		if(o != null && o.dead(this))
		{
			return true;
		}
		
		// Handle the alive case.
		return false;
		
	}

}
