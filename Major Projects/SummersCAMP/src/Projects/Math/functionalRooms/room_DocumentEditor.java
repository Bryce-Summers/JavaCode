package Projects.Math.functionalRooms;

import java.awt.Color;
import java.io.File;
import java.util.Iterator;

import util.FileIO;
import util.deSerialB;
import BryceMath.Factories.ExpressionFactory;
import BryceMath.Numbers.Equation;

import BryceMath.Structures.Matrix;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.large.gui_matrix;

import Game_Engine.GUI.Components.large.gui_window;
import Game_Engine.GUI.Components.small.gui_label;
import Projects.Math.Dormant;
import Projects.Math.aamathMain;
import Projects.Math.Spr;
import Projects.Math.LessonLogic.ProblemData;
import Projects.Math.LessonLogic.room_lesson;
import Projects.Math.LessonLogic.gui_annotationBox;
import Projects.Math.LessonLogic.Frames.Expression_Frame;
import Projects.Math.LessonLogic.Frames.Matrix_frame;

/*
 * The Assignment editing room.
 * 
 * Written by Bryce Summers
 * 7 - 29 - 2013.
 * 
 * Purpose : This class is a room_lesson that acts as a deserializer for the .bdf save files.
 * 
 * 		- This room consists of a tree representing a complete set of avenues a user has taken in the attempt to craft a solution to an assignment.
 * 		- This room supports saving these trees to external .bdf (Bryce Document Format).
 * 		- This room supports loading trees from previously saved .bdf files.
 * 
 * 		- Each tree consists of Frames and Annotation Boxes.
 * 		- Frames hold Problem data, whereas Annotation Boxes hold comments regarding
 * 		  which steps the user has taken to transition between the frames.
 * 
 * 
 * NOTE : This deserializer currently only supports Version 1 .bdf files.
 * 
 * Update 8 - 20 - 2013
 * 		- I implemented the deSerialB interface.
 * 		- I have added capability for the deserialization of Expression_Frames.
 */

// FIXME : Remove the bogus problem initialization and leave the lesson tree clear to start out.


public class room_DocumentEditor extends room_lesson implements deSerialB
{
	Matrix<Equation> data = null;

	File file_in = null;
	
	// Creates a Problem room from the data at the indicated filename.
	// This is the counterpart to the serialization method in the lesson class.
	public room_DocumentEditor(File file)
	{		
		file_in = file;
	}
	
	public room_DocumentEditor()
	{
	}
	
	public room_DocumentEditor(Matrix<Equation> data)
	{
		this.data = data;
	}
	
	public void iObjs()
	{
		super.iObjs();
		
		if(file_in == null)
		{
			lesson.clear();
			return;
		}
		
		serializeFrom(file_in);
		
	}	
	
	// -- Deserialization code.
	
	@Override
	public String getExtension()
	{
		return ".bdf";
	}

	// The deserialization method for .bdf files.
	// look at lesson.class for the corresponding Serialization method.
	@Override @Dormant
	public void serializeFrom(File file_in)
	{
		List<String> input = FileIO.readFile(file_in);
		
		Iterator<String> iter = input.iterator();
		
		// By pass the "written by Bryce label.
		iter.next();
		
		// Bypass the Version Tags. 
		iter.next();
		
		// Bypass the Blank space.
		iter.next();
		
		lesson.clear();
		
		
		// The deserializer now proceeds to deserialize every element tag that it recognizes.
		while(iter.hasNext())
		{
			String str = iter.next();
						
			if(str.equals("Matrix Frame"))
			{
				Matrix_frame m_new = parseMatrixFrame(iter);
				
				lesson.add(m_new);

				continue;
			}
			
			if(str.equals("gui_annotationBox"))
			{
				gui_annotationBox box = parseAnnotationBox(iter);
				
				lesson.add(box);
				continue;
			}
			
			if(str.equals("ShiftL"))
			{
				lesson.shiftLeft(new Integer(iter.next()));
				continue;
			}
			
			if(str.equals("ShiftR"))
			{
				lesson.shiftRight(new Integer(iter.next()));
				continue;
			}
			
			// @Dormant functionality
			if(str.equals("Expression Frame"))
			{				
				//Expression_Frame frame_new = parseExpressionFrame(iter);

				//lesson.add(frame_new);
				continue;
			}
			
		}
	}
	
	private Matrix_frame parseMatrixFrame(Iterator<String> iter)
	{
		boolean show_scalar = new Boolean(iter.next());
		
		// Extract the Scalar expression form the next line in the input file.
		Equation scalar = ExpressionFactory.createEquation(iter.next());
		
		Matrix<Equation> matrix = parseMatrix(iter);
		
		ProblemData initial_state = new ProblemData(matrix, scalar);
		initial_state.setShowScalar(show_scalar);
		
		Matrix_frame output = createMatrixFrame(initial_state, lesson);
		
		
		// -- Perform some more processing eventually.
		// FIXME : 
		return output;
	}
	
	private Matrix<Equation> parseMatrix(Iterator<String> iter)
	{
		// Extract the matrix dimensions.
		int rowNum = new Integer(iter.next());
		int colNum = new Integer(iter.next());
		
		Equation[][] matrix_data = new Equation[rowNum][colNum];
		
		// Serialize all of the matrix component data.
		for(int r = 0; r < rowNum; r++)
		for(int c = 0; c < colNum; c++)
		{
			Equation element = ExpressionFactory.createEquation(iter.next());
			
			matrix_data[r][c] = element;
		}
		
		// Put it all together to construct the matrix.
		return new Matrix<Equation>(matrix_data);
	}

	private gui_annotationBox parseAnnotationBox(Iterator<String> iter)
	{
		int num_boxes = new Integer(iter.next());

		gui_annotationBox output = createAnnotationBox();
		
		for(int i = 0; i < num_boxes; i++)
		{
			// Parse the string.
			String annotation_text = iter.next();

			// Parse the color of this annotation box.
			Color c = Color.decode(iter.next());
			
			// Add a box containing the string.
			output.populateLastBox(annotation_text);
			
			// Set the box's color to the saved color.
			output.setLastBoxColor(c);
			
			// Add new boxes to populate,
			// but do not add any superfluous boxes.
			if(i < num_boxes - 1)
			{
				output.I_BOX();
			}
		}
		
		return output;
	}
	
	// Parse expression Frames.
	@Dormant
	private Expression_Frame parseExpressionFrame(Iterator<String> iter)
	{
		// First parse all of the elems.
		List<Obj> elems = new List<Obj>();
		
		int h = 0;
		int x = 0;
		
		int ROW_H	 = aamathMain.ROW_H;
		
		while(iter.hasNext())
		{
			String s = iter.next();
			
			// Stop the parsing when we hit an "End" tag.
			if(s.equals("End"))
			{
				break;
			}
		
			// Parse gui_Matrices.
			if(s.equals("gui_matrix"))
			{
				Matrix<Equation> matrix = parseMatrix(iter);
				gui_matrix<Equation> g_matrix = new gui_matrix<Equation>(x, 0, matrix, ROW_H);
				elems.add(g_matrix);
			
				g_matrix.initialize();
				
				// Update dimensions.
				int h_new = g_matrix.getH();
				if(h_new > h)
				{
					h = h_new; 
				}
				
				x += g_matrix.getW();
				continue;
			}
			
			// Parse Labels.
			if(s.equals("gui_label"))
			{
				String message = iter.next();
				
				gui_label l = new gui_label(x, 0, 0, ROW_H);
				l.setText(message);
				l.fitToContents();
				elems.add(l);
				
				// Set connective images.
				if(message.equals("/"))
				{
					l.setImage(Spr.divide_symbol);
				}
				else if(message.equals("*"))
				{
					l.setImage(Spr.dot_symbol);
				}
				
				// Update dimensions.
				int h_new = l.getH();
				if(h_new > h)
				{
					h = h_new; 
				}
				
				x += l.getW();
			}
		}
		
		/*
		 * Now center all of the elems horizontally, center all of the labels vertically,
		 * and create a frame of an appropriate size.
		 */
		
		// FIXME : This should be deduced from the elems.
		Expression_Frame result = room_lesson.createExpressionFrame(h, lesson);
		
		int objs_x_offset = lesson.getWorld().getW()/2 - x/2;
		
		if(objs_x_offset < 0)
		{
			result.scrollH(x);
			result.setH(h + gui_window.SCROLLBARSIZE);
			objs_x_offset = 0;
		}
		
		for(Obj o : elems)
		{
			result.add(o);
			
			if(o instanceof gui_label)
			{
				o.setY(h/2 - o.getH()/2);
			}
		
			o.setX(o.getX() + objs_x_offset);
		}
		
		return result;
	}
}
