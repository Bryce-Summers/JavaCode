package BryceImages.Engines;
import static BryceMath.Calculations.MathB.lineAngle;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.Rendering.ColorCalculator;
import BryceMath.Calculations.Geometry;
import BryceMath.DoubleMath.Matrix;
import BryceMath.DoubleMath.Vector;
import BryceMath.Geometry.Circle;
import BryceMath.Geometry.Polygon;
import BryceMath.Geometry.Shape;
import Data_Structures.Structures.Box;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;

/*
 * Image_vectorGeometry.
 * Created: 12 - 19 - 2012, by Bryce Summers.
 * 
 * Purpose: This class will be used to implement vector based geometric primitives.
 * 			I will be focusing my efforts on 2 dimensional rendering,
 * 			but due to the nature of vector primitives,
 * 			they should all be able to be extended to 3 dimensions trivially.
 * 
 * Programmer Warning : Unlike normal painting, the initialization of primitives should be done from closest to farthest.
 * 
 * 
 * Methodology: This class has three main algorithmic parts.
 * 
 * 			1.  The Geometry Creation functions that initialize the geometric data list.
 * 				All of the data will be vectors, 
 * 				so each particular piece of geometry will be given a geometry id that will
 * 				be represented by a 1 dimensional vector. A vector containing this id number should always be 
 * 				put on the stack before any shape specific protocols.
 * 				The remaining data should be optimized precomputed vectors that allow for
 * 				color calculations to be performed as efficiently as possible.
 * 			2.	The color calculation function that parses data and 
 * 				sends it to the Geometry calculation functions.
 * 			3.	The Geometry calculation functions, which take parsed data
 * 				and an input vector and output a color.
 * 				These functions get passed the index of the first meaningful non header component inside of the data list.
 * 
 * Notes :  4 dimensional Vectors will be used, which will represent (x, y, z, transparency) tuples.
 * 			Other vectors will be used to represent Colors via (r, g, b, transparency).
 * 			Because all data is represented by 4 dimensional vectors, they can be kept interchangeably inside the data UBA.
 * 			Methods do not need to be able to differentiate between the two types, because they specify "Serialization". 
 * 			like methods for adding and parsing precomputed data from the data list.
 * 
 * 			I have chosen an Unbounded array to handle the data collection, because it will be index searched far more times
 * 			during rendering than the 1 period of time that it will be added to during precomputing.
 * 
 * 			The r, g, b, and transparency values will be derived from Java Color classes,
 * 			so the user does not need to know the internal structure and representation of this data.
 * 
 * Note : (x, y, z,   0   ) tuples will can be used to clear a region of space.
 * 
 * State_Info variables are used to control the compositing of geometric regions.
 * - These variables can hold three states:
 * 		1. TRANSPARENT - This indicates that the rest of the geometry list will be searched in search of underlying colors to blend the current color to.
 * 		2. OPAQUE - We have reached the bottom color in the list and no colors further down the list can influence the returned color.
 * 		3. CUT_OUT - Geometries with alpha = 0 will act as cutouts in that they will remove space from the next geometry's region,
 * 					 Thereby skipping the next geometry from proccessing.
 * 
 * 				FIXME : I could potentially implement cutouts that affect multiple geometries by allowing negative alpha values to indicate the number of geometries that are desired to be skipped.
 * 
 * The geometry initialization methods are public instead of protected to enable dynamically generated Images.
 */

// FIXME : Optimize everything.
// Utilize circle and rectangular bounding checks to speed up calculation.
// FIXME : Instead of creating inclined this or that, allow the user to specify a list of radiuses into the normal functions.

// FIXME : This class could be converted to a class that allows for custom images to be generated dynamically.

// FIXME : Create a bunch of static variables to replace the magic numbers representing the sizes of each data type.

// FIXME : Have the colors as part of the protocol header, not the footer.

public abstract class Image_vectorGeometry extends ColorCalculator
{
	// -- Local Data. Non mutable, so this is safe for multithreading.
	UBA<Vector> data	= new UBA<Vector>();
	
	// Used to store a list of the geometries embodied in this image for collision detection and other Geometric purposes.
	List<Shape> shapes  = new List<Shape>();
	
	// Local Color variables, used during data creation. 
	private Vector color_primary	= v(0, 0, 0, 1);// Black.
	
	double thickness_current = 1.0;
	
	// -- Geometry Constants.

	final int CIRCLE		= 0;
	final int RECTANGLE 	= 1;
	final int RECTANGLE_R2 	= 2;
	final int TRIANGLE		= 3;
	final int CURVE			= 4;
	final int CIRCLE_OUTLINE= 5;
	final int POLYGON		= 6;
	

	private enum STATE{ TRANSPARENT, OPAQUE, CUT_OUT;}
	
	// -- Private class.
	// A mutable persistent State container object.
	private class State_Info extends Box<STATE>
	{
		public State_Info(STATE val_input){super(val_input);}		
	};

	
	// -- Constructors.
	public Image_vectorGeometry(Dimension dim)
	{
		super(dim);
		i_geoms();
		
	}
	public Image_vectorGeometry(int w, int h)
	{
		super(w, h);
		i_geoms();
	}
	
	public void clear()
	{
		data.clear();
		shapes.clear();
	}
	
	// Allows the creation of this object with extra data.
	public Image_vectorGeometry(int w, int h, boolean wait_to_i_geoms)
	{
		super(w, h);
		// i_geoms Purposely omitted.
	}
	

	// -- Initialization method for the scene's geometric description.
	public abstract void i_geoms();
	

	// -- Primitive Creation functions.
	public void i_circle(Vector center)
	{
		i_circle(center, thickness_current);
	}
	
	public void i_circle(Vector center, double radius)
	{
		if(center.length != 2)
		{
			throw new Error("Invalid 2D circle coordinates!");
		}		
		
		// 1. Add geometry id.
		data.add(v(CIRCLE));
		
		// 2. Add center vector.
		data.add(center);
		
		// 3. Add the square of the radius value.
		data.add(v(radius*radius));
		
		// 4. Add primary color. (body)
		data.add(color_primary);

		shapes.add(new Circle(center, radius));
	}
	
	public void i_circle_outline(Vector center, double radius)
	{
		i_circle_outline(center, radius, thickness_current);
	}
	
	public void i_circle_outline(Vector center, double radius, double thickness)
	{
		
		if(center.length != 2)
		{
			throw new Error("Invalid 2D circle coordinates!");
		}
		
		// 1. Add geometry id.
		data.add(v(CIRCLE_OUTLINE));
		
		// 2. Add center vector.
		data.add(center);
		
		// 3. Add the square of the radius value.
		
		//radius = radius + thickness;
		
		data.add(v(radius*radius));
		
		thickness = radius - thickness;
		
		thickness = Math.max(thickness, 0);
		
		// 4. Add the square of the minimum radius value.
		data.add(v(thickness*thickness));
		
		// 5. Add primary color. (body)
		data.add(color_primary);

		// Add a Circle to the list of Shapes.
		shapes.add(new Circle(center, radius));
	}
	
	public void i_rect(Vector v1, Vector v2)
	{
		i_rect(v1, v2, thickness_current);
	}
	
	public void i_rect(Vector v1, Vector v2, double radius)
	{
		Vector mid_point = v1.add(v2).div(2);
		
		// 1. Add geometry id.
		data.add(v(RECTANGLE));
		
		// 2. Add the Mid Point
		data.add(mid_point);
		
		// Parallel norm.
		Vector par_norm = v2.sub(v1).norm();
		
		// 3. Add the parallel unit direction vector.
		data.add(par_norm);

		// 4. Add the square of the parallel radius value.
		data.add(v(v2.sub(mid_point).sqr_mag()));
		
		// 5. Add the square of the perpendicular radius value.
		data.add(v(radius*radius));
		
		// 6. Add primary color. (body)
		data.add(color_primary);
		
		
		// -- Add a new Quadrilateral to the list of shapes.
		Vector s1, s2, s3, s4;
		
		Vector perp = par_norm.getPerp().mult(radius);
				
		s1 = v1.sub(perp);
		s2 = v1.add(perp);
		
		s3 = v1.add(perp);
		s4 = v1.sub(perp);
		
		shapes.add(new Polygon(s1, s2, s3, s4));
	}

	// Allows the creation of a rectangle that gradually changes from one radius to another.
	public void i_inclined_rect(Vector v1, Vector v2, double radius1, double radius2)
	{
		Vector mid_point = v1.add(v2).div(2);
		
		// 1. Add geometry id.
		data.add(v(RECTANGLE_R2));
		
		// 2. Add the Mid Point
		data.add(mid_point);
		
		// 3. Add the 2 end points.
		data.add(v1);
		
		// 4.
		data.add(v2);
		
		// 5. Add the parallel unit direction vector.
		data.add(v2.sub(v1).norm());

		// 6. Add the square of the parallel radius value.
		data.add(v(v2.sub(mid_point).sqr_mag()));
		
		// 7. Add the first perpendicular radius value.
		data.add(v(radius1));
		
		// 8. Add the second perpendicular radius value.
		data.add(v(radius2));
		
		// 9. Add primary color. (body)
		data.add(color_primary);
	}

	public void i_triangle(Vector v1, Vector v2, Vector v3)
	{
		// Instantiate the header.
		data.add(v(TRIANGLE));
		
		// Include each of the three points.
		data.add(v1);
		data.add(v2);
		data.add(v3);
		
		// Include the proper color.
		data.add(color_primary);		
		
		shapes.add(new Polygon(v1, v2, v3));
		
	}
	
	public void i_curve(Vector v1, Vector v2, Vector v3)
	{
		i_curve(v1, v2, v3, thickness_current);
	}
	
	final double SMALL = .0000001;
	
	// A simple curve defined by 3 points.
	public void i_curve(Vector v1, Vector v2, Vector v3, double radius)
	{
		// -- Handle trivial cases.
		if(v1.sub(v2).mag() < SMALL)
		{
			
			// All vector points are the same.
			if(v1.sub(v3).mag() < SMALL)
			{
				i_circle(v1, radius);
				return;
			}
			
			// Two the same.
			i_rect(v1, v3, radius);
			return;
		}
		
		// Two the same.
		if(v1.sub(v3).mag() < SMALL)
		{
			// Two the same.
			i_rect(v1, v2, radius);
			return;
		}
		
		// Two the same.
		if(v2.sub(v3).mag() < SMALL)
		{
			// Two the same.
			i_rect(v1, v2, radius);
			return;
		}
		
		// We compute the unit vectors representing the two sides.
		
		Vector side1 = v2.sub(v1).norm();
		Vector side2 = v3.sub(v2).norm();
		
		// Handle trivial collinear linear collections of points.
		if(Math.abs(side1.dot(side2)) > .9999)
		{
			Vector side3 = v1.sub(v3);
			
			// Compute the lengths of the vectors.
			double l1 = side1.mag();
			double l2 = side2.mag();
			double l3 = side3.mag();
			
			// Create a rectangle of the given radius that passes through all of the vertices.
			
			// Side v2 - v1 is the longest.
			if(l1 > l2 && l1 > l3)
			{
				i_rect(v2, v1, radius);
				return;
			}

			// Side v2 - v1 is the longest.
			if(l2 > l1 && l2 > l3)
			{
				i_rect(v3, v2, radius);
				return;
			}
			
			i_rect(v1, v3, radius);
			return;
			
		}

		// Now we will compute the midpoint of v1-v2 and v2-v3;
		
		Vector mid1 = v1.add(v2).div(2);
		Vector mid2 = v2.add(v3).div(2);
		
		// We will now compute a vector that is perpendicular to each side of the triangle. (V1V2 and V2V3)
		
		// NOTE : We know that these calculations produce perpendicular vectors, because the two sides are linearly independent and non trivial.
		
		// We do this by projecting the vectors onto each other and subtracting the parallel components.
		Vector mid1_perp = side2.sub(side2.proj(side1));
		
		Vector mid2_perp = side1.sub(side1.proj(side2));
		
		// Now find the intersetion of the two lines formed by adding mutiples of the perpendiculars to their respective midpoint.
		
		/*
		 * Mid1_{0} + mid1_perp_{0}*X = Mid2_{0} + mid2_perp_{0}*Y
		 * Mid1_{1} + mid1_perp_{1}*X = Mid2_{1} + mid2_perp_{1}*Y
		 * 
		 * mid1_perp_{0}*X - mid2_perp_{0}*Y = Mid2_{0} - Mid1_{0}
		 * mid1_perp_{1}*X - mid2_perp_{1}*Y = Mid2_{1} - Mid1_{1}
		 * 
		 * This system can be solved by standard Ax = b;
		 */
		
		Matrix A = new Matrix(v(mid1_perp.get(0), - mid2_perp.get(0)),
							  v(mid1_perp.get(1), - mid2_perp.get(1))
							 );
		
		Vector b = v(mid2.get(0) - mid1.get(0), mid2.get(1) - mid1.get(1));
		
		Vector resultingScalars = A.solve(b);
		
		Vector center = mid1.add(mid1_perp.mult(resultingScalars.get(0))); 
		
		// Relate each point to the center.
		v1 = v1.sub(center);
		v2 = v2.sub(center);
		v3 = v3.sub(center);
		
		// Compute angles.
		double angle1 = lineAngle(v1.get(0), v1.get(1), 0, 0);
		double angle2 = lineAngle(v2.get(0), v2.get(1), 0, 0);
		double angle3 = lineAngle(v3.get(0), v3.get(1), 0, 0);

	
		if(angle3 < angle1)
		{
			angle3 += 360;
		}
		
		if(angle2 < angle1)
		{
			angle2 += 360;
		}

		// If the angles were inputed in the wrong direction, then swap v1 and v3 and their angles.
		if(angle2 > angle3)
		{
			Vector temp = v1;
			v1 = v3;
			v3 = temp;
			
			double temp2 = angle1;
			angle1 = angle3;
			angle3 = temp2;
			
			angle1 -= 360;
			angle2 -= 360;
		}
		
		//print("A1 = " + angle1 + ", A2 = " + angle2 + ", A3 = " + angle3);
		
		// Compute the radiuses of the circle.
		double circle_radius = v1.mag();
		
		double minRadius = Math.max(circle_radius - radius, 0);
		
		double maxRadius = circle_radius + radius;
	
		// Remember whether we are computing an interior of exterior section.
		boolean interior = true;
		
		// If the curve is greater than 180 degrees, then we need to instead compute the exterior of the complementary curve.
		if(angle3 - angle1 > 180)
		{
			interior = false;
			Vector temp = v3;
			v3 = v1;
			v1 = temp;

			// The angles need to be swapped as well.
			double tempAngle = angle1;
			angle1 = angle3;
			angle3 = tempAngle;
			
			double anglev2 = angle1 + .0000001;
			
			v2 = v(Math.cos(Math.toRadians(anglev2)),
				   -Math.sin(Math.toRadians(anglev2)));

		}
		//-- Now offload the precomputed data.
				
		// Instantiate the header.
		data.add(v(CURVE));
		
		// Add the center location.
		data.add(center);
	
		// Add the normalized vectors.
		data.add(v1.norm());
		data.add(v3.norm());

		// Add cross products.
		data.add(v2.cross(v1));
		data.add(v2.cross(v3));
			
		// Add whether we are computing the exterior or not.
		if(interior)
		{
			data.add(v(-1));
		}
		else
		{
			data.add(v(1));
		}

		// Add minimum radius squared
		data.add(v(minRadius*minRadius));

		// Add maximum radius squared
		data.add(v(maxRadius*maxRadius));

		// Add the current color.
		data.add(color_primary);

	}
	
	public void i_polygon(Vector ... points)
	{
		// Instantiate the header.
		data.add(v(POLYGON));
		
		// Include the proper color.
		data.add(color_primary);
		
		int len = points.length;
		
		// Find out how many points there are.
		data.add(v(len));
		
		// -- Include each point on the polygon.
	
		for(int i = 0; i < len; i++)
		{
			data.add(points[i]);
		}
		
		// Handle Edge case.
		data.add(points[0]);
		
		shapes.add(new Polygon(points));
	}
	
	// -- Composite shapes.
	
	public void i_round_rect(Vector v1, Vector v2)
	{
		i_round_rect(v1, v2, thickness_current);
	}
	
	// -- Compound Geometry creation functions.
	public void i_round_rect(Vector v1, Vector v2, double radius)
	{
		
		// Create rounded ends for the rectangle.
		i_circle(v1, radius);
		i_circle(v2, radius);
		
		// Create a rectangle on top of them.
		i_rect(v1, v2, radius); 	
	}
	
	public void i_line(Vector ... points)
	{
		i_line(thickness_current, points);
	}
	
	// Create a connected string of rounded rectangles.
	public void i_line(double radius, Vector ... points)
	{
		int end = points.length;
		for(int i = 1; i < end; i++)
		{
			i_circle(points[i - 1], radius);
			i_rect(points[i - 1], points[i], radius);
		}
		
		// Create the end circle.
		if(end > 0)
		{
			i_circle(points[end - 1], radius);
		}
		
	}
			
	// -- Color Calculation Method.
	public Color getColor(double x, double y)
	{
		// Convert coordinates to a position vector.
		Vector z = v(x, y);
		
		// Initialize the color_stack;
		UBA<Vector> CS	= new UBA<Vector>();
		
		// Calculate the color stack for this pixel;			
		parseGeometry(z, CS);		
		
		// Parse the final color from the Color stack.
		return parseColor(CS);
				
	}
	
	// Parses the data list and calls geometry calculation functions to build the color stack;
	private void parseGeometry(Vector z, UBA<Vector> CS)
	{
		// Initialize list iteration variables.
		int i 	= 0;
		int end = data.size();
		State_Info opaque = new State_Info(STATE.TRANSPARENT);
		
		while( i < end)
		{
		
			STATE state = opaque.val;
			if(state == STATE.OPAQUE)
			{
				break;
			}
			
			if(state == STATE.CUT_OUT)
			{
				
			}
			
			// Watach out for Floating point errors.
			int geometry_type = (int) data.get(i).get(0);
			
			// Move i to the start of the current geometry's data.
			i++;
		
			switch(geometry_type)
			{
				case CIRCLE:
					i = c_circle(i, z, CS, opaque);
					break;
					
				case CIRCLE_OUTLINE:
					i = c_circle_outline(i, z, CS, opaque);
					break;				
					
				case RECTANGLE:
					i = c_rectangle(i, z, CS, opaque);
					break;
					
				case RECTANGLE_R2:
					i = c_rectangle_r2(i, z, CS, opaque);
					break;
				
				case TRIANGLE:
					i = c_triangle(i, z, CS, opaque);
					break;
				case CURVE:
					i = c_curve(i, z, CS, opaque);
					break;
				case POLYGON:
					i = c_polygon(i, z, CS, opaque);
					break;
					
				default: throw new Error("Malformed Data list. Last code was : " + geometry_type);
			}
			
		}
		
	}
	
	// Blends the top two elements of the color stack together according to the alpha value of the second element.
	private Color parseColor(UBA<Vector> color_stack)
	{
		// Initialize iteration variables.
		int end = color_stack.size();
		
		// Start with a clear background color.
		Vector output = v(0,0,0,0);

		// The number of iterations has been predetermined by the size of CS.
		for(int i = 0; i < end; i++)
		{
			
			// Alpha weighted expectation values if you will.
			double vTop, vBottom;
			
			Vector top	= color_stack.pop();
			
			// weights from Alpha value of top color;
			vTop = top.get(3);
			vBottom = 1 - vTop;
			
			// Blend the top color onto the output bottom color.
			output = top.mult(vTop).add(output.mult(vBottom));
		}
			
		// Convert the output vector to a Color and return it.
		return v_to_color(output);
	}

	// -- Geometry Calculation Functions. (These must agree with their initializations!)
	
	
	private int c_circle(int i, Vector z, UBA<Vector> CS, State_Info opaque)
	{	
		if(cutOutState(opaque, data.get(i + 2)))
		{
			return i + 3;
		}
		
		// Initialize geometric data.
		Vector center 			= data.get(i);
		double radius_squared	= data.get(i + 1).get(0);// 1 dimensional vector parsed as a double value.
		
		// Shift to an origin centered vector;
		z = z.sub(center);
		
		// Perform an efficient circle calculation.
		if(z.sqr_mag() <= radius_squared)
		{
			Vector color = data.get(i + 2);
			// A blend of the primary and secondary colors.
			CS.push(color);
			
			// Check whether we have found an opaque color yet.
			opaque.val = getState(color);
		}
		
		// Returns the index of the next potential piece of geometry.
		return i + 3;
	}
	
	private int c_circle_outline(int i, Vector z, UBA<Vector> CS, State_Info opaque)
	{	
		
		if(cutOutState(opaque, data.get(i + 3)))
		{
			return i + 4;
		}
		
		// Initialize geometric data.
		Vector center 			= data.get(i);
		double radius_squared	= data.get(i + 1).get(0);// 1 dimensional vector parsed as a double value.
		double min_radius_squared = data.get(i + 2).get(0);
		
		// Shift to an origin centered vector;
		z = z.sub(center);
		
		double z_mag_sqr = z.sqr_mag();
		
		// Perform an efficient circle calculation.
		if(min_radius_squared < z_mag_sqr && z_mag_sqr <= radius_squared)
		{
			Vector color = data.get(i + 3);
			// A blend of the primary and secondary colors.
			CS.push(color);
			
			// Check whether we have found an opaque color yet.
			opaque.val = getState(color);
		}
		
		// Returns the index of the next potential piece of geometry.
		return i + 4;
	}
	
	private int c_rectangle(int i, Vector z, UBA<Vector> CS, State_Info opaque)
	{
		if(cutOutState(opaque, data.get(i + 4)))
		{
			return i + 5;
		}
		
		// -- Initialize geometric data.
		Vector mid_point			= data.get(i);
		
		Vector parrellel_direction	= data.get(i + 1);
		
		// Parallel rectilinear radius.
		double par_r_sqr			= data.get(i + 2).get(0);
		
		// Perpendicular rectilinear radius.
		double perp_r_sqr			= data.get(i + 3).get(0);
		
		
		// -- Compute the color.
		
		// First center the input vector on the mid_point.
		z = z.sub(mid_point);
		
		// Compute the squares of the parallel and perpendicular components of the vector.
		Vector parrellel_componant_vector	= z.proj(parrellel_direction);
		double parrellel_componant_sqr		= parrellel_componant_vector.sqr_mag();
		double perpendicular_componant_sqr	= z.sub(parrellel_componant_vector).sqr_mag();
		
		// Color inbounds pixels.
		if(parrellel_componant_sqr <= par_r_sqr && perpendicular_componant_sqr <= perp_r_sqr)
		{
			// Add the geometry's color to the stack.
			Vector color = data.get(i + 4);
			CS.push(color);
			
			// Check for opaque colors.
			opaque.val = getState(color);
		}
		
		return i + 5;
	}
	
	private int c_rectangle_r2(int i, Vector z, UBA<Vector> CS, State_Info opaque)
	{
		
		if(cutOutState(opaque, data.get(i + 7)))
		{
			return i + 8;
		}
		
		// Store the original z;
		Vector z_start = z;
		
		// -- Initialize geometric data.
		Vector mid_point			= data.get(i);
		
		Vector v1 = data.get(i + 1);
		
		Vector v2 = data.get(i + 2);
		
		Vector parrellel_direction	= data.get(i + 3);
		
		// Parallel rectilinear radius.
		double par_r_sqr			= data.get(i + 4).get(0);
		
		// Perpendicular rectilinear radius1.
		double perp_r1			= data.get(i + 5).get(0);

		// Perpendicular rectilinear radius1.		
		double perp_r2			= data.get(i + 6).get(0);
		
		// -- Compute the color.
		
		// First center the input vector on the mid_point.
		z = z.sub(mid_point);
		
		// Compute the squares of the parallel and perpendicular components of the vector.
		Vector parrellel_componant_vector	= z.proj(parrellel_direction);
		double parrellel_componant_sqr		= parrellel_componant_vector.sqr_mag();
		double perpendicular_componant		= z.sub(parrellel_componant_vector).mag();
		
		// Calculate the percentage we are along the parrallel componant.
		double parralled_percentage = .5*(Math.sqrt(parrellel_componant_sqr) / Math.sqrt(par_r_sqr));
		

		double mag1 = z_start.sub(v1).mag();
		double mag2 = z_start.sub(v2).mag();

		
		// Adjust the parralled_percentage value;
		if(mag1 < mag2)
		{
			parralled_percentage = .5 - parralled_percentage;
		}
		else
		{
			parralled_percentage += .5;
		}
		
		double perp_here = parralled_percentage*perp_r2 + (1 - parralled_percentage)*perp_r1;
		
		// Color inbounds pixels.
		if(parrellel_componant_sqr <= par_r_sqr && perpendicular_componant <= perp_here)
		{
			// Add the geometry's color to the stack.
			Vector color = data.get(i + 7);
			CS.push(color);
			
			// Check for opaque colors.  
			opaque.val = getState(color);
		}
		
		return i + 8;
	}
	
	// Compute highly efficient triangles.
	private int c_triangle(int i, Vector z, UBA<Vector> CS, State_Info opaque)
	{
		
		if(cutOutState(opaque, data.get(i + 3)))
		{
			return i + 4;
		}
		
		
		Vector A = data.get(i);
		Vector B = data.get(i + 1);
		Vector C = data.get(i + 2);
		Vector P = z;
		
		double[] barycentric = Geometry.barycentric(P, A, B, C);

		double u = barycentric[0];
		double v = barycentric[1];
		//double w = barycentric[2];
		
		// Check if point is in triangle
		if((u >= 0) && (v >= 0) && (u + v < 1))
		{
			// Add the geometry's color to the stack.
			Vector color = data.get(i + 3);
			CS.push(color);
			
			// Check for opaque colors.  
			opaque.val = getState(color);
		}
		
		return i + 4;
		
	}
	
	// Compute whether a given coordinate is inside of this curve.
	// z is the coordinate, CS is a Color Stack.
	private int c_curve(int i, Vector z, UBA<Vector> CS, State_Info opaque)
	{
		
		if(cutOutState(opaque, data.get(i + 8)))
		{
			return i + 9;
		}
		
		Vector center = data.get(i);
		
		Vector end_point1   = data.get(i + 1);
		Vector end_point2 	= data.get(i + 2);
		
		double cross1A = data.get(i + 3).get(2);
		double cross2A = data.get(i + 4).get(2);
		
		boolean exterior = data.get(i + 5).get(0) == 1;
			
		double min_radius_sqr = data.get(i + 6).get(0);
		double max_radius_sqr = data.get(i + 7).get(0);
		
		// Center z
		z = z.sub(center);
		
		/* 
		 * First we need to know if the given point is within the
		 * correct distance away from the center.
		 */
		double dist_sqr = z.sqr_mag();
		
		// Return if the coordinate has a bad radius.
		if(dist_sqr > max_radius_sqr || dist_sqr < min_radius_sqr)
		{
			return i + 9;
		}

		/* 
		 * Determine whether a point is on the correct side of lines by the direction
		 * of the z component of the cross product of two 2 dimensional vectors.
		 */
		int sign1 = sign(z.cross(end_point1).get(2));
		int sign2 = sign(z.cross(end_point2).get(2));
		
		boolean pred = sign1 == sign(cross1A) && sign2 == sign(cross2A);

		if(exterior)
		{
			pred = !pred;
		}
		
		if(pred)
		{
			// Add the geometry's color to the stack.
			Vector color = data.get(i + 8);
			CS.push(color);
			
			// Check for opaque colors.  
			opaque.val = getState(color);
		}
		
		return i + 9;
	}
	
	// Compute whether a given coordinate is inside of this curve.
	// z is the coordinate, CS is a Color Stack.
	private int c_polygon(int i, Vector z, UBA<Vector> CS, State_Info opaque)
	{
		Vector color = data.get(i);
		
		int sides = (int) data.get(i + 1).get(0);
		
		int output_index = i + sides + 3;
		
		if(cutOutState(opaque, color))
		{
			return output_index;
		}
		
		boolean odd = false;
				
		// An arbitrary direction vector.
		Vector dZ = v(1, 0);

		for(int k = 0; k < sides; k++)
		{
			Vector p1 = data.get(i + 2 + k);
			Vector p2 = data.get(i + 2 + k + 1);
			
			if(Geometry.ray_line_segment_intersect(z, dZ, p1, p2))
			{
				odd = !odd;
			}
		}
				
		if(odd)
		{
			// Add the geometry's color to the stack.
			CS.push(color);
			
			// Check for opaque colors.  
			opaque.val = getState(color);
		}
		
		return output_index;
	}
	
	private int sign(double input)
	{
		if(input == 0)
		{
			return 0;
		}
		
		if(input > 0)
		{
			return 1;
		}

		// less than.
		return -1;

	}
	
	// -- Local Variable Functions.
	
	// Set the primary color.
	protected void set_color(Color c1)
	{
		 color_primary = color_to_v(c1);
	}
	
	// Set the drawing line thickness.
	protected void set_thickness(double val)
	{
		thickness_current = val;
	}
	
	// -- Helper Functions.
	
	// forms a vector from input doubles.
	public Vector v(double ... input)
	{
		return new Vector(input);
	}
	
	/* Returns true whether a color is opaque or not.
	 * Clear geometry will clear the image to transparent.
	 * FIXME : Perhaps I should instead only cut out a transparent section from the shape below this one if it is 0.
	 */
	private STATE getState(Vector color)
	{
		double val = color.data[3];
		
		if(val == 1)
		{
			return STATE.OPAQUE;
		}
		
		if(val == 0)
		{
			return STATE.CUT_OUT;
		}
		
		return STATE.TRANSPARENT;
	}
	
	// Given a state info, this will return true if it is in cut out mode.
	//Mutates it back to transparent mode if the input color is not another cut out.
	private boolean cutOutState(State_Info state, Vector color)
	{		
		
		if(state.val == STATE.CUT_OUT)
		{
			if(getState(color) != STATE.CUT_OUT)
			{
				state.val = STATE.TRANSPARENT;
			}
			return true;
		}
		
		return false;
	}
	
	// Converts between vector implementation of colors and java Colors.
	private Color v_to_color(Vector input)
	{
		assert(input.length == 4);
		return new Color(	(int)input.get(0), (int)input.get(1),
							(int)input.get(2), (int)(input.get(3)*255)
						 );
	}
	
	private Vector color_to_v(Color input)
	{
		return v(input.getRed(), input.getGreen(),
				 input.getBlue(), 1.0*input.getAlpha()/255
				);
	}
	
	// takes and angle in degree and converts it to a unit vector in the given direction.
	public Vector v_dir(double angle)
	{
		return v(Math.cos(Math.toRadians(angle)),- Math.sin(Math.toRadians(angle)));
	}
	
	/* FIXME We need to allow for geometries to be linearly mapped to the image.
	// Appends another specified geometry to this one.
	public void append(Image_vectorGeometry input)
	{
		data.append(input.data);
		shapes.append(input.shapes);
	}
	*/

}