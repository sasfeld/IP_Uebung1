package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.outline;

public class Outline {

	private int[][] originalBinaryPixels;
	private int[][] erodedPixels;
	private int[][] outlinePixels;

	protected int width;
	protected int height;

	private int[][] structureElement;

	public Outline() {
		this.create4NeighboursStructureElement();
	}

	private void create4NeighboursStructureElement() {
		this.structureElement = new int[3][3];

		this.structureElement[0][0] = 0;
		this.structureElement[0][1] = 0xff000000;
		this.structureElement[0][2] = 0;
		this.structureElement[1][0] = 0xff000000;
		this.structureElement[1][1] = 0xff000000;
		this.structureElement[1][2] = 0xff000000;
		this.structureElement[2][0] = 0;
		this.structureElement[2][1] = 0xff000000;
		this.structureElement[2][2] = 0;
	}

	public int[][] getOriginalPixels() {
		return originalBinaryPixels;
	}

	public void setOriginalBinaryPixels(int width, int height, int[] originalPixels) {
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

	public void setOriginalBinaryPixels(int[][] originalPixels) {
		this.originalBinaryPixels = originalPixels;
	}

	public int[][] executeOutline() {
		this.ensureThatOriginalWasSet();

		this.erodePixel();
		this.outlinePixel();

		return this.outlinePixels;
	}

	protected int[][] dilatePixel(int[][] structureElement, int binaryPixels[][]) {
		int[][] dilatedPixels = new int[this.width][this.height];
		
		for (int i = -1; i < structureElement.length - 1; i++) {
			for (int j = -1; j < structureElement[i + 1].length - 1; j++) {
				for (int x = 0; i + x < binaryPixels.length - 1; x++) {
					for (int y = 0; j + y < binaryPixels[x].length - 1; y++) {
						if (structureElement[i + 1][j + 1] == 0xff000000
								&& i + x >= 0
								&& j + y >= 0
								&& binaryPixels[x][y] == structureElement[i + 1][j + 1]
								) {
							dilatedPixels[i + x][j + y] = structureElement[i + 1][j + 1];
						}
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
				invertedPixels[i][j] = pixels[i][j] == 0xff000000 ? 0xffffffff : 0xff000000;
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
	 * Take original pixel, do erosion
	 */
	protected void erodePixel() {
		int[][] invertedOriginal = this.invertPixels(this.originalBinaryPixels);
		int[][] reflectedStructureElement = this.reflectPixels(this.structureElement);

		this.erodedPixels = this.invertPixels(this.dilatePixel(reflectedStructureElement, invertedOriginal));
	}

	protected void outlinePixel() {
		// Take eroded pixels, intersect with original ones -> intersection is
		// the outline
		int[][] invertedEroded = this.invertPixels(this.erodedPixels);
		this.outlinePixels = new int[this.erodedPixels.length][this.erodedPixels[0].length];

		for (int i = 0; i < invertedEroded.length; i++) {
			for (int j = 0; j < invertedEroded[i].length; j++) {
				if (invertedEroded[i][j] == this.originalBinaryPixels[i][j]) {
					this.outlinePixels[i][j] = invertedEroded[i][j];
				} else {
					this.outlinePixels[i][j] = 0xffffffff;
				}
			}
		}
	}

	private void ensureThatOriginalWasSet() {
		if (null == this.originalBinaryPixels) {
			throw new IllegalStateException(
					"originalPixels wasn't set. Please set it before calling executeOutline().");
		}
	}

	private int calc1DPosition(int width, int x, int y) {
		int pos = y * width + x;
		return pos;
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
