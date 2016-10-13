package BryceSound;

/*
 * Spectral Proccessor Class.
 * Written By Bryce Summers on 3/14/2016.
 * 
 * This class takes in arrays of bytes, fourier transforms them, manipulates their spectrums, 
 * and then returns modified arrays of bytes.
 * 
 * We use a windowing function to get rid of the interval discontinuity noise introduced while FFTing.
 * I still need to figure out how to get the original signal back.
 */

public class SpectralProccessor {
	
	// These guys are only used as intermediaries while doing the transform
	// and inverse fast fourier transform.
	double[] data_real;
	double[] data_imag;
	
	// These are arrays of values that we will actually manipulate.
	// such as the frequency magnitudes in the data mag buffer.
	double[] data_mag;
	double[] data_phase;
	
	double[] window;
	
	// -- Loading and Ejection functions.
	// These allow the data come in and out of this Spectral Proccessor.
	
	public void load(byte[] data, int start, int end)
	{
		// Compute the next highest integer power of 2.
		int len = end - start;
		int N = len;
		N--;
		N |= N >> 1;
		N |= N >> 2;
		N |= N >> 4;
		N |= N >> 8;
		N |= N >> 16;
		//N |= N >> 32;
		N++;
		
		make_window(len);
		
		// load len's worth of data into our double arrays.
		data_real = new double[N];
		data_imag = new double[N];
		for(int i = 0; i < len; i++)
		{
			// bytes converted to doubles between 0 and 1.
			data_real[i] = data[i + start]*1.0 / Byte.MAX_VALUE*window[i];
			data_imag[i] = 0.0;
		}
		
		// Pad the buffer up to an integer power of 2.
		for(int i = len; i < N; i++)
		{
			data_real[i] = 0.0;
			data_imag[i] = 0.0;
		}
		
		FFT.FFT(data_real,  data_imag);
		
		data_mag = new double[N];
		data_phase = new double[N];
		
		for(int k = 0; k < N; k++)
		{
			double r = data_real[k];
			double i = data_imag[k];
			data_mag[k] = Math.sqrt(r*r + i*i);
			data_phase[k] = Math.atan2(i, r);
		}
	}
	
	// Creates a window of the given size.
	public void make_window(int n)
	{

		
		window = new double[n];
		for(int i = 0; i < window.length; i++)
		{
			// Bryce Cosine window.
			window[i] = .5 - .5*Math.cos(i*Math.PI*2/window.length);

			// blackman window:
					/*0.42 - 0.5 * Math.cos(2*Math.PI*i/(n - 1)) 
						+ 0.08 * Math.cos(4*Math.PI*i/(n - 1));*/
		}
	}
	
	// Ejects an array of bytes.
	public void eject_b(byte[] out, int start, int end)
	{
		// -- Convert from (mag, phase) back to (real, imaginary).		
		int len = end - start;
		int N = data_mag.length;
		for(int i = 0; i < N; i++)
		{
			double R = data_mag[i];
			double theta = data_phase[i];
			data_real[i] = R*Math.cos(theta);
			data_imag[i] = R*Math.sin(theta);
		}
		
		FFT.iFFT(data_real,  data_imag);

		
		// Convert back to bytes.
		for(int i = 0; i < len; i++)
		{
			out[i + start] = (byte)(data_real[i]*Byte.MAX_VALUE);
		}
	}


	// -- Spectrum processing functions.
	// REQUIRES: power in [0, 1)
	public void setFreq(int i, double power)
	{
		if(i < data_mag.length)
		data_mag[i] = power;
	}

	public double getPower(int i)
	{
		return data_mag[i];
	}
	
	// Assumes a normal digital sound signal with a spectrum that falls off.
	// Returns the index of the Fundamental frequency.
	public int getFundamental()
	{
		double max = 0.0;
		int index = -1;
	
		for(int i = 0; i < data_mag.length; i++)
		{
			if(data_mag[i] > max)
			{
				max = data_mag[i];
				index = i;
			}			
		}
		
		double threshold = max * .9;
		
		// Return first peak above the threshold.
		for(int i = 0; i < data_mag.length; i++)
		{
			if(data_mag[i] > threshold)
			{				
				return i;
			}			
		}
		
		
		return index;
	}
	
	public void isolate_frequency(int index)
	{
		for(int i = 0; i < data_mag.length; i++)
		{
			data_mag[i] = i == index 
						  ? data_mag[i]
						  : 0;
		}
	}

	// -- Specific processing that we will do to the input.
	public void proccess_bryce1(byte[] tempBuffer, int start, int end)
	{
		load(tempBuffer, start, end);
		
		// Fundamental Frequency.
		int F1 = getFundamental();

		//isolate_frequency(F1);
		
    	double mag = getPower(F1);
    	int harmonics = 5;
    	int Fi = F1;

		//setFreq(F1, mag);
    	//System.out.println(F1);
    	
    	//setFreq(0, 100);



    	int len = end - start;

    	for(int i = 0; i < len; i++)
    	{
    		//setFreq(i, 0.0);
    		//System.out.println(data_mag[i]);
    	}

    	//setFreq(0, 10);
    	//data_phase[0] = 0.0;

    	for(int i = len/2; i < len; i++)
    	{
    		//setFreq(i, 0);
    	}

    	/*
    	for (int i = 0; i < harmonics; i++)
    	{
    		setFreq(Fi, mag);
    		Fi *= 2;
    		mag *= .5;
    	}
    	*/

    	// Load the processed bytes back into the input buffer.
    	eject_b(tempBuffer, start, end);
	}

}