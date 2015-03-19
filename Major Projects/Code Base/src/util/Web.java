package util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/** Web operations such as viewing a web page.
 * 
 * @author Bryce Summers
 * @data   8 - 12 - 2014
 *
 */



public class Web
{
	public static void viewWebsite(String name)
	{

		URI website = null;
		try
		{
			website = new URI(name);
		}
		catch (URISyntaxException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
