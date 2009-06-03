package gamelogic.units;

import gamelogic.Player;

public class Scanner extends Unit{
	public Scanner(Player p)
	{
		super(p, true);
		
		armour    = 4;
		hp        = 16;
		scanner   = 9;
		moves     = 12;
		
		curHp = hp;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_SCANNER);
		
		newTurn();
	}
}
