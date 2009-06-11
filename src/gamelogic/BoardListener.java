package gamelogic;

import java.util.Vector;

/**
 * @author julien
 * Provides an interface for a Board's client. The client is notified when a fire, a move, 
 * or a destroy occurs. The listener is then able to update his information about the board
 * without having to scan all the board.
 */
public interface BoardListener {
	/**
	 * Notifies a fire event.
	 * @param source The location of unit which just fired.
	 * @param dest The destination of the shot.
	 */
	public void fire(Square source, Square dest);
	
	/**
	 * Notifies a move event.
	 * @param source The previous location of the unit which just moved.
	 * @param path The path that the unit took.
	 */
	public void move(Square source, Vector<Square> path);
	
	/**
	 * Notifies a fly event.
	 * @param source The previous location of the unit which just moved.
	 * @param dest The position the unit takes now.
	 */
	public void fly(Square source, Square dest);
	
	/**
	 * Notifies a destroy event.
	 * @param dest The location of the unit which was destroyed.
	 */
	public void destroy(Square dest);
}
