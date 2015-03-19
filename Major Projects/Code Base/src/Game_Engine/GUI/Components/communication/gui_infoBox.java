package Game_Engine.GUI.Components.communication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.concurrent.CountDownLatch;

import BryceMath.Calculations.MathB;
import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.small.gui_paragraph;

public class gui_infoBox extends gui_closableContainer
{

	final String message;
	
	// These represent the location on the screen that this info box is talking about.
	Obj target;
	
	public gui_infoBox(double x, double y, int w, int h, String str, Obj target)
	{
		super(x, y, w, h);
		
		message = str;
		this.target = target;
	}

	public gui_infoBox(Rectangle screen, String str, Obj target)
	{
		super(screen);
		
		message = str;
		this.target = target;
	}

	@Override
	public void populateContent(int x, int y, int w, int h)
	{
		gui_paragraph P = new gui_paragraph(x, y, w, h);
		P.setText(message);
		P.setDrawBorders(false);
		obj_create(P);
	}
	
	// FIXME : This is a little bit risky.
	// I should have the normal draw method for containers called as well.
	@Override
	public void draw(ImageB image, AffineTransform AT, CountDownLatch calling_latch)
	{
		super.draw(image, AT, calling_latch);
		
		// Size of the pointer.
		int size = 32;
		
		// Bound info.
		double x = getX();
		double y = getY();
		double x2 = getX2();
		double y2 = getY2();
		
		// Compute the points of a triangle.
		double tx, ty;
		
		tx = target.getScreenX() + target.getW()/2;
		ty = target.getScreenY() + target.getH()/2;
		
		double tx2, ty2;
		double tx3, ty3;
				
		if(ty < y)
		{
			tx2 = MathB.bound(tx, x, x2 - size);
			ty2 = y;
			
			tx3 = tx2 + size;
			ty3 = ty2;
		}
		else if(ty > y2)
		{
			tx2 = MathB.bound(tx, x, x2 - size);
			ty2 = y2;
			
			tx3 = tx2 + size;
			ty3 = ty2;
		}
		else if(tx < x)
		{
			ty2 = MathB.bound(ty, y, y2 - size);
			tx2 = x;
			
			tx3 = tx2;
			ty3 = ty2 + size;		
		}
		else if(tx > x2)
		{
			ty2 = MathB.bound(ty, y, y2 - size);
			tx2 = x2;
			
			tx3 = tx2;
			ty3 = ty2 + size;			
		}
		else// Target is inside the info box.
		{
			return;
		}
		
		
		
		int[] xpoints = new int[3];
		xpoints[0] = (int)tx;
		xpoints[1] = (int)tx2;
		xpoints[2] = (int)tx3;
		
		int[] ypoints = new int[3];
		ypoints[0] = (int)ty;
		ypoints[1] = (int)ty2;
		ypoints[2] = (int)ty3;		

		Polygon T = new Polygon(xpoints, ypoints, 3);
				
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillPolygon(T);
		
	}
	
}
