package Game_Engine.Engine.ConnectedBodies;

import BryceImages.ColorCalculators.RayMarching.BryceMath;
import Game_Engine.Engine.Objs.Obj;

/*
 * These bodies are used for animation purposes.
 * 
 * These bodies should be strung together as modules to accomplish the animation of beings such as animals.
 * 
 * These bodies form trees.
 * 
 * FIXME : I need to comprehensibly make sure all of this works correctly.
 */

public abstract class obj_body extends Obj
{
	// -- Private Data.
	protected obj_body parent;
	//image_angle comes from obj specification.
	
	// An upper bound for how much this angle can deviate from its parent.
	protected double max_deviation_from_parent = 360.0;
	
	// The amount of angle rotation this body can rotate in one step due to gravity.
	protected double gravity_angle_step = 0.0; 

	// Gravity is by default downwards. 0 = right, 90 = up, 180 = left;
	public static double gravity_dir = 270;
	
	// These values define a the relative coordinate of this body with regards to the parent.
	protected double parent_distance;
	protected double parent_angle;
	
	// Stores this body's relative angle.
	public double myAngle = 0.0;
	
	// Defines the 0 direction of the child relative to the parent.
	public double myAngleStart;
	
	// -- Constructors.
	public obj_body(double x, double y, double relative_angle)
	{
		super(x, y);
		load_sprite();
		
		parent_distance = BryceMath.distance(0, 0, x, y);
		parent_angle = BryceMath.lineAngle(x, y, 0, 0);
		
		myAngleStart = relative_angle;
		
	}
	
	// -- Abstract specification.
	protected abstract void load_sprite();
	
	protected abstract void logic_update();
	
	// This abstraction lets the main update code to hang out here where it belongs.
	public void update()
	{
		// Run the individual logic update script.
		logic_update();
		
		sprite_angle = myAngle;
		
		if(!isRoot())
		{
			// Make sure the parent updates first.
			parent.ensureUpdate();
			
			// Compute current radial component for the position mapping.
			double offsetAngle = parent.sprite_angle + parent_angle;
			
			// Compute the new coordinates.
			double x = parent.getX() + parent_distance*Math.cos(Math.toRadians(offsetAngle));
			double y = parent.getY() - parent_distance*Math.sin(Math.toRadians(offsetAngle));
			
			// Apply local transformation.
			sprite_angle += myAngleStart + offsetAngle;
			
			// Set the new coordinates.
			setX(x);
			setY(y);
		}
		
		// Handle the changes in image_angle due to gravity.
		handle_gravity();
		
		handle_restraints();
	}
	
	// Gravity movement code. Allows for natural falls to the left and right.
	private void handle_gravity()
	{
		
		if(gravity_angle_step <= 0.0)
		{
			return;
		}
		
		// Fall right.
		if(-90 < sprite_angle && sprite_angle <= 90)
		{
			sprite_angle = Math.max(-90, sprite_angle -= gravity_angle_step);
			return;
		}
		
		// Fall left.
		if(90 < sprite_angle && sprite_angle < 270)
		{
			sprite_angle = Math.min(270, sprite_angle += gravity_angle_step);
			return;
		}
		
		
	}
	
	// Handles stiffness constraints on the amount this body can deviate from it's parent's angle.
	private void handle_restraints()
	{
		
		myAngle = myAngle % 360.0;
		
		if(isRoot())
		{
			return;
		}
		
		parent.sprite_angle = parent.sprite_angle % 360.0;
				
		double angle_diff = myAngle - parent.sprite_angle;
		if(angle_diff > 180)
		{
			angle_diff -= 360;
		}
		if(angle_diff < -180)
		{
			angle_diff += 360;
		}

		// Over the upper bound.
		if(angle_diff > max_deviation_from_parent)
		{
			sprite_angle = parent.sprite_angle + max_deviation_from_parent;
			myAngle = max_deviation_from_parent;
			return;
		}

		// Under the lower bound.
		if(angle_diff < -max_deviation_from_parent)
		{
			sprite_angle = parent.sprite_angle - max_deviation_from_parent;
			myAngle = -max_deviation_from_parent;
			return;
		}
		
	}
	
	// -- Helper functions.
	
	// Returns true iff this body is the main root body.
	protected boolean isRoot()
	{
		return parent == null;
	}
	
	public void addChild(obj_body obj)
	{
		obj.parent = this;
	}
	
	public void setParent(obj_body obj)
	{
		parent = obj;
	}
	
	public void makeRoot()
	{
		parent = null;
	}
	
	// Allows external sources to set this relative angle.
	public void setAngle(double angle)
	{
		myAngle = angle % 360;
	}
	
}
