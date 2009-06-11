package files;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author kbok
 * Provides a simple way to create different images depending on the player's
 * color. It just holds pixel data and generates images.
 * This is necessary since Java BufferedImages does not permit to modify
 * the palette after creation.
 * IndexedImages accepts integers as indexes, but they all have to be within 
 * the range 0..255. That way you can use InputSource.readUnsignedByte() with
 * these images.
 */
public class IndexedImage {
	public static Palette DEFAULT_PALETTE;
	private int[][] data;
	private int hotspotX, hotspotY;
	
	/**
	 * @author kbok
	 * Provides a small extension of an integer array.
	 */
	public class Palette
	{
		public int[] contents;
		
		/**
		 * Creates a new empty palette. All values are set to zero.
		 */
		public Palette() {
			contents = new int[256];
		}
		
		/**
		 * Creates a new Palette object using the given data.
		 * @param c The data the palette will use.
		 */
		public Palette(int[] c){
			contents = c;
		}
	}
	
	/**
	 * Constructs a new IndexedImage using the given image data.
	 * @param img The array containing the image data.
	 */
	public IndexedImage(int[][] img)
	{
		if(DEFAULT_PALETTE == null)
		{
			try {
				loadDefaultPalette();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		data = img;
	}
	
	/**
	 * Constructs a new empty IndexedImage of the given dimensions. 
	 * @param width The width of the new image.
	 * @param height The hight of the new image.
	 */
	public IndexedImage(int width, int height)
	{
		if(DEFAULT_PALETTE == null)
		{
			try {
				loadDefaultPalette();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		data = new int[height][];
		for(int i=0; i<height; i++)
			data[i] = new int[width];
	}
	
	/**
	 * Sets the hotspot coordinates of the image, if there is one.
	 * The hotspots defines usually the centre of the image, which is not always
	 * the middle point of the image.
	 * @param x The X coordinate of the hotspot point.
	 * @param y The Y coordinate of the hotspot point.
	 */
	public void setHotspot(int x, int y)
	{
		hotspotX = x;
		hotspotY = y;
	}
	
	/**
	 * Returns the X coordinate of the hotspot point.
	 * @return The X coordinate of the hotspot point.
	 */
	public int getHotspotX(){
		return hotspotX;
	}
	
	/**
	 * Returns the Y coordinate of the hotspot point.
	 * @return The Y coordinate of the hotspot point.
	 */
	public int getHotspotY(){
		return hotspotY;
	}
	
	/**
	 * Sets the index value of the pixel at coordinates (x, y).
	 * @param x The column of the pixel to set.
	 * @param y The row of the pixel to set.
	 * @param pixel The value of the index.
	 */
	public void setPixel(int x, int y, int pixel)
	{
		data[y][x] = pixel;
	}
	
	/**
	 * Creates a BufferedImage using its own image data and the given palette.
	 * @param palette The palette to use to create the image.
	 * @return The BufferedImage generated.
	 */
	public BufferedImage withPalette(Palette palette)
	{
		/* TODO: Use Hotspot instead of dimensions */
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		int height = data.length;
		int width = data[0].length;
		for(int y=0; y<height; y++)
			for(int x=0; x<width; x++)
				img.setRGB(x + (64-width)/2, y + (64-height)/2, palette.contents[data[y][x]]);
		
		return img;
	}
	
	
	/**
	 * Loads the default palette from a	file in the resource folder
	 * @throws IOException If there is an error with the file
	 */
	private void loadDefaultPalette() throws IOException
	{
		RandomAccessFile paletteFile = new RandomAccessFile(Path.getPath().concat("maxr.pal"), "r");
		DEFAULT_PALETTE = new Palette();
		for(int i=0; i<256; i++)
		{
			byte r, g, b;
			r = paletteFile.readByte();
			g = paletteFile.readByte();
			b = paletteFile.readByte();
			DEFAULT_PALETTE.contents[i] = 0xff << 24 | r << 16 | g << 8 | b;
		}
		
		DEFAULT_PALETTE.contents[0] = 0;
	}
}
