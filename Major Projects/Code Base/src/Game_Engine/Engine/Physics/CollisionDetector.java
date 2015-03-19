package Game_Engine.Engine.Physics;

import BryceMath.Geometry.Shape;

/*
 * Collision Detector Class.
 * 
 * Used to efficiently compute collisions between groups of Shapes.
 * 
 * Written by Bryce Summers on 4 - 24 - 2014.
 * 
 * Interesting collision detection tutorial : http://www.metanetsoftware.com/technique/tutorialA.html
 */

public class CollisionDetector <E>
{
	
	// FIXME :  Implement a Range Query Tree here...
	
	//Range Query Tree requires Treaps...
	
	// -- Data interface functions.
	
	// Adds the given shape to the internal data structures of this Engine.
	public void addShape(Shape s)
	{
		throw new Error("Implement Me Please!");
	}
	
	// Removes the given shape from consideration by this engine.
	public void removeShape(Shape s)
	{
		throw new Error("Implement Me Please!");
	}

	// Removes all shapes from the Collision Detector.
	public void clearShapes()
	{
		throw new Error("Implement Me Please!");
	}
	
	// -- Collision Queries.
	
	/*
	 * REQUIRES : An input object and a location that it will be placed at.
	 * 			  A class type for the type of object we are searching for.
	 * ENSURES  : returns the first object of the given class type found that collides with the input,
	 * 			  when the input is relocated to the given location. 
	 */
	public E collision_at_location(E input, double x, double y, Class<?> class1)
	{
		throw new Error("Implement Me Please!");
	}
	
	
	
}
