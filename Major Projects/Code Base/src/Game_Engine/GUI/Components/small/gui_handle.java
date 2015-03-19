package Game_Engine.GUI.Components.small;

import static BryceMath.Calculations.MathB.max;
import static BryceMath.Calculations.MathB.min;

import java.awt.Color;

import BryceMath.Calculations.Colors;
import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Interfaces.Pingable;
import Game_Engine.GUI.Sprites.StyleSpec;

/*
 * 	gui_handle class.
 * 	Written by Bryce Summers on 6 - 3 - 2013.
 * 
 *  Purpose : This class provides a draggable rectangle that can be moved around its world.
 *  		  This allows me to make components that can be dragged.
 */

public class gui_handle extends gui_label implements Pingable
{
	// -- public data.
	
	// Only one handle at a time may be dragged.
	
	public static gui_handle handle_held;
		
	// -- private data.
	
	// Handles know if they are being dragged. 
	protected boolean dragged = false;
	private boolean changed = false;
	
	// Handles know whether they should travel back home or not.
	private boolean revert;
	
	// Handles travel to the front of the view when being held, but they need to know which depth to go back to.
	private int savedDepth;
	
	/*
	 * When they are being dragged, 
	 * handles know where the mouse is holding them,
	 * relative to their location
	 */ 
	private int holdX;
	private int holdY;
	
	// Handles know their bounds of operation.
	
	// Vertical Variables.
	private int min_x = Integer.MIN_VALUE, max_x = Integer.MAX_VALUE;
		
	// Horizontal Variables.
	private int min_y = Integer.MIN_VALUE, max_y = Integer.MAX_VALUE;
	
	// Yellow.
	public final static Color C_DRAGGABLE = Colors.Color_hsv(60, 5, 100);
	
	private Color C_DRAGGED = StyleSpec.C_DRAGGED;

	// -- Constructors.
	public gui_handle(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		iVars();
	}
	
	public gui_handle(Rectangle r)
	{
		super(r);
		iVars();
	}
	
	private void iVars()
	{
		setRestingColor(C_DRAGGABLE);
		
		padding = 0;
		
		//setDrawBorders(true);
	}
	
	// -- Mouse code.

	@Override
	public void initialize()
	{
		super.initialize();
		
		int room_h = myContainer.getH();
		int room_w = myContainer.getW();
				
		min_x = Math.max(min_x, 0);
		min_y = Math.max(min_y, 0);
		max_x = Math.min(max_x, room_w - getW());
		max_y = Math.min(max_y, room_h - getH());
	}
	
	@Override
	public void mouseP(int mx, int my)
	{		
		// Do not update the dragged values if this element is already being dragged.
		if(dragged)
		{
			return;
		}
		
		// Remember where the scroll bar was clicked on by the mouse.
		holdX = (int) (mx - getX());
		holdY = (int) (my - getY());
		
		// Send this mouse into dragged mode.
		enterDragMode();

	}

	@Override
	public void global_mouseR()
	{
		if(dragged)
		exitDragMode();
	}
	
	// The handle logic for mouse holding.
	@Override
	public void global_mouseD(int mx, int my)
	{
		
		// Do nothing if this scroll bar is not being dragged.
		if(!dragged)
		{
			return;
		}

		setX(max(min_x, min(max_x, mx - holdX)));
		setY(max(min_y, min(max_y, my - holdY)));

		// Set the changed flag.
		changed = true;
	}

	// Returns the state of the flag,
	// then sets it back to false.
	@Override
	public boolean flag()
	{
		if(changed)
		{
			changed = false;
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setFlag(boolean flag)
	{
		changed = flag;
	}
	
	// Drag mode variables.
	private void enterDragMode()
	{
		interpolator.stop();
		
		// Do not allow for handles to be dragged if they are disabled.
		if(!isEnabled())
		{
			return;
		}
		
		dragged    = true;
		savedDepth = getDepth();
		handle_held = this;
		
		// Make this handle visible above all other handles.
		setDepth(Integer.MIN_VALUE + 1);
	}
	
	private void exitDragMode()
	{
		// Send this mouse out of dragged mode.
		dragged     = false;
		handle_held = null;
		
		// Revert the depth of this object.
		// NOTE : Since the sorting function preserves the order of the keys,
		//		  this handle will remain above all equal nodes after it is released.
		setDepth(savedDepth);

		if(revert)
		{
			revert();
		}
	}

	@Override
	protected void update()
	{
		super.update();

		if(mouseInRegion)
		{
			setColor(C_DRAGGED);
		}
		else
		{
			revertColor();
		}
	}
	
	public void setDragColor(Color c)
	{
		C_DRAGGED = c;
	}

	// Allows users to specify handles as homers.
	public void setHoming(boolean flag)
	{
		revert = flag;
	}
	
	@Override
	public void enable()
	{
		super.enable();
		
		//myColor = C_DRAGGABLE;
		
	}
	
	@Override
	public void disable()
	{
		super.disable();
		
		revertColor();
	}

	// Puts this handle back inside of its bounds.
	public void ensureBounds()
	{
		setX(max(min_x, min(max_x, getX())));
		setY(max(min_y, min(max_y, getY())));
	}
	
	// -- Bounds getters and setters.
	public void setMinX(int x)
	{
		min_x = x;
		initialized = true;
	}
	
	public void setMaxX(int x)
	{
		max_x = x;
		initialized = true;
	}
	
	public void setMinY(int y)
	{
		min_y = y;
		initialized = true;
	}
	
	public void setMaxY(int y)
	{
		max_y = y;
		initialized = true;
	}
	
	public int getMinX()
	{
		return min_x;
	}
	
	public int getMaxX()
	{
		return max_x;
	}
	
	public int getMinY()
	{
		return min_y;
	}
	
	public int getMaxY()
	{
		return max_y;
	}
	
	public void fixX(int x)
	{
		setMinX(x);
		setMaxX(x);
	}
	
	public void fixY(int y)
	{
		setMinY(y);
		setMaxY(y);
	}	
	
	// Handles can indicate at which percentage of their dimensions they currently reside in.
	public double getXPercentage()
	{
		return 1.0*(getX() - min_x) / (max_x - min_x);
	}
	
	public double getYPercentage()
	{
		return 1.0*(getY() - min_y) / (max_y - min_y);
	}
	
	// Allow others to know if this handle is being held.
	public boolean isHeld()
	{
		return handle_held == this;
	}
	
	protected int getHoldX()
	{
		return holdX;
	}
	
	protected int getHoldY()
	{
		return holdY;
	}


	
}
