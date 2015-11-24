package sistemidistribuiti.uno.workflow;

import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.game.Game;

/**
 * Data receiver class which manage the callback when RMI methods are sent to 
 * a class
 * @author christian
 *
 */
public class DataReceiver implements DataReceiverListener{
	private Game game;
	
	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

}
