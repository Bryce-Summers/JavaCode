package Game_Engine.GUI.Components.large;

import java.awt.Color;

import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.Engine.Objs.Obj_Container;
import Game_Engine.Engine.Objs.View;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.gui_display;
import Game_Engine.GUI.Components.small.gui_look_and_feel;
import Game_Engine.GUI.Components.small.gui_scrollbar;

/*
 * gui_window class.
 * 
 * Rebuilt on 6 - 2 - 2013.
 * Written by Bryce Summers.
 * 
 * Purpose : 1. This is the graphical version of an obj_container.
 * 			 2. This class encapsulates a game world into a 
 * 				scrollable window inside on a bigger screen.
 * 
 * Note : This object has two layers,
 * 			1. The first is a list of gui components including the display.
 * 			2. The second is a list of regular game components that are all contained inside the display.
 * 			These layers ensure that the background, world,
 * 			and foreground are never disturbed by the depths of incoming game objects.
 * 
 * On 8 - 18 - 2013. I added more functionality for changing the size of windows
 * 					 and allowing scrolling of the world for various sized worlds.
 * 
 * NOTE : Instead of enclosing the top layer in a union, we enclose it in a container
 * 		  to maintain the respective depths and locations of the components with the utmost confidence.
 *		  This eliminates the need for managing the depth stack and transporting elements to proper
 *		  x and y coordinates in this window's containing world.
 */


public class gui_window extends Obj_Container
{
	
	// -- Private Data.
	
	// -- The layers
	
	// - Background.
	protected gui_look_and_feel background;
	private static final int background_depth = 2;
	
	// - Intermediate 1.
	protected gui_display	world;
	private static final int world_depth = 1;
	
	// - Intermediate 2.
	private gui_scrollbar scrollV;
	private static final int scrollV_depth = 0;
	
	protected gui_scrollbar scrollH;
	private static final int scrollH_depth = 0;
	
	// - Foreground.
	private gui_look_and_feel borders;
	private static final int borders_depth = -1;
	
	// This is private, because the sub class can maintain higher confidence and readability
	// by accessing the height or width of the corresponding scrollbar.
	public static final int SCROLLBARSIZE = 25;
		
	
	/* Settings that tell the GUI to always display the scroll bars when false,
	 * and only display them when the world is bigger than the display environment when true.
	 */
	private boolean dynamic_vScroll = true;
	private boolean dynamic_hScroll = true;
	
	
	// -- Constructors.
	public gui_window(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		
	}
	
	public gui_window(Rectangle screen)
	{
		super(screen);
	}
	
	public gui_window(Rectangle screen, Rectangle sub_world, int worldW, int worldH)
	{
		super(screen);

		views.getFirst().setViewBounds(screen, sub_world);
		
		int size = SpriteLoader.gui_borderSize;
		world.getView().setWorldDimensions(worldW - size*2, worldH - size*2);

	}
	
	@Override
	protected void iViews()
	{
		// Now initialize the view.
		Rectangle screen, view;
		
		// Default View
		view = new Rectangle(0, 0, getW(), getH());
		
		// Default screen.
		screen = new Rectangle((int)getX(), (int)getY(), getW(), getH());

		// Add the views to the views list.
		views = new List<View>();
		
		// Add the mutable world view.
		views.add(new View(screen, view, getW(), getH()));
	}
		
	// -- Window definition functions.
	@Override
	public void iObjs()
	{
		int room_w = getW();
		int room_h = getH();
		
		// -- The Background Label.
		background = new gui_look_and_feel(0, 0, room_w, room_h);
		background.setDrawBorders(false);
		background.setDepth(background_depth);
		
		// Conserve drawing.
		//background.makeTransparent();
		background.setColor(Color.WHITE);
		
		obj_gui_create(background);
		
		// -- The display. (intermediate 1).

		int border_size = SpriteLoader.gui_borderSize;
	
		// Initialize the display and shrink its area so that no artifacts appear around the corners of the gui.
		Rectangle screen = new Rectangle(border_size, border_size,
				 room_w - 2*border_size, room_h - 2*border_size);
		
		world = new gui_display(screen);
		
		world.setDepth(world_depth);
		
		obj_gui_create(world);
		
		// -- Foreground.
		
		// Make A beautiful label in front of the display.
		borders = new gui_look_and_feel(0, 0, room_w, room_h);
		obj_gui_create(borders);
		
		// put it in front.
		borders.setDepth(borders_depth);
		borders.makeTransparent();
		
		// We want to still be able to register collisions in the display.
		borders.setCollidable(false);
		borders.setPadding(0);
	}
	
	// FIXME : Only create them when an object is out of bounds or when the User desires.
	public void scroll()
	{
		scrollH();
		scrollV();
	}
	
	public void scrollH()
	{
		if(scrollH != null)
		{
			scrollH.setFlag(true);
			scrollH.enable();
			return;
		}

		int size = SCROLLBARSIZE;
		
		int room_w = getW();
		int room_h = getH();
		
		// - horizontal scrollbar.
		scrollH = new gui_scrollbar(0, room_h - size, room_w, size);
		scrollH.setDepth(scrollH_depth);
		
		obj_gui_create(scrollH);
	}
	
	public void scrollV()
	{
		if(scrollV != null)
		{
			scrollV.setFlag(true);
			scrollV.enable();
			return;
		}
		
		int size = SCROLLBARSIZE;
		
		int room_w = getW();
		int room_h = getH();
		
		// - Vertical scrollbar.
		scrollV = new gui_scrollbar(room_w - size, 0, size, room_h);
		scrollV.setDepth(scrollV_depth);
		
		obj_gui_create(scrollV);		
	}

	// Allow the window to be made unscrollable.
	public void unscroll()
	{
		unscrollV();
		unscrollH();
	}
	
	public void unscrollV()
	{
		if(scrollV == null)
		{
			return;
		}
		
		if(!dynamic_vScroll)
		{
			scrollV.disable();
			world.getView().setViewY(0);
			return;
		}
		
		scrollV.kill();
		scrollV = null;
		
		world.getView().setViewY(0);
		
	}
	
	public void unscrollH()
	{
		if(scrollH == null)
		{
			return;
		}

		if(!dynamic_hScroll)
		{
			scrollH.disable();
			world.getView().setViewX(0);
			return;
		}
		
		scrollH.kill();
		scrollH = null;
		world.getView().setViewX(0);
	}

	// This allows for components to be created in this window's gui layers.
	public Obj obj_gui_create(Obj o)
	{
		return super.obj_create(o);
	}

	// This allows for the sub display components to all be placed in the same display layer.
	@Override
	public Obj obj_create(Obj o)
	{
		return world.obj_create(o);
	}
	
	@Override
	public void update()
	{
		if(!isEnabled())
		{
			return;
		}
		
		// Very important.
		super.update();

		getView().setScreenX((int)getX());
		getView().setScreenY((int)getY());
		
		/*
		world.getView().setScreenX((int)x);
		world.getView().setScreenY((int)y);
		*/
		
		handleScrollbars();
	}

	// FIXME : This method can probably be refactored.
	
	// This method updates the display according to the input of the scrollbars.
	public void handleScrollbars()
	{
		// -- Scroll the display to the proper y coordinate.
		if(scrollH != null && scrollH.flag())
		{
			View view = world.getView();

			int border_size = SpriteLoader.gui_borderSize;
			
			// The height of the entire world's display.
			int world_width = view.getWorldW();
			
			// The height of the window on the screen.
			int window_w = view.getScreenW();
			
			// The maximum x coordinate that we can scroll to in good taste.
			// (I believe that the minimum is zero.)
			// The width of the visible portion of the window.
			int max_x = world_width - window_w + border_size*2;
			
			// We need extra scrolling to account for the
			// space taken by the vertical scrollbar.
			if(scrollV != null)
			{
				max_x += scrollV.getW() - border_size;
			}
			
			int x_new = (int) (max_x*scrollH.getXValue());

			// Mutate the view.
			view.setViewX(x_new);
			
			if(view.flag())
			{
				redraw();
			}
			
		}
		
		// -- Scroll the display to the proper y coordinate.
		if(scrollV != null && scrollV.flag())
		{

			View view = world.getView();

			int border_size = SpriteLoader.gui_borderSize;
			
			
			// The height of the entire world's display.
			int world_height = view.getWorldH();
			
			// The height of the window on the screen.
			int window_h = view.getScreenH();
			
			// The maximum y coordinate that we can scroll to in good taste.
			// (I believe that the minimum is zero.)
			// The height of the visible portion of the window.
			int max_y = world_height - window_h + border_size*2;
			
			// We need extra scrolling to account for the
			// space taken by the horizontal scrollbar.
			if(scrollH != null)
			{
				max_y += scrollH.getH() - border_size;
			}
			
			int y_new = (int) (max_y*scrollV.getYValue());

			// Mutate the view.
			view.setViewY(y_new);
			
			if(view.flag())
			{
				redraw();
			}

		}
		
	}// <-- End of handle Scrollbars.
	
	public void setColor(Color c)
	{
		background.setColor(c);
	}
	
	// More powerful than setColor, this function sets the resting color for this window,
	// which cannot be changed by transient setColor calls.
	public void setRestingColor(Color c)
	{
		background.setRestingColor(c);
	}
	
	@Override
	public void disable()
	{
		super.disable();
		world.disable();
	}
	
	@Override
	public void enable()
	{
		super.enable();
		
		world.enable();
	}
	
	public void setDrawBorders(boolean flag)
	{
		borders.setDrawBorders(flag);
	}
	
	@Override
	public void global_mouseScroll(int amount)
	{
		
		if(!mouseInRegion || scrollV == null)
		{
			return;
		}

		scrollV.scroll(amount);
		handleScrollbars();
	}
	
	
	// Update the borders, background, and display to the new height.
	@Override
	public void setH(int h)
	{
		
		boolean redraw = super.getH() != h;
		
		// Ensure GUI redrawing accuracy.
		if(redraw)
		{
			redraw();
		}
		else
		{
			return;
		}
		
		super.setH(h);
		background.setH(h);
		borders.setH(h);
		
		// We have to update the values in the first layer.
		View v = getView();		
		v.setScreenH(h);
		v.setViewH(h);
		v.setWorldH(h);
		
		// We then have to update the values in the second layer.
		h -= SpriteLoader.gui_borderSize*2;
		v = world.getView();
		
		v.setScreenH(h);
		v.setViewH(h);
		v.setWorldH(h);
		
		world.setH(h);
		
		// Update the position of the horizontal scrollbar.
		if(scrollH != null)
		{
			scrollH.setY(getH() - scrollH.getH());
		}
		
		// Fix vertical scrollbar.
		if(scrollV != null)
		{
			scrollV.setH(h + SpriteLoader.gui_borderSize*2);
			double percentage = scrollV.getYValue();
			unscrollV();
			scrollV();
			scrollV.setYValue(percentage);
		}
		
		// Ensure GUI redrawing accuracy.
		if(redraw)
		{
			redraw();
		}
	}
	
	// Update the borders, background, and display to the new height.
	// FIXME : TESTME!
	@Override
	public void setW(int w)
	{
		boolean redraw = super.getW() != w;
		
		// Ensure GUI redrawing accuracy.
		if(redraw)
		{
			redraw();
		}
		else
		{
			return;
		}
		
		super.setW(w);
		background.setW(w);
		borders.setW(w);
		
		// We have to update the values in the first layer.
		View v = getView();		
		v.setScreenW(w);
		v.setViewW(w);
		v.setWorldW(w);
		
		// We then have to update the values in the second layer.
		w -= SpriteLoader.gui_borderSize;
		v = world.getView();
		
		v.setScreenW(w);
		v.setViewW(w);
		v.setWorldW(w);
		
		world.setW(w);
		
		// Update the position of the horizontal scrollbar.
		if(scrollV != null)
		{
			scrollV.setX(getW() - scrollV.getW());
		}
		
		if(scrollH != null)
		{
			scrollH.setW(w + SpriteLoader.gui_borderSize*2);
			double percentage = scrollH.getXValue();
			unscrollV();
			scrollV();
			scrollV.setXValue(percentage);
		}
		
		// Ensure GUI redrawing accuracy.
		if(redraw)
		{
			redraw();
		}
	}
	
	
	// Allow scrolling of the world from x = 0 to x = worldW.
	public void scrollH(int worldW)
	{
		View v = world.getView();
		
		v.setWorldW(worldW);
		scrollH();
	}
	
	// Allow scrolling of the world from y = 0 to y = worldW.
	public void scrollV(int worldH)
	{
		View v = world.getView();
		
		v.setWorldH(worldH);
		scrollV();
	}
	
	public gui_display getWorld()
	{
		return world;
	}
	
	// Sends a Graphically depicted message to the user.
	protected void ERROR(String s)
	{
		background.ERROR(s);
	}
	
	// Encapsulation functions for setting dynamic scrollbar appearances.
	public void setDynamicVScroll(boolean b)
	{
		dynamic_vScroll = b;
		
		// Always on if non dynamic.
		if(!b)
		{
			scrollV();
		}
	}
	
	// Encapsulation functions for setting dynamic scrollbar appearances.
	public void setDynamicHScroll(boolean b)
	{
		dynamic_hScroll = b;
		
		// Always on if non dynamic.
		if(!b)
		{
			scrollH();
		}
	}
	
	public void makeTransparent()
	{
		background.makeTransparent();
	}
	
	// Commands the vertical scroll bar to go to the desired percentage.
	public void setScrollV(double percentage)
	{
		if(scrollV != null)
		{
			scrollV.setYValue(percentage);
		}
	}
	
	// COmmands the vertical scroll bar to go to the desired percentage.
	public void setScrollH(double percentage)
	{
		if(scrollH != null)
		{
			scrollH.setXValue(percentage);
		}
	}
	
	// Boolean is scrolling functions.
	public boolean isScrollingH()
	{
		return scrollH != null;
	}
	
	public boolean isScrollingV()
	{
		return scrollV != null;
	}
	
}// <-- End of Class.