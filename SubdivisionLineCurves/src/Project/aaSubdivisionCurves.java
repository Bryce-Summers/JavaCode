package Project;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JFrame;

import util.ImageUtil;
import BryceImages.ColorCalculators.ccHeart;
import BryceImages.GUI.Display;
import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.ColorCalculator;
import BryceImages.Rendering.StartRender;
import BryceMath.Calculations.Colors;
import Data_Structures.Structures.IterB;
import Data_Structures.Structures.List;
import Data_Structures.Structures.Pair;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.gui_button;


/* 
 * Basic Main Class, Written and maintained by Bryce Summers
 * Updated: 12 - 21 - 2012:
 * 		- This file has been cleaned up to streamline my code base.
 * 		- This file was modified to lend itself to the new Render
*/

public class aaSubdivisionCurves
{
	
	public static void main(String[] args)
	{
		// Render an image in a JPanel using my rendering code.
		aaSubdivisionCurves program = new aaSubdivisionCurves();
		program.startNormal();
	}
	
	public void startNormal()
	{
		// -- Local Variables.

		BufferedImage image = ImageFactory.blank(800, 800);
		
		List<Pair<Double, Double>> points = new List<Pair<Double, Double>>();
		
		points.add(PAIR(.5, .5));
		points.add(PAIR(.8, .5));
		points.add(PAIR(.5, .2));
		points.add(PAIR(.2, .5));
		points.add(PAIR(.3, .7));
		points.add(PAIR(.8, .6));
		points.add(PAIR(.7, .8));
		points.add(PAIR(.5, .8));
		
		
		int SUBDIVISIONS = 4;
		
		for(int i = 0; i < SUBDIVISIONS; i++)
		{
			System.out.println("Subdividing, Pass = " + i);
			subdivide(points);
		}
		
		System.out.println("Drawing");
		drawCurve(image, points);
		
		System.out.println("Saving");
		ImageUtil.saveImage(image, "BrendSave");
		
		System.out.println("Please Have a nice day.");
	}
	
	// Subdivides a given line.
	public void subdivide(List<Pair<Double, Double>> curve)
	{
		// Extract a List Iterator.
		IterB<Pair<Double, Double>> iter = curve.getIter();
		
		int size = curve.size();
		
		// No plausible subdivision possible for trivial lines.
		if(size <= 1)
		{
			return;
		}
		
		Pair<Double, Double> A = iter.next();
		Pair<Double, Double> C = iter.next();
		
		// Handle curves of length 2 with a linear interpolation.
		if(size == 2)
		{
			// Note:
			iter.insertBefore(midpoint(A, C));
			return;
		}
		
		// The first subdivision will be a partial one using 3 points.
		Pair<Double, Double> B = A;
		Pair<Double, Double> D;
		
		do
		{
			// Retrieve the D value.
			D = iter.next();
			
			// Go back to C.
			iter.previous();
			
			// Insert the subdivided point before C.
			iter.insertBefore(subdivide(A, B, C, D));
			
			// Go back to C.
			iter.next();
			
			// Go back to D.
			iter.next();
			
			// Move the point references forward.
			A = B;
			B = C;
			C = D;
			
		}while(iter.hasNext());
		
		// Insert the last partial subdivision.
		iter.insertBefore(subdivide(A, B, C, D));
		
	}
	
	// -- Helper functions.
	
	// Creates a new Pair.
	private Pair<Double, Double> PAIR(double x, double y)
	{
		return new Pair<Double, Double>(x, y);		
	}
	
	
	private Pair<Double, Double> midpoint(Pair<Double, Double> p1, Pair<Double, Double> p2)
	{
		double x = (p1.getKey() + p2.getKey())/2.0;
		double y = (p1.getVal() + p2.getVal())/2.0;
		
		return new Pair<Double, Double>(x, y);
	}
	
	private Pair<Double, Double> subdivide( Pair<Double, Double> p1, Pair<Double, Double> p2,
											Pair<Double, Double> p3, Pair<Double, Double> p4)
	{
		double x = subdivide(p1.getKey(), p2.getKey(), p3.getKey(), p4.getKey());
		double y = subdivide(p1.getVal(), p2.getVal(), p3.getVal(), p4.getVal());
		
		return new Pair<Double, Double>(x, y);
	}
	
	private double subdivide(double d1, double d2, double d3, double d4)
	{
		return (d2 + d3)*9.0/16.0 - (d1 + d4)/16.0;
	}
	
	private void drawCurve(BufferedImage image,
			List<Pair<Double, Double>> points)
	{
		Graphics g = image.getGraphics();
		
		g = image.getGraphics();
		Graphics2D g2 = (Graphics2D)g;
		
		// Turn on all antialiasing.
		ImageUtil.enableAllAntialiasing(g2);
		
		
		Iterator<Pair<Double, Double>> iter = points.iterator();
		Pair<Double, Double> p1 = iter.next();
		Pair<Double, Double> p2;
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		g.setColor(Color.BLACK);
		
		// Increase the width of the lines.
		BasicStroke stroke =
		        new BasicStroke(20.0f,
		                        BasicStroke.CAP_BUTT,
		                        BasicStroke.JOIN_ROUND,
		                        20.0f);
				
		g2.setStroke(stroke);
		
		
		/*// Line Drawing code.
		while(iter.hasNext())
		{
			p2 = iter.next();
			g.drawLine((int)(p1.getKey()*w),
					   (int)(p1.getVal()*h),
					   (int)(p2.getKey()*w),
					   (int)(p2.getVal()*h));
			// Increment p1;
			p1 = p2;
		}
		*/
		
		int size = points.size();
		
		int[] xPoints = new int[size];
		int[] yPoints = new int[size];
		
		int index = 0;
		for(Pair<Double, Double> p : points)
		{
			xPoints[index] = (int)((double)p.getKey()*w);
			yPoints[index] = (int)((double)p.getVal()*h);
			
			index++;
		}
		
		g.drawPolyline(xPoints, yPoints, size);
		
		g.dispose();
	}
	
	
}

