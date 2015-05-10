package Project;

import java.awt.Color;
import java.util.Iterator;

import BryceImages.Engines.Image_vectorGeometry;
import BryceMath.Calculations.Colors;
import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.List;

public abstract class ccGraph extends Image_vectorGeometry
{

	protected int size;
	protected List<Integer> edges;
	
	// FIXME : Refactor both of these graphs together.
	
	// REQUIRES: w == h.
	public ccGraph(int w, int h, int size, List<Integer> edges)
	{
		super(w, h, true);

		this.size  = size;
		this.edges = edges;
		
		antiAliasing = 4;
		
		i_geoms();
	}

	@Override
	public void i_geoms()
	{
		drawVertices();
		drawEdges();
	}
	
	public void drawEdges()
	{		
		set_thickness(getWidth()/200);
		
		Iterator<Integer> iter = edges.iterator();
		
		while(iter.hasNext())
		{
			Integer v1_index = iter.next();
			Integer v2_index = iter.next();
			
			double r = getWidth()*1.0/size/2*.6;
			
			double e_thickness = r/5;
			
			Vector v1 = getVertice(v1_index);
			Vector v2 = getVertice(v2_index);
			
			// reddish.
			set_color(Colors.Color_hsv(0, 75, 100));
			i_rect(v1, v2, e_thickness - e_thickness/5);
			
			set_color(Color.BLACK);
			i_rect(v1, v2, e_thickness);
		}
		
	}
	
	public void drawVertices()
	{
		int V = size*size;
		
		double r = getWidth()*1.0/size/2*.6;
		
		
		for(int i = 0; i < V; i++)
		{
			Vector v = getVertice(i);

			// cyan.
			set_color(Colors.Color_hsv(175, 75, 100));
			i_circle(v, r - r/10);
						
			set_color(Color.BLACK);
			i_circle(v, r);
		}
	}
	
	// Returns the location of the given vertice.
	public abstract Vector getVertice(int index);
}
