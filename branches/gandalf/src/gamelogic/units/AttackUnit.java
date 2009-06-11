package gamelogic.units;

import gamelogic.Player;
import gamelogic.Square;

/**
 * @author kbok
 * Provides a model for all units that can attack.
 */
public class AttackUnit extends Unit{
	protected int attack;
	protected int range;
	protected int shots;
	protected float curShots;
	protected int ammo;
	protected int curAmmo;
	
	public AttackUnit(Player p, boolean moveable) {
		super(p, moveable);
	}
	
	/**
	 * Returns whether the unit is enable to attack at the time being.
	 * @param dest The Square to attack to.
	 * @return Whether the unit is able to attack.
	 */
	public boolean canShot(Square dest)
	{
		if(curShots < 1) return false;
		if(curAmmo < 1) return false;
		return true;
	}
	
	/**
	 * Makes the unit shot on the given Square.
	 * @param dest The Square the unit will shot to.
	 */
	public void shot(Square dest)
	{
		if(!canShot(dest)) return;
		curAmmo--;curShots--;
		getBoard().getUnitAt(dest).shot(attack);
		getBoard().notifyShot(getPosition(), dest, getClient());
	}
	
	/**
	 * Returns the amount of shots remaining.
	 * @return The amount of shots remaning.
	 */
	public int getShots()
	{
		return (int)Math.floor(curShots);
	}
	
	/**
	 * @return The Maximum amount of shots of the unit.
	 */
	public int getMaxShots()
	{
		return shots;
	}
	
	/**
	 * @return The fire range of the unit.
	 */
	public int getRange()
	{
		return range;
	}
	
	/**
	 * @return The current amount of ammunition remaining.
	 */
	public int getAmmo()
	{
		return curAmmo;
	}
	
	/**
	 * @return The maximum amount of ammunition of this unit.
	 */
	public int getMaxAmmo()
	{
		return ammo;
	}
	
	public boolean isAttackUnit()
	{
		return true;
	}
	
	public void newTurn()
	{
		curShots = shots;
		curMoves = moves;
	}
}
