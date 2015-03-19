package Projects.Math.LessonLogic;

import java.io.PrintStream;

import util.SerialB;
import BryceMath.Numbers.Equation;
import BryceMath.Structures.Matrix;

/*
 * Matric data object.
 * 
 * Written by Bryce Summers on 7 - 10 - 2013.
 * 
 * Purpose : Stores the complete information regarding a matrix that has been eliminated.
 */

public class ProblemData implements SerialB
{

	private Matrix<Equation> matrix;
	private Equation scalar;
	private boolean show_scalar;
	
	public ProblemData(Matrix<Equation> matrix, Equation scalar) 
	{
		this.matrix = matrix;
		this.scalar = scalar;
		show_scalar = false;
	}
	
	// -- Setter.
	public void setShowScalar(boolean flag)
	{
		show_scalar = flag;
	}	
	
	// -- Getters.
	public Matrix<Equation> getMatrix()
	{
		return matrix;
	}
	
	public Equation getScalar()
	{
		return scalar;
	}
	
	public boolean getShowScalar()
	{
		return show_scalar;
	}
	
	@Override
	public void serializeTo(PrintStream stream)
	{
		// Serialize the display of the scalar.
		stream.println(show_scalar);
		
		// First serialize the determinant scalar.
		scalar.serializeTo(stream);

		// Second serialize the Matrix.
		matrix.serializeTo(stream);
	}

	@Override
	public String getSerialName()
	{
		return "ProblemData";
	}

}
