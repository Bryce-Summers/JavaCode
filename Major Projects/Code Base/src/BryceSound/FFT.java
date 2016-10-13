package BryceSound;

/*
 * Fast Fourier Transform by Bryce Summers.
 * Written on 3/14/2016.
 */

public class FFT
{

	// Phases are computed by cos and sin probes
	// and identity cos(phase) + sin(phase) = 1.
	// Inverse transform = conj(FFT(conj)).
	// There are many tricks that could be used to speed up this implementation.
	// and the memory allocation and copying is horrendous.
	
	public static void FFT(double[] real, double [] imag)
	{
		assert(real.length == imag.length);
		int N = real.length;
		
		if(N == 1){	return;}
		
		assert((N & (N - 1)) == 0);// N needs to be an integer power of 2.
		
		int hN = N/2; // Half N. 
		
		double[] real_even = new double[hN];
		double[] real_odd  = new double[hN];
		double[] imag_even = new double[hN];
		double[] imag_odd  = new double[hN];

		// Divide the arrays up by even and odd indices.
		for(int i = 0; i < hN; i++)
		{
			real_even[i] = real[2*i];
			real_odd [i] = real[2*i + 1];
			imag_even[i] = imag[2*i];
			imag_odd [i] = imag[2*i + 1];
		}
		
		// Solve both sub FFT problems.
		FFT(real_even, imag_even);
		FFT(real_odd, imag_odd);
		
		for(int i = 0; i < hN; i++)
		{
			double theta = -2*Math.PI*i/N;
			
			// (A + Bi) * (C + Di).
			// (AC - BD) + (BC + AD)i.
			double A = Math.cos(theta);
			double B = Math.sin(theta);
			double C = real_odd[i];
			double D = imag_odd[i];
			
			double real_t = A*C - B*D;
			double imag_t = B*C + A*D;
			
			real[i] = real_even[i] + real_t;
			imag[i] = imag_even[i] + imag_t;
			real[i + hN] = real_even[i] - real_t;
			imag[i + hN] = imag_even[i] - imag_t;
		}
		
	}
	
	public static void iFFT(double[] real, double [] imag)
	{
		// Conjugate the input.
		int N = real.length;
		for(int i = 0; i < N; i++)
		{
			imag[i] *= -1;
		}
		
		FFT(real, imag);
		
		// Conjugate the scale the output.
		double scale = 1.0/N;
		for(int i = 0; i < N; i++)
		{
			imag[i] *= -scale;
			real[i] *=  scale;
		}
	}
}