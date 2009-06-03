package gamelogic.units;

import gamelogic.Player;

public class Surveyor extends Unit{
	public Surveyor(Player p){
		super(p, true);
	
		armour    = 4;
		hp        = 16;
		scanner   = 3;
		moves     = 7;
	
		curHp = hp;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_SURVEYOR);
		
		newTurn();
	}
}
