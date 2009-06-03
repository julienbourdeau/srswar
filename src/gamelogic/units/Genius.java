package gamelogic.units;

import gamelogic.Player;

public class Genius extends Unit{
	public Genius(Player p){
		super(p, true);
	
		armour    = 6;
		hp        = 24;
		scanner   = 3;
		moves     = 6;
	
		curHp = hp;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_GENIUS);
		
		newTurn();
	}
}
