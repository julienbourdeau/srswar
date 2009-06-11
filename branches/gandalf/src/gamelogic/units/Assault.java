package gamelogic.units;

import gamelogic.Player;
import gamelogic.Square;

public class Assault extends AttackUnit {
	public Assault(Player pl) {
		super(pl, true);
		
		attack    = 18;
		shots     = 2;
		range     = 6;
		ammo      = 14;
		armour    = 4;
		hp        = 24;
		scanner   = 5;
		moves     = 12;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_ASSAULT);
		
		curHp = hp;
		curAmmo = ammo;
		
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
