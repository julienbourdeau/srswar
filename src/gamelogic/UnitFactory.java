package gamelogic;

import gamelogic.units.*;

/**
 * @author julien
 * This class build all units.
 */
public class UnitFactory {
	/**
	 * @param name The name of a the unit to create
	 * @param owner The player who create this unit
	 * @return a new instance of the wanted unit
	 */
	public static gamelogic.units.Unit createUnitByName(String name, Player owner)
	{
		if(name.equals("appros")) return new Appros(owner);
		if(name.equals("assault")) return new Assault(owner);
		if(name.equals("constructor")) return new Constructor(owner);
		if(name.equals("fuel")) return new Fuel(owner);
		if(name.equals("genius")) return new Genius(owner);
		if(name.equals("minelayer")) return new MineLayer(owner);
		if(name.equals("missile")) return new Missile(owner);
		if(name.equals("mobdca")) return new MobDCA(owner);
		if(name.equals("repair")) return new Repair(owner);
		if(name.equals("rocket")) return new Rocket(owner);
		if(name.equals("scanner")) return new Scanner(owner);
		if(name.equals("scout")) return new Scout(owner);
		if(name.equals("surveyor")) return new Surveyor(owner);
		if(name.equals("tank")) return new Tank(owner);
		if(name.equals("airtransport")) return new AirTransport(owner);
		
		return null;
	}
}
