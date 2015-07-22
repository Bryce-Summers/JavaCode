package Project;

import java.awt.Color;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageProccess;
import Data_Structures.ADTs.Queue;
import Data_Structures.Structures.SingleLinkedList;

/*
 * Fern Jaws class.
 * 
 * Renders a picture using the Barnsley Fern.
 */

public class FernJaws implements ImageProccess
{
	
	final int ITERATIONS = 10000000;
	
	final double BACKGROUND_RATIO = 1.0;
	
	// Collect the data points.
	double [][] data;	// Column major ordered.
	
	// Closest Points calculations.
	int [][] closest_point_x;
	int [][] closest_point_y;
	int [][] current_distance_sqr;
	
	private enum Status{NOT_IN_QUEUE, IN_QUEUE};	
	Status [][] visited;// Used to ensure points only get enqueued once.
	
	
	
	double max_col[];// Maximum location in each column.
	double min_col[];// Minimum location in each column.
	double max_intensity = 0.0;// The global maximum intensity value.
	// The minnimum is 0.0.
	
	// -- Coordinates start at 0.0;
	double fractal_x = 0.0;
	double fractal_y = 0.0;
	
	// Boundary for the window view.
	double bounds = 50;//30;
	
	// Boundary length between fractal and body.
	double bound1 = 10;
	double bound2 = bound1 + 10;// Length of the border.
	double bound3 = bound2 + 10;// Length of the interpolation between body and outside.
	
	final double fractal_x_min = -2.1820;
	final double fractal_x_max =  2.6558;
	final double fractal_x_range = fractal_x_max - fractal_x_min;
	final double fractal_y_min =  0;
	final double fractal_y_max =  9.9983;
	final double fractal_y_range = fractal_y_max - fractal_y_min;
	
	// The bounds for the partitioning of a randomly chosen number into the 4 iteration cases.
	double outcome_1 = .01;
	double outcome_2 = outcome_1 + .85;
	double outcome_3 = outcome_2 + .07;
	// Outcome 4 otherwise.
	
	int IMAGE_W;
	int IMAGE_H;
	
	@Override
	public void proccess(BufferedImage image)
	{
		IMAGE_W = image.getWidth();
		IMAGE_H = image.getHeight();
		
		
		// Scale the length values.
		// Boundary for the window view.
		bounds = bounds/1920.0*IMAGE_W;		
		// Boundary length between fractal and body.
		bound1 = bound1/1920.0*IMAGE_W;
		bound2 = bound2/1920.0*IMAGE_W;
		bound3 = bound3/1920.0*IMAGE_W;
		
		System.out.println("Generating Data.");
		generateData();		
		System.out.println("Done.");
		
		System.out.println("Drawing Image.");
		drawImage(image);
		System.out.println("Done.");
	}
	
	void generateData()
	{
		data = new double[IMAGE_W][IMAGE_H];
		
		// Generate Primary Points.
		System.out.println("Generating Primary Points");
		for(int i = 0; i < ITERATIONS; i++)
		{
			iterate();
			addData();
		}
		
		System.out.println("Computing Statistics");
		computeStatistics();
		
		System.out.println("Computing Closest Points.");
		computeClosestPoints();
	}


	void iterate()
	{
		double x_new;
		double y_new;
		
		double outcome = Math.random();
		
		if(outcome < outcome_1)
		{
			x_new = 0.0;
			y_new = .16*fractal_y;
		}
		else if(outcome < outcome_2)
		{
			 x_new = 0.85*fractal_x + 0.04*fractal_y;
			 y_new = -0.04*fractal_x + 0.85*fractal_y + 1.6;
		}
		else if(outcome < outcome_3)
		{
			x_new = 0.2*fractal_x - 0.26*fractal_y;
			y_new = 0.23*fractal_x + 0.22*fractal_y + 1.6;
		}
		else
		{
			x_new = -0.15*fractal_x + 0.28*fractal_y;
			y_new =  0.26*fractal_x + 0.24*fractal_y + 0.44;
		}
		
		fractal_x = x_new;
		fractal_y = y_new;
		
		
	}
	
	
	// converts from fractal space coordinates to screen space coordinates, then updates the data values.
	void addData()
	{
		
		// Conversion between fractal coordinates and screen space coordinates.
		double x = IMAGE_W - bounds - (fractal_y - fractal_y_min)/fractal_y_range*(IMAGE_W-bounds*2);
		double y = bounds + (fractal_x - fractal_x_min)/fractal_x_range*(IMAGE_H - bounds*2);

		// Compute the fractions of what percentage the point is in the discrete integer square.
		int r = (int)y;
		int c = (int)x;
				
		double frac_x_c = x - c;
		double frac_y_c = y - r;
				
		double frac_x = 1.0 - frac_x_c;
		double frac_y = 1.0 - frac_y_c;
		
		frac_x = frac_x*frac_x;
		frac_y = frac_y*frac_y;
		
		frac_x_c = frac_x_c*frac_x_c;
		frac_y_c = frac_y_c*frac_y_c;
		
		data[c][r]     = data[c][r] + frac_x*frac_y;
		data[c][r+1]   = data[c][r+1] + frac_x*frac_y_c;
		data[c+1][r+1] = data[c+1][r+1] + frac_x_c*frac_y_c;
		data[c+1][r]   = data[c+1][r] + frac_x_c*frac_y;		  
		
	}

	// Compute the max and min value statistics.
	private void computeStatistics()
	{
		min_col = new double[IMAGE_W];
		max_col = new double[IMAGE_W];
		
		for(int c = 0; c < IMAGE_W; c++)
		{
			min_col[c] = IMAGE_H;
			max_col[c] = 0;
			for(int r = 0; r < IMAGE_H; r++)
			{
				double val = data[c][r];
				
				if(val > 0)
				{
					min_col[c] = Math.min(min_col[c], r);
					max_col[c] = Math.max(max_col[c], r);
					max_intensity = Math.max(max_intensity, val);
				}
			}
		}

	}
	
	
	// Somewhat linear algorithm for computing all closest points.
	private void computeClosestPoints()
	{
		
		closest_point_x = new int[IMAGE_W][IMAGE_H];
		closest_point_y = new int[IMAGE_W][IMAGE_H];
		current_distance_sqr = new int[IMAGE_W][IMAGE_H];
		visited = new Status[IMAGE_W][IMAGE_H];
		
		Queue<Integer> Q = new SingleLinkedList<Integer>();
		
		// Initialize the frontier with the starting points.
		for(int x = 0; x < IMAGE_W; x++)
		for(int y = 0; y < IMAGE_H; y++)
		{
						
			// Point in Fractal.
			if(data[x][y] > 0.0)
			{
				enq_point(x, y, Q);
				current_distance_sqr[x][y] = 0;
				closest_point_x[x][y] = x;
				closest_point_y[x][y] = y;
			}
			else // Point outside of fractal.
			{
				current_distance_sqr[x][y] = Integer.MAX_VALUE;
				closest_point_x[x][y] = -1; // No closest point yet.
				closest_point_y[x][y] = -1;
				visited[x][y] = Status.NOT_IN_QUEUE;
			}		
		}
		
		// Gradually expand the allocation of closest points.
		while(!Q.isEmpty())
		{

			// Work is done while expanding.
			// Info is only for expansion purposes.
			int x = Q.deq();
			int y = Q.deq();
			int point_x = closest_point_x[x][y];
			int point_y = closest_point_y[x][y];			

			visited[x][y] = Status.NOT_IN_QUEUE;
			
			
			// 8 way expansion.
			for(int x_new = x - 1; x_new <= x + 1; x_new++)
			for(int y_new = y - 1; y_new <= y + 1; y_new++)
			{
				
				
				// Ignore points that are out of bounds.
				if(		x_new < 0 ||
						x_new >= IMAGE_W ||
						y_new < 0 ||
						y_new >= IMAGE_H ||
						(x_new == x && y_new == y)
				   )
				{
					continue;
				}
				
				int dist = dist_sqr(x_new, y_new, point_x, point_y);
				
				if(dist < current_distance_sqr[x_new][y_new])
				{
					current_distance_sqr[x_new][y_new] = dist;
					closest_point_x[x_new][y_new] = point_x;
					closest_point_y[x_new][y_new] = point_y;
					
					if(visited[x][y] == Status.NOT_IN_QUEUE)
					{
						enq_point(x_new, y_new, Q);
					}
				}
				
			}
			
		}
		
	}
	
	private void enq_point(int x, int y, Queue<Integer> Q)
	{
		Q.enq(x);
		Q.enq(y);
		visited[x][y] = Status.IN_QUEUE;
	}
	
	private int dist_sqr(int x1, int y1, int x2, int y2)
	{
		int dx = x1 - x2;
		int dy = y1 - y2;
		
		return dx*dx + dy*dy;
	}
	
	
	private void drawImage(BufferedImage image)
	{
		for(int r = 0; r < IMAGE_H; r++)
		for(int c = 0; c < IMAGE_W; c++)
		{
			
			double ratio = data[c][r]/max_intensity;
			
			// Not in fractal.
			if(ratio <= 0)
			{
				double point_x = closest_point_x[c][r];
				double point_y = closest_point_y[c][r];
				
				double dx = point_x - c;
				double dy = point_y - r;
				
				double dist = Math.sqrt(current_distance_sqr[c][r]);
				

				if(dist > bound1 && dist <= bound2)
				{
					ratio = 1.0; // White.
				}
				else if(dist > bound2 && dist <= bound3)
				{
					dist = dist - bound2;
					ratio = 1.0 - Math.sin(dist/bound1*Math.PI/2);
					
				}
				else if(dist > bound3)
				{
					ratio = BACKGROUND_RATIO;// Black;
				}
				else // Boundary with fractal.
				{					
					//ratio = Math.sin(dist/bounds*Math.PI/2);
					ratio = dist / bound1;
					ratio = Math.pow(ratio, .3);

				}				
				
			}
			else // In Fractal.
			{
				// Improve resolution of lower intensity parts.
				ratio = Math.pow(ratio, .20);
				ratio = BACKGROUND_RATIO;
			}
			
			double min_c = min_col[c];
			double max_c = max_col[c];

			double per_x = c*1.0/IMAGE_W;
			double per_y = r*1.0/IMAGE_H;
			
			double dist_bound = Math.min(Math.abs(r - min_c), Math.abs(r - max_c));
			double interior_bound = Math.abs(r - (min_c + max_c)/2);
			// Make the Jaws more prominant.
			if(r > min_col[c] && r < max_col[c] && (dist_bound > Math.sqrt(current_distance_sqr[c][r])*30))
			{
				ratio = BACKGROUND_RATIO;
			}
			
			// get away the junk in the middle.
			if(per_x > .3 && interior_bound < 170/1920.0*IMAGE_W)
			{
				ratio = BACKGROUND_RATIO;
			}
			
			if(per_x <= .3 && per_x > .2 && interior_bound < 90/1920.0*IMAGE_W)
			{
				ratio = BACKGROUND_RATIO;
			}
			
			if(per_x <= .2 && per_x > .1 && interior_bound < 45/1920.0*IMAGE_W)
			{
				ratio = BACKGROUND_RATIO;
			}
			

			
			
			
			// Cut off the entire right quadrant.
			if(c > IMAGE_W*3/4)
			{
				ratio = BACKGROUND_RATIO;
			}
			

			{
				double x1 = .6935;
				double x2 = .5389;
				double y1 = .8055;
				double y2 = .9722;
				// Cut off the lower right wedge.
				// A carefully chosen linear equation.
				if(per_y > ((y1 - y2)/(x1 - x2))*(per_x-x2) + y2)
				{
					ratio = BACKGROUND_RATIO;
				}
			}

			ratio = 1.0 - ratio;
			
			
			ratio = Math.max(0,  Math.min(ratio, 1.0));
			
			// Scale the intensities to a scale from black to white.
			int val = (int)(255 * ratio);
						
			image.setRGB(c, r, new Color(val, val, val).getRGB());
			
		}
	}
	
}
