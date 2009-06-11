package gamelogic.units;

import gamelogic.Player;

public class Fuel extends Unit{
	public Fuel(Player p){
		super(p, true);

		armour    = 4;
		hp        = 20;
		scanner   = 3;
		moves     = 7;
	
		curHp = hp;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_FUEL);

		newTurn();
	}
}
