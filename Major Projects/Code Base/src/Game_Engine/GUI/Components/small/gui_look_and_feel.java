package Game_Engine.GUI.Components.small;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.interfaces.SerialImageB;
import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;
import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.obj_glidable;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.communication.gui_hoverText;
import Game_Engine.GUI.ImageProccessing.drawing_gui;

/*
 * The Look and Feel class.
 * Written by Bryce Summers
 * Date : 6 - 3 - 2013.
 * 
 * This class is the super class for all gui components.
 * 
 * Purpose : Abstracts away they drawing of borders and background rectangles.
 * 
 * Capabilities : Specifies the main look and feel attributes of my Graphic User Interface Components.
 * 					This also allows for centered images to be drawn onto look and feels.
 * 
 * 
 * Also specifies all of the times when components can redraw themselves.
 * 
 */

// FIXME : Draw the borders and backgrounds in a little bit to ensure aestetically pleasing gaps between the components.

public class gui_look_and_feel extends obj_glidable implements SerialImageB
{
	// -- Private data.
	
	// An integer that represents the GUI border size used for this component. 
	public int borderSize;// FIXME : Can this be removed to save memory?
	
	// FIXME : This may have ramifications if not left at 0;
	protected int padding = 2;
	
	
	private Color myColor = null;
	
	// Sets whether or not this label draws its borders.
	private boolean drawBorders = true;
	private boolean drawBackground = true;
	
	// Can override the drawBorders command.
	boolean drawBordersOnMouseHover = false;
		
	// -- The colors used in my GUI.
	public final static Color C_CLEAR	  = Colors.Color_hsv(0, 0, 0, 0);
	public final static Color C_DISABLED  = Colors.Color_hsv(0, 0, 0, 0);
	
	private Color basicColor = C_CLEAR;//Color.white;// C_CLEAR
	
	public gui_look_and_feel(double x, double y, int w, int h)
	{
		super(x, y);
		iVars(w, h);
	}
	
	public gui_look_and_feel(Rectangle r)
	{
		super(r.getX(), r.getY());
		iVars(r.getW(), r.getH());
	}

	private void iVars(int w_in, int h_in)
	{
		// BorderSize is standardized from the sprite class.
		borderSize = SpriteLoader.gui_borderSize;
		
		// The default color is white.
		if(myColor == null)
		{
			myColor = basicColor;
		}
		
		setW(w_in);
		setH(h_in);
		
		redraw();
	}
	
	@Override
	public boolean mouseCollision(int mouseX, int mouseY)
	{
		
		Rectangle bounds = new Rectangle((int)(getX() + borderSize),(int)(getY() + borderSize),
										getW() - borderSize*2 - 1, getH() - borderSize*2 - 1);
		

		// Before checking the normal image using relative coordinates check the label's background rectangle.
		if(bounds.containsPoint(mouseX, mouseY))
		{
			super.mouseInRegion = true;
			
			// This is down here so that the mouseInRegion is updated properly.
			if(!isVisible() || !collidable)
			{
				return false;
			}
			
			return true;
		}


		// If the first check fails, then return the result of the normal image collision test.
		boolean result = super.mouseCollision(mouseX, mouseY);
		super.mouseInRegion  = result;

		// This is down here so that the mouseInRegion is updated properly.
		if(!isVisible() || !collidable)
		{
			return false;
		}
		
		return result;
	

	}
	
	@Override
	public void draw (ImageB i, AffineTransform AT)
	{
		if (!isVisible())
		{
			return;
		}
	
		// Get the BufferedImage's Graphics context.
		Graphics2D g = (Graphics2D) i.getGraphics();
		
		if(drawBackground)
		drawBackground(g, AT);

		// Draw the sprite centered on this look and feel if it exists.
		if(sprite != null)
		{
			double x_new = getX() + getW()/2 - sprite.getWidth ()/2;
			double y_new = getY() + getH()/2 - sprite.getHeight()/2;
			
			drawImage(g, AT, x_new, y_new, sprite);
		}
		
		if(isEnabled() && (drawBorders || (mouseInRegion && drawBordersOnMouseHover) || super.highlighted))
		{
			drawBorders(g, AT);
		}
		
	}
	
	protected void drawBackground(Graphics2D g, AffineTransform AT)
	{
		// Background Rectangle
		if(isEnabled())
		{
			g.setColor(myColor);
		}
		else
		{
			g.setColor(C_DISABLED);
		}
				
		
		if(isEnabled() && drawBorders)
		{			
			fillRect(g, AT, getX() + borderSize + padding, getY() + borderSize + padding, getW() - borderSize*2 - padding*2, getH() - borderSize*2 - padding*2);
		}
		else
		{
			fillRect(g, AT, getX(), getY(), getW(), getH());
		}
		
	}

	
	// Draws the borders according to this component's state.
	protected void drawBorders(Graphics2D g, AffineTransform AT)
	{	
		// Can't draw trivial borders safely.
		if(getW() <= 0 || getH() <= 0)
		{
			return;
		}
		
		if(super.mouseInRegion || super.highlighted)
		{
			// Draw the highlighted GUI Borders.
			BufferedImage temp = drawing_gui.drawBorders_highlight(getW() - padding*2, getH() - padding*2);
			drawImage(g, AT, getX() + padding, getY() + padding, temp);
			return;
		}
				
		// Draw the GUI Borders.
		BufferedImage temp = drawing_gui.drawBorders(getW() - padding*2, getH() - padding*2);
		drawImage(g, AT, getX() + padding, getY() + padding, temp);
	}
	
	
	// -- Data access methods. (All mutations should include a redraw() call.)
	
	/* These mutations override the built in encapsualted Obj methods with redraw() calls.
	 * This ensures that redrawing occurs every time is is supposed to. the methods are also made to return immediately
	 * if the method would have no effect to prevent areas of the screen from being redrawn carelessly when the screen will not have changed.
	 */
	
	@Override
	public void setVisible(boolean visible)
	{
		if(isVisible() == visible)
		{
			return;
		}
		
		super.setVisible(visible);
		
		redraw();
	}
	
	public void setDrawBorders(boolean flag)
	{
		if(drawBorders != flag)
		{
			redraw();
		}
		drawBorders = flag;
	}
	
	// Returns the size in pixels of the borders.
	public int getBorderSize()
	{
		return borderSize;
	}
	
	// Sets the size of the borders to the desired pixel width.
	public void setBorderSize(int new_size)
	{
		if(new_size == borderSize)
		{
			return;
		}
		
		redraw();
		borderSize = new_size;
		redraw();
	}
	
	// Sets the color temporarily to this color until another process changes the color.
	public void setColor(Color c)
	{
		if(!myColor.equals(c))
		{
			redraw();
		}
		myColor = c;
	}
	
	public Color getColor()
	{
		return myColor;
	}
	
	// Reverts the color to the normal "resting" color.
	public void revertColor()
	{
		if(!myColor.equals(basicColor))
		{
			redraw();
		}
		
		myColor = basicColor;
	}
	
	// Updates the desired resting color.
	public void setRestingColor(Color c)
	{
		if(!basicColor.equals(c) || !myColor.equals(c))
		{
			redraw();
		}
		
		basicColor = c;
		myColor = c;
		setColor(c);
	}
	
	public void makeTransparent()
	{
		if(!myColor.equals(C_CLEAR))
		{
			redraw();
		}
		
		myColor = C_CLEAR;
		collidable = false;
	}
	
	// -- Informational helper functions.
	
	// Creates an Error of the given description with the default decay time of 50 steps.
	public void ERROR(String description)
	{
		ERROR(description, 120);
	}
	
	// Creates an Error of the given description with the given decay time in steps.
	public void ERROR(String description, int error_time)
	{
		if(error_time <= 0)
		{
			throw new Error("Non positive error_time entered.");
		}
		
		// Make this component the color of an error.
		//setRestingColor(Colors.C_ERROR);
		
		gui_float f = new gui_float(getScreenX() + getW() / 2, getScreenY() + getH() / 2, description, error_time);
		f.setDepth(Integer.MIN_VALUE);
		getRoom().obj_create(f);
	}
	
	// -- Data Serialization.
	
	@Override
	public BufferedImage serializeImage()
	{
		BufferedImage image  = ImageFactory.blank(getW(), getH());
		ImageB 		  imageb = new ImageB(image);
		
		
		AffineTransform AT = new AffineTransform();
		AT.translate(getX(), getY());

		draw(imageb, AT);
		
		return image;
	}
	
	// Overrides the draw borders flag if neccessary for when the mouse is in the region.
	public void setDrawBordersWhenMouseInRegion(boolean val)
	{
		if(drawBordersOnMouseHover == val)
		{
			return;
		}
		
		drawBordersOnMouseHover = val;
		redraw();
	}
	
	public void drawBordersOnlyOnHover()
	{
		setDrawBorders(false);
		setDrawBordersWhenMouseInRegion(true);
	}
	
	public void setDrawBackground(boolean flag)
	{
		if(drawBackground == flag)
		{
			return;
		}
		
		drawBackground = flag;
		redraw();
	}

	// Allow the padding to be changed.
	public void setPadding(int i)
	{
		if(padding == i)
		{
			return;
		}
		
		redraw();
		padding = i;
		redraw();
		
	}
	
}
