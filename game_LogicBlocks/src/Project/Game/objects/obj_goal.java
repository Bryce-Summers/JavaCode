package Project.Game.objects;

import Game_Engine.Engine.Objs.Obj;
import Project.Editor.Components.Spr;

/* 
 * Written by Bryce Summers on 6 - 21 - 2014.
 * 
 * Purpose :
 */

public class obj_goal extends Obj
{

	final int index;
	
	public obj_goal(double x_in, double y_in, int index)
	{
		super(x_in, y_in);
		
		this.index = index;
		
		sprite = Spr.goal[index];
		
		setDepth(-19);
	}
	
	@Override
	protected void update()
	{
	
	}

}
