package Game_Engine.GUI.Components.small;

import static BryceMath.Calculations.Colors.weightedAverageColor;

import java.awt.Color;

import BryceMath.Calculations.Colors;
import BryceMath.Geometry.Rectangle;
import Game_Engine.GUI.Components.communication.gui_hoverText;
import Game_Engine.GUI.Interfaces.Pingable;
import Game_Engine.GUI.Sprites.StyleSpec;

// Button class.
// Written by Bryce Summers 7/26/2012
// Purpose: This object is a genaric class to display stylish rectangles with or without text onto the screen.

// FIXME: Update this esoteric code.

public class gui_button extends gui_label implements Pingable
{
	// -- Private data.
	
	// -- Buttons are Pingable
	private boolean flag;
	private boolean clickedOn = false;
	
	// -- private Glowing variables.
	private boolean glow;
	private int 	glowPhase	= 0;
	private Color 	glowOn  	= Color.gray;
	private Color	glowOff		= Color.white;
	private int 	glowSpeed	= 10;
	
	private Color C_clickedOn = Colors.C_GRAY3;
	
	// Info fields.
	private static gui_hoverText INFO_LABEL = new gui_hoverText(0, 0, 128, 32);
	private String myInfoText = null;	
	
	
	public gui_button(double x, double y, int w, int h)
	{
		super(x, y, w, h);
		iVars();
	}
	
	public gui_button(Rectangle r)
	{
		super(r);
		iVars();
	}

	private void iVars()
	{
		setRestingColor(StyleSpec.C_BUTTON_UP);
		setDrawBorders(true);
	}

	// FIMXE : This update loop is rather illegible.
	@Override
	public void update()// This is the part that updates the label's background color.
	{		
		super.update();
	
		if(mouseInRegion && myInfoText != null && INFO_LABEL.getOBj() != this)
		{
			INFO_LABEL.setObj(this, myInfoText);
		}
		
		// Maintain a constant color if the user is selecting this button.
		if((mouseInRegion || highlighted) && isEnabled())
		{
			if(clickedOn)
			{
				setColor(C_clickedOn);
			}
			else
			{
				setColor(Color_mouse_in_region());
			}
		}
		else
		{
			// Else this button can be told to glow to highlight it for a user.
			if(isGlowing())
			{

				// Integer overflow safe cyclic glowing.
				glowPhase += glowSpeed;
				glowPhase = glowPhase % 360;
				
				// Cycle between the colors
				
				// FIXME : Utilize the Interpolator,
				// try to make these arcane mathematics computations more elegant.
				
				double glowOnPercentage =  .5 + .5*Math.sin(Math.toRadians(glowPhase));
				
				setColor(weightedAverageColor(glowOn, glowOff, glowOnPercentage));
				
			}
			else
			{
				revertColor();
			}
		}
	}
	
	@Override
	public void mouseP(int mx, int my)
	{
		// Do not allow clicking if this button is disabled.
		// Clicking is only with the left mouse button.
		if(!isEnabled() || !mouse_left())
		{
			return;
		}
		
		clickedOn = true;
	}
	
	// Clicking on a button will set this button's flag to true.
	// The object that created this button, or some sort of button checking object needs to check for this flag's status,
	// perform the necessary action, and set it back to false.
	@Override
	public void mouseR(int a, int b)
	{
		if(clickedOn)
		{
			flag = true;
		}
	}

	
	@Override
	public void global_mouseR()
	{
		clickedOn = false;
	}
	
	@Override
	public boolean flag()
	{
		if(flag)
		{
			flag = false;
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setFlag(boolean input)
	{
		flag = input;
	}

	public void setGlow(boolean glow)
	{
		this.glow = glow;
	}
	
	public void flash()
	{
		this.glow = true;
	}

	public boolean isGlowing()
	{
		return glow;
	}
	
	public void flash(int speed)
	{
		this.glow = true;
		glowSpeed = speed;
	}
	
	// Stop the glowing for disabled buttons.
	public void disable()
	{
		super.disable();
		setGlow(false);
	}
	
	// Stop the glowing for invisible buttons.
	public void setVisible(boolean flag)
	{
		super.setVisible(flag);
		if(!flag)
		{
			setGlow(false);
		}
	}
	
	// -- Colors representing certain states:
	// These should be overriden by subtypes to specify Aestetic features.
	
	// Returns the color for when the mouse hovers over this button.
	protected Color Color_mouse_in_region()
	{
		return StyleSpec.C_BUTTON_DOWN;
	}
	
	// look and feels can all declare info texts.
	public void INFO(String str)
	{
		myInfoText = str;
	}


}
