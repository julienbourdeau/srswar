package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import gamelogic.units.AttackUnit;
import gamelogic.units.Unit;
import graphics.ImageLibrary;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Provides a widget to view the status of the currently selected unit.
 * @author kbok
 */
public class UnitInfoPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	protected static final int AMMO_FULL  = 1;
	protected static final int AMMO_EMPTY = 2;
	protected static final int SHOT_FULL  = 3;
	protected static final int SHOT_EMPTY = 4;
	protected static final int HIT_FULL   = 5;
	protected static final int HIT_EMPTY  = 6;
	protected static final int MVMT_FULL  = 7;
	protected static final int MVMT_EMPTY = 8;
	
	protected Unit unit;
	
	/**
	 * Creates a new info panel and links it to the given unit. If unit equals null, 
	 * Then the panel will not display information.
	 */
	public UnitInfoPanel(Unit u) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setUnit(u);
		
		setBackground(Color.black);
		setPreferredSize(new Dimension(160, 100));
	}
	
	/**
	 * Sets the unit to display information about. Set to null to display no
	 * information.
	 * @param u The unit model to inspect
	 */
	public void setUnit(Unit u)
	{
		unit = u;
		
		if(u == null)
		{
			return;
		}
		
		repaint();
	}

	public void paint(Graphics g)
	{
		if(unit == null) return;
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int x, y = 0;
		int a = 60;
		int b = 20;
		
		/* Hits */
		g.drawImage(ImageLibrary.getSmallLabels(), b, 3, b+27, 8, 0, 0, 27, 5, null);
		for(x=0; x < unit.getHP()/4; x++)
			drawSmallImage(g, HIT_FULL, a+x*7, y);
		for(;x < unit.getMaxHP()/4; x++)
			drawSmallImage(g, HIT_EMPTY, a+x*7, y);
		y+=12;
		
		/* Ammo */
		if(unit.isAttackUnit())
		{
			g.drawImage(ImageLibrary.getSmallLabels(), b, y+3, b+27, y+8, 0, 6, 27, 11, null);
			for(x=0; x < ((AttackUnit)unit).getAmmo(); x++)
				drawSmallImage(g, AMMO_FULL, a+x*6, y+2);
			for(;x < ((AttackUnit)unit).getMaxAmmo(); x++)
				drawSmallImage(g, AMMO_EMPTY, a+x*6, y+2);
			y+=12;
		}
		
		/* Mvmt */
		g.drawImage(ImageLibrary.getSmallLabels(), b, y+3, b+27, y+8, 0, 12, 27, 17, null);
		for(x=0; x < unit.getMoves(); x++)
			drawSmallImage(g, MVMT_FULL, a+x*8, y+2);
		for(;x < unit.getMaxMoves(); x++)
			drawSmallImage(g, MVMT_EMPTY, a+x*8, y+2);
		y+=12;
		
		/* Shot */
		if(unit.isAttackUnit())
		{
			g.drawImage(ImageLibrary.getSmallLabels(), b, y+3, b+27, y+8, 0, 18, 27, 23, null);
			for(x=0; x < ((AttackUnit)unit).getShots(); x++)
				drawSmallImage(g, SHOT_FULL, a+x*9, y+4);
			for(;x < ((AttackUnit)unit).getMaxShots(); x++)
				drawSmallImage(g, SHOT_EMPTY, a+x*9, y+4);
			y+=12;
		}
	}
	
	private void drawSmallImage(Graphics g, int image, int x, int y)
	{
		Image i = ImageLibrary.getSmallIcons();
		switch(image)
		{
		case SHOT_FULL:
			g.drawImage(i, x, y, x+8, y+4, 0, 26, 8, 30, null);
			break;
		case SHOT_EMPTY:
			g.drawImage(i, x, y, x+8, y+4, 9, 26, 17, 30, null);
			break;
		case AMMO_FULL:
			g.drawImage(i, x, y, x+5, y+7, 0, 10, 5, 17, null);
			break;
		case AMMO_EMPTY:
			g.drawImage(i, x, y, x+5, y+7, 6, 10, 11, 17, null);
			break;
		case MVMT_FULL:
			g.drawImage(i, x, y, x+7, y+7, 0, 18, 7, 25, null);
			break;
		case MVMT_EMPTY:
			g.drawImage(i, x, y, x+7, y+7, 8, 18, 15, 25, null);
			break;
		case HIT_FULL:
			g.drawImage(i, x, y, x+6, y+9, 0, 0, 6, 9, null);
			break;
		case HIT_EMPTY:
			g.drawImage(i, x, y, x+6, y+9, 7, 0, 13, 9, null);
		default:
		}
	}
	
	/**
	 * Reloads the information from the unit.
	 */
	public void flush() {
		setUnit(unit);
	}
}
