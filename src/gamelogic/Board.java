package gamelogic;

import files.Map;
import gamelogic.units.Unit;

import java.util.Vector;

/**
 * @author kbok
 * Provides a model for the main game board. The board is responsible of localizing units, 
 * check that only one unit is present is on square, and propagating fire, move, and destroy 
 * events. The size of the grid is always 100x100 as this is one of the rules of the games.
 */
public class Board {
	private Vector<BoardListener> clients;
	protected gamelogic.units.Unit grid[][];
	private Map map;
	
	/**
	 * Creates a new empty board of size 100x100.
	 */
	public Board()
	{
		clients = new Vector<BoardListener>();
		grid = new Unit[112][112];
	}
	
	/**
	 * 
	 * @param m
	 */
	public void setMap(Map m)
	{
		map = m;
	}
	
	/**
	 * Returns the map associated with this board.
	 * @return The map of the board
	 */
	public Map getMap()
	{
		return map;
	}
	
	/**
	 * Adds an unit to to board at the given coordinates.
	 * Unlike setUnit() it also modify the unit's properties. It is useful for example
	 * with a newly created unit.
	 * @param unit The unit to assign to the board.
	 * @param x X position of the unit.
	 * @param y Y position of the unit.
	 */
	public void addUnit(Unit unit, int x, int y) {
		unit.setPosition(new Square(x, y));
		unit.setBoard(this);
		grid[x][y] = unit;
	}

	/**
	 * Returns the terrain type of the given square.
	 * @param s The current square.
	 * @return
	 */
	public int getTerrainType(Square s)
	{
		return getTerrainType(s.x, s.y);
	}
	
	/**
	 * Returns the terrain type of the given square.
	 * @param x The X coordinate of the current square
	 * @param y The Y coordinate of the current square
	 * @return
	 */
	public int getTerrainType(int x, int y)
	{
		return map.getTerrainType(x, y);
	}
	
	/**
	 * Registers a new client to listen to events on the board. The events are fired once
	 * they are completely finished, so the clients must not modify the state of the board
	 * in their methods.
	 * @param client The client listener to add to the list.
	 */
	public void addClient(BoardListener client)
	{
		clients.add(client);
	}
	
	/**
	 * Removes a client from the list. If it is present several times, removes it once.
	 * @param client The client listener to remove from the list.
	 */
	public void removeClient (BoardListener client)
	{
		clients.remove(client);
	}
	
	/**
	 * Return the unit located at the given position. The unit is untouched.
	 * @param x X position of the unit.
	 * @param y Y position of the unit.
	 * @return Unit located at the given point.
	 */
	public Unit getUnitAt(int x, int y)
	{
		return grid[x][y];
	}

	/**
	 * Return the unit located at the given position. The unit is untouched.
	 * @param loc Position of the unit.
	 * @return Unit located at the given point.
	 */
	public Unit getUnitAt(Square loc) {
		return getUnitAt(loc.x, loc.y);
	}
	
	/**
	 * Sets the given square containing the given unit. The unit is untouched.
	 * You have to modify the coordinates of the unit each time you use this method.
	 * @param x X position of the unit.
	 * @param y Y position of the unit.
	 * @param unit Unit to place in the given coordinates.
	 */
	public void setUnitAt(int x, int y, Unit unit)
	{
		grid[x][y] = unit;
	}
	
	/**
	 * Sets the given square containing the given unit. The unit is untouched.
	 * You have to modify the coordinates of the unit each time you use this method.
	 * @param pos Position of the unit.
	 * @param unit Unit to place in the given coordinates.
	 */
	public void setUnitAt(Square pos, Unit unit) {
		setUnitAt(pos.x, pos.y, unit);
	}
	
	public void notifyMove(Square pos, Vector<Square> path, BoardListener unitClient)
	{
		for(int i=0; i<clients.size(); i++)
			clients.get(i).move(pos, path);
		unitClient.move(pos, path);
	}
	
	public void notifyShot(Square source, Square dest, BoardListener unitClient)
	{
		for(int i=0; i<clients.size(); i++)
			clients.get(i).fire(source, dest);
		unitClient.fire(source, dest);
	}
	
	public void notifyFly(Square source, Square dest, BoardListener unitClient)
	{
		for(int i=0; i<clients.size(); i++)
			clients.get(i).fly(source, dest);
		unitClient.fly(source, dest);
	}
	
	public void notifyDestroy(Square dest, BoardListener unitClient)
	{
		for(int i=0; i<clients.size(); i++)
			clients.get(i).destroy(dest);
		unitClient.destroy(dest);
		
		Player p = getUnitAt(dest).getOwner();
		for(int i=0; i<112; i++)
			for(int j=0; j<112; j++)
				if(i!=dest.x && j!=dest.y)
					if(getUnitAt(i, j) != null)
						if(getUnitAt(i, j).getOwner()==p)
							return;
		
		p.disable();
		
	}
	
	/**
	 * Declares a new turn. Resets all turn-dependent properties of units such as moves and
	 * shots.
	 */
	public void newTurn()
	{
		for(int i=0; i<112; i++)
			for(int j=0; j<112; j++)
			{
				if(grid[i][j] != null)
					grid[i][j].newTurn();
			}
	}
}
