package Components;

public class Material
{
	
	public enum Bounce_Type{DIFFUSE, SPECULAR, TRANSMISSION};
	
	// Colors
	public final photonColor diffuse;
	public final photonColor specular;
	public final photonColor transmission;
	
	public final double refractive_index;
	public final double shininess;
	
	
	// Statistics.
	
	// The thresholds for a random() call to produce the given bounce type.
	private double prob_diffuse;
	private double prob_specular;
	//private double prob_transmission;
	
	
	public Material(
		double refractive_index,
		double shininess,
		photonColor coef_diffuse,
		photonColor coef_specular,
		photonColor coef_transmission)
	{
		this.refractive_index = refractive_index;
		this.shininess = shininess;
			
		this.diffuse = coef_diffuse;
		this.specular = coef_specular;
		this.transmission = coef_transmission;
		
		createStatistics();
	}
	
	public Material(
			double refractive_index,
			double shininess,
			photonColor coef_diffuse,
			photonColor coef_specular)
	{
		this.refractive_index = refractive_index;
		this.shininess = shininess;
				
		this.diffuse  = coef_diffuse;
		this.specular = coef_specular;
		this.transmission = photonColor.BLACK;
		
		createStatistics();
	}

	private void createStatistics()
	{
		double mag_diffuse = diffuse.getMagnitude();
		double mag_specular = specular.getMagnitude();
		double mag_transmissive = transmission.getMagnitude();
		
		double sum = mag_diffuse + mag_specular + mag_transmissive;
		
		prob_diffuse = mag_diffuse / sum;
		prob_specular = prob_diffuse + mag_specular / sum;
		//prob_transmission = prob_specular + mag_transmissive / sum;		
		
	}
	
	public boolean isDiffuse()
	{
		return diffuse.nonZero();		
	}
	
	public boolean isSpecular()
	{
		return specular.nonZero();		
	}
	
	public boolean isTransmissive()
	{
		return transmission.nonZero();		
	}
	
	// Returns a probability weighted photon bounce event for this material's properties.
	public Bounce_Type getWeightedBounceEvent()
	{
		
		double val = Math.random();
		
		if(val < prob_diffuse)
		{
			return Bounce_Type.DIFFUSE;
		}
		else if(val < prob_specular)
		{
			return Bounce_Type.SPECULAR;
		}
		else
		{
			return Bounce_Type.TRANSMISSION;
		}
			
	}
}
