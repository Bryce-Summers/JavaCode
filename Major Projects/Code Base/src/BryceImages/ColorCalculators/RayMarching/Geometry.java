package BryceImages.ColorCalculators.RayMarching;

import java.awt.Color;

import BryceImages.Rendering.ColorCalculator;

// Shape super class
// Written by Bryce Summers
// 7/22/2012

// Purpose: The purpose of this class is to provide standard methods for defining shape geometries.

public abstract class Geometry extends BryceMath implements Comparable<Geometry>
{

	public double myReflect = 0;
	public Color myColor = Color_hsv(0,100,100);
	public ColorCalculator cc = null;
	
	public double minDist = -1;
	public double startDist = -1;
	public boolean distChanged;
	
	// These vectors are used by Geometries in order for them to be implemented in non cartesian orientations.
	public Vector u = new Vector(1,0,0),v = new Vector(0,1,0);
	public Vector n = new Vector(0,0,1);
	
	// Methods that must be defined by a shape.	
	public double DE(Vector z, Vector dz)			// The inclusion of dz allows for optimizations in DE calculation.
	{											  	//This can be used for geometries where the exact collision point for the ray can be calculated. For instance common ray tracing shapes like planes.
		return DE(z);// By default optimization is not implemented.
	}
	public abstract double DE(Vector z);// The normal Distance estimation without optimization for directed a query.

	public int compareTo(Geometry g)
	{
		if(minDist - g.minDist > 0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
	
	public Color getColor(Vector z,double finalLighting)// Input a geometric coordinate, and an exterior lighting value.
	{
		Color c;
		if(cc !=null) // FIXME: make this map to u and j components instead arbitrary x and y components.
			c = cc.getColor(getComponent(z,u), getComponent(z,v)); // Coloring from a color calculator.
		else
			c = myColor; // Uniform coloring.
		
		// Return the appropriate color with an intensity correct for its lighting.
		return weightedAverageColor(Color.black,c,finalLighting);
	}
	
	public void setColorCalculator(ColorCalculator cc_in)
	{
		cc = cc_in;
	}
	
	
	public double reflectivity()
	{
		return myReflect;
	}
	
	public void setColor(Color c) {
		myColor = c;
	}
	public void setReflectivity(double in)
	{
		myReflect = in;
	}
	
	// Vector coordinate mathematics.
	// FIXME: for true independence, the temp, or "up" vector should be passed as an argument to this function.
	public void createCoordinateSystem(Vector n)
	{
		// this is here just in case someone gives this function a non unit normal vector.
		n = n.norm();
		// Handle special cases, where the normal vector is parallel to my arbitrarily chosen up vector.
		Vector temp = new Vector(0,0,1);
		if(temp.sub(n).mag() == 0)
		{
			temp = new Vector(.1,0,1);
		}
		else if(temp.add(n).mag() == 0){
			temp = new Vector(.1,0,-1);
		}
		
		v = n.cross(temp);
		
		u = v.cross(n);// We now have our coordinate system.
		
		u = u.norm();
		v = v.norm();
		this.n = n.norm();
	}
	
	// Returns the value of the inputed vector's unitCoordinateVector component.
	// For instance the vector (1,0,0) would return a value of 1 if the unit Vector(1,0,0) was given.
	public double getComponent(Vector in, Vector unitCoordinateVector)
	{
		return in.proj(unitCoordinateVector).mag();
	}
	
}
