package sistemidistribuiti.uno.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sistemidistribuiti.uno.model.game.Game;

/**
 * Remote interface with game methods
 * 
 * @author christian
 *
 */
public interface UnoRemoteGameInterface extends Remote{
	// IMPORTANTISSIMA LA REMOTE EXCEPTION
	
	/**
	 * Send game to server
	 * @param deck
	 * @throws RemoteException
	 */
	void sendGame(Game game) throws RemoteException;
}
