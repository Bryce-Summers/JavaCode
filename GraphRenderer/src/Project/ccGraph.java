package Project;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import BryceImages.Engines.Image_vectorGeometry;
import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;
import BryceMath.DoubleMath.Vector;
import Data_Structures.Structures.List;

public abstract class ccGraph extends Image_vectorGeometry
{

	protected int size;
	protected List<Integer> edges;
	
	/*
	BufferedImage image;
	Graphics g;
	Graphics2D g2;
	*/
	
	// FIXME : Refactor both of these graphs together.
	
	// REQUIRES: w == h.
	public ccGraph(int w, int h, int size, List<Integer> edges)
	{
		super(w, h, true);

		this.size  = size;
		this.edges = edges;
		
		antiAliasing = 2;
		
		/*
		image = ImageFactory.blank(w, w);
		g = image.getGraphics();
		g2 = (Graphics2D) g;
		g2 = (Graphics2D)g;
		  g2.setRenderingHint(
			        RenderingHints.KEY_ANTIALIASING,
			        RenderingHints.VALUE_ANTIALIAS_ON);
		  g2.setRenderingHint(
			        RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    g2.setRenderingHint(
			        RenderingHints.KEY_FRACTIONALMETRICS,
			        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		*/
		
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
			
			/*
			g2.setStroke(new BasicStroke((int)(e_thickness - e_thickness/5)));
			g.setColor(Colors.Color_hsv(0, 75, 100));
			g.drawLine((int)v1.getX(), (int)v1.getY(), (int)v2.getX(), (int)v2.getY());
			*/

			
			set_color(Color.BLACK);
			i_rect(v1, v2, e_thickness);
			/*
			g2.setStroke(new BasicStroke((int)(e_thickness)));
			g.setColor(Color.BLACK);
			g.drawLine((int)v1.getX(), (int)v1.getY(), (int)v2.getX(), (int)v2.getY());
			*/
			
			
		}
		
	}
	
	public void drawVertices()
	{
		int V = size*size;
		
		double r = getWidth()*1.0/size/2*.6;
		double r2 = r - r/10;
		
		for(int i = 0; i < V; i++)
		{
			Vector v = getVertice(i);

						
			// cyan.
			set_color(Colors.Color_hsv(175, 75, 100));
			i_circle(v, r2);
			/*
			g.setColor(Colors.Color_hsv(175, 75, 100));
			g.fillOval((int)(v.getX() - r2), (int)(v.getY() - r2), (int)(r2*2), (int)(r2*2));
			*/
			
			set_color(Color.BLACK);
			i_circle(v, r);
			/*
			g.setColor(Color.BLACK);
			g.fillOval((int)(v.getX() - r), (int)(v.getY() - r), (int)(r*2), (int)(r*2));
			*/


		}
	}
	
	// Returns the location of the given vertice.
	public abstract Vector getVertice(int index);
}
