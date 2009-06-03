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
	
	public Options()
	{
		_grid = true;
		_zoom = 64;
		_range = true;
		_scanner = true;
		_color = true;
	}
	
	public boolean grid(){
		return _grid;
	}
	
	public int zoom(){
		return _zoom;
	}
	
	public boolean range(){
		return _range;
	}

	public boolean scanner() {
		return _scanner;
	}
	
	public boolean color() {
		return _color;
	}
}
