package gamelogic.units;

import gamelogic.Player;
import gamelogic.Square;

public class AirTransport extends Unit {
	public AirTransport(Player p) {
		super(p, true);
		
		armour = 4;
		hp = 18;
		moves = 18;
		scanner = 5;
		
		ident = new UnitIdentifier(UnitIdentifier.UID_AIRTRANSPORT);
		
		curHp = hp;
		
		newTurn();
	}
	
	public void move(Square dest)
	{
		int distance = (int)Math.ceil(Math.sqrt((dest.x-getPosition().x)*(dest.x-getPosition().x)+
										(dest.y-getPosition().y)*(dest.y-getPosition().y)));
		if(curMoves < distance) return;
		curMoves -= distance;
		
		getBoard().setUnitAt(dest, this);
		getBoard().setUnitAt(getPosition(), null);
		Square oldPos = getPosition();
		setPosition(dest);
		
		getBoard().notifyFly(oldPos, dest, getClient());
	}
}
