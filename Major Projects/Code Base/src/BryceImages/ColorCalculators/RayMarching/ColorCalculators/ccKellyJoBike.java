package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_cylinder;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plate;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_torus;
import BryceMath.Calculations.Colors;

public class ccKellyJoBike extends PhongLighted3dPicture {

	public ccKellyJoBike(Dimension tempDim)
	{
		super(tempDim);
	}

	@Override
	public Color getColor(Vector z, double light)
	{
		return null;
	}

	@Override
	public void loadGeoms()
	{
		lights = new Vector[3];
		lights[0] = new Vector(5,5,3,.7);
		lights[1] = new Vector(15,30,5,.7);
		lights[2] = new Vector(0,20,30,.5);

		// Side view.
		CameraZ 		= new Vector(2, -10, 50);
		CameraFocus 	= new Vector(2, 0,  0);
		
		antiAliasing 	= 3;
		reflectionDepth	= 3;
		
		CameraDepth = 9; // full body :  .98;
		
		// Define English Horn Geometry.
		Geometry g;
		
		Vector wheel1 = v(0, 0, 0);
		Vector wheel2 = v(4, 0, 0);
		
		// Wheel 1.
		g = new geom_torus(wheel1, 1, .2, v(0, 0, 1));
		g.setColor(Color.black);
		geoms.add(g);
		
		// Wheel 2.
		g = new geom_torus(wheel2, 1, .2, v(0, 0, 1));
		g.setColor(Color.black);
		geoms.add(g);
		
		// The left point on the triangle.
		Vector v1 = v(1, 2, 0);
		
		// The bottom point on the triangle.
		Vector v2 = v(1.5, 0, 0);
		
		// The right point on the triangle.
		Vector v3 = v(3.5, 1.7, 0);
		
		connect(wheel1, v1);
		connect(wheel2, v2);
		connect(v1, v2);
		connect(v2, v3);
		connect(v1, v3);
		connect(wheel2, v3);
		
		// Create Spokes.		
		for(double r = 0; r < 2*Math.PI; r +=.5)
		{
			Vector add = v(Math.cos(r), Math.sin(r), 0);
			connect2(wheel1, wheel1.add(add));
			connect2(wheel2, wheel2.add(add));
		}

		
		// Create the handles.
		Vector handleL = v(1, 2, -1);
		Vector handleR = v(1, 2,  1);
		
		connect(v1, handleL);
		connect(v1, handleR);
		
		Vector handleL2 = v(1.5, 2, -1);
		Vector handleR2 = v(1.5, 2,  1);
		
		connect(handleL, handleL2);
		connect(handleR, handleR2);
		
		// Create the Seat.
		g = new geom_plate(v3, .5, .2, v(0, 1, 0));
		g.setColor(Color.BLACK);
		geoms.add(g);

	}// End of the code that creates Geoms.

	public void connect(Vector v1, Vector v2)
	{
		Geometry g = new geom_cylinder(v1, v2, .1);
		g.setColor(Colors.Color_hsv(180, 90, 100));
		geoms.add(g);
	}
	
	public void connect2(Vector v1, Vector v2)
	{
		Geometry g = new geom_cylinder(v1, v2, .05);
		g.setColor(Colors.Color_hsv(0, 0, 70));
		geoms.add(g);
	}
	
	public Vector v(double x, double y, double z)
	{
		return new Vector(x, y, z);
	}

	@Override
	public boolean withinBounds(Vector z) {
		
		return z.mag() < 200;
	}

	
}
