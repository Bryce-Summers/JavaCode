package Project.Game.objects;

import Game_Engine.Engine.Objs.Obj;
import Project.Editor.Components.Spr;

/*
 * Teleporters allow for pieces to move quickly to teleport locations.
 * 
 * Teleporters are not movers, but they do teleport to their own teleport location.
 */

public class obj_teleporter extends Obj
{

	public static obj_teleporter[] teleporters = new obj_teleporter[5];

	
	final int index;
	
	public obj_teleporter(double x_in, double y_in, int index)
	{
		super(x_in, y_in);
		
		sprite = Spr.teleporter[index];
		
		this.index = index;
		
		teleporters[index] = this;
	}

	@Override
	protected void update()
	{
		int w = getW();
		int h = getH();
		
		int center_x = (int) (getX() + w/2);				
		int center_y = (int) (getY() + h/2);
		
		// -- Deal with mover location.
		
		obj_mover mov = (obj_mover) instance_position(center_x, center_y, obj_mover.class);
		
		if(mov != null && !mov.isHeld())
		{
			obj_teleport_target_mover target = obj_teleport_target_mover.mover_targets[index];
			
			int mx = (int)(target.getX());
			int my = (int)(target.getY());
			
			if(target != null)
			{
				mov.glide(target);
			}
			
			// Dealt with moving teleporter and teleport target locations.
			obj_teleport_target_teleport target_t = obj_teleport_target_teleport.teleporter_targets[index];

			if(target_t == null)
			{
				return;
			}
			
			int tx = (int) target_t.getX();
			int ty = (int) target_t.getY();
			
			int ox = (int) (target_t.getX() - getX());
			int oy = (int) (target_t.getY() - getY());
			
			if(target_t != null)
			{
							
				int x = tx + ox;
				int y = ty + oy;
							
				if(instance_position(x, y, obj_groundSquare.class) != null)
				{
					moveTo(target_t);
					target_t.moveTo(x, y);
				}
				
				x = mx + ox;
				y = my + oy;
				
				if(instance_position(x, y, obj_groundSquare.class) != null)
				{
					target.moveTo(x, y);
				}
				
			}
			
			
		}
		
		
	}

}
