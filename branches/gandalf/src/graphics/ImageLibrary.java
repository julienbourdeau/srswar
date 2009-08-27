package graphics;

import files.GraphicsFile;
import files.IndexedImage;
import files.Path;
import gamelogic.units.Unit;
import gamelogic.units.UnitIdentifier;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;

/**
 * @author kbok
 * Provides a library of Images used for graphics that is independent of the format of
 * graphics data, implementation and loading state.
 */
public class ImageLibrary {
	protected static ImageLibrary singleton;
	
	protected GraphicsFile gFile;
	
	protected Image[] tanks;
	protected Image[] scouts;
	protected Image[] assaults;
	protected Image[][] units;
	protected Image bg;
	protected Image hit;
	protected Image explo;
	protected Image[] scroll_img;
	protected Image small_numbers;
	protected Image small_icons;
	protected Image small_labels;
	
	protected Toolkit toolkit;
	protected Cursor move;
	protected Cursor shot;
	protected Cursor select;
	protected Cursor not;
	protected Cursor[] scrollers;
	
	/**
	 * Constructs a new ImageLibrary and loads mandatory graphics.
	 * @throws FileNotFoundException 
	 */
	protected ImageLibrary(){
		try {
			gFile = new GraphicsFile(new RandomAccessFile(Path.getPath().concat("MAX.RES"), "r"));
			gFile.readIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		units = new Image[UnitIdentifier.UID_MAX][];
		tanks = new Image[8];
		scouts = new Image[8];
		assaults = new Image[8];
		scroll_img = new Image[8];
		
		scrollers = new Cursor[8];
		
		
		Image img_move, img_shot, img_select, img_not;
		
		try{
			hit = ImageIO.read(new File(Path.getPath().concat("fx/hit.png")));
			explo = ImageIO.read(new File(Path.getPath().concat("fx/explo_small.png")));
			
			small_numbers = ImageIO.read(new File(Path.getPath().concat("graphics/small_numbers.png")));
			small_icons = ImageIO.read(new File(Path.getPath().concat("graphics/small_icons.png")));
			small_labels = ImageIO.read(new File(Path.getPath().concat("graphics/small_labels.png")));
			
			img_move = ImageIO.read(new File(Path.getPath().concat("gfx/move.png")));
			img_shot = ImageIO.read(new File(Path.getPath().concat("gfx/attack.png")));
			img_select = ImageIO.read(new File(Path.getPath().concat("gfx/select.png")));
			img_not = ImageIO.read(new File(Path.getPath().concat("gfx/not.png")));
			for(int i=0; i<8; i++)
				scroll_img[i] = ImageIO.read(new File(new String(Path.getPath().concat("gfx/pf_")
					.concat(String.valueOf(i)).concat(".png"))));
				
			move = Toolkit.getDefaultToolkit().createCustomCursor(img_move, new Point(0, 0), "");
			shot = Toolkit.getDefaultToolkit().createCustomCursor(img_shot, new Point(0, 0), "");
			select = Toolkit.getDefaultToolkit().createCustomCursor(img_select, new Point(0, 0), "");
			not = Toolkit.getDefaultToolkit().createCustomCursor(img_not, new Point(0, 0), "");
			for(int i=0; i<8; i++)
				scrollers[i] = Toolkit.getDefaultToolkit().createCustomCursor(scroll_img[i], new Point(0, 0), "");
			
			// useless bg.setAccelerationPriority(1);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadImageByIdent(Image[] array, String ident, Orientation o)
	{
		IndexedImage[] imgs;
		try {
			imgs = gFile.getMultiImage(ident);
			array[o.toInt()] = imgs[o.toInt()].withPalette(IndexedImage.DEFAULT_PALETTE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the graphical representation of a unit, given a Unit object and an
	 * Orientation.
	 * @param u The Unit object to represent.
	 * @param o The current orientation of this unit.
	 * @return an Image that can be used to draw the unit.
	 */
	public static Image getImage(Unit u, Orientation o){
		if(singleton == null)
			singleton = new ImageLibrary();
		
		if(singleton.units[u.getIdent().getUid()] == null)
			singleton.units[u.getIdent().getUid()] = new Image[8];
		
		if(singleton.units[u.getIdent().getUid()][o.toInt()] == null)
			singleton.loadImageByIdent(singleton.units[u.getIdent().getUid()], u.getIdent().getFile(), o);
		
		return singleton.units[u.getIdent().getUid()][o.toInt()];
	}
	
	private static void checkSingleton()
	{
		if(singleton == null)
			singleton = new ImageLibrary();
	}
	
	/**
	 * Returns the background to be used if no map is available.
	 * @return The background of the BoardView.
	 */
	@Deprecated 
	public static Image getBackground(){
		checkSingleton();
		return singleton.bg;
	}

	/**
	 * @return The "shot" cursor
	 */
	public static Image getHit() {
		checkSingleton();
		return singleton.hit;
	}
		
	/**
	 * @return The "Move" cursor
	 */
	public static Cursor getCursorMove()
	{
		checkSingleton();
		return singleton.move;
	}
	
	/**
	 * @return The "Select" cursor
	 */
	public static Cursor getCursorSelect()
	{
		checkSingleton();
		return singleton.select;
	}
	
	/**
	 * @return The "Forbidden" cursor
	 */
	public static Cursor getCursorNot()
	{
		checkSingleton();
		return singleton.not;
	}
	
	/**
	 * @return The "Shot" cursor
	 */
	public static Cursor getCursorShot()
	{
		checkSingleton();
		return singleton.shot;
	}
	
	/**
	 * Returns the "scroll" cursors corresponding to the given orientation
	 * @param o The orientation of the cursor
	 * @return The Cursor object
	 */
	public static Cursor getCursorScroll(Orientation o)
	{
		checkSingleton();
		return singleton.scrollers[o.toInt()];
	}

	/**
	 * @return The image of the explosion
	 */
	public static Image getExplosion() {
		checkSingleton();
		return singleton.explo;
	}
	
	/**
	 * @return The small icons used in information panel
	 */
	public static Image getSmallIcons() {
		checkSingleton();
		return singleton.small_icons;
	}
	
	/**
	 * @return The small numbers used in information panel
	 */
	public static Image getSmallNumbers() {
		checkSingleton();
		return singleton.small_numbers;
	}
	
	/**
	 * @return The small labels used in information panel
	 */
	public static Image getSmallLabels() {
		checkSingleton();
		return singleton.small_labels;
	}
}
