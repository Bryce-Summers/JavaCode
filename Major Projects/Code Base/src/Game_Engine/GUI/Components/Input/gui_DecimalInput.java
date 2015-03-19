package Game_Engine.GUI.Components.Input;

import java.awt.event.KeyEvent;

import BryceMath.Geometry.Rectangle;
import BryceMath.Numbers.Rational;

public class gui_DecimalInput extends gui_InputBox<Rational>
{
	// -- Private Data.
	private gui_IntegerInput part_int;
	private gui_IntegerInput part_decimal;
	
	private boolean decimal = false;

	private boolean changed = false;
	
	// -- Constructors.

	public gui_DecimalInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		iVars();
	}


	public gui_DecimalInput(Rectangle rect)
	{
		super(rect);
		iVars();
	}

	
	private void iVars()
	{
		part_int     = new gui_IntegerInput(0, 0, 0, 0);
		part_int.setVisible(false);
		part_decimal = new gui_IntegerInput(0, 0, 0, 0);
		part_decimal.setVisible(false);
	}

	@Override
	public void clearInput()
	{
		decimal = false;
		part_int.clearInput();
		part_decimal.clearInput();
		
		refreshText();
	}


	@Override
	public Rational getInput()
	{
		return new Rational(toString());
	}

	public void forceKeyP(int key)
	{
		switch(key)
		{
			case KeyEvent.VK_PERIOD:
				decimal = true;
				refreshText();
				return;
			case KeyEvent.VK_BACK_SPACE :
				if(decimal && part_decimal.getInputSize() == 0)
				{
					decimal = false;
					refreshText();
					return;
				}
		}
		
		if(decimal && key != KeyEvent.VK_MINUS)
		{
			part_decimal.forceKeyP(key);
		}
		else
		{
			part_int.forceKeyP(key);			
		}

		if(query_changed())
		{
			refreshText();
		}
		
	}

	@Override
	public boolean input_changed()
	{
		boolean b1 = part_int	 .input_changed();
		boolean b2 = part_decimal.input_changed();
		
		if(changed || b1 || b2)
		{
			changed = false;
			return true;
		}
		
		return false;

	}
	
	public boolean query_changed()
	{
		return changed || part_int.query_changed() || part_decimal.query_changed();
	}

	
	// Allows input boxes in this package such as the Rational
	// input boxes to forcefully add a character to this box.
	void addChar(char c)
	{
		if(decimal)
		{
			part_decimal.addChar(c);
			return;
		}
		
		part_int.addChar(c);
	}
	
	public void refreshText()
	{
		setText(toString());
		changed = true;
	}


	public int getInputSize()
	{
		if(!decimal)
		{
			return part_int.getInputSize();
		}
		
		return part_int.getInputSize() + 1 + part_decimal.getInputSize();
	}
	
	public String toString()
	{
		// Handle trivial null imputs.
		if(getInputSize() == 0)
		{
			return part_int.toString();
		}
		
		if(decimal)
		{
			return part_int.toString() + '.' + part_decimal.toString();
		}
		else
		{
			return part_int.toString();
		}
	}
	
	public void update()
	{	
		super.update();
		
		fitToContents();
	}
	
	public void setDefaultText(String str)
	{
		part_int.setDefaultText(str);
		refreshText();
	}


	public void populateInput(double val)
	{

		String str = "" + val;
		
		String[] parts = str.split(".");
		
		part_int.populateInput(new Integer(parts[0]));
		part_decimal.populateInput(new Integer(parts[1]));
	}
}
