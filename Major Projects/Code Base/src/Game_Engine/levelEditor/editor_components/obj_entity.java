package Game_Engine.levelEditor.editor_components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

import util.Serializations;
import BryceMath.Calculations.Colors;
import Data_Structures.Structures.BitSet;
import Data_Structures.Structures.HashingClasses.Dict;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.obj_scalable;
import Game_Engine.GUI.Components.small.gui_handle;
import Game_Engine.SpriteFactories.ArrowImageFactory;

/*
 * Level Editor entities are little more than pictures that know their name and can be manipulated.
 * 
 * A level is created from a set of these pictures placed at various points in the level region.
 * 
 * These pictures can then be linked by arrows.
 * 
 * Streamlined 4 - 19 - 2014.
 */


public abstract class obj_entity extends obj_scalable
{
	
	// All Entities must be given a name.
	public String name;
	
	// Dynamic parameters.
	private String text = "";
	
	private BitSet direction_properties = new BitSet();
	
	private static boolean showKnobs = false;
	
	// Knobs used to resize and move entities.
	private gui_handle knob1, knob2;
	
	protected int x2, y2;
	
	public obj_entity(double x, double y, BufferedImage spr, String name, String parameter)
	{
		super(x, y);
		
		sprite = spr;
		this.name = name == null ? "" : name;
		this.text = parameter == null ? "" : parameter;

		//System.out.println(getX() + " " + getY());
		
		knob1 = new gui_handle(getX(), getY(), sprite.getWidth(), sprite.getHeight());
		knob2 = new gui_handle(getX(), getY(), sprite.getWidth(), sprite.getHeight());
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
		
		// Create knobs.
		knob1.setText("O");
		knob2.setText("O");
		
		knob1.setRestingColor(Colors.Color_hsv(0, 0, 0, 25));
		knob2.setRestingColor(Colors.Color_hsv(0, 0, 0, 25));
		
		myContainer.obj_create(knob1);
		myContainer.obj_create(knob2);
	}

	// Entities can easily clone themselves
	public abstract obj_entity clone(int x, int y);
	
	
	@Override
	// Normal Entity Serialization.
	// output the name, then output a list of value mappings that is serialized and deserialize as a dictionary.
	public void serializeTo(PrintStream stream)
	{
		// Start by printing the name of the entity on its own line.
		stream.println(name);
		Dict<String> dict = new Dict<String>();
		
		// -- Add default serial properties.
		
		dict.insert("x", "" + (int)getX());
		dict.insert("y", "" + (int)getY());
		dict.insert("x2", "" + x2);
		dict.insert("y2", "" + y2);
		
		// Serialize the direction properties bit set.
		int dp = direction_properties.toInt();
		// Make non marked entities move every way. 
		// Mark full movement entities similarly.
		if(dp == 0 || dp == 255)
		{
			dp = ~0;
		}
		dict.insert("dir", "" + dp);// Direction property.
		
		// Add this entities special parameter if it is non trivial, 
		// which can specify dynamic properties of entities.		
		String parameter = getText();
		
		if(parameter.length() > 0)
		{
			dict.insert("p1", parameter);
		}
		
		addSerialProperties(dict);
		Serializations.serial_dict(stream, dict);
	}
	
	// Enables subclasses to add extra properties for serialization.
	// Called by obj_entity serialization routine.
	protected abstract void addSerialProperties(Dict<String> dict);

	
	@Override
	public void update()
	{
		/* This is a level editor, so entities do not really do much. */
		
		if(showKnobs)
		{
			knob1.show();
			knob2.show();
		}
		else
		{
			knob1.hide();
			knob2.hide();
		}
		
		
		int x1 = (int)knob1.getX();
		int y1 = (int)knob1.getY();
		
		moveTo(x1, y1);
		
		int x2 = (int)knob2.getX();
		int y2 = (int)knob2.getY();
		
		arg2(x2, y2);
	}
	
	// FIXME : This currently prevents the second knob from 
	public void arg2(int x2_in, int y2_in)
	{
		
		// Warning, this may be spammed and lead to a design flaw.
		knob2.moveTo(x2_in, y2_in);
		
		// Snap the knobs to the grid.
		gui_level_editor.grid_drawer.snapToGrid(knob1);
		gui_level_editor.grid_drawer.snapToGrid(knob2);
		
		int x1 = (int)Math.min(knob1.getX(), knob2.getX());
		int y1 = (int)Math.min(knob1.getY(), knob2.getY());
		
		moveTo(x1, y1);

		x2 = (int)Math.max(knob1.getX(), knob2.getX());
		y2 = (int)Math.max(knob1.getY(), knob2.getY());

		double v_scale_old = v_scale;
		double h_scale_old = h_scale;
		
		v_scale = Math.abs(y2 - y1)/sprite.getHeight() + 1;
		h_scale = Math.abs(x2 - x1)/sprite.getWidth() + 1;
		
	}
	
	protected void die()
	{
		super.die();
		knob1.kill();
		knob2.kill();
	}
	
	// Entities currently die when clicked on.
	@Override
	public void mouseP(int mx, int my)
	{
		if(mouse_right())
		{
			die();
		}
	}
	
	// Kill Entities via drag as well.
	@Override
	public void mouseD(int mx, int my)
	{
		if(mouse_right())
		{
			die();
		}
	}

	// Dynamic Drawing Capability.
	public void draw(ImageB image, AffineTransform AT)
	{
		super.draw(image, AT);

		Graphics2D g = image.getGraphics();
		
		double x = getX();
		double y = getY();
		int w = getW();
		int h = getH();
		
		if(text.length() != 0)
		{
			drawTextCenter(image.getGraphics(), AT, x + w/2, y + h/2, text, 10);
		}
		
		if(mouseInRegion)
		{
			g.setColor(Color.BLUE);
			drawRect(g, AT, x + 1, y + 1, w - 2, h - 2);
		}
	}

	// Enables the arrows to be drawn for every segment of the scaled Obj.
	@Override
	public void tileDraw(ImageB image, AffineTransform AT, int x, int y, int w, int h)
	{
		// Draw arrows. // FIXME : Allow the size of the arrows depend on grid size.
		drawRadialArray(image.getGraphics(), AT, ArrowImageFactory.getArrows(64), direction_properties, x, y, w, h);
	}
	
	
	// -- Data modification functions.
	
	public void setText(String input)
	{
		text = input;
	}
	
	public String getText()
	{
		return text;
	}
	
	// Each of the last 8 bits represent whether a direction is present.
	public void setDirectionsAttributes(BitSet set)
	{
		direction_properties = set;
	}
	
	public static void setShowKnobs(boolean b)
	{
		showKnobs = b;
	}
}