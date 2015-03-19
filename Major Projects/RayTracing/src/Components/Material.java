package Components;

public class Material
{
	// Colors
	public final photonColor diffuse;
	public final photonColor specular;
	
	public final double refractive_index;
	public final double shininess;	
	
	public Material(
		double refractive_index,
		double shininess,
		photonColor coef_diffuse,
		photonColor coef_specular)
	{
		this.refractive_index = refractive_index;
		this.shininess = shininess;
			
		this.diffuse = coef_diffuse;
		this.specular = coef_specular;
	}
}
