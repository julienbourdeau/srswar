package gui;

/**
 * @author kbok
 * Provides a containers for all options of the GUI.
 */
public class Options {
	protected boolean _grid;
	protected int _zoom;
	protected boolean _range;
	protected boolean _scanner;
	protected boolean _color;
	
	/**
	 * Creates a new Options object.
	 */
	public Options()
	{
		_grid = true;
		_zoom = 64;
		_range = true;
		_scanner = true;
		_color = true;
	}
	
	/**
	 * @return whether the grid is displayed or not.
	 */
	public boolean grid(){
		return _grid;
	}
	
	/**
	 * @return the zoom of the BoardView.
	 */
	public int zoom(){
		return _zoom;
	}
	
	/**
	 * @return whether the range is displayed or not.
	 */
	public boolean range(){
		return _range;
	}

	/**
	 * @return whether the scanner is displayed or not.
	 */
	public boolean scanner() {
		return _scanner;
	}
	
	/**
	 * @return whether the color is displayed or not.
	 */
	public boolean color() {
		return _color;
	}
}
