/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold;

/**
 * This class capsulates the IsoData main algorithm. It solves the task of finding an appropriate threshold.
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 20.10.2015
 */
public class IsoData implements ThresholdFindingAlgorithm {

	public static final int DEFAULT_START_VALUE = 128;
	/**
	 * Start value (tO). Default is 128.
	 */
	private int startValue = DEFAULT_START_VALUE;
	
	
	public int getStartValue() {
		return startValue;
	}


	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}


	@Override
	public int calculateThreshold() {
		
		return 0;
	}

}
