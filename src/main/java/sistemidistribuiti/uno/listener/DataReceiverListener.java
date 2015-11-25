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
	 * Setup remote client with given game
	 * 
	 * @param game
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	void setupRemoteClient(Game game) throws RemoteException, NotBoundException;
	
	/**
	 * Set the Game received from client
	 * @param game
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	void setGame(Game game) throws RemoteException, NotBoundException;
}
