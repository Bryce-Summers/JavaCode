package SimpleEngine.interfaces;

import java.awt.Graphics;


public interface OBJ
{
	// Called once per frame.
	public abstract void draw(Graphics g);
	
	// Called once per update step. (Perhaps more often than drawing.)
	public abstract void update();

}