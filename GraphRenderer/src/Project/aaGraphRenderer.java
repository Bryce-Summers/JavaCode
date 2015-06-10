package Project;

import java.awt.image.BufferedImage;

import util.ImageUtil;
import BryceImages.Rendering.StartRender;
import Data_Structures.Structures.List;

public class aaGraphRenderer {

	public static void main(String[] args)
	{
		
		List<Integer> edges = new List<Integer>();
		
		// Dummy Test.
		//edges.append(0, 1, 1, 11, 11, 12, 12, 13);
		
		/*
		edges.append(6, 7, 3, 4, 0, 3, 8, 9, 4, 6, 3, 5, 0, 9, 1, 2, 0, 1);
		//edges.append(1, 9, 0, 2, 4, 8, 0, 4, 5, 8, 7, 8, 1, 5, 6, 7, 3, 4);

		renderGraph(new ccDense(1000, 1000, 10, edges), edges, "Dense");
		*/
		
		
		//edges.append(61, 62, 30, 31, 10, 11, 10, 20, 25, 26, 62, 63, 80, 81, 20, 30, 15, 25, 40, 50, 31, 32, 50, 60, 58, 59, 33, 34, 38, 39, 17, 18, 75, 85, 35, 36, 14, 24, 1, 2, 71, 72, 11, 12, 60, 61, 74, 75, 87, 97, 50, 51, 96, 97, 1, 11, 13, 14, 4, 14, 0, 1, 61, 71, 42, 43, 2, 3, 19, 29, 81, 91, 12, 22, 31, 41, 12, 13, 88, 89, 21, 22, 49, 59, 77, 87, 80, 90, 70, 80, 60, 70, 52, 62, 32, 42, 93, 94, 52, 53, 73, 74, 65, 66, 47, 57, 57, 67, 92, 93, 97, 98, 23, 33, 84, 94, 72, 82, 72, 73, 79, 89, 53, 54, 83, 93, 38, 48, 86, 96, 89, 99, 24, 25, 63, 64, 4, 5, 6, 7, 29, 39, 84, 85, 34, 44, 24, 34, 16, 26, 28, 29, 64, 65, 98, 99, 18, 19, 47, 48, 77, 78, 68, 78, 30, 40, 46, 47, 55, 65, 48, 49, 16, 17, 45, 46, 27, 28, 55, 56, 66, 76, 85, 86, 95, 96, 6, 16, 59, 69, 36, 46, 37, 47, 8, 9, 7, 8);
	
		edges.append(25, 35, 36, 37, 64, 74, 10, 20, 20, 21, 20, 30, 57, 58, 51, 52, 90, 91, 40, 50, 0, 1, 51, 61, 60, 61, 5, 15, 70, 71, 28, 29, 3, 13, 80, 90, 0, 10, 66, 76, 1, 11, 84, 94, 41, 42, 21, 22, 40, 41, 80, 81, 36, 46, 43, 53, 86, 96, 30, 40, 50, 60, 72, 82, 71, 72, 16, 17, 31, 41, 59, 69, 68, 78, 2, 3, 2, 12, 83, 93, 56, 57, 44, 45, 64, 65, 32, 33, 94, 95, 45, 46, 52, 53, 4, 14, 87, 88, 67, 77, 17, 27, 11, 12, 46, 56, 70, 80, 98, 99, 3, 4, 76, 86, 53, 54, 23, 24, 91, 92, 61, 62, 13, 23, 43, 44, 77, 87, 9, 19, 85, 95, 87, 97, 62, 63, 27, 37, 73, 83, 77, 78, 24, 34, 24, 25, 8, 9, 33, 43, 92, 93, 54, 64, 16, 26, 55, 65, 74, 75, 6, 7, 61, 71, 19, 29, 74, 84, 15, 16, 49, 59, 7, 8, 28, 38, 95, 96, 68, 69, 7, 17, 58, 68, 17, 18, 88, 98, 79, 89, 48, 49, 39, 49, 46, 47, 69, 79);
		
		renderGraph(new ccLattice(1000, 1000, 10, edges), edges, "Lattice");
				
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
