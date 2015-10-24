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
		this.structureElement[1][1] = 1;
		this.structureElement[1][2] = 1;
		this.structureElement[2][0] = 0;
		this.structureElement[2][1] = 1;
		this.structureElement[2][2] = 0;
	}
	
	public int[][] getOriginalPixels() {
		return originalBinaryPixels;
	}
	
	public void setOriginalBinaryPixels(int width, int height, int[] originalPixels) 
	{
		this.width = width;
		this.height = height;
		this.originalBinaryPixels = new int[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {				
				int pos = calc1DPosition(width, x, y);
				
			
				this.originalBinaryPixels[x][y] = originalPixels[pos];			
			}
		}
	}

	private int calc1DPosition(int width, int x, int y) {
		int pos = y * width + x;
		return pos;
	}
	
	public void setOriginalBinaryPixels(int[][] originalPixels) {
		this.originalBinaryPixels = originalPixels;
	}
	
	protected int[][] dilatePixel(int[][] structureElement, int binaryPixels[][])
	{
		int[][] dilatedPixels = new int[this.width][this.height];
		
		for (int i = 0; i < structureElement.length; i++) {
			for (int j = 0; j < structureElement[i].length; j++) {
				for (int x = 0; i + x < binaryPixels.length; x++) {
					for (int y = 0; j + y < binaryPixels[x].length; y++) {
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
			for (int j = 0; j < pixels[i].length; j++) {
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
			for (int j = 0; j < pixels[i].length; j++) {
				reflectedPixels[j][i] = pixels[i][j];
			}
		} 
		
		return reflectedPixels;
	}

	/**
	 *  Take original pixel, do erosion
	 */
	protected void erodePixel() 
	{
		int[][] invertedOriginal = this.invertPixels(this.originalBinaryPixels);
		int[][] reflectedStructureElement = this.reflectPixels(this.structureElement);
		
		this.erodedPixels = this.invertPixels(
				this.dilatePixel(reflectedStructureElement, invertedOriginal));	
	}	

	protected void outlinePixel()
	{
		// Take eroded pixels, intersect with original ones -> intersection is the outline
		this.outlinePixels = new int[this.erodedPixels.length][this.erodedPixels[0].length];
		
		for (int i = 0; i < erodedPixels.length; i++) {
			for (int j = 0; j < erodedPixels[i].length; j++) {
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
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pos = calc1DPosition(width, i, j);
				flat[pos] = pixels[i][j];
			}
		}
		
		return flat;
	}
}
