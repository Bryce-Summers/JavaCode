package Projects.Math.LessonLogic;

import java.awt.Color;
import java.io.PrintStream;

import BryceMath.Calculations.Colors;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;

import Game_Engine.GUI.Components.Input.textBased.gui_StringInput;
import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_button;
import Game_Engine.SpriteFactories.ArrowImageFactory;

import Projects.Math.Spr;

/*
 * Graphic User interface component for entering an arbitrary number of Strings in a row.
 * 
 * Written by Bryce Summers on 8 - 13 - 2013.
 * 
 * Capabilities : 
 * 		- Allows for the creation of Strings displayed in vertical order on the screen.
 * 		- Allows for the dynamic deletion of strings.
 */

public class gui_annotationBox extends gui_window
{
	private List<gui_StringInput> data;
	private List<gui_button> deletion_buttons;
	private List<gui_button> shift_down_buttons;
	
	int ROW_H;
	
	gui_button add_button;
	
	final int min_box_width;
	
	boolean height_extended = false;
	
	public gui_annotationBox(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		ROW_H = h;
		
		
		setRestingColor(Colors.C_GRAY2);
		setColor(Colors.C_GRAY2);
		
		setH(Spr.gui_borderSize*2);

		data = new List<gui_StringInput>();
		deletion_buttons = new List<gui_button>();
		shift_down_buttons = new List<gui_button>();

		// Create the add button.
		//add_button = new gui_button(world.getW() - ROW_H - SCROLLBARSIZE + Spr.gui_borderSize*2, - ROW_H, ROW_H, ROW_H);
		add_button = new gui_button(0, - ROW_H/2, gui_window.SCROLLBARSIZE, ROW_H/2);
		add_button.setDepth(-2);
		add_button.setText("+");
		add_button.drawBordersOnlyOnHover();
		add_button.setRestingColor(Colors.C_CLEAR);
		add_button.INFO("Create a new comment box!");
		obj_create(add_button);
		
		
		min_box_width = world.getW() - gui_window.SCROLLBARSIZE*2;
		
		// Create the very first box.
		I_BOX();
		
	}
	
	// Adds a new annotation box to the list.
	public void I_BOX()
	{
		gui_StringInput box = new gui_StringInput(gui_window.SCROLLBARSIZE, world.getH() + Spr.gui_borderSize*2, min_box_width, ROW_H - Spr.gui_borderSize*4);
		box.setDefaultText("");
		box.INFO("Text Boxes want the user to type text in them!");
		data.add(box);
		obj_create(box);
		
		// Prepare a proper iterator for the delete button.
		IterB<gui_StringInput> iter = data.getTailIter();
		iter.previous();
		
		// Create a new iterator location.

		
		// Create a delete button.
		op_button b = new op_button(0, world.getH(), gui_window.SCROLLBARSIZE, ROW_H/2, iter, op_type.DELETE);
		obj_create(b);
		deletion_buttons.add(b);

		// Create a shiftDown Button.
		op_button b_shift = new op_button(0, world.getH() + ROW_H/2, gui_window.SCROLLBARSIZE, ROW_H/2, iter, op_type.SHIFT_DOWN);
		obj_create(b_shift);
		shift_down_buttons.add(b_shift);

		// Compute iterator references.
		IterB<gui_button> iter_delete = deletion_buttons.getTailIter();
		iter_delete.previous();
		
		IterB<gui_button> iter_shift = shift_down_buttons.getTailIter();
		iter_shift.previous();
		
		
		// Give the deletion button the references.
		b.setDeleteIters(iter_delete, iter_shift);
		
		
		// Increase the size of the box.
		addH();
	}
	
	public void populateLastBox(String message)
	{
		data.getLast().populateInput(message);
	}
	
	public void setLastBoxColor(Color c)
	{
		data.getLast().setRestingColor(c);
	}
	
	// Handle the add Annotation button.
	@Override
	public void update()
	{
		// -- Very important.
		super.update();
		
		if(add_button.flag())
		{
			I_BOX();
		}

		// Deal with potential overflow of text boxes.
		int max_w = 0;
		for(gui_StringInput box : data)
		{
			box.fitToContents(min_box_width);
			max_w = Math.max(max_w, box.getW());
		}		
		if(max_w > min_box_width)
		{
			if(!isScrollingH())
			{
				scrollH(max_w + gui_window.SCROLLBARSIZE*2);
				scrollH.setXValue(1.0);
			}
			else
			{
				scrollH(max_w + gui_window.SCROLLBARSIZE*2);
			}
			
			if(!height_extended)
			{
				addH(gui_window.SCROLLBARSIZE);
				height_extended = true;
			}
			
		}
		else
		{
			unscrollH();
			
			if(height_extended)
			{
				subH(gui_window.SCROLLBARSIZE);
				height_extended = false;
			}
		}

	}
	
	// Gives the active cursor to the first box in the data list.
	public void toggle()
	{
		data.getFirst().toggle();
	}

	// Increase the height of this window.
	private void addH()
	{
		add_button.yAdd(ROW_H);
		setH(getH() + ROW_H);
	}
	
	private void remH()
	{
		add_button.ySub(ROW_H);
		setH(getH() - ROW_H);
	}
	
	private enum op_type{DELETE, SHIFT_UP, SHIFT_DOWN};
	
	// Buttons that can be set to different modes.
	// These op buttons should never mutate the ordering of object in their lists, because they each contain iterator references that will may become inconsistent if used willy nilly.
	private class op_button extends gui_button
	{
		// Annotation box reference.
		private IterB<gui_StringInput> iter;
		
		// Reference to the op_type.DELETE buttons.
		private IterB<gui_button> deleteIter;
		
		// Reference to the o_type.SHIFT_DOWN buttons.
		private IterB<gui_button> shifDownIter;
		
		final private op_type my_op;
		
		public op_button(double x, double y, int w, int h, IterB<gui_StringInput> iter, op_type op)
		{
			super(x, y, w, h);
			
			switch(op)
			{
				case DELETE:
					setText("X");
					INFO("Delete this comment permanently.");				
					break;
				case SHIFT_DOWN:
					// Downward pointing arrow image.
					setImage((ArrowImageFactory.getArrows(h))[6]);
					INFO("Shift this comment down.");
					break;
					
				case SHIFT_UP:
					throw new Error("Not yet Implemented");
			}
			
			this.iter = iter;
			
			drawBordersOnlyOnHover();
			setRestingColor(Colors.C_CLEAR);
			setDepth(-1);
			
			my_op = op;
		}
						
		public void setDeleteIters(IterB<gui_button> deleteIter, IterB<gui_button> shiftDownIter)
		{
			this.deleteIter = deleteIter;
			this.shifDownIter = shiftDownIter;
		}

		public void update()
		{
			super.update();
			
			// Deletes the corresponding annotation from the list and kills the annotation.
			if(flag())
			{
				switch(my_op)
				{
					case DELETE: deletionAction(); break;
					case SHIFT_UP:   shiftUpAction(); break;
					case SHIFT_DOWN: shiftDownAction(); break;
					
				}
				
			}
		}
		
		private void deletionAction()
		{
			gui_StringInput box = iter.current();
			box.kill();
			iter.remove();
			
			// Decrease the height of all of the succeeding elements.
			while(iter.hasNext())
			{
				Obj o = iter.next();
				o.setY(o.getY() - ROW_H);
			}
			

			// Now kill this deletion button.
			kill();
			deleteIter.remove();
			shifDownIter.current().kill();
			shifDownIter.remove();
			
			// Now decrease the y coordinate for all succeeding deletion buttons.
			while(deleteIter.hasNext())
			{
				deleteIter.next().ySub(ROW_H);
			}
			
			// Now decrease the y coordinate for all succeeding comment shifting buttons.
			while(shifDownIter.hasNext())
			{
				shifDownIter.next().ySub(ROW_H);
			}
			
			// Decrease the height of this window.
			remH();
			
			
			if(data.isEmpty())
			{
				I_BOX();
			}
			
		}

		// Iter twidling actions for swapping the text up or down.
		private void shiftUpAction()
		{
			if(!iter.hasPrevious())
			{
				ERROR("Cannot shift the top comment up more!");
				return;
			}
			
			// Jostles the iterator back and then forth again to find the buttons.
			gui_button current, up;
			up 		= iter.previous();
			current = iter.next();
			
			// Swap the text. Since the elements themselves do not get swapped, no mutations occur.
			String temp = current.getText();
			current.setText(up.getText());
			up.setText(temp);
		}
		
		private void shiftDownAction()
		{
			if(!iter.hasNext())
			{
				ERROR("Cannot shift the bottom comment down more!");
				return;
			}
			
			// Jostles the iterator back and then forth again to find the buttons.
			gui_button current, down;
			down	= iter.next();
			current = iter.previous();
			
			// Swap the text. Since the elements themselves do not get swapped, no mutations occur.
			String temp = current.getText();
			current.setText(down.getText());
			down.setText(temp);
		}

	}// End of Delete button class.

	@Override
	public String getSerialName()
	{
		return "gui_annotationBox";
	}

	// The serialization specification for gui annotation boxes.
	@Override
	public void serializeTo(PrintStream stream)
	{
		// Indicate how many boxes this annotation contains.
		stream.println(data.size());
		
		// Print all of the annotation data.
		for(gui_StringInput box : data)
		{
			// String.
			stream.println(box.getInput());
			
			// Color.
			stream.println(box.getColor().getRGB());
		}
		
		// Print an extra space at the end.
		stream.println();
	}
}
