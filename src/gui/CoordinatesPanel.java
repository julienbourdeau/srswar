package gui;

import gamelogic.Square;

import javax.swing.JLabel;

/**
 * Provides a view of the coordinates of the square under the cursor.
 * @author kbok
 */
public class CoordinatesPanel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sets the coordinates displayed by the panel.
	 * @param s Square under the cursor.
	 */
	public void setCoordinates(Square s)
	{
		this.setText(s.toString());
	}
}
