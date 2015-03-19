package Game_Engine.levelEditor.entities;

import java.awt.image.BufferedImage;
import java.io.PrintStream;

import Data_Structures.Structures.HashingClasses.Dict;
import Game_Engine.levelEditor.editor_components.obj_entity;

/*
 * Ent_node class.
 * 
 * Written by Bryce Summers on 5/9/2014.
 * 
 * Purpose : These nodes specify angles and may be connected to other splineNodes 
 * 			  to specify where a spline should be drawn.
 * 
 * FIXME : All of this functionality has not yet been implemented.
 * 
 * 
 */

public class Ent_splineNode extends obj_entity
{

	private Ent_splineNode other;

	// Nodes allow for highlighting.
	BufferedImage image_normal;
	BufferedImage image_highlight;
	
	// Preferably both the image and its highlight will be of the same dimensions.
	public Ent_splineNode(double x, double y, BufferedImage spr, BufferedImage highlight)
	{
		super(x, y, spr, null, null);

		image_normal = spr;
		image_highlight = highlight;
	}

	
	public void setOther(Ent_splineNode o)
	{
		other = o;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		// An implementation of selection highlighting.
		if(other != null && (mouseInRegion || other.mouseInRegion))
		{
			if(sprite != image_highlight)
			{
				redraw();
			}
			
			sprite = image_highlight;
		}
		else
		{
			if(sprite != image_normal)
			{
				redraw();
			}
			sprite = image_normal;
		}
		
	}
	
	// -- Entity methods.
	
	@Override
	public obj_entity clone(int x, int y)
	{
		return new Ent_splineNode(x, y, image_normal, image_highlight);
	}

	@Override
	public void serializeTo(PrintStream stream)
	{
		// Do not serialize these entities.
		// Overrides the serialization process.
	}
	
	// Enables subclasses to add extra properties for serialization.
	// Called by obj_entity serialization routine.
	@Override
	protected void addSerialProperties(Dict<String> dict)
	{
		// Nodes are not serialized.
		throw new Error("Nodes should not be serialized");
	}

}
