package Game_Engine.GUI.Components.small.buttons;

import java.io.IOException;
import java.net.URI;

import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.small.gui_button;

/*
 * 8/12/2014
 * 
 * Not complete
 * 
 * This should be used to create buttons that do a certain action when pressed.
 */

public class gui_webSiteButton extends gui_button
{
	
	private URI website;

	public gui_webSiteButton(double x, double y, int w, int h, URI website)
	{
		super(x, y, w, h);
		
	}
	
	public gui_webSiteButton(Rectangle r)
	{
		super(r);
	}
	
	public void update()
	{
		if(flag())
		{
			try
			{
				java.awt.Desktop.getDesktop().browse(website);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.out.println("Website could not be accessed");
			}
		}
	}
	
}
