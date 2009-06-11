package gamelogic;

/**
 * @author julien
 * Represents a Square in the board, that is, a single position containing or not
 * a unit.
 */
public class Square {
	public int x, y;

	/**
	 * Creates a new Square of coordinates (x, y)
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Duplicates a Square object.
	 * @param position Square object to duplicate.
	 */
	public Square(Square position) {
		this.x = position.x;
		this.y = position.y;
	}

	/**
	 * Returns true if the given Square is equal to the current Square. Two Squares
	 * are equals if their coordinates are equals.
	 * @param other The Square to compare to.
	 * @return Whether the Squares are equals.
	 */
	public boolean equals(Square other)
	{
		return (x == other.x && y == other.y);
	}
	
	public String toString()
	{
		String r = new String();
		r = r.concat(String.valueOf(x));
		r = r.concat(";");
		r = r.concat(String.valueOf(y));
		
		return r;
	}
	
	/**
	 * Returns the cost, in half-moves, of a move to a neighbor Square. To know the cost
	 * to distant Squares, use Pathfinder.
	 * @param dest The destination of the move.
	 * @return The cost, in half-moves. Can be 2 or 3.
	 */
	public int hMoveCost(Square dest) {
		int s = Math.abs(dest.x - x) + Math.abs(dest.y - y);
		return s+1;
	}
}
