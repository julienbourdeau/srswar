package graphics;

/**
 * @author kbok
 * Represents the orientation of a Unit. It can be used to decide which Image to use.
 */
public class Orientation {
	private int orientation;
	
	private Orientation(int i) {
		orientation = i;
	}
	
	/**
	 * Returns the integer representation of the Orientation. Useful for addressing
	 * arrays.
	 * @return The integer representation, as int.
	 */
	public int toInt() {
		return orientation;
	}
	
	/**
	 * Compares the Orientation to another. Two Orientations are equals if their 
	 * integer representation are the same.
	 * @param other Orientation to compare to.
	 * @return Whether they are equal or not.
	 */
	public boolean equalsTo(Orientation other)
	{
		return toInt() == other.toInt();
	}
	
	public static Orientation N  =new Orientation(0);
	public static Orientation NE =new Orientation(1);
	public static Orientation E  =new Orientation(2);
	public static Orientation SE =new Orientation(3);
	public static Orientation S  =new Orientation(4);
	public static Orientation SW =new Orientation(5);
	public static Orientation W  =new Orientation(6);
	public static Orientation NW =new Orientation(7);
	
	/**
	 * Computes an Orientation from a difference between two Squares.
	 * @param dx The X difference.
	 * @param dy The Y difference.
	 * @return the Orientation computed.
	 */
	public static Orientation fromDxDy(int dx, int dy) {
		if(dx== 0 && dy==-1) return N;
		if(dx== 1 && dy==-1) return NE;
		if(dx== 1 && dy== 0) return E;
		if(dx== 1 && dy== 1) return SE;
		if(dx== 0 && dy== 1) return S;
		if(dx==-1 && dy== 1) return SW;
		if(dx==-1 && dy== 0) return W;
		if(dx==-1 && dy==-1) return NW;
		
		return null;
	}
}
