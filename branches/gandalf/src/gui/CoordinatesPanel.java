package gui;

import gamelogic.Square;

import javax.swing.JLabel;

public class CoordinatesPanel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	public void setCoordinates(Square s)
	{
		this.setText(s.toString());
	}
}
