package gui;

import gamelogic.Game;
import gamelogic.GameListener;
import gamelogic.units.Unit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author kbok
 * Provides a panel in which to display information about the game and the selected
 * unit, and to enable the player to select options about the board and to make 
 * game-wide actions.
 */
public class Panel extends JPanel implements GameListener{
	private static final long serialVersionUID = 1L;
	protected JButton endTurn;
	protected OptionsPanel optionsPanel;
	protected JLabel whoseTurn;
	protected UnitInfoPanel infopanel;
	protected MiniMap minimap;
	protected Game game;
	protected ButtonListener buttonListener;
	protected CoordinatesPanel coordsPanel;
	
	protected class ButtonListener implements ActionListener {
		protected Panel parent;
		
		public ButtonListener(Panel parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("endTurn"))
				parent.nextTurn();
		}
	}
	
	/**
	 * Creates a new Panel associated with the given Game.
	 * @param g the Game to associate to.
	 */
	public Panel(Game g)
	{
		buttonListener = new ButtonListener(this);
		
		endTurn = new JButton("End Turn");
		endTurn.setActionCommand("endTurn");
		endTurn.addActionListener(buttonListener);
		
		whoseTurn = new JLabel("Ready");
		infopanel = new UnitInfoPanel(null);
		optionsPanel = new OptionsPanel();
		minimap = new MiniMap(g.getBoard(), g.getMinimap());
		coordsPanel = new CoordinatesPanel();
		
		add(endTurn);
		add(whoseTurn);
		add(infopanel);
		add(minimap);
		add(coordsPanel);
		add(optionsPanel);
		
		setPreferredSize(new Dimension(200, 200));
		
		game = g;
		game.addClient(this);
		
		updateTurnInfo();
		
		setBackground(Color.black);
	}

	protected void nextTurn() {
		game.endTurn();
	}

	public void endTurn() {
		updateTurnInfo();
	}

	private void updateTurnInfo() {
		String mesg = new String(game.whoseTurn().getName());
		mesg = mesg.concat(" is playing.");
		whoseTurn.setText(mesg);
		whoseTurn.setForeground(game.whoseTurn().getColor());
	}
	
	/**
	 * Updates the unit info panel using the given Unit as the data source.
	 * @param u The Unit providing the data.
	 */
	public void setUnitInfo(Unit u)
	{
		infopanel.setUnit(u);
	}

	/**
	 * Updates the minimap.
	 */
	public void flushInfo() {
		infopanel.flush();
	}

	public void setBoardView(BoardView boardview) {
		minimap.setBoardView(boardview);
	}
	
	public CoordinatesPanel getCoordinatesPanel()
	{
		return coordsPanel;
	}

	public void setOptions(Options options) {
		optionsPanel.setOptions(options);
	}
}
