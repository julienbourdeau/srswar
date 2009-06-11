package gui;

import files.Map;
import gamelogic.Board;
import gamelogic.BoardListener;
import gamelogic.Game;
import gamelogic.GameListener;
import gamelogic.Square;
import gamelogic.units.AttackUnit;
import graphics.AcceleratedGraphics;
import graphics.Grid;
import graphics.ImageLibrary;
import graphics.Orientation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import sound.SoundLibrary;
import sound.SoundPlayer;

/**
 * This is the main view of the game.
 * @author kbok
 */
public class BoardView extends Canvas implements GameListener{
	private static final long serialVersionUID = 1L;
	protected Board model;
	protected Listener listener;
	protected Options options;
	protected Square selectedUnit;
	protected Vector<graphics.UnitView> units;
	protected Timer timer;
	protected Updater updater;
	protected Game game;
	protected BufferStrategy bufferStrategy;
	protected BufferedImage bgBuffer;
	protected VolatileImage volBgBuffer;
	protected Panel panel;

	protected int deltaX, deltaY;
	protected boolean somethingExploding = false;
	protected Square explosionZone;
	protected int explosionIndex;
	
	protected boolean somethingExploding2 = false;
	protected Square explosionZone2;
	protected int explosionIndex2;
	
	protected Cursor curCursor;
	
	protected SoundPlayer soundPlayer;
	protected int scrollX = 0;
	protected int scrollY = 0;
	
	protected boolean scanner_mask[][];
	protected boolean updateBgBuffer;
	protected boolean resizeBgBuffer;
	private Map map;
	
	public static final int SCROLL_LEFT  = 1;
	public static final int SCROLL_RIGHT = 2;
	public static final int SCROLL_UP    = 3;
	public static final int SCROLL_DOWN  = 4;
	
	/**
	 * Represents a range of the view from one Square to another.
	 * @author kbok
	 */
	public class ViewRange{
		public Square start;
		public Square end;
		
		/**
		 * Creates a new ViewRange.
		 * @param s The top-left corner of the view.
		 * @param e The bottom-right corner of the view.
		 */
		public ViewRange(Square s, Square e)
		{
			start = s;
			end = e;
		}
	}
	
	/**
	 * Provides a simple way to ask repetitively the view to update itself.
	 * @author kbok
	 */
	protected class Updater extends TimerTask{
		protected BoardView parent;
		
		/**
		 * Creates a new Updater for the given BoardView.
		 * @param parent The BoardView to update.
		 */
		public Updater(BoardView parent){
			this.parent = parent;
		}
		
		public void run() {
			parent.paint();
		}
	}
	
	/**
	 * This class is used to catch all events happening to the BoardView, and filter them. 
	 * @author kbok
	 */
	protected class Listener implements BoardListener, MouseListener, MouseMotionListener, ComponentListener{
		protected BoardView parent;
		
		/**
		 * Creates a new Listener for the given BoardView.
		 * @param parent The receptor of the events.
		 */
		public Listener(BoardView parent){
			this.parent = parent;
		}
		
		public void destroy(Square dest) {
			somethingExploding2 = true;
			explosionIndex2 = 0;
			explosionZone2 = dest;
			calculateScannerMask();
		}

		public void fire(Square source, Square dest) {
			soundPlayer.play(SoundLibrary.getStreamByUnitAction(model.getUnitAt(source).getIdent().getName(), "attack"));
			parent.fireNotify(source, dest);
			somethingExploding = true;
			explosionIndex = 0;
			explosionZone = dest;
		}

		public void move(Square source, Vector<Square> path) {
			parent.moveNotify(source, path.get(path.size()-1));
			
		}

		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == 1)
				parent.leftclick(e.getX(), e.getY());
			if(e.getButton() == 3)
				parent.rightclick(e.getX(), e.getY());
		}

		public void mouseEntered(MouseEvent e) {
			parent.setCursor(curCursor);
			
		}

		public void mouseExited(MouseEvent e) {
			parent.setCursor(Cursor.getDefaultCursor());
			scrollX = 0;
			scrollY = 0;
		}

		public void mousePressed(MouseEvent e) {
			// unused
		}

		public void mouseReleased(MouseEvent e) {
			// unused
		}

		public void mouseDragged(MouseEvent e) {
			// unused
		}

		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			parent.notifyMove(x, y);
		}

		public void componentHidden(ComponentEvent e) {
		}

		public void componentMoved(ComponentEvent e) {
		}

		public void componentResized(ComponentEvent e) {
			resizeBgBuffer = true;
		}
		
		public void componentShown(ComponentEvent e) {
		}

		public void fly(Square source, Square dest) {
			parent.flyNotify(source, dest);
		}
	}
	
	/**
	 * Creates a new BoardView linked to the arguments.
	 * @param model The Board model of the view.
	 * @param o The options of the view.
	 * @param game The Game controller of the view.
	 */
	public BoardView(Board model, Options o, Game game)
	{
		this.game = game;
		options = o;
		listener = new Listener(this);
		model.addClient(listener);
		this.model = model;
		deltaX = 0; deltaY = 0;
		addMouseMotionListener(listener);
		addMouseListener(listener);
		addComponentListener(listener);
		
		soundPlayer = new SoundPlayer();
		Thread soundThread = new Thread(soundPlayer);
		soundThread.start();
		SoundLibrary.load();
		
		units = new Vector<graphics.UnitView>();
		for(int i=0; i<100; i++)
			for(int j=0; j<100; j++)
				if(model.getUnitAt(i, j) != null)
				{
					graphics.UnitView unit = new graphics.UnitView(model.getUnitAt(i, j), this, options);
					unit.setSoundPlayer(soundPlayer);
					units.add(unit);
				}
		
		setIgnoreRepaint(true);
		
		curCursor = ImageLibrary.getCursorNot();
		setCursor(ImageLibrary.getCursorNot());
		
		scanner_mask = new boolean[100][100];
		calculateScannerMask();
		
	}

	/**
	 * Scrolls to the given direction.
	 * @param direction The direction of the scrolling.
	 */
	public void scroll(int direction) {
		int speed = 2;
		
		switch(direction)
		{
		case SCROLL_UP:
			if(deltaY < speed) deltaY = 0;
			else deltaY -= speed;
			break;
		case SCROLL_DOWN:
			deltaY += speed;
			break;
		case SCROLL_LEFT:
			if(deltaX < speed) deltaX = 0;
			else deltaX -= speed;
			break;
		case SCROLL_RIGHT:
			deltaX += speed;
			break;
		}
	}

	/**
	 * Used when a shot occurs on the board.
	 * @param source The place where the shot came from
	 * @param dest The place where the shot occured
	 */
	public void fireNotify(Square source, Square dest) {
		game.getPanel().flushInfo();
	}

	/**
	 * Paints the content of the board view.
	 */
	@Deprecated
	public void paint() {
		Graphics g = bufferStrategy.getDrawGraphics();
		paint(g);
		g.dispose();
		bufferStrategy.show();
		
	}

	/**
	 * Starts the timer which will fire a repaint event every epsilon milliseconds
	 */
	public void startTimer(){
		updater = new Updater(this);
		timer = new Timer();
		timer.schedule(updater, 0, 20);
	}
	
	/**
	 * Used when w move occurs on the board.
	 * @param source The source of the move
	 * @param dest The destination of the unit.
	 */
	public void moveNotify(Square source, Square dest) {
		if(source.equals(selectedUnit))
		{
			selectedUnit = dest;
			game.getPanel().setUnitInfo(model.getUnitAt(dest));
		}
		calculateScannerMask();
	}
	
	/**
	 * For future use. To call when a fly occurs on the board.
	 * @param src The source of the move
	 * @param dst The destination of the unit.
	 */
	public void flyNotify(Square src, Square dst) {
		if(src.equals(selectedUnit))
		{
			selectedUnit = dst;
			game.getPanel().setUnitInfo(model.getUnitAt(dst));
		}
		calculateScannerMask();
	}

	/**
	 * @return The bounds of the view. To use with the minimap, for instance.
	 */
	public Rectangle getFrame()
	{
		Rectangle r = new Rectangle();
		r.x = deltaX;
		r.y = deltaY;
		r.width = getWidth();
		r.height = getHeight();
		
		return r;
	}
	
	protected void scroll()
	{
		if(scrollX < 0 && deltaX < -scrollX) deltaX = 0;
		else if(scrollX > 0 && deltaX > 112*options.zoom()-getWidth()) deltaX = 112*options.zoom()-getWidth();
		else{
			deltaX += scrollX;
			updateBgBuffer = true;
		}
		if(scrollY < 0 && deltaY < -scrollY) deltaY = 0;
		else if(scrollY > 0 && deltaY > 112*options.zoom()-getHeight()) deltaY = 112*options.zoom()-getHeight();
		else{
			deltaY += scrollY;
			updateBgBuffer = true;
		}
		game.whoseTurn().setView(new Square(deltaX/options.zoom(), deltaY/options.zoom()));
	}
	
	public void paint(Graphics g)
	{
		scroll();
		
		if(deltaX < 0) deltaX = 0;
		if(deltaY < 0) deltaY = 0;
		if(deltaX > 112*options.zoom() - getWidth()) deltaX = 112*options.zoom() - getWidth();
		if(deltaY > 112*options.zoom() - getHeight()) deltaY = 112*options.zoom() - getHeight();
		
		if(bgBuffer == null | resizeBgBuffer)
		{
			bgBuffer = new BufferedImage(getWidth()+options.zoom(), getHeight()+options.zoom(), BufferedImage.TYPE_INT_RGB);
			updateBgBuffer = true;
		}
		
		if(updateBgBuffer)
		{
			int sx = deltaX/options.zoom();
			int sy = deltaY/options.zoom();
			
			for(int y=0; y<=getHeight()/options.zoom()+1; y++)
				for(int x=0; x<=getWidth()/options.zoom()+1; x++)
					bgBuffer.getGraphics().drawImage(map.getTile(sx+x, sy+y), x*64-deltaX%64, y*64-deltaY%64, null);
			
			volBgBuffer = null;
			updateBgBuffer = false;
		}

		volBgBuffer = AcceleratedGraphics.drawVolatileImage((Graphics2D) g, volBgBuffer, 0, 0, getWidth(), getHeight(), 0, 0, bgBuffer);
		
		/* Draw grid */
		if(options.grid())
		{
			Grid.draw(g, 0, 0, getWidth(), getHeight(),
					options.zoom(), deltaX % options.zoom(), deltaY % options.zoom());
		}
		
		/* Draw units */
		for(int i=0; i<units.size(); i++)
		if(scanner_mask[units.get(i).model().getPosition().x][units.get(i).model().getPosition().y])
			units.get(i).draw(g);
			
		/* Draw cursor */
		if(selectedUnit != null) if(selectedUnit.x >= deltaX/options.zoom() 
				&& selectedUnit.x <= (deltaX+getWidth())/options.zoom()
				&& selectedUnit.y >= deltaY/options.zoom()
				&& selectedUnit.y <= (deltaY+getWidth())/options.zoom())
			graphics.Cursor.draw(g, selectedUnit.x*options.zoom() - deltaX,
					selectedUnit.y*options.zoom() - deltaY, options.zoom());
		
		/* Draw explosion */
		if(somethingExploding )
		{
			g.drawImage(ImageLibrary.getHit(),
					explosionZone.x * options.zoom() - deltaX, 
					explosionZone.y * options.zoom() - deltaY,
					explosionZone.x * options.zoom() + 64 - deltaX,
					explosionZone.y * options.zoom() + 64 - deltaY,
					64 * (explosionIndex/3), 0, 64 * ((explosionIndex/3)+1), 64, null);
			explosionIndex++;
			if(explosionIndex/3 >= 5)
				somethingExploding = false;
		}
		
		/* Draw explosion */
		if(somethingExploding2)
		{
			g.drawImage(ImageLibrary.getExplosion(),
					explosionZone2.x * options.zoom() - options.zoom()/2 - deltaX, 
					explosionZone2.y * options.zoom() - options.zoom()/2 - deltaY,
					explosionZone2.x * options.zoom() - options.zoom()/2 + 114 - deltaX,
					explosionZone2.y * options.zoom() - options.zoom()/2 + 108 - deltaY,
					114 * (explosionIndex2/3), 0, 114 * ((explosionIndex2/3)+1), 108, null);
			if(explosionIndex == 9)
			{
				units.remove(model.getUnitAt(explosionZone).getClient());
				model.setUnitAt(explosionZone, null);
			}
			explosionIndex2++;
			if(explosionIndex2/3 >= 14)
				somethingExploding2 = false;
		}
		
		/* Draw Range */
		if(options.range() && selectedUnit != null)
			if(model.getUnitAt(selectedUnit) != null)
				if(model.getUnitAt(selectedUnit).isAttackUnit())
		{
			g.setColor(Color.red);
			int r = ((AttackUnit)model.getUnitAt(selectedUnit)).getRange() * options.zoom();
			g.drawOval(selectedUnit.x * options.zoom() - (r-options.zoom())/2 - r/2 - deltaX,
					selectedUnit.y * options.zoom() - (r-options.zoom())/2 - r/2 - deltaY, 2*r, 2*r);
		}
		
		/* Draw Range */
		if(options.scanner() && selectedUnit != null)
			if(model.getUnitAt(selectedUnit) != null)
		{
			g.setColor(Color.yellow);
			int r = model.getUnitAt(selectedUnit).getScanner() * options.zoom();
			g.drawOval(selectedUnit.x * options.zoom() - (r-options.zoom())/2 - r/2 - deltaX,
					selectedUnit.y * options.zoom() - (r-options.zoom())/2 - r/2 - deltaY, 2*r, 2*r);
		}
	}

	protected void calculateScannerMask()
	{
		for(int i=0; i<100; i++)
			for(int j=0; j<100; j++)
				scanner_mask[i][j] = false;
		
		for(int i=0; i<units.size(); i++)
			if(units.get(i).model().getOwner().equals(game.whoseTurn()))
			{
				addVisibility(units.get(i).model().getPosition(), units.get(i).model().getScanner());
			}
	}
	
	/**
	 * Adds a zone to the visible zone.
	 * @param center The center of the disk containing the added zone.
	 * @param scanner The range of the scanner 
	 */
	public void addVisibility(Square center, int scanner)
	{
		int x=center.x;
		int y=center.y;
		
		for(int i=x-scanner; i<x+scanner+1; i++)
			for(int j=y-scanner; j<y+scanner+1; j++)
				if(i>=0 && j>=0 && i<100 && j<100)
					if(Math.sqrt((x-i)*(x-i)+(y-j)*(y-j)) <= scanner)
					scanner_mask[i][j] = true;
		
	}
	
	/**
	 * @return The associated options of the panel.
	 */
	public Options getOptions() {
		return options;
	}

	/**
	 * @param options The options to associate to the panel.
	 */
	public void setOptions(Options options) {
		this.options = options;
	}
	
	/**
	 * Used when the mouse moves on the view.
	 * @param x X Coordinate of the mouse
	 * @param y Y Coordinate of the mouse
	 */
	public void notifyMove(int x, int y) {
		int delta = 50;
		int diff = delta;
		int dx=0, dy=0;
		
		if(x < delta){
			dx=-1;
			diff = x;
		}
		if(x > getWidth() - delta){
			dx=1;
			diff = getWidth() - x;
		}
		if(y < delta){
			dy=-1;
			diff = Math.min(diff, y);
		}
		if(y > getHeight() - delta){
			dy=1;
			diff = Math.min(diff, getHeight() - y);
		}
		
		if(dx != 0 || dy !=0)
		{
			curCursor = ImageLibrary.getCursorScroll(Orientation.fromDxDy(dx, dy));
			setCursor(curCursor);
			
			if(diff <= delta/4)
				setScroll(4*dx, 4*dy);
			else if(diff <= delta/2)
				setScroll(2*dx, 2*dy);
			else 
				setScroll(dx, dy);
			
			return;
		}
		
		setScroll(0, 0);
		
		Square loc = new Square((x+deltaX)/options.zoom(), 
				(y+deltaY)/options.zoom());
		
		if(panel == null) panel = game.getPanel();
		
		panel.getCoordinatesPanel().setCoordinates(loc);
		
		if(model.getUnitAt(loc) != null)
		 if(isPlayerUnit(loc))
			{
				curCursor = ImageLibrary.getCursorSelect();
				setCursor(ImageLibrary.getCursorSelect());
				return;
			}
		
		if(selectedUnit != null) 
		 if(isPlayerUnit(selectedUnit))
		  if(model.getUnitAt(loc) != null)
		   if(!isPlayerUnit(loc))
			{
			  curCursor = ImageLibrary.getCursorShot();
			  setCursor(ImageLibrary.getCursorShot());
			  return;
			}
		
		if(selectedUnit != null)
		 if(isPlayerUnit(selectedUnit))
		  if(model.getUnitAt(loc) == null)
		  {
			  curCursor = ImageLibrary.getCursorMove();
			  setCursor(ImageLibrary.getCursorMove());
			  return;
		  }
		
		curCursor = ImageLibrary.getCursorNot();
		setCursor(ImageLibrary.getCursorNot());
	}

	private void setScroll(int dx, int dy) {
		scrollX = 5 * dx;
		scrollY = 5 * dy;
	}

	private void leftclick(int x, int y)
	{	
		Square loc = new Square((x+deltaX)/options.zoom(), 
				(y+deltaY)/options.zoom());
		
		if(model.getUnitAt(loc) != null)
		{
			if(isPlayerUnit(loc))
			{
				game.getPanel().setUnitInfo(model.getUnitAt(loc));
				selectedUnit = loc;
			}
			else if(isPlayerUnit(selectedUnit))
			{
				((AttackUnit)model.getUnitAt(selectedUnit)).shot(loc);
			}
		}else{
			if(selectedUnit == null) return;
			if(!isPlayerUnit(selectedUnit))
				return;
			try{
				model.getUnitAt(selectedUnit).move(loc);
			}catch(Exception e){
				System.out.println(e);
			}
		}
	}

	private void rightclick(int x, int y)
	{
		Square loc = new Square((x+deltaX)/options.zoom(), 
				(y+deltaY)/options.zoom());
		
		if(model.getUnitAt(loc) != null)
		{
			game.getPanel().setUnitInfo(model.getUnitAt(loc));
			selectedUnit = loc;
		}
	}
	
	private boolean isPlayerUnit(Square loc) {
		if(model.getUnitAt(loc) == null) return false;
		return game.whoseTurn().equals(model.getUnitAt(loc).getOwner());
	}

	/**
	 * Initializes graphics acceleration.
	 */
	public void initGraphics() {
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
	}
	
	/**
	 * Runs a paint-loop. Not used.
	 */
	public void loop() {
		while(true)
		{
			paint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return The X coordinate of the top-left corner of the viewport.
	 */
	public int getDeltaX() {
		return deltaX;
	}
	
	
	/**
	 * @return The Y coordinate of the top-left corner of the viewport.
	 */
	public int getDeltaY() {
		return deltaY;
	}

	public void endTurn() {
		calculateScannerMask();
		Square s = game.whoseTurn().getView();
		setDelta(options.zoom()*s.x, options.zoom()*s.y);
	}

	/**
	 * @param map Sets the map associated with the view.
	 */
	public void setMap(Map map) {
		this.map = map;
	}
	
	/**
	 * @return The range of Squares that are visible.
	 */
	public ViewRange getViewRange()
	{
		return new ViewRange(new Square(deltaX/options.zoom(), deltaY/options.zoom()),
							new Square((deltaX+getWidth())/options.zoom(), (deltaY+getHeight())/options.zoom()));
	}

	/**
	 * Sets the coordinates of the top-left corner of the viewport.
	 * @param i X coordinate 
	 * @param j Y coordinate
	 */
	public void setDelta(int i, int j) {
		deltaX = i;
		deltaY = j;
		updateBgBuffer = true;
	}
	
	/**
	 * @return The mask of the visible and non-visible zones.
	 */
	public boolean[][] getScannerMask()
	{
		return scanner_mask;
	}
}
