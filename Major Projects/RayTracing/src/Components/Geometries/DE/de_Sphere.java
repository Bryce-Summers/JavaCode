package Components.Geometries.DE;

import Components.DistanceFunction;
import Math.Vector3;

public class de_Sphere implements DistanceFunction
{
	
	private double radius;
	
	public de_Sphere(double radius)
	{
		this.radius = radius;
	}

	@Override
	public double DE(Vector3 point)
	{
		return Math.abs(point.mag() - radius);
	}

}
