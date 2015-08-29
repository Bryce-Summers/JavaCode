package project.client.Graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import project.client.data_structures.Vector;

/*
 * Bryce's Personal Drawing library built on top of Context2d;
 * 
 * FIXME : Make things in terms of points.
 * 
 * 
 * FIXME : Make this more performant.
 * 
 * 	We can draw only to integer coordinates and it may improve the performance.
 */

public class Drawing
{

	// The Drawing Context.
	public Graphics g;
	
	public Drawing(Graphics context)
	{
		g = context;
	}
	
	public void color(Color c)
	{
		g.setColor(c);
	}
	
	public void linesize(float size)
	{
        BasicStroke stroke =
        		new BasicStroke(size,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER);
        
        Graphics2D g2 = (Graphics2D)g;
        
		g2.setStroke(stroke);
		
	}
	
	// Draws a Circle. // FIXME : Do not draw the fill. use stroke instead.
	
	public void circle(double x, double y, double radius)
	{
		circle(x, y, radius, true);
	}
	
	public void circle(double x, double y, double radius, boolean fill)
	{
		if(fill)
		{
			g.fillOval((int)(x - radius), (int)(y - radius),(int)(radius*2),(int)(radius*2));
		}
		else
		{
			// Storke only.
			g.drawOval((int)(x - radius), (int)(y - radius),(int)(radius*2),(int)(radius*2));
		}
	}
	
	public void fontSize(int size)
	{
		g.setFont(new Font("TimesRoman", Font.PLAIN, size));
	}
	
	public void centered_text(String str, double x, double y, double max_width)
	{	
		Graphics2D g2d = (Graphics2D)g;
		Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(str, g2d);
	    int stringLen = (int)bounds.getWidth();
	    int stringH = (int)bounds.getHeight();
	    int start = (int)(x - stringLen/2);
	    g2d.drawString(str, (int)(start), (int)y);
	}  
 
	
	public void left_text(String str, double x, double y, double max_width)
	{
		
		Graphics2D g2d = (Graphics2D)g;
		Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(str, g2d);
	    //int stringLen = (int)bounds.getWidth();
	    int stringH = (int)bounds.getHeight();
	    
		g.drawString(str, (int)x, (int)y);
	}
	
	// Draws a Bezier curve with the given points and tangent vectors.
	public void bezier(
			double x1,  double y1, 
			double tx1, double ty1, 
			double x2,  double y2,
			double tx2, double ty2)
	{
		
		Graphics2D g2d = (Graphics2D) g;
	    GeneralPath path = new GeneralPath();
	    path.moveTo(x1, y2);
	    path.curveTo(x1 + tx1, y1 + ty1, x2 + tx2, y2 + ty2, x2, y2);
	    g2d.draw(path);

	}
	
	public void line(double x1, double y1, double x2, double y2)
	{	
	    Graphics2D g2d = (Graphics2D) g;
	    GeneralPath path = new GeneralPath();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		g2d.draw(path);
	}

	// FIXME : Have this be the internal function.
	public void arrow(Vector v1, Vector v2)
	{
		arrow(v1.getX(), v1.getY(), v2.getX(), v2.getY());
	}
	
	public void arrow(double x1, double y1, double x2, double y2)
	{		
		
		// A Vector pointing back to the original node.
		Vector diff = new Vector((x1 - x2), y1 - y2);
		
		diff = diff.norm();
		
		int arrow_size = 15;
		
		Vector back = diff.mult(arrow_size);
		
		Vector left  = diff.rotate2D(90).mult(arrow_size);
		Vector right = left.mult(-1);
		
		Vector p1 = new Vector(x2, y2);
		Vector line_end = p1.add(back);
		
		Vector p2 = line_end.add(left);
		Vector p3 = line_end.add(right);
				
		
		line(x1, y1, line_end.getX(), line_end.getY());
		
		
	    Graphics2D g2d = (Graphics2D) g;
	    GeneralPath path = new GeneralPath();
		path.moveTo(x2, y2);
		path.lineTo(p2.getX(), p2.getY());
		path.lineTo(p3.getX(), p3.getY());
		g2d.fill(path);
		
	}
	
}
