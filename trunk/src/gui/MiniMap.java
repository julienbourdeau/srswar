package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import gamelogic.Board;
import gamelogic.units.Unit;
import gui.BoardView.ViewRange;

import javax.swing.JPanel;

/**
 * @author kbok
 * Provides a global view of the map in a miniature version.
 */
public class MiniMap extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	protected BufferedImage map;
	protected Image emptyMap;
	protected Board board;
	private BoardView view;
	
	protected class Updater extends TimerTask{
		public void run() {
			updateMap();
		}
	}
	
	/**
	 * Creates a new MiniMap associated with the given Board. It will get information
	 * from this Board.
	 * @param b The Board to associate to.
	 */
	public MiniMap(Board b, Image image)
	{
		board = b;
		emptyMap = image;
		setPreferredSize(new Dimension(114, 114));
		
		addMouseListener(this);
		
		Timer t = new Timer();
		t.schedule(new Updater(), 0, 500);
	}

	public void setBoardView(BoardView v)
	{
		view = v;
	}
	
	private void updateMap() {
		if(map == null)
			map = new BufferedImage(112, 112, BufferedImage.TYPE_INT_RGB);
		
		map.getGraphics().drawImage(emptyMap, 0, 0, null);
		
		if(view.getScannerMask() != null)
		for(int i=0; i<112; i++)
		 for(int j=0; j<112; j++)
		 {
			 Unit u = board.getUnitAt(i, j);
			 Color c;
			 if((u != null) && view.getScannerMask()[i][j])
			 {
			 	c = u.getOwner().getColor();
			 	map.setRGB(i, j, c.getRGB());
			 }
		 }
		
		repaint();
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.gray);
		g.drawRect(0, 0, 113, 113);
		g.drawImage(map, 1, 1, null);
		if(view != null)
		{
			ViewRange r = view.getViewRange();
			g.setColor(Color.white);
			g.drawRect(r.start.x, r.start.y, r.end.x-r.start.x, r.end.y-r.start.y);
		}
	}

	public void mouseClicked(MouseEvent e) {
		ViewRange r = view.getViewRange();
		view.setDelta((e.getX()-(r.end.x-r.start.x)/2)*64, (e.getY()-(r.end.y-r.start.y)/2)*64);
	}

	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e)   {}
	public void mousePressed(MouseEvent e)  {}
	public void mouseReleased(MouseEvent e) {}
}
