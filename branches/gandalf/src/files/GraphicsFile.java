package files;

import java.awt.Graphics;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author kbok
 * Provides implementations of the routines necessary for reading the MAX.RES
 * file.
 */
public class GraphicsFile {
	private RandomAccessFile file;
	private int off_index;
	private Vector<Slice> index;

	/**
	 * @author kbok
	 * Represents a file in the archive file. They are represented by an offset
	 * and a length, and each of them are identified by a 8-byte name.
	 */
	public class Slice
	{
		byte name[];
		int offset;
		int size;

		/**
		 * Creates a new Slice using the given name, offset, and length.
		 * @param n An 8-byte name string.
		 * @param o The offset inside the MAX.RES file.
		 * @param s The length of the file.
		 */
		public Slice(byte[] n, int o, int s)
		{
			name = n;
			offset = o;
			size = s;
		}

		/**
		 * Compares the name of the slice with another's. It is useful for
		 * loading named resources.
		 * @param b The name to compare the slice to.
		 * @return Whether the names are equals or not.
		 */
		public boolean nameEquals(byte[] b)
		{
			for(int i=0; i<8 && i<b.length; i++)
				if(b[i] != name[i]) return false;
			return true;
		}

		/**
		 * Returns its name, as a String object.
		 * @return The name of the Slice.
		 */
		public String getNameString()
		{
			String s = null;

			try {
				s = new String(name, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return s;
		}
	}

	/**
	 * Creates a new GraphicsFile using the given already-created
	 * RandomAccessFile referencing MAX.RES.
	 * @param f The MAX.RES File Object.
	 */
	public GraphicsFile(RandomAccessFile f)
	{
		file = f;
		index = new Vector<Slice>();
	}

	/**
	 * Reads the names and offsets index, and save it.
	 * @throws IOException In case of an error reading MAX.RES.
	 */
	public void readIndex() throws IOException
	{
		file.seek(4);
		off_index = LittleEndianIO.readInt(file);

		file.seek(off_index);

		try{while(true)
		{
			byte name[] = new byte[8];
			file.read(name);
			int offset = LittleEndianIO.readInt(file);
			int size = LittleEndianIO.readInt(file);

			Slice s = new Slice(name, offset, size);
			index.add(s);
		}}catch(EOFException e)
		{}
	}

	/**
	 * Returns the slice corresponding to the given name.
	 * @param name The name of the slice we're looking for.
	 * @return The Slice object, if found. null if it wasn't found.
	 */
	public Slice getSlice(byte[] name)
	{
		for(int i=0; i<index.size(); i++)
			if(index.get(i).nameEquals(name))
				return index.get(i);

		return null;
	}

	/**
	 * Returns the slice corresponding to the given name, which is given as a
	 * String object. Useful for using directly with a String literal.
	 * @param name The name of the slice we're looking for.
	 * @return The Slice object, if found. null if it wasn't found.
	 */
	public Slice getSlice(String name)
	{
		return getSlice(name.getBytes());
	}

	/**
	 * Returns a IndexedImage array of the Multi-Image whose name is given.
	 * @param name The name of the Multi-Image we're looking for.
	 * @return An Array containing the IndexedImages.
	 * @throws IOException In case of an error reading MAX.RES.
	 */
	public IndexedImage[] getMultiImage(String name) throws IOException
	{
		Slice s = getSlice(name);
		MultiImage i = new MultiImage(file, s);
		return i.read();
	}

	/**
	 * Returns the String content of the given file.
	 * @param name The name of the file.
	 * @return A String object.
	 * @throws IOException
	 */
	public String getString(String name) throws IOException
	{
		Slice s = getSlice(name);
		if(s.size > 14) throw new IOException("Invalid Format");
		byte[] array = new byte[s.size];

		file.seek(s.offset);
		file.read(array);

		array[array.length-1] = ' ';
		return new String(array, "US-ASCII");
	}

	/**
	 * Returns a Vector object containing all the names of the files, as String
	 * objects.
	 * @return The list of the names of the files.
	 */
	public Vector<String> getNames()
	{
		Vector<String> list = new Vector<String>();
		for(int i=0; i<index.size(); i++)
			list.add(index.get(i).getNameString());
		return list;
	}

	/**
	 * Small class used for testing
	 * @author kbok
	 */
	private class ImagePanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		IndexedImage[] imgs;

		ImagePanel(IndexedImage[] imgs)
		{
			this.imgs = imgs;
		}

		public void paint(Graphics g)
		{
			g.drawImage(imgs[0].withPalette(IndexedImage.DEFAULT_PALETTE), 0, 0, null);
		}
	}

	/**
	 * For testing purposes.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		GraphicsFile f = new GraphicsFile(new RandomAccessFile("/home/kbok/MAX.RES", "r"));
		f.readIndex();
		Slice airtrans = f.getSlice("AIRTRANS");
		System.out.print("File Offset: ");
		System.out.println(airtrans.offset);
		IndexedImage[] imgs = f.getMultiImage("AIRTRANS");

		JFrame frame = new JFrame();
		ImagePanel panel = f.new ImagePanel(imgs);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
