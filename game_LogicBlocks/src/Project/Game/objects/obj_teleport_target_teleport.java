package Project.Game.objects;

import Data_Structures.Structures.BitSet;
import Project.Editor.Components.Spr;

public class obj_teleport_target_teleport extends obj_mover
{
	
	public static obj_teleport_target_teleport[] teleporter_targets = new obj_teleport_target_teleport[5];

	public obj_teleport_target_teleport(double x_in, double y_in, int index, BitSet prop)
	{
		super(x_in, y_in, index, prop);

		sprite = Spr.teleport_target_teleporter[index];
		teleporter_targets[index] = this;
	}

}
