package files;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Contains tools and routines used to read a map file (.wrl) and to represent it.
 * @author kbok
 */
public class Map {
	private RandomAccessFile f;
	private int[] palette;
	private BufferedImage[] terrains;
	private int[] terrains_types;
	private BufferedImage[][] data;
	private int[][] types;
	private int width, height;
	
	public static final int TYPE_NORMAL	 = 0;
	public static final int TYPE_WATER 	 = 1;
	public static final int TYPE_COAST 	 = 2;
	public static final int TYPE_BLOCKED = 3;
	
	/**
	 * Creates a new Map object from the file which name is given.
	 * @param name The name of the map file to load.
	 * @throws FileNotFoundException If the file cannot be found in the resource directory.
	 */
	public Map(String name) throws FileNotFoundException {
		try{
			f = new RandomAccessFile(name, "r");
		}catch(FileNotFoundException e)
		{
			f = new RandomAccessFile(Path.getPath().concat("maps/").concat(name), "r");
		}
		palette = new int[256];
	}
	
	private void readSize() throws IOException
	{
		f.seek(5);
		width = LittleEndianIO.readShort(f);
		height = LittleEndianIO.readShort(f);
		
		data = new BufferedImage[width][height];
		types = new int[width][height];
		
	}
	
	/**
	 * Returns the minimap image read from the map file.
	 * @return An Image object containing the minimap.
	 * @throws IOException Error reading from the file.
	 */
	public Image getMiniMap() throws IOException
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		f.seek(9);
		
		for(int y=0; y<height; y++)
			for(int x=0; x<width; x++)
			{
				int b = f.readUnsignedByte();
				img.setRGB(x, y, palette[b]);
			}

		
		return img;
	}
	
	private void readPalette() throws IOException
	{
		f.seek(9+width*height*3);
		short nTerrains = LittleEndianIO.readShort(f);
		f.seek(11+width*height*3+nTerrains*64*64);
		
		for(int i=0; i<256; i++)
		{
			int r = f.readUnsignedByte();
			int g = f.readUnsignedByte();
			int b = f.readUnsignedByte();
			palette[i] = r<<16 | g<<8 | b;
		}
		
	}
	
	private void readTerrains() throws IOException
	{
		f.seek(9+width*height*3);
		short nTerrains = LittleEndianIO.readShort(f);
		terrains = new BufferedImage[nTerrains];
		terrains_types = new int[nTerrains];
		
		for(int i=0; i<nTerrains; i++)
		{
			f.seek(11+width*height*3+i*64*64);
			terrains[i] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
			
			for(int y=0; y<64; y++)
				for(int x=0; x<64; x++)
				{
					terrains[i].setRGB(x, y, palette[f.readUnsignedByte()]);
				}
		}
	}
	
	private void readData() throws IOException
	{
		f.seek(9+width*height*3);
		short nTerrains = LittleEndianIO.readShort(f);
		
		/* Read terrains types */
		f.seek(11+width*height*3+nTerrains*64*64+3*256);
		
		for(int i=0; i<nTerrains; i++)
				terrains_types[i] = f.readUnsignedByte();
		
		/* Read map data */
		f.seek(9+width*height);
		
		/* The data is unsigned but it's OK since the amount of terrains won't
		 * exceed 32k
		 */
		for(int y=0; y<height; y++)
			for(int x=0; x<width; x++)
			{
				int id = LittleEndianIO.readShort(f);
				data[x][y] = terrains[id];
				types[x][y] = terrains_types[id];
			}
	}
	
	/**
	 * Returns the tile image at the given coordinates.
	 * @param x The X Coordinate of the tile
	 * @param y The Y coordinate of the tile
	 * @return A 64x64 Image containing the tile.
	 */
	public BufferedImage getTile(int x, int y)
	{
		try{
			return data[x][y];
		}catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	/**
	 * Returns the terrain type of the given tile, e.g. coast/ground/water
	 * @param x The X Coordinate of the tile
	 * @param y The Y coordinate of the tile
	 * @return The terrain type, as an integer.
	 */
	public int getTerrainType(int x, int y)
	{
		return types[x][y];
	}
	
	/**
	 * Reads data from the file.
	 * @throws IOException In case of error reading the file.
	 */
	public void readFully() throws IOException
	{
		readSize();
		readPalette();
		readTerrains();
		readData();
	}
}
