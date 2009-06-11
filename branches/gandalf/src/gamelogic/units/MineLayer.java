package gamelogic.units;

import gamelogic.Player;

public class MineLayer extends Unit{
	public MineLayer(Player p){
		super(p, true);
	
		armour    = 4;
		hp        = 24;
		scanner   = 4;
		moves     = 7;
	
		curHp = hp;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_MINELAYER);
		
		newTurn();
	}
}
