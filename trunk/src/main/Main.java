package main;

import java.io.IOException;

import files.Path;
import files.Scenario;
import gamelogic.Game;
import gui.MainWindow;
import gui.ScenarioDialog;
import gui.ScenarioDialogListener;

public class Main implements ScenarioDialogListener {
	public static void main(String[] args) throws IOException
	{
		new Main().run();
	}
	
	private void run() throws IOException
	{
		ScenarioDialog d = new ScenarioDialog(Path.getPath().concat("scenarios/"));
		d.addClient(this);
	}

	public void scenarioChosen(Scenario s) {
		System.out.println("Called scenarioChosen");
		
		MainWindow wnd = new MainWindow(800, 600);
		
		Game g = new Game(wnd);
		g.loadScenario(s);
		g.start();
		
		wnd.setVisible(true);
		wnd.setDefaultCloseOperation(MainWindow.EXIT_ON_CLOSE);
	}
}
