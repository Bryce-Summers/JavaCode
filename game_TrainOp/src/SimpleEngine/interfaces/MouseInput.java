package SimpleEngine.interfaces;

public interface MouseInput
{
	// REQURIES : x and y in the same coordinate space as this object.
	public boolean mouseCollision(int x_in, int y_in);
	
	public void global_mouseP();
	public void global_mouseR();
	public void global_mouseD(int x, int y);
	public void global_mouseM(int x, int y);
	public void global_mouseScroll(int scroll);
	
	public void mouseP(int x, int y);
	public void mouseR(int x, int y);
	public void mouseD(int x, int y);
	public void mouseM(int x, int y);
	public void mouseScroll(int x, int y, int scroll);
	
	
}
