package Game_Engine.Engine.Objs;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

import util.SerialB;
import util.interfaces.Function;
import BryceMath.Calculations.Geometry;
import BryceMath.DoubleMath.Vector;
import BryceMath.Geometry.Rectangle;
import Data_Structures.Structures.BitSet;
import Data_Structures.Structures.List;
import Game_Engine.Engine.Objs.actionLogging.obj_cursor;
import Game_Engine.Engine.engine.Game_input;
import Game_Engine.Engine.text.TextManager;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.boxes.gui_focusBox;

/*
 * The Object Class. Reconceived by Bryce Summers on 12 - 31 - 2012.
 * Purpose: To provide the game developer with a standardized 
 * 			super class for implementing Game components,
 * 			which interfaces with the Game Engine.
 * 
 * Objects also support at toSerial function that specifies a serialization for an object.
 * 
 * Objects support manual bounding rectangles, 
 * which allows them to represent arbitrary constructive drawings on the screen.
 */

// FIXME : update this out of date code.
// FIXME : Consider replacing the double x and y with rationals or ints.
// FIXME : Outsource rectangle like functions to the rectangle class.
// FIXME : Remove references to AffineTransforms, because they are not needed externally.

public abstract class Obj implements Comparable<Obj>, SerialB
{
	// FIXME : Update objects to use rectangles.
	
	// -- Local variables.
	private double y;// FIXME : Use the refactoring tool to encapsulate these fields.
	private double x;

	public double x_start, y_start;
	
	private int w = -1, h = -1;
	
	// Variables to communicate with this Object's container.	
	private boolean alive = true;
	private boolean end_step = false;
	private Obj_Container part_dead = null;
	
	private int depth = 0;
	
	// This is used as a flag that indicates the first update step.
	// This will eliminate many extraneous flags in sub classes.
	protected boolean initialized = false;
	
	// This equals itself if it is the room, otherwise it equals the container the this object is contained within.
	protected Obj_Container myContainer;
	
	// Drawing variables.
	protected BufferedImage sprite;
	protected double sprite_angle;
	protected int orgin_x = 0;
	protected int orgin_y = 0;
	
	// The thresholf that needs to be exceeded for collision checking on the image.
	private int alpha_collision_threshold = 0;
	private boolean visible = true;
	protected boolean collidable = true;
	
	// FIXME : Refactor this to clearly indicate how the objects physical presense on the screen is implemented.
	// Used to indicate changes in the the bounding box.
	protected int h_scale = 1;
	protected int v_scale = 1;
	
	// Stores whether the mouse is inside this Object's region.
	// Every object is responsible for setting this to true when necessary.
	// It is the container tree's responsibility to set these flags back to false when necessary.
	public boolean mouseInRegion = false;
	
	public boolean highlighted = false;
	
	// Objects know whether they have full functionality yet.
	private boolean enabled = true;
	
	public static boolean default_TEX = true;
	
	// Bryce TEX is a special formatting capability that can be enabled within any objects.
	private boolean TEX = default_TEX;
	
	// Used to maintain the integrity of the update loop.
	// Allows objects to force the update of another object before themselves.
	private boolean updated = false;
	
	// -- Constructors
	public Obj(double x_in, double y_in)
	{
		iVars(x_in, y_in);
	}
	
	// Generic constructor for an Object that does not care about its world's geometry.
	public Obj()
	{
		iVars(0, 0);
	}
	
	private void iVars(double x_in, double y_in)
	{
		setX(x_in);
		setY(y_in);
		
		x_start = getX();
		y_start = getY();
		
		redraw();
	}
	
	// Allows for object to initialize themselves after the first step.
	// This allows for objects to be initialized utilizing the full capabilities of an object.
	protected void initialize(){/* Do Nothing much */}
	
	// -- Standard mostly abstract methods.

	// Allow all objects to update themselves once per frame.
	protected abstract void update();
	
	// Note : Objects will only kill themselves during the update step.
	public void endStep(){/* Do Nothing */}

	// -- Handle Keyboard input.
	
	// Key Pressed.
	public void keyP(int key){/*Do Nothing*/}
	public void forceKeyP(int key){/* Do Nothing */}
	
	// Key Released.
	public void keyR(int key){/*Do Nothing*/}
	
	// -- Handle Mouse input.
	
	// This integer inputs represent the location of the cursor 
	// with regards to this object's container's coordinate system.
	
	// Mouse Pressed. 
	public void mouseP(int mx, int my){/*Do Nothing*/}
	
	// Mouse Released.
	public void mouseR(int mx, int my){/*Do Nothing*/}
	
	// Mouse moved.
	public void mouseM(int mx, int my){/*Do Nothing*/}
	
	// Mouse Dragged.
	public void mouseD(int mx, int my){/*Do Nothing*/}
	
	// -- Mouse button query functions.
	protected boolean mouse_left()
	{
		return Game_input.mouse_button == MouseEvent.BUTTON1;
	}
	
	protected boolean mouse_center()
	{
		return Game_input.mouse_button == MouseEvent.BUTTON2;
	}
	
	protected boolean mouse_right()
	{
		return Game_input.mouse_button == MouseEvent.BUTTON3;
	}
	
	// -- Handle Global Mouse input.

	// Global Mouse Pressed. 
	public void global_mouseP(){/*Do Nothing*/}
	
	// Global Mouse Released.
	public void global_mouseR(){/*Do Nothing*/}
	
	// Global Mouse moved.
	public void global_mouseM(int mx, int my){/*Do Nothing*/}
	
	// Global Mouse Dragged.
	public void global_mouseD(int mx, int my){/*Do Nothing*/}
	
	public void global_mouseScroll(int amount){/*Do Nothing */}
	
	// -- Engine Functions.

	// Objects know how to die.
	protected void die()
	{
		alive = false;
		
		// Don't leave zombie images on the screen.
		redraw();
	}
	
	// FIXME : Integrate this functionality into the kill() function using myContainer.
	public void kill(Obj_Container container)
	{
		part_dead = container;
	}
	
	//
	protected boolean dead()
	{
		return !alive;
	}
	
	// A special dead check for a particular container.
	public boolean dead(Obj_Container container)
	{
		return dead() || part_dead == container;
	}
	
	// Removes this object's death flags.
	public void ressurect()
	{
		alive     = true;
		part_dead = null;
	}
	
	// Allow outside object to kill other objects.
	public void kill()
	{
		die();
	}
	
	public void revert()
	{
		setX(x_start);
		setY(y_start);
	}
	
	// Allows for Objects to signal for their Containers to cease iteration.
	boolean getCeaseIteration()
	{
		if(end_step)
		{
			end_step = false;
			return true;
		}
		
		return false;
	}
	
	protected void breakIteration()
	{
		end_step = true;
	}
	
	// Allows Objects to be sorted by depth.
	public int compareTo(Obj o)
	{
		// Note: Smaller depths mean the object is closer to the viewer.
		if(depth > o.getDepth())
		{
			return -1;
		}
		
		if(depth < o.getDepth())
		{
			return 1;
		}
		
		return 0;
	}
	
	// -- Data access methods.
	
	public int getDepth()
	{
		return depth;
	}
	
	public int getW()
	{
		if(sprite == null || w != -1)
		{
			return w;
		}
	
		return sprite.getWidth()*h_scale;
	}
	
	public int getH()
	{
		if(sprite == null || h != -1)
		{
			return h;
		}
		
		return sprite.getHeight()*v_scale;
	}
	
	// Height setting function for Objs with no image.
	public void setH(int h_in)
	{
		if(h == h_in)
		{
			return;
		}
		
		redraw();
		h = h_in;
		redraw();
	}

	// Height setting function for Objs with no image.
	public void setW(int w_in)
	{
		if(w == w_in)
		{
			return;
		}
		
		redraw();
		w = w_in;
		redraw();
	}
	
	public void setDepth(int depth_new)
	{
		// Reserve the depths closest to the user.
		if(!(this instanceof obj_cursor))
		{
			depth_new = Math.max(Integer.MIN_VALUE + 2, depth_new);
		}
		
		depth = depth_new;
		
	}
	
	public void setCollidable(boolean flag)
	{
		collidable = flag;
	}
	
	public void setVisible(boolean flag)
	{
		if(visible != flag)
		{
			redraw();
		}
		visible = flag;
	}

	public void setImage(BufferedImage image)
	{
		if(sprite != image)
		{
			redraw();
		}
		
		sprite = image;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public boolean isCollidable()
	{
		return collidable;
	}
	
	// -- Room methods.
	
	public Room getRoom()
	{
		Obj_Container result = myContainer;
		
		while(!(result instanceof Room))
		{
			result = result.getContainer();
		}
		
		return (Room) result;
	}
	
	// Change the container of this object.
	public void setContainer(Obj_Container c)
	{
		myContainer = c;
	}
	
	public Obj_Container getContainer()
	{
		return myContainer;
	}
	
	protected void room_restart()
	{
		// Ensure that this only gets called once.
		end_step = true;
		getRoom().restart();
	}
	
	// Takes the name of a room and sends the game to this room.
	protected void room_goto(String name)
	{
		// Ensure that this call only gets called once.
		end_step = true;
		getRoom().goto_room(name);
	}
	
	// Takes the name of a room and sends the game to this room.
	protected void room_goto(Room r)
	{
		// Ensure that this call only gets called once.
		end_step = true;
		getRoom().goto_room(r);
	}	
	
	// -- Drawing methods
	
	// A drawing method that allows the containers to manage the drawing tree.
	// Functions just like a normal drawing method for perfunctory objects.
	public void draw(ImageB i, AffineTransform AT, CountDownLatch latch)
	{
		// Perform the drawing.
		draw(i, AT);
		
		// Decrement the latch.
		latch.countDown();
	}
	
	// This is the method that should be overridden for normal objects.
	public void draw(ImageB i, AffineTransform AT)
	{
		
		// Only draw objects that are visible.
		if (!visible)
		{
			return;
		}
		
		Graphics2D g = i.getGraphics();
		
		// Draw the image.
		drawImage(g, AT, getX(),getY(), sprite, sprite_angle);
		
	}
	
	// Draw images rotated about a pivot and placed with the pivot at the given point.
	public void drawImage(Graphics2D g, AffineTransform AT, double x, double y,
						  BufferedImage image, double pivotX, double pivotY, double angle)
	{ 

		// First create a new Affine transform object, because the original affineTransform should not be modified!
		AffineTransform newTransform = new AffineTransform();
		
		// Set the translation to location of the object
		newTransform.setToTranslation(x - pivotX + AT.getTranslateX(),
									  y - pivotY + AT.getTranslateY());
		
		// Rotate with the anchor point as the mid of the image 
		if(angle != 0)
		{
			newTransform.rotate(Math.toRadians(-angle), pivotX, pivotY);
		}

		// Draw the Image.
		g.drawImage(image, newTransform, null);
	}
	
	// Draw images rotated around the instance's orgin
	// and placed with the pivot at the orgin,
	// and drawn with the orgin at the given coordinates
	public void drawImage(Graphics2D g, AffineTransform AT, double x, double y, BufferedImage image, double angle)
	{		
		drawImage(g, AT, x, y, image, orgin_x, orgin_y, angle);																
	}
	
	
	public void drawImage(Graphics2D g, AffineTransform AT,double x, double y, BufferedImage image)//draw normal images with left corner at point
	{
		g.drawImage(image, (int)(x + AT.getTranslateX()), (int)(y + AT.getTranslateY()), null);
	}
	
	public static void drawImage(Graphics2D g, AffineTransform AT, BufferedImage image)
	{
		g.drawImage(image, AT, null);
	}
	
	public void fillRect(Graphics2D g, double x, double y, int w, int h)
	{
		g.fillRect((int)(x),(int)(y), w, h);
	}
	
	public void fillRect(Graphics2D g, AffineTransform affineTransform, double x, double y, int w, int h)
	{
		g.fillRect((int)(x + affineTransform.getTranslateX()),(int)(y + affineTransform.getTranslateY()), w, h);
	}
	
	public void drawRect(Graphics2D g, AffineTransform affineTransform, double x, double y, int w, int h)
	{
		g.drawRect((int)(x + affineTransform.getTranslateX()),(int)(y + affineTransform.getTranslateY()), w, h);
	}
	
	public void fillOval(Graphics2D g, AffineTransform affineTransform, double x, double y, int w, int h)
	{
		g.fillOval((int)(x + affineTransform.getTranslateX()),(int)(y + affineTransform.getTranslateY()), w, h);
	}
	
	public void drawLine(Graphics2D g, AffineTransform AT, double x1, double y1, double x2, double y2)
	{
		if(AT == null)
		{
			g.drawLine( (int)(x1),(int)(y1),
						(int)(x2),(int)(y2));
			return;
		}
		g.drawLine( (int)(x1 + AT.getTranslateX()),(int)(y1 + AT.getTranslateY()),
					(int)(x2 + AT.getTranslateX()),(int)(y2 + AT.getTranslateY()));
	}
	
	// Draws the indicated text left aligned onto the screen at the given location.
	public void drawTextLeft(Graphics2D g, AffineTransform AT, double x, double y, String str)
	{
		TextManager.drawTextLeft(g, AT, x, y, str, TEX);
	}
	
	// Draws the indicated text left aligned onto the screen at the given location with the given text height.
	public void drawTextLeft(Graphics2D g, AffineTransform AT, double x, double y, String str, int size)
	{
		TextManager.drawTextLeft(g, AT, x, y, str, size, TEX);
	}
	
	// Draws the indicated text centered at the given location.
	public void drawTextCenter(Graphics2D g, AffineTransform AT, double x, double y, String str)
	{
		TextManager.drawTextCenter(g, AT, x, y, str, TEX);
	}
	
	// Draws the indicated text centered at the given location,
	// with the given text height.
	// returns the x coordinate that the text is drawn at.
	public int drawTextCenter(Graphics2D g, AffineTransform AT,
									double x, double y, String str, int size)
	{
		return TextManager.drawTextCenter(g, AT, x, y, str, size, TEX);
	}
	
	// Draws the indicated text centered at the given location.
	public void drawTextRight(Graphics2D g, AffineTransform AT, double x, double y, String str)
	{
		TextManager.drawTextRight(g, AT, x, y, str, TEX);
	}
	
	// Draws the indicated text centered at the given location,
	// with the given text height.
	public void drawTextRight(Graphics2D g, AffineTransform AT,
									double x, double y, String str, int size)
	{
		TextManager.drawTextRight(g, AT, x, y, str, size, TEX);
	}

	
	// This is very accurate collision detection for images
	// to make a spot on an image uncollidable, just render that spot with an alpha value of zero!!!
	public boolean mouseCollision(int mouseX, int mouseY)
	{	

		if(!visible||!collidable){return false;}

		// Puts these variables relative to the object's coordinates.
		mouseX -= getX() - orgin_x;
		mouseY -= getY() - orgin_y;
		
		if(sprite == null)
		{
			if(Geometry.rectangle2(mouseX, mouseY, 0, 0, getW(), getH()))
			{
				mouseInRegion = true;
				return true;
			}

			return false;
		}		
		
		return sprite_collision(mouseX, mouseY);
	}
	
	// Performs a collision check for a sprite.
	// Handles obj_scalable scales as well.
	protected boolean sprite_collision(int mouseX, int mouseY)
	{
		
		int w = getW();
		int h = getH();
		
		if(mouseX >= 0 && mouseY >= 0 && mouseX < w && mouseY <  h 
					)//gets the alpha value from the pixel in the image;
				{	
					w = sprite.getWidth();
					h = sprite.getHeight();
			
					// Shift coordinates to top left image.
					mouseX = mouseX % w;
					mouseY = mouseY % h;
					
					// Local image collision check.
					if(sprite.getRGB(mouseX, mouseY) >>> 24 > alpha_collision_threshold)
					{
						mouseInRegion = true;
						return true;
					}					

				}
		
		return false;
	}
	
	public void center_orgin()
	{
		orgin_x = sprite.getWidth() /2;
		orgin_y = sprite.getHeight()/2;
	}
	
	// -- Collision code.

	
	// Returns the first Obj that contains a collidable pixel at the given location.
	public Obj instance_position(double x, double y, Class<?> class1)
	{
		return instance_position(x, y, class1, null);
	}

	// Returns the first Obj that contains a collidable pixel at the given location that also matches the given filter.
	public Obj instance_position(double x, double y, Class<?> class1, Function<Obj, Boolean> filter)
	{
		List<Obj> L = myContainer.getObjList();
		
		for(Obj o: L)
		{

			if(!class1.isAssignableFrom(o.getClass()) || o == this || !o.collidable)
			{
				continue;
			}
			
			if(filter != null && !filter.eval(o))
			{
				continue;
			}

			// Return o if there is a collision with the object at the desired location.
			if(o.pointCollision((int)x, (int)y))
			{
				return o;
			}
			
		}
		return null;
	}

	// This simple function acts as all of the collision checking for objects!!
	// I am very proud of this Function!!
	public Obj instance_place(double x, double y, Class<?> class1)
	{
		return instance_place(x, y, class1, null);
	}
	
	// Returns the first object that this object would intersect that also passes the filter.
	public Obj instance_place(double x, double y, Class<?> class1, Function<Obj, Boolean> filter)
	{
		List<Obj> L = myContainer.getObjList();
		
		for(Obj o: L)
		{
			if(!class1.isAssignableFrom(o.getClass() ) || o == this || !o.collidable)
			{
				continue;
			}
			
			if(filter != null && !filter.eval(o))
			{
				continue;
			}

			// Return o if there is a collision.
			if(collision(x, y, o))
			{
				return o;
			}
			
		}
		return null;
	}
	
	// Returns true if this object collides with the given other object, if this object were placed at the location x, y.
	public boolean collision(double x, double y, Obj o)
	{
		// Note: the origins are there, because objects might draw their images offset from their mathematical coordinate!!
		return
		   o.getX() - o.orgin_x				< x - orgin_x + getW()	&&
		   o.getX() - o.orgin_x + o.getW()	> x - orgin_x && 
		   o.getY() - o.orgin_y				< y - orgin_y + getH()	&&
		   o.getY() - o.orgin_y + o.getH()	> y - orgin_y;
	}
	
	// Returns true if this object collides with the given other object, if this object were placed at the location x, y.
	public boolean pointCollision(int x, int y)
	{
		if(!visible||!collidable){return false;}

		// Puts these variables relative to the object's coordinates.
		x -= getX() - orgin_x;
		y -= getY() - orgin_y;
		
		if(sprite == null)
		{
			if(Geometry.rectangle2(x, y, 0, 0, getW(), getH()))
			{
				return true;
			}

			return false;
		}
		
		if(x >= 0 && y >= 0 && x < sprite.getWidth() &&
		   y <  sprite.getHeight() && // In sprite rectangle;
			sprite.getRGB(x, y) >>> 24 > alpha_collision_threshold)//gets the alpha value from the pixel in the image;
		{	
			return true;
		}

		return false;
	}
	
	// Returns true if and only if this object currently collides with o.
	public boolean collision(Obj o)
	{
		return collision(getX(), getY(), o);
	}
	
	// Used to reset each Object's internal mouse Flags.
	public void resetMouseFlags()
	{
		// FIXME : This redrawing should be done somewhere else.
		if(mouseInRegion || highlighted)
		{
			redraw();
		}
		
		mouseInRegion = false;
		highlighted = false;
	}
	
	public Vector getPositionVector()
	{
		return new Vector(getX(), getY());
	}
	
	public Vector getStartingPositionVector()
	{
		return new Vector(x_start, y_start);
	}
	
	public int getRoomW()
	{
		return myContainer.getW();
	}
	
	public int getRoomH()
	{
		return myContainer.getH();
	}

	public boolean getInitialized()
	{
		return initialized;
	}
	
	public void setInitialized(boolean flag)
	{
		initialized = flag;
	}
	
	public void enable()
	{
		if(!enabled)
		{
			redraw();
		}
		
		enabled = true;
	}
	
	public void disable()
	{
		if(enabled)
		{
			redraw();
		}
		enabled = false;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean flag)
	{
		if(enabled != flag)
		{
			redraw();
		}
		enabled = flag;
	}
	
	public double getX2()
	{
		return getX() + getW();
	}
	
	public double getY2()
	{
		return getY() + getH();
	}

	// -- Object Serialization.
	
	@Override
	// Override this to specify the serializing of more specialized objects to a file.
	public void  serializeTo(PrintStream stream)
	{
		stream.println(getClass());
		stream.println("You should implement serialization for this class.");
		stream.println();
		throw new Error("Obj Serialization has not yet been implemented for: " + getClass().toString());
		/*
		stream.println(getClass());
		stream.println("x\n" + x);
		stream.println("y\n" + y);
		stream.println("x_start\n" + x_start);
		stream.println("y_start\n" + y_start);
		stream.println("visible\n" + getVisible());
		stream.println("collidable\n" + getCollidable());
		stream.println("enabled\n" + isEnabled());
		*/
	}
	
	@Override
	// The method that specifies which identifier will be used for this object.
	// Dangerous behavior could arise if two Objects specify the same identifier.
	public String getSerialName()
	{
		return "Temporary Serial Name : " + getClass().toString();
	}
	
	// -- Bryce TEXING flag methods.
	public void enableTEX()
	{
		if(TEX == false)
		{
			redraw();
			TEX = true;
		}
	}
	
	public void disableTEX()
	{
		if(TEX == true)
		{
			redraw();
			TEX = false;
		}
	}
	
	public boolean getTEX()
	{
		return TEX;
	}
	
	// Disables this object and makes it invisible.
	public void hide()
	{
		setVisible(false);
		disable();
	}
	
	// Enables this object and makes it visible.
	public void show()
	{
		setVisible(true);
		enable();
	}

	// Constants for Drawing tolerances.
	final int DRAW_TOLERANCE = 3 + SpriteLoader.gui_borderSize;
	final int DRAW_T2 = DRAW_TOLERANCE*2;
	
	// Have the GUI pipeline redraw this Obj's area to the screen.
	public void redraw()
	{
		/*
		// Find Calling functions to a piece of code.
		try
		{
			throw new Error();
		}
		catch(Error e)
		{
			e.printStackTrace();
		}
		//*/

		
		try
		{
			int x = getScreenX();
			int y = getScreenY();
			
			Rectangle rect = new Rectangle(x - DRAW_TOLERANCE - orgin_x,
										   y - DRAW_TOLERANCE - orgin_y,
										   getW() + DRAW_T2,
										   getH() + DRAW_T2);
			
			addDrawingRegion(rect);
		}
		catch(Exception e){};
	}	
	
	// Method for drawing optimized programs.
	public void addDrawingRegion(Rectangle rect)
	{
		getRoom().addDrawingRegion(rect);
	}
	
	// Returns the this Object's x coordinate in Screen space.
	public int getScreenX()
	{
		if(getRoom() == this)
		{
			return (int)getX();
		}
		
		return (int)(getX() + myContainer.getScreenX());
	}

	// Returns the this Object's y coordinate in Screen space.
	public int getScreenY()
	{
		if(getRoom() == this)
		{
			return (int)getY();
		}
		
		return (int)(getY() + myContainer.getScreenY());
	}

	/* 
	 * Encapsulation methods for Objects.
	 * These allow for geometric transformations to have side effects,
	 * such as GUI rendering optimization.
	 */
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getCenterX()
	{
		return x - orgin_x + w/2;
	}
	
	public double getCenterY()
	{
		return y - orgin_y + h/2;
	}
	
	public void xAdd(double xinc)
	{
		if(xinc == 0)
		{
			return;
		}
		
		redraw();
		x += xinc;
		redraw();
	}
	
	public void yAdd(double yinc)
	{
		if(yinc == 0)
		{
			return;
		}
		
		redraw();
		y += yinc;
		redraw();
	}
	
	public void xSub(double xinc)
	{
		if(xinc == 0)
		{
			return;
		}
		
		redraw();
		x -= xinc;
		redraw();
	}
	
	public void ySub(double yinc)
	{
		if(yinc == 0)
		{
			return;
		}
		
		redraw();
		y -= yinc;
		redraw();
	}
	
	public void setX(double x_in)
	{
		if(x_in == getX())
		{
			return;
		}
		
		redraw();
		x = x_in;
		redraw();
	}
	
	public void setY(double y_in)
	{
		if(y_in == getY())
		{
			return;
		}
		
		redraw();
		y = y_in;
		redraw();
	}
	
	// Centers this Object's origin on the center of its sprite.
	public void center_sprite_orgin()
	{
		int x, y;
		
		x = sprite.getWidth()/2;
		y = sprite.getHeight()/2;
		
		if(orgin_x == x && orgin_y == y)
		{
			return;
		}
		
		redraw();
		orgin_x = x;
		orgin_y = y;
		redraw();
	}
	
	// Safely sets this object's sprite.
	public void setSprite(BufferedImage image)
	{
		if(sprite == image)
		{
			return;
		}
		
		redraw();
		sprite = image;
		redraw();
	}
	
	public boolean offScreen()
	{
		int room_w = getContainer().getW();
		int room_h = getContainer().getH();
		
		int x = 0;
		int y = 0;
		
		int w = getW();
		int h = getH();
		
		boolean b1, b2, b3, b4;
		
		b1 = x < -w + orgin_x;
		b2 = y < -h + orgin_y;
		b3 = x > room_w + orgin_x;
		b4 = y > room_h + orgin_y;

		return b1 || b2 || b3 || b4;
		
	}
	
	// Make this Object seen above the given object.
	public void setDepthAbove(Obj o)
	{
		setDepth(o.getDepth() - 1);
	}
	
	// Make this Object seen below the given object.
	public void setDepthBelow(Obj o)
	{
		setDepth(o.getDepth() + 1);
	}
	
	public void setOrginX(int x)
	{
		if(x == orgin_x)
		{
			return;
		}
		
		redraw();
		orgin_x = x;
		redraw();
	}
	
	public void setOrginY(int y)
	{
		if(y == orgin_y)
		{
			return;
		}
		
		redraw();
		orgin_y = y;
		redraw();
	}
	
	public void setOrgin(int x, int y)
	{
		setOrginX(x);
		setOrginY(y);
	}
	// Updates this object, iff it has not yet been updated this step.
	// This can be used to make sure objects update in a well defined order.
	// For example, in obj_union it can be made so that all of the components update in a cohesive manner without gaps.
	public void ensureUpdate()
	{
		if(!updated)
		{
			updated = true;
			update();
		}
	}
	
	// resets the update flag for the next step.
	public void resetUpdateFlagForNextStep()
	{
		updated = false;
	}
	
	// Moves this object to the other object.
	public void moveTo(Obj o)
	{
		setX(o.getX());
		setY(o.getY());
	}
	
	public void moveTo(double x, double y)
	{
		setX(x);
		setY(y);
	}
	
	// Draws an array of images at 45 degree increments.
	// Excludes locations not in the given bit set.
	// array should contain 8 images.
	// Commonly used to communicate unique properties such as directional ability to the user. 
	protected void drawRadialArray(Graphics2D g, AffineTransform AT, BufferedImage[] array, BitSet bits)
	{
		drawRadialArray(g, AT, array, bits, getX(), getY(), getW(), getH());
	}
	
	protected void drawRadialArray(Graphics2D g, AffineTransform AT, BufferedImage[] array, BitSet bits, double x, double y, int w, int h)
	{
		if(array.length != 8)
		{
			throw new Error("Non 8 length arrays not supported.");
		}
		
		switch (bits.toInt())
		{
			// None.
			case 0: //drawTextCenter(image.getGraphics(), AT, x + w/2, y + h/2, "X", 10);
					break;
			// All.
			case 255:drawTextCenter(g, AT, x + w/2, y + h/2, "O", 10);
					 break;
			// All.
			case ~0:break;
			default:
			for(int i = 0; i < 8; i++)
			{
				directionDraw(g, AT, i, array, bits);
			}
			break;
		}
	}
	
	// Draw a direction to the screen, if it is present. Helper function for draw Radial Array.
	private void directionDraw(Graphics2D g, AffineTransform AT, int direction, BufferedImage[] array, BitSet bits)
	{
		if(bits.getBit(direction))
		{
			// Offset the image drawing by their directions.
			Vector v_dir = Vector.v_dir(45*(direction)).mult(16);
			
			BufferedImage spr = array[direction];
			
			drawImage(g, AT, getX() + getW()/2  - orgin_x - spr.getWidth()/2  + v_dir.getX(), 
							 getY() + getH()/2 - orgin_y - spr.getHeight()/2 + v_dir.getY(), spr);
		}
	}

	// Some syntactic sugar that enables objects to erase 
	// any focused graphic user interface components.
	protected static void eraseSelections()
	{
		gui_focusBox.eraseSelections();
	}
	
	public void setCollisionAlphaThreshold(int val)
	{
		alpha_collision_threshold = val;
	}

	
}