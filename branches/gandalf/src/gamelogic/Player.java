package gamelogic;

import java.awt.Color;
import java.util.Vector;

/**
 * @author julien
 * Represents a player. A Player is characterized by its name, its color, and its
 * internal game ID. The ID also defines the order of playing.
 */
public class Player {
	protected String name;
	protected Color color;
	protected int ID;
	protected boolean enabled;
	protected static Vector<Player> list;
	protected static int lastID;
	protected Square view;

	/**
	 * Creates a new Player with the given name and color. The new player will be
	 * automatically added to the player list.
	 * @param name
	 * @param color
	 */
	public Player(String name, Color color)
	{
		this.name=name;
		this.color=color;
		this.view = new Square(0, 0);
		enabled = true;
		
		if(list == null)
		{
			list = new Vector<Player>();
			list.add(this);
			lastID=0;
		}else{
			list.add(this);
			lastID++;
		}
		ID = lastID;
	}
	
	/**
	 * Returns the name of the player.
	 * @return The name, as a String object.
	 */
	public String getName()
	{
		return new String(name);
	}
	
	/**
	 * Returns the color of the player.
	 * @return The color, as a Color object.
	 */
	public Color getColor()
	{
		return new Color(color.getRGB());
	}
	
	/**
	 * Returns the Game-wide list of players.
	 * @return A copy of the Vector containing the players.
	 */
	public static Vector<Player> getList()
	{
		return new Vector<Player>(list);
	}
	
	/**
	 * Returns the number of players currently in the list.
	 * @return The number of players.
	 */
	public static int count()
	{
		return list.size();
	}
	
	/**
	 * Compares a Player object to another. A Player is equals to another if their
	 * unique ID are equals, disregarding their names and their colors.
	 * @param other The Player to compare to
	 * @return Whether the Players are equals.
	 */
	public boolean equals(Player other)
	{
		return (ID == other.ID);
	}

	/**
	 * When you you play you watch your units. You press "end turn", the player 2 want
	 * to see the last unit he watched, not yours. This method save this view.
	 * @param s The square to set
	 */
	public void setView(Square s)
	{
		view = s;
	}
	
	/**
	 * See setView to get more explanation.
	 * @return the last view of a player
	 */
	public Square getView()
	{
		return view;
	}
	
	
	/**
	 * A player is disable when he can't play. For example if it's not is turn or maybe
	 * all you have no unit available.
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * See disable to know what is the "enable concept"
	 * @return a boolean
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
}
