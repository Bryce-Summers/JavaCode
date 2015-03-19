package Game_Engine.levelEditor.entities;

import java.awt.image.BufferedImage;

import Data_Structures.Structures.HashingClasses.Dict;
import Game_Engine.levelEditor.editor_components.obj_entity;

public class Ent_aaScalable extends obj_entity
{

	public Ent_aaScalable(double x, double y, BufferedImage spr, String name, String parameter)
	{
		super(x, y, spr, name, parameter);
	}
	
	public Ent_aaScalable(double x, double y, BufferedImage spr, String name)
	{
		super(x, y, spr, name, "");
	}

	@Override
	public obj_entity clone(int x, int y)
	{
		return new Ent_aaScalable(x, y, sprite, name, getText());
	}

	// Enables subclasses to add extra properties for serialization.
	// Called by obj_entity serialization routine.
	@Override
	protected void addSerialProperties(Dict<String> dict)
	{
		/* No special properties for normal entities. */
	}

}
