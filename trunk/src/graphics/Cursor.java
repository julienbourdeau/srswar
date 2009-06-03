package graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author kbok
 * Provides routines for drawing the Cursor on the screen. This is the unit selection
 * Cursor, and not the mouse.
 */
public class Cursor {
	/**
	 * Draws the cursor. The (x, y) coordinates are given relative to the view and not
	 * to the board.
	 * @param g Graphics object to use
	 * @param x X coordinate of the unit selected
	 * @param y Y coordinate of the unit selected
	 * @param size Size of a square in the current view.
	 */
	public static void draw(Graphics g, int x, int y, int size)
	{
		g.setColor(Color.white);
		
		g.drawLine(x-1, y-1, x-1 + size/4, y-1);
		g.drawLine(x+size+1-size/4, y-1, x+size+1, y-1);
		g.drawLine(x+1, y+1, x-1 + size/4, y+1);
		g.drawLine(x+size+1-size/4, y+1, x+size-1, y+1);
		
		g.drawLine(x+1, y+size-1, x-1 + size/4, y+size-1);
		g.drawLine(x+size+1-size/4, y+size-1, x+size-1, y+size-1);
		g.drawLine(x-1, y+size+1, x-1 + size/4, y+size+1);
		g.drawLine(x+size+1-size/4, y+size+1, x+size+1, y+size+1);
		
		g.drawLine(x-1, y-1, x-1, y-1 + size/4);
		g.drawLine(x-1, y+size+1-size/4, x-1, y+size+1);
		g.drawLine(x+1, y+1, x+1, y-1 + size/4);
		g.drawLine(x+1, y+size+1-size/4, x+1, y+size-1);
		
		g.drawLine(x+size-1, y+1, x+size-1, y-1 + size/4);
		g.drawLine(x+size-1, y+size+1-size/4, x+size-1, y+size-1);
		g.drawLine(x+size+1, y-1, x+size+1, y-1 + size/4);
		g.drawLine(x+size+1, y+size+1-size/4, x+size+1, y+size+1);
	}
}
