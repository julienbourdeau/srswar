package gamelogic.units;

import java.util.Vector;

import files.Map;
import gamelogic.PathFinder;
import gamelogic.Player;
import gamelogic.Square;

public class Scout extends AttackUnit {
	public Scout(Player p)
	{
		super(p, true);
		
		attack    = 12;
		shots     = 1;
		range     = 3;
		ammo      = 10;
		armour    = 4;
		hp        = 16;
		scanner   = 9;
		moves     = 12;
		
		curHp = hp;
		curAmmo = ammo;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_SCOUT);
		
		newTurn();
	}
	
	public boolean canMove(Vector<Square> path)
	{
		if(!isMoveable()) return false;
		if(getBoard().getTerrainType(path.get(path.size()-1)) == Map.TYPE_BLOCKED) return false;
		int hCost = PathFinder.getHPathLength(getBoard(), getPosition(), path);
		if(hCost > 2*curMoves) return false;
		return true;
	}
}
