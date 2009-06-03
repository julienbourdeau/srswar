package gamelogic;

import java.util.Vector;

/* TODO: Create new Unit model */
@Deprecated 
public abstract class OldUnit {
	protected Square position;
	protected Board board;
	protected BoardListener client;
	protected Player owner;
	
	public int movements;
	public int curHMovements;
	public int shots;
	public int curShots;
	public int range;
	public int scanner;
	public int ammo;
	public int curAmmo;
	public int attack;
	public int armour;
	public int hp;
	public int curHp;
	
	public OldUnit()
	{
		this((Player)null);
	}
	
	public OldUnit(Player pl) {
		owner = pl;
	}
	
	public Player getOwner()
	{
		return owner;
	}

	public void setClient(BoardListener client){
		this.client = client;
	}
	
	public BoardListener getClient(){
		return client;
	}
	
	public void move(Square dest) throws Exception
	{
		Vector<Square> path = PathFinder.findPath(board, position, dest);
		if(path == null) return;
		if(!canMove(path)) return;
		int hCost = moveHCost(path);
		curHMovements -= hCost;
		
		//board.setUnitAt(dest, this);
		board.setUnitAt(position, null);
		Square oldPos = position;
		position = dest;
		
		board.notifyMove(oldPos, path, client);
	}

	public void setSquare(Square square) {
		position = square;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Square getPosition() {
		return new Square(position);
	}
	
	public abstract boolean canShot(Square dest);
	public void shot(Square dest)
	{
		if(!canShot(dest)) return;
		curAmmo--;curShots--;
		//board.getUnitAt(dest).fireOn(attack);
		board.notifyShot(position, dest, client);
	}
	
	public void fireOn(int attack)
	{
		curHp -= attack - armour;
		if(curHp < 1)
		{
			destroy();
		}
	}
	
	protected void destroy() {
		board.notifyDestroy(position, client);
	}

	public abstract boolean canMove(Vector<Square> path);
	public abstract void reset();
	
	protected int moveHCost(Vector<Square> path)
	{
		return PathFinder.getHPathLength(board, position, path);
	}
	
	public abstract String getName();

	public int getCurMoves() {
		return curHMovements/2;
	}
	
	public int getMoves() {
		return movements;
	}

	public int getCurShots() {
		return curShots;
	}
	
	public int getShots() {
		return shots;
	}

	public int getRange() {
		return range;
	}

	public int getCurAmmo() {
		return curAmmo;
	}

	public int getAmmo() {
		return ammo;
	}

	public int getCurHP() {
		return curHp;
	}

	public int getHP() {
		return hp;
	}

	public int getScanner() {
		return scanner;
	}
}
