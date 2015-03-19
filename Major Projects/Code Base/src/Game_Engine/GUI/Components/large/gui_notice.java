package Game_Engine.GUI.Components.large;

import BryceMath.Geometry.Rectangle;
import Game_Engine.Engine.Objs.Obj_union;
import Game_Engine.GUI.Components.small.gui_paragraph;
import Game_Engine.GUI.Components.small.buttons.gui_destroyButton;

public class gui_notice extends Obj_union
{
	
	// -- Private Data.
	gui_destroyButton button;
	gui_paragraph paragraph;

	// -- Constructor.
	public gui_notice(double x, double y, int w, int h)
	{
		super(x, y, w, h);
	}

	public gui_notice(Rectangle bounds)
	{
		super(bounds);
	}

	@Override
	public void iObjs()
	{
		button    = new gui_destroyButton(getW() - 50 + getX(), getY(), 50, 50);
		obj_create(button);
		paragraph = new gui_paragraph(	  getX(), getY() + 50, 				getW(), getH());
		obj_create(paragraph);
	}
	
	public void setText(String text)
	{
		paragraph.setText(text);
	}

}
