package Game_Engine.GUI.Components.large;

import util.Genarics;
import util.interfaces.Function;
import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_look_and_feel;

/*
 * Array class.
 * 
 * Allows for the creation of very simple arrays of look and feel elements.
 */

public class gui_array<T extends gui_look_and_feel> extends Obj_union
{

	Genarics<T> ge_T = new Genarics<T>();;
	
	int element_w, element_h, rows, cols;
	
	T[][] elems;
	
	// The Boolean is arbitrary.
	Function<Boolean, T> create_function;
	
	public gui_array(double x, double y, int element_w, int element_h, int num_r, int num_c,  Function<Boolean, T> create_element)
	{
		super(x, y, element_w*num_c, element_h*num_r);
		
		this.element_w = element_w;
		this.element_h = element_h;
		this.rows = num_r;
		this.cols = num_c;
		
		create_function = create_element;
	}

	public gui_array(Rectangle bounds)
	{
		super(bounds);
	}

	@Override
	public void iObjs()
	{
		T example = create_function.eval(true);
		
		elems = ge_T.Array_2d(rows, cols, example);
		
		// waste the example...
		
		int x = (int)getX();
		int y = (int)getY();
		
		for(int r = 0; r < rows; r++)
		for(int c = 0; c < cols; c++)
		{
			T temp = create_function.eval(true);
			temp.setW(element_w);
			temp.setH(element_h);
			temp.setX(x + element_w*c);
			temp.setY(y + element_h*r);
			elems[r][c] = temp;
			obj_create(temp);
		}
	}
	
	// Allow element access.
	public T getElem(int r, int c)
	{
		return elems[r][c];
	}

}
