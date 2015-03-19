package BryceMath.Geometry;

import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.UBA;

/*
 * Polygon Class.
 * 
 * Can represent Triangles, Quadrilaterals, etc.
 * 
 * The data is an array of vectors that each contain x and y locations.
 */

public class Polygon implements Shape
{
	UBA<Vector> data;
	
	public Polygon(Vector ... input)
	{
		this.data = new UBA<Vector>(input.length);
		
		for(Vector v : input)
		{
			data.add(v.clone());
		}
	}
	
	public boolean contains(Vector p)
	{
		throw new Error("Implement me Please!");
	}
}
