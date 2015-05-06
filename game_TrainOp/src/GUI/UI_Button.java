package GUI;

import java.awt.Color;
import java.awt.image.BufferedImage;

import util.interfaces.Action;
import util.interfaces.Consumer1;
import BryceMath.Calculations.Colors;
import SimpleEngine.interfaces.KeyInput;
import SimpleEngine.interfaces.MouseInput;

public class UI_Button extends UI_TextBox implements MouseInput, KeyInput
{

	public final int NORMAL = 0;
	public final int HOVER = 1;
	public final int CLICKED_ON_NO_HOVER = 2;
	public final int CLICKED_ON_HOVER = 3;
	public final int SELECTED = 4;
	
	public Color[] colors;
	private int current_color = 0;
	
	private UI_Button clickedOn = null;
	
	Action action = null;
	private Action mouse_in_action = null;
	private Consumer1<Integer> action_keyP = null;
	private Consumer1<Integer> action_keyR = null;
	
	public UI_Button(int x, int y, String str, BufferedImage ... images)
	{
		super(x, y, str, images);
		
		colors = new Color[5];
		
		colors[0] = Colors.C_CLEAR;
		colors[1] = Colors.Color_hsv(0, 0, 90, 50);
		colors[2] = Colors.Color_hsv(0, 0, 80, 50);
		colors[3] = Colors.Color_hsv(0, 0, 70, 30);
		colors[4] = Colors.Color_hsv(0, 20, 100, 30);
		
		setColor(null);
	}
	
	public UI_Button(int x, int y, int w, int h, String str)
	{
		super(x, y, w, h, str);
		
		colors = new Color[4];
		
		colors[0] = Colors.C_CLEAR;
		colors[1] = Colors.Color_hsv(0, 0, 90, 50);
		colors[2] = Colors.Color_hsv(0, 0, 80, 50);
		colors[3] = Colors.Color_hsv(0, 0, 70, 30);
		
		setColor(null);
	}
	
	
	// Functionality Specification functions.
	
	public void setAction(Action myAction)
	{
		action = myAction;
	}
	
	public void setOnMouseMove(Action action)
	{
		mouse_in_action = action;
	}
	
	public void setOnKeyP(Consumer1<Integer> action)
	{
		action_keyP = action;
	}
	
	public void setOnKeyR(Consumer1<Integer> action)
	{
		action_keyR = action;
	}
	
	
	// -- Aestetic visual specification functions.
	
	protected void resetColor()
	{
		current_color = -1;
		setColor(NORMAL);
	}
	
	public void setSelectedColor()
	{
		setColor(SELECTED);
	}
	
	private void setColor(int index)
	{
		// Do not allow color changing while in selection mode.
		if(current_color == SELECTED)
		{
			return;
		}
		
		super.setColor(colors[index]);
		current_color = index;
	}

	// -- Allow for a onMouseMove Action.
	public void mouseM(int x, int y)
	{
		if(mouse_in_action != null)
		mouse_in_action.action();
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

	@Override
	public void keyP(int key)
	{
		if(action_keyP != null)
		{
			action_keyP.eval(key);
		}
		
	}

	@Override
	public void keyR(int key)
	{
		if(action_keyR != null)
		{
			action_keyR.eval(key);
		}
		
	}
}
