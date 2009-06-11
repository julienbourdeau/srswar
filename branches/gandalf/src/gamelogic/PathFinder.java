package gamelogic;

import java.util.Vector;

/* This is A-RockStar ! See Soul Eater #04 for details */
/**
 * @author Julien
 * This class find a path to go from a square A to a square B. Not realy the best path but a path.
 */
public class PathFinder {
	
	/**
	 * @param b The board
	 * @param start The source square
	 * @param end The destination of the move
	 * @return A path to go from Start to End.
	 */
	public static Vector<Square> findPath(Board b, Square start, Square end)
	{
		Vector<Square> path = new Vector<Square>();
		
		int curX = start.x, curY = start.y, destX = end.x, destY = end.y;
		
		while(destX != curX || destY != curY)
		{
			if(destX != curX)
				curX+=(int)Math.signum(destX-curX);
			if(destY != curY)
				curY+=(int)Math.signum(destY-curY);
			if(b.getUnitAt(curX, curY) != null)
				return null;
			Square s = new Square(curX, curY); 
			path.add(s);
		}
		
		return path;
	}
	
	/**
	 * Give the length of a move in half square because diagonal move is allow
	 * @param b The board
	 * @param start The square of the start of this moment
	 * @param path the path to follow
	 * @return The lengh of a move in half square
	 */
	public static int getHPathLength(Board b, Square start, Vector<Square> path)
	{	
		int len = path.get(0).hMoveCost(start);
		for(int i=1; i<path.size(); i++)
			len += path.get(i).hMoveCost(path.get(i-1));
		return len;
	}
}
