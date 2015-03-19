package BryceImages.ColorCalculators.RayMarching;

// Basic Vector Class
// Written by Bryce Summers, 7/11/2012
// Purpose: This is a basic Vector class used to simplify the computation of vector math,
//			as my Graphical rendering gets more complex and voyages fully into the 3rd 
//			dimension.


// FIXME: Consider reimplementing this class as an arbitrary dimensional vector class.
public class Vector {

	//public double[] zz;// An array that will store all of the Vector's components.
	public double x, y, z, val = 1;
	private double magnitude;
	//private int mySize;
	private boolean magChanged; // Stores whether the magnitude has changed.
								// If it has changed, when the user requests a magnitude it will be calculated.
								// Otherwise it will return the stored value to avoid extra computation.

	public Vector(double x, double y, double z,double val){
		
		this.val = val;
		
		this.x = x;
		this.y = y;
		this.z = z;
						
		magChanged = true;// The magnitude doesn't exist until the user needs it.
	}
	public Vector(double x, double y, double z){
		
		this.x = x;
		this.y = y;
		this.z = z;
						
		magChanged = true;// The magnitude doesn't exist until the user needs it.
	}
	
	public Vector(double x, double y){
		
		this.x = x;
		this.y = y;
		this.z = 0;
		
		magChanged = true;
	}
	
	//--Elementary Math Functions
	// Note: o stands for other.
	// These work for any dimension of vector.
	public Vector add( Vector o )
	{

		Vector out = new Vector(x,y,z);
		
		out.x += o.x;
		out.y += o.y;
		out.z += o.z;
		
		magChanged = true;
		
		return out;

	}

	public Vector sub( Vector o )
	{

		Vector out = new Vector(x,y,z);
		
		out.x -= o.x;
		out.y -= o.y;
		out.z -= o.z;
		
		magChanged = true;
		
		return out;
	}
	
	public double dot( Vector o)
	{
		return x*o.x + y*o.y + z*o.z;
	}
	
	// Multiply this vector by a scalar.
	public Vector mult(double in)
	{
		Vector out = new Vector(x,y,z);
		
		out.x *= in;
		out.y *= in;
		out.z *= in;
		
		out.magnitude= mag() * in;
		out.magChanged = false;
		
		return out;
	}
	// divide this vector by a scalar.
	public Vector div(double in)
	{
		Vector out = new Vector(x,y,z);
		
		out.x /= in;
		out.y /= in;
		out.z /= in;
		
		out.magnitude = mag() / in;
		out.magChanged = false;
		
		return out;
	}
	
	// A function that returns the cross product of this and a second 3 - dimensional vectors.
	public Vector cross(Vector o)
	{
		
		return new Vector(y*o.z - z*o.y, z*o.x - x*o.z, x*o.y - y*o.x);
	}
	
	// An efficient magnitude calculation function that recalculates if the internal vector has changed.
	public double mag()
	{
		if(magChanged)
		{
			magnitude = Math.sqrt(x*x + y*y + z*z);
			magChanged = false;
		}
		
		return magnitude;
	}

	// Normalize this vector to become a unit vector in the proper direction
	public Vector norm()
	{
		if(!magChanged && magnitude == 1)
		{
			return this;
		}
		
		Vector out = this.div(mag());
		out.magnitude = 1; // At least it should be equal to 

		return out;
	}
	
	// Returns the reflection of a vector off of a surface with normal vector normal.
	public Vector reflection(Vector normal)
	{
		// Note: All vector calls with no vector name are called by this vector!!!
		return sub( normal.mult(2).mult(dot(normal)));
	}
	
	public void setMagnitude(double m)// Should only be called with correct information!!!
	{
		magChanged = false;
		magnitude = m;
	}
	
	// Vector projection of the component of this vector that lies along b.
	public Vector proj(Vector b)
	{	// See http://en.wikipedia.org/wiki/Vector_projection
		return b.mult(dot(b)/b.dot(b));
	}
	
	public boolean equals(Vector in)
	{
		return (x == in.x) && (y == in.y) && (z == in.z); 
	}
	
	public String toString()
	{
		return "X = "+x+" Y = "+y+" Z = "+z;
	}
}
