package files;
import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Provides routines to easily read little-endian files. All M.A.X.'s files are binary
 * little-endian so we need to make it easier.
 * @author kbok
 */
public class LittleEndianIO {
	/**
	 * Reads an integer from the given data input.
	 * @param i Data input source
	 * @return an integer
	 * @throws IOException In case of an error with the file
	 */
	public static int readInt(DataInput i) throws IOException
	{
		byte[] a = new byte[4];
		i.readFully(a);
		return ByteBuffer.wrap(a).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	/**
	 * Reads a short from the given data input.
	 * @param i Data input source
	 * @return a short
	 * @throws IOException In case of an error with the file
	 */
	public static short readShort(DataInput i) throws IOException
	{
		byte[] a = new byte[2];
		i.readFully(a);
		return ByteBuffer.wrap(a).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	/**
	 * Reads a byte from the given data input.
	 * @param i Data input source
	 * @return a byte
	 * @throws IOException In case of an error with the file
	 */
	public static byte readByte(DataInput i) throws IOException
	{
		byte[] a = new byte[1];
		i.readFully(a);
		return a[0];
	}
}
