package Game_Engine.Engine.Objs;

import java.awt.geom.AffineTransform;
import java.util.concurrent.CountDownLatch;

import BryceImages.Operations.Drawing;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.List;
import Game_Engine.GUI.Interfaces.Pingable;

/*
 * View, written by Bryce Summers on 1 - 4 - 2012.
 * Purpose: Allows for various parts of the screen to be "windows" into the game world.
 */

// FIXME, FIXME, FIXME !!! Major bugs in this class. Improve the drawing efficiency!!!

// FIXME : IDEA! Perhaps instead of creating new images, I should just bound the drawing area on each pass.

// The screen should be drawn according to the screen size, not the world size.

// The Only the portion of the screen that exists inside of the given view should be rendered.

/* Note : All mutations check whether an actual 
 * mutation has been requested and set the changed flag to true if so.
 */

public class View implements Pingable
{
	// The screen is the rectangle that is the view scaled to screen space.
	private Rectangle screen;
	// The view is a rectangle boundary for what contents to be displayed.
	private Rectangle view;
	
	// These are used for setting explicit internal world bounds, (0, 0), (worldW, worldH);
	private int worldW, worldH;
	
	// Keeps track of whether the view has been mutated or not.
	// This is useful for drawing optimizations employed by GUI systems.
	private boolean flag;

	public View(Rectangle screen, Rectangle view, int w, int h)
	{
		this.screen = screen;
		this.view   = view;
		this.worldW = w;
		this.worldH = h;
		
	}

	// Draws The given list of objects to the given destination image scaled and translated according to this view.
	public void draw(ImageB dest, List<Obj> L, CountDownLatch latch, AffineTransform AT)
	{
		// Compute the area of the destination screen that we wish to draw to.
		Rectangle offsetBounds = screen.offset(AT.getTranslateX(), AT.getTranslateY());
		
		// Create a bounds safe Bryce Image from the given image and this view's screen bounds.
		dest = dest.getSubImage(offsetBounds);
		
		if(dest == null)
		{
			latch.countDown();
			return;
		}

		renderThread r = new renderThread(dest, L, latch, AT);

		r.start();
	}

	private class renderThread extends Thread
	{
		// Local data.
		final ImageB dest;
		final List<Obj> L;
		final CountDownLatch latch;
		final AffineTransform AT;

		private renderThread(ImageB dest, List<Obj> L, CountDownLatch latch, AffineTransform AT)
		{
			// Store all of the data.
			this.dest = dest;
			this.L = L;
			this.latch = latch;
			this.AT = Drawing.translate(AT, screen.getX() - view.getX(), screen.getY() - view.getY());
		}

		public void run()
		{

			// Formulate the world image by drawing the world's objects onto the world.
			for(Obj o : L)
			{
				// Do not draw invisible objects.
				if(o.isVisible())
				drawObj(o);
			}
			
			latch.countDown();
		}
		
		private void drawObj(Obj o)
		{
			if(!(o instanceof Obj_Container))
			{
				// Handle the normal object case.
				o.draw(dest, AT);
			}			

			CountDownLatch tempLatch = new CountDownLatch(1);
			o.draw(dest, AT, tempLatch);

			// Wait for the object container to complete its frame rendering.
			try
			{
				tempLatch.await();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}
		
	}

	public void setViewBounds(Rectangle screen, Rectangle view)
	{
		if(this.screen.equals(screen) && this.view.equals(view))
		{
			return;
		}
		
		this.screen = screen;
		this.view   = view;
		mutate();
	}

	public Rectangle getBounds()
	{
		return screen;
	}
	
	// -- View modification methods.
	// FIXME : Perhaps we should just make these immutable.
	public void setScreenX(int x)
	{
		if(screen.getX() == x)
		{
			return;
		}
		
		screen.setX(x);
		mutate();
	}
	
	public void setScreenY(int y)
	{
		if(screen.getY() == y)
		{
			return;
		}
		
		screen.setY(y);
		mutate();
	}
	
	public void setScreenDimensions(int w, int h)
	{
		setScreenW(w);
		setScreenH(h);
	}
	
	public void setScreenW(int w)
	{
		if(screen.getW() == w)
		{
			return;
		}
		
		screen.setW(w);
		mutate();
	}
	
	public void setScreenH(int h)
	{
		if(screen.getH() == h)
		{
			return;
		}
		
		screen.setH(h);
		mutate();
	}
	
	public void setViewX(int x)
	{
		if(view.getX() == x)
		{
			return;
		}
		
		view.setX(x);
		mutate();
	}
	
	public void setViewY(int y)
	{
		if(view.getY() == y)
		{
			return;
		}
		
		view.setY(y);
		mutate();
	}
	
	// Moves the view by the given offset amount.
	public void moveView(int x, int y)
	{
		setViewX(getViewX() + x);
		setViewY(getViewY() + y);
	}

	public void setViewDimensions(int w, int h)
	{
		setViewW(w);
		setViewH(h);
	}
	
	public void setViewW(int w)
	{
		if(view.getW() == w)
		{
			return;
		}
		
		view.setW(w);
		mutate();
	}
	
	public void setViewH(int h)
	{
		if(view.getH() == h)
		{
			return;
		}
		
		view.setH(h);
		mutate();
	}
	
	public void setWorldDimensions(int w, int h)
	{
		setWorldW(w);
		setWorldH(h);
	}
	
	public void setWorldW(int w)
	{
		if(worldW == w)
		{
			return;
		}
		
		worldW = w;
		mutate();
	}
	
	public void setWorldH(int h)
	{
		if(worldH == h)
		{
			return;
		}
		
		worldH = h;
		mutate();
	}
	
	// -- View information methods.

	public int getScreenX()
	{
		return screen.getX();
	}

	public int getScreenY()
	{
		return screen.getY();
	}

	public int getScreenW()
	{
		return screen.getW();
	}

	public int getScreenH()
	{
		return screen.getH();
	}
	
	public int getViewX()
	{
		return view.getX();
	}

	public int getViewY()
	{
		return view.getY();
	}
	
	public int getViewW()
	{
		return view.getW();
	}
	
	public int getViewH()
	{
		return view.getH();
	}
	
	public int getWorldW()
	{
		return worldW;
	}
	
	public int getWorldH()
	{
		return worldH;
	}

	// FIXME : See if there is a trunary operator implementation of this syntax.
	@Override
	public boolean flag()
	{
		if(flag)
		{
			flag = false;
			return true;
		}
		
		return false;
	}

	@Override
	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}
	
	private void mutate()
	{
		flag = true;
	}
}