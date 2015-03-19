package Projects.Math.LessonLogic.Frames;

import java.io.PrintStream;

import util.SerialB;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.large.gui_window;

/*
 * Expression_Frame class.
 * 
 * Written by Bryce Summers on 8 - 20 - 2013.
 * 
 * Purpose : This class allows for me to insert scalars, matrices, and label connectives for the following activities.
 * 
 * 		- Displaying the expression to the screen.
 * 		- Allowing the user to select elements from within the expression.
 * 		- Saving and loading the expressions to a file.
 */

public class Expression_Frame extends gui_window implements SerialB
{
	private List<Obj> elems = new List<Obj>();
	
	public Expression_Frame(int w, int h)
	{
		super(0, 0, w, h);
	}
	
	public Expression_Frame(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public Expression_Frame(Rectangle screen)
	{
		super(screen);
	}

	// The user can use this function to add the Object to the list of serializable objects.
	public void add(Obj o)
	{
		elems.add(o);
		super.obj_create(o);
	}
	
	@Override
	public void serializeTo(PrintStream stream)
	{
		for(Obj o : elems)
		{
			stream.println(o.getSerialName());
			o.serializeTo(stream);
		}
		
		stream.println("End");
		
		// Create a lovely blank space.
		stream.println();
	}
	
	@Override
	// The serialization identifier.
	public String getSerialName()
	{
		return "Expression Frame";
	}
}
