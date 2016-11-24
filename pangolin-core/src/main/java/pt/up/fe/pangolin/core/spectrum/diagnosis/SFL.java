package pt.up.fe.pangolin.core.spectrum.diagnosis;

import java.util.Arrays;

import pt.up.fe.pangolin.core.spectrum.Spectrum;

public class SFL {

	public static double[] diagnose(Spectrum s, boolean[] errorVector) {
		double[] diagnosis = new double[s.getComponentsSize()];
		Arrays.fill(diagnosis, 0);
		
		for (int c = 0; c < s.getComponentsSize(); c++) {
			double n[][] = new double[][]{{0,0},{0,0}};
			
			for (int t = 0; t < errorVector.length; t++) {
				boolean isError = errorVector[t];
				boolean isInvolved = s.isInvolved(t, c);
				
				n[isInvolved ? 1 : 0][isError ? 1 : 0] += 1.0;
			}
			
			diagnosis[c] = ochiai(n);
		}
		
		return diagnosis;
	}

	private static double ochiai(double[][] n) {
		double denom = Math.sqrt(n[1][1] + n[0][1]) * Math.sqrt(n[1][1] + n[1][0]);
		if (denom == 0) {
			return 0;
		}
		return n[1][1] / denom;
	}
	
}
