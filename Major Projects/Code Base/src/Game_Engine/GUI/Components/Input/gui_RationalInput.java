package Game_Engine.GUI.Components.Input;

import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import BryceMath.Numbers.Rational;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.gui_look_and_feel;
import Game_Engine.GUI.Components.small.boxes.Selectable;
import Game_Engine.GUI.Components.small.boxes.gui_focusBox;

public class gui_RationalInput extends Obj_union implements keyInput<Rational>, Selectable
{

	private gui_DecimalInput num;
	private gui_DecimalInput denom;
	private gui_look_and_feel foreground;
	
	private boolean changed = false;

	// Store which mode this input box is in.
	private static final int INTEGER_MODE  = 0;
	private static final int RATIONAL_MODE = 1;
	private int mode = INTEGER_MODE;
	
	public gui_RationalInput(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public void iObjs()
	{
		int borderSize = SpriteLoader.gui_borderSize;
		
		int compH = getH()/2 + borderSize;
		
		// Format the numerator and denominator boxes to correctly fill the rational boxes subspace.
		
		denom = new gui_DecimalInput(getX() + borderSize, getY() + getH()/2, getW() - borderSize*2, compH - borderSize);
		denom.setDrawBorders(false);
		obj_create(denom);
		
		num = new gui_DecimalInput(getX() + borderSize, getY() + borderSize, getW() - borderSize*2, compH - borderSize*2);
		num.setDrawBorders(false);
		obj_create(num);
		
		foreground = new gui_look_and_feel(getX(), getY(), getW(), getH());
		foreground.makeTransparent();
		obj_create(foreground);
		
		// Start in Integer Mode.
		toIntegerMode();

	}
	
	public void endStep()
	{	
		super.endStep();
		
		fitToContents();
	}
	
	public void fitToContents()
	{
		num.fitToContents();
		denom.fitToContents();
		
		setW(Math.max(num.getW() + SpriteLoader.gui_borderSize*2, denom.getW()) + SpriteLoader.gui_borderSize*2);

		
		num  .setW(getW() - SpriteLoader.gui_borderSize*2);
		denom.setW(getW() - SpriteLoader.gui_borderSize*2);
		
		foreground.setW(super.getW());
	}
	
	public Rational getInput()
	{
		Rational numerator   = num.getInput();
		Rational denominator = denom.getInput();
		
		
		// Malformed numerator.
		if(numerator == null)
		{
			numerator = Rational.ONE;
		}
		
		
		// Malformed denominator
		if(denominator == null)
		{
			return numerator;
		}
		
		if(numerator.eq(0) && !denominator.eq(0))
		{
			numerator = Rational.ONE;
		}
		
		// Divide by 0 error.
		if(denominator.eq(0))
		{
			return numerator;
		}
		
		return numerator.div(denominator);
	}
	
	public void clearInput()
	{
		num.clearInput();
		denom.clearInput();
		toIntegerMode();		
	}
	
	@Override
	public void keyP(int key)
	{	
		// Perform no action if neither of this component's input boxes are selected.
		if(!isSelected())
		{
			return;
		}
		
		forceKeyP(key);
	}

	// The actual key event logic.
	public void forceKeyP(int key)
	{		
		switch(key)
		{
			case KeyEvent.VK_SLASH  :
			case KeyEvent.VK_DIVIDE :

				if(mode == INTEGER_MODE)
				{
					toRationalMode();
					denom.toggle();
					return;
				}
				
				denom.toggle();
				return;
				
			case KeyEvent.VK_BACK_SPACE:
				
				boolean denom_empty = denom.toString().length() == 0;

				if(mode == RATIONAL_MODE &&
				   denom_empty)
				{
					toIntegerMode();
					num.toggle();
					
					// Do not allow erroneous backspace commands to go to the numerator.
					breakIteration();
				}
				
				break;
		}
	}
	
	public void toIntegerMode()
	{
		num.setH(getH() - SpriteLoader.gui_borderSize*2);
		mode = INTEGER_MODE;
	}
	
	public void toRationalMode()
	{
		num.setH(getH()/2 - SpriteLoader.gui_borderSize);
		mode = RATIONAL_MODE;
		
		// Add a multiplicative identity to the numerator box.
		if(num.getInputSize() == 0)
		{
			num.addChar('1');
			num.refreshText();
		}
	}
	
	@Override
	public void draw(ImageB i, AffineTransform AT)
	{
		super.draw(i, AT);
	}
	
	public boolean isSelected()
	{
		gui_focusBox selection = gui_focusBox.selection;
		
		// Perform no action if neither of this component's input boxes are selected.
		if(selection != num && selection != denom)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean input_changed()
	{
		
		boolean b1 = num.input_changed();
		boolean b2 = denom.input_changed();
		
		if(changed || b1 || b2)
		{
			changed = false;
			return true;
		}
	
		return false;

	}
	
	public boolean query_changed()
	{
		return changed || num.query_changed() || denom.query_changed();
	}	
	
	// -- FIXME A hacky function that allows for temporary text setting.
	public void setDefaultText(String str)
	{
		num.setDefaultText(str);
		changed = true;
	}
	
	public String toString()
	{
		return getInput().toString();
	}
	
	public void toggle()
	{
		if(mode == INTEGER_MODE)
		{
			num.toggle();
		}
		else
		{
			denom.toggle();
		}
	}
	
	public void setDrawBorders(boolean flag)
	{
		foreground.setDrawBorders(flag);
	}
	
	public gui_DecimalInput getNum()
	{
		return num;
	}
	
	public  gui_DecimalInput getDenom()
	{
		return denom;
	}
}