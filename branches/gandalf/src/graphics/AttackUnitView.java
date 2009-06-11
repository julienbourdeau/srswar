package graphics;

import gamelogic.units.Unit;
import gui.BoardView;
import gui.Options;

/**
 * Draft class which will draw attack units in the future.
 * @author kbok
 */
public class AttackUnitView extends UnitView{

	/**
	 * Creates a new attack unit's view on the given BoardView and options for the given unit.
	 * @param m The Unit associated with the view.
	 * @param board The View of the board the unit belongs to.
	 * @param o The options of the BoardView.
	 */
	public AttackUnitView(Unit m, BoardView board, Options o) {
		super(m, board, o);
		// TODO Auto-generated constructor stub
	}

}
