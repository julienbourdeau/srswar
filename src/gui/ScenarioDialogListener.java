package gui;

import files.Scenario;

/**
 * @author kbok
 * Provides a callback for the ScenarioDialog to notify when the used had chosen the
 * Scenario to play.
 */
public interface ScenarioDialogListener {
	/**
	 * Fired when a scenario is chosen by the user.
	 * @param s The Scenario object representing the one chosen by the user.
	 */
	public void scenarioChosen(Scenario s);
}
