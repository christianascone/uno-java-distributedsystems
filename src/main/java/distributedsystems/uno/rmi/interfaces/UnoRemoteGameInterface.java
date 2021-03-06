package distributedsystems.uno.rmi.interfaces;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import distributedsystems.uno.model.game.Game;

/**
 * Remote interface with game methods {@link RemoteException} is very important
 * for RMI
 * 
 * @author Christian Ascone
 *
 */
public interface UnoRemoteGameInterface extends Remote {

	/**
	 * Setup game to server
	 * 
	 * @param deck
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	void setupGame(Game game) throws RemoteException, NotBoundException;

	/**
	 * Send the current game to other players
	 * 
	 * @param game
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	void sendGame(Game game) throws RemoteException, NotBoundException;

}
