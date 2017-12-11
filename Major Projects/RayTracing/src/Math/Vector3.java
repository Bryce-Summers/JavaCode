package Math;

/*
 * An adapted Vector class.
 * Written for use with 3 dimensional computer graphics.
 * 
 * Adapted from my arbitrary dimensional code on 7/17/2015;
 */

// FIXME : This class is currently highly mutable, this should be reevaluated..

public class Vector3
{
	private static int x = 0;
	private static int y = 1;
	private static int z = 2;
	
	
	public double[] data;// An array that will store all of the Vector's components.
	public final int length;
	
	private double magnitude;// Used to optimize calls for the vector's magnitude.
	
	private boolean magChanged; // A flag used to determine if the magnitude is already correct.

	// Create a Vector from a data set of doubles.
	public Vector3(double ... input)
	{
		// Set the immutable length variable for this vector.
		length = input.length;
		
		if(length > 3)
		{
			throw new Error("Vector3 objects cannot have more than 3 coordinates.");
		}

		// Make a copy of the input data and store it in this vector's data field.
		data = new double[3];
		
		int i = 0;
		for(double val: input)
		{
			data[i] = val;
			i++;
		}

		magChanged = true; // The magnitude doesn't exist until the user needs it.

	}
	
	/*
	// Create a 0 vector
	public Vector(int len)
	{
		length = len;
		data = new double[length];
		magnitude = 0;
		magChanged = false;
	}
	*/
	
	//----Elementary Vector Functions
		
	// These work for any dimension of vector.
	// All of these functions do not mutate this vector, 
	// but instead return a new vector.
	
	// Addition.
	// Returns a new vector that is the result of adding an input vector to this vector.
	public Vector3 add( Vector3 input )
	{

		Vector3 output = new Vector3(data);
		
		double[] input_data	 = input.getData();
		double[] output_data = output.getData();
		
		int i = 0;
		for(double val: input_data)
		{
			output_data[i] += val;
			i++;
		}
				
		return output;

	}

	// Subtraction. 
	// Returns a new vector that is the result of subtracting an input vector from this vector.
	public Vector3 sub( Vector3 input )
	{

		Vector3 output = new Vector3(data);
		
		double[] input_data	 = input.getData();
		double[] output_data = output.getData();
		
		int i = 0;
		for(double val: input_data)
		{
			output_data[i] -= val;
			i++;
		}
				
		return output;

	}
	
	public Vector3 min(Vector3 input)
	{
		Vector3 output = new Vector3(data);
		
		double[] input_data	 = input.getData();
		double[] output_data = output.getData();
		
		int i = 0;
		for(double val: input_data)
		{
			output_data[i] = Math.min(output_data[i], val);
			i++;
		}
				
		return output;
	}
	
	public Vector3 max(Vector3 input)
	{
		Vector3 output = new Vector3(data);
		
		double[] input_data	 = input.getData();
		double[] output_data = output.getData();
		
		int i = 0;
		for(double val: input_data)
		{
			output_data[i] = Math.max(output_data[i], val);
			i++;
		}
				
		return output;
	}
	
	// The dot, or inner product of two vectors.
	public double dot( Vector3 input )
	{
		
		double output = 0;
		
		int i = 0;
		for(double j: input.getData())
		{
			output += data[i]*j;
			i++;
		}
		return output;
		
	}
	
	// Scalar multiplication.
	// Returns a new vector that is the product of this vector and a scalar.
	public Vector3 mult(double input)
	{
		
		Vector3 output = new Vector3(data);
		double[] output_data = output.getData();
		
		for(int i = 0; i < length; i++)
		{
			if(output_data[i] != 0)
			output_data[i] *= input;
		}
		
		output.magnitude= mag() * input;
		output.magChanged = false;
		
		return output;
	}
	// divide this vector by a scalar.
	public Vector3 div(double input)
	{
		Vector3 output = new Vector3(data);
		double[] output_data = output.getData();
		
		for(int i = 0; i < length; i++)
		{
			output_data[i] /= input;
		}
		
		output.magnitude= mag() / input;
		output.magChanged = false;
		
		return output;
	}
	
	// Returns the scalar t such that t*input = this.
	public double div(Vector3 input)
	{
		
		for(int i = 0; i < length; i++)
		{
			double num = data[i];
			double denom = input.data[i];
			
			if(num != 0 && denom != 0)
			{
				return num / denom;
			}
		}
		
		return 0;
	}
	
	// A function that returns the cross product of this and a second 3 - dimensional vectors.
	public Vector3 cross(Vector3 o)
	{
		if (length > 3 || o.length > 3)
		{
			throw new Error("Vector: Cross Product requires vectors of length 3!");
		}
		
		double [] output_data = new double[3];

		if(length == 2 && o.length == 2)
		{
			output_data[x] = 0;
			output_data[y] = 0;
			output_data[2] = data[x]*o.data[y] - data[y]*o.data[x];
			return new Vector3(output_data);
		}
		
		output_data[x] = data[y]*o.data[z] - data[z]*o.data[y];
		output_data[y] = data[z]*o.data[x] - data[x]*o.data[z];
		output_data[z] = data[x]*o.data[y] - data[y]*o.data[x];
		
		return new Vector3(output_data);
	}
	
	// Method for Accessing a vector's data; 
	private double[] getData()
	{
		return data;
	}
	
	// Method for accessing individual stored doubles.
	public double get(int i)
	{
		// Warning, this is not very safe.
		return data[i];
	}
	
	public double getX()
	{
		return data[x];
	}
	
	public double getY()
	{
		return data[y];
	}
	
	public double getZ()
	{
		return data[z];
	}
	
	public void setX(double val)
	{
		data[x] = val;
	}
	
	public void setY(double val)
	{
		data[y] = val;
	}
	
	public void setZ(double val)
	{
		data[z] = val;
	}
	
	// An efficient magnitude calculation function that recalculates if the internal vector has changed.
	public double mag()
	{
		if(magChanged)
		{
			magnitude = Math.sqrt(dot(this));
			magChanged = false;
		}
		
		return magnitude;
	}
	
	// FIXME: this function might never actually be used, and perhaps it should be removed to reduce function clutter.
	// Used to optimize certain calculations. Returns the square of the magnitude.
	public double sqr_mag()
	{
		if(magChanged)
		{
			return dot(this);
		}
		else
		{
			return magnitude*magnitude;
		}
	}

	// Normalize this vector to become a unit vector in the proper direction
	// Returns the normalized vector, does not mutate the original vector.
	public Vector3 norm()
	{
		if(!magChanged && magnitude == 1)
		{
			return this;
		}
		
		Vector3 out = this.div(mag());
		out.magnitude = 1; // At least it should be equal to 1!

		return out;
	}
	
	// Returns the reflection of a vector off of a surface with normal vector normal.
	// Simply subtracts 2 times the vectors projection onto the normal to remove it and negate it!
	public Vector3 reflection(Vector3 normal)
	{
		// Note: All vector calls with no vector name are called by this vector!!!
		return sub( normal.mult(2).mult(dot(normal)));
	}
	
	// Should only be called with correct information!!!
	public void setMagnitude(double m)
	{
		magChanged = false;
		magnitude = m;
	}
	
	// Vector projection of the component of this vector that lies along b.
	public Vector3 proj(Vector3 b)
	{	
		// See http://en.wikipedia.org/wiki/Vector_projection
		return b.mult(dot(b)/b.mag());
	}
	
	public Vector3 perp2d()
	{
		if(length != 2)
		{
			throw new Error("perp2d only workds for 2 dimensional vectors.");
		}
		return new Vector3(-get(y), get(x));
	}
	
	// Returns true if and only if all components of this vector
	// are equal to all components in the input vector.
	public boolean equals(Vector3 input)
	{
		double[] input_data = input.getData();
		
		for(int i = 0; i < length; i++)
		{
			if(input_data[i] != data[i])
			{
				return false;
			}
		}
		
		return true; 
	}
	
	@Override
	public String toString()
	{
		String output = "(";

		// There is no such thing as a vector of length 0!
		// Add the first element to the list.
		output += data[0];
		
		for(int i = 1; i < length; i++)
		{
			output += ", " + data[i];
		}
				
		output += ")";
		
		return output;
	}
	
	@Override
	public Vector3 clone()
	{
		return new Vector3(data);
	}
	
	
	// Returns a new vector whose x component and y component fall within the given square bounds.
	public static Vector3 randV(int start1, int end1, int start2, int end2)
	{
		int len1 = end1 - start1;
		int len2 = end2 - start2;
		
		return new Vector3(len1*Math.random() + start1, len2*Math.random() + start2);
	}

	// Returns a vector that is perpendicular to this vector in R^2.
	public Vector3 getPerp()
	{
		if(length != 2)
		{
			throw new Error("This function only applies to 2 Dimensional vectors.");
		}
		
		return new Vector3(-data[1], data[0]);
	}
	
	public boolean isZero()
	{
		for(double d : data)
		{
			if(d != 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	// Returns a zero vector of the given number of dimensions.
	public static Vector3 zero(int size)
	{
		double[] data = new double[size];
		
		for(int i = 0; i < size; i++)
		{
			data[i] = 0;
		}
		
		return new Vector3(data);
	}
	
	public static Vector3 average(Vector3 ... input)
	{
	
		if(input.length == 0)
		{
			throw new Error("ERROR : Cannot tell what the dimension is with no input vectors");
		}
		
		Vector3 output = zero(input[0].length);
		
		for(Vector3 v : input)
		{
			output = output.add(v);
		}
		
		return output.div(input.length);
		
	}
	
	// Vector creation function;
	public static Vector3 v(double ... input)
	{
		return new Vector3(input);
	}
	
	// vector direction function.
	// REQUIRES : Angle must be in degrees : preferably [0, 360).
	public static Vector3 v_dir(double angle)
	{
		return v(Math.cos(Math.toRadians(angle)),- Math.sin(Math.toRadians(angle)));
	}
	
    // returns true iff this point is less than the input point on every dimension.
	public boolean lessThan(Vector3 pt)
    {
    	for(int i = 0; i < data.length; i++)
    	{
    		if(!(this.data[i] < pt.data[i]))
    		{
    			return false;
    		}
    	}
    	
        return true;
    }

    public boolean lessThanOrEqual(Vector3 pt)
    {
    	for(int i = 0; i < data.length; i++)
    	{
    		if(!(this.data[i] <= pt.data[i]))
    		{
    			return false;
    		}
    	}
    	
        return true;
    }

    // returns true iff this point is greater than the input point on ever dimension.
    public boolean greaterThan(Vector3 pt)
    {
    	for(int i = 0; i < data.length; i++)
    	{
    		if(!(this.data[i] > pt.data[i]))
    		{
    			return false;
    		}
    	}
	
    	return true;
    }

    public boolean greaterThanOrEqual(Vector3 pt)
    {
    	for(int i = 0; i < data.length; i++)
    	{
    		if(!(this.data[i] >= pt.data[i]))
    		{
    			return false;
    		}
    	}
    	
        return true;
    }
}