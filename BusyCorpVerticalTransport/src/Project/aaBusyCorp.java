package Project;

import java.awt.Color;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import GUI.Button;
import GUI.TextBox;
import SimpleEngine.MainRoom;

public class aaBusyCorp extends MainRoom
{

	public static void main(String[] args)
	{
		new aaBusyCorp("Busy Corp!", 1200, 800);
	}
	
	public aaBusyCorp(String gameName, int w, int h)
	{
		super(gameName, w, h);
		
		int wr = 100;
		int hr = 100;
		
		BufferedImage b1 = ImageFactory.ColorRect(Color.WHITE,  wr, hr);
		BufferedImage b2 = ImageFactory.ColorRect(Color.BLUE,  wr, hr);
		BufferedImage b3 = ImageFactory.ColorRect(Color.RED,  wr, hr);
		BufferedImage b4 = ImageFactory.ColorRect(Color.GREEN,  wr, hr);
		
		addOBJ(new Button(200, 200, "Box", b1, b2, b3, b4));
		
	}

}
