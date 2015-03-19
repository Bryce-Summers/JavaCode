package GUI;

import java.awt.image.BufferedImage;

import SimpleEngine.interfaces.MouseInput;

public class Button extends TextBox implements MouseInput
{

	public final int NORMAL = 0;
	public final int HOVER = 1;
	public final int CLICKED_ON_NO_HOVER = 2;
	public final int CLICKED_ON_HOVER = 3;
	
	private Button clickedOn = null;
	
	public Button(int x, int y, String str, BufferedImage normal, BufferedImage hover,
											BufferedImage clicked_on_no_hover, BufferedImage clicked_on_hover)
	{
		super(x, y, str, normal, hover, clicked_on_no_hover, clicked_on_hover);
	}

	public void mouseM(int x, int y)
	{
	}
	
	public void mouseP(int x, int y)
	{
		clickedOn = this;
		setImage(CLICKED_ON_HOVER);
	}
	
	public void mouseR(int x, int y)
	{
		if(clickedOn == this)
		{
			clickedOn = null;
			action();			
		}
		
		setImage(HOVER);
	}

	@Override
	public void global_mouseP() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void global_mouseR()
	{
		setImage(NORMAL);
		clickedOn = null;
	}

	@Override
	public void global_mouseD(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void global_mouseM(int x, int y) {

		if(mouseCollision(x, y))
		{
			setImage(clickedOn == this ? CLICKED_ON_HOVER : HOVER);
		}
		else
		{
			setImage(clickedOn == this ? CLICKED_ON_NO_HOVER : NORMAL);
		}
		
		
	}

	@Override
	public void global_mouseScroll(int scroll) {
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
	
	// FIXME deal with what the action is,
	public void action() {
	}
}
