package project.client;


/*
 * Dear Bryce, you need to add source module lines to the xml files in order
 * to get the gwt to automatically translate them for you.
 * 
 * 
 * This class provides all of the code that interfaces with the Google Windowing Toolkit.
 * 
 * 
 * TODO:
 * 
 * Finish the Graph Visualizer.
 * 
 * Implement a N-body simulation to keep the nodes and edges apart from each other.
 * <!> Use the context2d.scale() functions and translation functions to always keep the graph in view.
 * 
 * Implement interactivity by allowing the use to click and drag the various nodes.
 * 
 * Implement GRAPH (ical) programming language for constructing and manipulating graphs.
 * 
 * Use this information visualization to demonstrate how data structures work.
 * 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import SimpleEngine.MainRoom;
import project.client.CodeStructures.Processor;
import project.client.GraphStructures.Edge;
import project.client.GraphStructures.Node;
import project.client.interfaces.Body;
 
public class WebCAMP extends MainRoom
{
    
	public static void main(String[] args)
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Set the GUI widow to a custom dimension.
			
		new WebCAMP("Bryce Graphical Language.", (int)dim.getWidth(), (int)dim.getHeight());		
	}
	
    public WebCAMP(String gameName, int w, int h)
    {
		super(gameName, w, h);
		
		canvasWidth = w;
		canvasHeight = h;
	}
   
    final int canvasWidth;
    final int canvasHeight;
    
    // Graph Visualization code.
    public ArrayList<Node> vertices = new ArrayList<Node>();
    public ArrayList<Edge> edges    = new ArrayList<Edge>();
    
    Processor processor;
    int code_example_num = 0;
        
    
    int canvasW, canvasH;
    
	@Override
	public void initialize(int w, int h)
	{
		// TODO Auto-generated method stub
		
		canvasW = w;
		canvasH = h;
		
        BuildCode();        
        
    }
    
    // Builds the code and starts up the processor.
    public void BuildCode()
    {
    	ArrayList<String> code = new ArrayList<String>();
    	
   	
    	/*
		code.add("'1' -> '2'");
		code.add("'2' -> '3'");
		code.add("'3' -> '4'");
		code.add("'4' -> '5'");
    	*/
    	
    	/*
    	code.add("'4' -> '6'");
    	code.add("'6' -> '7'");
    	code.add("'6' -> '5'");
    	code.add("'4' -> '2'");
    	code.add("'2' -> '3'");
    	code.add("'2' -> '1'");
    	*/
    	
    	/*
    	code.add("'2' -> '4'");
    	code.add("'2' -> '1'");
    	code.add("'4' -> '3'");
    	code.add("'4' -> '6'");
    	code.add("'6' -> '5'");
    	code.add("'6' -> '7'");
    	*/
    	
    	
    	
    	    	
    	if(code_example_num == 6)
       	{
    		code.add("// Graph Building.");
    		code.add("A -> B");
    		code.add("B <- C");
    		code.add("C <-> D");
    		code.add("A <- E");
       	}
    	
    	if(code_example_num == 5)
       	{
    	code.add("//Operations.");	
    	code.add("A = (1+2)");
    	code.add("A = (5|0)");
    	code.add("A = 1 ==6 ");
    	code.add("A = 1 + 1");
    	code.add("A = 5 % 2");
    	code.add("A = 2 << 5");
    	code.add("A = 8 >> 2");
    	code.add("A = 1 + 1 + 1");
    	code.add("A = 2*3 + 1*1");
    	code.add("A = (5 | 0) & (1|6)");
    	code.add("A = (5 | 0) & 1 ==6");
    	code.add("A = (5 | 0) & (1|6) & 1 ==6 ");
    	code.add("A = (((1) + 3)) + 3");
       	}
    	
    	

    	
    	if(code_example_num == 0)
    	{
    	
	    	code.add("def.fib(n)");
	    	code.add("  if(n <= 1)");
	    	code.add("    return = n");
	    	code.add("    return");
	    	code.add("  end");
	    	code.add("");
	    	code.add("  var.a = fib(n - 1)");
	    	code.add("  var.b = fib(n - 2)");
	    	code.add("  return = a + b");
	    	code.add("  return");
	    	code.add("end");
	    	code.add("");
	    	code.add("// End of function");
	    	code.add("result = fib(4)");
    	
    	}
    	
   	
    	// Scope and Shadowing Example.

    	if(code_example_num == 1)
    	{
        	code.add("// Shadowing");
	    	code.add("VAR = \"GLOBAL\"");
	    	code.add("if(1)");
	    	code.add("  var.VAR = \"CLASS\"");
	    	code.add("  for(i = 0; i < 5; i++)");
	    	code.add("      var. VAR[i] = \"LOCAL\"");
	    	code.add("  end");
	    	code.add("end");
    	}
    	    	
    	    	
    	if(code_example_num == 2)
    	{
    		
    	code.add("// Conditionals");
    	code.add("// and Loops.");
    	code.add("A = 0");
    	code.add("if(A ==0)");
    	code.add("  B = 2");
    	code.add("end");
    	
    	code.add("if(A != 0)");
    	code.add("end");
    	
    	
    	code.add("while(A <= 1)");
    	code.add("   A++");
    	code.add("end");
    	
    	
    	code.add("A = 0");
    	code.add("for(i=0;i < 2; i++)");
    	code.add("for(i2=0;i2 < 2; i2++)");
    	code.add("   A++");
    	code.add("   end");
    	code.add("end");
    	}

    	if(code_example_num == 3)
       	{
       		code.add("// Recursive Counting");
	    	code.add("def.foo(n)");
	    	code.add("  if(n == 0)");
	    	code.add("    return = 0");
	    	code.add("    return");
	    	code.add("  end");
	    	code.add("  result = foo(n - 1) + 1");
	    	code.add("  return = result");
	    	code.add("  return");
	    	code.add("end");
	    	code.add("");
	    	code.add("result = foo(5)");
       	}
    	
    	
       	if(code_example_num == 4)
       	{
       		code.add("//Linked List");
       		code.add("head = head");
       		code.add("size = 0");
       		code.add("");
       		code.add("def.push(data)");
       		code.add("  node.data = data");
       		code.add("  node.next = head");
       		code.add("  head = &node");
       		code.add("  return = 0");
       		code.add("  return");
       		code.add("end");
       		code.add("");
       		code.add("def.pop()");
       		code.add("  return = head.data");
       		code.add("  head = &head.next");
       		code.add("  return = 0");
       		code.add("  return");
       		code.add("end");
       		code.add("");
       		code.add("push(0)");
       		code.add("push(1)");
       		code.add("push(2)");
       		code.add("");
       		code.add("A = pop()");
       		code.add("B = pop()");
       		code.add("C = pop()");
       	}
    	//*/
    	
    	code_example_num = (code_example_num + 1) % 7;
    	
    	processor = new Processor(code);
    	
    }
    
    @Override
    public void update()
    {
       processor.update();
    }
    
    public void draw(Graphics g)
    {
    	
    	enableAllAntialiasing((Graphics2D) g);
    	
    	// White Background.
    	g.setColor(Color.white);
    	g.fillRect(0, 0, canvasW, canvasH);
    	        
        processor.draw(g);                
    }
    
	public static void enableAllAntialiasing(Graphics2D g2)
	{

		g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
			        RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(
			        RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(
			        RenderingHints.KEY_FRACTIONALMETRICS,
			        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}
    
    
    // -- Mouse Interaction Code.

    public static Body clicked_on;
    
    @Override
	public void global_mouseR()
	{
		clicked_on = null;
	}

	public void global_mouseD(int mouseX, int mouseY)
	{
		
		if(clicked_on != null)
		{
			clicked_on.x = mouseX;
			clicked_on.y = mouseY;
		}
		
	}

	@Override
	public void mouseP(int mouseX, int mouseY)
	{
	
		for(Body b : processor.graph.current_vertices)
		{
			if(b.containsPoint(mouseX, mouseY))
			{
				clicked_on = b;
			}
		}
		
	}


	public void keyR(int key)
	{
		if(key == ' ')
		{
			BuildCode();
		}
	}

	// Communicates Keyboard Input to the Processor class.
	public void keyP(int key)
	{
		// TODO Auto-generated method stub
		if(key == KeyEvent.VK_LEFT)
		{
			processor.prev();
		}

		if(key == KeyEvent.VK_RIGHT)
		{
			processor.next();
		}
		
		if(key == KeyEvent.VK_UP)
		{
			processor.viewUp();
		}

		if(key == KeyEvent.VK_DOWN)
		{
			processor.viewDown();
		}
	}
    
}
