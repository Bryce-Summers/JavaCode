package Game_Engine.GUI.Components.large;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import BryceMath.Calculations.Colors;
import BryceMath.Numbers.Number;
import BryceMath.Structures.Vector;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.Engine.text.TextManager;
import Game_Engine.GUI.Components.small.gui_label;

/*
 * gui_vector 
 * Written by Bryce Summers on 6 - 5 - 2013.
 * 
 * Purpose : Creates a union of handles that each display the corresponding data elements in vector.
 * 			Handles the formatting and display of a column or row vector, acts as a complex gui_label.
 * 
 * Obj_glidable functionality and code cleaned up pn 7 - 16 - 2013.
 */

public class gui_vector<T extends Number<T>> extends Obj_union
{

	// -- Private data.
	UBA<gui_label> boxes;
	Vector<T> data;
	
	private int box_size;
	private final boolean hori;
	
	private boolean draw_borders = true;
	
	private UBA<Integer> widths;
	
	public static Color color = Colors.C_CLEAR;
	
	
	public gui_vector(Vector<T> data, int size, boolean hori)
	{
		super(0, 0, size*data.length, size);
		
		this.data = data;
		box_size  = size;
		this.hori = hori;
	}
	
	public gui_vector(double x, double y, Vector<T> data, int size, boolean hori)
	{
		super(x, y, size*data.length, size);
		
		this.data = data;
		box_size  = size;
		this.hori = hori;
	}

	// Used to construct vectors with a precomputed uniform width to the components.
	public gui_vector(double x, double y, Vector<T> data, UBA<Integer> widths, int w, int h)
	{
		super(x, y, w, h);
		
		this.data = data;
		box_size  = h;
		this.hori = true;
		
		this.widths = widths;
	}
	

	@Override
	public void iObjs()
	{
	
		// instantiate this vector based on whether is is a horizontal vector or vertical vector.
		if(hori)
		{
			iHori();
		}
		else
		{
			iVert();
		}
	}
	
	private void iHori()
	{
		int len = data.length;
		
		boxes = new UBA<gui_label>(len);
		
		int offset = 0;
		
		for(int i = 0; i < len; i++)
		{
		
			String message = data.get(i).toString();
			
			// -- Create the boxes.
			
			// Make a vector of labels for the data.
			// The widths should be adjusted to contain the text.
			// The height should be the box_size;
				
			int box_w;
			
			if(widths == null)
			{
				box_w = getBoxW(message);
			}
			else
			{
				box_w = widths.get(i);
			}
				
			// Create the handle.
			gui_label h = new gui_label(getX() + offset, getY(),
								  		  box_w, box_size);
			h.setColor(color);
			offset += box_w;
				 
			// Give the box the proper message.
			h.setText(message);
			
			// Add the box to the array of boxes.
			boxes.add(h);
			
			// Add the box to the Object list.
			obj_create(h);
			
		}// End of the for loop.
		
		setW(offset);
	}
	
	private void iVert()
	{
				
		int len = data.length;
		
		UBA<String> messages = new UBA<String>(len);
		
		int max_len = 0;
		
		// Generate all of the text and compute the maximum length.
		for(T r : data)
		{
			String message = r.toString();
			messages.add(message);
			
			int message_len = getBoxW(message);
			
			if(message_len > max_len)
			{
				max_len = message_len; 
			}
		}
		
		// All widths are constant in a column, based on the maximum width of the text inside.
		// (All heights are = to box_size).
		int box_w = max_len;
		
		boxes = new UBA<gui_label>(len);
		
		for(int i = 0; i < len; i++)
		{
			I_SCALARLABEL(messages, i, box_w);
		}
		
		// Correct the union height and width;
		setH(len*box_size);
		setW(box_w);
		
	}
	
	private void I_SCALARLABEL(UBA<String> messages, int i, int box_w)
	{
		// Create the handle.
		gui_label h = new gui_label(getX(), getY() + i*box_size, box_w, box_size);
		
		h.setColor(color);
		
		// Give it its proper message.
		h.setText(messages.get(i));
		
		// Add the box to the array of boxes.
		boxes.add(h);
		
		// Add the box to the Object list.
		obj_create(h);
	}

	// Returns the proper width of a box containing a string of the given size;
	private int getBoxW(String message)
	{
		return Math.max(TextManager.getLen(message, true) + 25, box_size);
	}

	public int size()
	{
		return boxes.size();
	}
	
	public gui_label getElem(int n)
	{
		return boxes.get(n);
	}
	
	public Vector<T> getData()
	{
		return data;
	}
	
	public void setDrawBorders(boolean flag)
	{
		draw_borders = flag;
	}

	// Draw the borders, if that is desired.
	public void draw(ImageB i, AffineTransform AT)
	{
		
		if(!draw_borders)
		{
			return;
		}
			
		
		int sep = 2;
		
		int x1 = (int)getX() + sep;
		int x2 = x1 + getW() - 1 - sep*2;
		
		int y1 = (int)getY() + sep;
		int y2 = y1 + getH() - 1 - sep*2;
		
		int bracket_size = box_size/4;
		
		Graphics2D g = i.getGraphics();
		g.setColor(Color.BLACK);

		
		// Left bracket.
		
		drawLine((Graphics2D) g, AT, x1, y1, x1 + bracket_size, y1);
		drawLine((Graphics2D) g, AT, x1, y1, x1, y2);
		drawLine((Graphics2D) g, AT, x1, y2, x1 + bracket_size, y2);
		
		
		// Right bracket.
		
		drawLine((Graphics2D) g, AT, x2, y1, x2 - bracket_size, y1);
		drawLine((Graphics2D) g, AT, x2, y1, x2, y2);
		drawLine((Graphics2D) g, AT, x2, y2, x2 - bracket_size, y2);
	}
	
	public void makeTransparent()
	{
		for(gui_label l : boxes)
		{
			l.makeTransparent();
		}
	}
}
