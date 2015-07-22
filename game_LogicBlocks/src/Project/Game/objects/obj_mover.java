package Project.Game.objects;

import java.awt.geom.AffineTransform;

import Data_Structures.Structures.BitSet;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.small.gui_handle;
import Game_Engine.SpriteFactories.ArrowImageFactory;
import Project.Game.Componenets.guiBox;

/* 
 * Written by Bryce Summers on 6 - 21 - 2014.
 * 
 * Purpose : Movers are objects that can be moved by the player while obeying movement rules.
 */

public class obj_mover extends gui_handle
{
	public final int index;
	
	// Direction flags. 0 through 7 represent 8 directions 45 degrees apart. 0 = right, 2 = up, 4 = left, 6 = down, 7 = south east, etc.
	private final BitSet direction_properties;
	
	public obj_mover(double x_in, double y_in, int index, BitSet dir)
	{
		super(x_in, y_in, 64, 64);
		
		this.index = index;
		
		setDrawBackground(false);
		
		setDepth(-1);
		
		direction_properties = dir;
		
	}
	
	public void update()
	{
		super.update();
	}
	
	@Override
	public void global_mouseR()
	{
		if(dragged)
		{
			// Lock this mover onto the grid.
			Obj o = obj_groundSquare.getLastSquareInPath();
			//double x = o.getX() - holdOffsetX();
			//double y = o.getY() - holdOffsetY();
			glide(o);
			
			guiBox.inc_move(obj_groundSquare.getMoveLen());
		}
	
		super.global_mouseR();
	}
	
	// Move this mover to the given x and y location.
	public void move(double x, double y)
	{
		//x -= holdOffsetX();
		//y -= holdOffsetY();
		setX(x);
		setY(y);
	}
	
	public int holdOffsetX()
	{
		return getHoldX()/64*64;
	}
	
	public int holdOffsetY()
	{
		return getHoldY()/64*64;
	}
	
	// Draw arrows on component.
	@Override
	public void draw(ImageB image, AffineTransform AT)
	{
		// Draw image.
		super.draw(image, AT);

		// Draw arrows.
		drawRadialArray(image.getGraphics(), AT, ArrowImageFactory.getArrows(32), direction_properties);
	}
	


	// Allow for direction access.
	public BitSet getDirectionSet()
	{
		return direction_properties;
	}
	
}
