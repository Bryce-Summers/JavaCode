package Project_PhotonTracer2D.Geometries;

import Components.Geometry;
import Components.Material;
import Components.photonColor;
import Math.Vector3;

/* 2D Line Class
 * 
 * Written by Bryce Summers on 7/16/2015.
 * 
 */

public class g_line extends Geometry
{

	public g_line(Material mat)
	{
		super(mat);
	}

	@Override
	protected double sub_getIntersection(Vector3 point, Vector3 dir)
	{
		return 0;
	}

	@Override
	public photonColor getColor()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Vector3 sub_getNormal(Vector3 point)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
