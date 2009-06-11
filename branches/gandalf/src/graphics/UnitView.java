package graphics;

import gamelogic.BoardListener;
import gamelogic.Square;
import gamelogic.units.Unit;
import gui.Options;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

/**
 * Provides a graphical representation of a unit of the BoardView.
 * @author kbok
 */
public class UnitView implements BoardListener {
	protected Unit model;
	protected gui.BoardView boardview;
	protected int curX, curY;
	protected Vector<Square> curPath;
	protected int pathIndex;
	protected Options o;
	protected Orientation or;
	protected boolean mustPlayDrive = false;
	
	protected float speed = 0;
	
	/**
	 * Creates a new UnitView for the given unit, on the BoardView board.
	 * @param m The Unit represented by the constructed view
	 * @param board The BoardView the unit belongs to
	 * @param o The Options of the BoardView.
	 */
	public UnitView(Unit m, gui.BoardView board, Options o)
	{
		this.o = o;
		or = Orientation.N;
		model = m;
		model.setClient(this);
		boardview = board;
		curX = o.zoom()*model.getPosition().x;
		curY = o.zoom()*model.getPosition().y;
	}
	
	/* for backward compatibility */
	@Deprecated
	/**
	 * Draws the unit. Deprecated.
	 * @param g Graphics to use.
	 * @param u The Unit model.
	 * @param x The x coordinate of the unit
	 * @param y The y coordinate of the unit
	 * @param size The size of the unit to draw
	 */
	public void draw(Graphics g, Unit u, int x, int y, int size)
	{
		g.drawImage(ImageLibrary.getImage(u, or), x, y, size, size, null);
	}
	
	/**
	 * Draws the unit of the given Graphics
	 * @param g Graphics from paint() to use
	 */
	public void draw(Graphics g)
	{
		Rectangle r = boardview.getFrame();
		r.x -= o.zoom();
		r.y -= o.zoom();
		r.width += 2*o.zoom();
		r.height += 2*o.zoom();
		
		boolean visible = r.contains(model.getPosition().x * o.zoom(),
				model.getPosition().y * o.zoom());
		
		if(curPath == null)	{
			if(visible) draw(g, model, model.getPosition().x * o.zoom() - boardview.getDeltaX(),
								model.getPosition().y * o.zoom() - boardview.getDeltaY(), o.zoom());
		}else{
			int dx = (int)Math.signum(curPath.get(pathIndex).x*o.zoom() - curX);
			int dy = (int)Math.signum(curPath.get(pathIndex).y*o.zoom() - curY);
			int destX = curPath.get(pathIndex).x*o.zoom();
			int destY = curPath.get(pathIndex).y*o.zoom();
			
			if(speed<7) speed+=0.7;
			
			curX += ((int)speed)*dx;
			curY += ((int)speed)*dy;
			
			if(Math.abs(curX - destX) < 7
				&& Math.abs(curY - destY) < 7)
			{
				pathIndex++;
		
				if(pathIndex == curPath.size())
				{
					curPath = null;
					curX = destX;
					curY = destY;
					speed = 0;
				}else{
					Orientation oldOr = or;
					or = computeOrientation();
					if(!or.equals(oldOr))
					{
						curX = destX;
						curY = destY;
					}
				}
			}
			
			if(visible) draw(g, model, 
					curX - boardview.getDeltaX(), curY - boardview.getDeltaY(), o.zoom());
		}
		
		if(visible && o.color())
		{
			g.setColor(model.getOwner().getColor());
			g.drawRect(curX+4 - boardview.getDeltaX(),
					curY+4 - boardview.getDeltaY(), o.zoom()-8, o.zoom()-8);
			g.drawRect(curX+3 - boardview.getDeltaX(),
					curY+3 - boardview.getDeltaY(), o.zoom()-6, o.zoom()-6);
		}
		
	}

	public void destroy(Square dest) {
		// TODO Auto-generated method stub
		
	}

	public void fire(Square source, Square dest) {
		or = Orientation.fromDxDy((int)Math.signum(dest.x - source.x), 
				(int)Math.signum(dest.y - source.y));
	}

	public void move(Square source, Vector<Square> path) {
		curPath = path;
		pathIndex = 0;
		curX = o.zoom()*source.x;
		curY = o.zoom()*source.y;

		or = computeOrientation();
		if(or == null) or = Orientation.N;
		mustPlayDrive = true;
	}

	private Orientation computeOrientation() {
		return Orientation.fromDxDy(
				(int)Math.signum((curPath.get(pathIndex).x*o.zoom() - curX)/7),
				(int)Math.signum((curPath.get(pathIndex).y*o.zoom() - curY)/7));
	}

	/**
	 * @return The Model of the UnitView.
	 */
	public Unit model() {
		return model;
	}

	@Override
	public void fly(Square source, Square dest) {
		// TODO Auto-generated method stub
		
	}
}
