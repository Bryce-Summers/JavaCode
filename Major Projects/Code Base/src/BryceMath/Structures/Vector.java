package BryceMath.Structures;

import java.util.Iterator;

import util.Genarics;
import BryceMath.Numbers.Number;
import Data_Structures.Structures.UBA;

/*
 * A rewritten Vector class for use with linear algebra.
 * More emphasis has been placed on arbitrary length vectors.
 * rewritten and genaricized 11/14/2012, by Bryce Summers
 */
public class Vector<T extends Number<T>> implements Iterable<T>
{
	// FIXME: Fully implement Numbers.

	public UBA<T> data;// An array that will store all of the Vector's components.
	public final int length;
	
	private T magnitude;// Used to optimize calls for the vector's magnitude.
	
	private boolean magChanged; // A flag used to determine if the magnitude is already correct.

	// Create a Vector from a data set of doubles.
	public Vector(T ... input)
	{
		
		// Set the immutable length variable for this vector.
		length = input.length;
		
		// Make a copy of the input data and store it in this vector's data field.
		data = new UBA<T>(length); 
		
		for(T val: input)
		{
			data.add(val);
		}
						
		magChanged = true;// The magnitude doesn't exist until the user needs it.

	}
	
	// Create a Vector from a data set of doubles.
	public Vector(UBA<T> input)
	{
		
		// Set the immutable length variable for this vector.
		length = input.size();
		
		// Make a copy of the input data and store it in this vector's data field.
		data = input.copy();
		
		magChanged = true;// The magnitude doesn't exist until the user needs it.

	}
	
	//----Elementary Vector Functions
		
	// These work for any dimension of vector.
	// All of these functions do not mutate this vector, 
	// but instead return a new vector.
	
	// Addition.
	// Returns a new vector that is the result of adding an input vector to this vector.
	public Vector<T> add( Vector<T> input )
	{

		Vector<T> output = new Vector<T>(data);
		
		UBA<T> input_data  = input.getData();
		UBA<T> output_data = output.getData();
		
		int i = 0;
		for(T val: input_data)
		{
			output_data.set(i, output_data.get(i).add(val));
			i++;
		}
		
		magChanged = true;
		return output;

	}

	// Subtraction. 
	// Returns a new vector that is the result of subtracting an input vector from this vector.
	public Vector<T> sub( Vector<T> input )
	{

		Vector<T> output = new Vector<T>(data);
		
		UBA<T> input_data	 = input.getData();
		UBA<T> output_data = output.getData();
		
		int i = 0;
		for(T val : input_data)
		{
			output_data.set(i, output_data.get(i).sub(val));
			i++;
		}
				
		magChanged = true;
		return output;

	}
	
	// The dot, or inner product of two vectors.
	public T dot( Vector<T> input )
	{
		
		T output = input.get(0).zero();
		
		int i = 0;
		for(T j: input.getData())
		{			
			output = output.add(data.get(i).mult(j));
			i++;
		}
		return output;
		
	}
	
	// Scalar multiplication.
	// Returns a new vector that is the product of this vector and a scalar.
	public Vector<T> mult(T input)
	{
		
		Vector<T> output = new Vector<T>(data);
		UBA<T> output_data = output.getData();
		
		for(int i = 0; i < length; i++)
		{
			output_data.set(i, output_data.get(i).mult(input));
		}
		
		output.magnitude = mag().mult(input);
		output.magChanged = false;
		
		return output;
	}
	
	// Scalar multiplication.
	// Returns a new vector that is the product of this vector and a scalar.
	public Vector<T> mult(int input)
	{
		
		Vector<T> output = new Vector<T>(data);
		UBA<T> output_data = output.getData();
		
		for(int i = 0; i < length; i++)
		{
			output_data.set(i, output_data.get(i).mult(input));
		}
		
		output.magnitude = mag().mult(input);
		output.magChanged = false;
		
		return output;
	}
	
	// divide this vector by a scalar.
	public Vector<T> div(T input)
	{
		Vector<T> output = new Vector<T>(data);
		
		UBA<T> output_data = output.getData();
		
		for(int i = 0; i < length; i++)
		{
			output_data.set(i, output_data.get(i).div(input));
		}
		
		output.magnitude= mag().div(input);
		output.magChanged = false;
		
		return output;
	}
	
	// A function that returns the cross product of this and a second 3 - dimensional vectors.
	@SuppressWarnings("unchecked")
	public Vector<T> cross(Vector<T> o)
	{
		if (length != 3)
		{
			throw new Error("Vector: Cross Product requires vectors of length 3!");
		}
			
		T [] output_data = (T[])new Object[3];
		
		output_data[0] = data.get(1).mult(o.data.get(2)).sub(data.get(2).mult(o.data.get(1)));
		output_data[1] = data.get(2).mult(o.data.get(0)).sub(data.get(0).mult(o.data.get(2)));
		output_data[2] = data.get(0).mult(o.data.get(1)).sub(data.get(1).mult(o.data.get(0)));
		
		return new Vector<T>(data);
	}
	
	// Method for Accessing a vector's data; 
	private UBA<T> getData()
	{
		return data;
	}
	
	// Returns a copy of this array.
	public T[] toArray()
	{
		return data.toArray();
	}
	
	// Method for accessing individual stored doubles.
	public T get(int i)
	{
		// Warning, this is not very safe.
		return data.get(i);
	}
	
	// An efficient magnitude calculation function that recalculates if the internal vector has changed.
	public T mag()
	{
		if(magChanged)
		{
			// FIXME : the computation of the magnitudes should be dealt with.
			try
			{
				magnitude  = dot(this).sqrt();
			}
			catch(Error e){
				magnitude = get(0).one();
			};
			magChanged = false;
		}
		
		return magnitude;
	}
	
	// FIXME: this function might never actually be used, and perhaps it should be removed to reduce function clutter.
	// Used to optimize certain calculations. Returns the square of the magnitude.
	public T sqr_mag()
	{
		if(magChanged)
		{
			return dot(this);
		}
		else
		{
			return magnitude.mult(magnitude);
		}
	}

	// Normalize this vector to become a unit vector in the proper direction
	public Vector<T> norm()
	{
		if(!magChanged && magnitude.eq(1))
		{
			return this;
		}
		
		Vector<T> out = this.div(mag());
		out.magnitude = out.get(0).one(); // At least it should be equal to 1!

		return out;
	}
	
	// Returns the reflection of a vector off of a surface with normal vector normal.
	public Vector<T> reflection(Vector<T> normal)
	{
		// Note: All vector calls with no vector name are called by this vector!!!
		return sub( normal.mult(2)).mult(dot(normal));
	}
	
	// Should only be called with correct information!!!
	public void setMagnitude(T m)
	{
		magChanged = false;
		magnitude = m;
	}
	
	// Vector projection of the component of this vector that lies along b.
	public Vector<T> proj(Vector<T> b)
	{	
		// See http://en.wikipedia.org/wiki/Vector_projection
		return b.mult(dot(b).div(b.mag()));
	}
	
	// Returns true if and only if all components of this vector
	// are equal to all components in the input vector.
	public boolean equals(Vector<T> input)
	{
		UBA<T> input_data = input.getData();
		
		return input_data.equals(data);

	}
	
	// -- A Hopefully type safe Vector equality function.
	private Genarics<T> ge_T = new Genarics<T>();
	
	@Override
	public boolean equals(Object o)
	{
		return ge_T.xequal(this, o);
	}
	
	public String toString()
	{
		String output = "(";

		output += data.get(0);// There is no such thing as a vector of length 0!
		
		for(int i = 1; i < length; i++)
		{
			output += ", " + data.get(i);
		}
				
		output += ")";
		
		return output;
	}

	@Override
	public Iterator<T> iterator()
	{
		return data.iterator();
	}
	
}
