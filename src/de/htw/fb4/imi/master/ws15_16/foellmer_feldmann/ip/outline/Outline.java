package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.outline;

public class Outline {
	
	private int[][] originalBinaryPixels;
	private int[][] erodedPixels;
	private int[][] outlinePixels;
	
	protected int width;
	protected int height;
	
	private int[][] structureElement;
	
	public Outline()
	{
		this.create4NeighboursStructureElement();
	}
	
	private void create4NeighboursStructureElement()
	{
		this.structureElement = new int[3][3];
		
		this.structureElement[0][0] = 0;
		this.structureElement[0][1] = 1;
		this.structureElement[0][2] = 0;
		this.structureElement[1][0] = 1;
		this.structureElement[1][2] = 1;
		this.structureElement[1][3] = 1;
		this.structureElement[2][0] = 0;
		this.structureElement[2][1] = 1;
		this.structureElement[2][3] = 0;
	}
	
	public int[][] getOriginalPixels() {
		System.out.println("test1: " + originalBinaryPixels.length);
		return originalBinaryPixels;
	}
	
	public void setOriginalBinaryPixels(int width, int height, int[] originalPixels) 
	{
		this.width = width;
		this.height = height;
		this.originalBinaryPixels = new int[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; x++) {				
				int pos = x * width + y;
				
				this.originalBinaryPixels[x][y] = originalPixels[pos];
			}
		}
	}
	
	public void setOriginalBinaryPixels(int[][] originalPixels) {
		this.originalBinaryPixels = originalPixels;
	}
	
	protected int[][] dilatePixel(int[][] structureElement, int binaryPixels[][])
	{
		int[][] dilatedPixels = new int[this.width][this.height];
		
		for (int i = 0; i < structureElement.length; i++) {
			for (int j = 0; j < structureElement.length; j++) {
				for (int x = 0; x < binaryPixels.length; x++) {
					for (int y = 0; y < binaryPixels.length; y++) {
						dilatedPixels[i + x][j + y] = 1;
					}
				}
			}
		}
		
		return dilatedPixels;
	}
	
	protected int[][] invertPixels(int pixels[][]) {
		int[][] invertedPixels = new int[pixels.length][pixels[0].length];
		
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels.length; j++) {
				if (pixels[i][j] == 0){
					invertedPixels[i][j] = 1;
				} else {
					invertedPixels[i][j] = 0;
				}
			}
		} 
		
		return invertedPixels;
	}
	
	private int[][] reflectPixels(int[][] pixels) {
		int[][] reflectedPixels = new int[pixels.length][pixels[0].length];
		
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels.length; j++) {
				reflectedPixels[j][i] = pixels[i][j];
			}
		} 
		
		return null;
	}

	/**
	 *  Take original pixel, do erosion
	 */
	protected void erodePixel() 
	{
		int[][] inverted = this.invertPixels(this.originalBinaryPixels);
		int[][] reflectedStructure = this.reflectPixels(this.structureElement);
		
		this.erodedPixels = this.invertPixels(
				this.dilatePixel(reflectedStructure, inverted));	
	}	

	protected void outlinePixel()
	{
		// Take eroded pixels, intersect with original ones -> intersection is the outline
		this.outlinePixels = new int[this.erodedPixels.length][this.erodedPixels[0].length];
		
		for (int i = 0; i < erodedPixels.length; i++) {
			for (int j = 0; j < erodedPixels.length; j++) {
				if (this.erodedPixels[i][j] == this.originalBinaryPixels[i][j]) {
					this.outlinePixels[i][j] = this.erodedPixels[i][j];
				}
			}
		}
	}
	
	private void ensureThatOriginalWasSet() {
		if (null == this.originalBinaryPixels) {
			throw new IllegalStateException("originalPixels wasn't set. Please set it before calling executeOutline().");
		}		
	}
	
	public int[][] executeOutline()
	{
		this.ensureThatOriginalWasSet();
		
		this.erodePixel();
		this.outlinePixel();
		
		return this.outlinePixels;
	}

	public int[] getFlatArray(int width, int height, int[][] pixels) {
		int[] flat = new int[width * height];
		
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				int pos = i * width + j;
				flat[pos] = pixels[i][j];
			}
		}
		
		return flat;
	}
}
