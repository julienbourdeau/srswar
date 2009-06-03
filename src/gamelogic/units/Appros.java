package gamelogic.units;

import gamelogic.Player;

public class Appros extends Unit {
	public Appros(Player p){
		super(p, true);
	
		armour    = 4;
		hp        = 24;
		scanner   = 3;
		moves     = 7;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_APPROS);
	
		curHp = hp;
	
		newTurn();
	}
}
