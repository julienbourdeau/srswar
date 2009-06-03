package files;
import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndianIO {
	public static int readInt(DataInput i) throws IOException
	{
		byte[] a = new byte[4];
		i.readFully(a);
		return ByteBuffer.wrap(a).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	public static short readShort(DataInput i) throws IOException
	{
		byte[] a = new byte[2];
		i.readFully(a);
		return ByteBuffer.wrap(a).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	public static byte readByte(DataInput i) throws IOException
	{
		byte[] a = new byte[1];
		i.readFully(a);
		return a[0];
	}
}
