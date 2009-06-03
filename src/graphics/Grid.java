package graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author kbok
 * Provides routines for drawing the grid on the screen.
 */
public class Grid {
	/**
	 * Draws the grid on the screen.
	 * @param g The Graphics object to use.
	 * @param x The X coordinate of the grid.
	 * @param y The X coordinate of the grid.
	 * @param w Width of the grid.
	 * @param h Height of the grid.
	 * @param zoom Size of a Square.
	 * @param deltaX delta(scroll state) X of the view.
	 * @param deltaY delta(scroll state) Y of the view.
	 */
	public static void draw(Graphics g, int x, int y, int w, int h, int zoom, int deltaX, int deltaY)
	{
		g.setColor(Color.lightGray);
		for(int i=x-deltaX; i<w; i+=zoom)
			g.drawLine(i, y, i, h);

		for(int i=y-deltaY; i<h; i+=zoom)
			g.drawLine(x, i, w, i);
	}
}
