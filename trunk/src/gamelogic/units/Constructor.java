package gamelogic.units;

import gamelogic.Player;

public class Constructor extends Unit{
	public Constructor(Player p){
		super(p, true);
	
		armour    = 6;
		hp        = 24;
		scanner   = 3;
		moves     = 6;
	
		ident = new UnitIdentifier(UnitIdentifier.UID_CONSTRUCTOR);
		
		curHp = hp;
	
		newTurn();
	}
}
