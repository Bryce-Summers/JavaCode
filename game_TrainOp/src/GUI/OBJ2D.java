package GUI;

import SimpleEngine.interfaces.OBJ;

public abstract class OBJ2D implements OBJ
{
	protected int x;
	protected int y;
	
	// -- Constructor.
	public OBJ2D(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public abstract int getW();
	public abstract int getH();
	
	// It is important to maintain the original hash function.
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	// REQURIES : x and y in the same coordinate space as this object.
	public boolean mouseCollision(int x_in, int y_in)
	{
		return 	x_in >= x &&
				x_in < x + getW() &&
				y_in >= y &&
				y_in < y + getH();
	}
	
	// Interface for getting information about x and y.
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
