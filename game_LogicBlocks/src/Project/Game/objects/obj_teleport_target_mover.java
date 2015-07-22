package Project.Game.objects;

import Data_Structures.Structures.BitSet;
import Project.Editor.Components.Spr;

public class obj_teleport_target_mover extends obj_mover
{
	public static obj_teleport_target_mover[] mover_targets = new obj_teleport_target_mover[5];
	
	public obj_teleport_target_mover(double x_in, double y_in, int index, BitSet prop)
	{
		super(x_in, y_in, index, prop);

		sprite = Spr.teleporter_target_mover[index];
		mover_targets[index] = this;
		
		setDepth(-2);
	}

}
