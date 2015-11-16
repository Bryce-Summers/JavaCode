// An SVG file Generator.

public class Main
{
	
	public static void main(String[] args)
	{
		new Main();
	}

	
	
	
	public Main()
	{
		//formCircleFile();
		//formAperatureFile();
		formDegenerateSquare();
	}
	
	public void log(String str)
	{
		System.out.println(str);
	}
		
	public void printHeader(double w, double h)
	{
		log("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		log("<!-- Generator: Adobe Illustrator 16.0.4, SVG Export Plug-In . SVG Version: 6.00 Build 0)  -->");
		log("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		log("<svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"");
		log("width=\""  + w + "px\"");
		log("height=\"" + h + "px\""); 
		log("viewBox=\"0 0 " + w + " " + h + "\" enable-background=\"new 0 0 " + w +" " + h + "\"");
		log("xml:space=\"preserve\">");
	}
	
	public void printEnd()
	{
		log("</svg>");
	}
	
	public void printPolygon(double[] x_coords, double[] y_coords, String color)
	{
		StringBuilder sb = new StringBuilder();
		int len = x_coords.length;
		for(int i = 0; i < len; i++)
		{
			sb.append(x_coords[i] + "," + y_coords[i] + " ");
		}
		
		log("<polygon fill=\"" + color + "\" points=\"" + sb + "\"/>");
	}
	
	public void formCircleFile()
	{
		printHeader(100, 100);
		
		int len = 100;
		double[] x_coords = new double[len];
		double[] y_coords = new double[len];
		
		for(int i = 0; i < len; i++)
		{
			double angle = i*Math.PI*2/len;
			x_coords[i] = 50 + 50*Math.cos(angle);
			y_coords[i] = 50 + 50*Math.sin(angle);
		}
		
		printPolygon(x_coords, y_coords, "#1865ED");
		printEnd();
	}
	
	public void formAperatureFile()
	{
		printHeader(150, 150);
		
		int len = 100;
		
		int center = 75;
		int radius = 25;
		
		double[] angles_outer = new double[len];
		double[] angles_inner = new double[len];
		
		// Populate the angles.
		for(int i = 0; i < len; i++)
		{
			angles_inner[i] = i*Math.PI*2/len;
			angles_outer[i] = (i + .5)*Math.PI*2/len;
		}

		int inner_radius = radius;
		int outer_radius = radius*2;
		
		// Form inner triangles.
		for(int i = 0; i < len; i++)
		{
			double[] x_coords = new double[3];
			double[] y_coords = new double[3];
			
			double angle_inner1 = angles_inner[i];
			double angle_outer  = angles_outer[i];
			double angle_inner2 = angles_inner[(i + 1) % len];
			
			x_coords[0] = center + inner_radius*Math.cos(angle_inner1);
			y_coords[0] = center + inner_radius*Math.sin(angle_inner1);
			
			x_coords[1] = center + outer_radius*Math.cos(angle_outer);
			y_coords[1] = center + outer_radius*Math.sin(angle_outer);
			
			x_coords[2] = center + inner_radius*Math.cos(angle_inner2);
			y_coords[2] = center + inner_radius*Math.sin(angle_inner2);
			
			printPolygon(x_coords, y_coords, "#00FFF2");
			
			x_coords[1] = center;
			y_coords[1] = center;
			
			// Yellow inner circle.
			printPolygon(x_coords, y_coords, "#FFF291");
			
		}
		
		// Form outer Triangles.
		for(int i = 0; i < len; i++)
		{
			double[] x_coords = new double[3];
			double[] y_coords = new double[3];
			
			double angle_outer_1 = angles_outer[i];
			double angle_inner1  = angles_inner[(i + 1) % len];
			double angle_outer_2 = angles_outer[(i + 1)% len];
			
			
			x_coords[0] = center + outer_radius*Math.cos(angle_outer_1);
			y_coords[0] = center + outer_radius*Math.sin(angle_outer_1);
			
			x_coords[1] = center + inner_radius*Math.cos(angle_inner1);
			y_coords[1] = center + inner_radius*Math.sin(angle_inner1);
			
			x_coords[2] = center + outer_radius*Math.cos(angle_outer_2);
			y_coords[2] = center + outer_radius*Math.sin(angle_outer_2);
			
			// Orange outer polygons.
			printPolygon(x_coords, y_coords, "#FF8168");
			
			x_coords[1] = center + radius*2.5*Math.cos(angle_inner1);
			y_coords[1] = center + radius*2.5*Math.sin(angle_inner1);
			
			// Red Ends.
			printPolygon(x_coords, y_coords, "#FF1900");
		}		
				
		printEnd();
	}
	
	
	public void formDegenerateSquare()
	{
		int w = 500;
		int h = 500;
		
		printHeader(w, h);
				
		int inc = w/500;
		
		// Horizontal Triangles.
		for(int x = 0; x < w; x += inc)
		{
			double[] x_coords = new double[3];
			double[] y_coords = new double[3];
			
			x_coords[0] = 0.0;
			y_coords[0] = 0.0;
			
			x_coords[1] = x;
			y_coords[1] = h;
			
			x_coords[2] = x+inc;
			y_coords[2] = h;
			
			String color = x % (inc*2) == 0 ? "#000000" : "#001DFF";
			printPolygon(x_coords, y_coords, color);			
		}
		
		// Vertical Triangles.
		for(int y = 0; y < h; y += inc)
		{
			double[] x_coords = new double[3];
			double[] y_coords = new double[3];
			
			x_coords[0] = 0.0;
			y_coords[0] = 0.0;
			
			x_coords[1] = w;
			y_coords[1] = y;
			
			x_coords[2] = w;
			y_coords[2] = y+inc;
			
			String color = y % (inc*2) == 0 ? "#001DFF" : "#000000";
			printPolygon(x_coords, y_coords, color);			
		}
		
		printEnd();
	}

}