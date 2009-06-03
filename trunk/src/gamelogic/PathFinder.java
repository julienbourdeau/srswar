package gamelogic;

import java.util.Vector;

/* This is A-RockStar ! See Soul Eater #04 for details */
public class PathFinder {
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
	
	public static int getHPathLength(Board b, Square start, Vector<Square> path)
	{	
		int len = path.get(0).hMoveCost(start);
		for(int i=1; i<path.size(); i++)
			len += path.get(i).hMoveCost(path.get(i-1));
		return len;
	}
}
