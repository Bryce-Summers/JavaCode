package GUI;

import java.awt.Color;
import java.awt.image.BufferedImage;

import util.interfaces.Action;
import BryceMath.Calculations.Colors;
import SimpleEngine.interfaces.MouseInput;

public class UI_Button extends UI_TextBox implements MouseInput
{

	public final int NORMAL = 0;
	public final int HOVER = 1;
	public final int CLICKED_ON_NO_HOVER = 2;
	public final int CLICKED_ON_HOVER = 3;
	
	public Color[] colors;
	private int current_color = 0;
	
	private UI_Button clickedOn = null;
	
	Action action = null;
	
	public UI_Button(int x, int y, String str, BufferedImage ... images)
	{
		super(x, y, str, images);
		
		colors = new Color[4];
		
		colors[0] = Colors.C_CLEAR;
		colors[1] = Colors.Color_hsv(0, 0, 97, 50);
		colors[2] = Colors.Color_hsv(0, 0, 80, 50);
		colors[3] = Colors.Color_hsv(0, 0, 70, 30);
		
		
	}
	
	public void setAction(Action myAction)
	{
		action = myAction;
	}
	
	private void setColor(int index)
	{
		super.setColor(colors[index]);
		current_color = index;
	}

	public void mouseM(int x, int y)
	{
	}
	
	
	public void mouseP(int x, int y)
	{
		clickedOn = this;
		setColor(CLICKED_ON_HOVER);
	}
	
	public void mouseR(int x, int y)
	{
		if(clickedOn == this)
		{
			clickedOn = null;
			action();			
		}
		
		setColor(HOVER);
	}

	@Override
	public void global_mouseP() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void global_mouseR()
	{
		clickedOn = null;
		if(current_color != HOVER)
		{
			setColor(NORMAL);
		}
	}

	@Override
	public void global_mouseD(int x, int y)
	{	
		if(clickedOn != null)
		{
			setColor(mouseCollision(x, y) ? CLICKED_ON_HOVER : CLICKED_ON_NO_HOVER);	
		}
		else
		{
			setColor(NORMAL);
		}
		
				
	}

	@Override
	public void global_mouseM(int x, int y)
	{
		setColor(mouseCollision(x, y) ? HOVER : NORMAL);	
	}

	@Override
	public void global_mouseScroll(int scroll)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseD(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseScroll(int x, int y, int scroll) {
		// TODO Auto-generated method stub
		
	}

	// Directly call the input action.
	public void action()
	{
		action.action();
	}
}
