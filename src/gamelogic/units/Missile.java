package gamelogic.units;

import java.util.Vector;

import gamelogic.PathFinder;
import gamelogic.Player;
import gamelogic.Square;

public class Missile extends AttackUnit{
	public Missile(Player p){
		super(p, true);
	
		attack    = 22;
		shots     = 1;
		range     = 10;
		ammo      = 6;
		armour    = 4;
		hp        = 24;
		scanner   = 4;
		moves     = 6;
	
		curHp = hp;
		curAmmo = ammo;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_MISSILE);
	
		newTurn();
	}
	
	public void shot(Square dest)
	{
		if(!canShot(dest)) return;
		curAmmo--;
		curShots = 0;
		curMoves = 0;
		getBoard().getUnitAt(dest).shot(attack);
		getBoard().notifyShot(getPosition(), dest, getClient());
	}
	
	public void move(Square dest)
	{
		Vector<Square> path = PathFinder.findPath(getBoard(), getPosition(), dest);
		if(path == null) return;
		if(!canMove(path)) return;
		curShots = 0;
		
		super.move(dest);
	}
}
