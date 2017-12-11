package components;

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
	
	// this = min(this + add/scale, 1);
	public void scaleAddBoundIrradiance(photonIrradiance add, photonIrradiance scale)
	{
		/*
		this.red   = Math.min(this.red   + add.red/  scale.red,   1);
		this.green = Math.min(this.green + add.green/scale.green, 1);
		this.blue  = Math.min(this.blue  + add.blue/ scale.blue,  1);
		 */
		
		double scale_val = Math.max(Math.max(scale.red,  scale.green), scale.blue);
		
		this.red   = this.red   + add.red/  scale_val;
		this.green = this.green + add.green/scale_val;
		this.blue  = this.blue  + add.blue/ scale_val;
		
	}
}