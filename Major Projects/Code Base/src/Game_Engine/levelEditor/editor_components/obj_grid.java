package Game_Engine.levelEditor.editor_components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import Data_Structures.ADTs.Bunch2;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;

/*
 * This class is used to draw a grid over an entire container.
 */

public class obj_grid extends Obj
{

	private int grid_w;
	private int grid_h;
	
	private int offset_x;
	private int offset_y;
	
	public obj_grid(int offset_x_in, int offset_y_in, int grid_w_in, int grid_h_in)
	{
		super(offset_x_in, offset_y_in);

		setGridW(grid_w_in);
		grid_h = grid_h_in;
		
		offset_x = offset_x_in;
		offset_y = offset_y_in;
		
		
	}

	@Override
	public void update()
	{
		/* Do Nothing */
	}
	
	// This object draws grids when visible.
	public void draw(ImageB i, AffineTransform AT)
	{
		drawGrid(i.getGraphics(), AT);
	}
	
	public void drawGrid(Graphics2D g, AffineTransform AT)
	{		
		int w = myContainer.getW();
		int h = myContainer.getH();
		
		g.setColor(Color.black);
		
		// Draw horizontal lines.
		for(int r = offset_y; r < h; r += grid_h)
		{
			drawLine(g, AT, 0, r, w, r);
		}
		
		// Draw vertical lines.
		for(int c = offset_x; c < w; c += getGridW())
		{
			drawLine(g, AT, c, 0, c, h);
		}
	}

	// -- Data Access methods.
	
	public int getGridW()
	{
		return grid_w;
	}

	public void setGridW(int grid_w)
	{
		this.grid_w = grid_w;
	}
	
	public int getGridH()
	{
		return grid_h;
	}

	public void setGridH(int grid_h)
	{
		this.grid_h = grid_h;
	}

	public int getOffsetX()
	{
		return offset_x;
	}
	
	public int getOffsetY()
	{
		return offset_y;
	}
	
	// Snaps the coordinates to the given grid.
	public Bunch2<Integer, Integer> snapToGrid(double x, double y)
	{
		
		// Update : 6 - 28 - 2014 : Fixed bug where user could not place object in top or left fields.
		int snapX = getGridW();
		int ex = (int) (x - getOffsetX() + snapX);
		ex = snapX*((int)(ex/snapX)) + getOffsetX() - snapX;

		int snapY = getGridH();
		int ey = (int) (y - getOffsetY() + snapY);
		ey = snapY*((int)(ey/snapY)) + getOffsetY() - snapY;
		
		return new Bunch2<Integer, Integer>(ex, ey);
	}
	
	// Snaps the given object to this grid.
	public void snapToGrid(Obj o)
	{
		Bunch2<Integer, Integer> coords = snapToGrid(o.getX(), o.getY());
		o.moveTo(coords.getType1(), coords.getType2());
	}
}
