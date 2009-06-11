package gamelogic;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import files.Map;
import files.Scenario;
import gui.BoardView;
import gui.MainWindow;
import gui.Options;
import gui.Panel;

/**
 * @author julien
 * Represents a game.
 * FIXME the Game model should not depend of the GUI view.
 * Maybe we should create interfaces or so.
 */
public class Game {
	protected MainWindow parent;
	protected Board board;
	protected Panel panel;
	protected int turn;
	protected int curPlayer;
	protected Vector<GameListener> clients;
	private Map map;
	protected Options options;
	
	/**
	 * Build a new game, initialize parent (window), client, board, option.
	 * @param p The main window
	 */
	public Game(MainWindow p)
	{
		parent = p;
		clients = new Vector<GameListener>();
		board = new Board();
		options = new Options();
	}
	
	/**
	 * Launch the game.  
	 */
	public void start()
	{
		turn = 0;
		curPlayer = 0;
		
		BoardView boardview = new BoardView(board, options, this);
		boardview.setMap(map);
		panel = new Panel(this);
		
		boardview.setBounds(0, 0, 800, 600);
		panel.setBoardView(boardview);
		panel.setOptions(options);
		addClient(boardview);
		parent.getContentPane().setLayout(new BorderLayout());
		parent.getContentPane().add(boardview, BorderLayout.CENTER);
		parent.getContentPane().add(panel, BorderLayout.WEST);
		parent.pack();
		parent.setVisible(true);
		
		boardview.initGraphics();
		//boardview.loop();
		boardview.startTimer();
	}
	
	public void endTurn()
	{
		do{
			curPlayer++;
			if(curPlayer > Player.count()-1)
				endTurnAllPlayers();
		}
		while(!Player.getList().get(curPlayer).isEnabled());
				
		checkVictory();
		
		notifyEndTurn();
	}
	
	private void victory(String winner)
	{
		JOptionPane.showMessageDialog(null, winner.concat(" wins the game !"));
		parent.setVisible(false);
		System.exit(0);
	}
	
	private void checkVictory()
	{
		int playersCount = 0;
		
		for(int i=0; i<Player.count(); i++)
			if(Player.getList().get(i).isEnabled())
				playersCount++;
		
		if(playersCount==1)
		{
			for(int i=0; i<Player.count(); i++)
				if(Player.getList().get(i).isEnabled())
					victory(Player.getList().get(i).getName());
		}
	}

	public void endTurnAllPlayers()
	{
		curPlayer = 0;
		board.newTurn();
		panel.flushInfo();
		turn++;
	}
	
	public Player whoseTurn()
	{
		return Player.getList().get(curPlayer);
	}
	
	public void addClient(GameListener newComer)
	{
		clients.add(newComer);
	}
	
	protected void notifyEndTurn()
	{
		for(int i=0; i<clients.size(); i++)
		{
			clients.get(i).endTurn();
		}
	}
	
	public Panel getPanel()
	{
		return panel;
	}

	public Board getBoard() {
		return board;
	}
	
	public void loadScenario(Scenario s)
	{
		s.loadBoard(board);
		try {
			map = new Map(s.getWorld());
			map.readFully();
			board.setMap(map);
			} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public Image getMinimap() {
		try {
			return map.getMiniMap();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
