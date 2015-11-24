package sistemidistribuiti.uno.listener;

import sistemidistribuiti.uno.model.game.Game;

/**
 * Listener used for the RMI callback
 * 
 * @author christian
 *
 */
public interface DataReceiverListener {
	/**
	 * Set the Game received from client
	 * @param game
	 */
	void setGame(Game game);
}
