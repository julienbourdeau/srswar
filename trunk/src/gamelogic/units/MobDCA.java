package gamelogic.units;

import gamelogic.Player;

public class MobDCA extends AttackUnit{
	public MobDCA(Player p){
		super(p, true);
	
		attack    = 22;
		shots     = 1;
		range     = 7;
		ammo      = 10;
		armour    = 4;
		hp        = 24;
		scanner   = 4;
		moves     = 7;
	
		curHp = hp;
		curAmmo = ammo;
	
		newTurn();
	}
	
	public String getName(){
		return "mobdca";
	}
}
