package gamelogic.units;

import gamelogic.Player;
import gamelogic.Square;

public class Rocket extends AttackUnit{
	public Rocket(Player p){
		super(p, true);
	
		attack    = 15;
		shots     = 2;
		range     = 7;
		ammo      = 16;
		armour    = 4;
		hp        = 24;
		scanner   = 4;
		moves     = 6;
	
		curHp = hp;
		curAmmo = ammo;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_ROCKET);
	
		newTurn();
	}
	
	public void shot(Square dest)
	{
		if(!canShot(dest)) return;
		curAmmo--;curShots--;curMoves-=moves/shots;
		getBoard().notifyShot(getPosition(), dest, getClient());
	}
}

