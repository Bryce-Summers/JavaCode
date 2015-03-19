package BryceMath.DoubleMath;



/*
 * Matrix Class. Deals with most Linear algebra needs.
 * Written by Bryce Summers, 11/14/2012.
 * Extensive work done on 12 - 28 - 2012.
 */

public class Matrix
{
	// The data stored in the matrix.
	private double [][] data;
	
	// The matrix's dimensions.
	private final int m;
	private final int n;
	
	// Meta data.
	
	// The matrix has been eliminated, but with non-normalized pivots.
	private boolean ge_form = false;
	private boolean ge1_form = false;
	private boolean echelon_form = false;
	private int 	rank = -1;

	// -- Constructors
	public Matrix(Vector ... rows)
	{
		m = rows.length;
		n = rows[0].length;
				
		data = new double[m][n];
		
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
	public Matrix(boolean want_cols, Vector ... cols)
	{

		n = cols.length;
		m = cols[0].length;
						
		data = new double[m][n];
		
		for(int c = 0; c < n; c++)
		for(int r = 0; r < m; r++)
		{
			data[r][c] = cols[c].get(r);
		}
		
	}
	
	public Matrix(double[][] data)
	{
		this.data = data;
		m = data.length;
		n = data[0].length;
	}
	
	// -- Fundamental Matrices operations.
	
	public Matrix transpose()
	{
		// Interpret this matrix's columns as rows.
		return new Matrix(columns());
	}
	
	// Product of the eigen vectors.
	public double det()
	{
		if(m != n)
		{
			throw new Error("ERROR: Determinant only defined for square matrices.");
		}
		
		Matrix reduced = ge();
		double result = 1;
		
		for(int c = 0; c < n; c++)
		{
			result = result*reduced.get(c, c);
		}
		
		return result;
	}
	
	// Sum of the diagonal elements / Sum of the eigen vectors.
	public double trace()
	{
		if(m != n)
		{
			throw new Error("ERROR: Trace only defined for square matrices.");
		}
		
		double result = 0;
		
		for(int c = 0; c < n; c++)
		{
			result = result+get(c, c);
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
	
	public Matrix inverse()
	{
		if(m != n)
		{
			throw new Error("ERROR: Non-square matrices have no inverse.");
		}
		
		// If this Matrix is singular, it has no inverse.
		if (det() == 0)
		{
			return null;
		}
		
		// Perform Gaus-Jordan elimination.
		
		// Augment A with the proper identity matrix.
		Matrix A = appendCol(identity(m).columns());
		
		// Reduce A to Echelon form.
		A = A.echelon();
		
		// Return the second half of the augmented matrix.
		Vector[] output = new Vector[m];
		
		for(int c = 0; c < n; c++)
		{
			output[c] = A.getCol(n + c);
		}
		
		// Convert columns to a matrix.
		return new Matrix(true, output);
		
	}
	
	// -- Computations.
	
	// Ax = b;
	// FIXME: We need to deal with unsolvable systems and systems with infinite solutions.
	public Vector solve(Vector b)
	{
		// Create an augmented matrix.
		Matrix A = appendCol(b);
		
		// Gaussian eliminate A.
		A = A.echelon();
				
		// Return the eliminated solution vector.
		return A.getCol(n);
	}
	
	// Returns the LDU factorization for this Matrix if it exists.
	// Returns null otherwise.
	// FIXME: Decide what form of output is best.
	public Matrix[] LDU()
	{
		Matrix L = identity(m);
		Matrix D = identity(m);
		
		Matrix U = ge2(L, D); 
		
		if(U == null)
		{
			throw new Error("ERROR: LDU factorization does not exist.");
		}
		
		// Form output.
		Matrix[] output = new Matrix[3];
		output[0] = L;
		output[1] = D;
		output[2] = U;
		
		return output;
	}
	
	
	// Returns the QR factorization for this matrix, if it exists.
	public Matrix[] QR()
	{
		throw new Error("Not yet Implemented.");
	}
	
	public Matrix mult(Matrix B)
	{
		
		Vector[] rows = rows();
		Vector[] cols = B.columns();
		
		double[][] data = new double[m][n];
		
		// Perform matrix multiplication via vector arithmetic.
		for(int r = 0; r < m; r++)
		for(int c = 0; c < n; c++)
		{
			data[r][c] = rows[r].dot(cols[c]);
		}
		
		return new Matrix(data);
	}
	
	// Returns the reduced echelon form of this matrix.
	public Matrix echelon()
	{
		if(echelon_form)
		{
			return this;
		}
		
		// First gaussian eliminate, normalizing the pivots.
		Matrix A = ge1();
		
		Vector[] rows = A.rows();
		
		for(int r = m - 1; r > 0; r--)
		{
			// First find the r and c coordinates of the pivot in this row.
			int c = 0;
			boolean found = true;
			
			while(rows[r].get(c) == 0)
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
		Matrix output = new Matrix(rows);
		output.echelon_form = true;
		output.ge1_form = true;
		output.ge_form  = true;
		rank = A.rank;
		
		return output;
	}
	
	// Returns the gaussian eliminated version of this matrix;
	// Pivots are not normilized.
	// Resulting matrix also knows its rank.
	public Matrix ge()
	{
		if(ge_form)
		{
			return this;
		}
		
		Vector[] rows = rows();
		
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
			if(rows[r].get(c) == 0)
			{
				pivot_found = false;
				// Search for a possible entry for a row exchange.
				
				while(r2 < m)
				{
					if(rows[r2].get(c) != 0)
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
			
			double pivot = rows[r].get(c);
			rank++; 
					
			// Create a vector used for eliminating the rest of the rows.
			Vector temp = rows[r].div(pivot);
			
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
		
		Matrix output = new Matrix(rows); 
		output.ge_form = true;
		output.rank = rank;
		
		return output;
	}

	// Returns the gaussian eliminated version of this matrix;
	// Pivots are indeed normalized.
	// Resulting matrix also knows its rank.
	public Matrix ge1()
	{
		if(ge1_form)
		{
			return this;
		}
		
		Vector[] rows = rows();
		
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
			if(rows[r].get(c) == 0)
			{
				pivot_found = false;
				// Search for a possible entry for a row exchange.
				
				while(r2 < m)
				{
					if(rows[r2].get(c) != 0)
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
			
			double pivot = rows[r].get(c);
			rank++; 
					
			// Create a vector used for eliminating the rest of the rows.
			Vector temp = rows[r].div(pivot);
			
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
		
		Matrix output = new Matrix(rows); 
		output.ge1_form = true;
		output.ge_form = true;
		output.rank = rank;
		
		return output;
	}
	
	// Gaussian elimination for LDU factorization.
	// Stores L info in L, and D info in D, while U is formed through gaussian elimination.
	private Matrix ge2(Matrix L, Matrix D)
	{
		if(ge1_form)
		{
			return this;
		}
		
		Vector[] rows = rows();
		
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
			if(rows[r].get(c) == 0)
			{
				pivot_found = false;
				// Search for a possible entry for a row exchange.
				
				while(r2 < m)
				{
					if(rows[r2].get(c) != 0)
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
			
			double pivot = rows[r].get(c);
			
			// Store this pivot info in D.
			D.data[r][r] = pivot;
			rank++;
								
			// Create a vector used for eliminating the rest of the rows.
			Vector temp = rows[r].div(pivot);
			
			// Normalizes pivots.
			rows[r] = temp;
			
			// Now eliminate all non 0 entries.
			while(r2 < m)
			{
				// Store elimination matrices data in L.
				L.data[r2][r] = rows[r2].get(c)/pivot;
				
				rows[r2] = rows[r2].sub(temp.mult(rows[r2].get(c)));				
				r2++;
			}
			
			c++;
		}// End of outer for loop.
		
		Matrix output = new Matrix(rows); 
		output.ge1_form = true;
		output.ge_form = true;
		output.rank = rank;
		
		return output;
	}
	
	// -- Methods to go between matrices and vectors.
	
	public Vector[] columns()
	{
		// Initialize an array of column vectors.
		Vector[] output = new Vector[n];
		
		for(int c = 0; c < n; c++)
		{
			double[] temp = new double[m];
		
			// Populate each of the num_columns column vectors.
			for(int r = 0; r < m; r++)
			{
				temp[r] = data[r][c];
			}
			
			output[c] = new Vector(temp);
		}
		
		return output;
	}
	
	
	public Vector[] rows()
	{
		// Initialize an array of row vectors.
		Vector[] output = new Vector[m];
		
		for(int r = 0; r < m; r++)
		{
			double[] temp = new double[n];
		
			// Populate each of the num_rows row vectors.
			for(int c = 0; c < n; c++)				
			{
				temp[c] = data[r][c];
			}
			
			output[r] = new Vector(temp);
		}
		
		return output;
	}
	
	public Vector getRow(int r)
	{
		double[] output = new double[n];
		
		for(int c = 0; c < n; c++)
		{
			output[c] = data[r][c];
		}
		
		return new Vector(output);
	}
	
	public Vector getCol(int c)
	{
		double[] output = new double[m];
		
		for(int r = 0; r < m; r++)
		{
			output[r] = data[r][c];
		}
		
		return new Vector(output);
	}
	
	public Matrix appendCol(Vector ... new_c)
	{
		// Create a new array of columns.
		Vector[] output = new Vector[n + new_c.length];
		
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
		
		return new Matrix(true, output);
	}
	
	public Matrix appendRow(Vector ... new_r)
	{
		// Create a new array of rows.
		Vector[] output = (Vector[])new Object[m + new_r.length];
		
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
		
		return new Matrix(output);
	}
	
	public double get(int r, int c)
	{
		return data[r][c];
	}
	
	
	// -- Helper methods.
	
	// Returns an identity matrix of the given size.
	public Matrix identity(int size)
	{
		double[][] data = new double[size][size];
		
		// fill in the diagonals with 1's.
		
		for(int i = 0; i < size; i++)
		{
			data[i][i] = 1;
		}
		
		Matrix output = new Matrix(data);
		
		output.ge_form = true;
		output.ge1_form = true;
		output.echelon_form = true;
		
		return output; 
	}
	
	public boolean eq(Matrix other)
	{
		boolean output = true;
		
		// Matrices are equal if their data is equivalent.
		for(int c = 0; c < n; c++)
		for(int r = 0; r < m; r++)
		{
			if(data[r][c] != other.data[r][c])
			{
				output = false;
			}
		}
		
		return output;
		
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
		String output = "Matrix [Rows: " + m + ", Columns: " + n + "]\num_columns";
		Vector[] rows = rows();
		for(Vector r : rows)
		{
			output = output + r + '\n';
		}
		return output;
	}
	
}