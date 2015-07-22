package Components;

import Math.Vector3;

public interface DistanceFunction
{

	// -- Returns the distance the given point is from the object.
	// Distance Estimation.
	double DE(Vector3 point);
	
}
