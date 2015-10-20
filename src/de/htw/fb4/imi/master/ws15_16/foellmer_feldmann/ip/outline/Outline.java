package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.outline;

public class Outline {
	
	private int[] originalPixels;
	private int[] erodedPixels;
	private int[] outlinePixels;
	
	public int[] getOriginalPixels() {
		return originalPixels;
	}
	
	public void setOriginalPixels(int[] originalPixels) {
		this.originalPixels = originalPixels;
	}

	protected void erodePixel() 
	{
		// Take original pixel, do erosion
	}
	
	protected void outlinePixel()
	{
		// Take eroded pixels, do outlining
	}
	
	private void ensureThatOriginalWasSet() {
		if (null == this.originalPixels) {
			throw new IllegalStateException("originalPixels wasn't set. Please set it before calling executeOutline().");
		}		
	}
	
	public int[] executeOutline()
	{
		this.ensureThatOriginalWasSet();
		
		this.erodePixel();
		this.outlinePixel();
		
		return this.outlinePixels;
	}
}
