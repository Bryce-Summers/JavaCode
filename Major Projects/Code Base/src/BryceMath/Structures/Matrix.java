package BryceMath.Structures;

import java.io.PrintStream;

import util.Genarics;
import util.SerialB;
import BryceMath.Numbers.Number;
import Data_Structures.Structures.Pair;

/*
 * Matrix Class. Deals with most Linear algebra needs.
 * Written by Bryce Summers, 11/14/2012.
 * Extensive work done on 12 - 28 - 2012.
 * 
 * More work done the week of 7 - 15 - 2013.
 */

// FIXME : I need to clean up and seriously test all of this code.

// FIXME : I should implement timesTranspose() and transposeTimes() functions.

public class Matrix<T extends Number<T>> implements SerialB
{
	// The data stored in the matrix.
	// Row major ordering. [row][col]
	private T[][] data;
	
	// The matrix's dimensions.
	private final int m;
	private final int n;
	
	// Meta data.
	
	// The matrix has been eliminated, but with non-normalized pivots.
	private boolean ge_form = false;
	private boolean ge1_form = false;
	private boolean echelon_form = false;
	private int 	rank = -1;

	// Deals with T elements.
	private Genarics<T> ge_t   = new Genarics<T>();
	// Deals with Vector_n<T> elements.
	private Genarics<Vector<T>> ge_v = new Genarics<Vector<T>>();
	// Deals with Matrix<T> elements.
	private Genarics<Matrix<T>> ge_m = new Genarics<Matrix<T>>();
	
	// -- Constructors
	public Matrix(Vector<T> ... rows)
	{
		m = rows.length;
		n = rows[0].length;
		
		data = ge_t.Array_2d(m, n, rows[0].get(0));
		
		for(int c = 0; c < n; c++)
		for(int r = 0; r < m; r++)
		{
			data[r][c] = rows[r].get(c);
		}
		
	}
	
	// FIXME: Decide how to deal with erroneous vector array inputs.
	// FIXME: Perhaps condense these constructors. 
	// FIXME: Implement genaric numbers and vectors.
	// FIXME: Consider storing the eliminated matrix for further use.
	
	// -- Constructors
	public Matrix(boolean want_cols, Vector<T> ... cols)
	{
		// Ignore the want_cols flag.
		
		n = cols.length;
		m = cols[0].length;
						
		data = ge_t.Array_2d(m, n, cols[0].get(0));
		
		for(int c = 0; c < n; c++)
		for(int r = 0; r < m; r++)
		{
			data[r][c] = cols[c].get(r);
		}
		
	}
	
	// Takes in a Matrix that is in [row][col] form. (Row major).
	public Matrix(T[][] data)
	{
		
		this.data = data;
		m = data.length;
		n = data[0].length;
	}
	
	// -- Fundamental Matrices operations.

	public Matrix<T> transpose()
	{
		// Interpret this matrix's columns as rows.
		return new Matrix<T>(columns());
	}
	
	// Returns the conjugate of this matrix.
	public Matrix<T> conjugate()
	{
		Matrix<T> output = clone();
		for(int r = 0; r < m; r++)
		for(int c = 0; c < n; c++)
		{
			output.data[r][c] = output.data[r][c].conj();
		}
		return output;
	}
	
	public Matrix<T> conjugateTranspose()
	{
		throw new Error("Implement Me Please!");
	}
	
	//Clones this matrix. FIXME : Check to see if this is efficient or not, it can probably be done better. 
	@Override
	public Matrix<T> clone()
	{
		return new Matrix<T>(rows());
	}
	
	// Product of the eigen vectors.
	public T det()
	{
		if(m != n)
		{
			throw new Error("ERROR: Determinant only defined for square matrices.");
		}
		
		Matrix<T> reduced = ge();
		T result = data[0][0].one();
		
		for(int c = 0; c < n; c++)
		{
			result = result.mult(reduced.get(c, c));
		}
		
		return result;
	}
	
	// Sum of the diagonal elements / Sum of the eigen vectors.
	public T trace()
	{
		if(m != n)
		{
			throw new Error("ERROR: Trace only defined for square matrices.");
		}
		
		T result = data[0][0].zero();
		
		for(int c = 0; c < n; c++)
		{
			result = result.add(get(c, c));
		}
		
		return result;
	}
	
	public int getRank()
	{
		// If rank has not yet been computed.
		if(rank < 0)
		{
			rank = ge().rank;
		}
		
		return rank;
	}
	
	// Returns NULL if the matrix has no inverse.
	public Matrix<T> inverse()
	{
		if(m != n)
		{
			throw new Error("ERROR: Non-sqaure matrices have no inverse.");
		}
		
		// If this Matrix is singular, it has no inverse.
		if (det().eq(0))
		{
			return null;
		}
		
		// Perform Gaus-Jordan elimination.
		
		// Augment A with the proper identity matrix.
		Matrix<T> A = appendCol(identity(m).columns());
		
		// Reduce A to Echelon form.
		A = A.echelon();
		
		// Return the second half of the augmented matrix.
		Vector<T>[] output = ge_v.Array(m, getCol(0));
		
		for(int c = 0; c < n; c++)
		{
			output[c] = A.getCol(n + c);
		}
		
		// Convert columns to a matrix.
		return new Matrix<T>(true, output);
		
	}
	
	// -- Computations.
	
	// Ax = b;
	// FIXME: We need to deal with unsolvable systems and systems with infinite solutions.

	@SuppressWarnings("unchecked")
	public Vector<T> solve(Vector<T> b)
	{
		// Create an augmented matrix.
		Matrix<T> A = appendCol(b);
		
		// Gaussian eliminate A then reverse eliminate it.
		A = A.echelon();
		
		// Return the eliminated solution vector.
		return A.getCol(n);
	}
	
	// Returns the LDU factorization for this Matrix if it exists.
	// Returns null otherwise.
	// FIXME: Decide what form of output is best.
	public Matrix<T>[] LDU()
	{
		Matrix<T> L = identity(m);
		Matrix<T> D = identity(m);
		Matrix<T> U = ge2(L, D);
		
		if(U == null)
		{
			//throw new Error("ERROR: LDU factorization does not exist.");
			return null;
		}
		
		//System.out.println(L);
		
		// Form output.
		Matrix<T>[] output = ge_m.Array(3, this);
		output[0] = L;
		output[1] = D;
		output[2] = U;
		
		return output;
	}
	
	
	// Returns the QR factorization for this matrix, if it exists.
	public Matrix<T>[] QR()
	{
		throw new Error("Not yet Implemented.");
	}
	
	public Matrix<T> mult(Matrix<T> other)
	{
		
		if(n != other.m)
		{
			throw new Error("Error : Invalid Matrix dimensions for proper multiplication");
		}
		
		Vector<T>[] rows = rows();
		Vector<T>[] cols = other.columns();
		
		int row_num = rows.length;
		int col_num = cols.length;
		
		T[][] data = ge_t.Array_2d(row_num, col_num, this.data[0][0]);
		
		// Perform matrix multiplication via vector arithmetic.
		for(int r = 0; r < row_num; r++)
		for(int c = 0; c < col_num; c++)
		{
			data[r][c] = rows[r].dot(cols[c]);
		}
		
		return new Matrix<T>(data);
	}
	
	public Matrix<T> add(Matrix<T> other)
	{
		if(m != other.m || n != other.n)
		{
			throw new Error("Error : Dimensions do not match");
		}
		
		T[][] data_new = ge_t.Array_2d(m, n, get(0, 0));
		
		for(int r = 0; r < m; r++)
		for(int c = 0; c < n; c++)
		{
			data_new[r][c] = data[r][c].add(other.data[r][c]);
		}
		
		return new Matrix<T>(data_new);
	}
	
	public Matrix<T> sub(Matrix<T> other)
	{
		if(m != other.m || n != other.n)
		{
			throw new Error("Error : Dimensions do not match");
		}
		
		T[][] data_new = ge_t.Array_2d(m, n, get(0, 0));
		
		for(int r = 0; r < m; r++)
		for(int c = 0; c < n; c++)
		{
			data_new[r][c] = data[r][c].sub(other.data[r][c]);
		}
		
		return new Matrix<T>(data_new);
	}
	
	public Matrix<T> div(T scalar)
	{
		T[][] data_new = ge_t.Array_2d(m, n, get(0, 0));
		
		for(int r = 0; r < m; r++)
		for(int c = 0; c < n; c++)
		{
			data_new[r][c] = data[r][c].div(scalar);
		}
		
		return new Matrix<T>(data_new);
	}
	
	public Matrix<T> mult(T scalar)
	{
		T[][] data_new = ge_t.Array_2d(m, n, get(0, 0));
		
		for(int r = 0; r < m; r++)
		for(int c = 0; c < n; c++)
		{
			data_new[r][c] = data[r][c].mult(scalar);
		}
		
		return new Matrix<T>(data_new);
	}
	
	// Returns the reduced echelon form of this matrix.
	public Matrix<T> echelon()
	{
		if(echelon_form)
		{
			return this;
		}
		
		// First Gaussian eliminate, normalizing the pivots.
		Matrix<T> A = ge1();
		
		Vector<T>[] rows = A.rows();
		
		for(int r = m - 1; r > 0; r--)
		{
			// First find the r and c coordinates of the pivot in this row.
			int c = 0;
			boolean found = true;
			
			while(rows[r].get(c).eq(0))
			{
				c++;
				if(c == n)
				{
					found = false;
					break;
				}
			}
			
			if(!found)
			{
				break;
			}
			
			// Next reverse eliminate.
			for(int r2 = r - 1; r2 >= 0; r2--)
			{
				rows[r2] = rows[r2].sub(rows[r].mult(rows[r2].get(c)));
			}
		}
		
		// Prepare output;
		Matrix<T> output = new Matrix<T>(rows);
		output.echelon_form = true;
		output.ge1_form = true;
		output.ge_form  = true;
		rank = A.rank;
		
		return output;
	}
	
	// Returns the gaussian eliminated version of this matrix;
	// Pivots are not normilized.
	// Resulting matrix also knows its rank.
	public Matrix<T> ge()
	{
		if(ge_form)
		{
			return this;
		}
		
		Vector<T>[] rows = rows();
		
		// Stores the index of the last non eliminated columns.
		int c = 0;
		int rank = 0;
		
		// Form a pivot in every row.
		for(int r = 0; r < m && c < n; r++)
		{
			// The row that will be eliminated.
			int r2 = r + 1;
			
			boolean pivot_found = true;
			
			// Search for a pivot for this column.
			if(rows[r].get(c).eq(0))
			{
				pivot_found = false;
				// Search for a possible entry for a row exchange.
				
				while(r2 < m)
				{
					if(!(rows[r2].get(c).eq(0)))
					{
						swap(rows,r, r2);
						r2++;
						pivot_found = true;
						break;
					}
					
					r2++;
				}
			}
			
			
			// If this entire column is filled with 0s,
			// try the next column.
			if(!pivot_found)
			{
				r--;
				c++;
				continue;
			}
			
			T pivot = rows[r].get(c);
			rank++; 
			
			// Create a vector used for eliminating the rest of the rows.
			Vector<T> temp = rows[r].div(pivot);
			
			// Normalizes pivots.
			// rows[r] = temp;
			
			// Now eliminate all non 0 entries.
			while(r2 < m)
			{
				rows[r2] = rows[r2].sub(temp.mult(rows[r2].get(c)));				
				r2++;
			}
			
			c++;
		}// End of outer for loop.
		
		Matrix<T> output = new Matrix<T>(rows); 
		output.ge_form = true;
		output.rank = rank;
		
		return output;
	}

	// Returns the gaussian eliminated version of this matrix;
	// Pivots are indeed normalized.
	// Resulting matrix also knows its rank.
	public Matrix<T> ge1()
	{
		if(ge1_form)
		{
			return this;
		}
		
		Vector<T>[] rows = rows();
		
		// Stores the index of the last non eliminated columns.
		int c = 0;
		int rank = 0;
		
		// Form a pivot in every row.
		for(int r = 0; r < m && c < n; r++)
		{
			// The row that will be eliminated.
			int r2 = r + 1;
			
			boolean pivot_found = true;
			
			// Search for a pivot for this column.
			if(rows[r].get(c).eq(0))
			{
				pivot_found = false;
				// Search for a possible entry for a row exchange.
				
				while(r2 < m)
				{
					if(!(rows[r2].get(c).eq(0)))
					{
						swap(rows,r, r2);
						r2++;
						pivot_found = true;
						break;
					}
					
					r2++;
				}
			}
			
			
			// If this entire column is filled with 0s,
			// try the next column.
			if(!pivot_found)
			{
				r--;
				c++;
				continue;
			}
			
			T pivot = rows[r].get(c);
			rank++; 
					
			// Create a vector used for eliminating the rest of the rows.
			Vector<T> temp = rows[r].div(pivot);
			
			// Normalizes pivots.
			rows[r] = temp;
			
			// Now eliminate all non 0 entries.
			while(r2 < m)
			{
				rows[r2] = rows[r2].sub(temp.mult(rows[r2].get(c)));				
				r2++;
			}
			
			c++;
		}// End of outer for loop.
		
		Matrix<T> output = new Matrix<T>(rows); 
		output.ge1_form = true;
		output.ge_form = true;
		output.rank = rank;
		
		return output;
	}
	
	// Gaussian elimination for LDU factorization.
	// Stores L info in L, and D info in D, while U is formed through gaussian elimination.
	private Matrix<T> ge2(Matrix<T> L, Matrix<T> D)
	{
		if(ge1_form)
		{
			return this;
		}
		
		Vector<T>[] rows = rows();
		
		// Stores the index of the last non eliminated columns.
		int c = 0;
		int rank = 0;
		
		// Form a pivot in every row.
		for(int r = 0; r < m && c < n; r++)
		{
			// The row that will be eliminated.
			int r2 = r + 1;
			
			boolean pivot_found = true;
			
			// Search for a pivot for this column.
			if(rows[r].get(c).eq(0))
			{
				pivot_found = false;
				// Search for a possible entry for a row exchange.
				
				while(r2 < m)
				{
					if(!(rows[r2].get(c).eq(0)))
					{
						// LDU factorization not possible if row exchanges are necessary.
						return null;
						/*
						swap(rows,r, r2);
						r2++;
						pivot_found = true;
						break;
						*/
					}
					
					r2++;
				}
			}
			
			
			// If this entire column is filled with 0s,
			// try the next column.
			if(!pivot_found)
			{
				r--;
				c++;
				continue;
			}
			
			T pivot = rows[r].get(c);
			
			// Store this pivot info in D.
			D.data[r][r] = pivot;
			rank++;
								
			// Create a vector used for eliminating the rest of the rows.
			Vector<T> temp = rows[r].div(pivot);
			
			// Normalizes pivots.
			rows[r] = temp;
			
			// Now eliminate all non 0 entries.
			while(r2 < m)
			{
				// Store elimination matrices data in L.
				L.data[r2][r] = rows[r2].get(c).div(pivot);
				
				rows[r2] = rows[r2].sub(temp.mult(rows[r2].get(c)));				
				r2++;
			}
			
			c++;
		}// End of outer for loop.
		
		Matrix<T> output = new Matrix<T>(rows); 
		output.ge1_form = true;
		output.ge_form = true;
		output.rank = rank;
		
		return output;
	}
	
	// -- Methods to go between matrices and vectors.
	
	public Vector<T>[] columns()
	{
		// Initialize an array of column vectors.
		Vector<T>[] output = ge_v.Array(n, getCol(0));
		
		for(int c = 0; c < n; c++)
		{
			T[] temp = ge_t.Array(m, this.data[0][0]);
		
			// Populate each of the num_columns column vectors.
			for(int r = 0; r < m; r++)
			{
				temp[r] = data[r][c];
			}
			
			output[c] = new Vector<T>(temp);
		}
		
		return output;
	}
	
	public Vector<T>[] rows()
	{
		// Initialize an array of row vectors.
		Vector<T>[] output = ge_v.Array(m, getCol(0));
		
		for(int r = 0; r < m; r++)
		{
			T[] temp = ge_t.Array(n, this.data[0][0]);
		
			// Populate each of the num_rows row vectors.
			for(int c = 0; c < n; c++)				
			{
				temp[c] = data[r][c];
			}
			
			output[r] = new Vector<T>(temp);
		}
		
		return output;
	}

	public Vector<T> getRow(int r)
	{
		T[] output = ge_t.Array(n, this.data[0][0]);
		
		for(int c = 0; c < n; c++)
		{
			output[c] = data[r][c];
		}
		
		return new Vector<T>(output);
	}
	
	public Vector<T> getCol(int c)
	{
		T[] output = ge_t.Array(m, this.data[0][0]);
		
		for(int r = 0; r < m; r++)
		{
			output[r] = data[r][c];
		}
		
		return new Vector<T>(output);
	}
	

	public Matrix<T> appendCol(Vector<T> ... new_c)
	{
		// Create a new array of columns.
		Vector<T>[] output = ge_v.Array(n + new_c.length, new_c[0]);
		
		// Copy the old data.
		for(int c = 0; c < n; c++)
		{
			output[c] = getCol(c);
		}
		
		// Append the new column vectors.
		for(int c = 0; c < new_c.length; c++)
		{
			output[n + c] = new_c[c];
		}
		
		return new Matrix<T>(true, output);
	}
	
	public Matrix<T> appendRow(Vector<T> ... new_r)
	{
		// Create a new array of rows.
		Vector<T>[] output = ge_v.Array(m + new_r.length, getCol(0));
		
		// Copy the old data.
		for(int r = 0; r < m; r++)
		{
			output[r] = getRow(r);
		}
		
		// Append the new row vectors.
		for(int r = 0; r < new_r.length; r++)
		{
			output[n + r] = new_r[r];
		}
		
		return new Matrix<T>(output);
	}
	
	// Row, then column element retrieval.
	public T get(int r, int c)
	{
		return data[r][c];
	}
	
	
	// -- Helper methods.
	
	// Returns an identity matrix of the given size.
	public Matrix<T> identity(int size)
	{
		T[][] data = ge_t.Array_2d(size, size, this.data[0][0]);
		
		// fill in the diagonals with 1's.
		
		// Now that we are using generic numbers,
		// we need to zero out the array first.
		for(int r = 0; r < size; r++)
		for(int c = 0; c < size; c++)
		{
			data[r][c] = this.data[0][0].zero();
		}
		
		for(int i = 0; i < size; i++)
		{
			data[i][i] = this.data[0][0].one();
		}
		
		Matrix<T> output = new Matrix<T>(data);
		
		output.ge_form = true;
		output.ge1_form = true;
		output.echelon_form = true;
		
		return output; 
	}
	
	public boolean eq(Matrix<T> other)
	{
		if(other.m != m || other.n != n)
		{
			return false;
		}
		
		// Matrices are equal if their data is equivalent.
		for(int c = 0; c < n; c++)
		for(int r = 0; r < m; r++)
		{
			if(!(data[r][c].eq(other.data[r][c])))
			{
				return false;
			}
		}
		
		return true;
		
	}
	
	// A standard function to swap elements inside of an array.
	public static void swap(Object[] data, int index_1, int index_2)
	{
		Object temp	  = data[index_1];
		data[index_1] = data[index_2];
		data[index_2] = temp;
	}
	
	public String toString()
	{
		String output = "Matrix [Rows: " + m + ", Columns: " + n + "]\n";
		Vector<T>[] rows = rows();
		for(Vector<T> r : rows)
		{
			output = output + r + '\n';
		}
		return output;
	}
	
	public int getRowNum()
	{
		return m;
	}
	
	public int getColNum()
	{
		return n;
	}
	
	public boolean inRowEchelonForm()
	{
		// The minimum column of the leading term.
		int c_min = 0;

		// Iterate through every row.
		for(int r = 0; r < m; r++)
		{
			for(int c = 0; c < c_min; c++)
			{
				// Check for non zeros before the leading term.
				if(!get(r, c).eq(0))
				{
					return false;
				}
				
			}// End of c loop.

			// Check for extra leading zeros.
			do
			{
				if(c_min < n)
				{
					c_min++;
				}
				else
				{
					break;
				}
			}while(get(r, c_min - 1).eq(0));

			
			
			//ASSERT( (r, c_min) is either the last entry in row r, or it is non zero.
			
		}// end of every row loop.
		return true;
	}
	
	
	// -- Returns true if and only iff a matrix has a non zero row.
	// REQUIRES : The matrix must have a least 1 column and must have consistent rows.
	public boolean hasZeroRow()
	{
		Vector<T>[] rows = rows();
		
		T zero = rows[0].get(0).zero();
		
		for(Vector<T> row : rows)
		{
			if(row.dot(row).eq(zero))
			{
				return true;
			}
		}
		
		return false;
	}
	
	// Returns the proper minor for the given row and column.
	// This should probably be called with square matrices, but this function does the best it can with what it is given.
	public Pair<Matrix<T>, T> cofactor(int minor_r, int minor_c)
	{
		if(m < 2 || n < 2)
		{
			throw new Error("Cannot cofactor a matrice with only 1 row or column");
		}
		
		boolean sign = (minor_r + minor_c) % 2 == 0;
		
		T scalar;
		
		if(sign)
		{
			scalar = get(0, 0).one();
		}
		else
		{
			scalar = get(0, 0).one().mult(-1);
		}
		
		T[][] output = ge_t.Array_2d(m - 1, n - 1, this.data[0][0]);
		
		int y = 0;
		
		for(int r = 0; r < m; r++)
		{
			if(r == minor_r)
			{
				continue;
			}
			
			int x = 0;
			for(int c = 0; c < n; c++)
			{
				if(c == minor_c)
				{
					continue;
				}
				
				//System.out.println("x " + x + " y " + y + " r " + r + " c " + c);
				output[y][x] = data[r][c];
				
				x++;
			}
			
			y++;
		}
		
		return new Pair<Matrix<T>, T>(new Matrix<T>(output), scalar);
	}
	
	@Override
	public void serializeTo(PrintStream stream)
	{
		// -- Next Serialize the Matrix.
		int rowNum = getRowNum();
		int colNum = getColNum();
		
		// Serialize the dimensions.
		stream.println(rowNum);
		stream.println(colNum);
		
		// Serialize all of the data.
		for(int r = 0; r < rowNum; r++)
		for(int c = 0; c < colNum; c++)
		{
			T exp = get(r, c);
			exp.serializeTo(stream);
		}
	}

	@Override
	public String getSerialName()
	{
		return "Matrix";
	}

	// Returns true if and only if this matrix is 1 by 1.
	public boolean isTrivial()
	{
		return m == 1 && n == 1;
	}
	
	// Predicate for deciding whether two matrices may be interpreted as vectors that have a well defined dot product.
	public boolean canVectorDotWith(Matrix<T> other)
	{
		int m = this.m;
		int n = other.n;
		return m != n && (m == 1 || n == 1) && m == other.m && this.n == n;
	}

}