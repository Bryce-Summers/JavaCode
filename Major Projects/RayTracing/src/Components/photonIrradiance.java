package Components;

/*
 * Kind of like a photonColor, except it does not hide the values from the user.
 */

public class photonIrradiance
{

	public double red;
	public double green;
	public double blue;
	
	// Default constructor.
	public photonIrradiance()
	{
		red   = 0.0;
		green = 0.0;
		blue  = 0.0;
	}
	
	public photonIrradiance(double red, double green, double blue)
	{
		this.red   = red;
		this.green = green;
		this.blue  = blue;
	}
	
	// Sets this object's values to be the maximum of this object and the other object's values.
	public void maximize(photonIrradiance val)
	{
		red   = Math.max(red, val.red);
		green = Math.max(green, val.green);
		blue  = Math.max(blue, val.blue);
	}
	
	public photonColor toPhotonColor()
	{
		return new photonColor(this);
	}
	
}
