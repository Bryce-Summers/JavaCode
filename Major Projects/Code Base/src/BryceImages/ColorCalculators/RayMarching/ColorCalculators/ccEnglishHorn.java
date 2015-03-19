package BryceImages.ColorCalculators.RayMarching.ColorCalculators;

import java.awt.Color;
import java.awt.Dimension;

import BryceImages.ColorCalculators.RayMarching.Geometry;
import BryceImages.ColorCalculators.RayMarching.PhongLighted3dPicture;
import BryceImages.ColorCalculators.RayMarching.Vector;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_ball;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_block;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_cylinder;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plane;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_plate;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_simpleCurve;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_spaceCurve;
import BryceImages.ColorCalculators.RayMarching.Geometries.geom_torus;
import BryceMath.Calculations.Colors;

public class ccEnglishHorn extends PhongLighted3dPicture {

	public ccEnglishHorn(Dimension tempDim)
	{
		super(tempDim);
	}

	@Override
	public Color getColor(Vector z, double light)
	{
		return null;
	}

	@Override
	public void loadGeoms()
	{
		lights = new Vector[3];
		lights[0] = new Vector(5,5,3,.7);
		lights[1] = new Vector(15,30,5,.7);
		lights[2] = new Vector(0,20,30,.5);

		// Creation view.
		int yyy = 40;
		CameraZ 		= new Vector(yyy, 0, 55);
		CameraFocus 	= new Vector(0, yyy + 5,  0);

		// Side view.
		CameraZ 		= new Vector(5, 23, 50);
		CameraFocus 	= new Vector(0, 28,  0);
		
		antiAliasing 	= 3;
		reflectionDepth	= 3;
		
		CameraDepth = .85; // full body :  .98;
		
		Color C_BODY  = Color_hsv(0, 0, 50);// Body
		Color C_KEYS  = Color_hsv(0, 0, 75);// Keys
		Color C_EMILY = Color_hsv(0, 90,75);// English Horn / Emily Harmon Cylinders.
		
		// Define English Horn Geometry.
		Geometry g = new geom_cylinder(new Vector(0,0,0),new Vector(0,60,0) ,1);
		g.setColor(C_BODY);
		geoms.add(g);
		
		g = new body();
		g.setColor(C_BODY);
		geoms.add(g);

		// -- Create the keyworking geometry
		
		// This is the first cylinder that connects the starting key work to the first band
		g = new geom_cylinder(new Vector(0,7.2,1.1),new Vector(0,9,1.1) ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
	
		g = new geom_torus(new Vector(0,7,1),.1 ,.15, new Vector(0,1,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(new Vector(.25,7.2,.5), .5,.1, new Vector(0,1,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(new Vector(.65,7.2,.65), .3,.1, new Vector(1,0,1));
		g.setColor(C_BODY);
		geoms.add(g);
		
		// the first band
		g = new geom_ball(new Vector(0,9,0),1.02);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(new Vector(0,9,1),.1 ,.15, new Vector(0,1,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// This is the second cylinder
		g = new geom_cylinder(new Vector(0,8.5,1.1), new Vector(.6,8.5,.8) ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(new Vector(0,8.5,1),.08 ,.15, new Vector(0,1,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		Vector p = new Vector(.6,10,.8);
		Vector c = p;
		// The connection over the first band.
		g = new geom_cylinder(new Vector(.6,8.5,.8), p ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		p = p.add(new Vector(0,-.8,0));
		
		Vector e		= new Vector(.1,.1,.1);
		Vector up		= new Vector(0,0,1);
		Vector down		= new Vector(0,0,-1);
		Vector left		= new Vector(-1,0,0);
		Vector right	= new Vector( 1,0,0);
		
		Vector forw		= new Vector(0, 1, 0);
		Vector back		= new Vector(0,-1, 0);
		
		g = new geom_block(p.sub(e), p.add(e),up,.01);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		p = c;
		
		c = new Vector(0,10,1.1);
		
		// move back to the center;
		g = new geom_cylinder(c, p ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		p = c;
		
		c = c.add(up.mult(.05));
		c.y = 26;
		
		// The first major dowel
		g = new geom_cylinder(c, p.add(back.mult(.2)) ,.125); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		c = new Vector(-.75,10.5,.75);
		p = p.add(forw.mult(.5));
		
		// Goes to second key
		g = new geom_cylinder(c, p ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Key 2
		g = new geom_plate(c.add(down.mult(.1)), .3,.1, new Vector(-.75,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// Another embelishment of a dowel.
		g = new geom_torus(new Vector(0,9.8,1),.1 ,.15, new Vector(0,1,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// Now go and connect over to the right 3rd key.
		p = new Vector(0,10.9,1);
		c = p.add(right.mult(.8)).add(down.mult(.25));
		
		g = new geom_cylinder(c, p ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(c.add(down.mult(.1)),.0525,.2,up.add(right.mult(.5)));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		p = c;
		c = c.add(forw.mult(4));
		g = new geom_cylinder(c, p ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(c.add(back.mult(.4)),.4,.08,up.add(right.mult(.3)));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The perpendicular dowen to the third key dowel.
		p = new Vector(.35,12.5,.95);
		c = new Vector(1.25,12.5,.7);
		g = new geom_cylinder(c, p ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
				
		// Add balls to each end of the cross dowel.
		g = new geom_ball(c,.2);
		g.setColor(C_KEYS);
		geoms.add(g);
		g = new geom_ball(p,.2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(new Vector(.15,15,.4), .7, .1,forw);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The second major dowel.
		p = new Vector(-.35,14.4,1.1);
		c = new Vector(-.35,25,1.1);
		g = new geom_cylinder(c, p ,.1); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The last orange bangle on the second major dowel.
		g = new geom_torus(c, .1,.15, forw);
		g.setColor(C_KEYS);
		geoms.add(g);
	
		g = new geom_torus(p, .1,.1, forw);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		
		double y = 19;
		// Goes to 4th key
		g = new geom_cylinder(v(0,y,1.1), v(-.5,y,.7) ,.15); 
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Key 2
		g = new geom_plate(v(-.75,y,.7), .3,.1, new Vector(-1,0,.5));
		g.setColor(C_KEYS);
		geoms.add(g);
		

		// The orange bangles.
		
		// Bangle 1
		g = new geom_torus(v(0,16.5,1.1), .1,.1, forw);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(v(0,17,1.1), .1,.1, forw);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Bangle at key location.
		g = new geom_torus(v(0,19,1.1), .1,.1, forw);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		g = new geom_torus(v(0,19.5,1.1), .1,.1, forw);
		g.setColor(C_KEYS);
		geoms.add(g);
		

		// The last orange bangle.
		g = new geom_torus(v(0,26,1.1), .1,.2, forw);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// Comes off of oarange bangle 1.
		// Connects to right rod.
		g = new geom_cylinder(v(0,16.5,1.1), v(1,16.5,.65),.1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The right rod.
		g = new geom_cylinder(v(1,16.1,.6), v(1,48,.6),.1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The end of the right rod.
		g = new geom_ball(v(1,48,.6),.15);
		g.setColor(C_KEYS);
		geoms.add(g);		
		
		// extension rod before the cross doweled key coming from main dowel.
		g = new geom_cylinder(v(1,19,.6), v(.5,19,.8),.1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// End of extension rod embelishment.
		g = new geom_torus(v(.5,19,.8),.1,.1,v(.6,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Bangle on right rod.
		g = new geom_torus(v(1,19,.6),.1,.1,v(0,1,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Bangle at begginning of right rod.
		g = new geom_ball(v(1,16.1,.6),.15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Decorative ball
		g = new geom_ball(v(.8,16.1,.6),.15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		
		// Second Cross dowel-------------
		
		//Extension to second cross dowel.
		g = new geom_cylinder( v(0,19.5,1.1), v(.5,19.5,1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// corner of second cross dowel.
		g = new geom_torus(v(.5,19.5,.8),.1,.15,v(.6,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// cross dowel y || part.
		g = new geom_cylinder( v(.5,19.5,.8), v(.5,23,.8), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		c = v(.25,21,.9);
		p = v(.75,21,.6);
		// cross dowel y perpendicular part.
		g = new geom_cylinder(c , p, .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Decorative cross dowel ball.
		g = new geom_ball(c,.2);
		g.setColor(C_KEYS);
		geoms.add(g);
		// Decorative cross dowel ball 2.
		g = new geom_ball(p,.2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Key for 2nd cross dowell.
		g = new geom_plate (v(.5,23,.8),.3,.08, v(.8,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// 
		// CROSS CONSTRUCTION OVER THE 2ND DOWEL KEY.
		//FIXME: this bend looks awkward.
		Vector temp = v(.1, 22.5, 1.3);
		g = new geom_cylinder (v(-.4,22.5,1), temp,.1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (temp, v(.5,22.5,.8),.1);
		g.setColor(C_KEYS);
		geoms.add(g);

		
		// The start of the thin key code.
		g = new geom_cylinder (v(0,24,1.1), v(.5, 24, .85),.1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (v(.5, 24, .8), v(.5, 26, .85), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The cross dowel at the end of this thin key rod
		g = new geom_cylinder (v(.3,25.8,.90), v(.75, 25.8, .80),.1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball(v(.3,25.8,.90), .2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball(v(.75, 25.8, .80), .2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The skinny key itself.
		g = new geom_simpleCurve(v(1.5, 26.1, .85), 1, .125, up, 160, 180);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The skinny key itself.
		g = new geom_simpleCurve(v(-1+3*(.0603), 26.1+(3*.3420201533), .85), 1.5, .15, up, -20, 20);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Mid key left 1.
		g = new geom_cylinder (v(0, 25.5, 1.1), v(-.5, 26, 1.1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_block(v(-1.3, 26.2, 1.1), v(-.5, 25.8, 1.1), down, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(-1.7,26,1.3),.5,.1,v(.3,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Mid key left 2.
		temp = v(-2, 25, 1.1);
		g = new geom_cylinder (v(-.5, 23.5, 1), temp, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate( temp, .6, .1, v(.3,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Mid key left 3.
		temp = v(-1.3, 25.4, 1.3);
		g = new geom_cylinder (v(-.5, 24.5, 1), temp, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate( temp, .5, .1, v(.3,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Connect left 3 to left 2.
		g = new geom_cylinder (v(-.25, 23.3, 1), v(-.25, 24.2, 1), .2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// the || dowel on the left side under the left side keys.
		g = new geom_cylinder (v(-1, 22, .5), v(-1, 28, .5), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (v(-1, 22, .5), v(0, 22, .5), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball (v(-1, 22, .5),.15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball (v(-1, 28, .5),.15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (v(-1, 28, .5), v(0, 28, .5), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate( v(-1, 22, 0), .5, .1, v(1,0,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The left rod for the left side keys.
		g = new geom_cylinder (v(-.5, 26.5, .9), v(-.5, 54.5, .9), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball(v(-.5, 54.5, .9), .2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(v(0, 27, 1),.3,.1,up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (v(-.5, 26.7, .9), v(0, 26.7, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (v(-.5, 27.3, .9), v(0, 27.3, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The rectangle middle key.
		p = v(-.25, 28, 1);
		c = v(-.25, 28.5, 1);
		g = new geom_cylinder (p, c, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball (c,.15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball (p,.15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (v(-.25, 28, 1), v(0, 28, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_block(v(0, 28.05, 1), v(.5, 27.8, 1), down, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		

		// The second torus for the middle keys.
		g = new geom_cylinder (v(-.5, 29, .9), v(0, 29, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(v(.25, 29, 1),.3,.1,up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The third circle middle key that is a filled in plate.
		g = new geom_cylinder (v(-.5, 30.5, 1), v(0, 30.5, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, 30.5, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The dividing key between the two circle keys.
		g = new geom_simpleCurve(v(1,29.5+5,1), 5, .1, up,257,270);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// An ornament next to the third circle key.
		g = new geom_ball(v(-.5, 30.2, 1),.15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The fourth circle middle key that is a filled in plate.
		double i = 34;
		g = new geom_cylinder (v(-.5, i, 1), v(.25, i, 1.1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// the second band
		g = new geom_ball(new Vector(0,36,0),1.02);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The first circle after the second band.
		g = new geom_plate(v(.25, 37, .9), .3,.1, v(.1,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		temp = v(0, 37.3, .95);
		g = new geom_cylinder (v(.25, 37, .9), temp, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (temp, v(-.5, 37.3 , 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder (v(.25, 37, .9), v(-.5, 36 , 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		

		// Circle 2
		i = 39;
		g = new geom_cylinder (v(-.5, i, 1), v(.25, i, 1.1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Circle 3
		i = 40.5;
		g = new geom_cylinder (v(-.5, i, 1), v(.25, i, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Circle 4
		i = 41;
		g = new geom_cylinder (v(-.5, i, 1), v(.25, i, 1.1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i, 1), .25,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Circle 5
		i = 42.5;
		g = new geom_cylinder (v(-.5, i, 1), v(0, i, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(v(.25, i, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(v(.25, i, 1), .2,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Circle 6 (part of the flat piece chain)
		i = 44;
		g = new geom_cylinder (v(-.5, i, 1), v(0, i, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i+.6, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i+.6+.5, 1), .2,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);
		// Circle 6 B
		i = 45.6;
		g = new geom_cylinder (v(-.5, i, 1), v(0, i, 1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.25, i, 1), .3,.1, up);
		g.setColor(C_KEYS);
		geoms.add(g);


		g = new geom_simpleCurve(v(3, 35, .95), 4, .1, up, 150, 180);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_block(v(-1.1, 35, 1), v(-.9, 30, 1), down, .1);
		g.setColor(C_KEYS);
		geoms.add(g);

		// The Emily Harmon Cylinders.
		i = 35.25;
		g = new geom_cylinder(v(0,i,1), v(.25,i,1), .05);
		g.setColor(C_EMILY);
		geoms.add(g);
		
		g = new geom_cylinder(v(0,i,1), v(0,i-.5,1), .05);
		g.setColor(C_EMILY);
		geoms.add(g);
		
		g = new geom_cylinder(v(.4,i,1), v(.4,i-.5,1), .05);
		g.setColor(C_EMILY);
		geoms.add(g);
		
		g = new geom_cylinder(v(.65,i,1), v(.65,i-.5,1), .05);
		g.setColor(C_EMILY);
		geoms.add(g);
		
		i-= .25;
		
		g = new geom_cylinder(v(0,i,1), v(.25,i,1), .05);
		g.setColor(C_EMILY);
		geoms.add(g);
		
		g = new geom_cylinder(v(.4,i,1), v(.65,i,1), .05);
		g.setColor(C_EMILY);
		geoms.add(g);
		
		i-= .25;
		
		g = new geom_cylinder(v(0,i,1), v(.25,i,1), .05);
		g.setColor(C_EMILY);
		geoms.add(g);
		
		
		// Now add the parts that belong on the right side.
		
		// The attachment to the circle with an extra rectangular part.
		g = new geom_block(v(.15, 30.2, 1), v(.4, 29.9, 1), down, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The large mass of metal on the right side after the band that leads to the second key flower.
		g = new geom_block(v(1.4, 38.3, 1), v(1.6, 34, 1), down, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		
		// Now add the keys in the key flower.
		g = new geom_plate(v(1.7, 38.2, 1.2), .2,.1, v(1,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Now add the keys in the key flower.
		g = new geom_plate(v(1.3, 38.8, 1), .4, .1, v(1,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Upper pinkey keys.
		g = new geom_block( v(1.8, 39, 1), v(2.7, 38.7, 39), down, .2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_block( v(1.6, 39.8, 1), v(2.7, 39.5, 39), down, .2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(1.6, 39.65, 1), v(.7, 39.65, .8), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// The parrellel dowel to the uppe keys.
		Vector v1 = v(.7, 39.9, .8);
		Vector v2 = v(.7, 37.6, .8);
		
		g = new geom_cylinder(v1, v2, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball(v1, .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball(v2, .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		Vector v3, v4;
		
		v3 = v2.add(forw.mult(.4));
		v4 = v3.add(right.mult(.3)).add(down.mult(.3));
		
		g = new geom_cylinder(v3, v4, .11);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v3, v3.add(left.mult(1).add(up.mult(.3))), .11);
		g.setColor(C_KEYS);
		geoms.add(g);

		// middle perp right cross next to a block.
		g = new geom_cylinder(v(.4, 28.3, 1), v(1, 28.3, .9), .08);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// -- Upper right perpendicular cross dowels.
		g = new geom_cylinder(v(0, 42, 1), v(1, 42, .9), .08);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(.7, 41.5, 1), v(1, 41.5, .9), .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(.9, 42.5, 1), v(1, 42.5, .9), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(.75, 43.3, 1), v(1, 43.3, .9), .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(1.3, 44.8, .7), v(1.3, 47.5, .7), .2);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(1.3, 47.5, .7), v(1.3, 48.5, .7), .1);
		g.setColor(C_KEYS);
		geoms.add(g);		

		// This final perpendicular that connects the right rod to the final rod.
		g = new geom_cylinder(v(1.3, 48.5, .7), v(.8, 48.5, .7), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The final rod.
		g = new geom_cylinder(v(.8, 48, .7), v(.8, 58, .7), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// A ball at one end.
		g = new geom_ball(v(.8, 48, .7), .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The penultimate parrellel dowel.
		v1 = v(.55, 46, 1);
		v2 = v(.55, 48.5, 1);
		g = new geom_cylinder(v1, v2, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Balls at both ends.
		g = new geom_ball(v1, .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball(v2, .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Two perpendicular keys.
		g = new geom_cylinder(v(.55, 46.5, 1.1), v(1, 46.5, .7), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(.1, 46.5, 1.1), v(.55, 46.5, 1.1),  .1);
		g.setColor(C_KEYS);
		geoms.add(g);		
		
		// The actual key.
		g = new geom_plate(v(.1, 46.5, 1), .25, .11, v(0,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// the second of the Two perpendicular keys.
		double k2 = 47.5;
		g = new geom_cylinder(v(.55, k2, 1.1), v(1, k2, .7), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(.1, k2, 1.1), v(.55, k2, 1.1),  .1);
		g.setColor(C_KEYS);
		geoms.add(g);		
		
		// The actual key.
		g = new geom_plate(v(.1, k2, 1), .25, .11, v(0,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
				
		
		// Perp hook and key.
		v1 = v(.3, 51, 1.1);
		g = new geom_cylinder(v1, v(.9, 51, .9),  .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The parrellel dowel
		v2 = v1.add(forw.mult(2.5));
		g = new geom_cylinder(v1, v2.add(up.mult(.1)),  .12);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// A bangle.
		g = new geom_torus(v1, .15, .1, v(0,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The perpendicular dowell.
		v3 = v1.add(forw.mult(1)).add(left.mult(.3));
		v4 =  v1.add(forw.mult(1)).add(right.mult(.3)).add(down.mult(.05));
		g = new geom_cylinder(v3, v4, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Balls at the ends of the perp dowel.
		g = new geom_ball(v3, .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_ball(v4, .15);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// The key
		g = new geom_plate(v2, .3, .1, v(0,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		v3 = v2.add(back.mult(.5)).add(up.mult(.3));
		v4 = v3.add(left.mult(.7)).add(down.mult(.4));
		
		// Connec this key to the left rod.
		g = new geom_cylinder( v3, v4, .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_torus(v3, .1, .1, v(0,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Last left key.
		g = new geom_plate(v(-.3, 50.3, 1), .2, .1, v(-.5,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// Connect the last left key to the left rod.
		g = new geom_cylinder(v(-.3, 50.3, 1.1), v(-.5, 50.3, .9), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		
		// The very last key.
		g = new geom_ball(v(.8, 58, .7), .25);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_cylinder(v(.8, 57.5, .7), v(.1, 57.5, 1.1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);

		g = new geom_cylinder(v(.1, 57.5, 1.1), v(.1, 58.5, 1.1), .1);
		g.setColor(C_KEYS);
		geoms.add(g);
		
		g = new geom_plate(v(.1, 58.5, 1.1), .2, .1, v(0,0,1));
		g.setColor(C_KEYS);
		geoms.add(g);
		
		// mouthpiece.
		
		// Torus 1.
		g = new geom_torus(v(0,59,0), 1.05, .1, v(0,1,0));
		g.setColor(Color.GRAY);
		geoms.add(g);
		
		// Torus 2.
		g = new geom_torus(v(0,60,0), 1, .6, v(0,1,0));
		g.setColor(Color.GRAY);
		geoms.add(g);
		
		// Mouth Piece.
		
		g = new geom_simpleCurve(v(0,60, -20), 20, .25, v(1,0,0), -20, 0);
		g.setColor(Color_hsv(0,0,80));
		geoms.add(g);
		

		// -- Fancy plane.
		
		Color C_BACKGROUND = Colors.Color_hsv(176, 62, 100);
		
		// Fancy plane to show case reflections.
		g = new geom_plane(v(0, 0, -5), v(-.5, 0, 1));
		g.setReflectivity(.7);
		g.setColor(C_BACKGROUND);
		geoms.add(g);
		
		// Fancy plane to show case reflections.
		g = new geom_plane(v(-10, 0, 0), v(1, 0, 1));
		g.setReflectivity(.7);
		g.setColor(C_BACKGROUND);
		geoms.add(g);
		
		
		// Torus 2.
		g = new geom_torus(v(0, -1.45,0), 1.35, .6, v(0,1,0));
		g.setColor(C_KEYS);
		geoms.add(g);
		
	}// End of the code that creates Geoms.

	public Vector v(double x, double y, double z)
	{
		return new Vector(x, y, z);
	}

	@Override
	public boolean withinBounds(Vector z) {
		
		return z.mag() < 200;
	}

	private class body extends geom_spaceCurve
	{
		public body()
		{
			super();
			// End points of this parametric curve that represents the bell of this english horn.
			i1 = 0;
			i2 = 7.2;			
		}
		public Vector func1(double i)
		{
			return new Vector(0,i,0);
		}
		public double radius(double i)
		{			
			return .75*(2+1*cos(degToRad(i*25)));
		}
	}
	
}
