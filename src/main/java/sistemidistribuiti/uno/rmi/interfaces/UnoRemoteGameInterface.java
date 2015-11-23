package sistemidistribuiti.uno.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sistemidistribuiti.uno.model.card.impl.Deck;

/**
 * Remote interface with game methods
 * 
 * @author christian
 *
 */
public interface UnoRemoteGameInterface extends Remote{
	// IMPORTANTISSIMA LA REMOTE EXCEPTION
	
	/**
	 * Send deck to server
	 * @param deck
	 * @throws RemoteException
	 */
	void sendDeck(Deck deck) throws RemoteException;
}
