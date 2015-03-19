package Game_Engine.GUI.Components.large;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.PrintStream;

import BryceMath.Numbers.Number;
import BryceMath.Structures.Matrix;
import BryceMath.Structures.Vector;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_label;

/*
 * gui_matrix class.
 * 
 * Written on 6 - 3 - 2013.
 * By, Bryce Summers.
 * 
 * Purpose : This component creates a gui_matrix from a union of gui_vectors.
 * 			 It will perform operations on the matrix,
 * 			 but the vectors themselves provide a useful means of creating the graphical component.
 */


public class gui_matrix<T extends Number<T>> extends Obj_union
{

	// -- Private data.
	
	UBA<UBA<gui_label>> boxes;// column, row indexed.
	Matrix<T> data;
	
	private int box_size;
	
	private boolean drawStraightBrackets = false;
	
	// -- Constructor.
	public gui_matrix(double x, double y, Matrix<T> data, int box_size)
	{
		// The width and Heights are bogus, because they cannot be determined yet.
		super(x, y, 1, 1);
		
		this.data = data;
		this.box_size = box_size;
	}
	
	@Override
	public void iObjs()
	{

		// We use the columns, because they have constant dimensions, so they will line up.
		Vector<T>[] vectors = data.columns();
		
		int offsetX = 0;
		int h = 0;
		boxes = new UBA<UBA<gui_label>>();
		
		for(Vector<T> v : vectors)
		{
			// create the sub gui_vectors.
			gui_vector<T> gv = new gui_vector<T>(getX() + offsetX, getY(), v, box_size, false);
			gv.initialize();
			
			// Populate a new column of boxes.
			UBA<gui_label> col = new UBA<gui_label>();
			int len = gv.size();
			
			// Add all of the generated labels to this union.
			for(int i = 0; i < len; i++)
			{
				gui_label l = gv.getElem(i);
				obj_create(l);
				col.add(l);
			}
			
			boxes.add(col);
						
			offsetX += gv.getW();

			// Repeatedly set this.
			h = gv.getH();
		}
	
		// Correct this gui_matrix's width and height.
		setH(h);
		setW(offsetX);
	
	}

	// FIXME : Majorly refactor this code.
	public UBA<gui_vector<T>> getRows()
	{
		Vector<T>[] rows = data.rows();
		
		int w = rows[0].length;
		int h = rows.length;
		
		UBA<Integer> widths = new UBA<Integer>(w);
		
		// Calculate the widths.
		for(int i = 0; i < w; i++)
		{
			widths.add(getElem(0, i).getW());
		}
		
		int compH = getElem(0, 0).getH();

		UBA<gui_vector<T>> output = new UBA<gui_vector<T>>(h);
		
		for(int i = 0; i < h; i++)
		{
			Vector<T> r = rows[i];
			output.add(new gui_vector<T>(getX(), getElem(i, 0).getY(), r, widths, getW(), compH));
		}
		
		return output;
		
	}
	
	public UBA<gui_dragVector<T>> getDragRows()
	{
		Vector<T>[] rows = data.rows();
		
		int w = rows[0].length;
		int h = rows.length;
		
		UBA<Integer> widths = new UBA<Integer>(w);
		
		// Calculate the widths.
		for(int i = 0; i < w; i++)
		{
			widths.add(getElem(0, i).getW());
		}
		
		int compH = getElem(0, 0).getH();

		UBA<gui_dragVector<T>> output = new UBA<gui_dragVector<T>>(h);
		
		for(int i = 0; i < h; i++)
		{
			Vector<T> row_data = rows[i];
			output.add(new gui_dragVector<T>(getX(), getElem(i, 0).getY(), row_data, widths, getW(), compH));
		}
		
		return output;		
	}
	
	public gui_label getElem(int r, int c)
	{
		return boxes.get(c).get(r);
	}
	
	public void setStraightBrackets(boolean flag)
	{
		if(drawStraightBrackets != flag)
		{
			redraw();
		}
		drawStraightBrackets = flag;
	}

	// Draw the borders, if that is desired.
	public void draw(ImageB i, AffineTransform AT)
	{		
		int sep = 2;
		
		int x1 = (int)getX() + sep;
		int x2 = x1 + getW() - 1 - sep*2;
		
		int y1 = (int)getY() + sep;
		int y2 = y1 + getH() - 1 - sep*2;
		
		int bracket_size = box_size/4;
		
		Graphics2D g = i.getGraphics();
		g.setColor(Color.BLACK);

		// The pixel weight of the brackets.
		int WEIGHT = 3;
		
		// Left bracket.
		
		if(!drawStraightBrackets)
		{
			fillRect((Graphics2D) g, AT, x1, y1, bracket_size, WEIGHT);
			fillRect((Graphics2D) g, AT, x1, y2 - WEIGHT, bracket_size, WEIGHT);
		}
		
		fillRect((Graphics2D) g, AT, x1, y1, WEIGHT, y2 - y1);
		
		// Right bracket.
		
		if(!drawStraightBrackets)
		{
			fillRect((Graphics2D) g, AT, x2 - bracket_size, y1, bracket_size, WEIGHT);
			fillRect((Graphics2D) g, AT, x2 - bracket_size, y2 - WEIGHT, bracket_size, WEIGHT);
		}
		
		fillRect((Graphics2D) g, AT, x2 - WEIGHT, y1, WEIGHT, y2 - y1);
	}
	
	public Matrix<T> getData()
	{
		return data;
	}
	
	// gui_matrices merely serialize their matrix data to the stream.
	@Override
	public void serializeTo(PrintStream stream)
	{
		data.serializeTo(stream);
	}
	
	@Override
	// The serialization identifier.
	public String getSerialName()
	{
		return "gui_matrix";
	}
}
