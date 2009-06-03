package gamelogic.units;

import gamelogic.Player;
import gamelogic.Square;

public class Tank extends AttackUnit{
	public Tank(Player p)
	{
		super(p, true);
		
		attack    = 16;
		shots     = 2;
		range     = 4;
		ammo      = 14;
		armour    = 10;
		hp        = 24;
		scanner   = 4;
		moves     = 6;
		
		
		curHp = hp;
		curAmmo = ammo;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_TANK);
		
		newTurn();
	}	
	
	public void move(Square dest)
	{
		float oldMoves = curMoves;
		super.move(dest);
		float delta = oldMoves - curMoves;
		curShots -= shots*delta/(moves);
		curShots+=0.01; /* FIXME : Quick fix */
	}
	
	public void shot(Square dest)
	{
		float oldShots = curShots;
		super.shot(dest);
		float delta = oldShots - curShots;
		curMoves -= moves*delta/shots;
		curMoves+=0.01; /* FIXME : Same fix */
	}
}
