package gamelogic.units;

import java.util.Vector;

import files.Map;
import gamelogic.Board;
import gamelogic.BoardListener;
import gamelogic.PathFinder;
import gamelogic.Player;
import gamelogic.Square;

/**
 * @author kbok
 * Provides a global model for a single unit. These can be building, ground, water or air units.
 * Each unit inherits from this class and reimplements some methods.
 */
public class Unit {
	private Player owner;
	private BoardListener client;
	private Square position;
	private Board board;
	
	protected UnitIdentifier ident;
	
	protected int scanner;
	protected int hp;
	protected int armour;
	protected int moves;
	protected int curHp;
	protected float curMoves;
	
	private boolean moveable;
	
	/**
	 * Creates a new unit for the given player. If moveable is set to true, the unit will be moveable (that is, not a 
	 * building)
	 * @param p The owner of the new unit.
	 * @param moveable Set to true if the unit is moveable.
	 */
	public Unit(Player p, boolean moveable)
	{
		owner = p;
		this.moveable = moveable;
	}
	
	/**
	 * Returns the current owner of the unit.
	 * @return A reference to the owner of the unit.
	 */
	public Player getOwner()
	{
		return owner;
	}
	
	
	/**
	 * Sets the unique client of the unit. This client will be notified only by event on this unit.
	 * @param cl The client of the unit.
	 */
	public void setClient(BoardListener cl)
	{
		client = cl;
	}
	
	/**
	 * Returns the client of the unit.
	 * @return The current client of the unit.
	 */
	public BoardListener getClient()
	{
		return client;
	}
	
	/**
	 * Sets the position of the unit. The board will not be notified of this action.
	 * @param pos The position a the unit, as a Square object.
	 */
	public void setPosition(Square pos)
	{
		position = pos;
	}
	
	/**
	 * Returns the position of the unit.
	 * @return The position of the unit, as a Square object.
	 */
	public Square getPosition()
	{
		return position;
	}
	
	/**
	 * Sets the board associated with the unit.
	 * @param b The board parent of the unit.
	 */
	public void setBoard(Board b)
	{
		board = b;
	}
	
	/**
	 * Returns the board the unit belongs to.
	 * @return The board parent of the unit.
	 */
	public Board getBoard()
	{
		return board;
	}
	
	/**
	 * Make the unit being shot. The unit will automatically compute its new HP and destroy itself if necessary.
	 * @param damage The attack factor of the unit shooting.
	 */
	public void shot(int damage)
	{
		curHp -= damage - armour;
		if(curHp < 1) destroy();
	}
	
	/**
	 * Destroy the unit in the game. Does not destroy the object.
	 */
	public void destroy()
	{
		board.notifyDestroy(position, client);
	}
	
	/**
	 * Moves the unit to the specified Square. If there is no way the unit can find to go to the given destination, 
	 * the unit will not move. Otherwise, it will notify its board of the path taken.
	 * @param dest The destination of the unit.
	 */
	public void move(Square dest)
	{
		Vector<Square> path = PathFinder.findPath(board, position, dest);
		if(path == null) return;
		if(!canMove(path)) return;
		int hCost = PathFinder.getHPathLength(board, position, path);
		curMoves -= (float)hCost/2;
		
		board.setUnitAt(dest, this);
		board.setUnitAt(position, null);
		Square oldPos = position;
		position = dest;
		
		board.notifyMove(oldPos, path, client);
	}
	
	/**
	 * Indicates whether the unit can take the path given in one turn.
	 * @param path The path to take.
	 * @return Whether the unit can take the path.
	 */
	public boolean canMove(Vector<Square> path)
	{
		if(!moveable) return false;
		if(board.getTerrainType(path.get(path.size()-1)) != Map.TYPE_NORMAL) return false;
		int hCost = PathFinder.getHPathLength(board, position, path);
		if(hCost > 2*curMoves) return false;
		return true;
	}
	
	/**
	 * Returns the current amount of moves remaining.
	 * @return The moves remaining.
	 */
	public int getMoves()
	{
		return (int)Math.floor(curMoves);
	}
	
	/**
	 * Returns the maximum amount of moves for this unit.
	 * @return The maximum amount of moves.
	 */
	public int getMaxMoves()
	{
		return moves;
	}
	
	/**
	 * Returns the scanner range of this unit.
	 * @return The scanner range of this unit.
	 */
	public int getScanner()
	{
		return scanner;
	}
	
	/**
	 * Returns the initial HP of this unit.
	 * @return The inital HP of this unit.
	 */
	public int getMaxHP()
	{
		return hp;
	}
	
	/**
	 * Returns the armour value of this unit.
	 * @return The armour value of this unit.
	 */
	public int getArmour()
	{
		return armour;
	}
	
	/**
	 * Returns the current amount of HP remaining.
	 * @return The amount of HP remaining.
	 */
	public int getHP()
	{
		return curHp;
	}
	
	/**
	 * Returns the Identifier of the unit, which is unique for each type.
	 * @return The identifier of the unit.
	 */
	public UnitIdentifier getIdent()
	{
		return ident;
	}
	
	/**
	 * Computes new values for the beginning of a turn (For example, the shots, the moves...)
	 */
	public void newTurn()
	{
		curMoves = moves;
	}
	
	/**
	 * @return Whether the unit is moveable or not.
	 */
	public boolean isMoveable()
	{
		return moveable;
	}
	
	/**
	 * @return Whether the unit can attack or not.
	 */
	public boolean isAttackUnit()
	{
		return false;
	}
}
