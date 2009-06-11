package gamelogic;

/**
 * @author julien
 * Provides an interface for objects who wants to be notified about game-wide events.
 */
public interface GameListener {
	/**
	 * Fired when a new turn starts.
	 */
	public void endTurn();
}
