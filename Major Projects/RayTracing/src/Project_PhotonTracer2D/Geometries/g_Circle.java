package Project_PhotonTracer2D.Geometries;

import Components.Material;
import Math.Vector3;
import Project3D.Geometries.g_Sphere;

/* 2D Line Class
 * 
 * Written by Bryce Summers on 7/16/2015.
 * 
 */

public class g_Circle extends g_Sphere
{


	public g_Circle(Material mat, Vector3 location, double radius)
	{
		super(mat, location, radius);
	}

	@Override
	protected Vector3 sub_getNormal(Vector3 point)
	{
		double x = point.getX() - location.getX();
		double y = point.getY() - location.getY();
		
		return new Vector3(x, y).norm();	
	}
}