package GUI;

import java.awt.Color;
import java.awt.Graphics;

import util.interfaces.Consumer1;
import BryceMath.Calculations.Colors;
import BryceMath.Calculations.MathB;


/*
 * A GUI component that enables the user to select
 * from a set of states from [0, state_number]
 */

public class UI_Slider extends UI_Button
{

	int num_states = 1;
	int current_state = 0;
	int slider_w = 20;
	
	private Consumer1<Integer> action_stateChange = null;
	

	public UI_Slider(int x, int y, int w, int h, int num_states)
	{
		super(x, y, w, h, "");

		this.num_states = num_states;
		
		setDrawBorders(false);		
		
	}
	
	// Takes a function that maps integer states to procedures.
	public void setOnSlide(Consumer1<Integer> action)
	{
		action_stateChange = action;
	}
	
	public int getState()
	{
		return current_state;
	}
	
	@Override
	public void draw(Graphics g)
	{

		super.draw(g);
		
		int w = getW();
		int h = getH();

		
		int x_end = x + w - 1;
		int y_end = y + h - 1;
		int y_half = y + h/2;
		
		g.setColor(Color.black);
		
		// Left side.
		g.drawLine(x,  y,  x,  y_end);

		// Right side.
		g.drawLine(x_end,  y, x_end,  y_end);
		
		// Middle Line.		
		g.drawLine(x,  y_half,  x_end,  y_half);

		
		int slider_x = (getW() - slider_w)*current_state/num_states;
		
		g.setColor(Colors.Color_hsv(0, 0, 20));
		g.fillOval(x + slider_x, y + getH()/4, slider_w, getH()/2 - 1);
	}
	
	@Override
	public void mouseD(int x, int y)
	{
		super.mouseD(x, y);
		
		current_state = (x - slider_w/2 - this.x)*(num_states)/(getW() - slider_w);
		
		current_state = MathB.bound(current_state, 0,  num_states);
		
		if(action_stateChange != null)
		{
			action_stateChange.eval(current_state);
		}
	}

}