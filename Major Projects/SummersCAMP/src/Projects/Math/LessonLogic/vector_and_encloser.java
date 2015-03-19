package Projects.Math.LessonLogic;

import BryceMath.Numbers.Equation;
import BryceMath.Structures.Vector;
import BryceMath.Structures.Matrix;
import Game_Engine.GUI.Components.Input.textBased.gui_EquationInput;

/*
 * The vector extractor and metadata class.
 * 
 * Written by Bryce Summers on 8 - 21 - 2013.
 * 
 * This class packages a Vector<Equation> with a corresponding column or row number and its corresponding matrix.
 * 
 * 
 */

public class vector_and_encloser
{

	// -- private data.

	private final Vector<Equation> vector;

	private final boolean is_row_vector;

	private final int location_number;

	private final Matrix<Equation> matrix;

	// A box that provides values to the determinant.
	// Because this is a box, the values can be changed dynamically.
	private final gui_EquationInput determinant_scalar;	

	// -- Constructor.
	public vector_and_encloser(Vector<Equation> v, boolean is_row, int number, Matrix<Equation> enclosing, gui_EquationInput scalar)
	{
		vector = v;
		is_row_vector = is_row;
		location_number = number;
		matrix = enclosing;		
		determinant_scalar = scalar;
	}

	// -- Data access functions.
	public Vector<Equation> getVector()
	{
		return vector;
	}
	
	public boolean isRow()
	{
		return is_row_vector;
	}
	
	public int getNumber()
	{
		return location_number;
	}
	
	public Matrix<Equation> getMatrix()
	{
		return matrix;
	}
	
	public gui_EquationInput getScalarBox()
	{
		return determinant_scalar;
	}
}
