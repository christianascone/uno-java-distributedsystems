package sistemidistribuiti.uno.listener;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	void setGame(Game game) throws RemoteException, NotBoundException;
}
