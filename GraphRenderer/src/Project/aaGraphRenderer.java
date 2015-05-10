package Project;

import java.awt.image.BufferedImage;

import util.ImageUtil;
import BryceImages.Rendering.StartRender;
import Data_Structures.Structures.List;

public class aaGraphRenderer {

	public static void main(String[] args)
	{
		
		List<Integer> edges = new List<Integer>();
		
		edges.append(0, 1, 1, 11, 11, 12, 12, 13);
		
		renderGraph(new ccLattice(1000, 1000, 10, edges), edges, "Lattice");
		renderGraph(new ccDense(1000, 1000, 10, edges), edges, "Dense");
				
		System.out.println("Exiting.");
	}
	
	public static void renderGraph(ccGraph cc, List<Integer> edges, String name)
	{
		StartRender R = new StartRender(true);
		BufferedImage image = R.render(cc);
		System.out.println("Rendering complete for: " + name);
		
		ImageUtil.saveImage(image, name);
		
		System.out.println("Image Saved: " + name);
		
	}
	
}
